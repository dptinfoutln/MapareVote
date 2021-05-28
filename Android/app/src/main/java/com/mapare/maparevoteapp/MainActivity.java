package com.mapare.maparevoteapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;

import android.widget.ListView;
import android.widget.SeekBar;
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
import com.mapare.maparevoteapp.adapter.SortAdapter;
import com.mapare.maparevoteapp.model.entity_to_receive.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private AppBarConfiguration mAppBarConfiguration;

    private static Context AppContext;

    // Needs to be here (don't listen to IDE)
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    // Defines key preferences (sonar advices)
    public static final String LOGIN_STRING_KEY = "Login";
    public static final String TOKEN_STRING_KEY = "token";
    public static final String NAME_STRING_KEY = "name";
    public static final String EMAIL_STRING_KEY = "email";
    public static final String GO_TO_STRING_KEY = "go_to";
    public static final String FILTER_STRING_KEY = "Filter";
    public static final String PAGE_SIZE_STRING_KEY = "page_size";
    public static final String OPEN_VOTE_STRING_KEY = "open_vote";
    public static final String SEARCH_STRING_KEY = "search";
    public static final String SORTING_BY_STRING_KEY = "sorting_by";

    /**
     * The Progress value of page size.
     */
// Needed for filters
    int progressValueOfPageSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppContext = this; // To make our own getContext() accross the classes

        // Clear filters cache
        getSharedPreferences(FILTER_STRING_KEY, MODE_PRIVATE).edit().remove(PAGE_SIZE_STRING_KEY).apply();
        getSharedPreferences(FILTER_STRING_KEY, MODE_PRIVATE).edit().remove(OPEN_VOTE_STRING_KEY).apply();
        getSharedPreferences(FILTER_STRING_KEY, MODE_PRIVATE).edit().remove(SEARCH_STRING_KEY).apply();
        getSharedPreferences(FILTER_STRING_KEY, MODE_PRIVATE).edit().putString(SORTING_BY_STRING_KEY, getResources().getString(R.string.none_sort)).apply();


        LayoutInflater inflater = this.getLayoutInflater();
        // Popup for choosing page size
        AlertDialog.Builder pageSizeBuilder = new AlertDialog.Builder(this);
        View dialogViewPageSize = inflater.inflate(R.layout.dialog_page_size, null);
        pageSizeBuilder.setView(dialogViewPageSize);
        SeekBar seekBar = dialogViewPageSize.findViewById(R.id.dialog_seekBar);
        seekBar.setMin(10);
        seekBar.setMax(150);
        seekBar.setKeyProgressIncrement(10);
        int value = getSharedPreferences(FILTER_STRING_KEY, MODE_PRIVATE).getInt(PAGE_SIZE_STRING_KEY, 20);
        seekBar.setProgress(value);

        TextView seekText = dialogViewPageSize.findViewById(R.id.dialog_seekText);
        String progressText = "Nombre de votes par page : " + value;
        seekText.setText(progressText);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            String progressText;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValueOfPageSize = progress/10*10;
                progressText = "Nombre de votes par page : " + progressValueOfPageSize;
                seekText.setText(progressText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not used
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekText.setText(progressText);
            }
        });
        pageSizeBuilder.setPositiveButton("OK", (dialog, which) -> getSharedPreferences(FILTER_STRING_KEY, MODE_PRIVATE).edit().putInt(PAGE_SIZE_STRING_KEY, progressValueOfPageSize).apply());
        pageSizeBuilder.setNegativeButton("Annuler", (dialog, which) -> {});
        AlertDialog pageSizeDialog = pageSizeBuilder.create();

        //Popup for sorting
        AlertDialog.Builder sortBuilder = new AlertDialog.Builder(this);
        View dialogViewSort = inflater.inflate(R.layout.dialog_sort, null);
        sortBuilder.setView(dialogViewSort);
        ListView listViewSort = dialogViewSort.findViewById(R.id.sort_list);
        String savePicked = getSharedPreferences(FILTER_STRING_KEY, MODE_PRIVATE).getString(SORTING_BY_STRING_KEY, getResources().getString(R.string.none_sort));
        List<String> sortList = getSortList();
        SortAdapter adapterSort = new SortAdapter(this, sortList, savePicked);
        listViewSort.setAdapter(adapterSort);
        sortBuilder.setPositiveButton("OK", (dialog, which) -> getSharedPreferences(FILTER_STRING_KEY, MODE_PRIVATE).edit().putString(SORTING_BY_STRING_KEY, adapterSort.getSortPicked()).apply());
        AlertDialog sortDialog = sortBuilder.create();



        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_menu_filter));
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.app_bar_open_votes) {
                item.setChecked(!item.isChecked());
                getSharedPreferences(FILTER_STRING_KEY, MODE_PRIVATE).edit().putBoolean(OPEN_VOTE_STRING_KEY, item.isChecked()).apply();
            } else if (item.getItemId() == R.id.app_bar_page_size) {
                pageSizeDialog.show();
            } else if (item.getItemId() == R.id.app_bar_sort) {
                sortDialog.show();
            }
            return false;
        });
