package brendanddev;

/**
 * The main entry point for the HTTP server application.
 */
public class Main {


    /**
     * Main method to start the HTTP server
     * @param args Unused.
     */
    public static void main(String[] args) {
        HttpServer server = new HttpServer(8080);
        server.start();
    }
    
}
