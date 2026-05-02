package com.example.furqanmyqurancompanion.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.furqanmyqurancompanion.BuildConfig;
import com.example.furqanmyqurancompanion.Model.AiLinguisticService;
import com.example.furqanmyqurancompanion.R;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executors;

public class AiInsightActivity extends AppCompatActivity {

    private static final String TAG = "AiInsightActivity";

    private TextView tvAiResponse, tvOriginalVerse;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_insight);

        tvAiResponse = findViewById(R.id.tvAiResponse);
        tvOriginalVerse = findViewById(R.id.tvOriginalVerse);
        progressBar = findViewById(R.id.aiLoadingProgress);
        findViewById(R.id.btnBackAi).setOnClickListener(v -> finish());

        String arabicText = getIntent().getStringExtra("arabic_text");
        String translation = getIntent().getStringExtra("translation");

        if (arabicText == null || translation == null) {
            Log.e(TAG, "Missing required intent extras");
            finish();
            return;
        }

        tvOriginalVerse.setText(arabicText);
        fetchLinguisticInsight(arabicText, translation);
    }

    private void fetchLinguisticInsight(String arabic, String translation) {
        progressBar.setVisibility(View.VISIBLE);
        tvAiResponse.setText(R.string.consulting_ai);

        // FIXED: Use API key from BuildConfig instead of hardcoded
        String apiKey = BuildConfig.GEMINI_API_KEY;

        if (apiKey == null || apiKey.isEmpty()) {
            Log.e(TAG, "API key is not configured");
            tvAiResponse.setText("Error: API key not configured. Check your local.properties.");
            progressBar.setVisibility(View.GONE);
            return;
        }

        try {
            AiLinguisticService aiService = new AiLinguisticService(apiKey);
            ListenableFuture<GenerateContentResponse> future = aiService.getLinguisticAnalysis(arabic, translation);

            Futures.addCallback(future, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    runOnUiThread(() -> {
                        if (isFinishing()) return;
                        progressBar.setVisibility(View.GONE);

                        if (result != null && result.getText() != null) {
                            Log.d(TAG, "Successfully retrieved response: " + result.getText().substring(0, Math.min(result.getText().length(), 100)));
                            tvAiResponse.setText(result.getText());
                        } else {
                            Log.w(TAG, "Response or text is null");
                            tvAiResponse.setText(R.string.ai_empty_response);
                        }
                    });
                }

                @Override
                public void onFailure(Throwable t) {
                    runOnUiThread(() -> {
                        if (isFinishing()) return;
                        progressBar.setVisibility(View.GONE);

                        // Enhanced error logging
                        Log.e(TAG, "API call failed", t);
                        String errorMsg = t.getMessage() != null ? t.getMessage() : "Unknown error";

                        // Provide specific guidance based on error type
                        String displayMsg;
                        if (errorMsg.contains("404")) {
                            displayMsg = "Error 404: Model not found.\n\nTroubleshooting:\n" +
                                    "1. Verify your API key is valid\n" +
                                    "2. Check your Google API Console for available models\n" +
                                    "3. Try using 'gemini-pro' instead of 'gemini-1.5-flash'";
                        } else if (errorMsg.contains("401")) {
                            displayMsg = "Error 401: Invalid API key.\nCheck your credentials in local.properties.";
                        } else if (errorMsg.contains("GRpcError")) {
                            displayMsg = "Error: Missing dependency. Add to your build.gradle:\n" +
                                    "implementation 'com.google.ai.client.generativeai:google-generativeai-android:0.3.0'";
                        } else {
                            displayMsg = getString(R.string.failed_to_retrieve_analysis) + errorMsg;
                        }

                        tvAiResponse.setText(displayMsg);
                        Toast.makeText(AiInsightActivity.this, "API Error: " + errorMsg, Toast.LENGTH_LONG).show();
                    });
                }
            }, Executors.newSingleThreadExecutor());
        } catch (Exception e) {
            Log.e(TAG, "Exception during initialization", e);
            tvAiResponse.setText("Error initializing AI service: " + e.getMessage());
            progressBar.setVisibility(View.GONE);
        }
    }
}