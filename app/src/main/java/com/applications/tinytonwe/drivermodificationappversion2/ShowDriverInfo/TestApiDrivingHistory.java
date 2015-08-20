package com.applications.tinytonwe.drivermodificationappversion2.ShowDriverInfo;

import com.applications.tinytonwe.drivermodificationappversion2.AppData;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by admin on 8/14/2015.
 */
public class TestApiDrivingHistory {

    public static DrivingSession getSession(){

        DrivingSession drivingSession = new DrivingSession();

        drivingSession.DrivingMinutes = "10";

        drivingSession.InitialSafetyLevel = "106";

        drivingSession.FinalSafetyLevel = "485";

        drivingSession.InitialViolationPoints = "2";

        drivingSession.FinalViolationPoints = "5";

        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
        drivingSession.StartDate = DateTime.now().toString(dtf);

        return drivingSession;
    }

    public static void getDrivingHistory(){
        AppData appData = AppData.getAppDataInstance_();

        for(int loop=0; loop<10; loop++){
            appData.addDrivingSessions(getSession());
        }
    }
}
