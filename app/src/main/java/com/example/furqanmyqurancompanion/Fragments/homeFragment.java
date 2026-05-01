package com.example.furqanmyqurancompanion.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.furqanmyqurancompanion.Activities.BookmarksActivity;
import com.example.furqanmyqurancompanion.Activities.MainActivity;
import com.example.furqanmyqurancompanion.Activities.TasbeehCounterPage;
import com.example.furqanmyqurancompanion.Database.DatabaseHelper;
import com.example.furqanmyqurancompanion.Model.MyApplication;
import com.example.furqanmyqurancompanion.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class homeFragment extends Fragment {

    TextView greet_text_home_page,Juz_continue_Card , bookmark_card,Streak_Card, tvContinueReadingSurah;
    LinearLayout Continue_Reading_Button, Read_Quran_Quick_Access, Listen_Quran_Quick_Access , Namaz_Quick_Access ,Tasbeeh_counter_Quick_Access, Bookmark_Card_Layout;
    DatabaseHelper dbHelper;
    String lastType;
    int lastId;
    int lastAyahGlobalId;

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
        checkUserStatus();
        updateBookmarkCount();
        updateContinueReading();
        updateStreak();
    }

    private void checkUserStatus() {
        if (getActivity() == null)
            return;
        MyApplication application = (MyApplication) getActivity().getApplicationContext();
        if (application.isGuest()) {
            Bookmark_Card_Layout.setEnabled(false);
            Bookmark_Card_Layout.setAlpha(0.5f);
            Continue_Reading_Button.setEnabled(false);
            Continue_Reading_Button.setAlpha(0.5f);
            if (Streak_Card != null && Streak_Card.getParent() instanceof View) {
                ((View) Streak_Card.getParent()).setEnabled(false);
                ((View) Streak_Card.getParent()).setAlpha(0.5f);
            }
            bookmark_card.setText(R.string.login_to_use);
            Streak_Card.setText(R.string.login_to_use);
            if (tvContinueReadingSurah != null) {
                tvContinueReadingSurah.setText(R.string.login_to_use);
            }
        } else {
            Bookmark_Card_Layout.setEnabled(true);
            Bookmark_Card_Layout.setAlpha(1.0f);
            Continue_Reading_Button.setEnabled(true);
            Continue_Reading_Button.setAlpha(1.0f);
            if (Streak_Card != null && Streak_Card.getParent() instanceof View) {
                ((View) Streak_Card.getParent()).setEnabled(true);
                ((View) Streak_Card.getParent()).setAlpha(1.0f);
            }
        }
    }

    private void updateStreak() {
        if (dbHelper == null || Streak_Card == null || getActivity() == null) return;

        MyApplication application = (MyApplication) getActivity().getApplicationContext();
        if (application.isGuest()) return;
        
        String userId = application.getCurrentUserId();

        // Calculate dates
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_format), Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        String today = sdf.format(cal.getTime());

        cal.add(Calendar.DATE, -1);
        String yesterday = sdf.format(cal.getTime());

        // Check if last activity was yesterday
        Cursor cursor = dbHelper.getActivity(userId);
        boolean isYesterday = false;
        if (cursor.moveToFirst()) {
            int lastDateIndex = cursor.getColumnIndex("last_date");
            if (lastDateIndex != -1) {
                String lastDate = cursor.getString(lastDateIndex);
                if (yesterday.equals(lastDate)) {
                    isYesterday = true;
                }
            }
        }
        cursor.close();

        dbHelper.updateActivity(userId, today, isYesterday);

        // Update UI
        Cursor activityCursor = dbHelper.getActivity(userId);
        if (activityCursor.moveToFirst()) {
            int streakIndex = activityCursor.getColumnIndex("current_streak");
            if (streakIndex != -1) {
                int currentStreak = activityCursor.getInt(streakIndex);
                Streak_Card.setText(currentStreak + " Days");
            }
        }
        activityCursor.close();
    }

    private void updateContinueReading() {
        if (dbHelper == null || tvContinueReadingSurah == null || getActivity() == null) return;

        MyApplication application = (MyApplication) getActivity().getApplicationContext();
        if (application.isGuest()) return;

        Cursor cursor = dbHelper.getReadingProgress(application.getCurrentUserId());

        if (cursor.moveToFirst()) {
            int typeIndex = cursor.getColumnIndex("type");
            int idIndex = cursor.getColumnIndex("id");
            int ayahIdIndex = cursor.getColumnIndex("ayah_id");

            if (typeIndex != -1) lastType = cursor.getString(typeIndex); // type
            if (idIndex != -1) lastId = cursor.getInt(idIndex);     // id
            if (ayahIdIndex != -1) lastAyahGlobalId = cursor.getInt(ayahIdIndex); // ayah_id

            if ("surah".equals(lastType)) {
                // Find surah name from metadata
                for (com.example.furqanmyqurancompanion.Model.Surah_Metadata surah : application.getSurahs_metadata()) {
                    if (surah.getSurah_number() == lastId) {
                        tvContinueReadingSurah.setText(surah.getSurah_english_name());
                        break;
                    }
                }
            } else {
                tvContinueReadingSurah.setText("Juz " + lastId);
            }
        }
        cursor.close();
    }

    private void updateBookmarkCount() {
        if (dbHelper == null || bookmark_card == null || getActivity() == null) return;

        MyApplication application = (MyApplication) getActivity().getApplicationContext();
        if (application.isGuest()) return;
        
        String userId = application.getCurrentUserId();
        int count = dbHelper.getBookmarkCount(userId);
        bookmark_card.setText(count + " saved");
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
            startActivity(new Intent(getContext(), BookmarksActivity.class));
        });

        Continue_Reading_Button.setOnClickListener(v -> {
            if (lastType != null) {
                MyApplication application = (MyApplication) getActivity().getApplicationContext();
                if ("surah".equals(lastType)) {
                    application.setSurah_Clicked(String.valueOf(lastId));
                } else {
                    application.setJuz_Clicked(String.valueOf(lastId));
                }
                startActivity(new Intent(getContext(), com.example.furqanmyqurancompanion.Activities.ReadPage.class));
            } else {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).pager.setCurrentItem(1);
                }
            }
        });
    }

    public void init()
    {
        assert getView() != null;
        dbHelper = ((MyApplication) requireActivity().getApplicationContext()).getDbHelper();
        greet_text_home_page=getView().findViewById(R.id.greet_text_home_page);
        Juz_continue_Card=getView().findViewById(R.id.Juz_Continue_Card);
        Streak_Card=getView().findViewById(R.id.Streak_card);
        bookmark_card=getView().findViewById(R.id.Bookmarks_card);
        tvContinueReadingSurah = getView().findViewById(R.id.tvContinueReadingSurah);


        Tasbeeh_counter_Quick_Access=getView().findViewById(R.id.Tasbeeh_counter_Quick_Access);
        Listen_Quran_Quick_Access=getView().findViewById(R.id.Listen_Quran_Quick_Access);
        Read_Quran_Quick_Access=getView().findViewById(R.id.Read_Quran_Quick_Access);
        Namaz_Quick_Access=getView().findViewById(R.id.Namaz_Quick_Access);
        Continue_Reading_Button=getView().findViewById(R.id.Continue_Reading_Button);
        Bookmark_Card_Layout=getView().findViewById(R.id.Bookmark_Card_Layout);
    }
}