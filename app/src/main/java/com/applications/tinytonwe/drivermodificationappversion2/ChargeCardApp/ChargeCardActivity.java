package com.applications.tinytonwe.drivermodificationappversion2.ChargeCardApp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.CountDownTimer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.Common.NfcHelper;
import com.applications.tinytonwe.drivermodificationappversion2.Main.MainActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Mock.MockServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models.ChargeButtonConfig;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.OperationType;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerFactory;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerInterface;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.RealServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChargeCardActivity extends AppCompatActivity implements TaskListener,
        ChargeButtonAdapter.OnChargeButtonClickListener {

    private AppData appData;
    private TextView tv;
    private LinearLayout linearLayout;
    private String cardReadByNFC = "";
    private String prevCardReadByNFC = "";
    private String defaultColor = "#1d1d1d";

    // Dynamic buttons
    private RecyclerView buttonGrid;
    private ChargeButtonAdapter chargeButtonAdapter;
    private List<ChargeButtonConfig> chargeButtons = new ArrayList<>();

    // Fixed buttons
    private Button creditsBn;
    private Button cancelBn;

    private CountDownTimer countDownTimer;
    private NfcHelper nfcHelper;
    private Gson gson = new Gson();
    private boolean buttonsLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_card);

        initialize();
        registerListeners();
        loadDynamicButtons();
    }

    private void initialize() {
        nfcHelper = new NfcHelper(this);
        appData = AppData.getAppDataInstance_();

        linearLayout = (LinearLayout) findViewById(R.id.layout);
        linearLayout.setBackgroundColor(Color.parseColor(defaultColor));
    }

    private void registerListeners() {
        tv = (TextView) findViewById(R.id.textView);

        // Dynamic button grid
        buttonGrid = (RecyclerView) findViewById(R.id.chargeButtonGrid);
        buttonGrid.setLayoutManager(new GridLayoutManager(this, 2));
        chargeButtonAdapter = new ChargeButtonAdapter(chargeButtons, this);
        buttonGrid.setAdapter(chargeButtonAdapter);

        // Fixed buttons
        creditsBn = (Button) findViewById(R.id.credits);
        cancelBn = (Button) findViewById(R.id.cancel);

        creditsBn.setOnClickListener(v -> handleCheckCredits());
        cancelBn.setOnClickListener(v -> resetApp());
    }

    private void loadDynamicButtons() {
        ServerInterface server = ServerFactory.create(this);
        if (server instanceof MockServer) {
            ((MockServer) server).new GetChargeButtons();
        }
        // RealServer: will be wired when backend endpoints are ready
    }

    @Override
    public void onChargeButtonClicked(ChargeButtonConfig button) {
        cancelInactivityTimeout();

        if (!cardReadByNFC.isEmpty()) {
            String rfidUidS = cardReadByNFC.trim();
            long rfidUidL = NfcHelper.hexToLong(rfidUidS);
            appData.setCardIdReadLongValue_(rfidUidL);

            // Use the server to charge
            ServerInterface server = ServerFactory.create(this);
            if (server instanceof MockServer) {
                ((MockServer) server).new ChargeCard(
                        button.getEntitlementTypeId(),
                        button.isForceOption(),
                        false,
                        false
                );
            } else {
                RealServer realServer = (RealServer) server;
                realServer.new ChargeCard(
                        button.getEntitlementTypeId(),
                        button.isForceOption(),
                        false,
                        false
                );
            }

            disableAllButtons();
            tv.setText(getApplicationContext().getResources().getText(R.string.waitMessage).toString());
        }
    }

    private void handleCheckCredits() {
        cancelInactivityTimeout();

        if (!cardReadByNFC.isEmpty()) {
            long rfidUidL = NfcHelper.hexToLong(cardReadByNFC.trim());
            appData.setCardIdReadLongValue_(rfidUidL);

            ServerInterface server = ServerFactory.create(this);
            if (server instanceof MockServer) {
                ((MockServer) server).new ChargeCard(1, false, true, false);
            } else {
                RealServer realServer = (RealServer) server;
                realServer.new ChargeCard(1, false, true, false);
            }

            disableAllButtons();
            tv.setText(getApplicationContext().getResources().getText(R.string.waitMessage).toString());
        }
    }

    public void onResume() {
        super.onResume();
        nfcHelper.enableForegroundDispatch(this);
        resetApp();

        String cardHex = NfcHelper.readCardHex(getIntent());
        if (cardHex != null) {
            processCardRead(cardHex);
        }
    }

    private void processCardRead(String cardHex) {
        cardReadByNFC = cardHex;

        if (prevCardReadByNFC.equals(cardReadByNFC)) {
            linearLayout.setBackgroundColor(Color.parseColor(defaultColor));
            tv.setTextColor(Color.parseColor(defaultColor));
            tv.setBackgroundColor(Color.WHITE);
            resetApp();
            return;
        }

        prevCardReadByNFC = cardReadByNFC;
        setInactivityTimeout();
        tv.setText("Card UID read : " + cardReadByNFC + "\n" + "Please select an option");
        enableAllButtons();
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String cardHex = NfcHelper.readCardHex(intent);
        if (cardHex != null) {
            processCardRead(cardHex);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void setInactivityTimeout() {
        int timeoutDurationInMilli = Integer.parseInt(this.getResources().getText(R.string.inactivityTimeoutDuration).toString());
        int timeoutIntervalInMilli = Integer.parseInt(this.getResources().getText(R.string.timeoutInterval).toString());

        countDownTimer = new CountDownTimer(timeoutDurationInMilli, timeoutIntervalInMilli) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() { resetApp(); }
        }.start();
    }

    private void setSuccessTimeout() {
        int timeoutDurationInMilli = Integer.parseInt(this.getResources().getText(R.string.successTimerDuration).toString());
        int timeoutIntervalInMilli = Integer.parseInt(this.getResources().getText(R.string.successTimerInterval).toString());

        countDownTimer = new CountDownTimer(timeoutDurationInMilli, timeoutIntervalInMilli) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() { resetApp(); }
        }.start();
    }

    private void cancelInactivityTimeout() {
        if (countDownTimer != null) countDownTimer.cancel();
        countDownTimer = null;
    }

    private void disableAllButtons() {
        chargeButtonAdapter.setButtonsEnabled(false);
        creditsBn.setEnabled(false);
        cancelBn.setEnabled(false);
        creditsBn.setTextColor(Color.GRAY);
        cancelBn.setTextColor(Color.GRAY);
    }

    private void enableAllButtons() {
        chargeButtonAdapter.setButtonsEnabled(true);
        creditsBn.setEnabled(true);
        cancelBn.setEnabled(true);
        creditsBn.setTextColor(Color.WHITE);
        cancelBn.setTextColor(Color.WHITE);
    }

    private void DisplayMessage(CCMessages messageType) {
        String promptMessage = getApplicationContext().getResources().getText(R.string.promptMessage).toString();

        switch (messageType) {
            case OK:
                linearLayout.setBackgroundColor(Color.GREEN);
                tv.setTextColor(Color.BLACK);
                tv.setBackgroundColor(Color.GREEN);
                break;
            case ERROR:
                linearLayout.setBackgroundColor(Color.RED);
                tv.setTextColor(Color.BLACK);
                tv.setBackgroundColor(Color.RED);
                cancelBn.setEnabled(true);
                cancelBn.setTextColor(Color.WHITE);
                break;
            case DEFAULT:
                tv.setText(promptMessage);
                linearLayout.setBackgroundColor(Color.parseColor(defaultColor));
                tv.setTextColor(Color.parseColor(defaultColor));
                tv.setBackgroundColor(Color.WHITE);
                break;
        }
    }

    private void resetApp() {
        cardReadByNFC = "";
        disableAllButtons();
        appData.reset();
        DisplayMessage(CCMessages.DEFAULT);
    }

    public void onTaskFinished(Response response) {
        // Handle dynamic button loading
        if (response.operationType == OperationType.FETCH_CHARGE_BUTTONS && response.responseOk) {
            try {
                Type listType = new TypeToken<List<ChargeButtonConfig>>() {}.getType();
                List<ChargeButtonConfig> loaded = gson.fromJson(response.jsonData, listType);
                chargeButtons.clear();
                chargeButtons.addAll(loaded);
                chargeButtonAdapter.notifyDataSetChanged();
                buttonsLoaded = true;
            } catch (Exception e) {
                Toast.makeText(this, "Error loading buttons", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        // Handle charge/credit response (same as original)
        tv.setText(response.responseMessage);
        disableAllButtons();

        if (response.responseOk) {
            DisplayMessage(CCMessages.OK);
            setSuccessTimeout();
        } else {
            DisplayMessage(CCMessages.ERROR);
            cancelBn.setEnabled(true);
            cancelBn.setTextColor(Color.WHITE);
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
