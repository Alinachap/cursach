package com.testingsystem.client.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Utility class for displaying alerts and dialogs.
 */
public class AlertHelper {

    /**
     * Shows an information alert.
     *
     * @param title the alert title
     * @param message the alert message
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a warning alert.
     *
     * @param title the alert title
     * @param message the alert message
     */
    public static void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an error alert.
     *
     * @param title the alert title
     * @param message the alert message
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an error alert with exception details.
     *
     * @param title the alert title
     * @param message the alert message
     * @param exception the exception
     */
    public static void showError(String title, String message, Throwable exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        if (exception != null) {
            alert.getDialogPane().setExpandableContent(
                new javafx.scene.control.TextArea(exception.getMessage())
            );
        }
        
        alert.showAndWait();
    }

    /**
     * Shows a confirmation dialog.
     *
     * @param title the dialog title
     * @param message the confirmation message
     * @return true if user confirmed
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private AlertHelper() {
        throw new UnsupportedOperationException("AlertHelper class cannot be instantiated");
    }
}
