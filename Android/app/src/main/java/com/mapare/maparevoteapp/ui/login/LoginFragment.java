package com.mapare.maparevoteapp.ui.login;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mapare.maparevoteapp.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginFragment extends Fragment {
    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                String state = prefs.getString("state", null);
                Log.i("debug", state+"");
                if (state != null) {
                    loginButton.setClickable(true);
                    if (state.equals("CONNECTED")) {
                        Toast.makeText(getContext(), "Connecté", Toast.LENGTH_SHORT).show();
                        //Return back logged in
                        getActivity().onBackPressed();
                        // hide keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                    }
                    else {
                        Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                        passwordField.setError(getResources().getString(R.string.incorrect_password));
                        loginButton.startAnimation(shake);
                        prefs.edit().putString("state", null).apply();
                    }
                }
            }
        };

        this.requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE).getString("email", null);
        super.onViewCreated(view, savedInstanceState);

        this.requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE).edit().putString("token", null).apply();

        String savedEmail = this.requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE).getString("email", null);

        emailField = view.findViewById(R.id.emailField);
        passwordField = view.findViewById(R.id.passwordField);
        loginButton = view.findViewById(R.id.loginButton);

        if (savedEmail != null)
            emailField.setText(savedEmail);

        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            if(TextUtils.isEmpty(email)){
                emailField.setError(getResources().getString(R.string.empty_email));
            }
            else if (TextUtils.isEmpty(password)) {
                passwordField.setError(getResources().getString(R.string.empty_password));
            }
            else {
                loginButton.setClickable(false);
                loginAttempt(this.requireContext(), email, password);
            }
        });
    }

    @Override
    public void onResume() {
        // TODO: test avec une variable globale ici
        super.onResume();
        getContext().getSharedPreferences("Login", Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(listener);
    }



    private void loginAttempt(Context context, String email, String password) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = context.getString(R.string.API_URL) + requireContext().getString(R.string.LOGIN_URL);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Store BEARER token gotten
                        context.getSharedPreferences("Login", Context.MODE_PRIVATE).edit().putString("token", response).apply();
                        // Store the last email authenticated
                        context.getSharedPreferences("Login", Context.MODE_PRIVATE).edit().putString("email", email).apply();
                        // Store the state (needed to validate or not with a listener)
                        context.getSharedPreferences("Login", Context.MODE_PRIVATE).edit().putString("state", "CONNECTED").apply();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: manage errors
                context.getSharedPreferences("Login", Context.MODE_PRIVATE).edit().putString("state", "ERROR").apply();

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept", "text/plain");
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString((email + ":" + password).getBytes()));
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}