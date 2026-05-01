package com.example.furqanmyqurancompanion.Api;

import com.example.furqanmyqurancompanion.Model.AyahEditionsResponse;
import com.example.furqanmyqurancompanion.Model.JuzResponse;
import com.example.furqanmyqurancompanion.Model.ReciterResponse;
import com.example.furqanmyqurancompanion.Model.SurahContentResponse;
import com.example.furqanmyqurancompanion.Model.SurahResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface QuranAPiService {
    @GET("surah")
    Call<SurahResponse> getAllSurahs_MetaData();

    @GET("juz/{juzNumber}/quran-uthmani,en.sahih")
    Call<JuzResponse> getJuzWithTranslation(@Path("juzNumber") int juzNumber);

    @GET("surah/{surahNumber}/editions/quran-uthmani,en.sahih")
    Call<SurahContentResponse> getSurahWithTranslation(@Path("surahNumber") int surahNumber);

    @GET("ayah/{ayahNumber}/editions/quran-uthmani,en.sahih,en.asad")
    Call<AyahEditionsResponse> getAyahWithTafseer(@Path("ayahNumber") int ayahNumber);

    @GET("edition?format=audio&language=ar")
    Call<ReciterResponse> getReciters();

    @GET("surah/{surahNumber}/editions/quran-uthmani,en.sahih,{edition}")
    Call<SurahContentResponse> getSurahWithAudio(@Path("surahNumber") int surahNumber, @Path("edition") String edition);
}
