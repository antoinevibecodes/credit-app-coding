package com.applications.tinytonwe.drivermodificationappversion2.ReportingApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applications.tinytonwe.drivermodificationappversion2.Common.NfcHelper;
import com.applications.tinytonwe.drivermodificationappversion2.Main.MainActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Mock.MockServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models.TransactionRecord;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.OperationType;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerFactory;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerInterface;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Card history — search by card RFID or driver ID, view transaction history.
 */
public class CardHistoryActivity extends AppCompatActivity implements TaskListener {

    private EditText searchInput;
    private Button searchButton;
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private List<TransactionRecord> transactions = new ArrayList<>();
    private TextView totalCountText, emptyView;
    private NfcHelper nfcHelper;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_history);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Card History");

        searchInput = findViewById(R.id.cardSearchInput);
        searchButton = findViewById(R.id.cardSearchButton);
        recyclerView = findViewById(R.id.transactionList);
        totalCountText = findViewById(R.id.totalCountText);
        emptyView = findViewById(R.id.emptyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter(transactions);
        recyclerView.setAdapter(adapter);

        nfcHelper = new NfcHelper(this);

        searchButton.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();
            if (!query.isEmpty()) {
                searchByCard(query);
            } else {
                Toast.makeText(this, "Enter a card ID or driver ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchByCard(String query) {
        try {
            JSONObject json = new JSONObject();

            // Try to detect if it's a hex RFID or numeric driver ID
            try {
                long driverId = Long.parseLong(query);
                json.put("HasDriverId", true);
                json.put("DriverId", driverId);
                json.put("HasRfidUidL", false);
                json.put("RfidUidL", 0);
            } catch (NumberFormatException e) {
                // It's hex RFID
                long rfidL = NfcHelper.hexToLong(query);
                json.put("HasDriverId", false);
                json.put("DriverId", 0);
                json.put("HasRfidUidL", true);
                json.put("RfidUidL", rfidL);
            }

            json.put("PageNumber", 1);
            json.put("PageSize", 50);

            ServerInterface server = ServerFactory.create(this);
            if (server instanceof MockServer) {
                ((MockServer) server).new GetTransactionHistory(json.toString());
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error building search request", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcHelper.enableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String cardHex = NfcHelper.readCardHex(intent);
        if (cardHex != null) {
            searchInput.setText(cardHex);
            searchByCard(cardHex);
        }
    }

    @Override
    public void onTaskFinished(Response response) {
        if (response.responseOk && response.operationType == OperationType.FETCH_TRANSACTION_HISTORY) {
            try {
                JsonObject obj = JsonParser.parseString(response.jsonData).getAsJsonObject();
                int totalCount = obj.get("TotalCount").getAsInt();
                totalCountText.setText("Total transactions: " + totalCount);

                Type listType = new TypeToken<List<TransactionRecord>>() {}.getType();
                List<TransactionRecord> loaded = gson.fromJson(obj.get("Transactions"), listType);
                transactions.clear();
                transactions.addAll(loaded);
                adapter.notifyDataSetChanged();

                emptyView.setVisibility(transactions.isEmpty() ? TextView.VISIBLE : TextView.GONE);
                recyclerView.setVisibility(transactions.isEmpty() ? RecyclerView.GONE : RecyclerView.VISIBLE);
            } catch (Exception e) {
                Toast.makeText(this, "Error parsing transactions", Toast.LENGTH_SHORT).show();
            }
        } else if (!response.responseOk) {
            Toast.makeText(this, response.responseMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
