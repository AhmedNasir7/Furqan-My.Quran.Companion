package com.example.furqanmyqurancompanion.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furqanmyqurancompanion.Activities.ReadPage;
import com.example.furqanmyqurancompanion.Activities.TafseerActivity;
import com.example.furqanmyqurancompanion.Model.Ayah_Data;
import com.example.furqanmyqurancompanion.Model.MyApplication;
import com.example.furqanmyqurancompanion.R;

import java.util.List;

public class AyahSearchResultAdapter extends RecyclerView.Adapter<AyahSearchResultAdapter.VH> {

    private final Context context;
    private List<Ayah_Data> ayahs;

    public AyahSearchResultAdapter(Context context, List<Ayah_Data> ayahs) {
        this.context = context;
        this.ayahs = ayahs;
    }

    public void updateList(List<Ayah_Data> newList) {
        this.ayahs = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_profile_bookmark, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Ayah_Data ayah = ayahs.get(position);
        
        if (ayah.getSurah() != null) {
            holder.tvSurahNumber.setText(String.valueOf(ayah.getSurah().getSurah_number()));
            holder.tvSurahName.setText(ayah.getSurah().getSurah_english_name());
        }
        
        holder.tvAyahText.setText(ayah.getTranslation());
        holder.tvAyahLabel.setText(context.getString(R.string.ayah_label, ayah.getVerseNumber()));
        
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TafseerActivity.class);
            intent.putExtra("ayah_global_id", ayah.getGlobalVerseNumber());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return ayahs.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvSurahNumber, tvSurahName, tvAyahText, tvAyahLabel;

        VH(@NonNull View v) {
            super(v);
            tvSurahNumber = v.findViewById(R.id.tvSurahNumber);
            tvSurahName = v.findViewById(R.id.tvSurahName);
            tvAyahText = v.findViewById(R.id.tvAyahText);
            tvAyahLabel = v.findViewById(R.id.tvAyahLabel);
            
            // Hide elements not needed for search results
            v.findViewById(R.id.btnRemove).setVisibility(View.GONE);
            v.findViewById(R.id.btnPlay).setVisibility(View.GONE);
        }
    }
}
