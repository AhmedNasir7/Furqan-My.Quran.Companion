package com.example.furqanmyqurancompanion.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.furqanmyqurancompanion.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    TextInputEditText etLoginEmail , etLoginPassword;
    MaterialButton login_button;
    TextView signup_page_nav, guest_mode_nav;

    FirebaseAuth fb_auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        init();

        signup_page_nav.setOnClickListener(v->{
            startActivity(new Intent(LoginPage.this, SignUpPage.class));
            finish();
        });

        guest_mode_nav.setOnClickListener(v -> {
            SharedPreferences sPref = getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putBoolean("isGuest", true);
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            startActivity(new Intent(LoginPage.this, MainActivity.class));
            finish();
        });

        login_button.setOnClickListener(v->{
            String email = etLoginEmail.getText().toString().trim();
            String password = etLoginPassword.getText().toString().trim();



            fb_auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    SharedPreferences spref = getSharedPreferences("user",MODE_PRIVATE);
                    SharedPreferences.Editor editor = spref.edit();

                    boolean isLoggedIn=true;
                    editor.putBoolean("isLoggedIn",isLoggedIn);
                    editor.putString("user_email",email);
                    editor.putString("user_password",password);
                    editor.commit();

                    startActivity(new Intent(LoginPage.this,MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginPage.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void init(){
        etLoginEmail=findViewById(R.id.etLoginEmail);
        etLoginPassword=findViewById(R.id.etLoginPassword);
        login_button=findViewById(R.id.btnLogin);
        signup_page_nav=findViewById(R.id.tvSignUp);
        guest_mode_nav = findViewById(R.id.tvGuestMode);
        fb_auth = FirebaseAuth.getInstance();
    }
}