package brendanddev;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A simple HTTP client implemented from scratch using sockets
 */
public class HttpClient {

    private String host;
    private int port;

    /**
     * Constructs a new HttpClient that connects to the specified host and port.
     * 
     * @param host The hostname or IP address of the server to connect to.
     * @param port The port number on which the server is listening.
     */
    public HttpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Sends a basic HTTP GET request to the specified path on the server and returns
     * the raw response as a string.
     * 
     * @param path The path on the server to which the GET request is sent.
     * @return The raw HTTP response from the server.
     * @throws Exception If an I/O error occurs during the request.
     */
    public String get(String path) throws Exception {
        // Establishes a socket connection to the server
        try (Socket socket = new Socket(host, port)) {
            // 
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Sends raw HTTP GET request
            out.print("GET " + path + " HTTP/1.1\r\n");
            out.print("Host: " + host + "\r\n");
            out.print("Connection: close\r\n");
            out.print("\r\n");
            out.flush();

            // Read response
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }
            return response.toString();
        }
    }
    
}
