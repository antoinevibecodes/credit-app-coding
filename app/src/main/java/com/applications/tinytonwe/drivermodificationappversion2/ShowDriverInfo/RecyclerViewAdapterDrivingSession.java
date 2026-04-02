package com.applications.tinytonwe.drivermodificationappversion2.ShowDriverInfo;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.applications.tinytonwe.drivermodificationappversion2.R;

import java.util.ArrayList;

/**
 * Created by admin on 8/24/2015.
 */
public class RecyclerViewAdapterDrivingSession extends RecyclerView.Adapter<RecyclerViewAdapterDrivingSession.ViewHolder> {

    private ArrayList<Violation> violations_;
    private Activity activity_;

    public RecyclerViewAdapterDrivingSession(ArrayList<Violation> violations, Activity activity) {
        violations_ = violations;
        activity_ = activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView violationImage;

        public TextView violationDescription;

        public TextView violationFrequency;

        public ViewHolder(View v) {
            super(v);

            violationImage = (ImageView)v.findViewById(R.id.violationImage);
            violationDescription = (TextView)v.findViewById(R.id.violationDescription);
            violationFrequency = (TextView)v.findViewById(R.id.violationFrequency);
        }

    }

    @Override
    public RecyclerViewAdapterDrivingSession.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.driving_session_detail_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.violationImage.setImageDrawable(violations_.get(position).violationImage);
        holder.violationDescription.setText(violations_.get(position).description);
        holder.violationFrequency.setText("X" + violations_.get(position).frequency);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return violations_.size();
    }


}

