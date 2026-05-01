package com.example.furqanmyqurancompanion.Activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furqanmyqurancompanion.Adapters.Ayah_RecyclerAdapter;
import com.example.furqanmyqurancompanion.Database.DatabaseHelper;
import com.example.furqanmyqurancompanion.Model.Ayah_Data;
import com.example.furqanmyqurancompanion.Model.MyApplication;
import com.example.furqanmyqurancompanion.R;

import java.util.ArrayList;
import java.util.List;

public class BookmarksActivity extends AppCompatActivity {

    TextView tvReadPageTitle;
    RecyclerView rvAyahs;
    Ayah_RecyclerAdapter adapter;
    List<Ayah_Data> ayahList = new ArrayList<>();
    ImageView btnMenu;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_read_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.readHeader), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        loadBookmarks();
    }

    private void init() {
        tvReadPageTitle = findViewById(R.id.tvReadPageTitle);
        rvAyahs = findViewById(R.id.rvAyahs);
        btnMenu = findViewById(R.id.btnMenu);

        tvReadPageTitle.setText(getString(R.string.bookmarks));
        dbHelper = ((MyApplication) getApplicationContext()).getDbHelper();

        rvAyahs.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Ayah_RecyclerAdapter(this, ayahList);
        rvAyahs.setAdapter(adapter);

        btnMenu.setOnClickListener(v -> finish());
    }

    private void loadBookmarks() {
        MyApplication application = (MyApplication) getApplicationContext();
        String userId = application.getCurrentUserId();
        List<Ayah_Data> bookmarks = dbHelper.getBookmarkedAyahs(userId);
        ayahList.clear();
        ayahList.addAll(bookmarks);
        adapter.notifyDataSetChanged();
    }
}
