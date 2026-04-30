package com.example.furqanmyqurancompanion.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.furqanmyqurancompanion.R;
import com.google.android.material.button.MaterialButton;

public class TasbeehCounterPage extends AppCompatActivity {


    View Counter_CLick;
    ImageView Back_button;
    TextView count_text;
    MaterialButton reset_button;
    int Count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tasbeeh_counter_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        Back_button.setOnClickListener(v->{
            finish();
        });
        Counter_CLick.setOnClickListener(v->{
            Count+=1;
            count_text.setText(String.valueOf(Count));
        });
        reset_button.setOnClickListener(v->{
            Count=0;
            count_text.setText(String.valueOf(Count));
        });

    }


    public void init()
    {
        Counter_CLick=findViewById(R.id.clickable_Area);
        Back_button=findViewById(R.id.back_button_tasbeeh);
        count_text=findViewById(R.id.count_text);
        reset_button=findViewById(R.id.reset_button);
        count_text.setText(String.valueOf(Count));

    }

}
