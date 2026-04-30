package com.example.furqanmyqurancompanion.Model;

import java.util.List;

public class JuzResponse {
    private int code;
    private String status;
    private List<Juz_Data> data;

    public int getCode() { return code; }
    public String getStatus() { return status; }
    public List<Juz_Data> getData() { return data; }
}
