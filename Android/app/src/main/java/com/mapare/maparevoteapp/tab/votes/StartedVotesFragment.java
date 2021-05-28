package com.mapare.maparevoteapp.tab.votes;

import android.content.Context;

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
import java.util.Objects;

public class StartedVotesFragment extends VotesFragment {

    // StartedVotes request
    @Override
    protected void voteRequest(Context context, int page, int page_size, String search, Boolean open_vote, String sorting_by) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = getResources().getString(R.string.API_URL) + getResources().getString(R.string.STARTED_VOTES_URL) + "?"
                + getResources().getString(R.string.PAGE_FILTER) + page + "&" + getResources().getString(R.string.PAGE_SIZE_FILTER) + page_size + "&"
                + getResources().getString(R.string.SEARCH_FILTER) + search + "&" + getResources().getString(R.string.OPEN_VOTE_FILTER) + open_vote + "&"
                + getResources().getString(R.string.SORT_FILTER) + sorting_by;

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

                }, error -> {
            // TODO: manage different types of errors

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
                assert response.headers != null;
                int totalPages = (int) Float.parseFloat(Objects.requireNonNull(response.headers.get("pagecount")));
                totalPages = totalPages == 0 ? 1 : totalPages;
                context.getSharedPreferences("Filter", Context.MODE_PRIVATE).edit().putInt("total_pages", totalPages).apply();
                return super.parseNetworkResponse(response);
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}