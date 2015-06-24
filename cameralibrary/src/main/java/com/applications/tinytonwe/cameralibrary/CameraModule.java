package com.applications.tinytonwe.cameralibrary;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.view.Surface;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Created by admin on 6/23/2015.
 */
public class CameraModule{

    private Camera camera;
    private int frontCamera;
    private int backCamera;
    private int frontCameraDisplayCorrection;
    private int backCameraDisplayCorrection;

    private int cameraInUse;
    private int cameraInUseDisplayCorrection;
    private CurrentCamera currentCamera;

    private Activity callingActivity;
    private CameraOrientation cameraOrientation;

    private CameraPreview cameraPreview;
    private PictureTaken  pictureTaken;
    private FrameLayout cameraView;
    private SingletonCameraData cameraData = SingletonCameraData.getUniqueInstance();

    private boolean cropDimensionsSet = false;
    private float portraitCropPercentage = 0.6f;
    private float landscapeCropPercentage = 0.6f;

    private float cropPercentage;
    private CropPreview cropPreview;
    private boolean useCrop;

    private Bitmap originalImage;
    private Bitmap croppedImage;


    public CameraModule(Activity callingActivity, FrameLayout cameraView, boolean useCrop){
        this.callingActivity = callingActivity;
        currentCamera = CurrentCamera.FRONT;
        this.cameraView = cameraView;
        this.useCrop = useCrop;
    }

    public void setCropDimensions(float portraitCropPercentage, float landscapeCropPercentage){
        this.portraitCropPercentage = portraitCropPercentage;
        this.landscapeCropPercentage = landscapeCropPercentage;
    }

    private void setupCamera(){

        findDeviceCameras();

        cameraInUse = frontCamera;
        cameraInUseDisplayCorrection = frontCameraDisplayCorrection;

        if(currentCamera == CurrentCamera.BACK){
            cameraInUse = backCamera;
            cameraInUseDisplayCorrection = backCameraDisplayCorrection;
        }

        try {
            camera = Camera.open(cameraInUse);
            //setCropDimensions();
            camera.setDisplayOrientation(cameraInUseDisplayCorrection);
            startCameraPreview();
        }
        catch (Exception e){

        }

    }



    private void findDeviceCameras(){
        int camerasOnDevice = Camera.getNumberOfCameras();
        int rotation = this.callingActivity.getWindowManager().getDefaultDisplay().getRotation();

        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0;
                cameraOrientation = CameraOrientation.PORTRAIT;
                break;
            case Surface.ROTATION_90: degrees = 90;
                cameraOrientation = CameraOrientation.LANDSCAPE_RIGHT;
                break;
            case Surface.ROTATION_180: degrees = 180;
                cameraOrientation = CameraOrientation.PORTRAIT_DOWN;
                break;
            case Surface.ROTATION_270: degrees = 270;
                cameraOrientation = CameraOrientation.LANDSCAPE_LEFT;
                break;
        }


        Camera.CameraInfo info = new Camera.CameraInfo();

