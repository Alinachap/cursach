package com.testingsystem.server.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Server configuration using Singleton pattern.
 * Provides centralized access to server configuration.
 */
public class ServerConfig {
    private static final Logger logger = LogManager.getLogger(ServerConfig.class);
    
    private static volatile ServerConfig instance;
    
    private final ConfigLoader configLoader;

    /**
     * Private constructor for Singleton pattern.
     *
     * @throws IOException if configuration cannot be loaded
     */
    private ServerConfig() throws IOException {
        this.configLoader = new ConfigLoader();
        logger.info("ServerConfig initialized");
    }

    /**
     * Gets the singleton instance of ServerConfig.
     *
     * @return the ServerConfig instance
     * @throws RuntimeException if initialization fails
     */
    public static ServerConfig getInstance() {
        if (instance == null) {
            synchronized (ServerConfig.class) {
                if (instance == null) {
                    try {
                        instance = new ServerConfig();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to initialize ServerConfig", e);
                    }
                }
            }
        }
        return instance;
    }

    /**
     * Gets the server port.
     *
     * @return the server port
     */
    public int getServerPort() {
        return configLoader.getServerPort();
    }

    /**
     * Gets the maximum number of threads.
     *
     * @return the max threads
     */
    public int getMaxThreads() {
        return configLoader.getMaxThreads();
    }

    /**
     * Gets the database URL.
     *
     * @return the database URL
     */
    public String getDbUrl() {
        return configLoader.getDbUrl();
    }

    /**
     * Gets the database username.
     *
     * @return the database username
     */
    public String getDbUser() {
        return configLoader.getDbUser();
    }

    /**
     * Gets the database password.
     *
     * @return the database password
     */
    public String getDbPassword() {
        return configLoader.getDbPassword();
    }

    /**
     * Gets the database pool size.
     *
     * @return the pool size
     */
    public int getDbPoolSize() {
        return configLoader.getDbPoolSize();
    }

    /**
     * Gets a string property.
     *
     * @param key the property key
     * @return the property value
     */
    public String getString(String key) {
        return configLoader.getString(key);
    }

    /**
     * Gets a string property with default value.
     *
     * @param key the property key
     * @param defaultValue the default value
     * @return the property value or default
     */
    public String getString(String key, String defaultValue) {
        return configLoader.getString(key, defaultValue);
    }

    /**
     * Gets an integer property.
     *
     * @param key the property key
     * @return the property value as integer
     */
    public int getInt(String key) {
        return configLoader.getInt(key);
    }

    /**
     * Gets an integer property with default value.
     *
     * @param key the property key
     * @param defaultValue the default value
     * @return the property value as integer or default
     */
    public int getInt(String key, int defaultValue) {
        return configLoader.getInt(key, defaultValue);
    }

    /**
     * Resets the singleton instance (for testing).
     */
    public static synchronized void resetInstance() {
        instance = null;
        logger.info("ServerConfig instance reset");
    }
}