//


        // TODO: For (QRcode, nfc)
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(view -> Snackbar.make(view, "QRCode", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        drawerLayout = findViewById(R.id.drawer_layout);
        // Needed to close the keyboard if needed
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                // Not used
            }
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                // Not used
            }
            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                // Not used
            }
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
        navigationView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_publicVotes, R.id.nav_privateVotes, R.id.nav_startedVotes, R.id.nav_votedVotes, R.id.nav_login, R.id.nav_signup, R.id.nav_logout)
                .setOpenableLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        // Listener on the items of the nav menu
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_logout) {
                Toast.makeText(getBaseContext(), "Déconnecté", Toast.LENGTH_SHORT).show();
                getSharedPreferences(LOGIN_STRING_KEY, Context.MODE_PRIVATE).edit().putString(TOKEN_STRING_KEY, null).apply();
                getSharedPreferences(LOGIN_STRING_KEY, Context.MODE_PRIVATE).edit().putString(NAME_STRING_KEY, "##erased##").apply();

            } else {
                // This is for closing the drawer after acting on it, doesn't want this feature when menu_logout button is clicked on
                drawerLayout.closeDrawer(GravityCompat.START);

                toolbar.getMenu().setGroupVisible(R.id.app_bar_menu, true);
            }
            if (id == R.id.nav_login)
                toolbar.getMenu().setGroupVisible(R.id.app_bar_menu, false);
            if (id == R.id.nav_signup)
                toolbar.getMenu().setGroupVisible(R.id.app_bar_menu, false);
            // If already on the page that you want to navigate , doesn't reload the page
            if( !(item.isChecked() && id == item.getItemId()) ) {
                // This is for maintaining the behavior of the Navigation view
                NavigationUI.onNavDestinationSelected(item, navController);
            }
            return true;
        });
        // Defines the visibility of the items by default
        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_STRING_KEY, MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN_STRING_KEY, null);

        Menu navMenu = navigationView.getMenu();
        navMenu.findItem(R.id.nav_login).setVisible(token == null);
        navMenu.findItem(R.id.nav_signup).setVisible(token == null);
        navMenu.findItem(R.id.nav_logout).setVisible(token != null);
        navMenu.findItem(R.id.nav_privateVotes).setVisible(token != null);
        navMenu.findItem(R.id.nav_startedVotes).setVisible(token != null);
        navMenu.findItem(R.id.nav_votedVotes).setVisible(token != null);

        // Header
        View headerView = navigationView.getHeaderView(0);
        TextView nameHeader = headerView.findViewById(R.id.name);
        TextView emailHeader = headerView.findViewById(R.id.email);

        if (token != null) {
            String name = sharedPreferences.getString(NAME_STRING_KEY, null);
            String email = sharedPreferences.getString(EMAIL_STRING_KEY, null);
            nameHeader.setText(name);
            emailHeader.setText(email);
        } else {
            nameHeader.setText(R.string.app_name);
            emailHeader.setText(R.string.welcome);
        }


        listener = (prefs, key) -> {
            String content = prefs.getString(key, null);
            // To know when the user switch between login and logout
            switch (key) {
                case "token":
                    // Fetch user infos
                    if (content != null) {
                        getMeRequest();
                    }

                    // Add items if it depends on the user who should be logged in or not
                    navMenu.findItem(R.id.nav_login).setVisible(content == null);
                    navMenu.findItem(R.id.nav_signup).setVisible(content == null);
                    navMenu.findItem(R.id.nav_logout).setVisible(content != null);
                    navMenu.findItem(R.id.nav_privateVotes).setVisible(content != null);
                    navMenu.findItem(R.id.nav_votedVotes).setVisible(content != null);
                    navMenu.findItem(R.id.nav_startedVotes).setVisible(content != null);

                    // When disconnected or connected, navigate to the "home page"
                    navController.navigate(R.id.nav_publicVotes);
                    drawerLayout.openDrawer(GravityCompat.START);
                    toolbar.getMenu().setGroupVisible(R.id.app_bar_menu, true);
                    break;
                case "name":
                    // Header
                    if (content.equals("##erased##")) {
                        nameHeader.setText(R.string.app_name);
                        emailHeader.setText(R.string.welcome);

                    } else {
                        String name = prefs.getString(NAME_STRING_KEY, null);
                        String email = prefs.getString(EMAIL_STRING_KEY, null);
                        nameHeader.setText(name);
                        emailHeader.setText(email);
                    }
                    // If navigation is needed
                    break;
                case "go_to":
                    if ("login page".equals(content)) {
                        navController.navigate(R.id.nav_login);
                        toolbar.getMenu().setGroupVisible(R.id.app_bar_menu, false);
                    }
                    prefs.edit().putString(GO_TO_STRING_KEY, "nowhere").apply();
                    break;
                default:
                    break;
            }
        };
        getSharedPreferences(LOGIN_STRING_KEY, MODE_PRIVATE).registerOnSharedPreferenceChangeListener(listener);

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
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // Search feature in toolbar
        MenuItem searchMenu = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchMenu.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.equals("##code##EMPTY##code##")) //needed because setQuery() is not doing empty query like ""
                    query = "";
                getSharedPreferences(FILTER_STRING_KEY, MODE_PRIVATE).edit().putString(SEARCH_STRING_KEY, query).apply();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            //Clear query
            searchView.setQuery("##code##EMPTY##code##", true);
            // Collapse the action view
            searchView.onActionViewCollapsed();
            //Collapse the search widget
            searchMenu.collapseActionView();
        });
        searchView.setOnSearchClickListener(v -> {
            String searchKey = getSharedPreferences(FILTER_STRING_KEY, MODE_PRIVATE).getString(SEARCH_STRING_KEY, "");
            searchView.setQuery(searchKey, false);
        });

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void getMeRequest() {
        SharedPreferences prefs = getSharedPreferences(LOGIN_STRING_KEY, MODE_PRIVATE);
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
                        prefs.edit().putString(NAME_STRING_KEY, user.getFirstname() + "\t" + user.getName()).apply();
                    } catch (IOException e) { // shouldn't happen
                        e.printStackTrace();
                    }

                }, error -> {
            if (error instanceof AuthFailureError) {
                prefs.edit().putString(GO_TO_STRING_KEY, "login page").apply();
                Toast.makeText(this, "Session expirée", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                String token = getSharedPreferences(LOGIN_STRING_KEY, Context.MODE_PRIVATE).getString(TOKEN_STRING_KEY, null);
                params.put("Accept", "application/json; charset=utf8");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Gets context.
     *
     * @return the context
     */
    public static Context getContext() {
        return AppContext;
    }

    // Defines all sorting by in here
    private List<String> getSortList() {
        List<String> sortList = new ArrayList<>();
        sortList.add(getResources().getString(R.string.none_sort));
        sortList.add(getResources().getString(R.string.name_sort));
        sortList.add(getResources().getString(R.string.voters_sort));
        sortList.add(getResources().getString(R.string.startDate_sort));
        return sortList;
    }
}