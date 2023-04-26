package com.example.parapo.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parapo.R;
import com.example.parapo.databinding.FragmentTripsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripsFragment extends Fragment implements TripsInterface {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    TripsAdapter tripsAdapter;
    ArrayList<TripsUser> list;
    TextView noAvailableLabel;
    ImageView noAvailableView;

    private FragmentTripsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TripsViewModel tripsViewModel =
                new ViewModelProvider(this).get(TripsViewModel.class);

        binding = FragmentTripsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Activity activity = this.requireActivity();

        noAvailableLabel = root.findViewById(R.id.no_trasportation_label);
        noAvailableView = root.findViewById(R.id.no_transpo_view);
        recyclerView = root.findViewById(R.id.trips_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("Drivers");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        list = new ArrayList<>();

        tripsAdapter = new TripsAdapter(activity, list, this);


        recyclerView.setAdapter(tripsAdapter);
        setupDatabaseListener();


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
    private void setupDatabaseListener() {
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
                noAvailableLabel.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
                noAvailableView.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}