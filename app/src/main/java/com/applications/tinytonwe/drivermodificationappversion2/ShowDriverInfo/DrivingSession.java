package com.applications.tinytonwe.drivermodificationappversion2.ShowDriverInfo;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 8/14/2015.
 */
public class DrivingSession {

    public String DrivingMinutes;

    public String InitialSafetyLevel;

    public String FinalSafetyLevel;

    public String InitialViolationPoints;

    public String FinalViolationPoints;

    public String StartDate;

    public String getSafetyLevelEarned(){
        return Double.toString(Double.parseDouble(FinalSafetyLevel) - Double.parseDouble(InitialSafetyLevel));
    }

    public String getViolationPointsEarned(){
        return Double.toString(Double.parseDouble(FinalViolationPoints) - Double.parseDouble(InitialViolationPoints));
    }


    public String getStartDate(){

        try {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
            DateTime dt = dtf.parseDateTime(StartDate);
            Date date = dt.toDate();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            return simpleDateFormat.format(date);
        }
        catch (Exception e){
            e.printStackTrace();
            return "Error Parsing Date";
        }
    }
    //violations

    public String v1;

    public String v2;

    public String v3;

    public String v4;

    public String v5;

    public String v6;

    public String v7;

    public String v8;

    public String v9;

    public String v10;

    public String v11;

    public String v12;

    public String v13;

    public String v14;

    public String v15;

    public String v16;

    public String v17;

    public String v18;

    public String w1;

    public String w2;

    public String w3;

    public String w4;

    public String w5;

    public String w6;

    public String w7;

    public String w8;

    public String w9;

    public String w10;

    public String w11;

    public String w12;

    public String w13;

    public String w14;

    public String w15;

    public String w16;

    public String w17;

    public String w18;

}
