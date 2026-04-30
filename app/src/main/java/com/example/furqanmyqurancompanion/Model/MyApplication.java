package com.example.furqanmyqurancompanion.Model;

import android.app.Application;
import android.widget.Toast;

import com.example.furqanmyqurancompanion.Api.Api_Client;
import com.example.furqanmyqurancompanion.Api.QuranAPiService;

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

    @Override
    public void onCreate() {
        super.onCreate();
        fetchSurahData();
        if (juz_list.isEmpty()) {
            for (int i = 1; i <= 30; i++) {
                juz_list.add(i);
            }
        }
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
                }
            }

            @Override
            public void onFailure(Call<SurahResponse> call, Throwable t) {
                // Application context toast is fine here
            }
        });
    }
}
