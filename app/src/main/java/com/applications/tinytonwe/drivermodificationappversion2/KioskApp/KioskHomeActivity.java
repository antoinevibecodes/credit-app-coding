package com.applications.tinytonwe.drivermodificationappversion2.KioskApp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.Common.AuthManager;
import com.applications.tinytonwe.drivermodificationappversion2.Common.NfcHelper;
import com.applications.tinytonwe.drivermodificationappversion2.Main.MainActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Mock.MockServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.RealServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerFactory;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerInterface;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;

/**
 * Kiosk home screen — large "Tap Your Card" prompt, full screen immersive.
 * On NFC tap, fetches driver info and navigates to purchase screen.
 */
public class KioskHomeActivity extends AppCompatActivity implements TaskListener {

    private NfcHelper nfcHelper;
    private AppData appData;
    private TextView statusText, tapPrompt;
    private LinearLayout mainLayout;
    private CountDownTimer inactivityTimer;
    private int exitTapCount = 0;
    private long lastExitTapTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Full screen immersive
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_kiosk_home);

        enterImmersiveMode();

        nfcHelper = new NfcHelper(this);
        appData = AppData.getAppDataInstance_();

        statusText = findViewById(R.id.kioskStatusText);
        tapPrompt = findViewById(R.id.kioskTapPrompt);
        mainLayout = findViewById(R.id.kioskMainLayout);

        // Hidden exit: tap logo area 5 times rapidly to exit kiosk
        View exitArea = findViewById(R.id.kioskExitArea);
        exitArea.setOnClickListener(v -> {
            long now = System.currentTimeMillis();
            if (now - lastExitTapTime > 2000) {
                exitTapCount = 0;
            }
            lastExitTapTime = now;
            exitTapCount++;

            if (exitTapCount >= 5) {
                exitTapCount = 0;
                AuthManager auth = new AuthManager(this);
                auth.promptAdminPin(this, success -> {
                    if (success) {
                        finish();
                        startActivity(new Intent(this, MainActivity.class));
                    }
                });
            }
        });

        resetToTapPrompt();
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

    private void resetToTapPrompt() {
        appData.reset();
        tapPrompt.setText("TAP YOUR CARD");
        statusText.setText("Place your card on the device to get started");
        mainLayout.setBackgroundColor(Color.parseColor("#1d1d1d"));
        cancelInactivityTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcHelper.enableForegroundDispatch(this);
        enterImmersiveMode();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String cardHex = NfcHelper.readCardHex(intent);
        if (cardHex != null) {
            long rfidL = NfcHelper.hexToLong(cardHex);
            appData.setCardIdReadLongValue_(rfidL);
            appData.setCardIdReadStringValue_(cardHex);

            tapPrompt.setText("Reading card...");
            statusText.setText("Please wait");
            mainLayout.setBackgroundColor(Color.parseColor("#2c3e50"));

            // Fetch driver info
            ServerInterface server = ServerFactory.create(this);
            if (server instanceof MockServer) {
                ((MockServer) server).new GetDriverInformation(true);
            } else {
                ((RealServer) server).new GetDriverInformation(true);
            }
        }
    }

    @Override
    public void onTaskFinished(Response response) {
        if (response.responseOk) {
            // Navigate to purchase screen
            Intent intent = new Intent(this, KioskPurchaseActivity.class);
            startActivity(intent);
        } else {
            tapPrompt.setText("Card not recognized");
            statusText.setText("Please try again");
            mainLayout.setBackgroundColor(Color.parseColor("#c0392b"));
            startInactivityTimer(5000);
        }
    }

    private void startInactivityTimer(int millis) {
        cancelInactivityTimer();
        inactivityTimer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                resetToTapPrompt();
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
        // In kiosk mode, don't allow back unless admin exits
    }
}
