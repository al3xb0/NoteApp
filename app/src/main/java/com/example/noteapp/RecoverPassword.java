package com.example.noteapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class RecoverPassword extends AppCompatActivity {


    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    EditText editText;
    Button button, button2;


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
        button2 = findViewById(R.id.buttonCancel);



        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(RecoverPassword.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CreatePasswordActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Recover your password")
                .setSubtitle("Confirm with biometric is that you")
                .setNegativeButtonText("Cancel")
                .build();

        button.setOnClickListener(view -> {
            String text = editText.getText().toString();

            if (HashHelper.verifyHash(text,secretKey)){
                biometricPrompt.authenticate(promptInfo);
            } else {
                Toast.makeText(RecoverPassword.this, "Wrong secret word", Toast.LENGTH_SHORT).show();
            }
        });

        button2.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EnterPasswordActivity.class);
            startActivity(intent);
            finish();
        });
    }
}