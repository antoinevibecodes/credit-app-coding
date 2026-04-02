package com.applications.tinytonwe.drivermodificationappversion2.AdminApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Mock.MockServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.OperationType;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerFactory;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.ServerInterface;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;

import org.json.JSONObject;

/**
 * Create or edit a charge button.
 */
public class EditChargeButtonActivity extends AppCompatActivity implements TaskListener {

    private EditText nameInput, creditAmountInput, sortOrderInput, colorHexInput;
    private Spinner entitlementTypeSpinner;
    private CheckBox forceCheckbox, activeCheckbox;
    private Button saveButton;
    private int editingButtonId = -1;
    private boolean isEditMode = false;

    private String[] entitlementTypeNames = {
            "General", "Food", "TrafficTrack", "TinyTrack", "Train", "Arcade", "MemberAdvantages", "TransferCredits"
    };
    private int[] entitlementTypeIds = {1, 2, 3, 4, 5, 6, 7, 8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_charge_button);

        Toolbar toolbar = findViewById(R.id.tool_bar);

        nameInput = findViewById(R.id.editBtnName);
        creditAmountInput = findViewById(R.id.editBtnCreditAmount);
        sortOrderInput = findViewById(R.id.editBtnSortOrder);
        colorHexInput = findViewById(R.id.editBtnColorHex);
        entitlementTypeSpinner = findViewById(R.id.editBtnEntitlementType);
        forceCheckbox = findViewById(R.id.editBtnForce);
        activeCheckbox = findViewById(R.id.editBtnActive);
        saveButton = findViewById(R.id.editBtnSave);

        // Set up spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, entitlementTypeNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        entitlementTypeSpinner.setAdapter(spinnerAdapter);

        // Check if editing existing button
        Intent intent = getIntent();
        if (intent.hasExtra("buttonId")) {
            isEditMode = true;
            editingButtonId = intent.getIntExtra("buttonId", -1);
            toolbar.setTitle("Edit Button");

            nameInput.setText(intent.getStringExtra("buttonName"));
            creditAmountInput.setText(String.valueOf(intent.getIntExtra("creditAmount", 0)));
            sortOrderInput.setText(String.valueOf(intent.getIntExtra("sortOrder", 0)));
            colorHexInput.setText(intent.getStringExtra("colorHex"));
            forceCheckbox.setChecked(intent.getBooleanExtra("isForce", false));
            activeCheckbox.setChecked(intent.getBooleanExtra("isActive", true));

            // Set spinner to correct entitlement type
            int typeId = intent.getIntExtra("entitlementTypeId", 1);
            for (int i = 0; i < entitlementTypeIds.length; i++) {
                if (entitlementTypeIds[i] == typeId) {
                    entitlementTypeSpinner.setSelection(i);
                    break;
                }
            }
        } else {
            toolbar.setTitle("Create New Button");
            activeCheckbox.setChecked(true);
            colorHexInput.setText("#e3a21a");
        }

        saveButton.setOnClickListener(v -> saveButton());
    }

    private void saveButton() {
        String name = nameInput.getText().toString().trim();
        String creditStr = creditAmountInput.getText().toString().trim();
        String sortStr = sortOrderInput.getText().toString().trim();
        String colorHex = colorHexInput.getText().toString().trim();

        if (name.isEmpty() || creditStr.isEmpty()) {
            Toast.makeText(this, "Name and credit amount are required", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int creditAmount = Integer.parseInt(creditStr);
            int sortOrder = sortStr.isEmpty() ? 0 : Integer.parseInt(sortStr);
            int selectedTypeIndex = entitlementTypeSpinner.getSelectedItemPosition();
            int entitlementTypeId = entitlementTypeIds[selectedTypeIndex];
            boolean isForce = forceCheckbox.isChecked();
            boolean isActive = activeCheckbox.isChecked();

            JSONObject json = new JSONObject();
            if (isEditMode) {
                json.put("Id", editingButtonId);
            }
            json.put("Name", name);
            json.put("EntitlementTypeId", entitlementTypeId);
            json.put("CreditAmount", creditAmount);
            json.put("IsForceOption", isForce);
            json.put("IsActive", isActive);
            json.put("SortOrder", sortOrder);
            json.put("ColorHex", colorHex.isEmpty() ? "#e3a21a" : colorHex);

            int opType = isEditMode ? OperationType.UPDATE_CHARGE_BUTTON : OperationType.CREATE_CHARGE_BUTTON;

            ServerInterface server = ServerFactory.create(this);
            if (server instanceof MockServer) {
                ((MockServer) server).new ManageButton(opType, json.toString());
            }

        } catch (Exception e) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskFinished(Response response) {
        if (response.responseOk) {
            Toast.makeText(this, isEditMode ? "Button updated!" : "Button created!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error: " + response.responseMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
