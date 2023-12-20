package com.example.noteapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText editText1, editText2, editText3;
    Button buttonConfirm, buttonCancel;

    String password;

    private int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editText1 = findViewById(R.id.editTextTextPassword1);
        editText2 = findViewById(R.id.editTextTextPassword2);
        editText3 = findViewById(R.id.editTextActivePassword);
        buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonCancel = findViewById(R.id.buttonCancel);

        buttonConfirm.setOnClickListener(view -> {
            String text1 = editText1.getText().toString();
            String text2 = editText2.getText().toString();
            String oldPassword = editText3.getText().toString();

            SharedPreferences settings = EncryptedSharedPreferencesHelper.getEncryptedSharedPreferences(this);
            if (settings != null) {
                password = settings.getString("password", "");
            }

            if (text1.equals("") || text2.equals("")) {
                Toast.makeText(ChangePasswordActivity.this, "Password not entered", Toast.LENGTH_SHORT).show();
            } else {
                if (oldPassword.equals("")) {
                    Toast.makeText(ChangePasswordActivity.this, "Enter your active password", Toast.LENGTH_SHORT).show();
                } else {
                    if (HashHelper.verifyHash(oldPassword, password)) {
                        if (text1.equals(text2)) {
                            String regex = "^(?=\\S+$).{6,36}$";
                            Pattern pattern = Pattern.compile(regex);
                            Matcher matcher = pattern.matcher(text1);
                                if (matcher.matches()) {
                                    String hashedPassword = HashHelper.hashFunction(text1);
                                    SharedPreferences.Editor editor;
                                    if (settings != null) {
                                        editor = settings.edit();
                                        editor.putString("password", hashedPassword);
                                        editor.apply();
                                    }

                                    Intent intent = new Intent(getApplicationContext(), EnterPasswordActivity.class);
                                    startActivity(intent);
                                    finish();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Active password don't match pattern", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        counter--;
                        Toast.makeText(ChangePasswordActivity.this, "Wrong active password. Attempts left " + counter, Toast.LENGTH_SHORT).show();
                        if (counter <= 0) {
                            Toast.makeText(ChangePasswordActivity.this, "No more attempts", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), EnterPasswordActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                }
            }
        });

        buttonCancel.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EnterPasswordActivity.class);
            startActivity(intent);
            finish();
        });
    }
}