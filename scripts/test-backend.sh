#!/usr/bin/env bash
# Run all backend tests. Needs no Docker: unit tests are pure Mockito and the
# context tests boot against H2 with Kafka listeners disabled.
set -uo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
STATUS=0

for svc in product-service cart-service api-gateway; do
  echo "==> testing $svc"
  ( cd "$ROOT/backend/$svc" && ./mvnw -q -B test ) || STATUS=1
done

if [ $STATUS -eq 0 ]; then
  echo "ALL BACKEND TESTS PASSED"
else
  echo "BACKEND TESTS FAILED"
fi
exit $STATUS
