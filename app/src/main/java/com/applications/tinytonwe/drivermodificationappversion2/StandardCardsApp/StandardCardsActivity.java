package com.applications.tinytonwe.drivermodificationappversion2.StandardCardsApp;

import android.graphics.Color;
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
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models.StandardCardConfig;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.OperationType;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.RealServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerFactory;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerInterface;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StandardCardsActivity extends AppCompatActivity implements TaskListener {

    private RecyclerView recyclerView;
    private StandardCardAdapter adapter;
    private List<StandardCardConfig> allCards = new ArrayList<>();
    private List<StandardCardConfig> filteredCards = new ArrayList<>();
    private TextView emptyView;
    private Gson gson = new Gson();
    private String currentFilter = "all";

    private Button filterAll, filterBirthday, filterGuest, filterEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_cards);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Standard Cards");

        recyclerView = findViewById(R.id.standardCardList);
        emptyView = findViewById(R.id.emptyView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new StandardCardAdapter(filteredCards, this);
        recyclerView.setAdapter(adapter);

        // Filter buttons
        filterAll = findViewById(R.id.filterAll);
        filterBirthday = findViewById(R.id.filterBirthday);
        filterGuest = findViewById(R.id.filterGuest);
        filterEvent = findViewById(R.id.filterEvent);

        filterAll.setOnClickListener(v -> applyFilter("all"));
        filterBirthday.setOnClickListener(v -> applyFilter("birthday"));
        filterGuest.setOnClickListener(v -> applyFilter("guest"));
        filterEvent.setOnClickListener(v -> applyFilter("event"));

        // FAB to add card
        findViewById(R.id.fabAddCard).setOnClickListener(v -> {
            Toast.makeText(this, "Add card — use web dashboard for full form", Toast.LENGTH_SHORT).show();
        });

        loadCards();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCards();
    }

    private void loadCards() {
        ServerInterface server = ServerFactory.create(this);
        if (server instanceof MockServer) {
            ((MockServer) server).new GetStandardCards();
        } else {
            ((RealServer) server).new GetStandardCards();
        }
    }

    public void deleteCard(int cardId) {
        ServerInterface server = ServerFactory.create(this);
        String json = "{\"Id\":" + cardId + "}";
        if (server instanceof MockServer) {
            ((MockServer) server).new ManageStdCard(OperationType.DELETE_STANDARD_CARD, json);
        } else {
            ((RealServer) server).new ManageStdCard(OperationType.DELETE_STANDARD_CARD, json);
        }
    }

    private void applyFilter(String filter) {
        currentFilter = filter;

        // Update button styles
        Button[] buttons = {filterAll, filterBirthday, filterGuest, filterEvent};
        String[] types = {"all", "birthday", "guest", "event"};
        for (int i = 0; i < buttons.length; i++) {
            if (types[i].equals(filter)) {
                buttons[i].setBackgroundColor(Color.parseColor("#4a90d9"));
                buttons[i].setTextColor(Color.WHITE);
            } else {
                buttons[i].setBackgroundColor(Color.parseColor("#e0e0e0"));
                buttons[i].setTextColor(Color.parseColor("#333333"));
            }
        }

        filterAndDisplay();
    }

    private void filterAndDisplay() {
        filteredCards.clear();
        for (StandardCardConfig card : allCards) {
            if ("all".equals(currentFilter) || card.getCardType().equals(currentFilter)) {
                filteredCards.add(card);
            }
        }
        adapter.notifyDataSetChanged();

        emptyView.setVisibility(filteredCards.isEmpty() ? TextView.VISIBLE : TextView.GONE);
        recyclerView.setVisibility(filteredCards.isEmpty() ? RecyclerView.GONE : RecyclerView.VISIBLE);
    }

    @Override
    public void onTaskFinished(Response response) {
        if (response.responseOk && response.operationType == OperationType.FETCH_STANDARD_CARDS) {
            try {
                Type listType = new TypeToken<List<StandardCardConfig>>() {}.getType();
                List<StandardCardConfig> loaded = gson.fromJson(response.jsonData, listType);
                allCards.clear();
                allCards.addAll(loaded);
                filterAndDisplay();
            } catch (Exception e) {
                Toast.makeText(this, "Error parsing cards", Toast.LENGTH_SHORT).show();
            }
        } else if (response.operationType == OperationType.DELETE_STANDARD_CARD) {
            if (response.responseOk) {
                Toast.makeText(this, "Card deleted", Toast.LENGTH_SHORT).show();
                loadCards();
            } else {
                Toast.makeText(this, "Failed to delete: " + response.responseMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
