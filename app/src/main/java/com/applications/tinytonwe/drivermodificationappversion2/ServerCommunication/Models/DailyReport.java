package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Models;

import java.util.ArrayList;
import java.util.List;

public class DailyReport {
    private String date;
    private int totalTransactions;
    private int totalCreditsUsed;
    private List<ActivityUsageSummary> activities;

    public DailyReport() {
        activities = new ArrayList<>();
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(int totalTransactions) { this.totalTransactions = totalTransactions; }

    public int getTotalCreditsUsed() { return totalCreditsUsed; }
    public void setTotalCreditsUsed(int totalCreditsUsed) { this.totalCreditsUsed = totalCreditsUsed; }

    public List<ActivityUsageSummary> getActivities() { return activities; }
    public void setActivities(List<ActivityUsageSummary> activities) { this.activities = activities; }
}
