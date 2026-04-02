package com.applications.tinytonwe.drivermodificationappversion2.KioskApp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models.KioskPurchaseOption;

import java.util.List;

public class KioskPurchaseAdapter extends RecyclerView.Adapter<KioskPurchaseAdapter.ViewHolder> {

    private List<KioskPurchaseOption> options;
    private OnPurchaseClickListener listener;

    public interface OnPurchaseClickListener {
        void onPurchaseClicked(KioskPurchaseOption option);
    }

    public KioskPurchaseAdapter(List<KioskPurchaseOption> options, OnPurchaseClickListener listener) {
        this.options = options;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kiosk_purchase_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KioskPurchaseOption option = options.get(position);
        holder.name.setText(option.getName());
        holder.description.setText(option.getDescription());
        holder.price.setText(option.getPriceDisplay());
        holder.credits.setText(option.getCreditAmount() + " credits");

        holder.card.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPurchaseClicked(option);
            }
        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView name, description, price, credits;

        ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.kioskPurchaseCard);
            name = itemView.findViewById(R.id.kioskOptionName);
            description = itemView.findViewById(R.id.kioskOptionDescription);
            price = itemView.findViewById(R.id.kioskOptionPrice);
            credits = itemView.findViewById(R.id.kioskOptionCredits);
        }
    }
}
