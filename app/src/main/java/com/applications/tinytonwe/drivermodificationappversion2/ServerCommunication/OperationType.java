package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication;

public class OperationType {
    public static final int FETCH_CHARGE_BUTTONS = 100;
    public static final int CREATE_CHARGE_BUTTON = 101;
    public static final int UPDATE_CHARGE_BUTTON = 102;
    public static final int DELETE_CHARGE_BUTTON = 103;
    public static final int FETCH_ENTITLEMENT_TYPES = 104;

    public static final int FETCH_TRANSACTION_HISTORY = 200;
    public static final int FETCH_DAILY_REPORT = 201;

    public static final int FETCH_KIOSK_OPTIONS = 300;
    public static final int PROCESS_KIOSK_PURCHASE = 301;

    public static final int VALIDATE_PIN = 400;
}
