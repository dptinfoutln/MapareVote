package com.mapare.maparevoteapp;

import android.annotation.SuppressLint;
import android.graphics.BlurMaskFilter;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.MaskFilterSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

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
import com.mapare.maparevoteapp.model.entity_to_receive.Vote;
import com.mapare.maparevoteapp.adapter.UniqueChoiceAdapter;
import com.mapare.maparevoteapp.model.entity_to_receive.VoteResult;
import com.mapare.maparevoteapp.model.entity_to_send.Ballot;
import com.mapare.maparevoteapp.model.entity_to_send.BallotChoice;
import com.mapare.maparevoteapp.model.entity_to_send.Choice;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Vote activity.
 */
public class VoteActivity extends AppCompatActivity {
    private final List<BallotChoice> pickedChoices = new ArrayList<>();
    private CustomAdapter<?> adapter;
    private ListView listView;
    private List<VoteResult> resultList;

    private com.mapare.maparevoteapp.model.entity_to_receive.Ballot ballot;

    private MutableLiveData<String> BALLOT_STATE_CODE;
    private MutableLiveData<String> LOADING_STATE_CODE;
    private MutableLiveData<String> RESULT_STATE_CODE;

    /**
     * The Clicked token.
     */
// Needed for the blur
    boolean clickedToken = false;

    // Needed for resizing the layout when show result is clicked
    private Boolean clicked = false;
    private int height;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        BALLOT_STATE_CODE = new MutableLiveData<>();
        BALLOT_STATE_CODE.observe(this, s -> {
            if (s.equals("vote successful")) {
                Toast.makeText(VoteActivity.this, "Voté", Toast.LENGTH_SHORT).show();
                finish();
            } else if (s.equals("vote unsuccessful")) {
                Toast.makeText(VoteActivity.this, "Valeurs incorrectes", Toast.LENGTH_SHORT).show();
            }
        });

        Vote vote = (Vote) getIntent().getSerializableExtra("vote");
        String ballotToken = (String) getIntent().getSerializableExtra("token");


        listView = findViewById(R.id.choice_list);

        TextView labelField = findViewById(R.id.vote_labelField);
        labelField.setText("   " + vote.getLabel());

        TextView votedInfo = findViewById(R.id.vote_votedField);

        TextView infofield = findViewById(R.id.vote_infoField);

        String dateString = vote.getStartDate().toString().replace("[", "").replace("]", "");
        List<String> dateList = Arrays.asList(dateString.split(","));
        dateString = dateList.get(2) + "/" + dateList.get(1) + "/" + dateList.get(0);
        dateString = dateString.replace(" ", "");

        LocalDate endDate = LocalDate.now(ZoneId.of("GMT")).plusDays(1);

        String info = "Créé par " + vote.getVotemaker().getFirstname() + " " + vote.getVotemaker().getName() + ", ouvert depuis le " + dateString;

        if (vote.getEndDate() != null) {
            String dateString2 = vote.getEndDate().toString().replace("[", "").replace("]", "");
            List<String> dateList2 = Arrays.asList(dateString2.split(","));
            dateString2 = dateList2.get(2) + "/" + dateList2.get(1) + "/" + dateList2.get(0);
            dateString2 = dateString2.replace(" ", "");

            List<String> endDateList = Arrays.asList(dateString2.split("/"));
            endDate = LocalDate.of(Integer.parseInt(endDateList.get(2)), Integer.parseInt(endDateList.get(1)), Integer.parseInt(endDateList.get(0)));

            if (endDate.isBefore(LocalDate.now(ZoneId.of("GMT")).plusDays(1)))
                info += " et a pris fin le ";
            else
                info += " et prend fin le ";
            info += dateString2;
        }

        infofield.setText(info);

        TextView algoField = findViewById(R.id.vote_algoField);
        String algoText = getResources().getString(R.string.algo_text_view) + " " + vote.getAlgo();
        algoField.setText(algoText);

        Button voteButton = findViewById(R.id.voteButton);
        voteButton.setOnClickListener(v -> {
            // if none, prompt something
            if (adapter.getPickedOnes().isEmpty()) {
                Toast.makeText(this, "Vous n'avez pas fait de choix", Toast.LENGTH_SHORT).show();
            } else {
                pickedChoices.clear();
                // fetch the choices picked
                for (int id : adapter.getPickedOnes().keySet()) {  // <-- exception can't happen --'
                    pickedChoices.add(new BallotChoice(new Choice(id), adapter.getPickedOnes().get(id)));
                }
                // Makes the request to vote
                voteRequest(vote.getId(), new Ballot(pickedChoices));
            }
        });

