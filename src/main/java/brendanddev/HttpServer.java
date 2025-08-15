package brendanddev;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {


    public static void main(String[] args) {
        try {

            // Create a server socket that listens on port 8080
            // This is the port where the server will accept incoming connections
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server is listening on port 8080");

            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());

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
