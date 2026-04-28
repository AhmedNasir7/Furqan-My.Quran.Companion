package com.example.furqanmyqurancompanion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    LottieAnimationView lottie;
    FirebaseAuth fb_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        new Handler().postDelayed(() -> {
            SharedPreferences spref = getSharedPreferences("user",MODE_PRIVATE);
            boolean isLoggedIn = spref.getBoolean("isLoggedIn",false);
            if(isLoggedIn==true)
            {
                String email = spref.getString("user_email","");
                String password = spref.getString("user_password","");


                fb_auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {


                        startActivity(new Intent(SplashScreen.this,MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SplashScreen.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SplashScreen.this, LoginPage.class));
                        finish();
                    }
                });

            }
            else
            {
                startActivity(new Intent(SplashScreen.this, LoginPage.class));
                finish();
            }

        }, 4200);
    }

    public void init(){
        fb_auth=FirebaseAuth.getInstance();
        lottie = findViewById(R.id.lottie_loading_bar);
        lottie.setAnimation(R.raw.loading_bar);
        lottie.setSpeed(1.7f);
        lottie.playAnimation();
    }
}