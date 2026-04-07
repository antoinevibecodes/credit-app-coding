package com.applications.tinytonwe.drivermodificationappversion2.StandardCardsApp;

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
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models.StandardCardConfig;

import java.util.List;

public class StandardCardAdapter extends RecyclerView.Adapter<StandardCardAdapter.ViewHolder> {

    private List<StandardCardConfig> cards;
    private StandardCardsActivity activity;

    public StandardCardAdapter(List<StandardCardConfig> cards, StandardCardsActivity activity) {
        this.cards = cards;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.standard_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StandardCardConfig card = cards.get(position);
        holder.label.setText(card.getLabel());
        holder.details.setText(
                capitalize(card.getCardType()) + " — " + card.getDefaultCredits() + " credits (" + card.getEntitlementTypeName() + ")"
        );
        holder.rfid.setText("RFID: " + card.getRfidUidHex());
        holder.status.setText(card.isActive() ? "Active" : "Inactive");
        holder.status.setTextColor(card.isActive() ? Color.parseColor("#27ae60") : Color.GRAY);

        // Color indicator by type
        String typeColor;
        switch (card.getCardType()) {
            case "birthday": typeColor = "#3498db"; break;
            case "guest": typeColor = "#9b59b6"; break;
            case "event": typeColor = "#95a5a6"; break;
            default: typeColor = "#f39c12"; break;
        }
        holder.typeIndicator.setBackgroundColor(Color.parseColor(typeColor));

        holder.deleteBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(activity)
                    .setTitle("Delete Card")
                    .setMessage("Delete \"" + card.getLabel() + "\"?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        activity.deleteCard(card.getId());
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView label, details, rfid, status;
        View typeIndicator;
        ImageButton deleteBtn;

        ViewHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.stdCardLabel);
            details = itemView.findViewById(R.id.stdCardDetails);
            rfid = itemView.findViewById(R.id.stdCardRfid);
            status = itemView.findViewById(R.id.stdCardStatus);
            typeIndicator = itemView.findViewById(R.id.stdCardTypeIndicator);
            deleteBtn = itemView.findViewById(R.id.stdCardDelete);
        }
    }
}
