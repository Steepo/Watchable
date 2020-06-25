package com.warnercodes.watchable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.SignInButton;
import com.warnercodes.watchable.databinding.ActivityLoginBinding;

public class LoginPageActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    public Button buttonFacebook;
    public SignInButton buttonGoogle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        super.onCreate(savedInstanceState);

        buttonFacebook = binding.buttonFacebook;
        buttonGoogle = binding.buttonGoogle;

        buttonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPageActivity.this, FacebookSignInActivity.class));
            }
        });
        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPageActivity.this, GoogleSignInActivity.class));
            }
        });

    }
}