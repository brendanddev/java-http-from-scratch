package brendanddev;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
            // Reader for reading incoming HTTP requests
            // Write for sending HTTP responses
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
        ) {
            // Read the request line
            String requestLine = in.readLine();
            System.out.println("Received request: " + requestLine);

            // Response to send to the client
            String response = "<html><body><h1>Hello, World!</h1></body></html>";
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







    
    
}
