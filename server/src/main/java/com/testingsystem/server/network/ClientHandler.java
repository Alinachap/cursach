package com.testingsystem.server.network;

import com.testingsystem.common.dto.UserDTO;
import com.testingsystem.server.db.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientHandler implements Runnable {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);

    private final Socket clientSocket;
    private final ConnectionPool connectionPool;
    private final Long clientId;
    private final java.util.Map<Long, UserDTO> authenticatedUsers;

    private volatile boolean running = true;

    public ClientHandler(Socket clientSocket, ConnectionPool connectionPool,
                         Long clientId, java.util.Map<Long, UserDTO> authenticatedUsers) {
        this.clientSocket = clientSocket;
        this.connectionPool = connectionPool;
        this.clientId = clientId;
        this.authenticatedUsers = authenticatedUsers;
    }

    @Override
    public void run() {
        logger.info("Client {} connected from {}", clientId, clientSocket.getRemoteSocketAddress());

        try (InputStream input = clientSocket.getInputStream();
             OutputStream output = clientSocket.getOutputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(output), true)) {

            RequestProcessor processor = new RequestProcessor(connectionPool, authenticatedUsers);

            while (running) {
                try {
                    String request = reader.readLine();
                    if (request == null) {
                        logger.info("Client {} disconnected", clientId);
                        break;
                    }

                    if (request.trim().isEmpty()) {
                        continue;
                    }

                    logger.debug("Received from client {}: {}", clientId, request);

                    String response = processor.processRequest(request, clientId);

                    logger.debug("Sending to client {}: {}", clientId, response);
                    writer.println(response);

                } catch (SocketTimeoutException e) {
                } catch (IOException e) {
                    logger.warn("IO error with client {}", clientId, e);
                    break;
                }
            }

        } catch (IOException e) {
            logger.error("Error handling client {}", clientId, e);
        } finally {
            cleanup();
        }
    }

    public void stop() {
        running = false;
        cleanup();
    }

    private void cleanup() {
        authenticatedUsers.remove(clientId);
        try {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            logger.warn("Error closing socket for client {}", clientId, e);
        }
        logger.info("Client {} handler stopped", clientId);
    }

    public Long getClientId() {
        return clientId;
    }

    public Socket getSocket() {
        return clientSocket;
    }
}
