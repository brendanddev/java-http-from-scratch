package brendanddev.client;

import java.io.InputStream;
import java.util.Properties;

/**
 * Main entry point for accessing the HTTP client.
 */
public class MainClient {

    public static void main(String[] args) {

        try {
            // Load client properties from the configuration file
            Properties props = new Properties();
            try (InputStream in = MainClient.class.getResourceAsStream("/client.properties")) {
                props.load(in);
            }

            String host = props.getProperty("server.host", "localhost");
            int port = Integer.parseInt(props.getProperty("server.port", "9080"));

            // Create instance of HttpClient with the loaded properties
            HttpClient client = new HttpClient(host, port);

            // Try GET request to the root path
            String response = client.get("/");
            System.out.println("Response from server:");
            System.out.println(response);

            // Try POST request to the submit path
            String postResponse = client.post("/submit", "Hello from client!", "text/plain");
            System.out.println("Response from POST request:");
            System.out.println(postResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
