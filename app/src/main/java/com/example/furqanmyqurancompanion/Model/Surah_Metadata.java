package com.example.furqanmyqurancompanion.Model;

import com.google.gson.annotations.SerializedName;

public class Surah_Metadata {
    @SerializedName("number")
    int Surah_number;
    @SerializedName("name")
    String Surah_arabic_name;
    @SerializedName("englishName")
    String Surah_english_name;
    @SerializedName("englishNameTranslation")
    String Surah_english_meaning;
    @SerializedName("numberOfAyahs")
    int Surah_ayahs;

    public Surah_Metadata(int surah_number, String surah_arabic_name, String surah_english_name, String surah_english_meaning, int surah_ayahs) {
        Surah_number = surah_number;
        Surah_arabic_name = surah_arabic_name;
        Surah_english_name = surah_english_name;
        Surah_english_meaning = surah_english_meaning;
        Surah_ayahs = surah_ayahs;
    }

    public int getSurah_number() {
        return Surah_number;
    }

    public void setSurah_number(int surah_number) {
        Surah_number = surah_number;
    }

    public String getSurah_arabic_name() {
        return Surah_arabic_name;
    }

    public void setSurah_arabic_name(String surah_arabic_name) {
        Surah_arabic_name = surah_arabic_name;
    }

    public String getSurah_english_name() {
        return Surah_english_name;
    }

    public void setSurah_english_name(String surah_english_name) {
        Surah_english_name = surah_english_name;
    }

    public String getSurah_english_meaning() {
        return Surah_english_meaning;
    }

    public void setSurah_english_meaning(String surah_english_meaning) {
        Surah_english_meaning = surah_english_meaning;
    }

    public int getSurah_ayahs() {
        return Surah_ayahs;
    }

    public void setSurah_ayahs(int surah_ayahs) {
        Surah_ayahs = surah_ayahs;
    }
}
