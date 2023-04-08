package com.example.parapo.ui.notifications;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.parapo.login.sign_up.TravelersData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class TravelerViewModel extends ViewModel {

    private final MutableLiveData<String> fullNameTitle, fullName, email, birthday, gender;
    private FirebaseUser firebaseUser;
    private TravelersData travelersData;

    public void checkFirebaseUser(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {

        }
        else {
            getUserProfile();
        }
    }

    public void getUserProfile() {
        String userId = firebaseUser.getUid(); //GET THE UNIQUE USER ID IN FIREBASE
        //GET THE NODE TRAVELERS REFERENCE IN FIREBASE
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Travelers");
        userReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                travelersData = snapshot.getValue(TravelersData.class);
                if (travelersData !=null) {
                    fullNameTitle.setValue(travelersData.full_name);
                    fullName.setValue(travelersData.full_name);
                    email.setValue(firebaseUser.getEmail());
                    birthday.setValue(travelersData.birthday);
                    gender.setValue(travelersData.gender);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public TravelerViewModel() {
        fullNameTitle = new MutableLiveData<>();
        fullName = new MutableLiveData<>();
        email = new MutableLiveData<>();
        birthday = new MutableLiveData<>();
        gender = new MutableLiveData<>();

        checkFirebaseUser();
    }

    public LiveData<String> getFullNameHead() {
        return fullNameTitle;
    }
    public LiveData<String> getFullName() {
        return fullName;
    }
    public LiveData<String> getEmailAdd() {
        return email;
    }
    public LiveData<String> getBirthday() {
        return birthday;
    }
    public LiveData<String> getGender() {
        return gender;
    }
}