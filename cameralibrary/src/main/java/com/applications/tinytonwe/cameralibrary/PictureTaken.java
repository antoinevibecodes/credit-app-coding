package com.applications.tinytonwe.cameralibrary;

/**
 * Created by admin on 5/11/2015.
 */
public class PictureTaken {
    private byte[] pictureData;
    private int cameraUsed;
    private int cameraOrientation;

    public PictureTaken(byte[] pictureData, int cameraUsed, int cameraOrientation){
        this.pictureData = pictureData;
        this.cameraUsed = cameraUsed;
        this.cameraOrientation = cameraOrientation;
    }

    public byte[] getPictureData(){
        return this.pictureData;
    }

    public int getCameraUsed(){
        return this.cameraUsed;
    }

    public int getCameraOrientation(){
        return this.cameraOrientation;
    }
}
