# Multi-Threaded Web Server

A Java-based multi-threaded web server that can serve HTML, JPG, GIF, PNG, CSS, JS, and TXT files with proper MIME type support and 404 error handling.

## 🚀 Features

- **Multi-threaded request handling** - Each client connection is handled in a separate thread
- **404 error handling** - Beautiful error page for non-existent resources
- **Multiple file type support**:
  - HTML files (text/html)
  - JPG/JPEG images (image/jpeg)
  - GIF images (image/gif)
  - PNG images (image/png)
  - CSS files (text/css)
  - JavaScript files (application/javascript)
  - Text files (text/plain)
- **Proper MIME type detection** - Automatic content-type header based on file extension
- **Binary file support** - Images are served as raw bytes
- **HTTP/1.0 protocol support**
- **Maven project structure** - Easy build and dependency management

## 📋 Prerequisites

- Java 15 or higher
- Maven 3.6 or higher

## 🛠️ Installation & Setup

### 1. Install Maven (if not already installed)

**On Arch Linux:**
```bash
sudo pacman -S maven
```

**On Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install maven
```

**On macOS:**
```bash
brew install maven
```

### 2. Configure JAVA_HOME

Set the JAVA_HOME environment variable:

```bash
export JAVA_HOME=/usr/lib/jvm/java-24-openjdk
echo 'export JAVA_HOME=/usr/lib/jvm/java-24-openjdk' >> ~/.bashrc
```

**Note:** The exact path may vary depending on your Java installation. Use `which java` and `readlink -f /usr/bin/java` to find your Java installation path.

### 3. Verify Installation

```bash
java --version
mvn --version
echo $JAVA_HOME
```

## 🏗️ Building the Project

1. **Clone or navigate to the project directory:**
   ```bash
   cd multiThreadedWebServer
   ```

2. **Build the project:**
   ```bash
   mvn clean package
   ```

3. **Verify the build:**
   ```bash
   ls -la target/
   ```
   You should see `multi-threaded-web-server-1.0.0.jar` in the target directory.

## 🚀 Running the Server

### Option 1: Using the provided script (Recommended)
```bash
./run-server.sh
```

### Option 2: Direct Java execution
```bash
java -jar target/multi-threaded-web-server-1.0.0.jar
```

### Option 3: Using Maven
```bash
mvn exec:java -Dexec.mainClass="com.compunet.webserver.Main"
```

## 🌐 Testing the Server

Once the server is running, you can test it at `http://localhost:8080`

### Available Test Pages:
- **Home page:** http://localhost:8080/
- **Test page:** http://localhost:8080/test.html
- **Sample text file:** http://localhost:8080/sample.txt
- **CSS file:** http://localhost:8080/style.css
- **404 test:** http://localhost:8080/nonexistent.html

### Using curl to test:
```bash
# Test home page
curl http://localhost:8080/

# Test 404 error
curl http://localhost:8080/doesnotexist.txt

# Test text file
curl http://localhost:8080/sample.txt

# Test headers
curl -I http://localhost:8080/test.html
```

## 📁 Project Structure

```
multiThreadedWebServer/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── compunet/
│       │           └── webserver/
│       │               └── Main.java
│       └── resources/
│           ├── index.html
│           ├── test.html
│           ├── sample.txt
│           └── style.css
├── target/
│   └── multi-threaded-web-server-1.0.0.jar
├── pom.xml
├── run-server.sh
└── README.md
```

## 🔧 Configuration

The server runs on port 8080 by default. To change the port, modify the `init()` method in `Main.java`:

```java
ServerSocket server = new ServerSocket(8080); // Change 8080 to your desired port
```

## 📝 Adding New File Types

To add support for new file types, update the `MIME_TYPES` map in `Main.java`:

```java
static {
    MIME_TYPES.put("html", "text/html");
    MIME_TYPES.put("jpg", "image/jpeg");
    MIME_TYPES.put("gif", "image/gif");
    // Add your new file type here
    MIME_TYPES.put("pdf", "application/pdf");
}
```

## 🐛 Troubleshooting

### Common Issues:

1. **Port already in use:**
   ```bash
   # Find process using port 8080
   lsof -i :8080
   # Kill the process
   kill -9 <PID>
   ```

2. **JAR file not found:**
   ```bash
   # Rebuild the project
   mvn clean package
   ```

3. **Permission denied:**
   ```bash
   # Make the run script executable
   chmod +x run-server.sh
   ```

4. **Java version issues:**
   ```bash
   # Check Java version
   java --version
   # Should be 15 or higher
   ```

## 📊 Performance

The server uses a thread-per-connection model, which is suitable for:
- Development and testing
- Small to medium workloads
- Educational purposes

For production use, consider implementing:
- Thread pooling
- Connection pooling
- Request queuing
- Load balancing

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is for educational purposes.

## 🎯 Learning Objectives

This project demonstrates:
- Multi-threaded programming in Java
- HTTP protocol implementation
- File I/O operations
- MIME type handling
- Error handling and user experience
- Maven project management
- Build automation

---

**Happy coding! 🚀**