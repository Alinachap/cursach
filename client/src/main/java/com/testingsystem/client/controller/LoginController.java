package com.testingsystem.client.controller;

import com.testingsystem.client.model.Session;
import com.testingsystem.client.network.RequestBuilder;
import com.testingsystem.client.network.ServerConnection;
import com.testingsystem.client.util.AlertHelper;
import com.testingsystem.client.util.SceneManager;
import com.testingsystem.common.dto.UserDTO;
import com.testingsystem.common.enums.UserRole;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button clearButton;
    @FXML private Label messageLabel;
    @FXML private AnchorPane rootPane;

    private RequestBuilder requestBuilder;
    private SceneManager sceneManager;
    private Session session;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        requestBuilder = new RequestBuilder();
        sceneManager = SceneManager.getInstance();
        session = Session.getInstance();

        passwordField.setOnAction(event -> handleLogin());

        logger.info("Login view initialized");
    }

    @FXML
    private void handleLogin() {
        String login = loginField.getText().trim();
        String password = passwordField.getText();
        
        // Validate input
        if (login.isEmpty()) {
            showMessage("Please enter login", true);
            loginField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            showMessage("Please enter password", true);
            passwordField.requestFocus();
            return;
        }
        
        try {
            loginButton.setDisable(true);
            messageLabel.setVisible(false);

            ServerConnection connection = ServerConnection.getInstance();
            if (!connection.isConnected()) {
                try {
                    connection.connect();
                } catch (IOException e) {
                    showMessage("Cannot connect to server. Please ensure server is running.", true);
                    logger.error("Failed to connect to server", e);
                    return;
                }
            }

            var response = requestBuilder.login(login, password);

            UserDTO user = (UserDTO) response.get("data");

            if (user != null) {
                session.setCurrentUser(user);
                logger.info("User {} logged in successfully", login);

                loadMainView();
            } else {
                showMessage("Login failed. Please check your credentials.", true);
            }
            
        } catch (IOException e) {
            showMessage("Connection error: " + e.getMessage(), true);
            logger.error("Login error", e);
        } catch (Exception e) {
            showMessage("Login failed: " + e.getMessage(), true);
            logger.error("Login error", e);
        } finally {
            loginButton.setDisable(false);
        }
    }

    /**
     * Handles the clear button click.
     */
    @FXML
    private void handleClear() {
        loginField.clear();
        passwordField.clear();
        messageLabel.setVisible(false);
        loginField.requestFocus();
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
        messageLabel.setManaged(true);
        
        if (isError) {
            AlertHelper.showError("Login Error", message);
        }
    }

    private void loadMainView() {
        try {
            UserDTO user = session.getCurrentUser();

            if (user.getRole() == UserRole.ADMIN) {
                sceneManager.switchToScene("/fxml/MainView.fxml");
            } else {
                sceneManager.switchToScene("/fxml/MainView.fxml");
            }

            MainController controller = sceneManager.getController("/fxml/MainView.fxml", MainController.class);
            controller.initializeView();
            
        } catch (IOException e) {
            logger.error("Failed to load main view", e);
            AlertHelper.showError("Error", "Failed to load main view: " + e.getMessage());
        }
    }
}
