package com.example.furqanmyqurancompanion.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Surah_Data {

    @SerializedName("number")
    private int surahNumber;

    @SerializedName("name")
    private String arabicName;

    @SerializedName("englishName")
    private String englishName;

    @SerializedName("englishNameTranslation")
    private String englishNameTranslation;

    @SerializedName("revelationType")
    private String revelationType;

    @SerializedName("numberOfAyahs")
    private int numberOfAyahs;

    @SerializedName("ayahs")
    private List<Ayah_Data> ayahs;

    // Getters
    public int getSurahNumber() { return surahNumber; }
    public String getArabicName() { return arabicName; }
    public String getEnglishName() { return englishName; }
    public String getEnglishNameTranslation() { return englishNameTranslation; }
    public String getRevelationType() { return revelationType; }
    public int getNumberOfAyahs() { return numberOfAyahs; }
    public List<Ayah_Data> getAyahs() { return ayahs; }

    public void setSurahNumber(int surahNumber) { this.surahNumber = surahNumber; }
    public void setArabicName(String arabicName) { this.arabicName = arabicName; }
    public void setEnglishName(String englishName) { this.englishName = englishName; }
    public void setEnglishNameTranslation(String englishNameTranslation) { this.englishNameTranslation = englishNameTranslation; }
    public void setRevelationType(String revelationType) { this.revelationType = revelationType; }
    public void setNumberOfAyahs(int numberOfAyahs) { this.numberOfAyahs = numberOfAyahs; }
    public void setAyahs(List<Ayah_Data> ayahs) { this.ayahs = ayahs; }
}
