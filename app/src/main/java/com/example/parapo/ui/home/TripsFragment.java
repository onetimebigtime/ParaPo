package com.example.parapo.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parapo.MainActivity;
import com.example.parapo.R;
import com.example.parapo.databinding.FragmentTripsBinding;
import com.example.parapo.ui.dashboard.TrackFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class TripsFragment extends Fragment implements TripsInterface {
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

        recyclerView = root.findViewById(R.id.trips_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("Drivers");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.requireActivity()));
        list = new ArrayList<>();
        tripsAdapter = new TripsAdapter(this.requireActivity(), list, this);
        recyclerView.setAdapter(tripsAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    TripsUser tripsUser = dataSnapshot.getValue(TripsUser.class);
                    assert tripsUser != null;
                    boolean isOnline = tripsUser.isIs_online();
                    if (isOnline){
                        list.add(tripsUser);
                    }
                }
                tripsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
            }
        });

        /*final TextView textView = binding.textTrip;
        tripsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return root;
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putDouble("Latitude", list.get(position).getLatitude());
        bundle.putDouble("Longitude", list.get(position).getLongitude());
        getParentFragmentManager().setFragmentResult("requestKey", bundle);

        NavHostFragment.findNavController(this).navigate(R.id.navigation_track);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}