package com.applications.tinytonwe.drivermodificationappversion2.ReportingApp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models.TransactionRecord;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<TransactionRecord> transactions;

    public TransactionAdapter(List<TransactionRecord> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionRecord tx = transactions.get(position);

        holder.buttonName.setText(tx.getButtonName());
        holder.entitlementType.setText(tx.getEntitlementTypeName());
        holder.creditAmount.setText(tx.getCreditAmount() + " credits");
        holder.creditAmount.setTextColor(tx.getCreditAmount() < 0 ? Color.RED : Color.parseColor("#27ae60"));
        holder.timestamp.setText(tx.getTimestamp());

        if (tx.isAnonymous()) {
            holder.driverName.setText("Anonymous / Birthday Card");
            holder.driverName.setTextColor(Color.parseColor("#e67e22"));
        } else {
            holder.driverName.setText(tx.getDriverName());
            holder.driverName.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView buttonName, entitlementType, creditAmount, timestamp, driverName;

        ViewHolder(View itemView) {
            super(itemView);
            buttonName = itemView.findViewById(R.id.txButtonName);
            entitlementType = itemView.findViewById(R.id.txEntitlementType);
            creditAmount = itemView.findViewById(R.id.txCreditAmount);
            timestamp = itemView.findViewById(R.id.txTimestamp);
            driverName = itemView.findViewById(R.id.txDriverName);
        }
    }
}
