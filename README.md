# Enterprise Java Microservices + React 18 Exercise

Two Spring Boot microservices backed by SQL Server and Kafka, with a React 18 +
Redux Toolkit frontend.

## Architecture

```
Browser (React 18 + Redux Toolkit, :3000)
        │
        │  one origin, bearer token on every call
        ▼
   API gateway (:8080) ──── enforces the token ────┐
        │  routes by service name (lb://)          │ validates via
        │                                          ▼ /api/auth/me
        ├──────────► product-service (:8081) ──► SQL Server (productdb)
        │                    ▲                     
        │                    │ WebClient           
        └──────────► cart-service (:8082) ──────► SQL Server (cartdb)
                             │
                             ▼
                Kafka topics: cart-events, order-events
                             │
                             ▼
              product-service consumers (log events)

   all four services register with:
   Eureka discovery server (:8761)
```

### api-gateway (port 8080)
The single entry point. Routes to the services by name through Eureka
(`lb://product-service`, `lb://cart-service`), owns CORS, and rejects
unauthenticated API calls before they reach a service (see Design notes).

### discovery-server (port 8761)
Eureka registry. Every service registers here, so the gateway resolves service
names to live instances instead of hardcoded ports, with client-side load
balancing across instances.

### product-service (port 8081)
Product CRUD, pagination/sorting, native queries, stock reduction, and the
Kafka **consumers** (cart events and order events).

### cart-service (port 8082)
Cart operations, mock checkout / order history, login/token issuing, calls
product-service over **WebClient**, and the Kafka **producers**. `RestTemplate`
is not used anywhere in this project.

## Layout

```
PSP/
├── backend/
│   ├── discovery-server/   Eureka registry (:8761)
│   ├── api-gateway/        single entry point (:8080), routing + auth
│   ├── product-service/    controller → service → repository → entity
│   └── cart-service/       controller → service → repository → entity
├── frontend/               app/ features/ pages/ components/ services/ hooks/ routes/ utils/
├── docker-compose.yml      SQL Server + Kafka
├── scripts/run-backend.sh  build + boot all four services in order
└── scripts/test-backend.sh run all backend tests (no Docker)
```

## Prerequisites

- Java 17
- Node 18+
- Docker

## Running it

### 1. Infrastructure

```bash
docker compose up -d
```

This starts SQL Server on `localhost:1433` and Kafka on `localhost:9092`.

> **Apple Silicon note:** the image is `mcr.microsoft.com/azure-sql-edge`, the
> ARM64-native build of the SQL Server engine. It speaks the same TDS protocol,
> so the `mssql-jdbc` driver and `SQLServerDialect` are unchanged. The amd64
> `mssql/server:2022` image segfaults (exit 139) under Rosetta emulation on
> M-series Macs. On an x86 host the image can be swapped back with no code changes.

Create the two databases (first run only) with any SQL client pointed at
`localhost:1433` (user `sa`, password `Str0ng!Passw0rd`):

```sql
IF DB_ID('productdb') IS NULL CREATE DATABASE productdb;
IF DB_ID('cartdb')    IS NULL CREATE DATABASE cartdb;
```

Hibernate creates the tables on startup.

### 2. Backend

```bash
./scripts/run-backend.sh
```

Starts all four services in dependency order: discovery-server (:8761) first,
then product-service and cart-service, then api-gateway once both have
registered. The Eureka dashboard is at http://localhost:8761.

Starting individually requires the same order — the gateway needs the services
in the registry before its `lb://` routes resolve:

```bash
cd backend/discovery-server && ./mvnw spring-boot:run   # wait for :8761
cd backend/product-service  && ./mvnw spring-boot:run
cd backend/cart-service     && ./mvnw spring-boot:run
cd backend/api-gateway      && ./mvnw spring-boot:run   # after services register
```

### 3. Frontend

```bash
cd frontend
npm install
npm run dev        # http://localhost:3000
```

The frontend talks only to the gateway (`http://localhost:8080`); it never
addresses a service directly. Sign in with **`root` / `root1234`** (configurable
via `app.auth.username` / `app.auth.password`, or the `APP_AUTH_USERNAME` /
`APP_AUTH_PASSWORD` env vars).

## API

All endpoints are reached through the gateway at **`http://localhost:8080`**.
Every call except `/api/auth/**` requires an `Authorization: Bearer <token>`
header; the gateway returns 401 otherwise. The paths below are unchanged whether
called through the gateway or (in development) directly on a service port.

### product-service

| Method | Path | Purpose |
|---|---|---|
| POST | `/api/products` | Create |
| GET | `/api/products` | List all |
| GET | `/api/products/{id}` | Fetch one |
| PUT | `/api/products/{id}` | Update |
| DELETE | `/api/products/{id}` | Delete |
| GET | `/api/products/paged?page=&size=&sortBy=&direction=` | Pagination + sorting |
| GET | `/api/products/in-stock` | Java Streams filter |
| GET | `/api/products/inventory-value` | Java Streams transform |
| GET | `/api/products/above-price?minPrice=` | Native SQL query |
| GET | `/api/products/low-stock?threshold=&limit=` | Native query (SQL Server `TOP`) |
| GET | `/api/products/{id}/stock-check?quantity=` | Stock validation |
| POST | `/api/products/{id}/reduce-stock?quantity=` | Decrement stock at checkout |

### cart-service

