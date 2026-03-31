package com.testingsystem.client.controller;

import com.testingsystem.client.network.RequestBuilder;
import com.testingsystem.client.util.AlertHelper;
import com.testingsystem.common.dto.TestAssignmentDTO;
import com.testingsystem.common.dto.TestDTO;
import com.testingsystem.common.dto.TestResultDTO;
import com.testingsystem.common.dto.UserDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller for the admin panel.
 * Handles all administrative functions.
 */
public class AdminPanelController implements Initializable {
    private static final Logger logger = LogManager.getLogger(AdminPanelController.class);

    @FXML private Label panelTitleLabel;
    @FXML private TabPane adminTabPane;
    
    // Users tab
    @FXML private TextField userSearchField;
    @FXML private TableView<UserDTO> usersTable;
    @FXML private TableColumn<UserDTO, Long> userIdColumn;
    @FXML private TableColumn<UserDTO, String> userLoginColumn;
    @FXML private TableColumn<UserDTO, String> userNameColumn;
    @FXML private TableColumn<UserDTO, String> userRoleColumn;
    @FXML private TableColumn<UserDTO, String> userStatusColumn;
    @FXML private TableColumn<UserDTO, Void> userActionsColumn;
    
    // Tests tab
    @FXML private TableView<TestDTO> testsTable;
    @FXML private TableColumn<TestDTO, Long> testIdColumn;
    @FXML private TableColumn<TestDTO, String> testTitleColumn;
    @FXML private TableColumn<TestDTO, Integer> testTimeLimitColumn;
    @FXML private TableColumn<TestDTO, Integer> testPassingScoreColumn;
    @FXML private TableColumn<TestDTO, String> testStatusColumn;
    @FXML private TableColumn<TestDTO, Void> testActionsColumn;
    
    // Assignments tab
    @FXML private TableView<TestAssignmentDTO> assignmentsTable;
    @FXML private TableColumn<TestAssignmentDTO, String> assignUserColumn;
    @FXML private TableColumn<TestAssignmentDTO, String> assignTestColumn;
    @FXML private TableColumn<TestAssignmentDTO, String> assignDeadlineColumn;
    @FXML private TableColumn<TestAssignmentDTO, Integer> assignAttemptsColumn;
    @FXML private TableColumn<TestAssignmentDTO, String> assignStatusColumn;
    @FXML private TableColumn<TestAssignmentDTO, Void> assignActionsColumn;
    
    // Results tab
    @FXML private ComboBox<String> testFilterCombo;
    @FXML private TableView<TestResultDTO> resultsTable;
    @FXML private TableColumn<TestResultDTO, String> resultUserColumn;
    @FXML private TableColumn<TestResultDTO, String> resultTestColumn;
    @FXML private TableColumn<TestResultDTO, String> resultScoreColumn;
    @FXML private TableColumn<TestResultDTO, String> resultPassedColumn;
    @FXML private TableColumn<TestResultDTO, String> resultDateColumn;
    @FXML private TableColumn<TestResultDTO, String> resultDurationColumn;
    
    // Statistics tab
    @FXML private VBox overallStatsBox;
    @FXML private ComboBox<String> testStatsCombo;
    @FXML private VBox testStatsBox;

