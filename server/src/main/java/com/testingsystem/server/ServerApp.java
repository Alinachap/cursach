package com.testingsystem.server;

import com.testingsystem.server.config.ServerConfig;
import com.testingsystem.server.db.ConnectionPool;
import com.testingsystem.server.db.DbInitializer;
import com.testingsystem.server.network.ServerThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class ServerApp {
    private static final Logger logger = LogManager.getLogger(ServerApp.class);

    public static void main(String[] args) {
        logger.info("Starting Professional Skills Testing System Server...");

        ServerApp app = new ServerApp();
        app.run(args);
    }

    public void run(String[] args) {
        try {
            ServerConfig config = ServerConfig.getInstance();
            logger.info("Configuration loaded");
            logger.info("Server port: {}", config.getServerPort());
            logger.info("Max threads: {}", config.getMaxThreads());
            logger.info("Database URL: {}", config.getDbUrl());

            ConnectionPool connectionPool = ConnectionPool.getInstance(
                    config.getDbUrl(),
                    config.getDbUser(),
                    config.getDbPassword(),
                    config.getDbPoolSize()
            );
            logger.info("Connection pool initialized with {} connections", config.getDbPoolSize());

            boolean initDb = args.length > 0 && "--init".equals(args[0]);
            if (initDb) {
                logger.info("Initializing database...");
                DbInitializer dbInitializer = new DbInitializer(connectionPool);
                dbInitializer.initialize();
                logger.info("Database initialized successfully");
            }

            ServerThread server = ServerThread.getInstance(config, connectionPool);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Shutdown hook triggered");
                server.stop();
            }));

            logger.info("Server is ready to accept connections");
            server.start();

        } catch (IOException e) {
            logger.error("Failed to start server", e);
            System.exit(1);
        } catch (SQLException e) {
            logger.error("Database error", e);
            System.exit(1);
        } catch (Exception e) {
            logger.error("Unexpected error", e);
            System.exit(1);
        }
    }
}
