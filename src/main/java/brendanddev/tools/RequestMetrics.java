package brendanddev.tools;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author albanna
*/

/**
 * Singleton service for tracking HTTP request metrics and statistics.
 */
public class RequestMetrics {

	private static RequestMetrics instance;
	private final ConcurrentHashMap<String, AtomicLong> requestCounts = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, AtomicLong> responseTimes = new ConcurrentHashMap<>();
	private final AtomicLong totalRequests = new AtomicLong(0);
	private final Logger logger = Logger.getInstance();

	private RequestMetrics() {
	}

	/**
	 * Gets the singleton instance of RequestMetrics.
	 */
	public static synchronized RequestMetrics getInstance() {
		if (instance == null) {
			instance = new RequestMetrics();
		}
		return instance;
	}

	/**
	 * Records a request for the given endpoint.
	 */
	public void recordRequest(String method, String path, long responseTimeMs) {
		String endpoint = method + " " + path;
		requestCounts.computeIfAbsent(endpoint, k -> new AtomicLong(0)).incrementAndGet();
		responseTimes.computeIfAbsent(endpoint, k -> new AtomicLong(0)).addAndGet(responseTimeMs);
		totalRequests.incrementAndGet();

		logger.debug("Request recorded: " + endpoint + " (" + responseTimeMs + "ms)");
	}

	/**
	 * Gets the total number of requests processed.
	 */
	public long getTotalRequests() {
		return totalRequests.get();
	}

	/**
	 * Gets the request count for a specific endpoint.
	 */
	public long getRequestCount(String method, String path) {
		String endpoint = method + " " + path;
		AtomicLong count = requestCounts.get(endpoint);
		return count != null ? count.get() : 0;
	}

	/**
	 * Gets the average response time for a specific endpoint.
	 */
	public double getAverageResponseTime(String method, String path) {
		String endpoint = method + " " + path;
		AtomicLong count = requestCounts.get(endpoint);
		AtomicLong totalTime = responseTimes.get(endpoint);

		if (count == null || totalTime == null || count.get() == 0) {
			return 0.0;
		}

		return (double) totalTime.get() / count.get();
	}

	/**
	 * Prints current metrics to the logger.
	 */
	public void printMetrics() {
		logger.info("=== Request Metrics ===");
		logger.info("Total Requests: " + totalRequests.get());

		requestCounts.forEach((endpoint, count) -> {
			double avgTime = getAverageResponseTime(endpoint.split(" ")[0], endpoint.split(" ", 2)[1]);
			logger.info(String.format("%s: %d requests, %.2fms avg", endpoint, count.get(), avgTime));
		});
	}
}