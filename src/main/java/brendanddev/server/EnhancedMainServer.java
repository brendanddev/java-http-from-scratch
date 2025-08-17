package brendanddev.server;

import brendanddev.routing.RouteRegistry;
import brendanddev.tools.ConfigManager;
import brendanddev.tools.Logger;
import brendanddev.tools.RequestMetrics;

/**
 * @author albanna
 */

/**
 * Enhanced main server class demonstrating the new tools and routing features.
 * This shows how to use the new packages without modifying existing code.
 */
public class EnhancedMainServer {

	public static void main(String[] args) {
		// Initialize logging
		Logger logger = Logger.getInstance();
		logger.setLogLevel(Logger.LogLevel.DEBUG);
		logger.setFileLogging(true, "enhanced-server.log");
		logger.info("Starting Enhanced HTTP Server...");

		// Load configuration
		ConfigManager config = ConfigManager.getInstance();
		int port = config.getIntProperty("server.properties", "server.port", 9090);
		boolean enableMetrics = config.getBooleanProperty("server.properties", "metrics.enabled", true);

		// Create server
		HttpServer server = new HttpServer(port);

		// Initialize metrics if enabled
		RequestMetrics metrics = RequestMetrics.getInstance();

		// Create centralized route registry
		RouteRegistry routes = new RouteRegistry();

		// Register existing routes with descriptions
		routes.get("/", (request, body) -> {
			long startTime = System.currentTimeMillis();
			HttpResponse response = new HttpResponse(ServerUtils.loadTemplate("index.html"), 200, "OK");
			if (enableMetrics) {
				metrics.recordRequest("GET", "/", System.currentTimeMillis() - startTime);
			}
			return response;
		}, "Home page");

		routes.get("/about", (request, body) -> {
			long startTime = System.currentTimeMillis();
			HttpResponse response = new HttpResponse(ServerUtils.loadTemplate("about.html"), 200, "OK");
			if (enableMetrics) {
				metrics.recordRequest("GET", "/about", System.currentTimeMillis() - startTime);
			}
			return response;
		}, "About page");

		routes.post("/submit", (request, body) -> {
			long startTime = System.currentTimeMillis();
			logger.info("Received POST data: " + body);
			HttpResponse response = new HttpResponse(
					ServerUtils.loadTemplate("submit.html").replace("{{message}}", body), 200, "OK");
			if (enableMetrics) {
				metrics.recordRequest("POST", "/submit", System.currentTimeMillis() - startTime);
			}
			return response;
		}, "Submit form data");

		// Register new utility routes
		routes.get("/api/docs", routes.createApiDocsHandler(), "API Documentation");

		routes.get("/api/metrics", (request, body) -> {
			if (!enableMetrics) {
				return new HttpResponse("Metrics disabled", 404, "Not Found");
			}

			StringBuilder html = new StringBuilder();
			html.append("<html><head><title>Server Metrics</title></head><body>");
			html.append("<h1>Server Metrics</h1>");
			html.append("<p>Total Requests: ").append(metrics.getTotalRequests()).append("</p>");
			html.append("<h2>Endpoint Statistics:</h2>");
			html.append("<table border='1' style='border-collapse: collapse;'>");
			html.append("<tr><th>Endpoint</th><th>Count</th><th>Avg Response Time (ms)</th></tr>");

			routes.getAllRoutes().forEach(route -> {
				long count = metrics.getRequestCount(route.getMethod(), route.getPath());
				double avgTime = metrics.getAverageResponseTime(route.getMethod(), route.getPath());
				html.append("<tr>");
				html.append("<td>").append(route.getRouteKey()).append("</td>");
				html.append("<td>").append(count).append("</td>");
				html.append("<td>").append(String.format("%.2f", avgTime)).append("</td>");
				html.append("</tr>");
			});

			html.append("</table></body></html>");
			return new HttpResponse(html.toString(), 200, "OK");
		}, "Server metrics and statistics");

		routes.get("/api/health", (request, body) -> {
			return new HttpResponse("{\"status\":\"OK\",\"timestamp\":" + System.currentTimeMillis() + "}", 200, "OK");
		}, "Health check endpoint");

		// Apply all routes to the server
		routes.applyTo(server);

		// Print registered routes
		logger.info("Registered " + routes.getAllRoutes().size() + " routes:");
		routes.getAllRoutes().forEach(route -> logger.info("  " + route));

		// Start server
		logger.info("Server starting on port " + port);
		server.start();
	}
}