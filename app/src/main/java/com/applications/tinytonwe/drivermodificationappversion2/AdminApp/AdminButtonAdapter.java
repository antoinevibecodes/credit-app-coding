package com.applications.tinytonwe.drivermodificationappversion2.AdminApp;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models.ChargeButtonConfig;

import java.util.List;

public class AdminButtonAdapter extends RecyclerView.Adapter<AdminButtonAdapter.ViewHolder> {

    private List<ChargeButtonConfig> buttons;
    private AdminActivity activity;

    public AdminButtonAdapter(List<ChargeButtonConfig> buttons, AdminActivity activity) {
        this.buttons = buttons;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_button_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChargeButtonConfig button = buttons.get(position);
        holder.buttonName.setText(button.getName());
        holder.buttonDetails.setText(
                button.getEntitlementTypeName() + " — " + button.getCreditAmount() + " credits"
                + (button.isForceOption() ? " (Force)" : "")
        );

        try {
            holder.colorIndicator.setBackgroundColor(Color.parseColor(button.getColorHex()));
        } catch (Exception e) {
            holder.colorIndicator.setBackgroundColor(Color.GRAY);
        }

        holder.statusText.setText(button.isActive() ? "Active" : "Inactive");
        holder.statusText.setTextColor(button.isActive() ? Color.parseColor("#27ae60") : Color.GRAY);

        // Edit button
        holder.editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(activity, EditChargeButtonActivity.class);
            intent.putExtra("buttonId", button.getId());
            intent.putExtra("buttonName", button.getName());
            intent.putExtra("entitlementTypeId", button.getEntitlementTypeId());
            intent.putExtra("creditAmount", button.getCreditAmount());
            intent.putExtra("isForce", button.isForceOption());
            intent.putExtra("isActive", button.isActive());
            intent.putExtra("sortOrder", button.getSortOrder());
            intent.putExtra("colorHex", button.getColorHex());
            activity.startActivity(intent);
        });

        // Delete button
        holder.deleteBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(activity)
                    .setTitle("Delete Button")
                    .setMessage("Are you sure you want to delete \"" + button.getName() + "\"?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        activity.deleteButton(button.getId());
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return buttons.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView buttonName, buttonDetails, statusText;
        View colorIndicator;
        ImageButton editBtn, deleteBtn;

        ViewHolder(View itemView) {
            super(itemView);
            buttonName = itemView.findViewById(R.id.adminBtnName);
            buttonDetails = itemView.findViewById(R.id.adminBtnDetails);
            statusText = itemView.findViewById(R.id.adminBtnStatus);
            colorIndicator = itemView.findViewById(R.id.adminBtnColorIndicator);
            editBtn = itemView.findViewById(R.id.adminBtnEdit);
            deleteBtn = itemView.findViewById(R.id.adminBtnDelete);
        }
    }
}
