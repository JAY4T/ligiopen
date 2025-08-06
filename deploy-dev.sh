#!/bin/bash

# Updated deploy-dev.sh with better error handling and logging
# Exit on error
set -e

echo "=== LigiopenBackendApp Dev Deployment Started ==="
echo "Time: $(date)"

# Check if commit hash is passed as an argument
if [ -z "$1" ]; then
  echo "❌ Usage: $0 <commit-hash>"
  exit 1
fi

COMMIT_HASH=$1
RELEASES_DIR="/home/pipeline/releases/ligiopen_dev"
DEPLOY_BIN="/home/pipeline/production/ligiopen_dev/ligiopen"
SERVICE_NAME="ligiopen_dev"
BINARY_NAME="ligiopen-${COMMIT_HASH}.jar"
declare -a PORTS=("3000" "3001")

echo "📦 Deploying: $BINARY_NAME"
echo "🔍 Commit Hash: $COMMIT_HASH"

# Check if the binary exists
if [ ! -f "${RELEASES_DIR}/${BINARY_NAME}" ]; then
  echo "❌ Binary ${BINARY_NAME} not found in ${RELEASES_DIR}"
  exit 1
fi

echo "✅ Binary found: ${RELEASES_DIR}/${BINARY_NAME}"

# Keep a reference to the previous binary from the symlink
if [ -L "${DEPLOY_BIN}" ]; then
  PREVIOUS=$(readlink -f $DEPLOY_BIN)
  echo "📋 Current binary: ${PREVIOUS} (saved for rollback)"
else
  echo "ℹ️  No symbolic link found, no previous binary to backup"
  PREVIOUS=""
fi

rollback_deployment() {
  echo "🔄 Rolling back deployment..."
  if [ -n "$PREVIOUS" ]; then
    echo "   Restoring previous binary: ${PREVIOUS}"
    ln -sfn "${PREVIOUS}" "${DEPLOY_BIN}"
  else
    echo "   No previous binary to roll back to"
  fi

  # Wait before restarting services
  sleep 5

  # Restart all services with the previous binary
  for port in "${PORTS[@]}"; do
    SERVICE="${SERVICE_NAME}@${port}.service"
    echo "   Restarting $SERVICE..."
    if sudo systemctl restart $SERVICE; then
      echo "   ✅ $SERVICE restarted successfully"
    else
      echo "   ❌ Failed to restart $SERVICE"
    fi
  done

  echo "🔄 Rollback completed"
}

# Create the production directory if it doesn't exist
mkdir -p "$(dirname "$DEPLOY_BIN")"

# Promote the binary
echo "🚀 Promoting ${BINARY_NAME} to ${DEPLOY_BIN}..."
ln -sf "${RELEASES_DIR}/${BINARY_NAME}" "${DEPLOY_BIN}"

# Verify the symlink was created correctly
if [ -L "${DEPLOY_BIN}" ]; then
  echo "✅ Symlink created successfully"
  echo "   Target: $(readlink -f ${DEPLOY_BIN})"
else
  echo "❌ Failed to create symlink"
  exit 1
fi

WAIT_TIME=10
restart_service() {
  local port=$1
  local SERVICE="${SERVICE_NAME}@${port}.service"
  
  echo "🔄 Restarting ${SERVICE}..."

  # Stop the service first (ignore errors if it's not running)
  sudo systemctl stop "$SERVICE" 2>/dev/null || true
  
  # Wait a moment
  sleep 2

  # Start the service
  if sudo systemctl start "$SERVICE"; then
    echo "   ✅ Started $SERVICE"
  else
    echo "   ❌ Failed to start ${SERVICE}"
    echo "   📋 Checking service status..."
    sudo systemctl status "$SERVICE" --no-pager || true
    echo "   📋 Checking recent logs..."
    sudo journalctl -u "$SERVICE" --no-pager -n 10 || true
    
    rollback_deployment
    exit 1
  fi

  # Wait for the service to fully start
  echo "   ⏳ Waiting ${WAIT_TIME}s for ${SERVICE} to fully start..."
  sleep $WAIT_TIME

  # Check if the service is running
  if sudo systemctl is-active --quiet "${SERVICE}"; then
    echo "   ✅ ${SERVICE} is active and running"
    
    # Test if the application is responding on the port
    if timeout 10 bash -c "until nc -z localhost $port; do sleep 1; done" 2>/dev/null; then
      echo "   ✅ Application responding on port $port"
    else
      echo "   ⚠️  Application might not be responding on port $port yet"
    fi
  else
    echo "   ❌ ${SERVICE} failed to start correctly"
    echo "   📋 Service status:"
    sudo systemctl status "${SERVICE}" --no-pager || true
    echo "   📋 Recent logs:"
    sudo journalctl -u "${SERVICE}" --no-pager -n 20 || true
    
    rollback_deployment
    exit 1
  fi
}

# Restart services one by one
for port in "${PORTS[@]}"; do
  restart_service $port
done

echo ""
echo "🎉 Deployment completed successfully!"
echo "📊 Final Status:"
for port in "${PORTS[@]}"; do
  SERVICE="${SERVICE_NAME}@${port}.service"
  STATUS=$(sudo systemctl is-active "${SERVICE}" 2>/dev/null || echo "unknown")
  echo "   ${SERVICE}: $STATUS"
done

echo ""
echo "🌐 Services should be available at:"
for port in "${PORTS[@]}"; do
  echo "   http://localhost:$port"
done
echo "   https://dev.ligiopen.com"
echo ""
echo "=== Deployment Complete ==="

