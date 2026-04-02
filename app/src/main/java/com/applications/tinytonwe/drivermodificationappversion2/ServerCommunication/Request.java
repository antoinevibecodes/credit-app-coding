package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication;

/**
 * Created by admin on 6/19/2015.
 */
public class Request {
    public long rfidUidL;
    public String rfidUidS;
    public long DriverId;
    public String url;
    public int connectionTimeoutDuration;
    public int responseTimeoutDuration;
    public byte[] PictureData;
    public boolean useRfid;
    public boolean force;
    public int entitlementType;
    public boolean checkCredits = false;
    public boolean useDriverId = false;
}
