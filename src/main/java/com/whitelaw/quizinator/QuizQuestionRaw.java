/**
 * The QuizQuestionRaw class is a POJO that represents
 * the raw JSON. We use this in order to map the JSON
 * response to a Java object with Jackson's ObjectMapper.
 * Then we can use the QuizQuestionRaw object to create
 * a QuizQuestion object, which will contain the same
 * properties as well as more properties for
 * convenience.
 */

package com.whitelaw.quizinator;

// all fields are used by Jackson
@SuppressWarnings("unused")
public class QuizQuestionRaw {


    private String category;
    private String type;
    private String difficulty;
    private String question;
    private String correct_answer;
    private String[] incorrect_answers;

    public QuizQuestionRaw() {
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
