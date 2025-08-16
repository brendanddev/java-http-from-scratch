package brendanddev.server;

/**
 * Represents an HTTP response, including the response body, status code, and status text.
 * 
 * This class is used by the HTTP server to encapsulate both the content to send to the client
 * and the corresponding HTTP status.
 */
public class HttpResponse {

    String body;
    int statusCode;
    String statusText;

    /**
     * Constructs a new HttpResponse object with the specified body, status code, and status text.
     * 
     * @param body The response content to send to the client.
     * @param statusCode The HTTP status code.
     * @param statusText The HTTP status text corresponding to the status code.
     */
    public HttpResponse(String body, int statusCode, String statusText) {
        this.body = body;
        this.statusCode = statusCode;
        this.statusText = statusText;
    }
    
}
