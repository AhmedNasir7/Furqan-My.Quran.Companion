package com.example.furqanmyqurancompanion.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furqanmyqurancompanion.Activities.DuaDetailActivity;
import com.example.furqanmyqurancompanion.Api.Api_Client;
import com.example.furqanmyqurancompanion.Api.DuaApiService;
import com.example.furqanmyqurancompanion.Model.Dua;
import com.example.furqanmyqurancompanion.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DuaListFragment extends Fragment {

    private RecyclerView rv;
    private List<DuaApiService.Category> categories = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dua_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rvDuas);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        
        fetchDuaCategories();
    }

    private void fetchDuaCategories() {
        Api_Client.getDuaApiService().getCategories().enqueue(new Callback<DuaApiService.HisnResponse>() {
            @Override
            public void onResponse(Call<DuaApiService.HisnResponse> call, Response<DuaApiService.HisnResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories = response.body().categories;
                    rv.setAdapter(new CategoryAdapter(categories, category -> {
                        Intent intent = new Intent(getContext(), DuaDetailActivity.class);
                        intent.putExtra("categoryId", category.id);
                        intent.putExtra("categoryTitle", category.title);
                        startActivity(intent);
                    }));
                }
            }

            @Override
            public void onFailure(Call<DuaApiService.HisnResponse> call, Throwable t) {
                if (getContext() != null)
                    Toast.makeText(getContext(), "Failed to load duas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {
        private final List<DuaApiService.Category> items;
        private final OnItemClickListener listener;

        interface OnItemClickListener { void onItemClick(DuaApiService.Category category); }

        CategoryAdapter(List<DuaApiService.Category> items, OnItemClickListener listener) {
            this.items = items;
            this.listener = listener;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            DuaApiService.Category c = items.get(position);
            holder.tv.setText(c.title);
            holder.itemView.setOnClickListener(v -> listener.onItemClick(c));
        }

        @Override
        public int getItemCount() { return items.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tv;
            VH(@NonNull View v) {
                super(v);
                tv = v.findViewById(android.R.id.text1);
            }
        }
    }
}
