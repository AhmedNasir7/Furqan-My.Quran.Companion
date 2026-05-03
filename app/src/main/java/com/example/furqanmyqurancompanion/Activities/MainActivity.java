package com.example.furqanmyqurancompanion.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.furqanmyqurancompanion.R;
import com.example.furqanmyqurancompanion.Adapters.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    public ViewPager2 pager;
    BottomNavigationView BottomNavView;
    ViewPagerAdapter adapter;
    TextView tvHeaderUserName;
    MaterialButton btnLogout;

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
        loadUserData();

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
        tvHeaderUserName = findViewById(R.id.tvHeaderUserName);
        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences spref = getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor editor = spref.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.putBoolean("isGuest", false);
            editor.apply();

            startActivity(new Intent(MainActivity.this, LoginPage.class));
            finish();
        });
    }

    private void loadUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            if (name == null || name.isEmpty()) {
                name = user.getEmail();
                if (name != null && name.contains("@")) {
                    name = name.split("@")[0];
                }
            }
            tvHeaderUserName.setText(name);
        } else {
            tvHeaderUserName.setText("Guest User");
        }
    }
}
