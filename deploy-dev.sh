#!/bin/bash

# Updated deploy-dev.sh with aggressive process termination
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

# Function to aggressively kill processes on a port
kill_processes_on_port() {
  local port=$1
  echo "   🔨 Killing all processes on port $port..."
  
  # Get all PIDs using the port
  local pids=$(lsof -ti:$port 2>/dev/null || true)
  
  if [ -z "$pids" ]; then
    echo "   ℹ️  No processes found on port $port"
    return 0
  fi
  
  echo "   📋 Found processes: $pids"
  
  # First try SIGTERM
  for pid in $pids; do
    if kill -TERM $pid 2>/dev/null; then
      echo "   📤 Sent SIGTERM to process $pid"
    fi
  done
  
  # Wait 5 seconds
  sleep 5
  
  # Check if any are still running and kill with SIGKILL
  local remaining_pids=$(lsof -ti:$port 2>/dev/null || true)
  if [ -n "$remaining_pids" ]; then
    echo "   ⚡ Force killing remaining processes: $remaining_pids"
    for pid in $remaining_pids; do
      if kill -KILL $pid 2>/dev/null; then
        echo "   💀 Force killed process $pid"
      fi
    done
  fi
  
  # Final wait
  sleep 2
}

# Function to wait for port to be free with aggressive cleanup
wait_for_port_free() {
  local port=$1
  local max_wait=10  # Reduced initial wait time
  local count=0
  
  echo "   🔍 Checking if port $port is free..."
  
  # Initial check
  while lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; do
    if [ $count -ge $max_wait ]; then
      echo "   ⚡ Port $port still in use after ${max_wait} seconds, forcing cleanup..."
      kill_processes_on_port $port
      break
    fi
    
    echo "   ⏳ Port $port still in use, waiting... (${count}/${max_wait})"
    sleep 1
    count=$((count + 1))
  done
  
  # Final verification
  if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; then
    echo "   ❌ Port $port is still in use after aggressive cleanup"
    echo "   📋 Remaining processes:"
    lsof -Pi :$port -sTCP:LISTEN || true
    return 1
  fi
  
  echo "   ✅ Port $port is now free"
  return 0
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
restart_service() {
  local port=$1
  local SERVICE="${SERVICE_NAME}@${port}.service"
  
  echo "🔄 Restarting ${SERVICE}..."

  # Get the current PID if service is running
  local current_pid=""
  if systemctl is-active --quiet "$SERVICE"; then
    current_pid=$(systemctl show --property MainPID --value "$SERVICE" 2>/dev/null || true)
    if [ "$current_pid" != "0" ] && [ -n "$current_pid" ]; then
      echo "   📋 Current service PID: $current_pid"
    fi
  fi

  # Stop the service
  echo "   🛑 Stopping ${SERVICE}..."
  sudo systemctl stop "$SERVICE" 2>/dev/null || true
  
  # Wait a moment for systemd to process the stop
  sleep 2
  
  # If we had a PID and it's still running, kill it directly
  if [ -n "$current_pid" ] && [ "$current_pid" != "0" ]; then
    if kill -0 "$current_pid" 2>/dev/null; then
      echo "   ⚡ Service PID $current_pid still running, killing directly..."
      kill -TERM "$current_pid" 2>/dev/null || true
      sleep 3
      if kill -0 "$current_pid" 2>/dev/null; then
        echo "   💀 Force killing PID $current_pid..."
        kill -KILL "$current_pid" 2>/dev/null || true
      fi
    fi
  fi
  
  # Wait for the port to be free
  if ! wait_for_port_free $port; then
    echo "   ❌ Failed to free port $port"
    rollback_deployment
    exit 1
  fi

  # Start the service
  echo "   ▶️  Starting ${SERVICE}..."
  if sudo systemctl start "$SERVICE"; then
    echo "   ✅ Started $SERVICE"
  else
    echo "   ❌ Failed to start ${SERVICE}"
    echo "   📋 Checking service status..."
    sudo systemctl status "$SERVICE" --no-pager --lines=5 || true
    echo "   📋 Checking recent logs..."
    sudo journalctl -u "$SERVICE" --no-pager -n 10 || true
    
    rollback_deployment
    exit 1
  fi

  # Wait for the service to fully start
  echo "   ⏳ Waiting ${WAIT_TIME}s for ${SERVICE} to fully start..."
  
  # Monitor startup progress
  local startup_count=0
  while [ $startup_count -lt $WAIT_TIME ]; do
    if systemctl is-active --quiet "${SERVICE}"; then
      if nc -z localhost $port 2>/dev/null; then
        echo "   ✅ ${SERVICE} is active and responding on port $port"
        break
      fi
    fi
    sleep 1
    startup_count=$((startup_count + 1))
    
    # Show progress every 5 seconds
    if [ $((startup_count % 5)) -eq 0 ]; then
      echo "   ⏳ Still starting... (${startup_count}/${WAIT_TIME})"
    fi
  done

  # Final checks
  if sudo systemctl is-active --quiet "${SERVICE}"; then
    echo "   ✅ ${SERVICE} is active"
    
    if nc -z localhost $port 2>/dev/null; then
      echo "   ✅ Application responding on port $port"
    else
      echo "   ⚠️  Service is active but port $port may not be ready yet"
    fi
  else
    echo "   ❌ ${SERVICE} failed to start correctly"
    echo "   📋 Service status:"
    sudo systemctl status "${SERVICE}" --no-pager --lines=10 || true
    echo "   📋 Recent logs:"
    sudo journalctl -u "${SERVICE}" --no-pager -n 20 || true
    
    rollback_deployment
    exit 1
  fi
}

# Show current status before deployment
echo "📊 Pre-deployment status:"
for port in "${PORTS[@]}"; do
  SERVICE="${SERVICE_NAME}@${port}.service"
  STATUS=$(sudo systemctl is-active "${SERVICE}" 2>/dev/null || echo "unknown")
  echo "   ${SERVICE}: $STATUS"
  
  if lsof -Pi :$port -sTCP:LISTEN >/dev/null 2>&1; then
    echo "   Port $port: LISTENING"
  else
    echo "   Port $port: FREE"
  fi
done
echo ""

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
echo "📊 Port Status:"
for port in "${PORTS[@]}"; do
  if ss -tlnp | grep -q ":$port "; then
    echo "   Port $port: ✅ LISTENING"
  else
    echo "   Port $port: ❌ NOT LISTENING"
  fi
done

echo ""
echo "=== Deployment Complete ==="