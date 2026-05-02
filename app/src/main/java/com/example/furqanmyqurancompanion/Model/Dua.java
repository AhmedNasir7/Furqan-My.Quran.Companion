package com.example.furqanmyqurancompanion.Model;

public class Dua {
    private String title;
    private String purpose;
    private String arabic;
    private String translation;

    public Dua(String title, String purpose, String arabic, String translation) {
        this.title = title;
        this.purpose = purpose;
        this.arabic = arabic;
        this.translation = translation;
    }

    public String getTitle() { return title; }
    public String getPurpose() { return purpose; }
    public String getArabic() { return arabic; }
    public String getTranslation() { return translation; }
}
