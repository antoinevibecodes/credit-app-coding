package com.applications.tinytonwe.drivermodificationappversion2.Camera;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by admin on 8/12/2015.
 */
public class CameraData {
    private static CameraData uniqueInstance = null;
    private ArrayList<Bitmap> images_ = new ArrayList<>();
    private Bitmap bitmapChosen_ = null;

    public static CameraData getInstance() {
        if(uniqueInstance == null){
            uniqueInstance = new CameraData();
        }
        return uniqueInstance;
    }

    private CameraData() {

    }

    public void addImage(Bitmap bitmap){
        images_.add(bitmap);
    }

    public ArrayList<Bitmap> getImages(){
        return images_;
    }

    public void setBitmapChosen(Bitmap bitmapChosen){
        bitmapChosen_ = bitmapChosen;
    }

    public Bitmap getBitmapChosen(){
        return bitmapChosen_;
    }

    public void reset(){
        uniqueInstance = null;
        images_ = new ArrayList<>();
        bitmapChosen_ = null;
    }
}
