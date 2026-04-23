package com.example.furqanmyqurancompanion;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ViewPager2 pager;
    BottomNavigationView BottomNavView;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        adapter = new ViewPagerAdapter(this);
        pager.setAdapter(adapter);

        BottomNavView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.homepage) {
                pager.setCurrentItem(0);
            }
            else if (itemId == R.id.quran) {
                pager.setCurrentItem(1);
            }
            else if (itemId == R.id.audio) {
                pager.setCurrentItem(2);
            }
            else if (itemId == R.id.guide) {
                pager.setCurrentItem(3);
            }
            else if (itemId == R.id.profile) {
                pager.setCurrentItem(4);
            }
            return true;
        });

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        BottomNavView.setSelectedItemId(R.id.homepage);
                        break;
                    case 1:
                        BottomNavView.setSelectedItemId(R.id.quran);
                        break;
                    case 2:
                        BottomNavView.setSelectedItemId(R.id.audio);
                        break;
                    case 3:
                        BottomNavView.setSelectedItemId(R.id.guide);
                        break;
                    case 4:
                        BottomNavView.setSelectedItemId(R.id.profile);
                        break;

                }
            }
        });
    }

    private void init() {
        pager = findViewById(R.id.viewPagerNav);
        BottomNavView = findViewById(R.id.BottomNavView);
    }
}