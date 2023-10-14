package com.example.noteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RecoverPassword extends AppCompatActivity {

    EditText editText;
    Button button;

    String secretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        //load secretKey
        SharedPreferences settings = EncryptedSharedPreferencesHelper.getEncryptedSharedPreferences(this);
        if (settings != null) {
            secretKey = settings.getString("secretKey", "");
        }

        editText = (EditText) findViewById(R.id.enterRecoverSecretWord);
        button = (Button) findViewById(R.id.buttonRecover);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();

                if (text.equals(secretKey)){
                    Intent intent = new Intent(getApplicationContext(), CreatePasswordActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RecoverPassword.this, "Wrong secret word", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}