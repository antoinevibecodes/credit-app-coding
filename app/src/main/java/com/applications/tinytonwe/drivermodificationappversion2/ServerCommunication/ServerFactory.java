package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication;

import android.app.Activity;

import com.applications.tinytonwe.drivermodificationappversion2.BuildConfig;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Mock.MockServer;

public class ServerFactory {

    public static ServerInterface create(Activity activity) {
        if (BuildConfig.USE_MOCK_SERVER) {
            return new MockServer(activity);
        }
        return new RealServer(activity);
    }
}
