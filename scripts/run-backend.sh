#!/usr/bin/env bash
# Build both services, restart them, and wait until both report started.
# Usage: scripts/run-backend.sh [logdir]
set -uo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
LOGDIR="${1:-$ROOT/.run-logs}"
mkdir -p "$LOGDIR"

pkill -f "product-service-0.0.1-SNAPSHOT.jar" 2>/dev/null
pkill -f "cart-service-0.0.1-SNAPSHOT.jar" 2>/dev/null
sleep 1

for svc in product-service cart-service; do
  echo "==> building $svc"
  ( cd "$ROOT/backend/$svc" && ./mvnw -q -B clean package -DskipTests ) || {
    echo "BUILD FAILED: $svc"; exit 1;
  }
done

nohup java -jar "$ROOT/backend/product-service/target/product-service-0.0.1-SNAPSHOT.jar" > "$LOGDIR/product.log" 2>&1 &
nohup java -jar "$ROOT/backend/cart-service/target/cart-service-0.0.1-SNAPSHOT.jar"       > "$LOGDIR/cart.log"    2>&1 &

for _ in $(seq 1 40); do
  p=0; c=0
  grep -q "Started ProductServiceApplication" "$LOGDIR/product.log" 2>/dev/null && p=1
  grep -q "Started CartServiceApplication"    "$LOGDIR/cart.log"    2>/dev/null && c=1
  [ $p -eq 1 ] && [ $c -eq 1 ] && { echo "BOTH STARTED"; exit 0; }
  if grep -q "APPLICATION FAILED TO START" "$LOGDIR/product.log" "$LOGDIR/cart.log" 2>/dev/null; then
    echo "STARTUP FAILURE"
    grep -A 12 "APPLICATION FAILED TO START" "$LOGDIR/product.log" "$LOGDIR/cart.log" 2>/dev/null | head -30
    exit 1
  fi
  sleep 3
done

echo "TIMEOUT waiting for startup"
tail -20 "$LOGDIR/product.log" "$LOGDIR/cart.log"
exit 1
