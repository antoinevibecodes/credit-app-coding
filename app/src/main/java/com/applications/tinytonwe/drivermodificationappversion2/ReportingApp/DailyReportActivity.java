package com.applications.tinytonwe.drivermodificationappversion2.ReportingApp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Mock.MockServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.RealServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models.ActivityUsageSummary;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.OperationType;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerFactory;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerInterface;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Daily usage report — pick a date, see activity breakdown.
 */
public class DailyReportActivity extends AppCompatActivity implements TaskListener {

    private Button datePickerButton;
    private TextView selectedDateText, totalTransactionsText, totalCreditsText;
    private RecyclerView recyclerView;
    private ActivityUsageAdapter adapter;
    private List<ActivityUsageSummary> activities = new ArrayList<>();
    private Gson gson = new Gson();
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Daily Report");

        datePickerButton = findViewById(R.id.datePickerButton);
        selectedDateText = findViewById(R.id.selectedDateText);
        totalTransactionsText = findViewById(R.id.totalTransactionsText);
        totalCreditsText = findViewById(R.id.totalCreditsText);
        recyclerView = findViewById(R.id.usageList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ActivityUsageAdapter(activities);
        recyclerView.setAdapter(adapter);

        // Default to today
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        selectedDate = sdf.format(today.getTime());
        selectedDateText.setText("Date: " + selectedDate);

        datePickerButton.setOnClickListener(v -> showDatePicker());

        // Load today's report
        loadReport(selectedDate);
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    selectedDateText.setText("Date: " + selectedDate);
                    loadReport(selectedDate);
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void loadReport(String date) {
        ServerInterface server = ServerFactory.create(this);
        if (server instanceof MockServer) {
            ((MockServer) server).new GetDailyReport(date);
        } else {
            ((RealServer) server).new GetDailyReport(date);
        }
    }

    @Override
    public void onTaskFinished(Response response) {
        if (response.responseOk && response.operationType == OperationType.FETCH_DAILY_REPORT) {
            try {
                JsonObject obj = JsonParser.parseString(response.jsonData).getAsJsonObject();
                int totalTx = obj.get("TotalTransactions").getAsInt();
                int totalCredits = obj.get("TotalCreditsUsed").getAsInt();

                totalTransactionsText.setText("Total Transactions: " + totalTx);
                totalCreditsText.setText("Total Credits Used: " + totalCredits);

                Type listType = new TypeToken<List<ActivityUsageSummary>>() {}.getType();
                List<ActivityUsageSummary> loaded = gson.fromJson(obj.get("Activities"), listType);
                activities.clear();
                activities.addAll(loaded);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                Toast.makeText(this, "Error parsing report", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
