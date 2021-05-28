package com.mapare.maparevoteapp.tab.votes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapare.maparevoteapp.MainActivity;
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

    SharedPreferences.OnSharedPreferenceChangeListener listener;

    // Filters
    protected int page = 1;
    protected int page_size;
    protected String search;
    protected String sorting_by;
    protected Boolean open_vote;
    private int totalPages;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_votes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = view.findViewById(R.id.vote_list);
        TextView availability = view.findViewById(R.id.vote_availability);

        LinearLayout btnLayout = view.findViewById(R.id.vote_btnLay);
        Button prev = view.findViewById(R.id.vote_prevButton);
        prev.setOnClickListener(v -> voteRequest(getContext(), --page, page_size, search, open_vote, sorting_by));
        Button next = view.findViewById(R.id.vote_nextButton);
        next.setOnClickListener(v -> voteRequest(getContext(), ++page, page_size, search, open_vote, sorting_by));

        Button first = view.findViewById(R.id.vote_firstButton);
        first.setOnClickListener(v -> voteRequest(getContext(), page=1, page_size, search, open_vote, sorting_by));

        Button last = view.findViewById(R.id.vote_lastButton);
        last.setOnClickListener(v -> voteRequest(getContext(), page=totalPages, page_size, search, open_vote, sorting_by));


        LOADING_STATE_CODE = new MutableLiveData<>();
        LOADING_STATE_CODE.observe(requireActivity(), s -> {
            switch (s) {
                case "fetching all votes successful":
                    if (voteList.isEmpty()) {
                        availability.setVisibility(View.VISIBLE);
                    } else {
                        availability.setVisibility(View.INVISIBLE);
                        // Displays buttons
                        btnLayout.setVisibility(View.VISIBLE);
                    }
                    totalPages = MainActivity.getContext().getSharedPreferences(MainActivity.FILTER_STRING_KEY, Context.MODE_PRIVATE).getInt("total_pages", 1);

                    // Buttons displaying
                    prev.setEnabled(true);
                    next.setEnabled(true);
                    if (page == 1)
                        prev.setEnabled(false);
                    if (page == totalPages)
                        next.setEnabled(false);
                    if (page <= 2)
                        first.setVisibility(View.INVISIBLE);
                    else
                        first.setVisibility(View.VISIBLE);
                    if (page >= totalPages - 1)
                        last.setVisibility(View.INVISIBLE);
                    else
                        last.setVisibility(View.VISIBLE);

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
                    // clean vote & voter, for back button pressed
                    vote = null;
                    voter = null;
                    startActivity(intent);
                    break;
                case "not authorized":
                    Toast.makeText(getContext(), "Veuillez vous connecter", Toast.LENGTH_SHORT).show();
                    // Navigate to login page because access is denied
                    MainActivity.getContext().getSharedPreferences(MainActivity.LOGIN_STRING_KEY, Context.MODE_PRIVATE).edit().putString(MainActivity.GO_TO_STRING_KEY, "login page").apply();
                    break;
                case "session expired":
                    Toast.makeText(getContext(), "Session expirée", Toast.LENGTH_SHORT).show();
                    // Navigate to login page because access is denied
                    MainActivity.getContext().getSharedPreferences(MainActivity.LOGIN_STRING_KEY, Context.MODE_PRIVATE).edit().putString(MainActivity.GO_TO_STRING_KEY, "login page").apply();
                    break;
                default:
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
        // Filters
        page_size = MainActivity.getContext().getSharedPreferences(MainActivity.FILTER_STRING_KEY, Context.MODE_PRIVATE).getInt(MainActivity.PAGE_SIZE_STRING_KEY, 20);
        search =  MainActivity.getContext().getSharedPreferences(MainActivity.FILTER_STRING_KEY, Context.MODE_PRIVATE).getString(MainActivity.SEARCH_STRING_KEY, "");
        open_vote = MainActivity.getContext().getSharedPreferences(MainActivity.FILTER_STRING_KEY, Context.MODE_PRIVATE).getBoolean(MainActivity.OPEN_VOTE_STRING_KEY, false);
        sorting_by = MainActivity.getContext().getSharedPreferences(MainActivity.FILTER_STRING_KEY, Context.MODE_PRIVATE).getString(MainActivity.SORTING_BY_STRING_KEY, requireContext().getResources().getString(R.string.none_sort));

        listener = (prefs, key) -> {
            page = 1;
            switch (key) {
                case "page_size":
                    page_size = prefs.getInt(key, 20);
                    voteRequest(getContext(), page, page_size, search, open_vote, sorting_by);
                    break;
                case "search":
                    search = prefs.getString(key, "");
                    voteRequest(getContext(), page, page_size, search, open_vote, sorting_by);
                    break;
                case "open_vote":
                    open_vote = prefs.getBoolean(key, false);
                    voteRequest(getContext(), page, page_size, search, open_vote, sorting_by);
                    break;
                case "sorting_by":
                    sorting_by = prefs.getString(key, getResources().getString(R.string.none_sort));
                    sorting_by = getSort(sorting_by);
                    voteRequest(getContext(), page, page_size, search, open_vote, sorting_by);
                    break;
                default:
                    break;
            }

        };

        MainActivity.getContext().getSharedPreferences(MainActivity.FILTER_STRING_KEY, Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(listener);

        // Makes the request
        voteRequest(getContext(), page, page_size, search, open_vote, sorting_by);
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
            if (error instanceof AuthFailureError) {
                LOADING_STATE_CODE.setValue("session expired");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                String token = context.getSharedPreferences(MainActivity.LOGIN_STRING_KEY, Context.MODE_PRIVATE).getString(MainActivity.TOKEN_STRING_KEY, null);
                params.put("Accept", "application/json; charset=utf8");
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
            if (error instanceof AuthFailureError) {
                if (context.getSharedPreferences(MainActivity.LOGIN_STRING_KEY, Context.MODE_PRIVATE).getString(MainActivity.TOKEN_STRING_KEY, null) != null)
                    LOADING_STATE_CODE.setValue("session expired");
                else
                    LOADING_STATE_CODE.setValue("not authorized");

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                String token = context.getSharedPreferences(MainActivity.LOGIN_STRING_KEY, Context.MODE_PRIVATE).getString(MainActivity.TOKEN_STRING_KEY, null);
                params.put("Accept", "application/json; charset=utf8");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    protected void voteRequest(Context context, int page, int page_size, String search, Boolean open_vote, String sorting_by) {}

    @Override
    public Context getContext() {
        return MainActivity.getContext();
    }

    private String getSort(String name) {
        if (name.equals(getResources().getString(R.string.none_sort))) {
            name = null;
        } else if (name.equals(getResources().getString(R.string.name_sort))) {
            name = getResources().getString(R.string.NAME_SORT_VALUE);
        } else if (name.equals(getResources().getString(R.string.voters_sort))) {
            name = getResources().getString(R.string.VOTES_SORT_VALUE);
        } else if (name.equals(getResources().getString(R.string.startDate_sort))) {
            name = getResources().getString(R.string.STARTDATE_SORT_VALUE);
        }
        return name;
    }
}