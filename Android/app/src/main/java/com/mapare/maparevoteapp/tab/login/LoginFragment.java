package com.mapare.maparevoteapp.tab.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mapare.maparevoteapp.R;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;
    private MutableLiveData<String> CONNECTED_STATE_CODE;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CONNECTED_STATE_CODE = new MutableLiveData<>();
        CONNECTED_STATE_CODE.observe(requireActivity(), s -> {
            switch (s) {
                case "Connection successful":
                    Toast.makeText(getContext(), "Connecté", Toast.LENGTH_SHORT).show();
                    // Hides keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                    break;
                case "Server not responding":
                    // Manage ...

                    loginButton.setClickable(true);
                    break;
                case "Wrong inputs":
                    // Manage ...

                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                    passwordField.setError(getResources().getString(R.string.incorrect_password));
                    loginButton.startAnimation(shake);
                    loginButton.setClickable(true);
                    break;
                default:
                    break;
            }
        });

        String savedEmail = this.requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE).getString("email", null);

        emailField = view.findViewById(R.id.login_emailField);
        passwordField = view.findViewById(R.id.login_passwordField);
        loginButton = view.findViewById(R.id.loginButton);

        if (savedEmail != null)
            emailField.setText(savedEmail);

        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            if(email.isEmpty()) {
                emailField.setError(getResources().getString(R.string.empty_email));
            }
            else if (password.isEmpty()) {
                passwordField.setError(getResources().getString(R.string.empty_password));
            }
            else {
                loginButton.setClickable(false);
                // Makes the request
                loginAttempt(getContext(), email, password);
            }
        });
    }

    private void loginAttempt(Context context, String email, String password) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = getResources().getString(R.string.API_URL) + getResources().getString(R.string.LOGIN_URL);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                response -> {
                    // Store BEARER token gotten
                    context.getSharedPreferences("Login", Context.MODE_PRIVATE).edit().putString("token", response).apply();
                    // Store the last email authenticated
                    context.getSharedPreferences("Login", Context.MODE_PRIVATE).edit().putString("email", email).apply();

                    CONNECTED_STATE_CODE.setValue("Connection successful");
                }, error -> CONNECTED_STATE_CODE.setValue("Wrong inputs"))
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Accept", "text/plain");
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString((email + ":" + password).getBytes()));
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}