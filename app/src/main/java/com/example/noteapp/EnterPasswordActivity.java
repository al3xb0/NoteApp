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

        editText = (EditText) findViewById(R.id.enterPassword);
        button = (Button) findViewById(R.id.buttonEnter);
        button2 = (Button) findViewById(R.id.buttonForgotPassword);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();

                if (text.equals(password)){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EnterPasswordActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EnterPasswordActivity.this);
                builder.setTitle("Forgot password?");
                builder.setMessage("Do you want to recover your password?");
                builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), RecoverPassword.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNeutralButton("Cancel", null);
                builder.show();
            }
        });
    }
}