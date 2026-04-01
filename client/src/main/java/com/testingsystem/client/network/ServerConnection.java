package com.testingsystem.client.network;

import com.testingsystem.common.utils.Constants;
import com.testingsystem.common.utils.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerConnection {
    private static final Logger logger = LogManager.getLogger(ServerConnection.class);

    private static volatile ServerConnection instance;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String serverHost;
    private int serverPort;
    private volatile boolean connected = false;

    private ServerConnection() {
        this.serverHost = Constants.DEFAULT_SERVER_HOST;
        this.serverPort = Constants.DEFAULT_SERVER_PORT;
    }

    public static ServerConnection getInstance() {
        if (instance == null) {
            synchronized (ServerConnection.class) {
                if (instance == null) {
                    instance = new ServerConnection();
                }
            }
        }
        return instance;
    }

    public synchronized boolean connect(String host, int port) throws IOException {
        if (connected) {
            disconnect();
        }

        this.serverHost = host;
        this.serverPort = port;

        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(Constants.SOCKET_READ_TIMEOUT_MS);
            
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            
            connected = true;
            logger.info("Connected to server at {}:{}", host, port);
            return true;

        } catch (IOException e) {
            logger.error("Failed to connect to server at {}:{}", host, port, e);
            connected = false;
            throw e;
        }
    }

    public boolean connect() throws IOException {
        return connect(serverHost, serverPort);
    }

    public synchronized void disconnect() {
        connected = false;
        
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null && !socket.isClosed()) socket.close();
            logger.info("Disconnected from server");
        } catch (IOException e) {
            logger.warn("Error during disconnect", e);
        }
    }

    public synchronized String sendRequest(String command, String data) throws IOException {
        if (!connected || socket == null || socket.isClosed()) {
            throw new IOException("Not connected to server");
        }

        String request = command + "|" + data;
        logger.debug("Sending request: {}", command);
        writer.println(request);
        
        String response = reader.readLine();
        if (response == null) {
            throw new IOException("Connection closed by server");
        }
        
        logger.debug("Received response: {}", response.substring(0, Math.min(100, response.length())));
        return response;
    }

    public String sendRequest(String command, Object data) throws IOException {
        String serializedData;
        if (data instanceof String) {
            serializedData = (String) data;
        } else if (data instanceof Map) {
            serializedData = new String(SerializationUtils.serialize(data));
        } else {
            serializedData = new String(SerializationUtils.serialize(data));
        }
        return sendRequest(command, serializedData);
    }

    public String login(String login, String password) throws IOException {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("login", login);
        loginData.put("password", password);
        return sendRequest("LOGIN", loginData);
    }

    public boolean isConnected() {
        return connected;
    }

    public String getServerHost() {
        return serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.disconnect();
        }
        instance = null;
    }
}
