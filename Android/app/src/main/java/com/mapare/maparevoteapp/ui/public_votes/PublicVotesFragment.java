package com.mapare.maparevoteapp.ui.public_votes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapare.maparevoteapp.R;
import com.mapare.maparevoteapp.VoteActivity;
import com.mapare.maparevoteapp.adapter.VoteAdapter;
import com.mapare.maparevoteapp.model.entity_to_reveive.Vote;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicVotesFragment extends Fragment {
    private List<Vote> voteList;
    private MutableLiveData<String> LOADING_STATE_CODE;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_public_votes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = view.findViewById(R.id.vote_list);

        LOADING_STATE_CODE = new MutableLiveData<>();
        LOADING_STATE_CODE.observe(requireActivity(), s -> {
            VoteAdapter adapter = new VoteAdapter(getContext(), voteList);
            listView.setAdapter(adapter);
        });
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // Retrieve the selected vote
            int selectedItemId = (int) parent.getItemIdAtPosition(position);
            // TODO: check token
            // Start VoteActivity with passing the vote info
            Intent intent = new Intent(getContext(), VoteActivity.class);
            intent.putExtra("voteId", selectedItemId);
            startActivity(intent);
        });
        // Makes the request
        publicVotesRequest(getContext());
    }

    private void publicVotesRequest(Context context) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = getResources().getString(R.string.API_URL) + getResources().getString(R.string.PUBLIC_VOTE_URL);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                response -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    try {
                        voteList = objectMapper.readValue(response, new TypeReference<List<Vote>>(){});
                        LOADING_STATE_CODE.setValue("Finished");
                    } catch (IOException e) { // shouldn't happen
                        e.printStackTrace();
                    }

                    Log.i("votesPublic_request", voteList.toString());

                }, error -> {
            // TODO: manage different types of errors
            Log.i("votesPublic_request", "erreur_public_votes " + error.toString());

        })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Accept", "application/json");
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}