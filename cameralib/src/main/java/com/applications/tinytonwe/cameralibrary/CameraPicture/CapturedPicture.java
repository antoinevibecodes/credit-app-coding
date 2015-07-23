package com.applications.tinytonwe.cameralibrary.CameraPicture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;

import com.applications.tinytonwe.cameralibrary.CameraSettings.CameraSelected;
import com.applications.tinytonwe.cameralibrary.CameraSettings.CameraSettings;
import com.applications.tinytonwe.cameralibrary.CapturedPictureCallback;
import com.applications.tinytonwe.cameralibrary.LibData;

/**
 * Created by admin on 6/26/2015.
 */
public class CapturedPicture {

    private CameraSettings cameraSettings_;

    private Activity activity_;
    private CapturedPictureCallback capturedPictureCallback_;

    private Bitmap originalImage;
    private Bitmap croppedImage;

    private LibData cameraData;

    private Picture picture;

    public CapturedPicture(CameraSettings cameraSettings){
        cameraSettings_ = cameraSettings;

        activity_ = cameraSettings_.getCallingActivity_();
        capturedPictureCallback_ = (CapturedPictureCallback)activity_;

        cameraData = LibData.getUniqueInstance();
    }

    public void takePicture(){

        if(cameraSettings_.canCameraAutoFocus())
            cameraSettings_.getMyCamera_().autoFocus(autoFocusCallback_());
        else
            cameraSettings_.getMyCamera_().takePicture(null, null, pictureCallback_());
    }


    private Camera.PictureCallback pictureCallback_(){
        Camera.PictureCallback newPictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //do something
                picture = new Picture(data,cameraSettings_.getCameraSelected(),cameraSettings_.getCameraOrientation());
                storeImage();
                camera.startPreview();
            }
        };
        return newPictureCallback;
    }


    private Camera.AutoFocusCallback autoFocusCallback_(){
        Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {

                if(success) {
                    camera.takePicture(null, null, pictureCallback_());
                }
                else
                    camera.autoFocus(autoFocusCallback_());
            }
        };
        return autoFocusCallback;
    }


    public void storeImage(){

        try {
            CameraSelected cameraUsed = ((picture.getCameraUsed() == 0) ? CameraSelected.BACK : CameraSelected.FRONT);
            int cameraOrientation = picture.getCameraOrientation();

            Bitmap decodeImage = BitmapFactory.decodeByteArray(picture.getPictureData(), 0, picture.getPictureData().length);
            correctDisplayAndRotate(decodeImage, cameraOrientation, cameraUsed);
            cameraData.setOriginalBitmap(originalImage);
            cameraData.setCroppedBitmap(croppedImage);
            //Notify whoever subscribes that he can now access the taken picture
            capturedPictureCallback_.pictureTaken(originalImage,croppedImage);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    public Bitmap getOriginalImage(){
        return originalImage;
    }

    public Bitmap getCroppedImage(){
        return croppedImage;
    }
    private void correctDisplayAndRotate(Bitmap bitmapToRotate, int cameraOrientation, CameraSelected cameraUsed){
        Matrix matrix = new Matrix();
        int angle = 0;

        if(cameraUsed == CameraSelected.FRONT) {
            angle = correctionAngle(true,cameraOrientation);
            matrix.preScale(1, -1);
        }
        else{
            angle = correctionAngle(false,cameraOrientation);
        }

        if(angle != -9999)
            matrix.postRotate(angle);

        originalImage = Bitmap.createBitmap(bitmapToRotate, 0, 0, bitmapToRotate.getWidth(), bitmapToRotate.getHeight(), matrix, true);

        if(cameraData.isCropSet())
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