        for(int cameras=0; cameras<camerasOnDevice; cameras++){

            Camera.getCameraInfo(cameras, info);

            if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                frontCamera = cameras;
                frontCameraDisplayCorrection = (info.orientation + degrees) % 360;
                frontCameraDisplayCorrection = (360 - frontCameraDisplayCorrection) % 360;
            }
            if(info.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                backCamera = cameras;
                backCameraDisplayCorrection = (info.orientation - degrees + 360) % 360;
            }
        }
    }



    public void startCameraPreview(){

        setupCamera();
        cameraPreview = new CameraPreview(callingActivity,camera);
        cameraView.removeAllViews();
        cameraView.addView(cameraPreview);
        cropPicture();
    }


    private void cropPicture(){
        if(this.useCrop) {
           setCropPercentage();
            cropPreview = new CropPreview(callingActivity.getApplicationContext());
            cameraView.addView(cropPreview);
        }
    }

    /**
     * Here, I get the current device orientation and determine how big the cropping square should be.
     * The cropping square size percentages for the portrait and landscape mode can be isSet to
     * be different depending on the available screen size upon rotation
     */
    public void setCropPercentage(){

        int deviceOrientation = this.callingActivity.getResources().getConfiguration().orientation;

        switch (deviceOrientation){
            case 1:
                cropPercentage = portraitCropPercentage;
                break;
            case 2:
                cropPercentage = landscapeCropPercentage;
                break;
        }

        cameraData.setCropPercentage(cropPercentage);
    }

    public void takePicture(){
        String currentFocusMode = camera.getParameters().getFocusMode();

        if(currentFocusMode.equals("continuous-picture"))
            camera.autoFocus(autoFocusCallback());
        else
            camera.takePicture(null, null, pictureCallback());
    }



    private Camera.PictureCallback pictureCallback(){
        Camera.PictureCallback newPictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //do something
               pictureTaken = new PictureTaken(data,cameraInUse,cameraOrientation.getValue());
               cameraData.setPictureTaken(pictureTaken);
               processImage();
               resetCamera();
            }
        };
        return newPictureCallback;
    }


    private Camera.AutoFocusCallback autoFocusCallback(){
        Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {

                if(success) {
                    camera.takePicture(null, null, pictureCallback());
                }
                else
                    camera.autoFocus(autoFocusCallback());
            }
        };
        return autoFocusCallback;
    }


    public void flipCamera(){

        switch (currentCamera){
            case FRONT:
                currentCamera = CurrentCamera.BACK;
                break;
            case BACK:
                currentCamera = CurrentCamera.FRONT;
                break;
        }

        resetCamera();
    }


    private void resetCamera(){
        releaseCamera();
        startCameraPreview();
    }
    public void releaseCamera(){
        if(camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public Bitmap getCroppedImage(){
        return croppedImage;
    }

    public Bitmap getOriginalImage(){
        return originalImage;
    }


    public void processImage(){

        try {
            PictureTaken currentPicture = cameraData.getPictureTaken();
            CurrentCamera cameraUsed = ((currentPicture.getCameraUsed() == 0) ? CurrentCamera.BACK : CurrentCamera.FRONT);
            int cameraOrientation = currentPicture.getCameraOrientation();
            Bitmap decodeImage = BitmapFactory.decodeByteArray(currentPicture.getPictureData(), 0, currentPicture.getPictureData().length);
            correctDisplayAndRotate(decodeImage, cameraOrientation, cameraUsed);
            cameraData.setOriginalBitmap(originalImage);
            if (this.useCrop)
                cameraData.setCroppedBitmap(croppedImage);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    private void correctDisplayAndRotate(Bitmap bitmapToRotate, int cameraOrientation, CurrentCamera cameraUsed){
        Matrix matrix = new Matrix();
        int angle = 0;

        if(cameraUsed == CurrentCamera.FRONT) {
            angle = correctionAngle(true,cameraOrientation);
            matrix.preScale(1, -1);
        }
        else{
            angle = correctionAngle(false,cameraOrientation);
        }

        if(angle != -9999)
            matrix.postRotate(angle);

        originalImage = Bitmap.createBitmap(bitmapToRotate, 0, 0, bitmapToRotate.getWidth(), bitmapToRotate.getHeight(), matrix, true);

        croppedImage =  cropImage(originalImage);
    }


    public Bitmap cropImage(Bitmap bitmapToCrop){

        cameraData.setPictureDimensions(bitmapToCrop.getWidth(), bitmapToCrop.getHeight());

        int squareDimensions = cameraData.getActualCropDimension();

        int startXOfCrop = cameraData.getActualXOrigin();
        int startYOfCrop = cameraData.getActualYOrigin();

        Bitmap newBitmap = Bitmap.createBitmap(bitmapToCrop, startXOfCrop, startYOfCrop, squareDimensions, squareDimensions);

        return newBitmap;
    }

    public int correctionAngle(boolean frontCamera, int cameraOrientation){

        int angle = -9999;

        if(frontCamera) {
            switch (cameraOrientation) {
                case 1:
                    angle = 270;
                    break;
                case 4:
                    angle = 90;
                    break;
                case 3:
                    //Don't do anything
                    break;
                case 2:
                    angle = 180;
                    break;
            }
        }
        else{
            switch (cameraOrientation) {
                case 1:
                    angle = 90;
                    break;
                case 4:
                    angle = 270;
                    break;
                case 3:
                    angle = 180;
                    break;
                case 2:
                    angle = 0;
                    break;
            }

        }

        return angle;
    }



}
