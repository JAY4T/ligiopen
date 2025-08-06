#!/bin/bash
set -e

# Check arguments
if [ -z "$1" ]; then
  echo "Usage: $0 <commit-hash>"
  exit 1
fi

# Config
COMMIT_HASH=$1
RELEASES_DIR="/home/pipeline/releases/ligiopen_dev"
DEPLOY_BIN="/home/pipeline/production/ligiopen_dev/ligiopen"
SERVICE_NAME="ligiopen"
BINARY_NAME="ligiopen-${COMMIT_HASH}.jar"
PORTS=("3000" "3001")
HEALTH_CHECK_TIMEOUT=30  # Max seconds to wait for health check

# Validate binary exists
if [ ! -f "${RELEASES_DIR}/${BINARY_NAME}" ]; then
  echo "Error: Binary ${BINARY_NAME} not found in ${RELEASES_DIR}"
  exit 1
fi

# Health check function (modify for your app)
check_health() {
  local port=$1
  if curl -sSf "http://localhost:${port}/health" >/dev/null; then
    return 0
  fi
  return 1
}

# Backup previous version
if [ -L "${DEPLOY_BIN}" ]; then
  PREVIOUS=$(readlink -f "$DEPLOY_BIN")
  echo "Current version: ${PREVIOUS}"
else
  PREVIOUS=""
  echo "No previous version found"
fi

# Deploy new binary
echo "Deploying ${BINARY_NAME}..."
ln -sf "${RELEASES_DIR}/${BINARY_NAME}" "${DEPLOY_BIN}"

# Restart services sequentially with health checks
for port in "${PORTS[@]}"; do
  SERVICE="${SERVICE_NAME}@${port}.service"
  echo "Processing ${SERVICE}..."
  
  # Stop old instance
  echo "Stopping service..."
  sudo systemctl stop "$SERVICE" || true
  sleep 2  # Ensure clean shutdown
  
  # Start new instance
  echo "Starting new version..."
  if ! sudo systemctl start "$SERVICE"; then
    echo "Error: Failed to start ${SERVICE}"
    exit 1
  fi
  
  # Health check
  echo "Waiting for service to become healthy..."
  for ((i=1; i<=HEALTH_CHECK_TIMEOUT; i++)); do
    if check_health "$port"; then
      echo "Service is healthy on port ${port}"
      break
    fi
    if [ "$i" -eq "$HEALTH_CHECK_TIMEOUT" ]; then
      echo "Error: Health check timeout for ${SERVICE}"
      exit 1
    fi
    sleep 1
  done
done

echo "Deployment completed successfully"