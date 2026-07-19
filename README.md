# Enterprise Java Microservices + React 18 Exercise

Two Spring Boot microservices backed by SQL Server and Kafka, with a React 18 +
Redux Toolkit frontend.

## Architecture

```
Browser (React 18 + Redux Toolkit, :3000)
        │
        ├──────────────► product-service (:8081) ──► SQL Server (productdb)
        │                        ▲
        │                        │ WebClient
        └──────────────► cart-service (:8082) ────► SQL Server (cartdb)
                                 │
                                 ▼
                          Kafka topic: cart-events
                                 │
                                 ▼
                  product-service consumer (logs events)
```

### product-service (port 8081)
Product CRUD, pagination/sorting, native queries, and the Kafka **consumer**.

### cart-service (port 8082)
Cart operations, calls product-service over **WebClient**, and the Kafka
**producer**. `RestTemplate` is not used anywhere in this project.

## Layout

```
PSP/
├── backend/
│   ├── product-service/   controller → service → repository → entity
│   └── cart-service/      controller → service → repository → entity
├── frontend/              app/ features/ pages/ components/ services/ hooks/ routes/ utils/
├── docker-compose.yml     SQL Server + Kafka
└── scripts/run-backend.sh build + boot both services
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

Or individually:

```bash
cd backend/product-service && ./mvnw spring-boot:run
cd backend/cart-service    && ./mvnw spring-boot:run
```

### 3. Frontend

```bash
cd frontend
npm install
npm run dev        # http://localhost:3000
```

## API

### product-service (8081)

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

### cart-service (8082)

| Method | Path | Purpose |
|---|---|---|
| POST | `/api/cart/items` | Add to cart (validates via WebClient, publishes to Kafka) |
| GET | `/api/cart/{userId}` | Fetch cart (empty cart returns 200, not 404) |

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

**Error handling.** Each service has a `@RestControllerAdvice` producing one JSON
shape: `{timestamp, status, error, message, path, fieldErrors}`. Stack traces are
logged, never returned.

**Frontend layering.**

| Layer | Responsibility |
|---|---|
| `components/`, `pages/` | UI only — no store access, no service imports |
| `hooks/` | Logic (`useProducts`, `useCart`, `useProductForm`) |
| `features/` | Redux slices + thunks |
| `services/` | API — the only place importing axios |

Pagination and sorting are server-side; the search/price filters refine the
loaded page client-side via `useMemo`.

## Verification

The frontend includes headless-Chrome checks used to verify each step:

```bash
cd frontend
node scripts/verify-ui.mjs          # renders backend data, no console errors
node scripts/verify-routing.mjs     # routes, deep links, no full reloads
node scripts/verify-filters.mjs     # filtering is client-side (0 API calls)
node scripts/verify-pagination.mjs  # pages are disjoint and backend-driven
node scripts/verify-states.mjs      # spinner + error banner
node scripts/verify-cart.mjs        # add-to-cart through to Kafka
```

They require Google Chrome at the standard macOS path.

## Commit map

Part 1: `1A-setup` · `1B-entity-layer` · `1C-repository-layer` · `1D-service-layer`
· `1E-controller-crud` · `1F-paging-sorting-streams` · `1G-native-query`
· `1H-webclient` · `1I-kafka-producer` · `1J-kafka-consumer`
· `1K-completablefuture` · `1L-validation-logging`

Part 2: `2A-setup` · `2B-product-ui` · `2C-redux` · `2D-cart` · `2E-loading-error`
· `2F-search-filter` · `2G-pagination` · `2H-routing` · `2I-hooks-clean`
