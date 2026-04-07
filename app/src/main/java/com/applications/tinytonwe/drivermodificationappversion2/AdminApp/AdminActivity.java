package com.applications.tinytonwe.drivermodificationappversion2.AdminApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applications.tinytonwe.drivermodificationappversion2.Common.AuthManager;
import com.applications.tinytonwe.drivermodificationappversion2.Main.MainActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models.ChargeButtonConfig;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.OperationType;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerFactory;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerInterface;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.RealServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Mock.MockServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin panel — manage dynamic charge buttons.
 * Protected by admin PIN on entry.
 */
public class AdminActivity extends AppCompatActivity implements TaskListener {

    private RecyclerView recyclerView;
    private AdminButtonAdapter adapter;
    private List<ChargeButtonConfig> buttons = new ArrayList<>();
    private TextView emptyView;
    private AuthManager authManager;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Admin Panel — Charge Buttons");

        recyclerView = findViewById(R.id.adminButtonList);
        emptyView = findViewById(R.id.emptyView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdminButtonAdapter(buttons, this);
        recyclerView.setAdapter(adapter);

        // FAB to add new button
        findViewById(R.id.fabAddButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, EditChargeButtonActivity.class);
            startActivity(intent);
        });

        authManager = new AuthManager(this);

        // Require admin PIN
        if (!authManager.isAdmin()) {
            authManager.promptAdminPin(this, success -> {
                if (success) {
                    loadButtons();
                } else {
                    Toast.makeText(this, "Invalid PIN", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {
            loadButtons();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (authManager.isAdmin()) {
            loadButtons();
        }
    }

    private void loadButtons() {
        ServerInterface server = ServerFactory.create(this);
        if (server instanceof MockServer) {
            ((MockServer) server).new GetChargeButtons();
        } else {
            ((RealServer) server).new GetChargeButtons();
        }
    }

    public void deleteButton(int buttonId) {
        ServerInterface server = ServerFactory.create(this);
        String json = "{\"Id\":" + buttonId + "}";
        if (server instanceof MockServer) {
            ((MockServer) server).new ManageButton(OperationType.DELETE_CHARGE_BUTTON, json);
        } else {
            ((RealServer) server).new ManageButton(OperationType.DELETE_CHARGE_BUTTON, json);
        }
    }

    @Override
    public void onTaskFinished(Response response) {
        if (response.responseOk && response.operationType == OperationType.FETCH_CHARGE_BUTTONS) {
            try {
                Type listType = new TypeToken<List<ChargeButtonConfig>>() {}.getType();
                List<ChargeButtonConfig> loadedButtons = gson.fromJson(response.jsonData, listType);
                buttons.clear();
                buttons.addAll(loadedButtons);
                adapter.notifyDataSetChanged();
                emptyView.setVisibility(buttons.isEmpty() ? TextView.VISIBLE : TextView.GONE);
                recyclerView.setVisibility(buttons.isEmpty() ? RecyclerView.GONE : RecyclerView.VISIBLE);
            } catch (Exception e) {
                Toast.makeText(this, "Error parsing buttons", Toast.LENGTH_SHORT).show();
            }
        } else if (response.operationType == OperationType.DELETE_CHARGE_BUTTON) {
            if (response.responseOk) {
                Toast.makeText(this, "Button deleted", Toast.LENGTH_SHORT).show();
                loadButtons();
            } else {
                Toast.makeText(this, "Failed to delete: " + response.responseMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
