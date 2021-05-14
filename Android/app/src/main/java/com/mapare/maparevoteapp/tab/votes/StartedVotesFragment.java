package com.mapare.maparevoteapp.tab.votes;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.model.entity_to_receive.Vote;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartedVotesFragment extends VotesFragment {

    // StartedVotes request
    @Override
    protected void voteRequest(Context context, int page, int page_size) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = getResources().getString(R.string.API_URL) + getResources().getString(R.string.MY_VOTES_URL) + "?"
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

                    Log.i("votesStarted_request", voteList.toString());

                }, error -> {
            // TODO: manage different types of errors
            Log.i("votesStarted_request", "requête non réussi: " + error.toString());
            if (error instanceof AuthFailureError) {
                LOADING_STATE_CODE.setValue("session expired");
            }

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                String token = context.getSharedPreferences("Login", Context.MODE_PRIVATE).getString("token", null);
                params.put("Accept", "application/json; charset=utf8");
                params.put("Authorization", "Bearer " + token);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int totalPages = (int) Float.parseFloat(response.headers.get("pagecount"));
                totalPages = totalPages == 0 ? 1 : totalPages;
                context.getSharedPreferences("Filter", Context.MODE_PRIVATE).edit().putInt("total_pages", totalPages).apply();
                Log.i("start_header_totalPages", totalPages+"");
                return super.parseNetworkResponse(response);
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}