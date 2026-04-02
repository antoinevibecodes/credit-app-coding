package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models;

public class EntitlementCredit {
    private int entitlementTypeId;
    private String entitlementTypeName;
    private String credits;

    public EntitlementCredit() {}

    public EntitlementCredit(int entitlementTypeId, String entitlementTypeName, String credits) {
        this.entitlementTypeId = entitlementTypeId;
        this.entitlementTypeName = entitlementTypeName;
        this.credits = credits;
    }

    public int getEntitlementTypeId() { return entitlementTypeId; }
    public void setEntitlementTypeId(int entitlementTypeId) { this.entitlementTypeId = entitlementTypeId; }

    public String getEntitlementTypeName() { return entitlementTypeName; }
    public void setEntitlementTypeName(String entitlementTypeName) { this.entitlementTypeName = entitlementTypeName; }

    public String getCredits() { return credits; }
    public void setCredits(String credits) { this.credits = credits; }
}
