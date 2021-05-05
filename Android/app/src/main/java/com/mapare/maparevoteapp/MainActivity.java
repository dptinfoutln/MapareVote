package com.mapare.maparevoteapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
                R.id.nav_publicVotes, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_login, R.id.nav_signup, R.id.nav_logout)
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
        String token = getSharedPreferences("Login", MODE_PRIVATE).getString("token", null);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_login).setVisible(token == null);
        nav_Menu.findItem(R.id.nav_signup).setVisible(token == null);
        nav_Menu.findItem(R.id.nav_logout).setVisible(token != null);

        // Listener to know when the user switch between login and logout
        listener = (prefs, key) -> {
            Menu nav_Menu1 = navigationView.getMenu();

            String token1 = prefs.getString("token", null);
            Log.i("token", token1 + "");

            // Add items if it depends on the user who should be logged in or not
            nav_Menu1.findItem(R.id.nav_login).setVisible(token1 == null);
            nav_Menu1.findItem(R.id.nav_signup).setVisible(token1 == null);
            nav_Menu1.findItem(R.id.nav_logout).setVisible(token1 != null);

            // When disconnected or connected, navigate to the "home page"
            navController.navigate(R.id.nav_publicVotes);
            drawerLayout.openDrawer(GravityCompat.START);
        };
        getSharedPreferences("Login", MODE_PRIVATE).registerOnSharedPreferenceChangeListener(listener);

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
}