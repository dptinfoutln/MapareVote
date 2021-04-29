package com.mapare.maparevoteapp.ui.login;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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

public class LoginFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE).getString("email", null);
        super.onViewCreated(view, savedInstanceState);

        this.requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE).edit().putString("token", null).apply();

        String savedEmail = this.requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE).getString("email", null);

        EditText emailField = view.findViewById(R.id.emailField);
        EditText passwordField = view.findViewById(R.id.passwordField);
        Button loginButton = view.findViewById(R.id.loginButton);

        if (savedEmail != null)
            emailField.setText(savedEmail);

        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            Animation shake = AnimationUtils.loadAnimation(this.getContext(), R.anim.shake);
            if(TextUtils.isEmpty(email)){
                emailField.setError(getResources().getString(R.string.empty_email));
            }
            else if (TextUtils.isEmpty(password)) {
                passwordField.setError(getResources().getString(R.string.empty_password));
            }
            else {
                Toast.makeText(getContext(),"Connexion en cours", Toast.LENGTH_SHORT).show();
                loginAttempt(this.requireContext(), email, password);
                if (this.requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE).getString("token", null) == null)
                    passwordField.setError(getResources().getString(R.string.incorrect_password));

                else {
                    //Return back logged in
                    this.requireActivity().onBackPressed();
                    // hide keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
            }
            loginButton.startAnimation(shake);
        });
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

                        Toast.makeText(context, "Connexion réussie", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: manage errors
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