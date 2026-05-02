package com.example.furqanmyqurancompanion.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furqanmyqurancompanion.Activities.PrayerDetailActivity;
import com.example.furqanmyqurancompanion.Model.Prayer;
import com.example.furqanmyqurancompanion.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PrayerGuideListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prayer_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.rvPrayers);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        
        List<Prayer> prayers = loadPrayersFromAssets();
        rv.setAdapter(new PrayerAdapter(prayers, prayer -> {
            Intent intent = new Intent(getContext(), PrayerDetailActivity.class);
            intent.putExtra("prayer", prayer);
            startActivity(intent);
        }));
    }

    private List<Prayer> loadPrayersFromAssets() {
        String json = null;
        try {
            InputStream is = requireContext().getAssets().open("prayers.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
        
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<Prayer>>(){}.getType());
    }

    private static class PrayerAdapter extends RecyclerView.Adapter<PrayerAdapter.VH> {
        private final List<Prayer> items;
        private final OnItemClickListener listener;

        interface OnItemClickListener { void onItemClick(Prayer prayer); }

        PrayerAdapter(List<Prayer> items, OnItemClickListener listener) {
            this.items = items;
            this.listener = listener;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prayer_category, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            Prayer p = items.get(position);
            holder.tv.setText(p.getName());
            holder.itemView.setOnClickListener(v -> listener.onItemClick(p));
        }

        @Override
        public int getItemCount() { return items.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tv;
            VH(@NonNull View v) {
                super(v);
                tv = v.findViewById(R.id.tvPrayerCategoryName);
            }
        }
    }
}
