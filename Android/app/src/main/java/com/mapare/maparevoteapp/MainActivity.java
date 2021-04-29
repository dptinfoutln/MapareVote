package com.mapare.maparevoteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final String email = "test@mail.com";
    private final String passwd = "mdp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent login_intent = new Intent(this, LoginActivity.class);
//        startActivity(login_intent);
        final TextView textView = (TextView) findViewById(R.id.printRequest);
// ...

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.API_URL) + getString(R.string.LOGIN_URL);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        textView.setText("Response is: "+ response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //manageError(this, error);
                textView.setText("That didn't work!");
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept", "text/plain");
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString((email + ":" + passwd).getBytes()));
                return params;
            }
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}