package com.applications.tinytonwe.cameralibrary.CameraSettings;

import android.app.Activity;
import android.hardware.Camera;
import android.view.Surface;

import com.applications.tinytonwe.cameralibrary.LibData;

import java.util.List;

/**
 * Created by admin on 6/26/2015.
 */
public class CameraSettings {

    private Camera myCamera_;

    private int frontCameraId_;
    private int backCameraId_;
    private int frontCameraAngleCorrection_;
    private int backCameraAngleCorrection_;
    private boolean optimalPreviewSettings_ = true;
    private CameraOrientation cameraOrientation_;
    private CameraSelected cameraSelected_;

    private boolean cameraCanAutoFocus;

    private Camera.Parameters parameters_;

    private Activity callingActivity_;

    public CameraSettings(Activity callingActivity, CameraSelected cameraSelected){
        callingActivity_ = callingActivity;
        cameraSelected_ = cameraSelected;
        prepareCamera_();

    }

    private void prepareCamera_(){
        getDeviceCameras_();
        openCamera_();
    }


    private void openCamera_(){
        switch (cameraSelected_){
            case FRONT:
                myCamera_ = Camera.open(frontCameraId_);
                myCamera_.setDisplayOrientation(frontCameraAngleCorrection_);
                break;
            case BACK:
                myCamera_ = Camera.open(backCameraId_);
                myCamera_.setDisplayOrientation(backCameraAngleCorrection_);
                break;
            default:
                myCamera_ = Camera.open();
                myCamera_.setDisplayOrientation(frontCameraAngleCorrection_);
                break;
        }

        parameters_ = myCamera_.getParameters();
    }

    private void enableOptimalPreviewSettings(boolean optimalPreviewSettings){
        optimalPreviewSettings_ = optimalPreviewSettings;    }

    public boolean useOptimalPreviewSettings(){
        return optimalPreviewSettings_;
    }

    private void getDeviceCameras_(){
        int camerasOnDevice = Camera.getNumberOfCameras();
        int rotation = callingActivity_.getWindowManager().getDefaultDisplay().getRotation();

        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0;
                cameraOrientation_ = CameraOrientation.PORTRAIT;
                break;
            case Surface.ROTATION_90: degrees = 90;
                cameraOrientation_ = CameraOrientation.LANDSCAPE_RIGHT;
                break;
            case Surface.ROTATION_180: degrees = 180;
                cameraOrientation_ = CameraOrientation.PORTRAIT_DOWN;
                break;
            case Surface.ROTATION_270: degrees = 270;
                cameraOrientation_ = CameraOrientation.LANDSCAPE_LEFT;
                break;
        }


        Camera.CameraInfo info = new Camera.CameraInfo();

        for(int cameras=0; cameras<camerasOnDevice; cameras++){

            Camera.getCameraInfo(cameras, info);

            if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                frontCameraId_ = cameras;
                frontCameraAngleCorrection_ = (info.orientation + degrees) % 360;
                frontCameraAngleCorrection_ = (360 - frontCameraAngleCorrection_) % 360;
            }
            if(info.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                backCameraId_ = cameras;
                backCameraAngleCorrection_ = (info.orientation - degrees + 360) % 360;
            }
        }
    }

    public int getCameraOrientation(){
        return cameraOrientation_.getValue();
    }
    public void setCameraToUse(CameraSelected cameraSelected){
        cameraSelected_ = cameraSelected;
    }

    public int getCameraSelected(){
        return cameraSelected_.getValue();
    }

    public void flipCamera(){
       myCamera_ = null;

        switch (cameraSelected_){
            case FRONT:
                cameraSelected_ = CameraSelected.BACK;
                break;
            case BACK:
                cameraSelected_ = CameraSelected.FRONT;
                break;
        }

        openCamera_();


    }

    public void enableAutoFocus(){
        List<String> focusModes = parameters_.getSupportedFocusModes();

        for(int loop=0; loop<focusModes.size(); loop++){
            if(focusModes.get(loop).equals("continuous-picture")) {
                parameters_.setFocusMode("continuous-picture");
                cameraCanAutoFocus = true;
                return;
            }
        }
    }

    public void enableFlash(){
        List<String> flashModes = parameters_.getSupportedFlashModes();

        for(int loop=0; loop<flashModes.size(); loop++){
            if(flashModes.get(loop).equals("on")) {
                parameters_.setFocusMode("on");
                cameraCanAutoFocus = true;
                return;
            }
        }
    }

    public Activity getCallingActivity_(){
        return callingActivity_;
    }

    public void setCameraPreviewQuality(Quality quality){

        List<Camera.Size> supportedPreviewSizes = parameters_.getSupportedPreviewSizes();
        Camera.Size chosenPreviewSize = getCameraSize_(quality, supportedPreviewSizes);

        parameters_.setPreviewSize(chosenPreviewSize.width,chosenPreviewSize.height);
    }

    public void setCameraPictureQuality(Quality quality){

        List<Camera.Size> supportedPictureSizes = parameters_.getSupportedPictureSizes();
        Camera.Size chosenPreviewSize = getCameraSize_(quality, supportedPictureSizes);

        int width = chosenPreviewSize.width;
        int height = chosenPreviewSize.height;

        parameters_.setPreviewSize(width,height);

        LibData.getUniqueInstance().setPictureDimensions(width, height);
    }

    private Camera.Size getCameraSize_(Quality quality, List<Camera.Size> sizes){
        int sizeCount = sizes.size();
        Camera.Size chosenPreviewSize;

        int selectedPreviewSize = 0;

        switch (quality){
            case HIGH:
                selectedPreviewSize = 0;
            case MEDIUM_HIGH:
                selectedPreviewSize = 1;
            case MEDIUM:
                selectedPreviewSize = 2;
            case MEDIUM_LOW:
                selectedPreviewSize = 3;
            case LOW:
                selectedPreviewSize = 4;
        }

        if(selectedPreviewSize < sizeCount)
            chosenPreviewSize = sizes.get(selectedPreviewSize);
        else
            chosenPreviewSize = sizes.get(sizeCount-1);

        return chosenPreviewSize;
    }

    public Camera getMyCamera_(){
        return myCamera_;
    }

    public boolean canCameraAutoFocus(){
        return cameraCanAutoFocus;
    }
}
