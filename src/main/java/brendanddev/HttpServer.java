package brendanddev;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple HTTP server implemented from scratch in Java.
 * 
 * The server listens on a configurable TCP port defaulting to 8080,
 * accepts incoming HTTP requests, and responds with a basic HTML page.
 */
public class HttpServer {

    private int port;

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

            // Determine response to send based on the path
            String response = getResponse(request);
            
            // Sends the HTTP response back to the client
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println("Content-Length: " + response.length());
            out.println();
            out.println(response);
            
            // Flush the output stream to send the response
            // Then close socket to end the connection
            out.flush();
            socket.close();
            System.out.println("Response sent to client.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Determines the appropriate HTTP response based on the request path.
     * Acts as a simple router to return different content for different paths.
     * 
     * @param request The parsed HTTP request.
     * @return A string containing the HTML response content.
     */
    private String getResponse(HttpRequest request) {
        switch(request.getPath()) {
            case "/":
                return "<html><body><h1>Home</h1></body></html>";
            case "/about":
                return "<html><body><h1>About Us</h1></body></html>";
            default:
                return "<html><body><h1>404 Not Found</h1></body></html>";
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
