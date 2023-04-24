package com.example.parapo.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parapo.R;

import java.util.ArrayList;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripsViewHolder> {
    private final TripsInterface tripsInterface;
    Context context;
    ArrayList<TripsUser> list;

    public TripsAdapter(Context context, ArrayList<TripsUser> list,
                        TripsInterface tripsInterface) {
        this.context = context;
        this.list = list;
        this.tripsInterface = tripsInterface;
    }

    @NonNull
    @Override
    public TripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listView = LayoutInflater.from(context).inflate(R.layout.trips_item,parent,false);
        return new TripsViewHolder(listView, tripsInterface);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TripsViewHolder holder, int position) {
        TripsUser tripsUser = list.get(position);
        holder.route.setText(tripsUser.getRoute());
        holder.plateNumber.setText(tripsUser.getPlate_number());
        holder.seatCount.setText(String.valueOf(tripsUser.getSeatCount())+"/"+"10");
        holder.location.setText(String.valueOf(tripsUser.getLatitude()) +", "+ String.valueOf(tripsUser.getLongitude()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TripsViewHolder extends RecyclerView.ViewHolder{
        TextView route, plateNumber, seatCount, location;

        public TripsViewHolder(@NonNull View itemView, TripsInterface tripsInterface) {
            super(itemView);
            route = itemView.findViewById(R.id.trips_route);
            plateNumber = itemView.findViewById(R.id.trips_plate_number);
            seatCount = itemView.findViewById(R.id.trips_seat_count);
            location = itemView.findViewById(R.id.trips_location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tripsInterface != null) {
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            tripsInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
