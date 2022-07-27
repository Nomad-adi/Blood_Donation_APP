package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private TextInputEditText forgetEmail;
    Button forgetBtn;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        forgetEmail = findViewById(R.id.forgetEmail);
        forgetBtn = findViewById(R.id.forgetpass_btn);
        auth = FirebaseAuth.getInstance();

        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = forgetEmail.getText().toString();
                if (TextUtils.isEmpty(email)){
                    forgetEmail.setError("Email is required!");
                }else{
                    // forget password
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgetPasswordActivity.this, "Email send Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
                                finish();
                            }else{
                                Toast.makeText(ForgetPasswordActivity.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}