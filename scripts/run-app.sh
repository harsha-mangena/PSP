#!/usr/bin/env bash
# Bring up the entire app from a cold start: infra -> backend -> frontend.
#
#   1. docker compose up  (SQL Server + Kafka)
#   2. wait for SQL Server and Kafka to actually be ready (not just "container
#      running" - Azure SQL Edge holds port 1433 open well before it will
#      accept a real login)
#   3. scripts/run-backend.sh  (discovery -> product/cart -> gateway, in order)
#   4. frontend dev server
#
# Usage: scripts/run-app.sh [logdir]
set -uo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
LOGDIR="${1:-$ROOT/.run-logs}"
mkdir -p "$LOGDIR"

echo "==> starting infrastructure containers (SQL Server, Kafka)"
( cd "$ROOT" && docker compose up -d ) || { echo "docker compose up FAILED"; exit 1; }

echo "==> waiting for SQL Server to accept connections"
# The container can hold port 1433 open long before the engine inside is
# actually ready for logins, so wait for its own readiness line rather than
# just probing the port.
SQL_READY=false
for _ in $(seq 1 60); do
  # Capture first, then grep a variable - piping `docker logs` straight into
  # `grep -q` under `set -o pipefail` is broken: grep -q exits the instant it
  # finds a match, SIGPIPEs docker logs, and pipefail reports that SIGPIPE
  # (not grep's success) as the pipeline's exit status.
  SQL_LOGS="$(docker logs psp-sqlserver 2>&1)"
  if grep -q "SQL Server is now ready for client connections" <<< "$SQL_LOGS"; then
    SQL_READY=true
    break
  fi
  sleep 3
done
if [ "$SQL_READY" != "true" ]; then
  echo "    SQL Server did not report ready in time"
  docker logs --tail 30 psp-sqlserver 2>&1
  exit 1
fi
echo "    SQL Server ready"

echo "==> waiting for Kafka"
KAFKA_READY=false
for _ in $(seq 1 30); do
  if docker exec psp-kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list >/dev/null 2>&1; then
    KAFKA_READY=true
    break
  fi
  sleep 2
done
if [ "$KAFKA_READY" != "true" ]; then
  echo "    Kafka did not report ready in time"
  docker logs --tail 30 psp-kafka 2>&1
  exit 1
fi
echo "    Kafka ready"

echo "==> starting backend (discovery, product, cart, gateway)"
"$ROOT/scripts/run-backend.sh" "$LOGDIR" || exit 1

echo "==> starting frontend"
pkill -f "vite" 2>/dev/null
sleep 1
rm -rf "$ROOT/frontend/node_modules/.vite"
if [ ! -d "$ROOT/frontend/node_modules" ]; then
  echo "    installing frontend dependencies (first run)"
  ( cd "$ROOT/frontend" && npm install ) || { echo "npm install FAILED"; exit 1; }
fi
( cd "$ROOT/frontend" && nohup npm run dev > "$LOGDIR/frontend.log" 2>&1 & )
for _ in $(seq 1 30); do
  grep -q "ready in" "$LOGDIR/frontend.log" 2>/dev/null && { echo "    frontend up"; break; }
  sleep 1
done

echo
echo "ALL STARTED"
echo "  frontend  http://localhost:3000"
echo "  gateway   http://localhost:8080"
echo "  eureka    http://localhost:8761"
echo "  product   http://localhost:8081  (direct access is 401 - go through the gateway)"
echo "  cart      http://localhost:8082  (direct access is 401 - go through the gateway)"
echo "  sql server localhost:1433"
echo "  kafka      localhost:9092"
echo
echo "sign in with root / root1234"
