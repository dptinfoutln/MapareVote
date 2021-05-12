package com.mapare.maparevoteapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapare.maparevoteapp.adapter.CustomAdapter;
import com.mapare.maparevoteapp.adapter.MultipleChoicesAdapter;
import com.mapare.maparevoteapp.adapter.ResultAdapter;
import com.mapare.maparevoteapp.adapter.WeightedChoicesAdapter;
import com.mapare.maparevoteapp.model.entity_to_reveive.Vote;
import com.mapare.maparevoteapp.adapter.UniqueChoiceAdapter;
import com.mapare.maparevoteapp.model.entity_to_reveive.VoteResult;
import com.mapare.maparevoteapp.model.entity_to_send.Ballot;
import com.mapare.maparevoteapp.model.entity_to_send.BallotChoice;
import com.mapare.maparevoteapp.model.entity_to_send.Choice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class VoteActivity extends AppCompatActivity {
    private final List<BallotChoice> pickedChoices = new ArrayList<>();

    private CustomAdapter<?> adapter;
    private ListView listView;

    private MutableLiveData<String> BALLOT_STATE_CODE;
    private MutableLiveData<String> LOADING_STATE_CODE;
    private MutableLiveData<String> RESULT_STATE_CODE;

    private com.mapare.maparevoteapp.model.entity_to_reveive.Ballot ballot;
    private List<VoteResult> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        BALLOT_STATE_CODE = new MutableLiveData<>();
        BALLOT_STATE_CODE.observe(this, s -> {
            Toast.makeText(VoteActivity.this, "Voté", Toast.LENGTH_SHORT).show();
            finish();
        });

        Vote vote = (Vote) getIntent().getSerializableExtra("vote");
        String ballotToken = (String) getIntent().getSerializableExtra("token");


        listView = findViewById(R.id.choice_list);

        TextView labelField = findViewById(R.id.vote_labelField);
        labelField.setText(vote.getLabel());

        TextView infofield = findViewById(R.id.vote_infoField);
        String info = "Créé par " + vote.getVotemaker().getFirstname() +" "+ vote.getVotemaker().getName() + ", ouvert depuis le " + vote.getStartDate().toString();
        infofield.setText(info);

        Button voteButton = findViewById(R.id.voteButton);
        voteButton.setOnClickListener(v -> {
            // if none, prompt something
            if (adapter.getPickedOnes().isEmpty()) {
                Toast.makeText(this, "Vous n'avez pas fait de choix", Toast.LENGTH_SHORT).show();
            } else {
                // fetch the choices picked
                for (int id : adapter.getPickedOnes().keySet()) {
                    pickedChoices.add(new BallotChoice(new Choice(id), adapter.getPickedOnes().get(id)));
                }
                // Makes the request to vote
                voteRequest(vote.getId(), new Ballot(pickedChoices));
            }
        });



        if (ballotToken != null) {
            // deactivate the button, if already voted
            voteButton.setEnabled(false);
            // Wait the "callback" of the request to get the ballot
            LOADING_STATE_CODE = new MutableLiveData<>();
            LOADING_STATE_CODE.observe(this, s -> {
                switch (vote.getAlgo()) {
                    case "majority":
                        if (vote.getMaxChoices() == 1)
                            adapter = new UniqueChoiceAdapter(VoteActivity.this, vote.getChoices(), ballot);
                        else
                            adapter = new MultipleChoicesAdapter(VoteActivity.this, vote.getChoices(), vote.getMaxChoices(), ballot);
                        break;
                    case "borda":
                    case "STV":
                        adapter = new WeightedChoicesAdapter(VoteActivity.this, vote.getChoices(), ballot);
                        break;
                    default:
                        break;
                }
                listView.setAdapter(adapter);

                // Let the user know that he has already voted for this vote
                TextView votedInfo = findViewById(R.id.vote_votedField);
                votedInfo.setVisibility(View.VISIBLE);

                // Results
                if (vote.isIntermediaryResult()) {
                    RESULT_STATE_CODE = new MutableLiveData<>();
                    RESULT_STATE_CODE.observe(this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            // Showing Results feature
                            ExpandableListView resultView = findViewById(R.id.vote_resultView);
                            ResultAdapter resultAdapter = new ResultAdapter(VoteActivity.this, resultList);
                            resultView.setAdapter(resultAdapter);
                        }
                    });
                    getResultRequest(vote.getId());

//                    AtomicBoolean clicked = new AtomicBoolean(false);
//                    resultView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            if (!clicked.get()) {
//                                getResultRequest(vote.getId());
//                            }
//                            clicked.set(!clicked.get());
//                        }
//                    });
                }
            });

            getBallotRequest(vote.getId());
        } else {
            switch (vote.getAlgo()) {
                case "majority":
                    if (vote.getMaxChoices() == 1)
                        adapter = new UniqueChoiceAdapter(VoteActivity.this, vote.getChoices());
                    else
                        adapter = new MultipleChoicesAdapter(VoteActivity.this, vote.getChoices(), vote.getMaxChoices());
                    break;
                case "borda":
                case "STV":
                    adapter = new WeightedChoicesAdapter(VoteActivity.this, vote.getChoices());
                    break;
                default:
                    break;
            }
            listView.setAdapter(adapter);
        }
    }

    private void voteRequest(int id, Ballot ballot) {
        Log.i("ballotenvoyé", ballot.toString());
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

                return json.getBytes();
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getBallotRequest(int id) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.API_URL) + getResources().getString(R.string.VOTE_URL) + id + getResources().getString(R.string.MYBALLOT_URL);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                response -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    try {
                        ballot = objectMapper.readValue(response, com.mapare.maparevoteapp.model.entity_to_reveive.Ballot.class);
                    } catch (IOException e) { // happens because the ballot is null is the vote is anonymous
                    }
                    LOADING_STATE_CODE.setValue("fetching myBallot successful");

                }, error -> {
            // TODO: manage different types of errors
            Log.i("ballot_request", "requête non réussi: " + error.toString());

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                String token = getSharedPreferences("Login", MODE_PRIVATE).getString("token", null);
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getResultRequest(int id) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.API_URL) + getResources().getString(R.string.VOTE_URL) + id + getResources().getString(R.string.RESULT_URL);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                response -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    try {
                        resultList = objectMapper.readValue(response, new TypeReference<List<VoteResult>>() {
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    RESULT_STATE_CODE.setValue("fetching results successful");
                    Log.i("results_response", response);

                }, error -> {
            // TODO: manage different types of errors
            Log.i("result_request", "requête non réussi: " + error.toString());

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                String token = getSharedPreferences("Login", MODE_PRIVATE).getString("token", null);
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}