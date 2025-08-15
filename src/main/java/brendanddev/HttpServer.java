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

    /**
     * Main entry point to start the HTTP server.
     * @param args Unused.
     */
    public static void main(String[] args) {
        try {

            // Create a server socket that listens on port 8080
            // This is the port where the server will accept incoming connections
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server is listening on port 8080");

            // Loop to keep server running
            while (true) {
                // Accept incoming client connection
                Socket socket = serverSocket.accept();

                // Reader for incoming HTTP request
                // Writer for sending HTTP response
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                // Reads first line of the HTTP request
                String requestLine = in.readLine();
                System.out.println("Received request: " + requestLine);

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

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
