package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models;

public class KioskPurchaseOption {
    private int id;
    private String name;
    private String description;
    private int creditAmount;
    private String priceDisplay;
    private int entitlementTypeId;

    public KioskPurchaseOption() {}

    public KioskPurchaseOption(int id, String name, String description, int creditAmount,
                               String priceDisplay, int entitlementTypeId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creditAmount = creditAmount;
        this.priceDisplay = priceDisplay;
        this.entitlementTypeId = entitlementTypeId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCreditAmount() { return creditAmount; }
    public void setCreditAmount(int creditAmount) { this.creditAmount = creditAmount; }

    public String getPriceDisplay() { return priceDisplay; }
    public void setPriceDisplay(String priceDisplay) { this.priceDisplay = priceDisplay; }

    public int getEntitlementTypeId() { return entitlementTypeId; }
    public void setEntitlementTypeId(int entitlementTypeId) { this.entitlementTypeId = entitlementTypeId; }
}
