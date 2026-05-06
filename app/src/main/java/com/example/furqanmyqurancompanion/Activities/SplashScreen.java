package com.example.furqanmyqurancompanion.Activities;

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
import com.example.furqanmyqurancompanion.Model.MyApplication;
import com.example.furqanmyqurancompanion.R;
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
        
        // Use a shorter delay and a simpler logic flow
        new Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            try {
                SharedPreferences spref = getSharedPreferences("user", MODE_PRIVATE);
                boolean isLoggedIn = spref.getBoolean("isLoggedIn", false);
                boolean isGuest = spref.getBoolean("isGuest", false);

                if (isLoggedIn) {
                    // Quick check: If Firebase already has a valid session, just go.
                    if (fb_auth.getCurrentUser() != null) {
                        startMainActivity();
                        return;
                    }

                    String email = spref.getString("user_email", "");
                    String password = spref.getString("user_password", "");

                    if (email.isEmpty() || password.isEmpty()) {
                        startLoginPage();
                    } else {
                        // Attempt login with a timeout or fallback
                        fb_auth.signInWithEmailAndPassword(email, password)
                                .addOnSuccessListener(authResult -> startMainActivity())
                                .addOnFailureListener(e -> {
                                    android.util.Log.e("SplashScreen", "Auto-login failed", e);
                                    startLoginPage();
                                });
                        
                        // Safety fallback: if no response in 10 seconds, go to login
                        new Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                            if (!isFinishing()) {
                                startLoginPage();
                            }
                        }, 10000);
                    }
                } else if (isGuest) {
                    startMainActivity();
                } else {
                    startLoginPage();
                }
            } catch (Exception e) {
                android.util.Log.e("SplashScreen", "Error in Splash logic", e);
                startLoginPage();
            }
        }, 1500);
    }

    private void startMainActivity() {
        if (!isFinishing()) {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }
    }

    private void startLoginPage() {
        if (!isFinishing()) {
            startActivity(new Intent(SplashScreen.this, LoginPage.class));
            finish();
        }
    }

    public void init(){
        fb_auth=FirebaseAuth.getInstance();
        lottie = findViewById(R.id.lottie_loading_bar);
        lottie.setAnimation(R.raw.loading_bar);
        lottie.setSpeed(1.7f);
        lottie.playAnimation();
    }
}