| Method | Path | Purpose |
|---|---|---|
| POST | `/api/cart/items` | Add to cart (validates via WebClient, publishes to Kafka) |
| GET | `/api/cart/{userId}` | Fetch cart (empty cart returns 200, not 404) |
| PUT | `/api/cart/{userId}/items/{itemId}` | Set line quantity (re-validates stock) |
| DELETE | `/api/cart/{userId}/items/{itemId}` | Remove a line |
| POST | `/api/orders/checkout` | Mock payment — place an order |
| GET | `/api/orders/{userId}` | Order history |
| POST | `/api/auth/login` | Sign in, returns a session token |
| GET | `/api/auth/me` | Validate a token |
| POST | `/api/auth/logout` | Revoke a token |

## Design notes

**Layered strictly.** Controllers delegate to services and never touch
repositories. Entities never leave the service layer — DTOs are the API contract.

**Parallel validation.** `CartService.fetchAndValidateInParallel` runs the
product fetch and the stock check concurrently with `CompletableFuture` and
joins them via `thenCombine`, so the cost is the slower call rather than the sum.
`CompletionException` is unwrapped so domain exceptions keep their HTTP status.

**Kafka.** Events are keyed by `cartId` so a cart's events stay ordered within a
partition. Publish failures are logged but do not fail the request — the cart
write has already committed. The consumer wraps `JsonDeserializer` in
`ErrorHandlingDeserializer` so a poison message cannot wedge it.

**Checkout is a mock payment.** No payment provider is called, but everything
around it is real: `OrderService.checkout` decrements stock in product-service over
WebClient, persists the order, clears the cart, and publishes to `order-events`.
Stock is reduced *before* the order is written, so if any line is short the
whole transaction rolls back and no order exists. Product name and price are
snapshotted onto the order line, so later catalogue edits never rewrite history.

Orders live in cart-service rather than a separate order-service — a third
service would mean another database, port and deployment for what is a mock
checkout.

**Auth is enforced at the gateway.** `AuthService` (in cart-service) validates
credentials from configuration and issues an in-memory token. The gateway's
`AuthenticationFilter` runs before routing and rejects any API call without a
valid token — it asks cart-service (`/api/auth/me`) to resolve the token on
every request, so revocation is immediate rather than waiting for a cache to
expire. On success it forwards an `X-Authenticated-User` header so services
receive the resolved identity. `/api/auth/**` is public, since that is how a
caller obtains a token.

Traffic through the gateway (`:8080`) — which is all the frontend uses — is
therefore protected: `curl localhost:8080/api/products` returns 401 without a
token. The service ports (`:8081`, `:8082`) remain open for local development
and are not exposed publicly; the gateway is the intended entry point. A
fully locked-down setup would also put Spring Security on each service so they
trust only the gateway (e.g. a shared header or mTLS) — that is the remaining
step beyond this exercise. Tokens are held in memory, so restarting cart-service
ends all sessions; the frontend detects this via `/api/auth/me` on boot and
signs out cleanly.

Cart and orders are scoped to the signed-in username, so signing in as a
different user yields a different cart and order history.

**Error handling.** Each service has a `@RestControllerAdvice` producing one JSON
shape: `{timestamp, status, error, message, path, fieldErrors}`. Stack traces are
logged, never returned.

**Frontend layering.**

| Layer | Responsibility |
|---|---|
| `components/`, `pages/` | UI only — no store access, no service imports |
| `hooks/` | Logic (`useAuth`, `useProducts`, `useCart`, `useOrders`, `useProductForm`) |
| `features/` | Redux slices + thunks |
| `services/` | API — the only place importing axios |

Pagination and sorting are server-side; the search/price filters refine the
loaded page client-side via `useMemo`.

## Verification

### Backend tests

```bash
./scripts/test-backend.sh     # or: cd backend/<service> && ./mvnw test
```

70 tests, ~15s, **no Docker required**. Unit tests are pure JUnit 5 + Mockito;
the context tests run against the `test` profile (H2 in SQL Server compatibility
mode, Kafka and Eureka disabled), so they validate each bean graph without live
infrastructure.

Covered: product CRUD and the not-found paths, stock reduction including the
refusal to go negative, stream filtering and inventory maths, pagination and
sort direction, cart quantity accumulation, cross-cart item access, checkout
totals and the roll-back when a line is short on stock, order-line price
snapshotting, the auth token lifecycle, and the gateway auth filter
(public-path passthrough, preflight, and missing-token rejection).

### Frontend end-to-end

These drive real Chrome and **do** need the full stack running:

```bash
cd frontend
node scripts/verify-login.mjs       # route guard, bad credentials, reload, sign-out
node scripts/verify-ui.mjs          # renders backend data, no console errors
node scripts/verify-routing.mjs     # routes, deep links, no full reloads
node scripts/verify-filters.mjs     # filtering is client-side (0 API calls)
node scripts/verify-pagination.mjs  # pages are disjoint and backend-driven
node scripts/verify-states.mjs      # spinner + error banner
node scripts/verify-cart.mjs        # add-to-cart through to Kafka
node scripts/verify-orders.mjs      # quantity, remove, pay, order history, stock
```

They require Google Chrome at the standard macOS path.

## Commit map

Part 1: `1A-setup` · `1B-entity-layer` · `1C-repository-layer` · `1D-service-layer`
· `1E-controller-crud` · `1F-paging-sorting-streams` · `1G-native-query`
· `1H-webclient` · `1I-kafka-producer` · `1J-kafka-consumer`
· `1K-completablefuture` · `1L-validation-logging`

Part 2: `2A-setup` · `2B-product-ui` · `2C-redux` · `2D-cart` · `2E-loading-error`
· `2F-search-filter` · `2G-pagination` · `2H-routing` · `2I-hooks-clean`
