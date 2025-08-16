package brendanddev.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import brendanddev.model.HttpRequest;

/**
 * A simple HTTP server implemented from scratch in Java.
 * 
 * The server listens on a configurable TCP port defaulting to 8080,
 * accepts incoming HTTP requests, and responds with a basic HTML page.
 */
public class HttpServer {

    private int port;
    private final Map<String, HttpHandler> routes = new HashMap<>();
    

    // Constructs an HttpServer that listens on the specified port
    public HttpServer(int port) {
        this.port = port;
    }

    /**
     * Starts the HTTP server.
     */
    public void start() {
        // Create a server socket that listens on the specified port
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            // Loop to keep server running
            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a route to the server with a specific HTTP method and path.
     * 
     * @param method The HTTP method.
     * @param path The route path.
     * @param handler The HttpHandler to handle requests to this route.
     */
    public void addRoute(String method, String path, HttpHandler handler) {
        routes.put(method.toUpperCase() + " " + path, handler);
    }


    /**
     * Handles a single client connection by reading the HTTP request and sending a simple HTTP response.
     * 
     * @param socket The client socket to handle.
     */
    private void handleClient(Socket socket) {
        try (
            // Reader for reading incoming HTTP requests from the client
            // Writer for sending HTTP responses to the client
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
        ) {
            // Parse incoming HTTP request into an HttpRequest object
            HttpRequest request = parseRequest(in);
            System.out.println("Received request: " + request);

            // Read body for POST/PUT requests if content-length header is set
            int contentLength = 0;
            String contentLengthHeader = request.getHeaders().get("Content-Length");
            if (contentLengthHeader != null) {
                contentLength = Integer.parseInt(contentLengthHeader);
            }

            // Prepare char array to store the request body
            char[] bodyChars = new char[contentLength];
            if (contentLength > 0) {
                // Read body from the input stream
                in.read(bodyChars, 0, contentLength);
            }
            // Convert char array to String
            String body = new String(bodyChars);

            // Lookup the registered route handler for this HTTP method and path
            HttpHandler handler = routes.get(request.getMethod().toUpperCase() + " " + request.getPath());
            HttpResponse response;
            
            if (handler != null) {
                // If a handler exists for the request, use it to generate a response
                response = handler.handle(request, body);
            } else {
                // If no handler found, return 404 Not Found
                response = new HttpResponse("<h1>404 Not Found</h1>", 404, "Not Found");
            }
            
            // Convert body to bytes
            byte[] bodyBytes = response.body.getBytes("UTF-8");


            // Send headers with CRLF
            out.print("HTTP/1.1 " + response.statusCode + " " + response.statusText + "\r\n");
            out.print("Content-Type: text/html; charset=UTF-8\r\n");
            out.print("Content-Length: " + bodyBytes.length + "\r\n");
            out.print("\r\n");
            out.flush();

            // Send body
            OutputStream os = socket.getOutputStream();
            os.write(bodyBytes);
            os.flush();

            // Close connection
            socket.close();
            System.out.println("Response sent to client.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Parses the HTTP request from the input stream and constructs an HttpRequest object.
     * 
     * @param in The BufferedReader connected to the client socket.
     * @return HttpRequest object containing the parsed request data.
     * @throws Exception If an error occurs while reading the request.
     */
    private HttpRequest parseRequest(BufferedReader in) throws Exception {

        // Reads the request line from the input stream
        String requestLine = in.readLine();

        // Splits the request line into parts
        String[] requestParts = requestLine.split(" ");
        String method = requestParts[0];
        String path = requestParts[1];
        String httpVersion = requestParts[2];

        // Read headers until a blank line is encountered
        Map<String, String> headers = new HashMap<>();
        String line;
        while (!(line = in.readLine()).isEmpty()) {
            int colonIndex = line.indexOf(":");
            headers.put(line.substring(0, colonIndex).trim(),
                    line.substring(colonIndex + 1).trim());
        }

        // Return a fully populated HttpRequest object
        return new HttpRequest(method, path, httpVersion, headers);
    }








    
}
