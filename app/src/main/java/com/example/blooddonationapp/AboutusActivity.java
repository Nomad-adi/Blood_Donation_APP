package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.blooddonationapp.R;

public class AboutusActivity extends AppCompatActivity {

    private TextView desc;
    private Toolbar toolbar_aboutus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        toolbar_aboutus = findViewById(R.id.toolbar_aboutus);
        setSupportActionBar(toolbar_aboutus);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About Us");


        desc = findViewById(R.id.desc);
        String about_us = "Blood Donation App\n" +
                " Find Blood or Become a Donor!";
        desc.setText(about_us);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}