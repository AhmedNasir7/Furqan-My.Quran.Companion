package com.example.furqanmyqurancompanion.Model;

import com.google.gson.annotations.SerializedName;

public class Reciter {
    @SerializedName("identifier")
    private String identifier;

    @SerializedName("name")
    private String name;

    @SerializedName("englishName")
    private String englishName;

    public String getIdentifier() { return identifier; }
    public String getName() { return name; }
    public String getEnglishName() { return englishName; }
}
