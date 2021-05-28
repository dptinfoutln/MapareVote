package com.mapare.maparevoteapp.tab.register;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;


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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.model.entity_to_send.User;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {
    private EditText nameField;
    private EditText firstnameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private Button registerButton;
    private MutableLiveData<String> REGISTERED_STATE_CODE;

    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        REGISTERED_STATE_CODE = new MutableLiveData<>();
        REGISTERED_STATE_CODE.observe(requireActivity(), s -> {
            InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            switch (s) {
                case "Registration successful":

                    Toast.makeText(getContext(), "Création du compte réussie", Toast.LENGTH_SHORT).show();

                    // Hides keyboard
                    inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

                    SharedPreferences prefs = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
                    // Save email for the next prompting
                    prefs.edit().putString("email", email).apply();
                    // Navigate to the login page right after a successful registration
                    prefs.edit().putString("go_to", "login page").apply();
                    break;
                case "Server not responding":
                    // Manage ...

                    break;
                case "Wrong inputs":
                    // Manage ...

                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                    emailField.setError(getResources().getString(R.string.incorrect_email));
                    registerButton.startAnimation(shake);

                    // Hides keyboard
                    inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                    break;
                default:
                    break;
            }
            registerButton.setClickable(true);
        });

        nameField = view.findViewById(R.id.register_nameField);
        firstnameField = view.findViewById(R.id.register_firstnameField);
        emailField = view.findViewById(R.id.register_emailField);
        passwordField = view.findViewById(R.id.register_passwordField);
        confirmPasswordField = view.findViewById(R.id.register_confirmPasswordField);
        registerButton = view.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> {
            String name = nameField.getText().toString();
            String firstname = firstnameField.getText().toString();
            email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            String confirmPassword = confirmPasswordField.getText().toString();

            if (name.isEmpty()) {
                nameField.setError(getResources().getString(R.string.empty_firstname));
            }
            else if (firstname.isEmpty()) {
                firstnameField.setError(getResources().getString(R.string.empty_firstname));
            }
            else if (email.isEmpty()) {
                emailField.setError(getResources().getString(R.string.empty_email));
            }
            else if (password.isEmpty()) {
                passwordField.setError(getResources().getString(R.string.empty_password));
            }
            else if (confirmPassword.isEmpty()) {
                confirmPasswordField.setError(getResources().getString(R.string.empty_password));
            }
            else if (!confirmPassword.equals(password)) {
                confirmPasswordField.setError(getResources().getString(R.string.incorrect_password));
            }
            else {
                User userToRegister = new User(email, name, firstname, password);
                registerButton.setClickable(false);
                // Makes the request
                registerAttempt(getContext(), userToRegister);
            }
        });
    }

    private void registerAttempt(Context context, User user) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = getResources().getString(R.string.API_URL) + getResources().getString(R.string.SIGNUP_URL);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                response -> REGISTERED_STATE_CODE.setValue("Registration successful"), error -> REGISTERED_STATE_CODE.setValue("Wrong inputs"))
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Accept", "application/json; charset=utf-8");
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }

            @Override
            public byte[] getBody() {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                String json = null;
                try {
                    json = objectMapper.writeValueAsString(user);
                } catch (JsonProcessingException e) { // shouldn't happen
                    e.printStackTrace();
                }

                assert json != null;

                return json.getBytes();
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
