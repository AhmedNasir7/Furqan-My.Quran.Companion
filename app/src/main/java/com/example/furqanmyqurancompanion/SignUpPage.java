package com.example.furqanmyqurancompanion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpPage extends AppCompatActivity {

    TextInputEditText etSignUpEmail , etSignupPassword , etSignupConfirmPassword;
    MaterialButton signupButton;
    TextView login_page_nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        login_page_nav.setOnClickListener(v->{
            startActivity(new Intent(SignUpPage.this, LoginPage.class));
            finish();
        });
    }

    private void init(){
        etSignUpEmail=findViewById(R.id.etSignUpEmail);
        etSignupPassword=findViewById(R.id.etSignUpPassword);
        etSignupConfirmPassword=findViewById(R.id.etSignupConfirmPassword);
        signupButton=findViewById(R.id.btnSignUp);
        login_page_nav=findViewById(R.id.tvLogin);
    }
}