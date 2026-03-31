package com.testingsystem.server.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Database connection pool using Singleton pattern.
 * Manages a pool of reusable database connections.
 */
public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
    
    private static volatile ConnectionPool instance;
    
    private final BlockingQueue<Connection> connectionPool;
    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final int poolSize;

    /**
     * Private constructor for Singleton pattern.
     *
     * @param jdbcUrl the database URL
     * @param username the database username
     * @param password the database password
     * @param poolSize the size of the connection pool
     */
    private ConnectionPool(String jdbcUrl, String username, String password, int poolSize) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.poolSize = poolSize;
        this.connectionPool = new LinkedBlockingQueue<>(poolSize);
        
        initializePool();
    }

    /**
     * Gets the singleton instance of ConnectionPool.
     *
     * @param jdbcUrl the database URL
     * @param username the database username
     * @param password the database password
     * @param poolSize the size of the connection pool
     * @return the ConnectionPool instance
     */
    public static ConnectionPool getInstance(String jdbcUrl, String username, String password, int poolSize) {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool(jdbcUrl, username, password, poolSize);
                }
            }
        }
        return instance;
    }

    /**
     * Gets the singleton instance (for use after initial creation).
     *
     * @return the ConnectionPool instance
     * @throws IllegalStateException if not initialized
     */
    public static ConnectionPool getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ConnectionPool not initialized");
        }
        return instance;
    }

    /**
     * Initializes the connection pool.
     */
    private void initializePool() {
        logger.info("Initializing connection pool with size: {}", poolSize);
        
        for (int i = 0; i < poolSize; i++) {
            try {
                Connection connection = createConnection();
                connectionPool.offer(connection);
                logger.debug("Created connection {}", i + 1);
            } catch (SQLException e) {
                logger.error("Failed to create connection", e);
            }
        }
        
        logger.info("Connection pool initialized with {} connections", connectionPool.size());
    }

    /**
     * Creates a new database connection.
     *
     * @return a new Connection
     * @throws SQLException if connection creation fails
     */
    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    /**
     * Gets a connection from the pool.
     *
     * @return a Connection from the pool
     * @throws SQLException if no connection available
     */
    public Connection getConnection() throws SQLException {
        try {
            Connection connection = connectionPool.poll(5, TimeUnit.SECONDS);
            if (connection == null) {
                logger.warn("Connection pool exhausted, creating new connection");
                return createConnection();
            }
            
            if (!connection.isValid(1)) {
                logger.debug("Connection invalid, creating new connection");
                connection.close();
                return createConnection();
            }
            
            return connection;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Interrupted while waiting for connection", e);
        }
    }

    /**
     * Returns a connection to the pool.
     *
     * @param connection the connection to return
     */
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed() && connection.isValid(1)) {
                    if (!connectionPool.offer(connection, 1, TimeUnit.SECONDS)) {
                        connection.close();
                    }
                } else {
                    connection.close();
                }
            } catch (SQLException | InterruptedException e) {
                logger.warn("Failed to return connection to pool", e);
                try {
                    connection.close();
                } catch (SQLException ex) {
                    // Ignore
                }
            }
        }
    }

    /**
     * Gets the current pool size.
     *
     * @return the number of available connections
     */
    public int getPoolSize() {
        return connectionPool.size();
    }

    /**
     * Closes all connections in the pool.
     */
    public void shutdown() {
        logger.info("Shutting down connection pool");
        
        for (Connection connection : connectionPool) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.warn("Failed to close connection", e);
            }
        }
        
        connectionPool.clear();
        instance = null;
        logger.info("Connection pool shut down");
    }
}
