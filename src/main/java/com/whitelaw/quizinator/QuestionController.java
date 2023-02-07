/**
 * QuestionController: Handles the question scene;
 * one question per instance of this controller.
 */

package com.whitelaw.quizinator;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import java.util.Random;

// Text getters are used to populate resultsTable
@SuppressWarnings("unused")
public class QuestionController {

    @FXML
    private Label questionNumberLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label difficultyLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label questionLabel;
    @FXML
    private RadioButton firstAnswerButton;
    @FXML
    private RadioButton secondAnswerButton;
    @FXML
    private RadioButton thirdAnswerButton;
    @FXML
    private RadioButton fourthAnswerButton;
    @FXML
    private Button nextDoneButton;
    @FXML
    private Button backButton;
    @FXML
    private ToggleGroup answerToggle;

    private final WelcomeController mainController;
    private final QuestionController prevController;
    private final QuizQuestion question;
    private final int questionNumber; // 0-indexed to match question list, but displayed as 1-indexed
    private final boolean isLastQuestion;

    private Scene thisScene;
    private int correctAnswerId;
    private String[] answers;
    private String questionText;
    private String correctAnswerText;
    private String selectedAnswerText;

    public QuestionController(WelcomeController mainController, QuestionController prevController) {
        this.mainController = mainController;
        this.prevController = prevController;

        // If no previous controller, this is the first question (0 index)
        // Otherwise, increment the question number from the previous controller
        this.questionNumber = (prevController == null) ? 0 : prevController.getQuestionNumber() + 1;

        this.question = mainController.getQuestionList().get(questionNumber);
        this.isLastQuestion = (questionNumber == mainController.getQuestionList().size() - 1);

        try {
            FXMLLoader loader = new FXMLLoader(QuizinatorApplication.class.getResource("fxml/question.fxml"));
            loader.setController(this);
            thisScene = new Scene(loader.load());
        } catch(Exception e) {
            System.err.println("--- ERROR LOADING QUESTION SCENE! ---");
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        // Set header labels
        questionNumberLabel.setText("# %d".formatted(questionNumber + 1));
        categoryLabel.setText(question.getCategory());
        difficultyLabel.setText(question.getDifficulty());
        typeLabel.setText(question.getType());
        questionLabel.setText(question.getQuestion());

        boolean isTrueFalse = (question.getIncorrect_answers().length == 1);
        if(isTrueFalse) {
            answers = new String[2];

            answers[0] = "False";
            answers[1] = "True";
            firstAnswerButton.setText("False");
            secondAnswerButton.setText("True");

            // Hide unused answer buttons
            thirdAnswerButton.setVisible(false);
            thirdAnswerButton.setManaged(false);
            fourthAnswerButton.setVisible(false);
            fourthAnswerButton.setManaged(false);


            correctAnswerId = question.getCorrect_answer().equals("True") ? 1 : 0;

        } else {
            answers = new String[4];

            // Randomly assign correct answer to one of the 4 answer buttons
            Random rand = new Random();
            correctAnswerId = rand.nextInt(4);

            // Build answer array from correct answer and incorrect answers
            int incorrectCountIndex = 0;
            for(int i = 0; i < 4; i++) {
                if(i == correctAnswerId) {
                    answers[i] = question.getCorrect_answer();
                } else {
                    answers[i] = question.getIncorrect_answers()[incorrectCountIndex];
                    incorrectCountIndex++;
                }
            }
            firstAnswerButton.setText(answers[0]);
            secondAnswerButton.setText(answers[1]);
            thirdAnswerButton.setText(answers[2]);
            fourthAnswerButton.setText(answers[3]);
        }

        // Set an id to each button to identify it later
        firstAnswerButton.setUserData(0);
        secondAnswerButton.setUserData(1);
        thirdAnswerButton.setUserData(2);
        fourthAnswerButton.setUserData(3);


        if(prevController == null)  backButton.setDisable(true);
        if(isLastQuestion) nextDoneButton.setText("Done");

        // Clear default focus on first element
        Platform.runLater(() -> questionNumberLabel.requestFocus());
    }

    @FXML
    private void onBackClicked() {
        if(prevController != null) {
            prevController.setAndShowScene();
        }
    }

    @FXML
    private void onNextClicked() {
        RadioButton selectedAnswer = (RadioButton) answerToggle.getSelectedToggle();
        if(selectedAnswer == null) {
            System.err.println("--- NO ANSWER SELECTED! ---");
        } else {
            int selectedAnswerId = (int) selectedAnswer.getUserData();
            calculateAndEnterScore(selectedAnswerId);

            if(isLastQuestion) {
                loadAndShowResultScene();
            } else {
                loadAndShowNextQuestionScene();
            }
        }
    }

    private void loadAndShowNextQuestionScene() {
        int nextIndex = questionNumber+1;

        QuestionController nextController;
        try {
            nextController = mainController.getQuestionControllerList().get(nextIndex);
        } catch (Exception e) {
            nextController = null;
        }

        if(nextController != null) {
            nextController.setAndShowScene();
        } else {
            QuestionController questionController = new QuestionController(mainController, this);
            mainController.getQuestionControllerList().add(questionController);
            questionController.setAndShowScene();
        }
    }

    private void loadAndShowResultScene() {
        ResultController resultController = new ResultController(mainController);
        resultController.setAndShowScene();
    }

    private void calculateAndEnterScore(int selectedAnswerId) {
        questionText = question.getQuestion();
        correctAnswerText = question.getCorrect_answer();
        selectedAnswerText = answers[selectedAnswerId];

        int questionScore = (selectedAnswerId == correctAnswerId) ? 1 : 0;
        mainController.setScore(questionNumber, questionScore);
    }

    public void setAndShowScene() {
        mainController.getThisStage().setScene(thisScene);
        mainController.getThisStage().setTitle("Quizinator - Quiz in Progress...");
    }

    public int getQuestionNumber() {
    	return questionNumber;
    }

    public String getQuestionText() {
    	return questionText;
    }

    public String getCorrectAnswerText() {
    	return correctAnswerText;
    }

    public String getSelectedAnswerText() {
        return selectedAnswerText;
    }

}
