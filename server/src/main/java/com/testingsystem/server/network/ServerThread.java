package com.testingsystem.server.network;

import com.testingsystem.common.dto.UserDTO;
import com.testingsystem.server.config.ServerConfig;
import com.testingsystem.server.db.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Main server thread that accepts client connections.
 * Uses a thread pool for handling multiple clients concurrently.
 * Implements the Singleton pattern.
 */
public class ServerThread {
    private static final Logger logger = LogManager.getLogger(ServerThread.class);
    
    private static volatile ServerThread instance;
    
    private final int port;
    private final int maxThreads;
    private final ConnectionPool connectionPool;
    private final Map<Long, UserDTO> authenticatedUsers;
    private final AtomicLong clientIdCounter;
    
    private ExecutorService threadPool;
    private ServerSocket serverSocket;
    private volatile boolean running = false;

    /**
     * Private constructor for Singleton pattern.
     *
     * @param config the server configuration
     * @param connectionPool the database connection pool
     */
    private ServerThread(ServerConfig config, ConnectionPool connectionPool) {
        this.port = config.getServerPort();
        this.maxThreads = config.getMaxThreads();
        this.connectionPool = connectionPool;
        this.authenticatedUsers = new ConcurrentHashMap<>();
        this.clientIdCounter = new AtomicLong(0);
    }

    /**
     * Gets the singleton instance of ServerThread.
     *
     * @param config the server configuration
     * @param connectionPool the database connection pool
     * @return the ServerThread instance
     */
    public static ServerThread getInstance(ServerConfig config, ConnectionPool connectionPool) {
        if (instance == null) {
            synchronized (ServerThread.class) {
                if (instance == null) {
                    instance = new ServerThread(config, connectionPool);
                }
            }
        }
        return instance;
    }

    /**
     * Starts the server.
     *
     * @throws IOException if server socket cannot be created
     */
    public void start() throws IOException {
        if (running) {
            logger.warn("Server is already running");
            return;
        }

        // Create thread pool
        threadPool = new ThreadPoolExecutor(
                maxThreads,
                maxThreads * 2,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        // Create server socket
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000);
        
        running = true;
        logger.info("Server started on port {}", port);
        logger.info("Max threads: {}", maxThreads);

        // Accept connections
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(30000);
                
                Long clientId = clientIdCounter.incrementAndGet();
                ClientHandler clientHandler = new ClientHandler(
                        clientSocket, connectionPool, clientId, authenticatedUsers);
                
                threadPool.submit(clientHandler);
                logger.debug("Submitted client {} handler to thread pool", clientId);
                
            } catch (SocketTimeoutException e) {
                // Continue accepting connections
            } catch (IOException e) {
                if (running) {
                    logger.error("Error accepting connection", e);
                }
            }
        }
    }

    /**
     * Stops the server.
     */
    public void stop() {
        logger.info("Stopping server...");
        running = false;

        // Close server socket
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                logger.info("Server socket closed");
            } catch (IOException e) {
                logger.error("Error closing server socket", e);
            }
        }

        // Shutdown thread pool
        if (threadPool != null) {
            threadPool.shutdown();
            try {
                if (!threadPool.awaitTermination(30, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                }
                logger.info("Thread pool shut down");
            } catch (InterruptedException e) {
                threadPool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        // Close connection pool
        if (connectionPool != null) {
            connectionPool.shutdown();
        }

        logger.info("Server stopped");
    }

    /**
     * Checks if the server is running.
     *
     * @return true if running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Gets the number of connected clients.
     *
     * @return the number of authenticated clients
     */
    public int getConnectedClientCount() {
        return authenticatedUsers.size();
    }

    /**
     * Gets the server port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets the thread pool.
     *
     * @return the executor service
     */
    public ExecutorService getThreadPool() {
        return threadPool;
    }

    /**
     * Resets the singleton instance (for testing).
     */
    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.stop();
        }
        instance = null;
        logger.info("ServerThread instance reset");
    }
}
