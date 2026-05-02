package com.example.furqanmyqurancompanion.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furqanmyqurancompanion.Api.Api_Client;
import com.example.furqanmyqurancompanion.Api.DuaApiService;
import com.example.furqanmyqurancompanion.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DuaDetailActivity extends AppCompatActivity {

    RecyclerView rv;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dua_detail);

        int categoryId = getIntent().getIntExtra("categoryId", -1);
        String categoryTitle = getIntent().getStringExtra("categoryTitle");

        tvTitle = findViewById(R.id.tvDuaCategoryTitle);
        tvTitle.setText(categoryTitle);

        rv = findViewById(R.id.rvDuaDetails);
        rv.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBackDua).setOnClickListener(v -> finish());

        if (categoryId != -1) {
            fetchDuaDetails(categoryId);
        }
    }

    private void fetchDuaDetails(int categoryId) {
        Api_Client.getDuaApiService().getDuaDetails(categoryId).enqueue(new Callback<Map<String, List<DuaApiService.DuaDetail>>>() {
            @Override
            public void onResponse(Call<Map<String, List<DuaApiService.DuaDetail>>> call, Response<Map<String, List<DuaApiService.DuaDetail>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // The API returns a map where the key is the category name in capitals
                    List<DuaApiService.DuaDetail> details = new ArrayList<>();
                    for (List<DuaApiService.DuaDetail> list : response.body().values()) {
                        details.addAll(list);
                    }
                    rv.setAdapter(new DuaDetailAdapter(details));
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<DuaApiService.DuaDetail>>> call, Throwable t) {
                Toast.makeText(DuaDetailActivity.this, "Failed to load details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static class DuaDetailAdapter extends RecyclerView.Adapter<DuaDetailAdapter.VH> {
        private final List<DuaApiService.DuaDetail> items;

        DuaDetailAdapter(List<DuaApiService.DuaDetail> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dua, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            DuaApiService.DuaDetail d = items.get(position);
            holder.tvTitle.setText("Dua #" + (position + 1));
            holder.tvArabic.setText(d.arabicText);
            holder.tvTranslation.setText(d.translation);
            
            if (d.transliteration != null && !d.transliteration.isEmpty()) {
                holder.tvTransliteration.setVisibility(View.VISIBLE);
                holder.tvTransliteration.setText(d.transliteration);
            } else {
                holder.tvTransliteration.setVisibility(View.GONE);
            }
            
            holder.tvPurpose.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() { return items.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvTitle, tvArabic, tvTranslation, tvPurpose, tvTransliteration;
            VH(@NonNull View v) {
                super(v);
                tvTitle = v.findViewById(R.id.duaTitle);
                tvArabic = v.findViewById(R.id.duaArabic);
                tvTranslation = v.findViewById(R.id.duaTranslation);
                tvPurpose = v.findViewById(R.id.duaPurpose);
                tvTransliteration = v.findViewById(R.id.duaTransliteration);
            }
        }
    }
}
