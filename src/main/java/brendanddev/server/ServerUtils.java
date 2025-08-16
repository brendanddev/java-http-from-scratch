package brendanddev.server;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for server related helper methods and operations.
 */
public class ServerUtils {

    /**
     * Loads an HTML template from the resources/templates directory.
     * 
     * @param templateName The file name of the template to load.
     * @return The content of the template as a String, or an error message if loading fails.
     */
    public static String loadTemplate(String templateName) {
        try {
            Path templatePath = Paths.get(ServerUtils.class.getClassLoader()
                .getResource("templates/" + templateName).toURI());
            return new String(java.nio.file.Files.readAllBytes(templatePath), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return "<h1>Error loading template</h1>";
        }
    }
    
}
