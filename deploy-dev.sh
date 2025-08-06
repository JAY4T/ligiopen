#!/bin/bash

# Fixed deploy-dev.sh with proper rolling deployment (zero downtime)
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

  # Wait for binary change to take effect
  sleep 10

  # Restart all services with previous binary
  for port in "${PORTS[@]}"; do
    SERVICE="${SERVICE_NAME}@${port}.service"
    echo "   Rolling back $SERVICE..."
    sudo systemctl restart "$SERVICE"
    if sudo systemctl is-active --quiet "$SERVICE"; then
      echo "   ✅ $SERVICE rolled back successfully"
    else
      echo "   ❌ Failed to roll back $SERVICE"
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

WAIT_TIME=15
rolling_restart_service() {
  local port=$1
  local SERVICE="${SERVICE_NAME}@${port}.service"
  
  echo "🔄 Rolling restart of ${SERVICE}..."
  echo "   📊 Current status before restart: $(sudo systemctl is-active $SERVICE 2>/dev/null || echo 'inactive')"

  # Use systemctl restart instead of separate stop/start to avoid port conflicts
  echo "   🔄 Restarting ${SERVICE}..."
  if sudo systemctl restart "$SERVICE"; then
    echo "   ✅ Restarted $SERVICE"
  else
    echo "   ❌ Failed to restart ${SERVICE}"
    echo "   📋 Checking service status..."
    sudo systemctl status "$SERVICE" --no-pager || true
    echo "   📋 Checking recent logs..."
    sudo journalctl -u "$SERVICE" --no-pager -n 15 || true
    return 1
  fi

  # Wait for the service to fully start
  echo "   ⏳ Waiting ${WAIT_TIME}s for ${SERVICE} to fully start..."
  sleep $WAIT_TIME

  # Health check
  if sudo systemctl is-active --quiet "${SERVICE}"; then
    echo "   ✅ ${SERVICE} is active and running"
    
    # Test if the application is responding on the port
    echo "   🔍 Testing application response on port $port..."
    if timeout 30 bash -c "until nc -z localhost $port; do sleep 1; done" 2>/dev/null; then
      echo "   ✅ Application responding on port $port"
      return 0
    else
      echo "   ⚠️  Application not responding on port $port within timeout"
      echo "   📋 Port status:"
      lsof -i :$port 2>/dev/null || echo "No process found on port $port"
      echo "   📋 Recent logs:"
      sudo journalctl -u "${SERVICE}" --no-pager -n 10 || true
      return 1
    fi
  else
    echo "   ❌ ${SERVICE} failed to start correctly"
    echo "   📋 Service status:"
    sudo systemctl status "${SERVICE}" --no-pager || true
    echo "   📋 Recent logs:"
    sudo journalctl -u "${SERVICE}" --no-pager -n 20 || true
    return 1
  fi
}

# Perform rolling restart - one service at a time to maintain availability
echo "🔄 Starting rolling deployment..."
FAILED_SERVICES=()

for port in "${PORTS[@]}"; do
  echo ""
  echo "📍 Processing service on port $port..."
  
  if rolling_restart_service $port; then
    echo "   ✅ Successfully deployed to port $port"
  else
    echo "   ❌ Failed to deploy to port $port"
    FAILED_SERVICES+=($port)
    
    # If this is a critical failure (all services failed), rollback immediately
    if [ ${#FAILED_SERVICES[@]} -eq ${#PORTS[@]} ]; then
      echo "💥 All services have failed! Rolling back immediately..."
      rollback_deployment
      exit 1
    fi
  fi
  
  # Wait between services to ensure stability
  if [ $port != "${PORTS[-1]}" ]; then  # Don't wait after last service
    echo "   ⏳ Waiting before next service to ensure stability..."
    sleep 5
  fi
done

# Check results
echo ""
if [ ${#FAILED_SERVICES[@]} -eq 0 ]; then
  echo "🎉 Rolling deployment completed successfully!"
elif [ ${#FAILED_SERVICES[@]} -eq ${#PORTS[@]} ]; then
  echo "💥 All services failed to deploy! This should have been caught earlier."
  rollback_deployment
  exit 1
else
  echo "⚠️  Partial deployment: ${#FAILED_SERVICES[@]} of ${#PORTS[@]} services failed"
  echo "   Failed ports: ${FAILED_SERVICES[*]}"
  echo "   Service is still available on working instances"
  echo "   ⚠️  WARNING: Consider investigating failed services or triggering manual rollback"
  echo "   ℹ️  To rollback manually, run: $0 rollback"
fi

echo ""
echo "📊 Final Status:"
for port in "${PORTS[@]}"; do
  SERVICE="${SERVICE_NAME}@${port}.service"
  STATUS=$(sudo systemctl is-active "${SERVICE}" 2>/dev/null || echo "unknown")
  
  if [[ " ${FAILED_SERVICES[*]} " =~ " ${port} " ]]; then
    echo "   ${SERVICE}: $STATUS ❌"
  else
    echo "   ${SERVICE}: $STATUS ✅"
  fi
  
  # Show what's actually listening on each port
  PORT_STATUS=$(lsof -i :$port 2>/dev/null | grep LISTEN | head -1 || echo "No process listening")
  echo "   Port $port: $PORT_STATUS"
done

echo ""
echo "🌐 Services should be available at:"
for port in "${PORTS[@]}"; do
  echo "   http://localhost:$port"
done
echo "   https://dev.ligiopen.com"
echo ""
echo "=== Rolling Deployment Complete ==="