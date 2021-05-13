package com.mapare.maparevoteapp.tab.votes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.VoteActivity;
import com.mapare.maparevoteapp.adapter.VoteAdapter;
import com.mapare.maparevoteapp.model.entity_to_receive.User;
import com.mapare.maparevoteapp.model.entity_to_receive.Vote;
import com.mapare.maparevoteapp.model.entity_to_receive.VotedVote;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotesFragment extends Fragment {
    protected List<Vote> voteList;
    protected MutableLiveData<String> LOADING_STATE_CODE;
    protected Vote vote;
    protected User voter;

    protected int page;
    protected int page_size;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_votes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = view.findViewById(R.id.vote_list);
        TextView availability = view.findViewById(R.id.vote_availability);

        LOADING_STATE_CODE = new MutableLiveData<>();
        LOADING_STATE_CODE.observe(requireActivity(), s -> {
            switch (s) {
                case "fetching all votes successful":
                    if (voteList.isEmpty())
                        availability.setVisibility(View.VISIBLE);

                    VoteAdapter adapter = new VoteAdapter(getContext(), voteList);
                    listView.setAdapter(adapter);
                    break;
                case "fetching specific vote with voter successful":
                    Intent intent = new Intent(getContext(), VoteActivity.class);
                    intent.putExtra("vote", vote);
                    String ballotToken = null;
                    // if already voted then pass the token of the ballot
                    if (voter.getVotedVotes() != null)
                        for (VotedVote v : voter.getVotedVotes())
                            if (v.getVote().getId() == vote.getId()) {
                                ballotToken = v.getToken();
                                break;
                            }
                    intent.putExtra("token", ballotToken);
                    // clean vote & vote, for back button pressed
                    vote = null;
                    voter = null;
                    startActivity(intent);
                    break;
                case "not authorized":
                    Toast.makeText(getContext(), "Veuillez vous connecter", Toast.LENGTH_SHORT).show();
                    // Navigate to login page because access is denied
                    requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE).edit().putString("go_to", "login page").apply();
                    break;
                case "session expired":
                    Toast.makeText(getContext(), "Session expirée", Toast.LENGTH_SHORT).show();
                    // Navigate to login page because access is denied
                    requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE).edit().putString("go_to", "login page").apply();
                    break;
            }
        });
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // Retrieve the selected vote
            int selectedItemId = (int) parent.getItemIdAtPosition(position);
            // Makes the request for fecthing the user informations
            getMeRequest(getContext());
            // Makes the request for fecthing the vote informations
            getVoteRequest(getContext(), selectedItemId);


        });
        // TODO: pagination
        page = 1;
        page_size = 200;
    }

    private void getVoteRequest(Context context, int id) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = getResources().getString(R.string.API_URL) + getResources().getString(R.string.VOTE_URL) + id;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                response -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    try {
                        vote = objectMapper.readValue(response, Vote.class);
                        LOADING_STATE_CODE.setValue("fetching specific vote successful");
                        if (voter != null)
                            LOADING_STATE_CODE.setValue("fetching specific vote with voter successful");
                    } catch (IOException e) { // shouldn't happen
                        e.printStackTrace();
                    }

                }, error -> {
            // TODO: manage different types of errors
            Log.i("vote_request", "requête non réussi: " + error.toString());
            if (error instanceof AuthFailureError) {
                LOADING_STATE_CODE.setValue("session expired");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                String token = context.getSharedPreferences("Login", Context.MODE_PRIVATE).getString("token", null);
                Log.i("token", token + "");
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getMeRequest(Context context) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = getResources().getString(R.string.API_URL) + getResources().getString(R.string.ME_URL);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                response -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    try {
                        voter = objectMapper.readValue(response, User.class);
                        if (vote != null)
                            LOADING_STATE_CODE.setValue("fetching specific vote with voter successful");

                    } catch (IOException e) { // shouldn't happen
                        e.printStackTrace();
                    }

                }, error -> {
            // TODO: manage different types of errors
            Log.i("voter_request", "requête non réussi: " + error.toString());
            if (error instanceof AuthFailureError) {
                if (context.getSharedPreferences("Login", Context.MODE_PRIVATE).getString("token", null) != null)
                    LOADING_STATE_CODE.setValue("session expired");
                else
                    LOADING_STATE_CODE.setValue("not authorized");

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