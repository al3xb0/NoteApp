package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //load password
        SharedPreferences settings = EncryptedSharedPreferencesHelper.getEncryptedSharedPreferences(this);
        String password = null;
        if (settings != null) {
            password = settings.getString("password", "");
        }

        String finalPassword = password;

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            if (finalPassword == null || finalPassword.equals("")){
                //if no password exists
                intent = new Intent(getApplicationContext(), CreatePasswordActivity.class);
            } else {
                //if password exists
                intent = new Intent(getApplicationContext(), EnterPasswordActivity.class);
            }
            startActivity(intent);
            finish();

        }, 2000);
    }
}