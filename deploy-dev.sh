#!/bin/bash
set -eo pipefail

# ======================
# CONFIGURATION
# ======================
COMMIT_HASH=$1
RELEASES_DIR="/home/pipeline/releases/ligiopen_dev"
DEPLOY_BIN="/home/pipeline/production/ligiopen_dev/ligiopen"
SERVICE_NAME="ligiopen_dev"
BINARY_NAME="ligiopen-${COMMIT_HASH}.jar"
PORTS=("3000" "3001")
MAX_RETRIES=3
RETRY_DELAY=5
MIN_MEMORY_MB=500
MIN_DISK_PERCENT=10

# ======================
# PREREQUISITE CHECKS
# ======================

# 1. Validate arguments
if [ -z "$COMMIT_HASH" ]; then
  echo "❌ Usage: $0 <commit-hash>"
  exit 1
fi

# 2. Verify Java is available
if ! command -v java >/dev/null 2>&1; then
  echo "❌ Java is not installed or not in PATH"
  exit 1
fi

# 3. Check disk space
DISK_PERCENT=$(df --output=pcent / | tail -1 | tr -d '% ')
if [ "$DISK_PERCENT" -gt $((100 - MIN_DISK_PERCENT)) ]; then
  echo "❌ Critical: Low disk space (${DISK_PERCENT}% used)"
  exit 1
fi

# 4. Check available memory
FREE_MEM=$(free -m | awk '/Mem:/ {print $4}')
if [ "$FREE_MEM" -lt $MIN_MEMORY_MB ]; then
  echo "⚠️ Warning: Low memory available (${FREE_MEM}MB free)"
fi

# 5. Validate binary exists
if [ ! -f "${RELEASES_DIR}/${BINARY_NAME}" ]; then
  echo "❌ Error: Binary ${BINARY_NAME} not found in ${RELEASES_DIR}"
  exit 1
fi

# ======================
# DEPLOYMENT FUNCTIONS
# ======================

service_status() {
  systemctl is-active "${SERVICE_NAME}@$1.service" >/dev/null 2>&1
}

get_logs() {
  echo "=== Journal Logs ==="
  journalctl -u "${SERVICE_NAME}@$1.service" -n 20 --no-pager || true
  echo -e "\n=== Application Output ==="
  tail -n 20 "/home/pipeline/logs/ligiopen_dev/output-$1.log" 2>/dev/null || true
  echo -e "\n=== Error Output ==="
  tail -n 20 "/home/pipeline/logs/ligiopen_dev/error-$1.log" 2>/dev/null || true
}

# ======================
# DEPLOYMENT PROCESS
# ======================

# Backup previous version
if [ -L "${DEPLOY_BIN}" ]; then
  PREVIOUS=$(readlink -f "${DEPLOY_BIN}")
  echo "ℹ️ Current version: ${PREVIOUS}"
else
  PREVIOUS=""
  echo "⚠️ No previous version found"
fi

# Deploy new binary
echo "🚀 Deploying ${BINARY_NAME}..."
ln -sf "${RELEASES_DIR}/${BINARY_NAME}" "${DEPLOY_BIN}"

# Restart services
for port in "${PORTS[@]}"; do
  SERVICE="${SERVICE_NAME}@${port}.service"
  echo "🔧 Processing ${SERVICE}..."
  
  # Cleanup existing service
  echo "🛑 Stopping existing service..."
  sudo systemctl stop "$SERVICE" || true
  sudo fuser -k "${port}/tcp" 2>/dev/null || true
  sleep 2
  
  # Start with retries
  for ((retry=1; retry<=MAX_RETRIES; retry++)); do
    echo "🔄 Starting attempt ${retry}/${MAX_RETRIES}..."
    
    if sudo systemctl start "$SERVICE"; then
      # Verify service started
      for ((i=1; i<=5; i++)); do
        if service_status "$port"; then
          echo "✅ Service successfully started on port ${port}"
          break 2
        fi
        sleep 2
      done
    fi
    
    if [ "$retry" -lt "$MAX_RETRIES" ]; then
      echo "⚠️ Attempt ${retry} failed, retrying in ${RETRY_DELAY} seconds..."
      sleep "$RETRY_DELAY"
    else
      echo "❌ Error: Failed to start ${SERVICE} after ${MAX_RETRIES} attempts"
      echo "📜 Service logs:"
      get_logs "$port"
      
      # Emergency cleanup
      sudo systemctl stop "$SERVICE" || true
      sudo pkill -f "java.*${port}" || true
      
      exit 1
    fi
  done
done

echo "🎉 Deployment completed successfully"