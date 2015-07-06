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
}
