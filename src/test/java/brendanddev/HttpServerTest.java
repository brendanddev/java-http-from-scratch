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
     * Tests that the server can handle multiple clients connecting at the same time.
     * 
     * @throws Exception if the server fails to start, requests cannot be sent,
     *                   or responses cannot be read.
     */
    @Test
    void testMultipleClients() throws Exception {
        
        // Create and configure the server
        HttpServer server = new HttpServer(8080);
        server.addRoute("GET", "/", (req, body) ->
                new HttpResponse("Hello World", 200, "OK"));
        
        // Start the server in a separate daemon thread so it doesn't block the test
        Thread serverThread = new Thread(server::start);
        serverThread.setDaemon(true);
        serverThread.start();

        // Small delay to ensure server is up before sending requests
        Thread.sleep(1000);

        // Number of concurrent clients to simulate
        int clientCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(clientCount);

        // Task that each simulated client will run
        Callable<String> clientTask = () -> sendGetRequest("http://localhost:8080/");

        // Create and run multiple client tasks concurrently
        List<Future<String>> futures = executor.invokeAll(
            java.util.Collections.nCopies(clientCount, clientTask)
        );

        // Verify clients got correct responses
        for (Future<String> future : futures) {
            assertEquals("Hello World", future.get());
        }

        // Shutdown the executor service
        executor.shutdown();
    }

    /**
     * Stress test for the HttpServer to simulate many clients connecting simultaneously.
     * 
     * This test demonstrates that the current server implementation, which handles each client sequentially,
     * will fail under high concurrency. Without multithreading, the server cannot handle multiple simultaneous 
     * connections, resulting in "Connection refused" errors.
     * 
     * @throws Exception if the server fails to start, requests cannot be sent,
     *                   or responses cannot be read.
     */
    @Test
    void stressTestMultipleClients() throws Exception {
        // Start the server
        HttpServer server = new HttpServer(8080);
        server.addRoute("GET", "/", (req, body) ->
                new HttpResponse("Hello World", 200, "OK"));

        Thread serverThread = new Thread(server::start);
        serverThread.setDaemon(true);
        serverThread.start();

        waitForServer(8080);

        int clientCount = 300;
        ExecutorService executor = Executors.newFixedThreadPool(100);
        Callable<String> clientTask = () -> sendGetRequest("http://localhost:8080/");

        long startTime = System.currentTimeMillis();

        List<Future<String>> futures = executor.invokeAll(
                java.util.Collections.nCopies(clientCount, clientTask)
        );

        long endTime = System.currentTimeMillis();
        System.out.println("All requests completed in " + (endTime - startTime) + " ms");

        // Verify all responses
        for (Future<String> future : futures) {
            assertEquals("Hello World", future.get());
        }

        executor.shutdown();
    }

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

    /**
     * Waits for the server to start listening on the given port.
     * 
     * It attempts to connect multiple times (50 attempts with 100ms intervals) before giving up.
     * Its a utility to ensure that stress tests start only after the server is ready to accept connections.
     * 
     * @param port The port to check for server readiness.
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    private void waitForServer(int port) throws InterruptedException {
        // Try connecting 50 times
        for (int i = 0; i < 50; i++) { 
            try (java.net.Socket s = new java.net.Socket("localhost", port)) {
                return; // connected successfully
            } catch (java.io.IOException e) {
                Thread.sleep(100); // wait 100ms before retrying
            }
        }
        throw new RuntimeException("Server did not start in time");
    }

    
}
