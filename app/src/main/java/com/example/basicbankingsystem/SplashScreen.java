package com.example.basicbankingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;

    TextView mAppNameTextView;
    ImageView mLogoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //to hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mLogoImageView = findViewById(R.id.logo);
        mAppNameTextView = findViewById(R.id.app_name);

        mAppNameTextView.startAnimation(AnimationUtils.loadAnimation(SplashScreen.this, R.anim.bottom_ani));
        mLogoImageView.startAnimation(AnimationUtils.loadAnimation(SplashScreen.this, R.anim.up_ani));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            }
        }, SPLASH_SCREEN);
    }
}