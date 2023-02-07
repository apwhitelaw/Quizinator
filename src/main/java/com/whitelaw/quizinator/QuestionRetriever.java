package com.whitelaw.quizinator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QuestionRetriever {

    private static final String API_URL = "https://opentdb.com/api.php?";
    private static final String AMOUNT_URL = "amount=";
    private static final String CATEGORY_URL = "&category=";
    private static final String DIFFICULTY_URL = "&difficulty=";
    private static final String TYPE_URL = "&type=";

    private static final String[] difficultyRequestArray = {"any", "easy", "medium", "hard"};
    private static final String[] typeRequestArray = {"any", "multiple", "boolean"};

    public static List<QuizQuestion> retrieveQuestionSet(int amount, int category, int difficulty, int type) throws Exception {

        // Build API request URL
        String requestUrl = API_URL;
        requestUrl += AMOUNT_URL + amount;
        if(category != 8) {
            requestUrl += CATEGORY_URL + category;
        }
        if(difficulty != 0) {
            requestUrl += DIFFICULTY_URL + difficultyRequestArray[difficulty];
        }
        if(type != 0) {
            requestUrl += TYPE_URL + typeRequestArray[type];
        }

        System.out.println(requestUrl);

        // Get JSON result array from API response
        JSONObject json = new JSONObject(getHTML(requestUrl));
        JSONArray resultArray = json.getJSONArray("results");

        // Map JSON to Java Object
        ObjectMapper mapper = new ObjectMapper();
        List<QuizQuestionRaw> questionListRaw = mapper.readValue(resultArray.toString(), new TypeReference<>() {});

        // Transform list into QuizQuestion objects
        List<QuizQuestion> questionList = new ArrayList<>();
        for (QuizQuestionRaw questionRaw : questionListRaw) questionList.add(new QuizQuestion(questionRaw));

        return questionList;
    }

    // Send get request to API at urlString and return response as string
    private static String getHTML(String urlString) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }
        }

        return result.toString();
    }


}
