package com.applications.tinytonwe.drivermodificationappversion2.ShowDriverInfo;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applications.tinytonwe.drivermodificationappversion2.R;

import java.util.ArrayList;


/**
 * Created by admin on 8/14/2015.
 */
public class RecyclerViewAdapterDrivingHistory extends RecyclerView.Adapter<RecyclerViewAdapterDrivingHistory.ViewHolder>{

    private ArrayList<DrivingSession> drivingHistory_;
    private static RecyclerViewClickListener itemListener_;

    public RecyclerViewAdapterDrivingHistory(ArrayList<DrivingSession> drivingHistory, RecyclerViewClickListener itemListener) {
        drivingHistory_ = drivingHistory;
        itemListener_ = itemListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CardView drivingSession_;

        public TextView drivingMinutes;

        public TextView safetyLevelEarned;

        public TextView violationPointsEarned;

        public TextView startDate;

        public ViewHolder(View v) {
            super(v);

            drivingMinutes = (TextView)v.findViewById(R.id.drivingMinutesVal);
            safetyLevelEarned = (TextView)v.findViewById(R.id.safetyLevelEarned);
            violationPointsEarned = (TextView)v.findViewById(R.id.violationPointsEarned);
            startDate = (TextView)v.findViewById(R.id.startDate);
            drivingSession_ = (CardView)v.findViewById(R.id.drivingSession);
            drivingSession_.setOnClickListener(this);
        }

        public void onClick(View view){
            itemListener_.recyclerViewListClicked(this.getLayoutPosition());
        }

    }

    @Override
    public RecyclerViewAdapterDrivingHistory.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.driving_session_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.drivingMinutes.setText(drivingHistory_.get(position).DrivingMinutes);
        holder.safetyLevelEarned.setText(drivingHistory_.get(position).getSafetyLevelEarned());
        holder.violationPointsEarned.setText(drivingHistory_.get(position).getViolationPointsEarned());
        holder.startDate.setText(drivingHistory_.get(position).getStartDate());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return drivingHistory_.size();
    }

}
