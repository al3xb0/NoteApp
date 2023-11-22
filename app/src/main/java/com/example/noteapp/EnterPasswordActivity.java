package com.example.noteapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnterPasswordActivity extends AppCompatActivity {

    EditText editText;
    Button button, button2;

    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        //load password
        SharedPreferences settings = EncryptedSharedPreferencesHelper.getEncryptedSharedPreferences(this);
        if (settings != null) {
            password = settings.getString("password", "");
        }

        editText = findViewById(R.id.enterPassword);
        button = findViewById(R.id.buttonEnter);
        button2 = findViewById(R.id.buttonForgotPassword);

        button.setOnClickListener(view -> {
            String text = editText.getText().toString();
            String regex = "^(?=\\S+$).{1,36}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()){
                if (HasherHelper.verifyHash(text,password)){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EnterPasswordActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EnterPasswordActivity.this, "Min 1 character, max 36", Toast.LENGTH_SHORT).show();
            }
        });

        button2.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EnterPasswordActivity.this);
            builder.setTitle("Forgot password?");
            builder.setMessage("Do you want to recover your password?");
            builder.setPositiveButton("Recover", (dialogInterface, i) -> {
                Intent intent = new Intent(getApplicationContext(), RecoverPassword.class);
                startActivity(intent);
                finish();
            });
            builder.setNeutralButton("Cancel", null);
            builder.show();
        });
    }
}