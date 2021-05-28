package com.example.maparemap;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int MENU_ID = 0;
    private static final int LOGOUT_ID  = 1000;
    private static final int SETTINGS_ID  = 2000;
    private static final int ORDER = 0;

    private View dialogView;
    private AlertDialog joinDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button joinLobbyButton = findViewById(R.id.joinLobbyButton);

        joinLobbyButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_join_lobby, viewGroup, false);
            builder.setView(dialogView);
            joinDialog = builder.create();
            joinDialog.show();
            Button cancelJoinButton = dialogView.findViewById(R.id.cancelJoinButton);
            Log.i("test", cancelJoinButton.toString());
            cancelJoinButton.setOnClickListener(w -> {
                joinDialog.cancel();
            });
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(MENU_ID, LOGOUT_ID, ORDER, getResources().getString(R.string.logout));
        menu.add(MENU_ID, SETTINGS_ID, ORDER, getResources().getString(R.string.settings));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == LOGOUT_ID) {
            SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor Ed = sp.edit();
            Ed.putString("email", null);
            Ed.putString("password", null);
            Ed.apply();
            Intent login_intent = new Intent(this, LoginActivity.class);
            startActivity(login_intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}