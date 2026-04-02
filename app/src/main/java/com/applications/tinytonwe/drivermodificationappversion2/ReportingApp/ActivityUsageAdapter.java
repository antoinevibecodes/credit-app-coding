package com.applications.tinytonwe.drivermodificationappversion2.ReportingApp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models.ActivityUsageSummary;

import java.util.List;

public class ActivityUsageAdapter extends RecyclerView.Adapter<ActivityUsageAdapter.ViewHolder> {

    private List<ActivityUsageSummary> activities;

    public ActivityUsageAdapter(List<ActivityUsageSummary> activities) {
        this.activities = activities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.usage_summary_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityUsageSummary item = activities.get(position);
        holder.activityName.setText(item.getButtonName());
        holder.entitlementType.setText(item.getEntitlementTypeName());
        holder.usageCount.setText(item.getUsageCount() + " rides");
        holder.creditsUsed.setText(item.getTotalCreditsUsed() + " credits");
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView activityName, entitlementType, usageCount, creditsUsed;

        ViewHolder(View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.usageActivityName);
            entitlementType = itemView.findViewById(R.id.usageEntitlementType);
            usageCount = itemView.findViewById(R.id.usageCount);
            creditsUsed = itemView.findViewById(R.id.usageCreditsUsed);
        }
    }
}
