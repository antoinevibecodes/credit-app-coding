package com.applications.tinytonwe.drivermodificationappversion2.Common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.InputType;
import android.widget.EditText;

public class AuthManager {

    private static final String PREFS_NAME = "TinyTowneAuth";
    private static final String KEY_IS_ADMIN = "isAdmin";
    private static final String KEY_ADMIN_PIN = "adminPin";
    private static final String DEFAULT_ADMIN_PIN = "1234";

    private Context context;

    public AuthManager(Context context) {
        this.context = context;
    }

    public boolean isAdmin() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_ADMIN, false);
    }

    public void setAdmin(boolean isAdmin) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_IS_ADMIN, isAdmin).apply();
    }

    public String getAdminPin() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_ADMIN_PIN, DEFAULT_ADMIN_PIN);
    }

    public void setAdminPin(String pin) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_ADMIN_PIN, pin).apply();
    }

    public void promptAdminPin(Activity activity, final AuthCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Admin PIN Required");

        final EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        input.setHint("Enter admin PIN");
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String enteredPin = input.getText().toString().trim();
            if (enteredPin.equals(getAdminPin())) {
                setAdmin(true);
                callback.onAuthenticated(true);
            } else {
                callback.onAuthenticated(false);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            callback.onAuthenticated(false);
        });

        builder.show();
    }

    public interface AuthCallback {
        void onAuthenticated(boolean success);
    }
}