    private RequestBuilder requestBuilder;
    private List<UserDTO> allUsers;
    private List<TestDTO> allTests;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        requestBuilder = new RequestBuilder();
        setupTables();
    }

    private void setupTables() {
        // Users table
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userLoginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        userNameColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName()));
        userRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        userStatusColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().isActive() ? "Active" : "Blocked"));
        
        // Tests table
        testIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        testTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        testTimeLimitColumn.setCellValueFactory(new PropertyValueFactory<>("timeLimit"));
        testPassingScoreColumn.setCellValueFactory(new PropertyValueFactory<>("passingScore"));
        testStatusColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().isActive() ? "Active" : "Inactive"));
        
        // Assignments table
        assignUserColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        assignTestColumn.setCellValueFactory(new PropertyValueFactory<>("testName"));
        assignDeadlineColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDeadline() != null ? 
                    cellData.getValue().getDeadline().toString() : "N/A"));
        assignAttemptsColumn.setCellValueFactory(new PropertyValueFactory<>("attemptsLeft"));
        assignStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Results table
        resultUserColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        resultTestColumn.setCellValueFactory(new PropertyValueFactory<>("testName"));
        resultScoreColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getScorePercent() != null ? 
                    cellData.getValue().getScorePercent() + "%" : "N/A"));
        resultPassedColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                Boolean.TRUE.equals(cellData.getValue().getIsPassed()) ? "Yes" : "No"));
        resultDateColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getStartTime() != null ? 
                    cellData.getValue().getStartTime().toString() : "N/A"));
        resultDurationColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDurationMinutes() != null ? 
                    cellData.getValue().getDurationMinutes() + " min" : "N/A"));
    }

    @FXML private void handleBack() {
        adminTabPane.getScene().getWindow().hide();
    }

    @FXML private void handleAddUser() {
        // Show create user dialog (similar to MainController)
        AlertHelper.showInfo("Create User", "Dialog would open here");
    }

    @FXML private void handleRefreshUsers() {
        loadUsers();
    }

    @FXML private void handleSearchUsers() {
        filterUsers(userSearchField.getText());
    }

    @FXML private void handleCreateTest() {
        AlertHelper.showInfo("Create Test", "Dialog would open here");
    }

    @FXML private void handleRefreshTests() {
        loadTests();
    }

    @FXML private void handleAssignTest() {
        AlertHelper.showInfo("Assign Test", "Dialog would open here");
    }

    @FXML private void handleRefreshAssignments() {
        loadAssignments();
    }

    @FXML private void handleRefreshResults() {
        loadResults();
    }

    @FXML private void handleRefreshStats() {
        loadStatistics();
    }

    private void loadUsers() {
        try {
            allUsers = requestBuilder.getUsers();
            usersTable.getItems().setAll(allUsers);
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to load users: " + e.getMessage());
        }
    }

    private void filterUsers(String searchText) {
        if (allUsers == null) return;
        
        if (searchText == null || searchText.isEmpty()) {
            usersTable.getItems().setAll(allUsers);
        } else {
            usersTable.getItems().setAll(allUsers.stream()
                .filter(u -> u.getLogin().toLowerCase().contains(searchText.toLowerCase()) ||
                            (u.getFirstName() + " " + u.getLastName()).toLowerCase().contains(searchText.toLowerCase()))
                .toList());
        }
    }

    private void loadTests() {
        try {
            allTests = requestBuilder.getTests(false);
            testsTable.getItems().setAll(allTests);
            
            // Update filter combo
            testFilterCombo.getItems().clear();
            testFilterCombo.getItems().add("All Tests");
            for (TestDTO test : allTests) {
                testFilterCombo.getItems().add(test.getTitle());
            }
            testFilterCombo.setValue("All Tests");
            
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to load tests: " + e.getMessage());
        }
    }

    private void loadAssignments() {
        try {
            List<TestAssignmentDTO> assignments = requestBuilder.getAssignments(null);
            assignmentsTable.getItems().setAll(assignments);
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to load assignments: " + e.getMessage());
        }
    }

    private void loadResults() {
        try {
            List<TestResultDTO> results = requestBuilder.getResults(null);
            resultsTable.getItems().setAll(results);
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to load results: " + e.getMessage());
        }
    }

    private void loadStatistics() {
        try {
            Map<String, Object> stats = requestBuilder.getStatistics();
            
            overallStatsBox.getChildren().clear();
            for (Map.Entry<String, Object> entry : stats.entrySet()) {
                Label label = new Label(entry.getKey() + ": " + entry.getValue());
                overallStatsBox.getChildren().add(label);
            }
            
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to load statistics: " + e.getMessage());
        }
    }
}