        boolean checkVoteClosed = endDate.isBefore(LocalDate.now(ZoneId.of("GMT")).plusDays(1));
        if (ballotToken != null) {
            // Displays the token if voted
            TextView tokenField = findViewById(R.id.vote_ballotTokenField);
            SpannableString tokenString = new SpannableString(getResources().getString(R.string.ballot_token_text_view) + " " + ballotToken);
            float radius = tokenField.getTextSize() / 3;
            MaskFilterSpan span = new MaskFilterSpan(new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL));
            int tokenSize = getResources().getString(R.string.ballot_token_text_view).length() + 1;
            tokenString.setSpan(span, tokenSize, ballotToken.length() + tokenSize, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tokenField.setText(tokenString);
            tokenField.setVisibility(View.VISIBLE);
            tokenField.setOnClickListener(v -> {
                clickedToken = !clickedToken;
                if (clickedToken) {
                    tokenString.removeSpan(span);
                    tokenField.setText(tokenString);
                } else {
                    tokenString.setSpan(span, tokenSize, ballotToken.length() + tokenSize, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tokenField.setText(tokenString);
                }
            });
            // Wait the "callback" of the request to get the ballot if voted
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

                LinearLayout layout_choice = findViewById(R.id.listView_linearLayout);
                layout_choice.getLayoutParams().height = calculateHeight(listView);
                layout_choice.requestLayout();
            });
            // Fetch ballot
            getBallotRequest(vote.getId());
            // Let the user know that he has already voted for this vote
            votedInfo.setVisibility(View.VISIBLE);
        } else {
            if (checkVoteClosed) {
                switch (vote.getAlgo()) {
                    case "majority":
                        if (vote.getMaxChoices() == 1)
                            adapter = new UniqueChoiceAdapter(VoteActivity.this, vote.getChoices(), null);
                        else
                            adapter = new MultipleChoicesAdapter(VoteActivity.this, vote.getChoices(), vote.getMaxChoices(), null);
                        break;
                    case "borda":
                    case "STV":
                        adapter = new WeightedChoicesAdapter(VoteActivity.this, vote.getChoices(), null);
                        break;
                    default:
                        break;
                }
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
            }
            listView.setAdapter(adapter);

            LinearLayout layout_choice = findViewById(R.id.listView_linearLayout);
            layout_choice.getLayoutParams().height = calculateHeight(listView);
            layout_choice.requestLayout();
        }
        if (ballotToken != null || checkVoteClosed) {
            // deactivate the button, if already voted or vote is closed
            voteButton.setEnabled(false);
            // Show Results
            if (vote.isIntermediaryResult() || checkVoteClosed) {
                RESULT_STATE_CODE = new MutableLiveData<>();
                RESULT_STATE_CODE.observe(this, s1 -> {
                    // Showing Results feature
                    ExpandableListView resultView = findViewById(R.id.vote_resultView);
                    ResultAdapter resultAdapter = new ResultAdapter(VoteActivity.this, resultList);
                    resultView.setAdapter(resultAdapter);

                    LinearLayout layout_result = findViewById(R.id.expandableView_linearLayout);

                    resultView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
                        if (Boolean.FALSE.equals(clicked)) {
                            height = calculateHeight(resultView);
                        } else {
                            height = 0;
                        }
                        layout_result.getLayoutParams().height = height;
                        layout_result.requestLayout();
                        clicked = !clicked;

                        return false;
                    });
                });
                getResultRequest(vote.getId());
            }
            if (checkVoteClosed) {
                // Change the reason why the button is deactivated
                votedInfo.setVisibility(View.VISIBLE);
                votedInfo.setText(R.string.closed_vote);
            }
        }
    }

    private void voteRequest(int id, Ballot ballot) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.API_URL) + getResources().getString(R.string.VOTE_URL) + id + getResources().getString(R.string.BALLOT_URL);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                response -> BALLOT_STATE_CODE.setValue("vote successful"), error -> BALLOT_STATE_CODE.setValue("vote unsuccessful")) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                String token = getSharedPreferences("Login", MODE_PRIVATE).getString("token", null);
                params.put("Accept", "application/json; charset=utf-8");
                params.put("Content-Type", "application/json; charset=utf-8");
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
                        ballot = objectMapper.readValue(response, com.mapare.maparevoteapp.model.entity_to_receive.Ballot.class);
                    } catch (IOException e) { // happens because the ballot is null is the vote is anonymous
                    }
                    LOADING_STATE_CODE.setValue("fetching myBallot successful");

                }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                String token = getSharedPreferences("Login", MODE_PRIVATE).getString("token", null);
                params.put("Accept", "application/json; charset=utf-8");
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

                }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                String token = getSharedPreferences("Login", MODE_PRIVATE).getString("token", null);
                params.put("Accept", "application/json; charset=utf-8");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private int calculateHeight(ListView list) {

        int cHeight = 0;


        if (list instanceof ExpandableListView) {
            for (int j = 0; j < list.getCount(); j++) {
                int childCount = ((ExpandableListView) list).getExpandableListAdapter().getChildrenCount(j);
                for (int i = 0; i < childCount; i++) {
                    View childView = ((ExpandableListView) list).getExpandableListAdapter().getChildView(j, i, false, null, list);
                    childView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    cHeight += childView.getMeasuredHeight();
                }
                //dividers height
                cHeight += list.getDividerHeight() * childCount;
            }
        }


        for (int i = 0; i < list.getCount(); i++) {
            View childView = list.getAdapter().getView(i, null, list);
            childView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            cHeight += childView.getMeasuredHeight();
        }
        //dividers height
        cHeight += list.getDividerHeight() * list.getCount();

        return cHeight;
    }
}