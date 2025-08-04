package com.compunet.webserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Main {

    // MIME type mapping for different file extensions
    private static final Map<String, String> MIME_TYPES = new HashMap<>();
    
    static {
        MIME_TYPES.put("html", "text/html");
        MIME_TYPES.put("htm", "text/html");
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("css", "text/css");
        MIME_TYPES.put("js", "application/javascript");
        MIME_TYPES.put("txt", "text/plain");
    }

    public void dispatchWorker(Socket socket) {
        new Thread(() -> {
            try {
                handleRequest(socket);
            } catch (IOException e) {
                System.err.println("Error handling request: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error closing socket: " + e.getMessage());
                }
            }
        }).start();
    }

    public void handleRequest(Socket socket) throws IOException {
        try (var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             var writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            
            String line;
            String resource = "/index.html"; // Default resource
            
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                if (line.startsWith("GET")) {
                    resource = line.split(" ")[1];
                    // Handle root path
                    if (resource.equals("/")) {
                        resource = "/index.html";
                    }
                    break;
                }
            }
            
            // Remove leading slash for file path
            String fileName = resource.startsWith("/") ? resource.substring(1) : resource;
            System.out.println("The client is asking for: " + fileName);
            
            // Send the response to the client
            sendResponse(socket, fileName);
        }
    }

    public void sendResponse(Socket socket, String fileName) throws IOException {
        // Path to the resources directory
        String resourcePath = "src/main/resources/";
        File file = new File(resourcePath + fileName);
        
        System.out.println("Looking for file: " + file.getAbsolutePath());
        
        var writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        if (file.exists() && file.isFile()) {
            // Get file extension for MIME type
            String extension = getFileExtension(fileName);
            String contentType = MIME_TYPES.getOrDefault(extension.toLowerCase(), "application/octet-stream");
            
            // Read file content
            byte[] fileContent = Files.readAllBytes(file.toPath());
            int contentLength = fileContent.length;
            
            // Build HTTP response
            StringBuilder response = new StringBuilder();
            response.append("HTTP/1.0 200 OK\r\n");
            response.append("Content-Type: ").append(contentType).append("\r\n");
            response.append("Content-Length: ").append(contentLength).append("\r\n");
            response.append("Connection: close\r\n");
            response.append("\r\n");
            
            // Send headers
            writer.write(response.toString());
            writer.flush();
            
            // Send file content
            if (isTextFile(contentType)) {
                // For text files, send as string
                String textContent = new String(fileContent);
                writer.write(textContent);
            } else {
                // For binary files, send raw bytes
                var outputStream = socket.getOutputStream();
                outputStream.write(fileContent);
                outputStream.flush();
            }
            
            System.out.println("File served successfully: " + fileName + " (Content-Type: " + contentType + ")");
        } else {
            // File not found - send 404 response
            send404Response(writer);
            System.out.println("404 Not Found: " + fileName);
        }
        
        writer.close();
    }

    private void send404Response(BufferedWriter writer) throws IOException {
        String html404 = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>404 - File Not Found</title>
                <style>
                    body { font-family: Arial, sans-serif; text-align: center; margin-top: 50px; }
                    h1 { color: #d32f2f; }
                    .error-code { font-size: 72px; color: #d32f2f; margin-bottom: 20px; }
                    .message { font-size: 18px; color: #666; }
                    .home-link { margin-top: 30px; }
                    .home-link a { color: #2196f3; text-decoration: none; }
                    .home-link a:hover { text-decoration: underline; }
                </style>
            </head>
            <body>
                <div class="error-code">404</div>
                <h1>File Not Found</h1>
                <div class="message">The requested resource could not be found on this server.</div>
                <div class="home-link">
                    <a href="/">Go to Home Page</a>
                </div>
            </body>
            </html>
            """;
        
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.0 404 Not Found\r\n");
        response.append("Content-Type: text/html\r\n");
        response.append("Content-Length: ").append(html404.getBytes().length).append("\r\n");
        response.append("Connection: close\r\n");
        response.append("\r\n");
        response.append(html404);
        
        writer.write(response.toString());
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }

    private boolean isTextFile(String contentType) {
        return contentType.startsWith("text/") || 
               contentType.equals("application/javascript") ||
               contentType.equals("application/json");
    }

    public void init() throws IOException {
        ServerSocket server = new ServerSocket(8082);
        System.out.println("Server started on port 8082");
        System.out.println("Access the server at: http://localhost:8082");
        System.out.println("Supported file types: HTML, JPG, GIF, PNG, CSS, JS, TXT");

        while (true) {
            System.out.println("Waiting for a client...");
            Socket socket = server.accept();
            System.out.println("Client connected from: " + socket.getInetAddress().getHostAddress());
            dispatchWorker(socket);
        }
    }

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.init();
    }
} 