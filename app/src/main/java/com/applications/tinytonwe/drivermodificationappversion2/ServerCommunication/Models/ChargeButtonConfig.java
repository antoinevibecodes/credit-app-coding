package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models;

public class ChargeButtonConfig {
    private int id;
    private String name;
    private int entitlementTypeId;
    private String entitlementTypeName;
    private int creditAmount;
    private boolean isForceOption;
    private boolean isActive;
    private int sortOrder;
    private String colorHex;

    public ChargeButtonConfig() {}

    public ChargeButtonConfig(int id, String name, int entitlementTypeId, String entitlementTypeName,
                              int creditAmount, boolean isForceOption, boolean isActive,
                              int sortOrder, String colorHex) {
        this.id = id;
        this.name = name;
        this.entitlementTypeId = entitlementTypeId;
        this.entitlementTypeName = entitlementTypeName;
        this.creditAmount = creditAmount;
        this.isForceOption = isForceOption;
        this.isActive = isActive;
        this.sortOrder = sortOrder;
        this.colorHex = colorHex;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getEntitlementTypeId() { return entitlementTypeId; }
    public void setEntitlementTypeId(int entitlementTypeId) { this.entitlementTypeId = entitlementTypeId; }

    public String getEntitlementTypeName() { return entitlementTypeName; }
    public void setEntitlementTypeName(String entitlementTypeName) { this.entitlementTypeName = entitlementTypeName; }

    public int getCreditAmount() { return creditAmount; }
    public void setCreditAmount(int creditAmount) { this.creditAmount = creditAmount; }

    public boolean isForceOption() { return isForceOption; }
    public void setForceOption(boolean forceOption) { isForceOption = forceOption; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }

    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }
}
