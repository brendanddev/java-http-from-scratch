package brendanddev.routing;

import brendanddev.server.HttpHandler;


/**
 * 
 * @author albanna
*/


/**
 * Represents a single route in the HTTP server.
 */
public class Route {

	private final String method;
	private final String path;
	private final HttpHandler handler;
	private final String description;

	public Route(String method, String path, HttpHandler handler) {
		this(method, path, handler, "");
	}

	public Route(String method, String path, HttpHandler handler, String description) {
		this.method = method.toUpperCase();
		this.path = path;
		this.handler = handler;
		this.description = description;
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public HttpHandler getHandler() {
		return handler;
	}

	public String getDescription() {
		return description;
	}

	public String getRouteKey() {
		return method + " " + path;
	}

	@Override
	public String toString() {
		return String.format("%s %s - %s", method, path, description);
	}
}