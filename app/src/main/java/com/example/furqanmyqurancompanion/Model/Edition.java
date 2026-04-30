package com.example.furqanmyqurancompanion.Model;

import com.google.gson.annotations.SerializedName;

public class Edition {
    @SerializedName("identifier")
    private String identifier;

    @SerializedName("language")
    private String language;

    @SerializedName("name")
    private String name;

    @SerializedName("englishName")
    private String englishName;

    @SerializedName("format")
    private String format;

    @SerializedName("type")
    private String type;

    @SerializedName("direction")
    private String direction;

    @SerializedName("text")
    private String text;

    public String getIdentifier() { return identifier; }
    public String getLanguage() { return language; }
    public String getName() { return name; }
    public String getEnglishName() { return englishName; }
    public String getFormat() { return format; }
    public String getType() { return type; }
    public String getDirection() { return direction; }
    public String getText() { return text; }

    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public void setLanguage(String language) { this.language = language; }
    public void setName(String name) { this.name = name; }
    public void setEnglishName(String englishName) { this.englishName = englishName; }
    public void setFormat(String format) { this.format = format; }
    public void setType(String type) { this.type = type; }
    public void setDirection(String direction) { this.direction = direction; }
    public void setText(String text) { this.text = text; }
}
