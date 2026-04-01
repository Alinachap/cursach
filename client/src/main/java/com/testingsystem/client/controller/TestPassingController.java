package com.testingsystem.client.controller;

import com.testingsystem.client.network.RequestBuilder;
import com.testingsystem.client.util.AlertHelper;
import com.testingsystem.common.dto.AnswerOptionDTO;
import com.testingsystem.common.dto.QuestionDTO;
import com.testingsystem.common.dto.TestDTO;
import com.testingsystem.common.dto.TestResultDTO;
import com.testingsystem.common.enums.QuestionType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Controller for the test passing view.
 * Handles test taking with timer and answer submission.
 */
public class TestPassingController implements Initializable {
    private static final Logger logger = LogManager.getLogger(TestPassingController.class);

    @FXML private Label testTitleLabel;
    @FXML private Label timerLabel;
    @FXML private ScrollPane questionsScrollPane;
    @FXML private VBox questionsContainer;
    @FXML private Label questionNumberLabel;
    @FXML private Button prevButton;
    @FXML private Button nextButton;

    private RequestBuilder requestBuilder;
    private TestDTO currentTest;
    private List<QuestionDTO> questions;
    private Map<Long, List<Long>> userAnswers;
    private int currentQuestionIndex;
    private LocalDateTime startTime;
    private int timeLimitMinutes;
    private Timer timer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        requestBuilder = new RequestBuilder();
        userAnswers = new HashMap<>();
        currentQuestionIndex = 0;
    }

    /**
     * Sets up the test passing view with test data.
     *
     * @param test the test to take
     */
    public void setupTest(TestDTO test) {
        this.currentTest = test;
        this.timeLimitMinutes = test.getTimeLimit();
        this.startTime = LocalDateTime.now();
        
        testTitleLabel.setText(test.getTitle());
        
        try {
            questions = requestBuilder.getQuestions(test.getId(), true);
            loadQuestions();
            startTimer();
            updateNavigation();
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to load questions: " + e.getMessage());
            logger.error("Failed to load questions", e);
        }
    }

    private void loadQuestions() {
        questionsContainer.getChildren().clear();
        
        for (int i = 0; i < questions.size(); i++) {
            QuestionDTO question = questions.get(i);
            VBox questionBox = createQuestionBox(question, i + 1);
            questionBox.setVisible(i == 0);
            questionBox.setManaged(i == 0);
            questionsContainer.getChildren().add(questionBox);
        }
        
        updateQuestionNumber();
    }

    private VBox createQuestionBox(QuestionDTO question, int number) {
        VBox box = new VBox(15);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-radius: 5px;");
        
        Label questionLabel = new Label(number + ". " + question.getQuestionText());
        questionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        questionLabel.setWrapText(true);
        
        VBox optionsBox = new VBox(8);
        
        if (question.getQuestionType() == QuestionType.SINGLE) {
            ToggleGroup group = new ToggleGroup();
            
            for (AnswerOptionDTO option : question.getAnswerOptions()) {
                RadioButton radio = new RadioButton(option.getOptionText());
                radio.setToggleGroup(group);
                radio.setUserData(option.getId());
                
                // Check if previously selected
                List<Long> answers = userAnswers.get(question.getId());
                if (answers != null && answers.contains(option.getId())) {
                    radio.setSelected(true);
                }
                
                radio.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                    if (isSelected) {
                        userAnswers.put(question.getId(), Collections.singletonList(option.getId()));
                    }
                });
                
                optionsBox.getChildren().add(radio);
            }
        } else {
            for (AnswerOptionDTO option : question.getAnswerOptions()) {
                CheckBox checkbox = new CheckBox(option.getOptionText());
                checkbox.setUserData(option.getId());
                
                // Check if previously selected
                List<Long> answers = userAnswers.get(question.getId());
                if (answers != null && answers.contains(option.getId())) {
                    checkbox.setSelected(true);
                }
                
                checkbox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                    List<Long> currentAnswers = userAnswers.computeIfAbsent(
                        question.getId(), k -> new ArrayList<>());
                    
                    if (isSelected) {
                        currentAnswers.add(option.getId());
                    } else {
                        currentAnswers.remove(option.getId());
                    }
                });
                
                optionsBox.getChildren().add(checkbox);
            }
        }
        
        box.getChildren().addAll(questionLabel, optionsBox);
        return box;
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    Duration elapsed = Duration.between(startTime, LocalDateTime.now());
                    Duration remaining = Duration.ofMinutes(timeLimitMinutes).minus(elapsed);
                    
                    if (remaining.isNegative() || remaining.isZero()) {
                        timer.cancel();
                        handleSubmitTest();
                    } else {
                        long minutes = remaining.toMinutes();
                        long seconds = remaining.getSeconds() % 60;
                        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));

                        if (remaining.toMinutes() < 5) {
                            timerLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    @FXML private void handlePrevious() {
        if (currentQuestionIndex > 0) {
            questionsContainer.getChildren().get(currentQuestionIndex).setVisible(false);
            questionsContainer.getChildren().get(currentQuestionIndex).setManaged(false);
            currentQuestionIndex--;
            questionsContainer.getChildren().get(currentQuestionIndex).setVisible(true);
            questionsContainer.getChildren().get(currentQuestionIndex).setManaged(true);
            updateNavigation();
            updateQuestionNumber();
        }
    }

    @FXML private void handleNext() {
        if (currentQuestionIndex < questions.size() - 1) {
            questionsContainer.getChildren().get(currentQuestionIndex).setVisible(false);
            questionsContainer.getChildren().get(currentQuestionIndex).setManaged(false);
            currentQuestionIndex++;
            questionsContainer.getChildren().get(currentQuestionIndex).setVisible(true);
            questionsContainer.getChildren().get(currentQuestionIndex).setManaged(true);
            updateNavigation();
            updateQuestionNumber();
        }
    }

    @FXML private void handleSubmitTest() {
        if (timer != null) {
            timer.cancel();
        }
        
        if (!AlertHelper.showConfirmation("Submit Test", "Are you sure you want to submit?")) {
            startTimer();
            return;
        }
        
        try {
            // Convert answers to submission format
            List<RequestBuilder.UserAnswer> answers = new ArrayList<>();
            for (Map.Entry<Long, List<Long>> entry : userAnswers.entrySet()) {
                answers.add(new RequestBuilder.UserAnswer(entry.getKey(), entry.getValue()));
            }
            
            TestResultDTO result = requestBuilder.submitTest(currentTest.getId(), answers);

            StringBuilder message = new StringBuilder("Test completed!\n\n");
            message.append("Score: ").append(result.getScorePercent()).append("%\n");
            message.append("Status: ").append(Boolean.TRUE.equals(result.getIsPassed()) ? "PASSED" : "FAILED");

            AlertHelper.showInfo("Test Results", message.toString());

            Stage stage = (Stage) timerLabel.getScene().getWindow();
            stage.close();
            
        } catch (IOException e) {
            AlertHelper.showError("Error", "Failed to submit test: " + e.getMessage());
            logger.error("Failed to submit test", e);
        }
    }

    private void updateNavigation() {
        prevButton.setDisable(currentQuestionIndex == 0);
        nextButton.setDisable(currentQuestionIndex == questions.size() - 1);
    }

    private void updateQuestionNumber() {
        questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
    }

    public void cleanup() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
