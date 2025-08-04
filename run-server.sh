#!/bin/bash

# Multi-Threaded Web Server Runner Script

echo "🚀 Starting Multi-Threaded Web Server..."
echo "========================================"

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java is not installed or not in PATH"
    exit 1
fi

# Check if the JAR file exists
if [ ! -f "target/multi-threaded-web-server-1.0.0.jar" ]; then
    echo "❌ Error: JAR file not found. Please run 'mvn package' first."
    exit 1
fi

echo "✅ Java found: $(java -version 2>&1 | head -n 1)"
echo "✅ JAR file found: target/multi-threaded-web-server-1.0.0.jar"
echo ""
echo "🌐 Server will be available at: http://localhost:8082"
echo "📁 Supported file types: HTML, JPG, GIF, PNG, CSS, JS, TXT"
echo "🔧 Press Ctrl+C to stop the server"
echo ""

# Run the server
java -jar target/multi-threaded-web-server-1.0.0.jar 