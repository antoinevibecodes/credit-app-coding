package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models;

public class ActivityUsageSummary {
    private String buttonName;
    private String entitlementTypeName;
    private int usageCount;
    private int totalCreditsUsed;

    public ActivityUsageSummary() {}

    public ActivityUsageSummary(String buttonName, String entitlementTypeName, int usageCount, int totalCreditsUsed) {
        this.buttonName = buttonName;
        this.entitlementTypeName = entitlementTypeName;
        this.usageCount = usageCount;
        this.totalCreditsUsed = totalCreditsUsed;
    }

    public String getButtonName() { return buttonName; }
    public void setButtonName(String buttonName) { this.buttonName = buttonName; }

    public String getEntitlementTypeName() { return entitlementTypeName; }
    public void setEntitlementTypeName(String entitlementTypeName) { this.entitlementTypeName = entitlementTypeName; }

    public int getUsageCount() { return usageCount; }
    public void setUsageCount(int usageCount) { this.usageCount = usageCount; }

    public int getTotalCreditsUsed() { return totalCreditsUsed; }
    public void setTotalCreditsUsed(int totalCreditsUsed) { this.totalCreditsUsed = totalCreditsUsed; }
}
