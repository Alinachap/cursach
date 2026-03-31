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

/**
 * Scene manager for handling view transitions.
 * Implements the Factory Method pattern for creating scenes.
 */
public class SceneManager {
    private static final Logger logger = LogManager.getLogger(SceneManager.class);
    
    private static volatile SceneManager instance;
    
    private final Map<String, FXMLLoader> loaderCache;
    private Stage mainStage;

    /**
     * Private constructor for Singleton pattern.
     */
    private SceneManager() {
        this.loaderCache = new HashMap<>();
    }

    /**
     * Gets the singleton instance of SceneManager.
     *
     * @return the SceneManager instance
     */
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

    /**
     * Sets the main stage.
     *
     * @param stage the main application stage
     */
    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    /**
     * Gets the main stage.
     *
     * @return the main stage
     */
    public Stage getMainStage() {
        return mainStage;
    }

    /**
     * Loads a scene from FXML.
     *
     * @param fxmlPath the path to the FXML file
     * @return the loaded Parent node
     * @throws IOException if loading fails
     */
    public Parent loadScene(String fxmlPath) throws IOException {
        logger.debug("Loading scene: {}", fxmlPath);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        
        loaderCache.put(fxmlPath, loader);
        return root;
    }

    /**
     * Gets the controller for a loaded scene.
     *
     * @param fxmlPath the path to the FXML file
     * @param controllerClass the controller class
     * @param <T> the controller type
     * @return the controller instance
     */
    @SuppressWarnings("unchecked")
    public <T> T getController(String fxmlPath, Class<T> controllerClass) {
        FXMLLoader loader = loaderCache.get(fxmlPath);
        if (loader == null) {
            throw new IllegalStateException("Scene not loaded: " + fxmlPath);
        }
        return loader.getController();
    }

    /**
     * Sets the scene on the main stage.
     *
     * @param root the root node
     */
    public void setScene(Parent root) {
        if (mainStage != null) {
            mainStage.setScene(new Scene(root));
            mainStage.centerOnScreen();
        }
    }

    /**
     * Sets the scene and shows the stage.
     *
     * @param root the root node
     */
    public void showScene(Parent root) {
        setScene(root);
        if (mainStage != null) {
            mainStage.show();
        }
    }

    /**
     * Switches to a new scene.
     *
     * @param fxmlPath the path to the FXML file
     * @throws IOException if loading fails
     */
    public void switchToScene(String fxmlPath) throws IOException {
        Parent root = loadScene(fxmlPath);
        showScene(root);
    }

    /**
     * Creates a new scene without switching.
     * Factory method for creating scenes.
     *
     * @param fxmlPath the path to the FXML file
     * @return the created Scene
     * @throws IOException if loading fails
     */
    public Scene createScene(String fxmlPath) throws IOException {
        Parent root = loadScene(fxmlPath);
        return new Scene(root);
    }

    /**
     * Clears the loader cache.
     */
    public void clearCache() {
        loaderCache.clear();
    }

    /**
     * Resets the singleton instance.
     */
    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.clearCache();
        }
        instance = null;
    }
}
