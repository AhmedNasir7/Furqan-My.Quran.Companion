package com.example.furqanmyqurancompanion.Api;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DuaApiService {
    
    class Category {
        @SerializedName("ID") public int id;
        @SerializedName("TITLE") public String title;
    }

    class HisnResponse {
        @SerializedName("English") public List<Category> categories;
    }

    class DuaDetail {
        @SerializedName("ID") public int id;
        @SerializedName("ARABIC_TEXT") public String arabicText;
        @SerializedName("TRANSLATED_TEXT") public String translation;
        @SerializedName("LANGUAGE_ARABIC_TRANSLATED_TEXT") public String transliteration;
    }

    @GET("husn_en.json")
    Call<HisnResponse> getCategories();

    @GET("{id}.json")
    Call<Map<String, List<DuaDetail>>> getDuaDetails(@Path("id") int id);
}
