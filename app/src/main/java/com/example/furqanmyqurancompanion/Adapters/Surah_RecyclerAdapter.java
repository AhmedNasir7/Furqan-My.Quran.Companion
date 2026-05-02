package com.example.furqanmyqurancompanion.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furqanmyqurancompanion.Activities.ReadPage;
import com.example.furqanmyqurancompanion.Model.MyApplication;
import com.example.furqanmyqurancompanion.Model.Surah_Metadata;
import com.example.furqanmyqurancompanion.R;
import android.content.Context;
import android.widget.TextView;

import java.util.List;

public class Surah_RecyclerAdapter  extends   RecyclerView.Adapter<Surah_RecyclerAdapter.Surah_View_holder>{

    Context context;
    List<Surah_Metadata> list;
    public Surah_RecyclerAdapter(Context context, List<Surah_Metadata> surah_metadata) {
        this.context=context;
        this.list=surah_metadata;
    }

    @NonNull
    @Override
    public Surah_View_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_surah,parent,false);
        return new Surah_View_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Surah_View_holder holder, int position) {

        Surah_Metadata current_surah = list.get(position);

        holder.surah_english_meaning_verses.setText("Verses: " +String.valueOf(current_surah.getSurah_ayahs()));
        holder.surah_number.setText(String.valueOf(current_surah.getSurah_number()));
        holder.surah_arabic_name.setText(current_surah.getSurah_arabic_name().toString().trim());
        holder.surah_english_name.setText(current_surah.getSurah_english_name().toString().trim());

        holder.itemView.setOnClickListener(v->{
            MyApplication application = (MyApplication) context.getApplicationContext();
            application.setSurah_Clicked(current_surah.getSurah_number() + "");
            context.startActivity(new Intent(context, ReadPage.class));
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<Surah_Metadata> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public class Surah_View_holder extends RecyclerView.ViewHolder{

        TextView surah_number, surah_english_name , surah_arabic_name , surah_english_meaning_verses;
        public Surah_View_holder(@NonNull View itemView) {
            super(itemView);
            surah_arabic_name=itemView.findViewById(R.id.surah_arabic_name);
            surah_number=itemView.findViewById(R.id.surah_number);
            surah_english_name=itemView.findViewById(R.id.surah_english_name);
            surah_english_meaning_verses=itemView.findViewById(R.id.surah_english_meaning_verses);
        }
    }
}
