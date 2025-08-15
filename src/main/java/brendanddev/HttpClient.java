package brendanddev;

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


    public String get(String path) throws Exception {

        try (Socket socket = new Socket(host, port)) {
            
        }

    }
    
}
