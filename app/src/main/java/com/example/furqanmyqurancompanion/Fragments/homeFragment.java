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
import com.example.furqanmyqurancompanion.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment {

    TextView greet_text_home_page,Juz_continue_Card , bookmark_card,Streak_Card;
    LinearLayout Continue_Reading_Button, Read_Quran_Quick_Access, Listen_Quran_Quick_Access , Namaz_Quick_Access ,Tasbeeh_counter_Quick_Access;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

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

        Continue_Reading_Button.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).pager.setCurrentItem(1);
            }
        });
    }

    public void init()
    {
        assert getView() != null;
        greet_text_home_page=getView().findViewById(R.id.greet_text_home_page);
        Juz_continue_Card=getView().findViewById(R.id.Juz_Continue_Card);
        Streak_Card=getView().findViewById(R.id.Streak_card);
        bookmark_card=getView().findViewById(R.id.Bookmarks_card);

        Tasbeeh_counter_Quick_Access=getView().findViewById(R.id.Tasbeeh_counter_Quick_Access);
        Listen_Quran_Quick_Access=getView().findViewById(R.id.Listen_Quran_Quick_Access);
        Read_Quran_Quick_Access=getView().findViewById(R.id.Read_Quran_Quick_Access);
        Namaz_Quick_Access=getView().findViewById(R.id.Namaz_Quick_Access);
        Continue_Reading_Button=getView().findViewById(R.id.Continue_Reading_Button);
    }
}