package com.example.parapo.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.parapo.MainActivity;
import com.example.parapo.SignInActivity;
import com.example.parapo.databinding.FragmentTravelerBinding;
import com.example.parapo.login.sign_up.SignUpFragment;
import com.example.parapo.login.sign_up.TravelersData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TravelerFragment extends Fragment {
    private FragmentTravelerBinding binding;
    private TextView fullNameHead, fullNameLabel, emailLabel, birthdayLabel, genderLabel;
    private String fullNameTitle, fullName, email, birthday, gender;
    private FirebaseUser firebaseUser;
    private TravelersData travelersData;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TravelerViewModel travelerViewModel =
                new ViewModelProvider(this).get(TravelerViewModel.class);

        binding = FragmentTravelerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        fullNameHead = binding.travelersFullnameHead;
        fullNameLabel = binding.travelersFullnameLabel;
        emailLabel = binding.travelersEmailLabel;
        birthdayLabel = binding.travelersBirthdayLabel;
        genderLabel = binding.travelersGenderLabel;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(this.requireActivity(), "Something is wrong", Toast.LENGTH_SHORT).show();
        }
        else {
            getUserProfile();
        }

        Button logOutButton = binding.logoutButton;
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(root.getContext(), SignInActivity.class));//END SIGNUP ACTIVITY
            }
        });

        /*final TextView textView = binding.textTraveler;
        travelerViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return root;
    }

    private void getUserProfile() {
        String userId = firebaseUser.getUid();
        DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("Travelers");
        referenceUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                travelersData = snapshot.getValue(TravelersData.class);
                if (travelersData != null) {
                    fullNameTitle = travelersData.full_name;
                    fullName = travelersData.full_name;
                    email = firebaseUser.getEmail();
                    birthday = travelersData.birthdate;
                    gender = travelersData.gender;

                    fullNameHead.setText(fullNameTitle);
                    fullNameLabel.setText(fullName);
                    emailLabel.setText(email);
                    birthdayLabel.setText(birthday);
                    genderLabel.setText(gender);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}