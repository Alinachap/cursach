package com.testingsystem.server.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Server configuration loader.
 * Loads configuration from server.properties file.
 */
public class ConfigLoader {
    private static final Logger logger = LogManager.getLogger(ConfigLoader.class);
    
    private static final String CONFIG_FILE = "/server.properties";
    
    private final Properties properties;

    /**
     * Constructs a ConfigLoader and loads configuration.
     *
     * @throws IOException if configuration file not found
     */
    public ConfigLoader() throws IOException {
        properties = new Properties();
        loadConfiguration();
    }

    /**
     * Loads configuration from properties file.
     *
     * @throws IOException if file not found or cannot be read
     */
    private void loadConfiguration() throws IOException {
        try (InputStream input = getClass().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                logger.warn("Configuration file not found: {}", CONFIG_FILE);
                setDefaultProperties();
                return;
            }
            properties.load(input);
            logger.info("Configuration loaded from {}", CONFIG_FILE);
        }
    }

    /**
     * Sets default properties when config file is not found.
     */
    private void setDefaultProperties() {
        properties.setProperty("server.port", "12345");
        properties.setProperty("server.maxThreads", "10");
        properties.setProperty("db.url", "jdbc:postgresql://localhost:5432/testingsystem");
        properties.setProperty("db.user", "postgres");
        properties.setProperty("db.password", "password");
        properties.setProperty("db.poolSize", "5");
        logger.info("Default configuration set");
    }

    /**
     * Gets a string property.
     *
     * @param key the property key
     * @return the property value
     */
    public String getString(String key) {
        return properties.getProperty(key);
    }

    /**
     * Gets a string property with default value.
     *
     * @param key the property key
     * @param defaultValue the default value
     * @return the property value or default
     */
    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Gets an integer property.
     *
     * @param key the property key
     * @return the property value as integer
     * @throws NumberFormatException if value cannot be parsed
     */
    public int getInt(String key) {
        String value = properties.getProperty(key);
        return Integer.parseInt(value);
    }

    /**
     * Gets an integer property with default value.
     *
     * @param key the property key
     * @param defaultValue the default value
     * @return the property value as integer or default
     */
    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    /**
     * Gets the server port.
     *
     * @return the server port
     */
    public int getServerPort() {
        return getInt("server.port", 12345);
    }

    /**
     * Gets the maximum number of threads.
     *
     * @return the max threads
     */
    public int getMaxThreads() {
        return getInt("server.maxThreads", 10);
    }

    /**
     * Gets the database URL.
     *
     * @return the database URL
     */
    public String getDbUrl() {
        return getString("db.url", "jdbc:postgresql://localhost:5432/testingsystem");
    }

    /**
     * Gets the database username.
     *
     * @return the database username
     */
    public String getDbUser() {
        return getString("db.user", "postgres");
    }

    /**
     * Gets the database password.
     *
     * @return the database password
     */
    public String getDbPassword() {
        return getString("db.password", "password");
    }

    /**
     * Gets the database pool size.
     *
     * @return the pool size
     */
    public int getDbPoolSize() {
        return getInt("db.poolSize", 5);
    }
}
