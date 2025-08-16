package brendanddev.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 
 * @author albanna
*/

/**
 * Singleton Logger service for centralized logging across the HTTP server
 * project. Provides different log levels and can write to both console and
 * file.
 */
public class Logger {

	public enum LogLevel {
		DEBUG, INFO, WARN, ERROR
	}

	private static Logger instance;
	private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private LogLevel minLogLevel = LogLevel.INFO;
	private boolean enableFileLogging = false;
	private String logFilePath = "server.log";

	private Logger() {
	}

	/**
	 * Gets the singleton instance of the Logger.
	 */
	public static synchronized Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}

	/**
	 * Sets the minimum log level to display.
	 */
	public void setLogLevel(LogLevel level) {
		this.minLogLevel = level;
	}

	/**
	 * Enables or disables file logging.
	 */
	public void setFileLogging(boolean enable, String filePath) {
		this.enableFileLogging = enable;
		if (filePath != null) {
			this.logFilePath = filePath;
		}
	}

	public void debug(String message) {
		log(LogLevel.DEBUG, message);
	}

	public void info(String message) {
		log(LogLevel.INFO, message);
	}

	public void warn(String message) {
		log(LogLevel.WARN, message);
	}

	public void error(String message) {
		log(LogLevel.ERROR, message);
	}

	public void error(String message, Throwable throwable) {
		log(LogLevel.ERROR, message + " - " + throwable.getMessage());
		if (shouldLog(LogLevel.ERROR)) {
			throwable.printStackTrace();
		}
	}

	private void log(LogLevel level, String message) {
		if (!shouldLog(level)) {
			return;
		}

		String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
		String logMessage = String.format("[%s] %s: %s", timestamp, level.name(), message);

		// Console logging
		System.out.println(logMessage);

		// File logging
		if (enableFileLogging) {
			writeToFile(logMessage);
		}
	}

	private boolean shouldLog(LogLevel level) {
		return level.ordinal() >= minLogLevel.ordinal();
	}

	private void writeToFile(String message) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath, true))) {
			writer.println(message);
		} catch (IOException e) {
			System.err.println("Failed to write to log file: " + e.getMessage());
		}
	}
}
