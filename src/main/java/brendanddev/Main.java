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

        // GET route
        server.addRoute("GET", "/", (requestm, body) ->
            new HttpResponse("<h1>Welcome to the Home Page!</h1>", 200, "OK")
        );

        // POST route
        server.addRoute("POST", "/submit", (request, body) -> 
            new HttpResponse("<h1>Received POST: " + body + "</h1>", 200, "OK")        
        );

        // Start server
        server.start();
    }
    
}
