package com.applications.tinytonwe.drivermodificationappversion2;

import android.graphics.Bitmap;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Created by admin on 6/24/2015.
 */
public class AppData {

    public static AppData appDataInstance_;

    private Bitmap croppedImage_ = null;
    private Bitmap originalImage_ = null;
    private long cardIdReadLongValue_ = 0;
    private String cardIdReadStringValue_ = "";
    private String driverFirstName_ = null;
    private String driverLastName_ = null;
    private String dob_ = null;
    private Bitmap driverImage_ = null;

    private String totalCredits_ = null;
    private String generalCredits_ = null;
    private String foodCredits_ = null;
    private String trafficTrackCredits_ = null;
    private String tinyTrackCredits_ = null;
    private String trainCredits_ = null;
    private String arcadeCredits_ = null;


    private long driverId_ = 0;

    public static AppData getAppDataInstance_(){
        if(appDataInstance_ == null){
            appDataInstance_ = new AppData();
        }
        return appDataInstance_;
    }

    public void setDriverId(long driverId){
        driverId_ = driverId;
    }

    public long getDriverId_(){
        return driverId_;
    }

    public void setCroppedImage_(Bitmap croppedImage_){
        this.croppedImage_ = croppedImage_;
    }

    public void setOriginalImage_(Bitmap originalImage_){
        this.originalImage_ = originalImage_;
    }

    public Bitmap getCroppedImage_(){
        return croppedImage_;
    }

    public Bitmap getOriginalImage_(){
        return originalImage_;
    }

    public void setCardIdReadLongValue_(long cardIdReadLongValue_){
        this.cardIdReadLongValue_ = cardIdReadLongValue_;
    }

    public void setCardIdReadStringValue_(String cardIdReadStringValue_){
        this.cardIdReadStringValue_ = cardIdReadStringValue_;
    }

    public long getCardIdReadLongValue_(){
        return this.cardIdReadLongValue_;
    }

    public String getCardIdReadStringValue_(){
        return this.cardIdReadStringValue_;
    }

    public byte[] getPictureData(){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        croppedImage_ = Bitmap.createScaledBitmap(croppedImage_,400,400,false);
        croppedImage_.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void setDriverFirstName(String driverFirstName){
            driverFirstName_ = driverFirstName;
    }

    public void setDriverLastName(String driverLastName){
        driverLastName_ = driverLastName;
    }

    public void setDriverDob(String dob){

        try {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
            DateTime dt = dtf.parseDateTime(dob);
            java.util.Date date = dt.toDate();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String newDate = simpleDateFormat.format(date);

            dob_ = newDate;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setDriverImage(Bitmap driverImage){
        driverImage_ = driverImage;
    }

    public Bitmap getDriverImage(){
        return driverImage_;
    }

    public String getDriverFirstName(){
        return driverFirstName_;
    }

    public String getDriverLastName(){
        return driverLastName_;
    }

    public String getDob(){
        return dob_;
    }

    public void setTotalCredits(String totalCredits){
        totalCredits_ = totalCredits;
    }

    public void setGeneralCredits(String generalCredits){
        generalCredits_ = generalCredits;
    }

    public void setFoodCredits(String foodCredits){
        foodCredits_ = foodCredits;
    }

    public void setTrafficTrackCredits(String trafficTrackCredits){
        trafficTrackCredits_ = trafficTrackCredits;
    }

    public void setTinyTrackCredits(String tinyTrackCredits){
        tinyTrackCredits_ = tinyTrackCredits;
    }

    public void setTrainCredits(String trainCredits){
        trainCredits_ = trainCredits;
    }

    public void setArcadeCredits(String arcadeCredits){
        arcadeCredits_ = arcadeCredits;
    }

    public String getTotalCredits(){
        return totalCredits_;
    }

    public String getGeneralCredits(){
        return generalCredits_;
    }

    public String getFoodCredits(){
        return foodCredits_;
    }

    public String getTrafficTrackCredits(){
        return trafficTrackCredits_;
    }

    public String getTinyTrackCredits(){
        return tinyTrackCredits_;
    }

    public String getTrainCredits(){
        return trainCredits_;
    }

    public String getArcadeCredits(){
        return arcadeCredits_;
    }


    public void reset(){
        croppedImage_ = null;
        originalImage_ = null;
        cardIdReadLongValue_ = 0;
        cardIdReadStringValue_ = "";
        driverFirstName_ = null;
        driverLastName_ = null;
        dob_ = null;
        driverImage_ = null;
        driverId_ = 0;

        totalCredits_ = null;
        generalCredits_ = null;
        foodCredits_ = null;
        trafficTrackCredits_ = null;
        tinyTrackCredits_ = null;
        trainCredits_ = null;
        arcadeCredits_ = null;

    }
}
