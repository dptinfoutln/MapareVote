package com.mapare.maparevoteapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapare.maparevoteapp.adapter.CustomAdapter;
import com.mapare.maparevoteapp.model.entity_to_reveive.Vote;
import com.mapare.maparevoteapp.adapter.UniqueChoiceAdaptater;
import com.mapare.maparevoteapp.model.entity_to_send.Ballot;
import com.mapare.maparevoteapp.model.entity_to_send.BallotChoice;
import com.mapare.maparevoteapp.model.entity_to_send.Choice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteActivity extends AppCompatActivity {
    private TextView labelField;
    private TextView infofield;
    private Button voteButton;
    private Button backButton;
    private ListView listView;
    private List<BallotChoice> pickedChoices = new ArrayList<>();

    private CustomAdapter<?> adapter;

    private MutableLiveData<String> BALLOT_STATE_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BALLOT_STATE_CODE = new MutableLiveData<>();
        BALLOT_STATE_CODE.observe(this, s -> {
            Toast.makeText(VoteActivity.this, "Voté", Toast.LENGTH_SHORT).show();
            finish();
        });

        Vote vote = (Vote) getIntent().getSerializableExtra("vote");

        switch (vote.getAlgo()) {
            case "majority":
                if (vote.getMaxChoices() == 1) {
                    setContentView(R.layout.activity_vote_majority_unique);
                    listView = findViewById(R.id.choice_list);

                    Log.i("debug", vote.getChoices().toString());
                    adapter = new UniqueChoiceAdaptater(this, vote.getChoices());
                    listView.setAdapter(adapter);

                } else {
                    // lancer l'autre layout
                }
                break;
            default:
                // lancer layout classique
                setContentView(R.layout.activity_vote);
                break;
        }

        // Common fields across the layouts
        labelField = findViewById(R.id.vote_labelField);
        labelField.setText(vote.getLabel());

        infofield = findViewById(R.id.vote_infoField);
        String info = "Créé par " + vote.getVotemaker().getFirstname() +" "+ vote.getVotemaker().getName() + ", ouvert depuis le " + vote.getStartDate().toString();
        infofield.setText(info);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish();
        });

        voteButton = findViewById(R.id.voteButton);
        voteButton.setOnClickListener(v -> {
            // if none, prompt something
            if (adapter.getPickedOnes().isEmpty()) {
                Toast.makeText(this, "Vous n'avez pas fait de choix", Toast.LENGTH_SHORT).show();
            }else {
                // fetch the choices picked
                pickedChoices.add(new BallotChoice(new Choice((int) adapter.getPickedOnes().get(0)), 1));
                // Makes the request to vote
                voteRequest(vote.getId(), new Ballot(pickedChoices));
            }
        });
    }

    private void voteRequest(int id, Ballot ballot) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.API_URL) + getResources().getString(R.string.VOTE_URL) + id + getResources().getString(R.string.BALLOT_URL);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                response -> {
                    BALLOT_STATE_CODE.setValue("vote successful");
                    Log.i("ballot_request", "requête réussi: " + response);
                }, error -> {
            // TODO: manage different types of errors
            Log.i("ballot_request", "requête non réussi: " + error);
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();

                String token = getSharedPreferences("Login", MODE_PRIVATE).getString("token", null);
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }

            @Override
            public byte[] getBody() {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                String json = null;
                try {
                    json = objectMapper.writeValueAsString(ballot);
                } catch (JsonProcessingException e) { // shouldn't happen
                    e.printStackTrace();
                }

                assert json != null;

                Log.i("ballot", json);

                return json.getBytes();
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}