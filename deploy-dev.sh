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
# SETUP SYSTEM CONFIGURATION FIRST
# ======================

echo "🔧 Setting up system configuration..."

# Ensure log directory exists
mkdir -p /home/pipeline/logs/ligiopen_dev
chown pipeline:pipeline /home/pipeline/logs/ligiopen_dev

# Create systemd service template BEFORE starting services
cat > /etc/systemd/system/ligiopen_dev@.service << 'EOF'
[Unit]
Description=Ligiopen Spring Boot Development %i Service
After=network.target postgresql.service
Requires=postgresql.service

[Service]
Type=simple
ExecStart=/usr/bin/java -jar -Dserver.port=%i /home/pipeline/production/ligiopen_dev/ligiopen
WorkingDirectory=/home/pipeline/production/ligiopen_dev
User=pipeline
Group=pipeline
Environment="APP_PORT=%i"
EnvironmentFile=-/etc/env/ligiopen_dev.env
LimitNOFILE=65536
Restart=on-failure
RestartSec=10
TimeoutSec=30
PrivateTmp=true
ProtectSystem=strict
ReadWritePaths=/home/pipeline/logs/ligiopen_dev /tmp
NoNewPrivileges=true
StandardOutput=file:/home/pipeline/logs/ligiopen_dev/output-%i.log
StandardError=file:/home/pipeline/logs/ligiopen_dev/error-%i.log

[Install]
WantedBy=multi-user.target
EOF

# Create Nginx configuration
cat > /etc/nginx/sites-available/ligiopen-dev << 'EOF'
upstream ligiopen_dev {
    server 127.0.0.1:3000;
    server 127.0.0.1:3001;
}

server {
    listen 80;
    server_name dev.ligiopen.com;

    location / {
        proxy_pass http://ligiopen_dev;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }
}
EOF

# Enable Nginx site
ln -sf /etc/nginx/sites-available/ligiopen-dev /etc/nginx/sites-enabled/
rm -f /etc/nginx/sites-enabled/default

# Reload systemd to recognize new services
systemctl daemon-reload
echo "✅ System configuration completed"

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

cleanup_port() {
  local port=$1
  echo "🧹 Cleaning up port ${port}..."
  
  # Stop systemd service
  sudo systemctl stop "${SERVICE_NAME}@${port}.service" 2>/dev/null || true
  
  # Wait a moment for graceful shutdown
  sleep 2
  
  # Force kill any remaining processes on the port
  sudo fuser -k "${port}/tcp" 2>/dev/null || true
  
  # Kill any Java processes that might be using the port
  sudo pkill -f "java.*-Dserver.port=${port}" 2>/dev/null || true
  
  # Additional cleanup - find and kill processes listening on the port
  local pids=$(sudo lsof -ti:${port} 2>/dev/null || true)
  if [ -n "$pids" ]; then
    echo "🔪 Force killing processes on port ${port}: $pids"
    sudo kill -9 $pids 2>/dev/null || true
  fi
  
  # Wait for port to be free
  for i in {1..10}; do
    if ! sudo lsof -i:${port} >/dev/null 2>&1; then
      echo "✅ Port ${port} is now free"
      return 0
    fi
    echo "⏳ Waiting for port ${port} to be free... (${i}/10)"
    sleep 1
  done
  
  echo "⚠️ Warning: Port ${port} may still be in use"
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

# Clean up all ports first
echo "🧹 Cleaning up existing services..."
for port in "${PORTS[@]}"; do
  cleanup_port "$port"
done

# Deploy new binary
echo "🚀 Deploying ${BINARY_NAME}..."
mkdir -p "$(dirname "$DEPLOY_BIN")"
ln -sf "${RELEASES_DIR}/${BINARY_NAME}" "${DEPLOY_BIN}"

# Restart services
for port in "${PORTS[@]}"; do
  SERVICE="${SERVICE_NAME}@${port}.service"
  echo "🔧 Starting ${SERVICE}..."
  
  # Start with retries
  for ((retry=1; retry<=MAX_RETRIES; retry++)); do
    echo "🔄 Starting attempt ${retry}/${MAX_RETRIES} for port ${port}..."
    
    # Double-check port is free before starting
    if sudo lsof -i:${port} >/dev/null 2>&1; then
      echo "⚠️ Port ${port} still in use, performing additional cleanup..."
      cleanup_port "$port"
    fi
    
    if sudo systemctl start "$SERVICE"; then
      # Verify service started and is healthy
      sleep 3
      if service_status "$port"; then
        # Additional check: verify the port is actually being used by our service
        if sudo lsof -i:${port} >/dev/null 2>&1; then
          echo "✅ Service successfully started on port ${port}"
          break
        else
          echo "⚠️ Service started but port ${port} not in use"
        fi
      else
        echo "⚠️ Service failed to start properly"
      fi
    fi
    
    if [ "$retry" -lt "$MAX_RETRIES" ]; then
      echo "⚠️ Attempt ${retry} failed, retrying in ${RETRY_DELAY} seconds..."
      cleanup_port "$port"
      sleep "$RETRY_DELAY"
    else
      echo "❌ Error: Failed to start ${SERVICE} after ${MAX_RETRIES} attempts"
      echo "📜 Service logs:"
      get_logs "$port"
      
      # Emergency cleanup
      cleanup_port "$port"
      exit 1
    fi
  done
done

# Test Nginx configuration and reload
echo "🌐 Configuring Nginx..."
if nginx -t; then
  systemctl reload nginx
  echo "✅ Nginx configuration reloaded"
else
  echo "⚠️ Nginx configuration test failed"
fi

# Final status check
echo "🔍 Final status check..."
for port in "${PORTS[@]}"; do
  if service_status "$port"; then
    echo "✅ Port ${port}: Service running"
  else
    echo "❌ Port ${port}: Service not running"
  fi
done

echo "🎉 Deployment completed successfully"