package com.applications.tinytonwe.drivermodificationappversion2.KioskApp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Mock.MockServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.RealServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models.KioskPurchaseOption;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.OperationType;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerFactory;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerInterface;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Kiosk purchase screen — shows available credit packages for self-checkout.
 */
public class KioskPurchaseActivity extends AppCompatActivity implements TaskListener {

    private AppData appData;
    private TextView welcomeText, balanceText, statusText;
    private RecyclerView recyclerView;
    private KioskPurchaseAdapter adapter;
    private List<KioskPurchaseOption> options = new ArrayList<>();
    private LinearLayout mainLayout;
    private CountDownTimer inactivityTimer;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_kiosk_purchase);
        enterImmersiveMode();

        appData = AppData.getAppDataInstance_();

        welcomeText = findViewById(R.id.kioskWelcomeText);
        balanceText = findViewById(R.id.kioskBalanceText);
        statusText = findViewById(R.id.kioskPurchaseStatus);
        recyclerView = findViewById(R.id.kioskPurchaseList);
        mainLayout = findViewById(R.id.kioskPurchaseLayout);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new KioskPurchaseAdapter(options, this::onPurchaseSelected);
        recyclerView.setAdapter(adapter);

        // Show driver info
        String name = appData.getDriverFirstName();
        if (name != null && !name.isEmpty()) {
            welcomeText.setText("Welcome, " + name + "!");
        } else {
            welcomeText.setText("Welcome, Guest!");
        }
        balanceText.setText("Current balance: " + (appData.getTotalCredits() != null ? appData.getTotalCredits() : "0") + " credits");

        // Load purchase options
        loadOptions();
        startInactivityTimer(30000);
    }

    private void enterImmersiveMode() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    private void loadOptions() {
        ServerInterface server = ServerFactory.create(this);
        if (server instanceof MockServer) {
            ((MockServer) server).new GetKioskOptions();
        } else {
            ((RealServer) server).new GetKioskOptions();
        }
    }

    private void onPurchaseSelected(KioskPurchaseOption option) {
        cancelInactivityTimer();

        new AlertDialog.Builder(this)
                .setTitle("Confirm Purchase")
                .setMessage("Buy " + option.getName() + " for " + option.getPriceDisplay() + "?\n\n"
                        + option.getDescription() + "\n"
                        + option.getCreditAmount() + " credits will be added.")
                .setPositiveButton("Buy", (dialog, which) -> {
                    processPurchase(option);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    startInactivityTimer(30000);
                })
                .show();
    }

    private void processPurchase(KioskPurchaseOption option) {
        try {
            JSONObject json = new JSONObject();
            json.put("RfidUidL", appData.getCardIdReadLongValue_());
            json.put("PurchaseOptionId", option.getId());
            json.put("PaymentMethod", "card");

            ServerInterface server = ServerFactory.create(this);
            if (server instanceof MockServer) {
                ((MockServer) server).new PurchaseKiosk(json.toString());
            } else {
                ((RealServer) server).new PurchaseKiosk(json.toString());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error processing purchase", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskFinished(Response response) {
        if (response.operationType == OperationType.FETCH_KIOSK_OPTIONS && response.responseOk) {
            try {
                Type listType = new TypeToken<List<KioskPurchaseOption>>() {}.getType();
                List<KioskPurchaseOption> loaded = gson.fromJson(response.jsonData, listType);
                options.clear();
                options.addAll(loaded);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                Toast.makeText(this, "Error loading options", Toast.LENGTH_SHORT).show();
            }
        } else if (response.operationType == OperationType.PROCESS_KIOSK_PURCHASE) {
            if (response.responseOk) {
                mainLayout.setBackgroundColor(Color.parseColor("#27ae60"));
                statusText.setVisibility(View.VISIBLE);
                statusText.setText("Purchase Successful!\n" + response.responseMessage);
                recyclerView.setVisibility(View.GONE);

                // Auto return to kiosk home after 5 seconds
                startInactivityTimer(5000);
            } else {
                mainLayout.setBackgroundColor(Color.parseColor("#c0392b"));
                statusText.setVisibility(View.VISIBLE);
                statusText.setText("Purchase Failed\n" + response.responseMessage);
                startInactivityTimer(5000);
            }
        }
    }

    private void startInactivityTimer(int millis) {
        cancelInactivityTimer();
        inactivityTimer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                finish(); // Go back to KioskHomeActivity
            }
        }.start();
    }

    private void cancelInactivityTimer() {
        if (inactivityTimer != null) {
            inactivityTimer.cancel();
            inactivityTimer = null;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
