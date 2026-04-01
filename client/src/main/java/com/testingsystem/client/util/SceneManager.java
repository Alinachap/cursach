package com.testingsystem.client.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private static final Logger logger = LogManager.getLogger(SceneManager.class);

    private static volatile SceneManager instance;

    private final Map<String, FXMLLoader> loaderCache;
    private Stage mainStage;

    private SceneManager() {
        this.loaderCache = new HashMap<>();
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            synchronized (SceneManager.class) {
                if (instance == null) {
                    instance = new SceneManager();
                }
            }
        }
        return instance;
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public Parent loadScene(String fxmlPath) throws IOException {
        logger.debug("Loading scene: {}", fxmlPath);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        
        loaderCache.put(fxmlPath, loader);
        return root;
    }

    @SuppressWarnings("unchecked")
    public <T> T getController(String fxmlPath, Class<T> controllerClass) {
        FXMLLoader loader = loaderCache.get(fxmlPath);
        if (loader == null) {
            throw new IllegalStateException("Scene not loaded: " + fxmlPath);
        }
        return loader.getController();
    }

    public void setScene(Parent root) {
        if (mainStage != null) {
            mainStage.setScene(new Scene(root));
            mainStage.centerOnScreen();
        }
    }

    public void showScene(Parent root) {
        setScene(root);
        if (mainStage != null) {
            mainStage.show();
        }
    }

    public void switchToScene(String fxmlPath) throws IOException {
        Parent root = loadScene(fxmlPath);
        showScene(root);
    }

    public Scene createScene(String fxmlPath) throws IOException {
        Parent root = loadScene(fxmlPath);
        return new Scene(root);
    }

    public void clearCache() {
        loaderCache.clear();
    }

    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.clearCache();
        }
        instance = null;
    }
}
