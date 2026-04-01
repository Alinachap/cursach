package com.testingsystem.client.controller;

import com.testingsystem.client.network.RequestBuilder;
import com.testingsystem.client.util.AlertHelper;
import com.testingsystem.common.dto.TestResultDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultsViewController implements Initializable {
    private static final Logger logger = LogManager.getLogger(ResultsViewController.class);

    @FXML private Label scoreLabel;
    @FXML private Label statusLabel;
    @FXML private Label durationLabel;
    @FXML private Label dateLabel;
    @FXML private VBox questionsReviewContainer;

    private RequestBuilder requestBuilder;
    private TestResultDTO currentResult;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        requestBuilder = new RequestBuilder();
    }

    public void setupResult(TestResultDTO result) {
        this.currentResult = result;
        
        if (result != null) {
            scoreLabel.setText(result.getScorePercent() + "%");
            
            boolean passed = Boolean.TRUE.equals(result.getIsPassed());
            statusLabel.setText(passed ? "PASSED" : "FAILED");
            statusLabel.getStyleClass().remove(passed ? "status-failed" : "status-passed");
            statusLabel.getStyleClass().add(passed ? "status-passed" : "status-failed");
            
            Long duration = result.getDurationMinutes();
            durationLabel.setText(duration != null ? duration + " minutes" : "N/A");
            
            dateLabel.setText(result.getStartTime() != null ? 
                result.getStartTime().toString() : "N/A");
        }
    }

    @FXML private void handleBack() {
        questionsReviewContainer.getScene().getWindow().hide();
    }
}
