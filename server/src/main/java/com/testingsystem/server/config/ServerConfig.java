package com.testingsystem.server.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ServerConfig {
    private static final Logger logger = LogManager.getLogger(ServerConfig.class);

    private static volatile ServerConfig instance;

    private final ConfigLoader configLoader;

    private ServerConfig() throws IOException {
        this.configLoader = new ConfigLoader();
        logger.info("ServerConfig initialized");
    }

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

    public int getServerPort() {
        return configLoader.getServerPort();
    }

    public int getMaxThreads() {
        return configLoader.getMaxThreads();
    }

    public String getDbUrl() {
        return configLoader.getDbUrl();
    }

    public String getDbUser() {
        return configLoader.getDbUser();
    }

    public String getDbPassword() {
        return configLoader.getDbPassword();
    }

    public int getDbPoolSize() {
        return configLoader.getDbPoolSize();
    }

    public String getString(String key) {
        return configLoader.getString(key);
    }

    public String getString(String key, String defaultValue) {
        return configLoader.getString(key, defaultValue);
    }

    public int getInt(String key) {
        return configLoader.getInt(key);
    }

    public int getInt(String key, int defaultValue) {
        return configLoader.getInt(key, defaultValue);
    }

    public static synchronized void resetInstance() {
        instance = null;
        logger.info("ServerConfig instance reset");
    }
}
