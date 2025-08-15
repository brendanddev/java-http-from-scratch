package brendanddev;

import java.util.Map;

/**
 * A class representing a parsed HTTP request.
 * 
 * This class stores the main components of an HTTP request, including the HTTP method type,
 * the requested path, the HTTP version, and any headers associated with the request.
 */
public class HttpRequest {

    private final String method;
    private final String path;
    private final String httpVersion;
    private final Map<String, String> headers;

    /**
     * Constructs a new HttpRequest with the specified method, path, HTTP version, and headers.
     * 
     * @param method The HTTP method (e.g., GET, POST)
     * @param path The requested path (e.g., /index.html)
     * @param httpVersion The HTTP version (e.g., HTTP/1.1)
     * @param headers A map of headers associated with the request, where the key is the header name and the value is the header value.
     */
    public HttpRequest(String method, String path, String httpVersion, Map<String, String> headers) {
        this.method = method;
        this.path = path;
        this.httpVersion = httpVersion;
        this.headers = headers;
    }

    // Returns the HTTP method of the request
    public String getMethod() {
        return method;
    }

    // Returns the requested path of the request
    public String getPath() {
        return path;
    }

    // Returns the HTTP version of the request
    public String getHttpVersion() {
        return httpVersion;
    }

    // Returns the headers of the request as a map
    public Map<String, String> getHeaders() {
        return headers;
    }

    // Returns a string representation of the HTTP request
    @Override
    public String toString() {
        return method + " " + path + " " + httpVersion + " " + headers;
    }
    
}
