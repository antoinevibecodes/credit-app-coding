package com.applications.tinytonwe.drivermodificationappversion2;

import android.graphics.Bitmap;


/**
 * Created by admin on 6/24/2015.
 */
public class AppData {

    public static AppData appDataInstance;

    private Bitmap croppedImage = null;
    private Bitmap originalImage = null;
    private long cardIdReadLongValue = 0;
    private String cardIdReadStringValue = "";

    public static AppData getAppDataInstance(){
        if(appDataInstance == null){
            appDataInstance = new AppData();
        }
        return appDataInstance;
    }

    public void setCroppedImage(Bitmap croppedImage){
        this.croppedImage = croppedImage;
    }

    public void setOriginalImage(Bitmap originalImage){
        this.originalImage = originalImage;
    }

    public Bitmap getCroppedImage(){
        return croppedImage;
    }

    public Bitmap getOriginalImage(){
        return originalImage;
    }

    public void setCardIdReadLongValue(long cardIdReadLongValue){
        this.cardIdReadLongValue = cardIdReadLongValue;
    }

    public void setCardIdReadStringValue(String cardIdReadStringValue){
        this.cardIdReadStringValue = cardIdReadStringValue;
    }

    public long getCardIdReadLongValue(){
        return this.cardIdReadLongValue;
    }

    public String getCardIdReadStringValue(){
        return this.cardIdReadStringValue;
    }
}
