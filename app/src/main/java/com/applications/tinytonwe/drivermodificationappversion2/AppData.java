package com.applications.tinytonwe.drivermodificationappversion2;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;


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
        croppedImage_.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void setDriverFirstName(String driverFirstName){
            driverFirstName_ = driverFirstName;
    }

    public void setDriverLastName(String driverLastName){
        driverLastName = driverLastName_;
    }

    public void setDriverDob(String dob){
        dob_ = dob;
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
    }
}
