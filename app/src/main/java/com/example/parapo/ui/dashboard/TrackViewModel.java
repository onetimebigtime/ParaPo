package com.example.parapo.ui.dashboard;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TrackViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    //private TrackEx ex;
    public String text1;
    public void sample(){
        text1 = "What is fragmen";
        mText.setValue(text1);
    }

    public TrackViewModel() {
        mText = new MutableLiveData<>();
        //String sample = "This is a fragment";
        //ex = new TrackEx(sample);
        sample();
    }

    public LiveData<String> getText() {
        return mText;
    }
}