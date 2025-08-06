#!/bin/bash
set -e

COMMIT_HASH=$1
RELEASES_DIR="/home/pipeline/releases/ligiopen_dev"
DEPLOY_BIN="/home/pipeline/production/ligiopen_dev/ligiopen"
SERVICE_NAME="ligiopen_dev"
BINARY_NAME="ligiopen-${COMMIT_HASH}.jar"
PORTS=("3000" "3001")

# Health check (replace with your app's readiness check)
check_alive() {
  curl -sSf "http://localhost:$1/health" >/dev/null 2>&1
}

# --- Deployment ---
ln -sf "${RELEASES_DIR}/${BINARY_NAME}" "${DEPLOY_BIN}"

for port in "${PORTS[@]}"; do
  SERVICE="${SERVICE_NAME}@${port}.service"
  echo "Restarting $SERVICE..."
  
  # Stop old instance (if running)
  sudo systemctl stop "$SERVICE" || true
  sleep 2  # Let port release
  
  # Start new instance
  sudo systemctl start "$SERVICE"
  
  # Verify it came up
  for retry in {1..10}; do
    if check_alive $port; then
      echo "$SERVICE is healthy on port $port"
      break
    elif [ $retry -eq 10 ]; then
      echo "ERROR: $SERVICE failed to start!"
      exit 1
    fi
    sleep 3
  done
done

echo "Deployment successful - all instances updated"