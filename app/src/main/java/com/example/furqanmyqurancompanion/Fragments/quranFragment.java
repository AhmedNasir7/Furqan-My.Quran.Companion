package com.example.furqanmyqurancompanion.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.furqanmyqurancompanion.Adapters.Juz_RecyclerAdapter;
import com.example.furqanmyqurancompanion.Adapters.Surah_RecyclerAdapter;
import com.example.furqanmyqurancompanion.Api.Api_Client;
import com.example.furqanmyqurancompanion.Api.QuranAPiService;
import com.example.furqanmyqurancompanion.Model.MyApplication;
import com.example.furqanmyqurancompanion.Model.SurahResponse;
import com.example.furqanmyqurancompanion.Model.Surah_Metadata;
import com.example.furqanmyqurancompanion.R;

import android.util.Log;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.furqanmyqurancompanion.Model.DeepSearchService;
import com.example.furqanmyqurancompanion.Model.Juz_Data;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class quranFragment extends Fragment {

    RecyclerView rv_surah;
    MyApplication application;
    Surah_RecyclerAdapter surahAdapter;
    Juz_RecyclerAdapter juzAdapter;
    EditText et_search;
    ProgressBar searchProgress;
    DeepSearchService deepSearchService;
    AppCompatButton btn_by_surah, btn_by_juz;
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private boolean isSurahTab = true;

    public quranFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quran, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        setupTabs();
        setupSearch();
    }

    private void setupSearch() {
        deepSearchService = new DeepSearchService();
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (searchRunnable != null) searchHandler.removeCallbacks(searchRunnable);
                searchRunnable = () -> performSearch(s.toString());
                searchHandler.postDelayed(searchRunnable, 600);
            }
        });
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            resetSearch();
            return;
        }

        if (isSurahTab) {
            List<Surah_Metadata> filtered = application.getSurahs_metadata().stream()
                    .filter(s -> s.getSurah_english_name().toLowerCase().contains(query.toLowerCase()) ||
                            String.valueOf(s.getSurah_number()).equals(query))
                    .collect(Collectors.toList());

            if (filtered.isEmpty() && query.length() > 3) {
                performDeepSearch(query, "Surah");
            } else {
                surahAdapter.updateList(filtered);
            }
        } else {
            List<Integer> filtered = application.getJuz_list().stream()
                    .filter(j -> String.valueOf(j).equals(query))
                    .collect(Collectors.toList());
            
            if (filtered.isEmpty() && query.length() > 3) {
                performDeepSearch(query, "Juz");
            } else {
                juzAdapter.updateList(filtered);
            }
        }
    }

    private void performDeepSearch(String query, String type) {
        searchProgress.setVisibility(View.VISIBLE);
        ListenableFuture<GenerateContentResponse> future = deepSearchService.search(query, type);

        Futures.addCallback(future, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    searchProgress.setVisibility(View.GONE);
                    try {
                        String json = result.getText();
                        Log.d("quranFragment", "Deep search response: " + json);
                        if (json != null) {
                            // Extract JSON array if AI added markdown backticks
                            if (json.contains("[") && json.contains("]")) {
                                json = json.substring(json.indexOf("["), json.lastIndexOf("]") + 1);
                            }
                            JSONArray array = new JSONArray(json);
                            List<Integer> ids = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                Object idObj = obj.get("id");
                                if (idObj instanceof Integer) {
                                    ids.add((Integer) idObj);
                                } else if (idObj instanceof String) {
                                    String idStr = (String) idObj;
                                    if (idStr.contains(":")) {
                                        idStr = idStr.split(":")[0];
                                    }
                                    try {
                                        // Remove all non-numeric characters before parsing
                                        ids.add(Integer.parseInt(idStr.replaceAll("[^0-9]", "")));
                                    } catch (NumberFormatException ignored) {}
                                }
                            }
                            updateListWithIds(ids);
                        }
                    } catch (Exception e) {
                        Log.e("quranFragment", "Deep search parsing error", e);
                        Toast.makeText(getContext(), "Search failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("quranFragment", "Deep search call failed", t);
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    searchProgress.setVisibility(View.GONE);
                    String errorMsg = "AI Search unavailable: " + t.getMessage();
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    Log.e("DeepSearchService", errorMsg, t);

                });
            }
        }, Executors.newSingleThreadExecutor());
    }

    private void updateListWithIds(List<Integer> ids) {
        if (isSurahTab) {
            List<Surah_Metadata> filtered = application.getSurahs_metadata().stream()
                    .filter(s -> ids.contains(s.getSurah_number()))
                    .collect(Collectors.toList());
            surahAdapter.updateList(filtered);
        } else {
            List<Integer> filtered = application.getJuz_list().stream()
                    .filter(ids::contains)
                    .collect(Collectors.toList());
            juzAdapter.updateList(filtered);
        }
    }

    private void resetSearch() {
        if (isSurahTab) {
            surahAdapter.updateList(application.getSurahs_metadata());
        } else {
            juzAdapter.updateList(application.getJuz_list());
        }
    }

    private void setupTabs() {
        btn_by_surah.setOnClickListener(v -> {
            isSurahTab = true;
            rv_surah.setAdapter(surahAdapter);
            updateTabUI(btn_by_surah, btn_by_juz);
            resetSearch();
            et_search.setText("");
        });

        btn_by_juz.setOnClickListener(v -> {
            isSurahTab = false;
            rv_surah.setAdapter(juzAdapter);
            updateTabUI(btn_by_juz, btn_by_surah);
            resetSearch();
            et_search.setText("");
        });
    }

    private void updateTabUI(AppCompatButton active, AppCompatButton inactive) {
        if (getContext() == null) return;
        active.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.secondary));
        active.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

        inactive.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));
        inactive.setTextColor(ContextCompat.getColor(getContext(), R.color.neutral));
    }


    public void init(View view) {
        rv_surah = view.findViewById(R.id.rv_surah);
        btn_by_surah = view.findViewById(R.id.btn_by_surah);
        btn_by_juz = view.findViewById(R.id.btn_by_juz);
        et_search = view.findViewById(R.id.et_search);
        searchProgress = view.findViewById(R.id.searchProgress);

        rv_surah.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        if (getActivity() != null) {
            application = (MyApplication) getActivity().getApplicationContext();
        }
        
        if (application != null) {
            surahAdapter = new Surah_RecyclerAdapter(getActivity(), application.getSurahs_metadata());
            rv_surah.setAdapter(surahAdapter);
            juzAdapter = new Juz_RecyclerAdapter(getActivity(), application.getJuz_list());
        }
    }
}
