/**
 * ResultController: Handles the result scene;
 * calculates the score and displays it to the user.
 */

package com.whitelaw.quizinator;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ResultController {

    @FXML
    private Label scoreLabel;
    @FXML
    private TableView<QuestionController> resultsTable;

    private final WelcomeController mainController;
    private final int[] scores;

    private Scene thisScene;

    public ResultController(WelcomeController mainController) {
        this.mainController = mainController;
        this.scores = mainController.getScores();

        try {
            FXMLLoader loader = new FXMLLoader(QuizinatorApplication.class.getResource("fxml/result.fxml"));
            loader.setController(this);
            thisScene = new Scene(loader.load());
        } catch(Exception e) {
            thisScene = null;
            System.err.println("--- ERROR LOADING RESULT SCENE! ---");
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        int total = 0;
        for (int num : scores) total += num;
        double score = (total / (double) scores.length) * 100;
        scoreLabel.setText(String.format("%.1f%%", score));

        setupTable();

        // Clear default focus on first element
        Platform.runLater(() -> scoreLabel.requestFocus());
    }

    private void setupTable() {

        TableColumn<QuestionController, String> questionColumn = new TableColumn<>("Question");
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        questionColumn.setPrefWidth(450);
        questionColumn.setMaxWidth(450);

        TableColumn<QuestionController, String> correctAnswerColumn = new TableColumn<>("Correct Answer");
        correctAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("correctAnswerText"));
        correctAnswerColumn.setMinWidth(100);
        correctAnswerColumn.setPrefWidth(100);
        correctAnswerColumn.setMaxWidth(100);

        TableColumn<QuestionController, String> selectedAnswerColumn = new TableColumn<>("Selected Answer");
        selectedAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("selectedAnswerText"));
        selectedAnswerColumn.setMinWidth(100);
        selectedAnswerColumn.setPrefWidth(100);
        selectedAnswerColumn.setMaxWidth(100);

        resultsTable.getColumns().add(questionColumn);
        resultsTable.getColumns().add(correctAnswerColumn);
        resultsTable.getColumns().add(selectedAnswerColumn);

        ObservableList<QuestionController> questionControllerObservableList = FXCollections.observableArrayList(mainController.getQuestionControllerList());
        resultsTable.setItems(questionControllerObservableList);

        selectedAnswerColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    // if correct, make cell green, else red
                    if (scores[getIndex()] == 1) {
                        setText(item);
                        setStyle("-fx-background-color: #009200");
                    } else {
                        setText(item);
                        setStyle("-fx-background-color: #920000");
                    }
                }
            }
        });
    }

    public void setAndShowScene() {
        mainController.getThisStage().setScene(thisScene);
        mainController.getThisStage().setTitle("Quizinator - Results");
    }

    @FXML
    private void onRestartClicked() {
        mainController.getThisStage().close();

        WelcomeController welcomeController = new WelcomeController(new Stage());
        welcomeController.setAndShowScene();
    }

    @FXML
    private void onQuitClicked() {
        Platform.exit();
    }

}
