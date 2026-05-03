package com.example.furqanmyqurancompanion.Model;

import android.util.Log;

import com.example.furqanmyqurancompanion.BuildConfig;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.ListenableFuture;

public class DeepSearchService {
    private static final String TAG = "DeepSearchService";
    private final GenerativeModelFutures model;
    private static final String MODEL_NAME = "gemini-3-flash-preview";

    public DeepSearchService() {
        String apiKey = BuildConfig.GEMINI_API_KEY;
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("YOUR_API_KEY")) {
            Log.e(TAG, "GEMINI_API_KEY is missing or invalid in BuildConfig!");
        }
        GenerativeModel gm = new GenerativeModel(MODEL_NAME, apiKey);
        this.model = GenerativeModelFutures.from(gm);
    }

    public ListenableFuture<GenerateContentResponse> search(String query, String type) {
        String prompt = "You are an intelligent Islamic Search Assistant. The user is searching for something in " + type + ".\n" +
                "Query: \"" + query + "\"\n\n" +
                "Task: Find relevant " + type + " based on keywords, context, or semantic similarity.\n" +
                "Provide the response as a valid JSON array of objects. Each object must contain:\n" +
                "1. 'id': The integer " + type + " number (e.g., if type is Surah, return 1 to 114. If type is Juz, return 1 to 30).\n" +
                "2. 'title': A short descriptive title of why this is relevant.\n" +
                "3. 'description': A brief explanation (max 15 words).\n\n" +
                "Constraint: Return ONLY the raw JSON array. No markdown, no extra text. Ensure 'id' is always an integer.";

        Content content = new Content.Builder().addText(prompt).build();
        return model.generateContent(content);
    }
}
