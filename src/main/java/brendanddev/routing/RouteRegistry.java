package brendanddev.routing;

import brendanddev.server.HttpHandler;
import brendanddev.server.HttpResponse;
import brendanddev.tools.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author albanna
*/

/**
 * Centralized route registry for managing HTTP routes. Provides a clean way to
 * register, organize, and document API endpoints.
 */
public class RouteRegistry {

	private final Map<String, Route> routes = new HashMap<>();
	private final Logger logger = Logger.getInstance();

	/**
	 * Registers a new route with the registry.
	 */
	public RouteRegistry register(String method, String path, HttpHandler handler) {
		return register(method, path, handler, "");
	}

	/**
	 * Registers a new route with description.
	 */
	public RouteRegistry register(String method, String path, HttpHandler handler, String description) {
		Route route = new Route(method, path, handler, description);
		routes.put(route.getRouteKey(), route);
		logger.info("Registered route: " + route);
		return this; // For method chaining
	}

	/**
	 * Registers a GET route.
	 */
	public RouteRegistry get(String path, HttpHandler handler) {
		return register("GET", path, handler);
	}

	/**
	 * Registers a GET route with description.
	 */
	public RouteRegistry get(String path, HttpHandler handler, String description) {
		return register("GET", path, handler, description);
	}

	/**
	 * Registers a POST route.
	 */
	public RouteRegistry post(String path, HttpHandler handler) {
		return register("POST", path, handler);
	}

	/**
	 * Registers a POST route with description.
	 */
	public RouteRegistry post(String path, HttpHandler handler, String description) {
		return register("POST", path, handler, description);
	}

	/**
	 * Registers a PUT route.
	 */
	public RouteRegistry put(String path, HttpHandler handler) {
		return register("PUT", path, handler);
	}

	/**
	 * Registers a DELETE route.
	 */
	public RouteRegistry delete(String path, HttpHandler handler) {
		return register("DELETE", path, handler);
	}

	/**
	 * Gets a route handler for the given method and path.
	 */
	public HttpHandler getHandler(String method, String path) {
		Route route = routes.get(method.toUpperCase() + " " + path);
		return route != null ? route.getHandler() : null;
	}

	/**
	 * Gets all registered routes.
	 */
	public List<Route> getAllRoutes() {
		return new ArrayList<>(routes.values());
	}

	/**
	 * Creates an API documentation handler that lists all routes.
	 */
	public HttpHandler createApiDocsHandler() {
		return (request, body) -> {
			StringBuilder html = new StringBuilder();
			html.append("<html><head><title>API Documentation</title></head><body>");
			html.append("<h1>API Endpoints</h1>");
			html.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");
			html.append("<tr><th>Method</th><th>Path</th><th>Description</th></tr>");

			getAllRoutes().forEach(route -> {
				html.append("<tr>");
				html.append("<td>").append(route.getMethod()).append("</td>");
				html.append("<td>").append(route.getPath()).append("</td>");
				html.append("<td>").append(route.getDescription()).append("</td>");
				html.append("</tr>");
			});

			html.append("</table></body></html>");
			return new HttpResponse(html.toString(), 200, "OK");
		};
	}

	/**
	 * Applies all registered routes to an HttpServer.
	 */
	public void applyTo(brendanddev.server.HttpServer server) {
		routes.values().forEach(route -> {
			server.addRoute(route.getMethod(), route.getPath(), route.getHandler());
		});
		logger.info("Applied " + routes.size() + " routes to server");
	}
}
