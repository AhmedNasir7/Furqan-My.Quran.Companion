package com.example.furqanmyqurancompanion.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.furqanmyqurancompanion.Activities.MainActivity;
import com.example.furqanmyqurancompanion.Activities.TasbeehCounterPage;
import com.example.furqanmyqurancompanion.Database.DatabaseHelper;
import com.example.furqanmyqurancompanion.Model.MyApplication;
import com.example.furqanmyqurancompanion.R;

public class homeFragment extends Fragment {

    TextView greet_text_home_page,Juz_continue_Card , bookmark_card,Streak_Card;
    LinearLayout Continue_Reading_Button, Read_Quran_Quick_Access, Listen_Quran_Quick_Access , Namaz_Quick_Access ,Tasbeeh_counter_Quick_Access, Bookmark_Card_Layout;
    DatabaseHelper dbHelper;

    public homeFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateBookmarkCount();
    }

    private void updateBookmarkCount() {
        if (dbHelper != null && bookmark_card != null && getActivity() != null) {
            String userId = ((MyApplication) getActivity().getApplicationContext()).getCurrentUserId();
            int count = dbHelper.getBookmarkCount(userId);
            bookmark_card.setText(count + " saved");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        updateBookmarkCount();

        Listen_Quran_Quick_Access.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).pager.setCurrentItem(2);
            }
        });
        Read_Quran_Quick_Access.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).pager.setCurrentItem(1);
            }
        });
        Namaz_Quick_Access.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).pager.setCurrentItem(3);
            }
        });
        Tasbeeh_counter_Quick_Access.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), TasbeehCounterPage.class));
        });

        Bookmark_Card_Layout.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), com.example.furqanmyqurancompanion.Activities.BookmarksActivity.class));
        });

        Continue_Reading_Button.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).pager.setCurrentItem(1);
            }
        });
    }

    public void init()
    {
        assert getView() != null;
        dbHelper = ((MyApplication) getActivity().getApplicationContext()).getDbHelper();
        greet_text_home_page=getView().findViewById(R.id.greet_text_home_page);
        Juz_continue_Card=getView().findViewById(R.id.Juz_Continue_Card);
        Streak_Card=getView().findViewById(R.id.Streak_card);
        bookmark_card=getView().findViewById(R.id.Bookmarks_card);

        Tasbeeh_counter_Quick_Access=getView().findViewById(R.id.Tasbeeh_counter_Quick_Access);
        Listen_Quran_Quick_Access=getView().findViewById(R.id.Listen_Quran_Quick_Access);
        Read_Quran_Quick_Access=getView().findViewById(R.id.Read_Quran_Quick_Access);
        Namaz_Quick_Access=getView().findViewById(R.id.Namaz_Quick_Access);
        Continue_Reading_Button=getView().findViewById(R.id.Continue_Reading_Button);
        Bookmark_Card_Layout=getView().findViewById(R.id.Bookmark_Card_Layout);
    }
}