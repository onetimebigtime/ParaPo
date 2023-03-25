package com.example.parapo.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TravelerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TravelerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Traveler fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}