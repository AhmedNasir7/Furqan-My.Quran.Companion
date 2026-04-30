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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class quranFragment extends Fragment {

    RecyclerView rv_surah;
    MyApplication application;
    Surah_RecyclerAdapter surahAdapter;
    Juz_RecyclerAdapter juzAdapter;



    AppCompatButton btn_by_surah, btn_by_juz;

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
    }



    private void setupTabs() {
        btn_by_surah.setOnClickListener(v -> {
            rv_surah.setAdapter(surahAdapter);
            updateTabUI(btn_by_surah, btn_by_juz);
        });

        btn_by_juz.setOnClickListener(v -> {

            rv_surah.setAdapter(juzAdapter);
            updateTabUI(btn_by_juz, btn_by_surah);
        });
    }

    private void updateTabUI(AppCompatButton active, AppCompatButton inactive) {
        active.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.secondary));
        active.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

        inactive.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.white));
        inactive.setTextColor(ContextCompat.getColor(getActivity(), R.color.neutral));
    }


    public void init(View view) {
        rv_surah = view.findViewById(R.id.rv_surah);
        btn_by_surah = view.findViewById(R.id.btn_by_surah);
        btn_by_juz = view.findViewById(R.id.btn_by_juz);

        rv_surah.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        application=(MyApplication) getActivity().getApplicationContext();
        surahAdapter = new Surah_RecyclerAdapter(getActivity(), application.getSurahs_metadata());
        rv_surah.setAdapter(surahAdapter);

        juzAdapter = new Juz_RecyclerAdapter(getActivity(), application.getJuz_list());
    }
}
