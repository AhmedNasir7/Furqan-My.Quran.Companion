package com.example.furqanmyqurancompanion.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpPage extends AppCompatActivity {

    TextInputEditText etSignUpEmail , etSignupPassword , etSignupConfirmPassword;
    MaterialButton signupButton;
    FirebaseAuth fb_auth;
    TextView login_page_nav;

    FirebaseFirestore database;




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

        signupButton.setOnClickListener(v->{
            String email = etSignUpEmail.getText().toString().trim();
            String password = etSignupPassword.getText().toString().trim();
            String cPassword = etSignupConfirmPassword.getText().toString().trim();

            if(cPassword.equals(password))
            {
                fb_auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        new Handler().postDelayed(() -> {
                            Toast.makeText(SignUpPage.this,"Signup was successful",Toast.LENGTH_LONG).show();
                            String userId = fb_auth.getCurrentUser().getUid();
                            HashMap<String, Object> user = new HashMap<>();
                            user.put("email", email);

                            database.collection("user").document(userId).set(user).
                                    addOnSuccessListener(aVoid -> Log.d("TAG", "DocumentSnapshot successfully written!"))
                                    .addOnFailureListener(e -> Log.w("TAG", "Error writing document", e));





                        }, 1000);
                        SharedPreferences spref = getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor editor = spref.edit();

                        boolean isLoggedIn=true;
                        editor.putBoolean("isLoggedIn",isLoggedIn);
                        editor.putString("user_email",email);
                        editor.putString("user_password",password);
                        editor.commit();
                        startActivity(new Intent(SignUpPage.this,MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpPage.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                Toast.makeText(SignUpPage.this,"Passwords don't match",Toast.LENGTH_LONG).show();
                etSignupConfirmPassword.setError(

                        "No match"
                );
            }


        });
    }

    private void init(){
        fb_auth=FirebaseAuth.getInstance();
        etSignUpEmail=findViewById(R.id.etSignUpEmail);
        etSignupPassword=findViewById(R.id.etSignUpPassword);
        etSignupConfirmPassword=findViewById(R.id.etSignupConfirmPassword);
        signupButton=findViewById(R.id.btnSignUp);
        login_page_nav=findViewById(R.id.tvLogin);
        database=FirebaseFirestore.getInstance();

    }
}