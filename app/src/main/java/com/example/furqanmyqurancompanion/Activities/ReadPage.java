package com.example.furqanmyqurancompanion.Activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furqanmyqurancompanion.Adapters.Ayah_RecyclerAdapter;
import com.example.furqanmyqurancompanion.Api.Api_Client;
import com.example.furqanmyqurancompanion.Api.QuranAPiService;
import com.example.furqanmyqurancompanion.Database.DatabaseHelper;
import com.example.furqanmyqurancompanion.Model.Ayah_Data;
import com.example.furqanmyqurancompanion.Model.JuzResponse;
import com.example.furqanmyqurancompanion.Model.Juz_Data;
import com.example.furqanmyqurancompanion.Model.MyApplication;
import com.example.furqanmyqurancompanion.Model.SurahContentResponse;
import com.example.furqanmyqurancompanion.Model.Surah_Data;
import com.example.furqanmyqurancompanion.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadPage extends AppCompatActivity {

    TextView tvReadPageTitle;
    RecyclerView rvAyahs;
    Ayah_RecyclerAdapter adapter;
    List<Ayah_Data> ayahList = new ArrayList<>();
    ImageView btnMenu;
    private String currentType; // "surah" or "juz"
    private int currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_read_page);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.readHeader), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        loadContent();
    }

    private void init() {
        tvReadPageTitle = findViewById(R.id.tvReadPageTitle);
        rvAyahs = findViewById(R.id.rvAyahs);
        btnMenu = findViewById(R.id.btnMenu);

        rvAyahs.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Ayah_RecyclerAdapter(this, ayahList);
        rvAyahs.setAdapter(adapter);

        btnMenu.setOnClickListener(v -> finish());
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveProgress();
    }

    private void saveProgress() {
        if (ayahList == null || ayahList.isEmpty()) return;

        LinearLayoutManager layoutManager = (LinearLayoutManager) rvAyahs.getLayoutManager();
        if (layoutManager != null) {
            int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
            if (firstVisiblePosition != RecyclerView.NO_POSITION) {
                Ayah_Data lastReadAyah = ayahList.get(firstVisiblePosition);
                MyApplication application = (MyApplication) getApplicationContext();
                application.getDbHelper().updateReadingProgress(
                        application.getCurrentUserId(),
                        currentType,
                        currentId,
                        lastReadAyah.getGlobalVerseNumber()
                );
            }
        }
    }

    private void loadContent() {
        MyApplication application = (MyApplication) getApplicationContext();
        String surahNum = application.getSurah_Clicked();
        String juzNum = application.getJuz_Clicked();

        if (surahNum != null) {
            currentType = "surah";
            currentId = Integer.parseInt(surahNum);
            fetchSurah(currentId);
        } else if (juzNum != null) {
            currentType = "juz";
            currentId = Integer.parseInt(juzNum);
            fetchJuz(currentId);
        }
    }

    private void fetchSurah(int num) {
        MyApplication application = (MyApplication) getApplicationContext();
        DatabaseHelper dbHelper = application.getDbHelper();
        String userId = application.getCurrentUserId();
        List<Ayah_Data> cachedAyahs = dbHelper.getAyahsForSurah(num, userId);

        if (!cachedAyahs.isEmpty()) {
            ayahList.clear();
            ayahList.addAll(cachedAyahs);
            // We need to set the title correctly. We can get it from application's metadata list.
            for (com.example.furqanmyqurancompanion.Model.Surah_Metadata metadata : application.getSurahs_metadata()) {
                if (metadata.getSurah_number() == num) {
                    tvReadPageTitle.setText(metadata.getSurah_english_name() + "\nSurah " + num);
                    break;
                }
            }
            adapter.notifyDataSetChanged();
            scrollToLastPosition();
            return;
        }

        QuranAPiService apiService = Api_Client.getQuranApiService();
        apiService.getSurahWithTranslation(num).enqueue(new Callback<SurahContentResponse>() {
            @Override
            public void onResponse(@NonNull Call<SurahContentResponse> call, @NonNull Response<SurahContentResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Surah_Data> surahs = response.body().getData();
                    if (surahs.size() >= 2) {
                        Surah_Data arabicSurah = surahs.get(0);
                        Surah_Data englishSurah = surahs.get(1);

                        ayahList.clear();
                        String bismillah = getString(R.string.Bismillah_Splash_Text);
                        for (int m = 0; m < arabicSurah.getAyahs().size(); m++) {
                            Ayah_Data ayah = arabicSurah.getAyahs().get(m);
                            String text = ayah.getArabicText();

                            // Strip Bismillah if it's the first ayah of any surah except Surah 1 and 9
                            if (ayah.getVerseNumber() == 1 && arabicSurah.getSurahNumber() != 1 && arabicSurah.getSurahNumber() != 9) {
                                if (text.startsWith(bismillah)) {
                                    text = text.substring(bismillah.length()).trim();
                                    ayah.setArabicText(text);
                                }
                            }

                            ayah.setTranslation(englishSurah.getAyahs().get(m).getArabicText());
                            ayahList.add(ayah);
                            // Cache it
                            dbHelper.addAyah(ayah, num);
                        }
                        tvReadPageTitle.setText(arabicSurah.getEnglishName() + "\nSurah " + arabicSurah.getSurahNumber());
                        adapter.notifyDataSetChanged();
                        scrollToLastPosition();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SurahContentResponse> call, @NonNull Throwable t) {
                Toast.makeText(ReadPage.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchJuz(int num) {
        MyApplication application = (MyApplication) getApplicationContext();
        DatabaseHelper dbHelper = application.getDbHelper();
        String userId = application.getCurrentUserId();
        List<Ayah_Data> cachedAyahs = dbHelper.getAyahsForJuz(num, userId);

        if (!cachedAyahs.isEmpty()) {
            ayahList.clear();
            ayahList.addAll(cachedAyahs);
            tvReadPageTitle.setText("Juz " + num);
            adapter.notifyDataSetChanged();
            scrollToLastPosition();
            return;
        }

        QuranAPiService apiService = Api_Client.getQuranApiService();
        apiService.getJuzWithTranslation(num).enqueue(new Callback<JuzResponse>() {
            @Override
            public void onResponse(@NonNull Call<JuzResponse> call, @NonNull Response<JuzResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Juz_Data> juzs = response.body().getData();
                    if (juzs.size() >= 2) {
                        Juz_Data arabicJuz = juzs.get(0);
                        Juz_Data englishJuz = juzs.get(1);

                        ayahList.clear();
                        String bismillah = getString(R.string.Bismillah_Splash_Text);
                        for (int k = 0; k < arabicJuz.getAyahs().size(); k++) {
                            Ayah_Data ayah = arabicJuz.getAyahs().get(k);
                            String text = ayah.getArabicText();

                            if (ayah.getVerseNumber() == 1) {
                                boolean isFatihah = (ayah.getSurah() != null && ayah.getSurah().getSurah_number() == 1) || ayah.getGlobalVerseNumber() == 1;
                                boolean isTawbah = (ayah.getSurah() != null && ayah.getSurah().getSurah_number() == 9);
                                
                                if (!isFatihah && !isTawbah) {
                                    if (text.startsWith(bismillah)) {
                                        text = text.substring(bismillah.length()).trim();
                                        ayah.setArabicText(text);
                                    }
                                }
                            }

                            ayah.setTranslation(englishJuz.getAyahs().get(k).getArabicText());
                            ayahList.add(ayah);
                            int surahId = (ayah.getSurah() != null) ? ayah.getSurah().getSurah_number() : -1;
                            dbHelper.addAyah(ayah, surahId);
                        }
                        tvReadPageTitle.setText("Juz " + num);
                        adapter.notifyDataSetChanged();
                        scrollToLastPosition();
                    } else {
                        Toast.makeText(ReadPage.this, "Unexpected API response format", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ReadPage.this, "Failed to fetch Juz", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JuzResponse> call, @NonNull Throwable t) {
                Toast.makeText(ReadPage.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scrollToLastPosition() {
        MyApplication application = (MyApplication) getApplicationContext();
        android.database.Cursor cursor = application.getDbHelper().getReadingProgress(application.getCurrentUserId());
        if (cursor.moveToFirst()) {
            int typeIndex = cursor.getColumnIndex("type");
            int idIndex = cursor.getColumnIndex("id");
            int ayahIdIndex = cursor.getColumnIndex("ayah_id");

            if (typeIndex != -1 && idIndex != -1 && ayahIdIndex != -1) {
                String type = cursor.getString(typeIndex);
                int id = cursor.getInt(idIndex);
                int lastAyahGlobalId = cursor.getInt(ayahIdIndex);

                if (currentType.equals(type) && currentId == id) {
                    for (int i = 0; i < ayahList.size(); i++) {
                        if (ayahList.get(i).getGlobalVerseNumber() == lastAyahGlobalId) {
                            final int position = i;
                            rvAyahs.post(() -> rvAyahs.scrollToPosition(position));
                            break;
                        }
                    }
                }
            }
        }
        cursor.close();
    }
}
