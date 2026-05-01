package com.example.furqanmyqurancompanion.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furqanmyqurancompanion.Model.Surah_Metadata;
import com.example.furqanmyqurancompanion.R;

import java.util.List;

public class AudioSurahAdapter extends RecyclerView.Adapter<AudioSurahAdapter.ViewHolder> {

    private List<Surah_Metadata> surahs;
    private OnSurahClickListener listener;

    public interface OnSurahClickListener {
        void onSurahClick(Surah_Metadata surah);
    }

    public AudioSurahAdapter(List<Surah_Metadata> surahs, OnSurahClickListener listener) {
        this.surahs = surahs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_surah, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Surah_Metadata surah = surahs.get(position);
        holder.tvSurahNumber.setText(String.valueOf(surah.getSurah_number()));
        holder.tvSurahArabic.setText(surah.getSurah_arabic_name());
        holder.tvSurahEnglish.setText(surah.getSurah_english_name());
        holder.tvSurahInfo.setText(surah.getSurah_english_meaning() + " • " + surah.getSurah_ayahs() + " Ayahs");

        holder.itemView.setOnClickListener(v -> listener.onSurahClick(surah));
    }

    @Override
    public int getItemCount() {
        return surahs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSurahNumber, tvSurahArabic, tvSurahEnglish, tvSurahInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSurahNumber = itemView.findViewById(R.id.surah_number);
            tvSurahArabic = itemView.findViewById(R.id.surah_arabic_name);
            tvSurahEnglish = itemView.findViewById(R.id.surah_english_name);
            tvSurahInfo = itemView.findViewById(R.id.surah_english_meaning_verses);
        }
    }
}
