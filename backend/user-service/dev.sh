#!/usr/bin/env bash
set -euo pipefail

echo "Starting Spring Boot in dev mode with auto-compile..."

WATCH_DIRS=()
[[ -d src/main ]] && WATCH_DIRS+=(src/main)
[[ -d src/test ]] && WATCH_DIRS+=(src/test)

polling_watch() {
  echo "Using POLLING watcher on: ${WATCH_DIRS[*]}"
  last=""
  while true; do
    current=$(find "${WATCH_DIRS[@]}" -type f -print0 \
      | xargs -0 stat -c '%Y %n' 2>/dev/null \
      | md5sum | awk '{print $1}')

    if [[ "$current" != "$last" ]]; then
      last="$current"
      echo "Change detected -> mvn compile"
      mvn -q -DskipTests compile || true
    fi
    sleep 1
  done
}

inotify_watch() {
  echo "Using inotify watcher on: ${WATCH_DIRS[*]}"
  while inotifywait -r -e modify,create,delete,move "${WATCH_DIRS[@]}"; do
    echo "Change detected -> mvn compile"
    mvn -q -DskipTests compile || true
  done
}

# If Docker doesn't forward events, force polling with env var
if [[ "${FORCE_POLLING:-false}" == "true" ]]; then
  polling_watch &
else
  if command -v inotifywait >/dev/null 2>&1; then
    inotify_watch &
  else
    polling_watch &
  fi
fi

# Important: disable fork so DevTools restart is reliable
exec mvn -q -DskipTests spring-boot:run -Dspring-boot.run.fork=false
