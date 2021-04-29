package com.mapare.maparevoteapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent main_intent = new Intent(this, MainActivity.class);

        SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);
        String savedEmail = sp.getString("email", null);
        String savedPass = sp.getString("password", null);

//        if (savedEmail != null && savedPass != null){
//            startActivity(main_intent);
//            finish();
//        }

        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            EditText emailField = findViewById(R.id.emailField);
            String email = emailField.getText().toString();
            EditText passwordField = findViewById(R.id.passwordField);
            String password = passwordField.getText().toString();
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

            if(TextUtils.isEmpty(email)){
                emailField.setError(getResources().getString(R.string.empty_email));
            }
            else if (TextUtils.isEmpty(password)) {
                passwordField.setError(getResources().getString(R.string.empty_password));
            }
            else if (!isGoodPassword(email, password)) {
                passwordField.setError(getResources().getString(R.string.incorrect_password));
            }
            else {

                SharedPreferences.Editor Ed = sp.edit();
                Ed.putString("email", email);
                Ed.putString("password", password);
                Ed.apply();
                startActivity(main_intent);
                finish();
            }

            loginButton.startAnimation(shake);
        });
    }

    private boolean isGoodPassword(String email, String password){
        return "password".equals(password);
    }
}