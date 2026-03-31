package com.testingsystem.client.controller;

import com.testingsystem.client.model.Session;
import com.testingsystem.client.network.RequestBuilder;
import com.testingsystem.client.util.AlertHelper;
import com.testingsystem.client.util.SceneManager;
import com.testingsystem.common.dto.*;
import com.testingsystem.common.enums.UserRole;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Controller for the main view.
 * Handles navigation and displays role-specific content.
 */
public class MainController implements Initializable {
    private static final Logger logger = LogManager.getLogger(MainController.class);

    @FXML private Label welcomeLabel;
    @FXML private Label roleLabel;
    @FXML private VBox specialistMenu;
    @FXML private VBox adminMenu;
    @FXML private ScrollPane contentScrollPane;
    @FXML private VBox contentArea;

    private RequestBuilder requestBuilder;
    private SceneManager sceneManager;
    private Session session;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        requestBuilder = new RequestBuilder();
        sceneManager = SceneManager.getInstance();
        session = Session.getInstance();
    }

    /**
     * Initializes the view after loading.
     */
    public void initializeView() {
        UserDTO user = session.getCurrentUser();
        if (user != null) {
            welcomeLabel.setText("Welcome, " + user.getFirstName() + "!");
            roleLabel.setText(user.getRole().getValue().toUpperCase());
            
            // Show appropriate menu
            if (user.getRole() == UserRole.ADMIN) {
                specialistMenu.setVisible(false);
                specialistMenu.setManaged(false);
                adminMenu.setVisible(true);
                adminMenu.setManaged(true);
                showAvailableTests();
            } else {
                adminMenu.setVisible(false);
                adminMenu.setManaged(false);
                showAvailableTests();
            }
        }
    }

    @FXML private void handleLogout() {
        if (AlertHelper.showConfirmation("Logout", "Are you sure you want to logout?")) {
            try {
                requestBuilder.logout();
            } catch (IOException e) {
                logger.warn("Logout request failed", e);
            }
            
            session.clear();
            
            try {
                sceneManager.switchToScene("/fxml/LoginView.fxml");
            } catch (IOException e) {
                logger.error("Failed to load login view", e);
            }
        }
    }

    @FXML private void handleShowTests() {
        showAvailableTests();
    }

    @FXML private void handleShowAssignments() {
        showMyAssignments();
    }

    @FXML private void handleShowResults() {
        showMyResults();
    }

    // Admin methods
    @FXML private void handleShowUsers() {
        showUsers();
    }

    @FXML private void handleCreateUser() {
        showCreateUserDialog();
    }

    @FXML private void handleShowAllTests() {
        showAllTests();
    }

    @FXML private void handleCreateTest() {
        showCreateTestDialog();
    }

    @FXML private void handleAssignTest() {
        showAssignTestDialog();
    }

    @FXML private void handleViewAssignments() {
        showAllAssignments();
    }

    @FXML private void handleViewResults() {
        showAllResults();
    }

    @FXML private void handleShowStatistics() {
        showStatistics();
    }

    // Display methods
    private void showAvailableTests() {
        contentArea.getChildren().clear();
        
        Label title = new Label("Available Tests");
        title.getStyleClass().add("section-title");
        contentArea.getChildren().add(title);
        
        try {
            List<TestDTO> tests = requestBuilder.getTests(true);
            
            if (tests.isEmpty()) {
                contentArea.getChildren().add(new Label("No tests available"));
                return;
            }
            
            for (TestDTO test : tests) {
                VBox testCard = createTestCard(test);
                contentArea.getChildren().add(testCard);
            }
            
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to load tests: " + e.getMessage());
            logger.error("Failed to load tests", e);
        }
    }

    private VBox createTestCard(TestDTO test) {
        VBox card = new VBox(10);
        card.getStyleClass().add("test-card");
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        
        Label titleLabel = new Label(test.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        Label descLabel = new Label(test.getDescription() != null ? test.getDescription() : "No description");
        descLabel.setWrapText(true);
        
        HBox infoBox = new HBox(20);
        infoBox.getChildren().addAll(
            new Label("Time: " + test.getTimeLimit() + " min"),
            new Label("Passing: " + test.getPassingScore() + "%")
        );
        
        Button startButton = new Button("Start Test");
        startButton.setOnAction(e -> handleStartTest(test));
        
        card.getChildren().addAll(titleLabel, descLabel, infoBox, startButton);
        return card;
    }

    private void handleStartTest(TestDTO test) {
        if (!AlertHelper.showConfirmation("Start Test", 
                "Start \"" + test.getTitle() + "\"?\nTime limit: " + test.getTimeLimit() + " minutes")) {
            return;
        }
        
        try {
            TestResultDTO result = requestBuilder.startTest(test.getId());
            
            // Open test passing window
            openTestPassingWindow(test, result);
            
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to start test: " + e.getMessage());
        }
    }

    private void openTestPassingWindow(TestDTO test, TestResultDTO result) {
        // In a full implementation, this would open a new window
        AlertHelper.showInfo("Test Started", "Test: " + test.getTitle() + "\nGood luck!");
    }

    private void showMyAssignments() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(createTitleLabel("My Assignments"));
        
        try {
            List<TestAssignmentDTO> assignments = requestBuilder.getMyAssignments();
            
            if (assignments.isEmpty()) {
                contentArea.getChildren().add(new Label("No assignments"));
                return;
            }
            
            for (TestAssignmentDTO assignment : assignments) {
                contentArea.getChildren().add(createAssignmentCard(assignment));
            }
            
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to load assignments: " + e.getMessage());
        }
    }

    private void showMyResults() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(createTitleLabel("My Results"));
        
        try {
            List<TestResultDTO> results = requestBuilder.getMyResults();
            
            if (results.isEmpty()) {
                contentArea.getChildren().add(new Label("No results yet"));
                return;
            }
            
            for (TestResultDTO result : results) {
                contentArea.getChildren().add(createResultCard(result));
            }
            
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to load results: " + e.getMessage());
        }
    }

    // Admin display methods
    private void showUsers() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(createTitleLabel("User Management"));
        
        try {
            List<UserDTO> users = requestBuilder.getUsers();
            
            for (UserDTO user : users) {
                contentArea.getChildren().add(createUserCard(user));
            }
            
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to load users: " + e.getMessage());
        }
    }

    private void showAllTests() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(createTitleLabel("All Tests"));
        
        try {
            List<TestDTO> tests = requestBuilder.getTests(false);
            
            for (TestDTO test : tests) {
                VBox card = createTestCard(test);
                
                // Add edit/delete buttons for admin
                HBox actions = new HBox(10);
                Button editBtn = new Button("Edit");
                Button deleteBtn = new Button("Delete");
                deleteBtn.setStyle("-fx-background-color: #e74c3c;");
                
                deleteBtn.setOnAction(e -> handleDeleteTest(test));
                
                actions.getChildren().addAll(editBtn, deleteBtn);
                card.getChildren().add(actions);
                
                contentArea.getChildren().add(card);
            }
            
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to load tests: " + e.getMessage());
        }
    }

    private void showAllAssignments() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(createTitleLabel("All Assignments"));
        
        try {
            List<TestAssignmentDTO> assignments = requestBuilder.getAssignments(null);
            
            for (TestAssignmentDTO assignment : assignments) {
                contentArea.getChildren().add(createAssignmentCard(assignment));
            }
            
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to load assignments: " + e.getMessage());
        }
    }

    private void showAllResults() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(createTitleLabel("All Results"));
        
        try {
            List<TestResultDTO> results = requestBuilder.getResults(null);
            
            for (TestResultDTO result : results) {
                contentArea.getChildren().add(createResultCard(result));
            }
            
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to load results: " + e.getMessage());
        }
    }

    private void showStatistics() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(createTitleLabel("Statistics"));
        
        try {
            Map<String, Object> stats = requestBuilder.getStatistics();
            
            VBox statsBox = new VBox(10);
            statsBox.setPadding(new Insets(15));
            statsBox.setStyle("-fx-background-color: white; -fx-border-radius: 5px;");
            
            for (Map.Entry<String, Object> entry : stats.entrySet()) {
                Label label = new Label(entry.getKey() + ": " + entry.getValue());
                statsBox.getChildren().add(label);
            }
            
            contentArea.getChildren().add(statsBox);
            
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to load statistics: " + e.getMessage());
        }
    }

    // Helper methods
    private Label createTitleLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-title");
        return label;
    }

    private VBox createUserCard(UserDTO user) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-radius: 5px;");
        
        card.getChildren().addAll(
            new Label(user.getFullName()),
            new Label("Login: " + user.getLogin()),
            new Label("Role: " + user.getRole().getValue()),
            new Label("Status: " + (user.isActive() ? "Active" : "Blocked"))
        );
        
        return card;
    }

    private VBox createAssignmentCard(TestAssignmentDTO assignment) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-radius: 5px;");
        
        card.getChildren().addAll(
            new Label("Test: " + assignment.getTestName()),
            new Label("Deadline: " + (assignment.getDeadline() != null ? assignment.getDeadline() : "No deadline")),
            new Label("Attempts left: " + assignment.getAttemptsLeft()),
            new Label("Status: " + assignment.getStatus().getValue())
        );
        
        return card;
    }

    private VBox createResultCard(TestResultDTO result) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-radius: 5px;");
        
        String statusStyle = Boolean.TRUE.equals(result.getIsPassed()) ? 
            "-fx-text-fill: green; -fx-font-weight: bold;" : 
            "-fx-text-fill: red; -fx-font-weight: bold;";
        
        Label statusLabel = new Label(Boolean.TRUE.equals(result.getIsPassed()) ? "PASSED" : "FAILED");
        statusLabel.setStyle(statusStyle);
        
        card.getChildren().addAll(
            new Label("Test: " + result.getTestName()),
            new Label("Score: " + result.getScorePercent() + "%"),
            statusLabel,
            new Label("Date: " + result.getStartTime())
        );
        
        return card;
    }

    // Dialog methods
    private void showCreateUserDialog() {
        Dialog<UserDTO> dialog = new Dialog<>();
        dialog.setTitle("Create User");
        dialog.setHeaderText("Enter user details");
        
        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        TextField loginField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("admin", "specialist");
        roleCombo.setValue("specialist");
        
        grid.add(new Label("Login:"), 0, 0);
        grid.add(loginField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("First Name:"), 0, 2);
        grid.add(firstNameField, 1, 2);
        grid.add(new Label("Last Name:"), 0, 3);
        grid.add(lastNameField, 1, 3);
        grid.add(new Label("Role:"), 0, 4);
        grid.add(roleCombo, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                try {
                    return requestBuilder.createUser(
                        loginField.getText(),
                        passwordField.getText(),
                        firstNameField.getText(),
                        lastNameField.getText(),
                        UserRole.fromValue(roleCombo.getValue())
                    );
                } catch (IOException e) {
                    AlertHelper.showError("Error", "Failed to create user: " + e.getMessage());
                }
            }
            return null;
        });
        
        Optional<UserDTO> result = dialog.showAndWait();
        result.ifPresent(user -> {
            AlertHelper.showInfo("Success", "User created: " + user.getLogin());
            showUsers();
        });
    }

    private void showCreateTestDialog() {
        Dialog<TestDTO> dialog = new Dialog<>();
        dialog.setTitle("Create Test");
        dialog.setHeaderText("Enter test details");
        
        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        TextField titleField = new TextField();
        TextArea descArea = new TextArea();
        descArea.setPrefRowCount(3);
        TextField timeField = new TextField("30");
        TextField passingField = new TextField("70");
        
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descArea, 1, 1);
        grid.add(new Label("Time Limit (min):"), 0, 2);
        grid.add(timeField, 1, 2);
        grid.add(new Label("Passing Score (%):"), 0, 3);
        grid.add(passingField, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                try {
                    TestDTO test = new TestDTO();
                    test.setTitle(titleField.getText());
                    test.setDescription(descArea.getText());
                    test.setTimeLimit(Integer.parseInt(timeField.getText()));
                    test.setPassingScore(Integer.parseInt(passingField.getText()));
                    return requestBuilder.createTest(test);
                } catch (IOException | NumberFormatException e) {
                    AlertHelper.showError("Error", "Failed to create test: " + e.getMessage());
                }
            }
            return null;
        });
        
        Optional<TestDTO> result = dialog.showAndWait();
        result.ifPresent(test -> {
            AlertHelper.showInfo("Success", "Test created: " + test.getTitle());
            showAllTests();
        });
    }

    private void showAssignTestDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Assign Test");
        dialog.setHeaderText("Select user and test");
        
        ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        ComboBox<UserDTO> userCombo = new ComboBox<>();
        ComboBox<TestDTO> testCombo = new ComboBox<>();
        TextField attemptsField = new TextField("1");
        
        try {
            userCombo.getItems().addAll(requestBuilder.getUsers().stream()
                .filter(u -> u.getRole() == UserRole.SPECIALIST).toList());
            testCombo.getItems().addAll(requestBuilder.getTests(true));
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to load data: " + e.getMessage());
            return;
        }
        
        grid.add(new Label("User:"), 0, 0);
        grid.add(userCombo, 1, 0);
        grid.add(new Label("Test:"), 0, 1);
        grid.add(testCombo, 1, 1);
        grid.add(new Label("Attempts:"), 0, 2);
        grid.add(attemptsField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == assignButtonType) {
                try {
                    UserDTO user = userCombo.getValue();
                    TestDTO test = testCombo.getValue();
                    if (user != null && test != null) {
                        requestBuilder.assignTest(user.getId(), test.getId(), 
                            Integer.parseInt(attemptsField.getText()));
                    }
                } catch (IOException | NumberFormatException e) {
                    AlertHelper.showError("Error", "Failed to assign test: " + e.getMessage());
                }
            }
            return null;
        });
        
        dialog.showAndWait();
        showAllAssignments();
    }

    private void handleDeleteTest(TestDTO test) {
        if (AlertHelper.showConfirmation("Delete Test", 
                "Are you sure you want to delete \"" + test.getTitle() + "\"?")) {
            try {
                requestBuilder.deleteTest(test.getId());
                AlertHelper.showInfo("Success", "Test deleted");
                showAllTests();
            } catch (IOException e) {
                AlertHelper.showError("Error", "Failed to delete test: " + e.getMessage());
            }
        }
    }
}
