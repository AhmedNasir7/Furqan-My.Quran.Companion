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

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.furqanmyqurancompanion.Adapters.DuaSearchResultAdapter;
import com.example.furqanmyqurancompanion.Model.DeepSearchService;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DuaListFragment extends Fragment {

    private RecyclerView rv;
    private List<DuaApiService.Category> categories = new ArrayList<>();
    private EditText et_dua_search;
    private ProgressBar duaSearchProgress;
    private DeepSearchService deepSearchService;
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dua_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rvDuas);
        et_dua_search = view.findViewById(R.id.et_dua_search);
        duaSearchProgress = view.findViewById(R.id.duaSearchProgress);
        
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        
        deepSearchService = new DeepSearchService();
        fetchDuaCategories();
        setupSearch();
    }

    private void setupSearch() {
        et_dua_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (searchRunnable != null) searchHandler.removeCallbacks(searchRunnable);
                searchRunnable = () -> performDuaSearch(s.toString());
                searchHandler.postDelayed(searchRunnable, 600);
            }
        });
    }

    private void performDuaSearch(String query) {
        if (query.isEmpty()) {
            rv.setAdapter(new CategoryAdapter(categories, this::onCategoryClick));
            return;
        }

        List<DuaApiService.Category> filtered = categories.stream()
                .filter(c -> c.title.toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        if (filtered.isEmpty() && query.length() > 4) {
            performDeepDuaSearch(query);
        } else {
            rv.setAdapter(new DuaSearchResultAdapter(getContext(), filtered));
        }
    }

    private void performDeepDuaSearch(String query) {
        duaSearchProgress.setVisibility(View.VISIBLE);
        ListenableFuture<GenerateContentResponse> future = deepSearchService.search(query, "Dua");

        Futures.addCallback(future, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    duaSearchProgress.setVisibility(View.GONE);
                    try {
                        String json = result.getText();
                        if (json != null) {
                            if (json.contains("[") && json.contains("]")) {
                                json = json.substring(json.indexOf("["), json.lastIndexOf("]") + 1);
                            }
                            JSONArray array = new JSONArray(json);
                            List<Integer> ids = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                ids.add(array.getJSONObject(i).getInt("id"));
                            }
                            
                            List<DuaApiService.Category> deepResults = categories.stream()
                                    .filter(c -> ids.contains(c.id))
                                    .collect(Collectors.toList());
                            
                            rv.setAdapter(new DuaSearchResultAdapter(getContext(), deepResults));
                        }
                    } catch (Exception e) {
                        Log.e("DuaListFragment", "Deep dua search error", e);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> duaSearchProgress.setVisibility(View.GONE));
            }
        }, Executors.newSingleThreadExecutor());
    }

    private void onCategoryClick(DuaApiService.Category category) {
        Intent intent = new Intent(getContext(), DuaDetailActivity.class);
        intent.putExtra("categoryId", category.id);
        intent.putExtra("categoryTitle", category.title);
        startActivity(intent);
    }

    private void fetchDuaCategories() {
        Api_Client.getDuaApiService().getCategories().enqueue(new Callback<DuaApiService.HisnResponse>() {
            @Override
            public void onResponse(Call<DuaApiService.HisnResponse> call, Response<DuaApiService.HisnResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories = response.body().categories;
                    rv.setAdapter(new CategoryAdapter(categories, DuaListFragment.this::onCategoryClick));
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dua_category, parent, false);
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
                tv = v.findViewById(R.id.tvDuaCategoryName);
            }
        }
    }
}
