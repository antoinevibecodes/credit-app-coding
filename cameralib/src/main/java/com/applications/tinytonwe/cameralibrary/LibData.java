package com.applications.tinytonwe.cameralibrary;

import android.graphics.Bitmap;

import com.applications.tinytonwe.cameralibrary.CameraPicture.Picture;
import com.applications.tinytonwe.cameralibrary.CameraSettings.CameraSelected;

/**
 * Created by admin on 4/23/2015.
 */
public class LibData {

    private static LibData libDataInstance = null;

    private int pictureHeight = 0;
    private int pictureWidth = 0;

    private float cropPercentage = 0f;

    private int screenHeight = 0;
    private int screenWidth = 0;

    private int displayCropHeight = 0;
    private int displayCropWidth = 0;
    private int displaySquareDimension = 0;

    private int actualCropHeight = 0;
    private int actualCropWidth = 0;
    private boolean useWidth = true;
    private int actualSquareDimension = 0;

    private int displayXOrigin = 0;
    private int actualXOrigin = 0;

    private int displayYOrigin = 0;
    private int actualYOrigin = 0;

    private int displayXEnd = 0;
    private int actualXEnd = 0;

    private int displayYEnd = 0;
    private int actualYEnd = 0;

    private int thumbnailDimensions = 0;

    private int scaledDimensionDisplay = 0;

    private int currentOrientation;
    private CameraSelected cameraSelected;


    private Picture picture;
    private Bitmap originalBitmap;
    private Bitmap croppedBitmap;

    private boolean cropSet = false;

    private LibData(){

    }

    /**
     * Constructor, returns a unique instance of this class
     * @return the sole instance of this class
     */
    public static LibData getUniqueInstance(){
        if(libDataInstance == null){
            libDataInstance = new LibData();
        }
        return libDataInstance;
    }

    /**
     * Sets how big the crop square should be
     * @param percent represents the percentage in decimals
     */
    public void setCropPercentage(float percent){
        cropPercentage = percent;
        cropSet = true;
    }


    public boolean isCropSet(){
        return cropSet;
    }
    /**
     * Sets the dimensions of the crop square on the actual screen size
     * @param screenWidth represents the width of the parent/actual screen size
     * @param screenHeight represents the height of he parent screen size
     */
    public void setScreenDimensions(int screenWidth, int screenHeight){
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        calculateDisplayCropDimensions();
    }

    /**
     * Sets the dimensions of the picture size to crop within the actual picture
     * @param pictureWidth represents the width of the actual picture
     * @param pictureHeight represents the height of the actual picture
     */
    public void setPictureDimensions(int pictureWidth, int pictureHeight){
        this.pictureHeight = pictureHeight;
        this.pictureWidth = pictureWidth;
        calculateActualCropDimensions();
    }

    /**
     * Calculate the dimensions of the physical cropping square based on the percentage and the
     * parent screen size
     * @return return the display dimension which is a square so return just one value
     */
    public int calculateDisplayCropDimensions(){
        displayCropHeight = (int) Math.floor(screenHeight * cropPercentage);
        displayCropWidth = (int) Math.floor(screenWidth * cropPercentage);
        displaySquareDimension =  ((displayCropWidth > displayCropHeight) ? displayCropHeight : displayCropWidth);
        calculateDisplayCoordinates();
        return displaySquareDimension;
    }

    /**
    * Calculate the dimensions of the virtual cropping square/image based on the percentage and the
    * actual image size
    * @return return the display dimension which is a square so return just one value
    */
    public int calculateActualCropDimensions(){
        actualCropHeight = (int) Math.ceil(pictureHeight * cropPercentage);
        actualCropWidth = (int) Math.ceil(pictureWidth * cropPercentage);
        actualSquareDimension =  ((actualCropWidth > actualCropHeight) ? actualCropHeight : actualCropWidth);
        calculateActualCoordinates();
        return actualSquareDimension;
    }

