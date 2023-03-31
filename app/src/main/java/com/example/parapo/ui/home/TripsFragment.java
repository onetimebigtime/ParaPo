package com.example.parapo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.parapo.databinding.FragmentTripsBinding;

public class TripsFragment extends Fragment {

    private FragmentTripsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TripsViewModel tripsViewModel =
                new ViewModelProvider(this).get(TripsViewModel.class);

        binding = FragmentTripsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTrip;
        tripsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}