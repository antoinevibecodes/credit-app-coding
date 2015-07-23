package com.applications.tinytonwe.cameralibrary.CameraPreview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.applications.tinytonwe.cameralibrary.CameraSettings.CameraSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 6/26/2015.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private CameraSettings cameraSettings_;
    private Camera myCamera_;

    private int width_;
    private int height_;

    private Activity activity_;
    private FrameLayout cameraView_;

    public CameraPreview(Activity activity, CameraSettings cameraSettings, FrameLayout cameraView){
        super(activity.getApplicationContext());

        activity_ = activity;
        cameraView_ = cameraView;
        cameraSettings_ = cameraSettings;
        myCamera_ = cameraSettings_.getMyCamera_();
    }


    public void startPreview(){
        this.getHolder().addCallback(this);

        cameraView_.removeAllViews();
        cameraView_.addView(this);
    }

    public void restartPreview(SurfaceHolder surfaceHolder){
        myCamera_ = cameraSettings_.getMyCamera_();
        displayPreview(surfaceHolder);
    }


    public void surfaceCreated(SurfaceHolder surfaceHolder){

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

        displayPreview(holder);
    }


    private void displayPreview(SurfaceHolder holder){

        setOptimalParameters();

        try {
            myCamera_.setPreviewDisplay(holder);
            myCamera_.startPreview();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder){
        //Don't worry, camera has already been released in the onPause
        releaseCamera_();
    }

    private void setOptimalParameters(){

        if(cameraSettings_.useOptimalPreviewSettings()) {
            //
            //Setting the camera's optimal preview size based on size of view to contain it
            //
            Camera.Parameters parameters = myCamera_.getParameters();

            //Setting the preview size
            List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
            Camera.Size optimalPreviewSize = getOptimalPreviewSize(supportedPreviewSizes, width_, height_);
            parameters.setPreviewSize(optimalPreviewSize.width, optimalPreviewSize.height);

            //setting the picture size
            List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
            parameters.setPictureSize(supportedPictureSizes.get(0).width, supportedPictureSizes.get(0).height);


            //setting autofocus if applicable
            List<String> focusModes = parameters.getSupportedFocusModes();
            for (int loop = 0; loop < focusModes.size(); loop++) {
                if (focusModes.get(loop).equals("continuous-picture")) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                    return;
                }
            }

            //setting picture mode
            parameters.setSceneMode(Camera.Parameters.SCENE_MODE_PORTRAIT);

                myCamera_.setParameters(parameters);
        }
    }


    private void releaseCamera_(){
        if(myCamera_ != null) {
            myCamera_.stopPreview();
            myCamera_.release();
            myCamera_ = null;
        }
    }

    public void flipCamera(){
        releaseCamera_();
        cameraSettings_.flipCamera();
        restartPreview(this.getHolder());
    }

    public void setCameraSettings_(CameraSettings cameraSettings){
        cameraSettings_ = cameraSettings;
    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width_ = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height_ = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width_, height_);
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}
