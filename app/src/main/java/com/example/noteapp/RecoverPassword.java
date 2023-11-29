package com.example.noteapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecoverPassword extends AppCompatActivity {

    EditText editText;
    Button button;

    String secretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        SharedPreferences settings = EncryptedSharedPreferencesHelper.getEncryptedSharedPreferences(this);
        if (settings != null) {
            secretKey = settings.getString("secretKey", "");
        }

        editText = findViewById(R.id.enterRecoverSecretWord);
        button = findViewById(R.id.buttonRecover);

        button.setOnClickListener(view -> {
            String text = editText.getText().toString();

            if (HashHelper.verifyHash(text,secretKey)){
                Intent intent = new Intent(getApplicationContext(), CreatePasswordActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(RecoverPassword.this, "Wrong secret word", Toast.LENGTH_SHORT).show();
            }
        });
    }
}