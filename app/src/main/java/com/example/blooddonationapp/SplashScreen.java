package com.example.blooddonationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private  ImageView logo;
    private TextView logoname, slogan;
    Animation topAnimation, bottomAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        logo = findViewById(R.id.logo);
        logoname = findViewById(R.id.logoname);
        slogan = findViewById(R.id.slogan);

        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.botton_animation);

        logo.setAnimation(topAnimation);
        logoname.setAnimation(bottomAnimation);
        slogan.setAnimation(bottomAnimation);

        Thread thread = new Thread(){

            @Override
            public void run() {

                try {
                    sleep(4300);
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };thread.start();
    }
}