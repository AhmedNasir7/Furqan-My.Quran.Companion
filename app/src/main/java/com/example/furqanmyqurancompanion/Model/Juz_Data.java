package com.example.furqanmyqurancompanion.Model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Juz_Data {
    @SerializedName("number")
    private int number;

    @SerializedName("ayahs")
    private List<Ayah_Data> ayahs;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Ayah_Data> getAyahs() {
        return ayahs;
    }

    public void setAyahs(List<Ayah_Data> ayahs) {
        this.ayahs = ayahs;
    }
}
