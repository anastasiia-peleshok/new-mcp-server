#!/bin/sh
set -e

host="$1"
shift
until mysql -h "$host" -u"$SPRING_DATASOURCE_USERNAME" -p"$SPRING_DATASOURCE_PASSWORD" -e "SELECT 1"; do
  echo "Waiting for MySQL..."
  sleep 2
done

exec "$@"
