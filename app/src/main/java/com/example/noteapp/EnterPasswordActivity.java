package com.example.noteapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnterPasswordActivity extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    EditText editText;
    Button button, button2;
    ImageButton fingerprintButton;

    String password;

    private int counter = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        SharedPreferences settings = EncryptedSharedPreferencesHelper.getEncryptedSharedPreferences(this);
        if (settings != null) {
            password = settings.getString("password", "");
        }

        editText = findViewById(R.id.enterPassword);
        button = findViewById(R.id.buttonEnter);
        button2 = findViewById(R.id.buttonForgotPassword);
        fingerprintButton = findViewById(R.id.fingerprintButton);

        button.setOnClickListener(view -> {
            String text = editText.getText().toString();
            String regex = "^(?=\\S+$).{1,36}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()){
                if (HashHelper.verifyHash(text,password)){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    counter--;
                    Toast.makeText(EnterPasswordActivity.this, "Wrong password. Attempts left " + counter, Toast.LENGTH_SHORT).show();
                    if (counter <= 0) {
                        button.setEnabled(false);
                        Toast.makeText(EnterPasswordActivity.this, "No more attempts, try after 30 sec", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(() -> {
                            button.setEnabled(true);
                            counter = 3;
                            Toast.makeText(EnterPasswordActivity.this, "You can try again!", Toast.LENGTH_SHORT).show();

                        },30000);
                    }
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

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(EnterPasswordActivity.this,
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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        fingerprintButton.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });


    }
}