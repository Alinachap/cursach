package com.testingsystem.server.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database initializer.
 * Executes SQL scripts to initialize the database schema.
 */
public class DbInitializer {
    private static final Logger logger = LogManager.getLogger(DbInitializer.class);
    
    private final ConnectionPool connectionPool;

    /**
     * Constructs a DbInitializer with the given connection pool.
     *
     * @param connectionPool the connection pool
     */
    public DbInitializer(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * Initializes the database by executing the init script.
     *
     * @throws SQLException if database error occurs
     * @throws IOException if script file not found
     */
    public void initialize() throws SQLException, IOException {
        logger.info("Starting database initialization");
        
        try (Connection conn = connectionPool.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Execute create tables script
            executeScript(stmt, "/db/create_tables.sql");
            logger.info("Tables created successfully");
            
            // Execute test data script
            executeScript(stmt, "/db/insert_test_data.sql");
            logger.info("Test data inserted successfully");
        }
        
        logger.info("Database initialization completed");
    }

    /**
     * Executes an SQL script from resources.
     *
     * @param stmt the statement to execute with
     * @param resourcePath the path to the script resource
     * @throws SQLException if database error occurs
     * @throws IOException if script not found
     */
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

    /**
     * Reads a script from resources.
     *
     * @param resourcePath the path to the script
     * @return the script content
     * @throws IOException if script not found
     */
    private String readScript(String resourcePath) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Script not found: " + resourcePath);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Drops all tables.
     *
     * @throws SQLException if database error occurs
     * @throws IOException if script not found
     */
    public void dropTables() throws SQLException, IOException {
        logger.info("Dropping all tables");
        
        try (Connection conn = connectionPool.getConnection();
             Statement stmt = conn.createStatement()) {
            
            executeScript(stmt, "/db/drop_tables.sql");
        }
        
        logger.info("All tables dropped");
    }
}
