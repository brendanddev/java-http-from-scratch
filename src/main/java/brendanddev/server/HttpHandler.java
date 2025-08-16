package brendanddev.server;

import brendanddev.model.HttpRequest;

/**
 * Functional interface representing a handler for processing HTTP requests.
 * 
 * Implementations of this interface define how to process an incoming HTTP request
 * and generate an appropriate HTTP response. This allows the server to support multiple
 * HTTP methods such as GET or POST, and routes in a clean, extensible way.
 */
@FunctionalInterface
public interface HttpHandler {

    /**
     * Handles an HTTP request and generates an HTTP response.
     * 
     * @param request The HttpRequest object containing the request method, path, and headers.
     * @param body The request body, if any. Can be null if no body.
     * @return A HttpResponse object containing the content, status code, and status text to send back to the client.
     */
    HttpResponse handle(HttpRequest request, String body);
    
}
