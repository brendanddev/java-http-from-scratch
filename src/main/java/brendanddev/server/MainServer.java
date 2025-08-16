package brendanddev.server;

/**
 * The main entry point for the HTTP server application.
 */
public class MainServer {


    /**
     * Main method to start the HTTP server
     * @param args Unused.
     */
    public static void main(String[] args) {
        HttpServer server = new HttpServer(8080);

        // GET route
        server.addRoute("GET", "/", (request, body) ->
            new HttpResponse(ServerUtils.loadTemplate("index.html"), 200, "OK")
        );

        server.addRoute("GET", "/about", (req, body) ->
            new HttpResponse(ServerUtils.loadTemplate("about.html"), 200, "OK")
        );
       
        // POST route
        server.addRoute("POST", "/submit", (req, body) ->
            new HttpResponse(ServerUtils.loadTemplate("submit.html").replace("{{message}}", body), 200, "OK")
        );

        // Start server
        server.start();
    }
    
}
