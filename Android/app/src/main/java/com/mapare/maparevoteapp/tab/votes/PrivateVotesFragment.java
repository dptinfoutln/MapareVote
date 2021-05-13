package com.mapare.maparevoteapp.tab.votes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.model.entity_to_reveive.Vote;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrivateVotesFragment extends VotesFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Makes the request
        privateVotesRequest(getContext(), page, page_size);
    }

    private void privateVotesRequest(Context context, int page, int page_size) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = getResources().getString(R.string.API_URL) + getResources().getString(R.string.PRIVATE_VOTE_URL) + "?"
                + getResources().getString(R.string.PAGE_FILTER) + page + "&" + getResources().getString(R.string.PAGE_SIZE_FILTER) + page_size;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                response -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    try {
                        voteList = objectMapper.readValue(response, new TypeReference<List<Vote>>() {
                        });
                        LOADING_STATE_CODE.setValue("fetching all votes successful");
                    } catch (IOException e) { // shouldn't happen
                        e.printStackTrace();
                    }

                    Log.i("votesPrivate_request", voteList.toString());

                }, error -> {
            // TODO: manage different types of errors
            Log.i("votesPrivate_request", "requête non réussi: " + error.toString());
            if (error instanceof AuthFailureError) {
                LOADING_STATE_CODE.setValue("session expired");
            }

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                String token = context.getSharedPreferences("Login", Context.MODE_PRIVATE).getString("token", null);
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}