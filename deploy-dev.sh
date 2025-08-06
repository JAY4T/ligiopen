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
  # Stop all services first
  for port in "${PORTS[@]}"; do
    SERVICE="${SERVICE_NAME}@${port}.service"
    echo "Stopping $SERVICE for rollback..."
    sudo systemctl stop $SERVICE || true
  done
  # Wait for ports to be released
  sleep 5
  # Restart all services with the previous binary
  for port in "${PORTS[@]}"; do
    SERVICE="${SERVICE_NAME}@${port}.service"
    echo "Restarting $SERVICE with rollback..."
    sudo systemctl start $SERVICE
    sleep 3  # Wait between service starts
  done
  echo "Rollback completed."
}

# Function to stop all services
stop_all_services() {
  echo "Stopping all services..."
  for port in "${PORTS[@]}"; do
    SERVICE="${SERVICE_NAME}@${port}.service"
    echo "Stopping ${SERVICE}..."
    sudo systemctl stop "$SERVICE" || true
  done
  # Wait for all ports to be released
  echo "Waiting for ports to be released..."
  sleep 5
  
  # Verify ports are actually free
  for port in "${PORTS[@]}"; do
    while netstat -tulpn | grep ":${port} " > /dev/null 2>&1; do
      echo "Port ${port} still in use, waiting..."
      sleep 2
    done
    echo "Port ${port} is now free"
  done
}

# Copy the binary to the deployment directory
echo "Promoting ${BINARY_NAME} to ${DEPLOY_BIN}..."
ln -sf "${RELEASES_DIR}/${BINARY_NAME}" "${DEPLOY_BIN}"

# Stop all services first to avoid port conflicts
stop_all_services

WAIT_TIME=10  # Increased wait time
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
  
  # Verify the port is actually listening
  local retries=5
  while [ $retries -gt 0 ]; do
    if netstat -tulpn | grep ":${port} " > /dev/null 2>&1; then
      echo "${SERVICE} is successfully listening on port ${port}"
      break
    else
      echo "Port ${port} not yet listening, waiting..."
      sleep 2
      retries=$((retries-1))
    fi
  done
  
  if [ $retries -eq 0 ]; then
    echo "Error: ${SERVICE} is not listening on port ${port}. Rolling back deployment."
    rollback_deployment
    exit 1
  fi
  
  echo "${SERVICE} started successfully."
}

# Start services sequentially with proper delays
for port in "${PORTS[@]}"; do
  restart_service $port
  # Add delay between starting different services
  sleep 3
done

echo "Deployment completed successfully."