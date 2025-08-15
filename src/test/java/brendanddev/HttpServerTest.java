package brendanddev;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Unit tests for the HttpServer class
 */
public class HttpServerTest {

    /**
     * Sends a simple HTTP GET request to the given URL and returns the first line of the response.
     * 
     * @param urlStr The URL to send the GET request to.
     * @return The first line of the response body.
     * @throws Exception If an error occurs while sending the request or reading the response.
     */
    private String sendGetRequest(String urlStr) throws Exception {
        // Create URL object and open connection
        URL url = java.net.URI.create(urlStr).toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // Read one line of the response
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            return in.readLine();
        }
    }

    
}
