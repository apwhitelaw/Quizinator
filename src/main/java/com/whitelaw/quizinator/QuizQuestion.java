/**
 * QuizQuestion: Main question object; wraps the raw object from the API
 * while also adding some extra functionality.
 */

package com.whitelaw.quizinator;

import static org.apache.commons.text.StringEscapeUtils.unescapeHtml4;

public class QuizQuestion {

    private String category;
    private String type;
    private String difficulty;
    private String question;
    private String correct_answer;
    private String[] incorrect_answers;


    public QuizQuestion() {
    }

    public QuizQuestion(QuizQuestionRaw quizQuestionRaw) {
        this.category = quizQuestionRaw.getCategory();
        this.difficulty = quizQuestionRaw.getDifficulty();
        this.type = quizQuestionRaw.getType();
        this.question = unescapeHtml4(quizQuestionRaw.getQuestion());
        this.correct_answer = unescapeHtml4(quizQuestionRaw.getCorrect_answer());
        this.incorrect_answers = quizQuestionRaw.getIncorrect_answers();
        for(int i = 0; i < incorrect_answers.length; i++) {
            incorrect_answers[i] = unescapeHtml4(incorrect_answers[i]);
        }

        // Enhanced switches to get difficulty index and type index
        int difficultyIndex;
        switch (difficulty) {
            case "easy" -> difficultyIndex = 1;
            case "medium" -> difficultyIndex = 2;
            case "hard" -> difficultyIndex = 3;
            default -> difficultyIndex = 0;
        }

        int typeIndex;
        switch (type) {
            case "multiple" -> typeIndex = 1;
            case "boolean" -> typeIndex = 2;
            default -> typeIndex = 0;
        }

        difficulty = WelcomeController.DIFFICULTIES[difficultyIndex];
        type = WelcomeController.TYPES[typeIndex];
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public String[] getIncorrect_answers() {
        return incorrect_answers;
    }
}
