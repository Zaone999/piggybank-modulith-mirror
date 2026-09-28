#!/usr/bin/env bash
# Creates a piggybank for an owner and deposits 20 euros three times with a withdrawal of 10 euros after the second deposit, through the Spring Shell CLI adapter.
# Usage: scripts/init-and-deposit.sh [owner-uuid]   (a random owner is used when none is given)
# Needs the database from infrastructure/docker-compose.yml to be running.
set -euo pipefail

cd "$(dirname "$0")/.."

OWNER="${1:-$(uuidgen | tr '[:upper:]' '[:lower:]')}"
JAR="target/piggybank-modulith-0.0.1-SNAPSHOT.jar"

if [ ! -f "$JAR" ]; then
    ./mvnw -q -DskipTests package
fi

COMMANDS="$(mktemp)"
trap 'rm -f "$COMMANDS"' EXIT

cat > "$COMMANDS" <<COMMANDS
init --owner $OWNER
deposit --owner $OWNER --amount 20
deposit --owner $OWNER --amount 20
withdraw --owner $OWNER --amount 10
deposit --owner $OWNER --amount 20
COMMANDS

echo "Piggybank owner: $OWNER"
java -jar "$JAR" "@$COMMANDS" \
    --spring.shell.interactive.enabled=false \
    --spring.main.web-application-type=none
