package com.example.furqanmyqurancompanion.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furqanmyqurancompanion.Model.Prayer;
import com.example.furqanmyqurancompanion.R;

import java.util.List;

public class PrayerDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_detail);

        Prayer prayer = (Prayer) getIntent().getSerializableExtra("prayer");
        if (prayer == null) {
            finish();
            return;
        }

        TextView tvTitle = findViewById(R.id.tvPrayerTitle);
        tvTitle.setText(prayer.getName());

        RecyclerView rvSteps = findViewById(R.id.rvPrayerSteps);
        rvSteps.setLayoutManager(new LinearLayoutManager(this));
        rvSteps.setAdapter(new StepAdapter(prayer.getSteps()));
        
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private static class StepAdapter extends RecyclerView.Adapter<StepAdapter.VH> {
        private final List<Prayer.Step> steps;

        StepAdapter(List<Prayer.Step> steps) {
            this.steps = steps;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prayer_step, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            Prayer.Step step = steps.get(position);
            holder.tvTitle.setText((position + 1) + ". " + step.getTitle());
            holder.tvDesc.setText(step.getDescription());
            if (step.getArabicText() != null && !step.getArabicText().isEmpty()) {
                holder.tvArabic.setVisibility(View.VISIBLE);
                holder.tvArabic.setText(step.getArabicText());
            } else {
                holder.tvArabic.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() { return steps.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDesc, tvArabic;
            VH(@NonNull View v) {
                super(v);
                tvTitle = v.findViewById(R.id.stepTitle);
                tvDesc = v.findViewById(R.id.stepDesc);
                tvArabic = v.findViewById(R.id.stepArabic);
            }
        }
    }
}
