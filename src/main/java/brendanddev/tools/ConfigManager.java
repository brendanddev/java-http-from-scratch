package brendanddev.tools;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author albanna
*/


/**
 * Singleton Configuration Manager for handling application properties. Supports
 * multiple property files and environment-specific configurations.
 */
public class ConfigManager {

	private static ConfigManager instance;
	private final ConcurrentHashMap<String, Properties> configCache = new ConcurrentHashMap<>();
	private final Logger logger = Logger.getInstance();

	private ConfigManager() {
	}

	/**
	 * Gets the singleton instance of the ConfigManager.
	 */
	public static synchronized ConfigManager getInstance() {
		if (instance == null) {
			instance = new ConfigManager();
		}
		return instance;
	}

	/**
	 * Loads a properties file from the classpath.
	 */
	public Properties loadConfig(String configFileName) {
		return configCache.computeIfAbsent(configFileName, this::loadPropertiesFile);
	}

	private Properties loadPropertiesFile(String fileName) {
		Properties props = new Properties();
		try (InputStream in = getClass().getResourceAsStream("/" + fileName)) {
			if (in != null) {
				props.load(in);
				logger.info("Loaded configuration file: " + fileName);
			} else {
				logger.warn("Configuration file not found: " + fileName);
			}
		} catch (Exception e) {
			logger.error("Failed to load configuration file: " + fileName, e);
		}
		return props;
	}

	/**
	 * Gets a property value with a default fallback.
	 */
	public String getProperty(String configFile, String key, String defaultValue) {
		Properties props = loadConfig(configFile);
		return props.getProperty(key, defaultValue);
	}

	/**
	 * Gets an integer property with a default fallback.
	 */
	public int getIntProperty(String configFile, String key, int defaultValue) {
		String value = getProperty(configFile, key, String.valueOf(defaultValue));
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			logger.warn("Invalid integer property: " + key + "=" + value + ", using default: " + defaultValue);
			return defaultValue;
		}
	}

	/**
	 * Gets a boolean property with a default fallback.
	 */
	public boolean getBooleanProperty(String configFile, String key, boolean defaultValue) {
		String value = getProperty(configFile, key, String.valueOf(defaultValue));
		return Boolean.parseBoolean(value);
	}
}