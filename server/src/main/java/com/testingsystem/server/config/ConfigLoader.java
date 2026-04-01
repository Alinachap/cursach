package com.testingsystem.server.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Logger logger = LogManager.getLogger(ConfigLoader.class);

    private static final String CONFIG_FILE = "/server.properties";

    private final Properties properties;

    public ConfigLoader() throws IOException {
        properties = new Properties();
        loadConfiguration();
    }

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

    private void setDefaultProperties() {
        properties.setProperty("server.port", "12345");
        properties.setProperty("server.maxThreads", "10");
        properties.setProperty("db.url", "jdbc:postgresql://localhost:5432/testingsystem");
        properties.setProperty("db.user", "postgres");
        properties.setProperty("db.password", "password");
        properties.setProperty("db.poolSize", "5");
        logger.info("Default configuration set");
    }

    public String getString(String key) {
        return properties.getProperty(key);
    }

    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getInt(String key) {
        String value = properties.getProperty(key);
        return Integer.parseInt(value);
    }

    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public int getServerPort() {
        return getInt("server.port", 12345);
    }

    public int getMaxThreads() {
        return getInt("server.maxThreads", 10);
    }

    public String getDbUrl() {
        return getString("db.url", "jdbc:postgresql://localhost:5432/testingsystem");
    }

    public String getDbUser() {
        return getString("db.user", "postgres");
    }

    public String getDbPassword() {
        return getString("db.password", "password");
    }

    public int getDbPoolSize() {
        return getInt("db.poolSize", 5);
    }
}
