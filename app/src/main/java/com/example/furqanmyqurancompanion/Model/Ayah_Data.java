package com.example.furqanmyqurancompanion.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Ayah_Data {
    @SerializedName("number")
    private int globalVerseNumber;

    @SerializedName("numberInSurah")
    private int verseNumber;

    @SerializedName("text")
    private String arabicText;

    @SerializedName("juz")
    private int juzNumber;

    @SerializedName("page")
    private int pageNumber;

    @SerializedName("manzil")
    private int manzilNumber;

    @SerializedName("ruku")
    private int rukuNumber;

    @SerializedName("hizbQuarter")
    private int hizbQuarter;

    @SerializedName("sajda")
    private Object sajda;

    @SerializedName("surah")
    private Surah_Metadata surah;

    @SerializedName("audio")
    private String audioUrl;

    private String translation;

    private boolean isBookmarked;

    // Getters
    public int getGlobalVerseNumber() { return globalVerseNumber; }
    public int getVerseNumber() { return verseNumber; }
    public String getArabicText() { return arabicText; }
    public int getJuzNumber() { return juzNumber; }
    public int getPageNumber() { return pageNumber; }
    public int getManzilNumber() { return manzilNumber; }
    public int getRukuNumber() { return rukuNumber; }
    public int getHizbQuarter() { return hizbQuarter; }
    public boolean isSajda() {
        if (sajda instanceof Boolean) {
            return (Boolean) sajda;
        }
        // If it's not a boolean, and not null, it's the Sajda object returned by the API
        return sajda != null;
    }
    public Surah_Metadata getSurah() { return surah; }
    public String getTranslation() { return translation; }
    public boolean isBookmarked() { return isBookmarked; }
    public String getAudioUrl() { return audioUrl; }

    // Setters
    public void setGlobalVerseNumber(int globalVerseNumber) { this.globalVerseNumber = globalVerseNumber; }
    public void setVerseNumber(int verseNumber) { this.verseNumber = verseNumber; }
    public void setArabicText(String arabicText) { this.arabicText = arabicText; }
    public void setJuzNumber(int juzNumber) { this.juzNumber = juzNumber; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }
    public void setManzilNumber(int manzilNumber) { this.manzilNumber = manzilNumber; }
    public void setRukuNumber(int rukuNumber) { this.rukuNumber = rukuNumber; }
    public void setHizbQuarter(int hizbQuarter) { this.hizbQuarter = hizbQuarter; }
    public void setIsSajda(boolean isSajda) { this.sajda = isSajda; }
    public void setSurah(Surah_Metadata surah) { this.surah = surah; }
    public void setTranslation(String translation) { this.translation = translation; }
    public void setBookmarked(boolean bookmarked) { isBookmarked = bookmarked; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }
}
