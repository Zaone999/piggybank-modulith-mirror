#!/usr/bin/env bash
# Registers the birth of a family member, creates a piggybank for them and deposits 20 euros three times with a withdrawal of 10 euros after the second deposit, through the Spring Shell CLI adapter.
# Usage: scripts/init-and-deposit.sh [first-name] [last-name]   (Emma Peeters when none are given)
# Needs the database from infrastructure/docker-compose.yml to be running.
set -euo pipefail

cd "$(dirname "$0")/.."

FIRST_NAME="${1:-Emma}"
LAST_NAME="${2:-Peeters}"
OWNER="$(uuidgen | tr '[:upper:]' '[:lower:]')"
JAR="target/piggybank-modulith-0.0.1-SNAPSHOT.jar"

if [ ! -f "$JAR" ]; then
    ./mvnw -q -DskipTests package
fi

COMMANDS="$(mktemp)"
trap 'rm -f "$COMMANDS"' EXIT

cat > "$COMMANDS" <<COMMANDS
born --member $OWNER --first-name $FIRST_NAME --last-name $LAST_NAME
init --owner $OWNER
deposit --owner $OWNER --amount 20
deposit --owner $OWNER --amount 20
withdraw --owner $OWNER --amount 10
deposit --owner $OWNER --amount 20
grandchildren
piggybanks
exit
COMMANDS

echo "Piggybank owner: $FIRST_NAME $LAST_NAME ($OWNER)"
java -jar "$JAR" "@$COMMANDS" \
    --spring.shell.interactive.enabled=false \
    --spring.main.web-application-type=none
