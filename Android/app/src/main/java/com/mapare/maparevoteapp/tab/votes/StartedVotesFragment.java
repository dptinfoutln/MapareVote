package com.mapare.maparevoteapp.tab.votes;

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

public class StartedVotesFragment extends VotesFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Makes the request
        startedVotesRequest(page, page_size);
    }

    private void startedVotesRequest(int page, int page_size) {
        //TODO: wait that the request exists
    }
}