package com.example.furqanmyqurancompanion.Activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.furqanmyqurancompanion.Api.Api_Client;
import com.example.furqanmyqurancompanion.Model.AyahEditionsResponse;
import com.example.furqanmyqurancompanion.Model.Ayah_Data;
import com.example.furqanmyqurancompanion.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TafseerActivity extends AppCompatActivity {
    TextView tvTafseer, tvAyahArabic, tvAyahTranslation;
    int ayahId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tafseer);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ayahId = getIntent().getIntExtra("ayah_global_id", 1);
        initViews();
        fetchTafseer();
    }

    private void initViews() {
        tvTafseer = findViewById(R.id.tvTafseer);
        tvAyahArabic = findViewById(R.id.tvAyahArabic);
        tvAyahTranslation = findViewById(R.id.tvAyahTranslation);
    }

    private void fetchTafseer() {
        Api_Client.getQuranApiService().getAyahWithTafseer(ayahId).enqueue(new Callback<AyahEditionsResponse>() {
            @Override
            public void onResponse(Call<AyahEditionsResponse> call, Response<AyahEditionsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Ayah_Data> editions = response.body().getData();
                    // API returns: Index 0: Arabic, Index 1: Translation (Sahih), Index 2: Tafseer (Asad)
                    if (editions != null && editions.size() >= 3) {
                        if (tvAyahArabic != null) tvAyahArabic.setText(editions.get(0).getArabicText());
                        if (tvAyahTranslation != null) tvAyahTranslation.setText(editions.get(1).getArabicText());
                        tvTafseer.setText(editions.get(2).getArabicText());
                    }
                } else {
                    Toast.makeText(TafseerActivity.this, "Failed to load Tafseer", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AyahEditionsResponse> call, Throwable t) {
                Toast.makeText(TafseerActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
