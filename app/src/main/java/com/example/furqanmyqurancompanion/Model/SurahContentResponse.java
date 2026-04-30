package com.example.furqanmyqurancompanion.Model;

import java.util.List;

public class SurahContentResponse {
    private int code;
    private String status;
    private List<Surah_Data> data;

    public int getCode() { return code; }
    public String getStatus() { return status; }
    public List<Surah_Data> getData() { return data; }
}
