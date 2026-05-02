package com.example.furqanmyqurancompanion.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furqanmyqurancompanion.Activities.DuaDetailActivity;
import com.example.furqanmyqurancompanion.Api.DuaApiService;
import com.example.furqanmyqurancompanion.R;

import java.util.List;

public class DuaSearchResultAdapter extends RecyclerView.Adapter<DuaSearchResultAdapter.VH> {

    private final Context context;
    private final List<DuaApiService.Category> items;

    public DuaSearchResultAdapter(Context context, List<DuaApiService.Category> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_dua_category, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        DuaApiService.Category c = items.get(position);
        holder.tv.setText(c.title);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DuaDetailActivity.class);
            intent.putExtra("categoryId", c.id);
            intent.putExtra("categoryTitle", c.title);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tv;
        VH(@NonNull View v) {
            super(v);
            tv = v.findViewById(R.id.tvDuaCategoryName);
        }
    }
}
