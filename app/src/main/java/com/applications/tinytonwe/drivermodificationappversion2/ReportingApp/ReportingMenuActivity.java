package com.applications.tinytonwe.drivermodificationappversion2.ReportingApp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.applications.tinytonwe.drivermodificationappversion2.Main.MainActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;

/**
 * Menu screen for Reports — lets user choose between Card History and Daily Report.
 */
public class ReportingMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting_menu);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Reports");

        CardView cardHistory = findViewById(R.id.cardHistoryCard);
        CardView dailyReport = findViewById(R.id.dailyReportCard);

        cardHistory.setOnClickListener(v -> {
            startActivity(new Intent(this, CardHistoryActivity.class));
        });

        dailyReport.setOnClickListener(v -> {
            startActivity(new Intent(this, DailyReportActivity.class));
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
