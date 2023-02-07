package com.whitelaw.quizinator;

import javafx.application.Application;
import javafx.stage.Stage;

public class QuizinatorApplication extends Application {
    @Override
    public void start(Stage stage) {
        WelcomeController welcomeController = new WelcomeController(new Stage());
        welcomeController.setAndShowScene();
    }

    public static void main(String[] args) {
        launch();
    }
}