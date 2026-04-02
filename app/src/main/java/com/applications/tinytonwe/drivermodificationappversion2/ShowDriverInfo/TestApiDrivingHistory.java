package com.applications.tinytonwe.drivermodificationappversion2.ShowDriverInfo;

import com.applications.tinytonwe.drivermodificationappversion2.AppData;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

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


        drivingSession.v1 = Integer.toString((int)Math.floor(Math.random()*2));

        drivingSession.v2 = Integer.toString((int)Math.floor(Math.random()*3));;

        drivingSession.v3 = Integer.toString((int)Math.floor(Math.random()*2));;

        drivingSession.v4 = Integer.toString((int)Math.floor(Math.random()*3));;

        drivingSession.v5 = Integer.toString((int)Math.floor(Math.random()*3));;

        drivingSession.v6 = Integer.toString((int)Math.floor(Math.random()*5));;

        drivingSession.v7 = Integer.toString((int)Math.floor(Math.random()*3));;

        drivingSession.v8 = Integer.toString((int)Math.floor(Math.random()*2));;

        drivingSession.v9 = Integer.toString((int)Math.floor(Math.random()*2));;

        drivingSession.v10 = Integer.toString((int)Math.floor(Math.random()*3));;

        drivingSession.v11 = Integer.toString((int)Math.floor(Math.random()*5));;

        drivingSession.v12 = Integer.toString((int)Math.floor(Math.random()*3));;

        drivingSession.v13 = Integer.toString((int)Math.floor(Math.random()*3));;

        drivingSession.v14 = Integer.toString((int)Math.floor(Math.random()*4));;

        drivingSession.v15 = Integer.toString((int)Math.floor(Math.random()*3));;

        drivingSession.v16 = Integer.toString((int)Math.floor(Math.random()*2));;

        drivingSession.v17 = Integer.toString((int)Math.floor(Math.random()*3));;

        drivingSession.v18 = Integer.toString((int)Math.floor(Math.random()*2));;

        return drivingSession;
    }

    public static void getDrivingHistory(){
        AppData appData = AppData.getAppDataInstance_();
        appData.resetDrivingHistory();

        for(int loop=0; loop<5; loop++){
            appData.addDrivingSessions(getSession());
        }
    }
}
