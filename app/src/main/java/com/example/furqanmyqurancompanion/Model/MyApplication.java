package com.example.furqanmyqurancompanion.Model;

import android.app.Application;
import android.widget.Toast;

import com.example.furqanmyqurancompanion.Api.Api_Client;
import com.example.furqanmyqurancompanion.Api.QuranAPiService;
import com.example.furqanmyqurancompanion.Database.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApplication extends Application {

    private String Juz_Clicked = null;
    private String Surah_Clicked = null;
    private List<Surah_Metadata> surahs_metadata = new ArrayList<>();
    private List<Integer> juz_list = new ArrayList<>();
    private DatabaseHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DatabaseHelper(this);
        
        if (dbHelper.isSurahMetadataLoaded()) {
            surahs_metadata.addAll(dbHelper.getAllSurahMetadata());
        } else {
            fetchSurahData();
        }

        if (juz_list.isEmpty()) {
            for (int i = 1; i <= 30; i++) {
                juz_list.add(i);
            }
        }

        if (!dbHelper.isQuranLoaded()) {
            downloadAllQuran();
        }
    }

    private void downloadAllQuran() {
        new Thread(() -> {
            QuranAPiService apiService = Api_Client.getQuranApiService();
            for (int i = 1; i <= 114; i++) {
                final int surahNum = i;
                apiService.getSurahWithTranslation(surahNum).enqueue(new Callback<SurahContentResponse>() {
                    @Override
                    public void onResponse(Call<SurahContentResponse> call, Response<SurahContentResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            List<Surah_Data> surahs = response.body().getData();
                            if (surahs.size() >= 2) {
                                Surah_Data arabicSurah = surahs.get(0);
                                Surah_Data englishSurah = surahs.get(1);
                                for (int m = 0; m < arabicSurah.getAyahs().size(); m++) {
                                    Ayah_Data ayah = arabicSurah.getAyahs().get(m);
                                    ayah.setTranslation(englishSurah.getAyahs().get(m).getArabicText());
                                    dbHelper.addAyah(ayah, surahNum);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SurahContentResponse> call, Throwable t) {
                    }
                });
                // Small delay to avoid hitting API rate limits too hard
                try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }).start();
    }

    public boolean isGuest() {
        SharedPreferences spref = getSharedPreferences("user", MODE_PRIVATE);
        return spref.getBoolean("isGuest", false);
    }

    public String getCurrentUserId() {
        if (isGuest()) {
            return "guest_user";
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return "guest_user"; // Default to guest if not logged in
    }

    public DatabaseHelper getDbHelper() {
        return dbHelper;
    }

    public String getJuz_Clicked() {
        return Juz_Clicked;
    }

    public void setJuz_Clicked(String juz_Clicked) {
        Juz_Clicked = juz_Clicked;
        Surah_Clicked = null;
    }

    public String getSurah_Clicked() {
        return Surah_Clicked;
    }

    public void setSurah_Clicked(String surah_Clicked) {
        Surah_Clicked = surah_Clicked;
        Juz_Clicked = null; // Clear juz if surah is set
    }

    public List<Surah_Metadata> getSurahs_metadata() {
        return surahs_metadata;
    }

    public void setSurahs_metadata(List<Surah_Metadata> surahs_metadata) {
        this.surahs_metadata = surahs_metadata;
    }

    public List<Integer> getJuz_list() {
        return juz_list;
    }

    public void setJuz_list(List<Integer> juz_list) {
        this.juz_list = juz_list;
    }

    private void fetchSurahData() {
        QuranAPiService apiService = Api_Client.getQuranApiService();
        Call<SurahResponse> call = apiService.getAllSurahs_MetaData();

        call.enqueue(new Callback<SurahResponse>() {
            @Override
            public void onResponse(Call<SurahResponse> call, Response<SurahResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    surahs_metadata.clear();
                    surahs_metadata.addAll(response.body().getData());
                    new Thread(() -> {
                        for (Surah_Metadata surah : response.body().getData()) {
                            dbHelper.addSurahMetadata(surah);
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<SurahResponse> call, Throwable t) {
                // Application context toast is fine here
            }
        });
    }
}
