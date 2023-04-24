package com.example.parapo.ui.dashboard;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TrackViewModel extends ViewModel {

    private final MutableLiveData<Double> mLatitude = new MutableLiveData<>();


    private final MutableLiveData<Double> mLongitude = new MutableLiveData<>();

    public void setmLatitude(double latitude) {
        mLatitude.setValue(latitude);
    }

    public void setmLongitude(double longitude) {
        mLongitude.setValue(longitude);
    }

    public LiveData<Double> getmLatitude() {
        return mLatitude;
    }
    public LiveData<Double> getmLongitude() {
        return mLongitude;
    }
}