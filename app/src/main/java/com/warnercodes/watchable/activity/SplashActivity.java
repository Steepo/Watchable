package com.warnercodes.watchable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private int movieId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int secondsDelayed = 1;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            movieId = Integer.parseInt(String.valueOf(bundle.get("movieId")));
        } else {
            Log.d("Extra", String.valueOf(movieId));
        }

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent newintent = new Intent(SplashActivity.this, GoogleSignInActivity.class);
                if (movieId != 0) {
                    newintent.putExtra("movieId", movieId);
                    Log.d("Extra", "MovieId: " + movieId);
                }
                startActivity(newintent);
                finish();
            }
        }, secondsDelayed * 1000);

    }
}