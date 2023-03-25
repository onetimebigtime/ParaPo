package com.example.parapo.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.parapo.databinding.FragmentTravelerBinding;

public class TravelerFragment extends Fragment {

    private FragmentTravelerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TravelerViewModel travelerViewModel =
                new ViewModelProvider(this).get(TravelerViewModel.class);

        binding = FragmentTravelerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textTraveler;
        travelerViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}