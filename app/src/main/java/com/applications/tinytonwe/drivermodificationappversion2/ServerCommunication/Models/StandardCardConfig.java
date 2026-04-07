package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models;

public class StandardCardConfig {
    private int id;
    private String rfidUidHex;
    private String label;
    private String cardType; // "birthday", "guest", "event"
    private int defaultCredits;
    private int entitlementTypeId;
    private String entitlementTypeName;
    private boolean isActive;

    public StandardCardConfig() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRfidUidHex() { return rfidUidHex; }
    public void setRfidUidHex(String rfidUidHex) { this.rfidUidHex = rfidUidHex; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }

    public int getDefaultCredits() { return defaultCredits; }
    public void setDefaultCredits(int defaultCredits) { this.defaultCredits = defaultCredits; }

    public int getEntitlementTypeId() { return entitlementTypeId; }
    public void setEntitlementTypeId(int entitlementTypeId) { this.entitlementTypeId = entitlementTypeId; }

    public String getEntitlementTypeName() { return entitlementTypeName; }
    public void setEntitlementTypeName(String entitlementTypeName) { this.entitlementTypeName = entitlementTypeName; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
