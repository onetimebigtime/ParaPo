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
import com.example.parapo.R;
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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TravelerViewModel travelerViewModel =
                new ViewModelProvider(this).get(TravelerViewModel.class);

        binding = FragmentTravelerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView fullNameHead = binding.travelersFullnameHead;
        TextView fullNameLabel = binding.travelersFullnameLabel;
        TextView emailLabel = binding.travelersEmailLabel;
        TextView birthdayLabel = binding.travelersBirthdayLabel;
        TextView genderLabel = binding.travelersGenderLabel;
        Button logoutButton = root.findViewById(R.id.logout_button);

        travelerViewModel.getFullNameHead().observe(getViewLifecycleOwner(), fullNameHead::setText);
        travelerViewModel.getFullName().observe(getViewLifecycleOwner(), fullNameLabel::setText);
        travelerViewModel.getEmailAdd().observe(getViewLifecycleOwner(), emailLabel::setText);
        travelerViewModel.getBirthday().observe(getViewLifecycleOwner(), birthdayLabel::setText);
        travelerViewModel.getGender().observe(getViewLifecycleOwner(), genderLabel::setText);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutUser();
            }
        });
        return root;
    }

    private void signOutUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        getActivity().finish();
        startActivity(new Intent(this.requireActivity(), SignInActivity.class));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}