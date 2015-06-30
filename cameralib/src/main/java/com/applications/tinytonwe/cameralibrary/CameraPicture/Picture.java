package com.applications.tinytonwe.cameralibrary.CameraPicture;

/**
 * Created by admin on 6/26/2015.
 */
public class Picture {
    private byte[] pictureData;
    private int cameraUsed;
    private int cameraOrientation;

    public Picture(byte[] pictureData, int cameraUsed, int cameraOrientation){
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
