package com.testingsystem.client;

import com.testingsystem.client.network.ServerConnection;
import com.testingsystem.client.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientApp extends Application {
    private static final Logger logger = LogManager.getLogger(ClientApp.class);

    public static void main(String[] args) {
        logger.info("Starting Professional Skills Testing System Client...");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Client application starting");
        
        try {
            SceneManager sceneManager = SceneManager.getInstance();
            sceneManager.setMainStage(primaryStage);

            primaryStage.setTitle("Professional Skills Testing System");
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);

            var root = sceneManager.loadScene("/fxml/LoginView.fxml");
            sceneManager.setScene(root);

            primaryStage.show();

            logger.info("Client application started successfully");

        } catch (Exception e) {
            logger.error("Failed to start client application", e);

            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Startup Error");
            alert.setHeaderText("Failed to start application");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();

            System.exit(1);
        }
    }

    @Override
    public void stop() {
        logger.info("Client application stopping");

        try {
            ServerConnection.getInstance().disconnect();
        } catch (Exception e) {
            logger.warn("Error during disconnect", e);
        }

        SceneManager.resetInstance();
        ServerConnection.resetInstance();

        logger.info("Client application stopped");
    }
}
