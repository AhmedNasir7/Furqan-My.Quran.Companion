package com.example.furqanmyqurancompanion.Model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Prayer implements Serializable {
    @SerializedName("name")
    private String name;
    
    @SerializedName("steps")
    private List<Step> steps;

    public Prayer(String name, List<Step> steps) {
        this.name = name;
        this.steps = steps;
    }

    public String getName() { return name; }
    public List<Step> getSteps() { return steps; }

    public static class Step implements Serializable {
        @SerializedName("title")
        private String title;
        
        @SerializedName("description")
        private String description;
        
        @SerializedName("arabicText")
        private String arabicText;

        public Step(String title, String description, String arabicText) {
            this.title = title;
            this.description = description;
            this.arabicText = arabicText;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getArabicText() { return arabicText; }
    }
}
