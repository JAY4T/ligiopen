#!/bin/bash
# Exit on error
set -e

# Check if commit hash is passed as an argument
if [ -z "$1" ]; then
  echo "Usage: $0 <commit-hash>"
  exit 1
fi

COMMIT_HASH=$1
RELEASES_DIR="/home/pipeline/releases/ligiopen_dev"
DEPLOY_BIN="/home/pipeline/production/ligiopen_dev/ligiopen"
SERVICE_NAME="ligiopen_dev"
BINARY_NAME="ligiopen-${COMMIT_HASH}.jar"
declare -a PORTS=("3000" "3001")

# Check if the binary exists
if [ ! -f "${RELEASES_DIR}/${BINARY_NAME}" ]; then
  echo "Binary ${BINARY_NAME} not found in ${RELEASES_DIR}"
  exit 1
fi

# Keep a reference to the previous binary from the symlink
if [ -L "${DEPLOY_BIN}" ]; then
  PREVIOUS=$(readlink -f $DEPLOY_BIN)
  echo "Current binary is ${PREVIOUS}, saved for rollback."
else
  echo "No symbolic link found, no previous binary to backup."
  PREVIOUS=""
fi

rollback_deployment() {
  if [ -n "$PREVIOUS" ]; then
    echo "Rolling back to previous binary: ${PREVIOUS}"
    ln -sfn "${PREVIOUS}" "${DEPLOY_BIN}"
  else
    echo "No previous binary to roll back to."
  fi
  # Stop and restart services sequentially for rollback
  for port in "${PORTS[@]}"; do
    SERVICE="${SERVICE_NAME}@${port}.service"
    echo "Stopping $SERVICE for rollback..."
    sudo systemctl stop $SERVICE || true
  done
  sleep 10
  for port in "${PORTS[@]}"; do
    SERVICE="${SERVICE_NAME}@${port}.service"
    echo "Starting $SERVICE with rollback..."
    sudo systemctl start $SERVICE
    sleep 8  # Wait between service starts
  done
  echo "Rollback completed."
}

# Copy the binary to the deployment directory
echo "Promoting ${BINARY_NAME} to ${DEPLOY_BIN}..."
ln -sf "${RELEASES_DIR}/${BINARY_NAME}" "${DEPLOY_BIN}"

# Stop all services first, then start them one by one
echo "Stopping all services..."
for port in "${PORTS[@]}"; do
  SERVICE="${SERVICE_NAME}@${port}.service"
  echo "Stopping ${SERVICE}..."
  sudo systemctl stop "$SERVICE" || true
done

# Wait for all services to stop
echo "Waiting for all services to stop..."
sleep 10

WAIT_TIME=20
restart_service() {
  local port=$1
  local SERVICE="${SERVICE_NAME}@${port}.service"
  echo "Starting ${SERVICE}..."
  
  # Start the service
  if ! sudo systemctl start "$SERVICE"; then
    echo "Error: Failed to start ${SERVICE}. Rolling back deployment."
    rollback_deployment
    exit 1
  fi
  
  # Wait for service to fully start
  echo "Waiting for ${SERVICE} to fully start..."
  sleep $WAIT_TIME
  
  # Check the status of the service
  if ! systemctl is-active --quiet "${SERVICE}"; then
    echo "Error: ${SERVICE} failed to start correctly. Rolling back deployment."
    rollback_deployment
    exit 1
  fi
  
  echo "${SERVICE} started successfully."
}

# Start services one by one with delays
for port in "${PORTS[@]}"; do
  restart_service $port
  # Wait between starting services to avoid conflicts
  sleep 5
done

echo "Deployment completed successfully."