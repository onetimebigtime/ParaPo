package com.example.parapo.ui.home;

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

    Context context;
    ArrayList<TripsUser> list;

    public TripsAdapter(Context context, ArrayList<TripsUser> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listView = LayoutInflater.from(context).inflate(R.layout.trips_item,parent,false);
        return new TripsViewHolder(listView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsViewHolder holder, int position) {
        TripsUser tripsUser = list.get(position);
        holder.fullName.setText(tripsUser.getFull_name());
        holder.birthday.setText(tripsUser.getBirthday());
        holder.gender.setText(tripsUser.getGender());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TripsViewHolder extends RecyclerView.ViewHolder{
        TextView fullName, birthday, gender;

        public TripsViewHolder(@NonNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.trips_fullname);
            birthday = itemView.findViewById(R.id.trips_birthday);
            gender = itemView.findViewById(R.id.trips_gender);
        }
    }
}
