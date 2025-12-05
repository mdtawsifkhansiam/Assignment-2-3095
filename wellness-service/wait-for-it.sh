#!/usr/bin/env bash
#   Use this script to test if a given TCP host/port are available

WAITFORIT_TIMEOUT=15
WAITFORIT_HOST=""
WAITFORIT_PORT=""

while [[ $# -gt 0 ]]
do
    case "$1" in
        --timeout=*)
        WAITFORIT_TIMEOUT="${1#*=}"
        shift
        ;;
        --)
        shift
        break
        ;;
        *)
        if [[ -z "$WAITFORIT_HOST" ]]; then
            WAITFORIT_HOST="$1"
        elif [[ -z "$WAITFORIT_PORT" ]]; then
            WAITFORIT_PORT="$1"
        fi
        shift
        ;;
    esac
done

if [[ -z "$WAITFORIT_HOST" || -z "$WAITFORIT_PORT" ]]; then
    echo "Usage: $0 host port [--timeout=seconds] -- command args"
    exit 1
fi

echo "Waiting for $WAITFORIT_HOST:$WAITFORIT_PORT for $WAITFORIT_TIMEOUT seconds..."

for ((i=0;i<WAITFORIT_TIMEOUT;i++)); do
    nc -z "$WAITFORIT_HOST" "$WAITFORIT_PORT" >/dev/null 2>&1
    result=$?
    if [[ $result -eq 0 ]]; then
        echo "$WAITFORIT_HOST:$WAITFORIT_PORT is available"
        exec "$@"
        exit 0
    fi
    sleep 1
done

echo "Timeout occurred after waiting $WAITFORIT_TIMEOUT seconds for $WAITFORIT_HOST:$WAITFORIT_PORT"
exit 1