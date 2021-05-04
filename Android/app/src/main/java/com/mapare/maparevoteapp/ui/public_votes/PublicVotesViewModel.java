package com.mapare.maparevoteapp.ui.public_votes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PublicVotesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PublicVotesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}