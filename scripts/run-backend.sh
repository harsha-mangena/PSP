#!/usr/bin/env bash
# Build and start the whole backend in dependency order:
#   discovery-server (8761) -> product-service (8081) + cart-service (8082) -> api-gateway (8080)
# Usage: scripts/run-backend.sh [logdir]
set -uo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
LOGDIR="${1:-$ROOT/.run-logs}"
mkdir -p "$LOGDIR"

SERVICES=(discovery-server product-service cart-service api-gateway)

for svc in "${SERVICES[@]}"; do
  pkill -f "${svc}-0.0.1-SNAPSHOT.jar" 2>/dev/null
done
sleep 1

for svc in "${SERVICES[@]}"; do
  echo "==> building $svc"
  ( cd "$ROOT/backend/$svc" && ./mvnw -q -B clean package -DskipTests ) || {
    echo "BUILD FAILED: $svc"; exit 1;
  }
done

start() {
  local svc=$1 marker=$2
  nohup java -jar "$ROOT/backend/$svc/target/$svc-0.0.1-SNAPSHOT.jar" \
    > "$LOGDIR/${svc}.log" 2>&1 &
  for _ in $(seq 1 40); do
    grep -q "$marker" "$LOGDIR/${svc}.log" 2>/dev/null && { echo "    $svc up"; return 0; }
    if grep -q "APPLICATION FAILED TO START" "$LOGDIR/${svc}.log" 2>/dev/null; then
      echo "    $svc FAILED"; grep -A 12 "Description:" "$LOGDIR/${svc}.log" | head -20; return 1
    fi
    sleep 2
  done
  echo "    $svc TIMEOUT"; tail -15 "$LOGDIR/${svc}.log"; return 1
}

# The registry must be accepting registrations before anything else starts.
echo "==> starting discovery-server"
start discovery-server "Started DiscoveryServerApplication" || exit 1

echo "==> starting services"
start product-service "Started ProductServiceApplication" || exit 1
start cart-service    "Started CartServiceApplication"    || exit 1

# The gateway resolves lb:// URIs, so it needs both services in the registry.
echo "==> waiting for registration"
for _ in $(seq 1 30); do
  COUNT=$(curl -s -H "Accept: application/json" http://localhost:8761/eureka/apps 2>/dev/null \
    | grep -o '"name":"[A-Z-]*"' | wc -l | tr -d ' ')
  [ "${COUNT:-0}" -ge 2 ] && { echo "    $COUNT services registered"; break; }
  sleep 2
done

echo "==> starting api-gateway"
start api-gateway "Started ApiGatewayApplication" || exit 1

echo "ALL STARTED  (gateway :8080  eureka :8761  product :8081  cart :8082)"
