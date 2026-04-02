package com.applications.tinytonwe.drivermodificationappversion2.ChargeCardApp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models.ChargeButtonConfig;

import java.util.List;

public class ChargeButtonAdapter extends RecyclerView.Adapter<ChargeButtonAdapter.ViewHolder> {

    private List<ChargeButtonConfig> buttons;
    private OnChargeButtonClickListener listener;
    private boolean buttonsEnabled = false;

    public interface OnChargeButtonClickListener {
        void onChargeButtonClicked(ChargeButtonConfig button);
    }

    public ChargeButtonAdapter(List<ChargeButtonConfig> buttons, OnChargeButtonClickListener listener) {
        this.buttons = buttons;
        this.listener = listener;
    }

    public void setButtonsEnabled(boolean enabled) {
        this.buttonsEnabled = enabled;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.charge_button_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChargeButtonConfig button = buttons.get(position);

        String label = button.getName() + "\n(" + button.getCreditAmount() + " credits)";
        holder.chargeButton.setText(label);
        holder.chargeButton.setEnabled(buttonsEnabled);

        try {
            holder.chargeButton.setBackgroundColor(Color.parseColor(button.getColorHex()));
        } catch (Exception e) {
            holder.chargeButton.setBackgroundColor(Color.parseColor("#e3a21a"));
        }

        holder.chargeButton.setTextColor(buttonsEnabled ? Color.WHITE : Color.GRAY);

        holder.chargeButton.setOnClickListener(v -> {
            if (listener != null && buttonsEnabled) {
                listener.onChargeButtonClicked(button);
            }
        });
    }

    @Override
    public int getItemCount() {
        return buttons.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Button chargeButton;

        ViewHolder(View itemView) {
            super(itemView);
            chargeButton = itemView.findViewById(R.id.dynamicChargeButton);
        }
    }
}
