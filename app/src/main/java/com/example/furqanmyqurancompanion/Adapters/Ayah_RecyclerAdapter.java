package com.example.furqanmyqurancompanion.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furqanmyqurancompanion.Database.DatabaseHelper;
import com.example.furqanmyqurancompanion.Model.Ayah_Data;
import com.example.furqanmyqurancompanion.Model.MyApplication;
import com.example.furqanmyqurancompanion.R;

import java.util.List;

public class Ayah_RecyclerAdapter extends RecyclerView.Adapter<Ayah_RecyclerAdapter.AyahViewHolder> {

    private Context context;
    private List<Ayah_Data> ayahs;
    private DatabaseHelper dbHelper;

    public Ayah_RecyclerAdapter(Context context, List<Ayah_Data> ayahs) {
        this.context = context;
        this.ayahs = ayahs;
        this.dbHelper = ((MyApplication) context.getApplicationContext()).getDbHelper();
    }

    @NonNull
    @Override
    public AyahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ayah, parent, false);
        return new AyahViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AyahViewHolder holder, int position) {
        Ayah_Data ayah = ayahs.get(position);

        holder.tvAyahNumber.setText(String.valueOf(ayah.getVerseNumber()));
        holder.tvAyahArabic.setText(ayah.getArabicText());
        holder.tvAyahEnglish.setText(ayah.getTranslation());

        // Set bookmark icon color based on status
        if (ayah.isBookmarked()) {
            holder.btnBookmark.setColorFilter(ContextCompat.getColor(context, R.color.secondary));
            holder.btnBookmark.setImageResource(R.drawable.bookmark); // Or a filled bookmark if you have one
        } else {
            holder.btnBookmark.setColorFilter(ContextCompat.getColor(context, R.color.neutral));
            holder.btnBookmark.setImageResource(R.drawable.bookmark);
        }

        holder.btnBookmark.setOnClickListener(v -> {
            boolean newStatus = !ayah.isBookmarked();
            ayah.setBookmarked(newStatus);
            String userId = ((MyApplication) context.getApplicationContext()).getCurrentUserId();
            dbHelper.toggleBookmark(ayah.getGlobalVerseNumber(), newStatus, userId);
            
            if (newStatus) {
                holder.btnBookmark.setColorFilter(ContextCompat.getColor(context, R.color.secondary));
                Toast.makeText(context, "Added to bookmarks", Toast.LENGTH_SHORT).show();
            } else {
                holder.btnBookmark.setColorFilter(ContextCompat.getColor(context, R.color.neutral));
                Toast.makeText(context, "Removed from bookmarks", Toast.LENGTH_SHORT).show();
            }
        });

        if (ayah.getVerseNumber() == 1) {
            if (ayah.getSurah() != null) {
                int surahNum = ayah.getSurah().getSurah_number();
                if (surahNum != 1 && surahNum != 9) {
                    holder.tvBismillah.setVisibility(View.VISIBLE);
                } else {
                    holder.tvBismillah.setVisibility(View.GONE);
                }
            } else if (ayah.getGlobalVerseNumber() != 1) {

                holder.tvBismillah.setVisibility(View.VISIBLE);
            } else {
                holder.tvBismillah.setVisibility(View.GONE);
            }
        } else {
            holder.tvBismillah.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return ayahs != null ? ayahs.size() : 0;
    }

    public static class AyahViewHolder extends RecyclerView.ViewHolder {
        TextView tvAyahNumber, tvAyahArabic, tvAyahEnglish, tvBismillah;
        ImageView btnBookmark;

        public AyahViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAyahNumber = itemView.findViewById(R.id.tvAyahNumber);
            tvAyahArabic = itemView.findViewById(R.id.tvAyahArabic);
            tvAyahEnglish = itemView.findViewById(R.id.tvAyahEnglish);
            tvBismillah = itemView.findViewById(R.id.tvBismillah);
            btnBookmark = itemView.findViewById(R.id.btnBookmark);
        }
    }
}
