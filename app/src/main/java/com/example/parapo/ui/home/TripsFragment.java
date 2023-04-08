package com.example.parapo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parapo.R;
import com.example.parapo.databinding.FragmentTripsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class TripsFragment extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    TripsAdapter tripsAdapter;
    ArrayList<TripsUser> list;

    private FragmentTripsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TripsViewModel tripsViewModel =
                new ViewModelProvider(this).get(TripsViewModel.class);

        binding = FragmentTripsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        /*
        recyclerView = root.findViewById(R.id.trips_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("Travelers");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.requireActivity()));
        list = new ArrayList<>();
        tripsAdapter = new TripsAdapter(this.requireActivity(), list);
        recyclerView.setAdapter(tripsAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    TripsUser tripsUser = dataSnapshot.getValue(TripsUser.class);
                    list.add(tripsUser);
                }
                tripsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
         */
        /*final TextView textView = binding.textTrip;
        tripsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}