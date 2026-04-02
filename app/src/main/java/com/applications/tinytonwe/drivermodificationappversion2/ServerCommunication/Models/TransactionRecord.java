package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models;

public class TransactionRecord {
    private long transactionId;
    private long driverId;
    private String driverName;
    private boolean isAnonymous;
    private String buttonName;
    private String entitlementTypeName;
    private int creditAmount;
    private String timestamp;
    private String transactionType;

    public TransactionRecord() {}

    public long getTransactionId() { return transactionId; }
    public void setTransactionId(long transactionId) { this.transactionId = transactionId; }

    public long getDriverId() { return driverId; }
    public void setDriverId(long driverId) { this.driverId = driverId; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public boolean isAnonymous() { return isAnonymous; }
    public void setAnonymous(boolean anonymous) { isAnonymous = anonymous; }

    public String getButtonName() { return buttonName; }
    public void setButtonName(String buttonName) { this.buttonName = buttonName; }

    public String getEntitlementTypeName() { return entitlementTypeName; }
    public void setEntitlementTypeName(String entitlementTypeName) { this.entitlementTypeName = entitlementTypeName; }

    public int getCreditAmount() { return creditAmount; }
    public void setCreditAmount(int creditAmount) { this.creditAmount = creditAmount; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
}
