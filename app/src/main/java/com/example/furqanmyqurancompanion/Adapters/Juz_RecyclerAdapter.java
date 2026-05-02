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
import com.example.furqanmyqurancompanion.Model.MyApplication;
import com.example.furqanmyqurancompanion.R;

import java.util.List;

public class Juz_RecyclerAdapter extends RecyclerView.Adapter<Juz_RecyclerAdapter.Juz_View_holder> {

    Context context;
    List<Integer> juz_list;

    public Juz_RecyclerAdapter(Context context, List<Integer> juz_list) {
        this.context = context;
        this.juz_list = juz_list;
    }

    @NonNull
    @Override
    public Juz_View_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_juz, parent, false);
        return new Juz_View_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Juz_View_holder holder, int position) {
        int juzNumber = juz_list.get(position);
        holder.juz_number.setText(String.valueOf(juzNumber));
        holder.juz_name_english.setText("Juz " + juzNumber);
        holder.juz_description.setText("Quran Part " + juzNumber);

        holder.itemView.setOnClickListener(v->{
            MyApplication application = (MyApplication) context.getApplicationContext();
            application.setJuz_Clicked(juzNumber + "");
            context.startActivity(new Intent(context, ReadPage.class));
        });
    }

    @Override
    public int getItemCount() {
        return juz_list.size();
    }

    public void updateList(List<Integer> newList) {
        this.juz_list = newList;
        notifyDataSetChanged();
    }

    public static class Juz_View_holder extends RecyclerView.ViewHolder {
        TextView juz_number, juz_name_english, juz_description;

        public Juz_View_holder(@NonNull View itemView) {
            super(itemView);
            juz_number = itemView.findViewById(R.id.juz_number);
            juz_name_english = itemView.findViewById(R.id.juz_name_english);
            juz_description = itemView.findViewById(R.id.juz_description);
        }
    }
}
