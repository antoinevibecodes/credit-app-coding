package com.applications.tinytonwe.drivermodificationappversion2.Main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.applications.tinytonwe.drivermodificationappversion2.AdminApp.AdminActivity;
import com.applications.tinytonwe.drivermodificationappversion2.ChargeCardApp.ChargeCardActivity;
import com.applications.tinytonwe.drivermodificationappversion2.Common.NfcHelper;
import com.applications.tinytonwe.drivermodificationappversion2.DriverInfoApp.DriverInfoActivity;
import com.applications.tinytonwe.drivermodificationappversion2.KioskApp.KioskHomeActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ReportingApp.ReportingMenuActivity;

public class MainActivity extends AppCompatActivity {

    private NfcHelper nfcHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcHelper = new NfcHelper(this);
        registerListener();
    }

    private void registerListener() {

        Toolbar toolbar_ = (Toolbar) findViewById(R.id.tool_bar);
        toolbar_.setTitle("Select Application");

        CardView chargeCard = (CardView) findViewById(R.id.chargeCard);
        CardView driverInfo = (CardView) findViewById(R.id.driverInfo);
        CardView adminPanel = (CardView) findViewById(R.id.adminPanel);
        CardView reports = (CardView) findViewById(R.id.reports);
        CardView kioskMode = (CardView) findViewById(R.id.kioskMode);

        chargeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(MActions.CHARGE_CARD);
            }
        });

        driverInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(MActions.DRIVER_INFO);
            }
        });

        adminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(MActions.ADMIN);
            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(MActions.REPORTS);
            }
        });

        kioskMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(MActions.KIOSK);
            }
        });
    }


    private void buttonHandler(MActions mActions) {
        switch (mActions) {
            case CHARGE_CARD:
                startActivity(new Intent(this, ChargeCardActivity.class));
                break;
            case DRIVER_INFO:
                startActivity(new Intent(this, DriverInfoActivity.class));
                break;
            case ADMIN:
                startActivity(new Intent(this, AdminActivity.class));
                break;
            case REPORTS:
                startActivity(new Intent(this, ReportingMenuActivity.class));
                break;
            case KIOSK:
                startActivity(new Intent(this, KioskHomeActivity.class));
                break;
        }
    }

    public void onResume() {
        super.onResume();
        nfcHelper.enableForegroundDispatch(this);
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
