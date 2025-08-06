#!/bin/bash
set -eo pipefail

# Configuration
COMMIT_HASH=$1
RELEASES_DIR="/home/pipeline/releases/ligiopen_dev"
DEPLOY_BIN="/home/pipeline/production/ligiopen_dev/ligiopen"
SERVICE_NAME="ligiopen_dev"
BINARY_NAME="ligiopen-${COMMIT_HASH}.jar"
PORTS=("3000" "3001")

# Enhanced Process Killer - Kills all Java processes using our ports
kill_port_processes() {
  local port=$1
  echo "🔫 Force killing processes on port ${port}..."
  sudo ss -tulpn | grep ":${port}" | awk '{print $7}' | cut -d'"' -f2 | xargs -r kill -9
  sudo lsof -ti :${port} | xargs -r kill -9
  sleep 2
}

# Validate binary exists
if [ ! -f "${RELEASES_DIR}/${BINARY_NAME}" ]; then
  echo "❌ Error: Binary not found"
  exit 1
fi

# Deploy new binary
echo "🚀 Deploying ${BINARY_NAME}..."
ln -sf "${RELEASES_DIR}/${BINARY_NAME}" "${DEPLOY_BIN}"

# Restart services with nuclear option for port conflicts
for port in "${PORTS[@]}"; do
  SERVICE="${SERVICE_NAME}@${port}.service"
  echo "🔧 Processing ${SERVICE}..."
  
  # Stop service and nuke any remaining processes
  echo "💣 Stopping service and cleaning up..."
  sudo systemctl stop "$SERVICE" || true
  kill_port_processes "$port"
  
  # Verify port is clear
  if ss -tulpn | grep -q ":${port}"; then
    echo "❌ Port ${port} still in use after cleanup!"
    exit 1
  fi
  
  # Start new instance
  echo "🚦 Starting service..."
  if ! sudo systemctl start "$SERVICE"; then
    echo "❌ Failed to start ${SERVICE}"
    journalctl -u "$SERVICE" -n 20 --no-pager
    exit 1
  fi
  
  # Verify startup
  for i in {1..10}; do
    if systemctl is-active "$SERVICE" && ! ss -tulpn | grep -q "java.*:${port}"; then
      echo "✅ Service on port ${port} started successfully"
      break
    fi
    sleep 2
  done
done

echo "🎉 Deployment completed successfully"