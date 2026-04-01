package com.testingsystem.server.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbInitializer {
    private static final Logger logger = LogManager.getLogger(DbInitializer.class);

    private final ConnectionPool connectionPool;

    public DbInitializer(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void initialize() throws SQLException, IOException {
        logger.info("Starting database initialization");

        try (Connection conn = connectionPool.getConnection();
             Statement stmt = conn.createStatement()) {

            executeScript(stmt, "/db/create_tables.sql");
            logger.info("Tables created successfully");

            executeScript(stmt, "/db/insert_test_data.sql");
            logger.info("Test data inserted successfully");
        }

        logger.info("Database initialization completed");
    }

    private void executeScript(Statement stmt, String resourcePath) throws SQLException, IOException {
        String script = readScript(resourcePath);
        String[] statements = script.split(";");

        for (String sql : statements) {
            String trimmedSql = sql.trim();
            if (!trimmedSql.isEmpty() && !trimmedSql.startsWith("--")) {
                stmt.execute(trimmedSql);
            }
        }
    }

    private String readScript(String resourcePath) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Script not found: " + resourcePath);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public void dropTables() throws SQLException, IOException {
        logger.info("Dropping all tables");

        try (Connection conn = connectionPool.getConnection();
             Statement stmt = conn.createStatement()) {

            executeScript(stmt, "/db/drop_tables.sql");
        }

        logger.info("All tables dropped");
    }
}