    /**
     * This method calculates and sets the starting and end points of the cropping square for the
     * parent view based on the cropping percentage
     */
    public void calculateDisplayCoordinates(){
        displayXOrigin = screenWidth/2 - displaySquareDimension/2;
        displayXEnd = screenWidth/2 + displaySquareDimension/2;
        displayYOrigin = screenHeight/2 - displaySquareDimension/2 - displaySquareDimension/4;
        displayYEnd = screenHeight/2 + displaySquareDimension/4;
    }

    /**
     * This method calculates and sets the starting and end points of the cropping square for the
     * actual image based on the cropping percentage
     */
    public void calculateActualCoordinates(){
        actualXOrigin  = pictureWidth/2 - actualSquareDimension/2;
        actualXEnd = pictureWidth/2 + actualSquareDimension/2;
        actualYOrigin = pictureHeight/2 - actualSquareDimension/2 - actualSquareDimension/4;
        actualYEnd = pictureHeight/2 + actualSquareDimension/4;
    }

    /**
     * This method sets the current device orientation
     * @param currentOrientation the current device orientation
     */
    public void setCurrentOrientation(int currentOrientation){
        //Value isSet each time device rotates in picture display activity
        this.currentOrientation = currentOrientation;
    }

    /**
     * This methods checks the current device orientation, as based on that sets then
     * dimensions of the pictures displayed
     */
    public void checkDeviceOrientation(){
        switch (currentOrientation) {
            case 1://portrait
                thumbnailDimensions = 350;
                scaledDimensionDisplay = 1000;
                break;
            case 2://landscape
                thumbnailDimensions = 300;
                scaledDimensionDisplay = 600;
                break;
        }
    }

    /**
     * Method returns the dimensions of the scaled displayed on the screen
     * @return returns the scaled dimensions of the pictures
     */
    public int getScaledDimensions(){
        checkDeviceOrientation();
        return scaledDimensionDisplay;
    }

    public int getDisplayCropDimension(){
        calculateDisplayCropDimensions();
        return displaySquareDimension;
    }

    /**
     * Method returns the dimensions of the scaled displayed on the pictures
     * @return returns the scaled dimensions of the pictures
     */
    public int getActualCropDimension(){
        calculateActualCropDimensions();
        return actualSquareDimension;
    }

    /**
     * This method returns the thumbnail dimensions of to use for displaying the pictures
     */
    public int getThumbnailDimensions(){
        return thumbnailDimensions;
    }

    /**
     * This method returns the x origin of the crop square on the physical device
     */
    public int getDisplayXOrigin(){
        return displayXOrigin;
    }

    /**
     * This method returns the y start of the crop square on the physical device
     */
    public int getDisplayYOrigin(){
        return displayYOrigin;
    }

    /**
     * This method returns the x end of the crop square on the physical device
     */
    public int getDisplayXEnd(){
        return displayXEnd;
    }

    /**
     * This method returns the y end of the crop square on the physical device
     */
    public int getDisplayYEnd(){
        return displayYEnd;
    }

    /**
     * This method returns the x origin of the cropped picture for the actual image
     */
    public int getActualXOrigin(){
        return actualXOrigin;
    }

    /**
     * This method returns the y origin of the cropped picture for the actual image
     */
    public int getActualYOrigin(){
        return actualYOrigin;
    }

    /**
     * This method empties the the current Singleton Data class
     */
    public void reset(){
        this.libDataInstance = null;
    }

    public CameraSelected getCameraSelected(){
        return this.cameraSelected;
    }

    public void setPicture(Picture picture){
        this.picture = picture;
    }

    public Picture getPicture(){
        return this.picture;
    }

    public void setOriginalBitmap(Bitmap originalBitmap){
        this.originalBitmap = originalBitmap;
        String here = "here Bitch";
    }

    public void setCroppedBitmap(Bitmap croppedBitmap){
        this.croppedBitmap = croppedBitmap;
    }

    public Bitmap getOriginalBitmap(){
        return  originalBitmap;
    }

    public Bitmap getCroppedBitmap(){
        return croppedBitmap;
    }
}
