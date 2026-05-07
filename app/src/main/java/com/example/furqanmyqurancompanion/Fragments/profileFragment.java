package com.example.furqanmyqurancompanion.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.furqanmyqurancompanion.Activities.TafseerActivity;
import com.example.furqanmyqurancompanion.Database.DatabaseHelper;
import com.example.furqanmyqurancompanion.Model.Ayah_Data;
import com.example.furqanmyqurancompanion.Model.MyApplication;
import com.example.furqanmyqurancompanion.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class profileFragment extends Fragment {

    private RecyclerView rvBookmarks;
    private BookmarkAdapter adapter;
    private List<Ayah_Data> bookmarkList = new ArrayList<>();
    private TextView tvUserName, tvVersesReadCount;
    private MyApplication application;
    private DatabaseHelper dbHelper;

    public profileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        init(view);
        loadUserData();
        loadBookmarks();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBookmarks();
    }

    private void init(View view) {
        rvBookmarks = view.findViewById(R.id.rvBookmarks);
        
        tvUserName = view.findViewById(R.id.tvUserName);
        tvVersesReadCount = view.findViewById(R.id.tvVersesReadCount);

        application = (MyApplication) getActivity().getApplicationContext();
        dbHelper = application.getDbHelper();

        rvBookmarks.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookmarkAdapter(getContext(), bookmarkList, ayah -> {
            dbHelper.toggleBookmark(ayah.getGlobalVerseNumber(), false, application.getCurrentUserId());
            loadBookmarks();
        });
        rvBookmarks.setAdapter(adapter);
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
            tvUserName.setText(name);
        } else {
            tvUserName.setText("Guest User");
        }
        
        // Mock verses read for now
        tvVersesReadCount.setText("0");
    }

    private void loadBookmarks() {
        bookmarkList.clear();
        bookmarkList.addAll(dbHelper.getBookmarkedAyahs(application.getCurrentUserId()));
        adapter.notifyDataSetChanged();
    }

    private static class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.VH> {
        private final Context context;
        private final List<Ayah_Data> items;
        private final OnRemoveListener removeListener;

        interface OnRemoveListener { void onRemove(Ayah_Data ayah); }

        BookmarkAdapter(Context context, List<Ayah_Data> items, OnRemoveListener removeListener) {
            this.context = context;
            this.items = items;
            this.removeListener = removeListener;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_profile_bookmark, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            Ayah_Data ayah = items.get(position);
            
            if (ayah.getSurah() != null) {
                holder.tvSurahNumber.setText(String.valueOf(ayah.getSurah().getSurah_number()));
                holder.tvSurahName.setText(ayah.getSurah().getSurah_english_name());
            }
            
            holder.tvAyahText.setText(ayah.getTranslation());
            holder.tvAyahLabel.setText("Ayah " + ayah.getVerseNumber());

            holder.btnRemove.setOnClickListener(v -> removeListener.onRemove(ayah));
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, TafseerActivity.class);
                intent.putExtra("ayah_global_id", ayah.getGlobalVerseNumber());
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() { return items.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvSurahNumber, tvSurahName, tvAyahText, tvAyahLabel;
            ImageView btnRemove, btnPlay;

            VH(@NonNull View v) {
                super(v);
                tvSurahNumber = v.findViewById(R.id.tvSurahNumber);
                tvSurahName = v.findViewById(R.id.tvSurahName);
                tvAyahText = v.findViewById(R.id.tvAyahText);
                tvAyahLabel = v.findViewById(R.id.tvAyahLabel);
                btnRemove = v.findViewById(R.id.btnRemove);
                btnPlay = v.findViewById(R.id.btnPlay);
            }
        }
    }
}
