package com.example.furqanmyqurancompanion.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furqanmyqurancompanion.Model.Ayah_Data;
import com.example.furqanmyqurancompanion.R;

import java.util.List;

public class Ayah_RecyclerAdapter extends RecyclerView.Adapter<Ayah_RecyclerAdapter.AyahViewHolder> {

    private Context context;
    private List<Ayah_Data> ayahs;

    public Ayah_RecyclerAdapter(Context context, List<Ayah_Data> ayahs) {
        this.context = context;
        this.ayahs = ayahs;
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

        holder.tvAyahNumber.setText(String.valueOf(ayah.getVerseNumber()-1));
        holder.tvAyahArabic.setText(ayah.getArabicText());
        holder.tvAyahEnglish.setText(ayah.getTranslation());

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

        public AyahViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAyahNumber = itemView.findViewById(R.id.tvAyahNumber);
            tvAyahArabic = itemView.findViewById(R.id.tvAyahArabic);
            tvAyahEnglish = itemView.findViewById(R.id.tvAyahEnglish);
            tvBismillah = itemView.findViewById(R.id.tvBismillah);
        }
    }
}
