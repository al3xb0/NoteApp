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

public class CreatePasswordActivity extends AppCompatActivity {

    EditText editText1, editText2, editText3;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        editText1 = findViewById(R.id.editTextTextPassword1);
        editText2 = findViewById(R.id.editTextTextPassword2);
        editText3 = findViewById(R.id.editTextSecretKey);
        button = findViewById(R.id.buttonConfirm);

        button.setOnClickListener(view -> {
            String text1 = editText1.getText().toString();
            String text2 = editText2.getText().toString();
            String secretKey = editText3.getText().toString();



            if (text1.equals("") || text2.equals("")){
                Toast.makeText(CreatePasswordActivity.this, "Password not entered", Toast.LENGTH_SHORT).show();
            } else {
                if (secretKey.equals("")){
                    Toast.makeText(CreatePasswordActivity.this, "Enter secret word please", Toast.LENGTH_SHORT).show();
                } else {
                    if(text1.equals(text2)) {
                        String regex = "^(?=\\S+$).{6,36}$";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(text1);

                        if (matcher.matches()) {
                            String hashedPassword = HasherHelper.hashFunction(text1);
                            String hashedSecretKey = HasherHelper.hashFunction(secretKey);
                            SharedPreferences settings = EncryptedSharedPreferencesHelper.getEncryptedSharedPreferences(CreatePasswordActivity.this);
                            SharedPreferences.Editor editor;
                            if (settings != null) {
                                editor = settings.edit();
                                editor.putString("password", hashedPassword);
                                editor.putString("secretKey", hashedSecretKey);
                                editor.apply();
                            }


                            Intent intent = new Intent(getApplicationContext(), EnterPasswordActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(CreatePasswordActivity.this, "Password don't match pattern", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CreatePasswordActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}