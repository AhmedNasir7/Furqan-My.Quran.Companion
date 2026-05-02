package com.example.furqanmyqurancompanion.Model;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.ListenableFuture;

public class AiLinguisticService {
    private final GenerativeModelFutures model;
    private static final String MODEL_NAME = "gemini-1.5-flash";
    public AiLinguisticService(String apiKey) {
        // Updated to use the standard "gemini-1.5-flash" model name
        GenerativeModel gm = new GenerativeModel(MODEL_NAME, apiKey);
        this.model = GenerativeModelFutures.from(gm);
    }

    public ListenableFuture<GenerateContentResponse> getLinguisticAnalysis(String verseArabic, String verseTranslation) {
        String prompt = "Act as a Classical Arabic Linguistic Assistant. Analyze the following Quranic verse.\n" +
                "Verse: " + verseArabic + "\n" +
                "Translation: " + verseTranslation + "\n\n" +
                "Provide the following details in a clean, structured format with clear headings:\n" +
                "1. **Root Letters (Huruf al-Asliyah)** of the key words.\n" +
                "2. **Morphological Breakdown** (Noun/Verb/Particle analysis).\n" +
                "3. **Grammatical Context (I'rab)** for the main components.\n" +
                "4. **Cross-reference**: Mention 1 other verse where a primary root from this verse is used.\n\n" +
                "**Strict Rule**: Do not provide spiritual, sectarian, or personal interpretations. Stick to linguistics and grammar based on classical dictionaries like Lisan al-Arab.";

        return generateContent(prompt);
    }

    public ListenableFuture<GenerateContentResponse> generateContent(String prompt) {
        Content content = new Content.Builder().addText(prompt).build();
        return model.generateContent(content);
    }
}