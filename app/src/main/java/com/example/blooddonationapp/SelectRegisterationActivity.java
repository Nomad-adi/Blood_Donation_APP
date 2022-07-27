package com.example.blooddonationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectRegisterationActivity extends AppCompatActivity {

    private Button DonarRegistertnBtn,RecipientRegisterationBtn;
    private TextView back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_registeration);

        DonarRegistertnBtn = findViewById(R.id.donarbtn);
        RecipientRegisterationBtn = findViewById(R.id.recipientbtn);
        back_btn = findViewById(R.id.back_bnt);

        DonarRegistertnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectRegisterationActivity.this, DonarRegisterationActivity.class);
                startActivity(intent);
            }
        });

        RecipientRegisterationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectRegisterationActivity.this, RecipientRegisterationActivity.class);
                startActivity(intent);
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectRegisterationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}