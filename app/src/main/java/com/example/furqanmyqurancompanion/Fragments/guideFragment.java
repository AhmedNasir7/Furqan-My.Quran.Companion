package com.example.furqanmyqurancompanion.Fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.furqanmyqurancompanion.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class guideFragment extends Fragment {

    public guideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.guideTabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.guideViewPager);

        viewPager.setAdapter(new GuidePagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Prayer Guide");
                    break;
                case 1:
                    tab.setText("Duas");
                    break;
            }
        }).attach();
    }

    private static class GuidePagerAdapter extends FragmentStateAdapter {
        public GuidePagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new PrayerGuideListFragment();
            } else {
                return new DuaListFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}