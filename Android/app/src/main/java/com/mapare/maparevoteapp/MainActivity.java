package com.mapare.maparevoteapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.mapare.maparevoteapp.model.entity_to_receive.User;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private AppBarConfiguration mAppBarConfiguration;

    // Needs to be here (don't listen to IDE)
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        // TODO: Need to have a look (QRcode, nfc)
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        drawerLayout = findViewById(R.id.drawer_layout);
        // Needed to close the keyboard if needed
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {}
            @Override
            public void onDrawerClosed(@NonNull View drawerView) {}
            @Override
            public void onDrawerStateChanged(int newState) {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_publicVotes, R.id.nav_privateVotes, R.id.nav_startedVotes, R.id.nav_login, R.id.nav_signup, R.id.nav_logout)
                .setOpenableLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        // Listener on the items of the nav menu
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_logout) {
                Log.i("debug", "clicked");

                Toast.makeText(getBaseContext(), "Déconnecté", Toast.LENGTH_SHORT).show();
                getSharedPreferences("Login", Context.MODE_PRIVATE).edit().putString("token", null).apply();
                getSharedPreferences("Login", Context.MODE_PRIVATE).edit().putString("name", "##erased##").apply();
            } else {
                // This is for closing the drawer after acting on it, doesn't want this feature when menu_logout button is clicked on
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            // If already on the page that you want to navigate , doesn't reload the page
            if( !(navigationView.getMenu().findItem(R.id.nav_publicVotes).isChecked() && id == R.id.nav_publicVotes) ) {
                // This is for maintaining the behavior of the Navigation view
                NavigationUI.onNavDestinationSelected(item, navController);
            }
            return true;
        });
        // Defines the visibility of the items by default
        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_login).setVisible(token == null);
        nav_Menu.findItem(R.id.nav_signup).setVisible(token == null);
        nav_Menu.findItem(R.id.nav_logout).setVisible(token != null);
        nav_Menu.findItem(R.id.nav_privateVotes).setVisible(token != null);
        nav_Menu.findItem(R.id.nav_startedVotes).setVisible(token != null);

        // Header
        View headerView = navigationView.getHeaderView(0);
        TextView nameHeader = headerView.findViewById(R.id.name);
        TextView emailHeader = headerView.findViewById(R.id.email);

        if (token != null) {
            String name = sharedPreferences.getString("name", null);
            String email = sharedPreferences.getString("email", null);
            nameHeader.setText(name);
            emailHeader.setText(email);
        } else {
            nameHeader.setText(R.string.app_name);
            emailHeader.setText(R.string.welcome);
        }


        listener = (prefs, key) -> {
            Log.i("key", key);
            String content = prefs.getString(key, null);
            // To know when the user switch between login and logout
            switch (key) {
                case "token":
                    // Fetch user infos
                    if (content != null) {
                        getMeRequest();
                    }

                    // Add items if it depends on the user who should be logged in or not
                    nav_Menu.findItem(R.id.nav_login).setVisible(content == null);
                    nav_Menu.findItem(R.id.nav_signup).setVisible(content == null);
                    nav_Menu.findItem(R.id.nav_logout).setVisible(content != null);
                    nav_Menu.findItem(R.id.nav_privateVotes).setVisible(content != null);
                    nav_Menu.findItem(R.id.nav_startedVotes).setVisible(content != null);

                    // When disconnected or connected, navigate to the "home page"
                    navController.navigate(R.id.nav_publicVotes);
                    drawerLayout.openDrawer(GravityCompat.START);
                    break;
                case "name":
                    // Header
                    if (content.equals("##erased##")) {
                        nameHeader.setText(R.string.app_name);
                        emailHeader.setText(R.string.welcome);

                    } else {
                        String name = prefs.getString("name", null);
                        String email = prefs.getString("email", null);
                        nameHeader.setText(name);
                        emailHeader.setText(email);
                    }
                    // If navigation is needed
                    break;
                case "go_to":
                    Log.i("listener", content + "");
                    switch (content) {
                        case "login page":
                            navController.navigate(R.id.nav_login);
                            break;
                        // Add other destinations
                    }
                    prefs.edit().putString("go_to", "nowhere").apply();
                    break;
            }
        };
        getSharedPreferences("Login", MODE_PRIVATE).registerOnSharedPreferenceChangeListener(listener);

        // Check if token is the session is still active
        if (token != null) {
            getMeRequest();
        }

        // Start the app with the drawer opened
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        // Handle back click to close menu
        Log.i("backPressed", "touche détectée");
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            Log.i("backPressed", "executé");
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Log.i("backPressed", "non executé");
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void getMeRequest() {
        SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.API_URL) + getResources().getString(R.string.ME_URL);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                response -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    try {
                        User user = objectMapper.readValue(response, User.class);
                        prefs.edit().putString("name", user.getFirstname() + "\t" + user.getName()).apply();
                        Log.i("getMe", response);
                    } catch (IOException e) { // shouldn't happen
                        e.printStackTrace();
                    }

                }, error -> {
            // TODO: manage different types of errors
            if (error instanceof AuthFailureError) {
                prefs.edit().putString("go_to", "login page").apply();
                Toast.makeText(this, "Session expirée", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                String token = getSharedPreferences("Login", Context.MODE_PRIVATE).getString("token", null);
                params.put("Accept", "application/json; charset=utf-8");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}