package mos.quarkus.play.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.enterprise.context.ApplicationScoped;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class OpenAIService {

    final private OkHttpClient httpClient = new OkHttpClient();


    public String requestDatingChatAnswer(String systemInstruction, List<String> chatHistory, String apiKey) throws IOException {
        ArrayList<String> chatWithInstruction = new ArrayList<>(List.of(systemInstruction));
        chatWithInstruction.addAll(chatHistory);
        String openAIRequest = gptRequestWithHistory(chatWithInstruction);
        String jsonResponse = doRequest(openAIRequest, apiKey);
        return extractResponseMessage(jsonResponse);
    }


    private String extractResponseMessage(String jsonResponse) throws IOException {
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> map = gson.fromJson(jsonResponse, mapType);
        List<Map<String,Object>> choices = (List<Map<String, Object>>) map.get("choices");
        if (choices == null || choices.isEmpty() || choices.getFirst().get("message") == null) {
            throw new IOException("Answer from OpenAI GPT does not contain a response (choices): " +choices);
        }
        return (String) ((Map)choices.getFirst().get("message")).get("content");
    }

    private String doRequest(String jsonRequest, String apiKey) throws IOException {
        RequestBody body = RequestBody.create(jsonRequest, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Failed to request OpenAI -->  " + response.code() + "  --> Body: "+ (response.body() != null ? response.body().string() : "----empty----"));
            }
            return response.body().string();
        }
    }


    private String gptRequestWithHistory(List<String> chatHistory) {
        Map<String, Object> content = new LinkedHashMap<>();
        content.put("model", "gpt-3.5-turbo");
        content.put("temperature", 1);
        content.put("max_tokens", 256);
        content.put("top_p", 1);
        content.put("frequency_penalty", 0.1);
        content.put("presence_penalty", 0.5);
        List<Map<String, String>> messages = new ArrayList<>();
        content.put("messages", messages);
        for (int i = 0; i < chatHistory.size(); i++) {
            String role;
            if (i == 0) {
                role = "system";
            } else if (i % 2 == 1) {
                role = "assistant";
            } else {
                role = "user";
            }
            messages.add(Map.of("role", role, "content", chatHistory.get(i)));
        }
        Gson gson = new Gson();
        return gson.toJson(content);
    }


}
