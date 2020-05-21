package com.warnercodes.watchable;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.warnercodes.watchable.ui.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(com.warnercodes.watchable.SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, secondsDelayed * 500);
    }
}