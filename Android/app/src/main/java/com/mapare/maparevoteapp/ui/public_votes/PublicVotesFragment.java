package com.mapare.maparevoteapp.ui.public_votes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

public class PublicVotesFragment extends Fragment {

    private PublicVotesViewModel publicVotesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        publicVotesViewModel =
                new ViewModelProvider(this).get(PublicVotesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_public_votes, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        publicVotesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        publicVotesRequest(getContext());
    }

    private void publicVotesRequest(Context context) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = getResources().getString(R.string.API_URL) + getResources().getString(R.string.PUBLIC_VOTE_URL);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                response -> {
                    // do things
                    Log.i("debug", response);
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

//                    Type listOfMyClassObject = new TypeToken<ArrayList<Vote>>() {}.getType();
//
//                    Gson gson = new Gson();
//                    List<Vote> voteList = gson.fromJson(response, listOfMyClassObject);
                    List<Vote> voteList = null;
                    try {
                        voteList = objectMapper.readValue(response, new TypeReference<List<Vote>>(){});
                    } catch (IOException e) { // shouldn't happen
                        e.printStackTrace();
                    }

                    assert voteList != null;
                    Log.i("debug", voteList.toString()+"");

                }, error -> {
            // TODO: manage different types of errors
            Log.i("debug", error.toString());

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