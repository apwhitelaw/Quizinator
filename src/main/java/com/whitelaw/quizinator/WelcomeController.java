/**
 * WelcomeController: Main controller for the application;
 * handles the welcome screen and the quiz itself.
 */


package com.whitelaw.quizinator;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class WelcomeController {

    static String[] CATEGORIES = {"Any Category", "General Knowledge", "Entertainment: Books", "Entertainment: Film",
            "Entertainment: Music", "Entertainment: Musicals & Theatres",
            "Entertainment: Television", "Entertainment: Video Games",
            "Entertainment: Board Games", "Science & Nature", "Science: Computers",
            "Science: Mathematics", "Mythology", "Sports", "Geography", "History",
            "Politics", "Art (Broken?)", "Celebrities", "Animals", "Vehicles", "Entertainment: Comics",
            "Science: Gadgets", "Entertainment: Japanese Anime & Manga",
            "Entertainment: Cartoon & Animations"};

    static String[] DIFFICULTIES = {"Any", "Easy", "Medium", "Hard"};

    static String[] TYPES = {"Any", "Multiple Choice", "True/False"};


    @FXML
    private Label titleLabel;
    @FXML
    private Spinner<Integer> numberOfQuestionsSpinner;
    @FXML
    private ChoiceBox<String> difficultyChoiceBox;
    @FXML
    private ChoiceBox<String> categoryChoiceBox;
    @FXML
    private ChoiceBox<String> typeChoiceBox;

    private final Stage thisStage;
    private final List<QuestionController> questionControllerList;

    private Scene thisScene;
    private List<QuizQuestion> questionList;
    private int[] scores;

    public WelcomeController(Stage stage) {
        // Main controller (this) holds reference to main stage
        thisStage = stage;
        questionControllerList = new ArrayList<>();

        try {
            FXMLLoader loader = new FXMLLoader(QuizinatorApplication.class.getResource("fxml/welcome.fxml"));
            loader.setController(this);
            thisScene = new Scene(loader.load());
        } catch(Exception e) {
            System.err.println("--- ERROR LOADING WELCOME SCENE! ---");
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        numberOfQuestionsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 10));
        categoryChoiceBox.getItems().addAll(CATEGORIES);
        categoryChoiceBox.getSelectionModel().select(0);
        difficultyChoiceBox.getItems().addAll(DIFFICULTIES);
        difficultyChoiceBox.getSelectionModel().select(0);
        typeChoiceBox.getItems().addAll(TYPES);
        typeChoiceBox.getSelectionModel().select(0);

        // Clear default focus on first element
        Platform.runLater(() -> titleLabel.requestFocus());
    }

    @FXML
    protected void onStartClicked() {
        int num = numberOfQuestionsSpinner.getValue();
        int categoryIndex = categoryChoiceBox.getSelectionModel().getSelectedIndex();
        int difficultyIndex = difficultyChoiceBox.getSelectionModel().getSelectedIndex();
        int typeIndex = typeChoiceBox.getSelectionModel().getSelectedIndex();
        scores = new int[num];

        try {
            questionList = QuestionRetriever.retrieveQuestionSet(num, categoryIndex+8, difficultyIndex, typeIndex);
        } catch (Exception e) {
            System.err.println("--- ERROR RETRIEVING QUESTIONS! ---");
            e.printStackTrace();
        }

        try {
            loadAndShowQuestionScene();
        } catch (Exception e) {
            System.err.println("--- ERROR LOADING QUESTION SCENE! ---");
            e.printStackTrace();
        }
    }

    private void loadAndShowQuestionScene() {
        QuestionController questionController = new QuestionController(this, null);
        questionControllerList.add(questionController);
        questionController.setAndShowScene();
    }

    public void setAndShowScene() {
        thisStage.setScene(thisScene);
        thisStage.setTitle("Quizinator");
        thisStage.show();
    }

    @FXML
    protected void onQuitClicked() {
        Platform.exit();
    }

    public Stage getThisStage() {
        return thisStage;
    }

    public void setScore(int index, int score) {
        this.scores[index] = score;
    }

    public int[] getScores() {
        return scores;
    }

    public List<QuizQuestion> getQuestionList() {
        return questionList;
    }

    public List<QuestionController> getQuestionControllerList() {
        return questionControllerList;
    }

}
