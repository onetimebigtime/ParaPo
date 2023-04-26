package com.example.parapo;

import android.os.Bundle;
import android.widget.Toast;

import com.example.parapo.ui.home.TripsUser;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.parapo.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        BottomNavigationView navView = findViewById(R.id.nav_view);
        BadgeDrawable badgeDrawable = navView.getOrCreateBadge(R.id.navigation_trips);
        getDriversCount(badgeDrawable);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_trips, R.id.navigation_track, R.id.navigation_traveler)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
    public NavController getNavController() {
        return navController;
    }
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    public void getDriversCount (BadgeDrawable badgeDrawable) {
        List<TripsUser> list = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Drivers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int onlineCount = 0;
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    TripsUser userData = dataSnapshot.getValue(TripsUser.class);
                    if (userData != null && userData.isIs_online()) {
                        list.add(userData);
                        onlineCount++;
                    }
                }
                if (onlineCount == 0) {
                    badgeDrawable.setVisible(false);
                }
                else {
                    badgeDrawable.setVisible(true);
                    badgeDrawable.setNumber(onlineCount);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to get the drivers count!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}