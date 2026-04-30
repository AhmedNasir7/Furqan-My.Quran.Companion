package com.example.furqanmyqurancompanion.Model;

import java.util.List;

public class SurahResponse {
    private int code;
    private String status;
    private List<Surah_Metadata> data;

    public int getCode() { return code; }
    public String getStatus() { return status; }
    public List<Surah_Metadata> getData() { return data; }
}