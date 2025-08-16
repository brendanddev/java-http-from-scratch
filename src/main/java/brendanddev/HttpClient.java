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
     * Sends a GET request and returns only the response body, excluding headers.
     * 
     * @param path The path on the server to which the GET request is sent.
     * @return The body of the HTTP response from the server.
     * @throws Exception If an I/O error occurs during the request.
     */
    public String get(String path) throws Exception {
        String rawResponse = getRaw(path);

        // Split response into headers and body
        int seperatorIndex = rawResponse.indexOf("\r\n\r\n");
        if (seperatorIndex == -1) {
            throw new RuntimeException("Invalid HTTP response: No header-body separator found.");
        }
        // Return everything after blank like (body)
        return rawResponse.substring(seperatorIndex + 4);
    }


    /**
     * Sends a basic HTTP GET request to the specified path on the server and returns
     * the raw response as a string.
     * 
     * @param path The path on the server to which the GET request is sent.
     * @return The raw HTTP response from the server.
     * @throws Exception If an I/O error occurs during the request.
     */
    public String getRaw(String path) throws Exception {
        // Establishes a socket connection to the server
        try (Socket socket = new Socket(host, port)) {
            // Sets up input and output streams for communication with the server
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

    /**
     * Sends a HTTP POST request with a body and returns only the response body.
     * 
     * @param path The path on the server to which the POST request is sent.
     * @param body The body of the POST request to be sent to the server.
     * @param contentType The content type of the body (e.g., "application/json").
     *                    This is used to set the Content-Type header in the request.
     * @return The body of the HTTP response from the server.
     * @throws Exception If an I/O error occurs during the request.
     */
    public String post(String path, String body, String contentType) throws Exception {
        
        // Establishes a socket connection to the server
        try (Socket socket = new Socket(host, port)) {
            // Sets up input and output streams for communication with the server
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send POST request line and headers
            out.print("POST " + path + " HTTP/1.1\r\n");
            out.print("Host: " + host + "\r\n");
            out.print("Connection: close\r\n");
            out.print("Content-Type: " + contentType + "\r\n");
            out.print("Content-Length: " + body.length() + "\r\n");
            out.print("\r\n");

            // Send the request body
            out.print(body);
            out.flush();

            // Read response
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }

            // Return only the body of the response
            int seperatorIndex = response.indexOf("\r\n\r\n");
            if (seperatorIndex == -1) {
                throw new RuntimeException("Invalid HTTP response: No header-body separator found.");
            }
            return response.substring(seperatorIndex + 4);
        }
    }



    
}
