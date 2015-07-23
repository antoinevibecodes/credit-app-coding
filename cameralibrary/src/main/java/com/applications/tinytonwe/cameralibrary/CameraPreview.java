package com.applications.tinytonwe.cameralibrary;

/**
 * Created by admin on 6/23/2015.
 */

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * Created by admin on 4/22/2015.
 * This is the camera surface preview class. This is the surface on which the camera gets
 * displayed before it is shown on the application screen
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    //private SurfaceHolder surfaceHolder;
    private Camera myCamera;

    private int width;
    private int height;
    /**
     * The constructor for this class. This class gets the handle of the camera and context
     * of the activity calling it. It is here that the surface callback is also registered
     * to obtain information about the durface such as when it is created
     * @param context Represents the context of the activity wishing to use this class
     * @param myCamera Represents a handler to the camera to be used
     */
    public CameraPreview(Context context, Camera myCamera){
        super(context);

        this.myCamera = myCamera;

        this.getHolder().addCallback(this);
    }

    /**
     *This method gets called when this surface view gets created. It is here that
     * we start the camera preview since we are sure that the surface has been created.
     * Displaying the camera on a surface which has not been created leads to errors.
     * @param surfaceHolder surface holder on whcih to display the camera for this surface
     */
    public void surfaceCreated(SurfaceHolder surfaceHolder){

    }

    public void releaseCamera(){
        if(myCamera == null)
            return;

        myCamera.stopPreview();
        myCamera.release();
        myCamera = null;

    }

    /**
     *  This method gets called when the surface has been destroyed. As of this time,
     *  nothing has been implemented for this method.
     * @param surfaceHolder the surface holder of the camera which as destroyed
     */
    public void surfaceDestroyed(SurfaceHolder surfaceHolder){

        //Don't worry, camera has already been released in the onPause
        releaseCamera();
    }


    /**
     * This mehtod is called whenever the size of the surface changes, like for example
     * during a device rotation, and initially when the surface is created. When the device rotates,
     * the height and width changes, and it is here that that change is being caught.
     * @param holder the surface holder/handle of the current surface
     * @param format
     * @param width the width of the new surface/screen
     * @param height the height of the new surface/screen
     */
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height){

        if (surfaceHolder.getSurface() == null)
            return;

        try {
            myCamera.stopPreview();
        }
        catch (Exception e){

        }

        //
        //check if already preview is on and stop it this depends on when Changed is fired
        //

        //
        //set camera parameters
        //

        Camera.Parameters parameters = myCamera.getParameters();

        // compute preview parameters
        List<Camera.Size> supportedPreviewSizes
                = parameters.getSupportedPreviewSizes();
        Camera.Size optimalPreviewSize =
                    getOptimalPreviewSize(supportedPreviewSizes, this.width, this.height);
        parameters.setPreviewSize(optimalPreviewSize.width, optimalPreviewSize.height);

        //computer optimal picture size
        List<Camera.Size> supportedPictureSizes =
                parameters.getSupportedPictureSizes();
        Camera.Size optimalPictureSize =
                supportedPictureSizes.get(supportedPictureSizes.size() / 2);
        parameters.setPictureSize(optimalPictureSize.width, optimalPictureSize.height);

        //set focus mode
        List<String> focusModes = parameters.getSupportedFocusModes();
        for( int loop = 0 ; loop < focusModes.size() ; loop++ )
            if(focusModes.get(loop).equals("continuous-picture"))
                parameters.setFocusMode("continuous-picture");

        //set front and back camera

        //
        //display preview
        //
        try {
            this.myCamera.setParameters(parameters);
            this.myCamera.setPreviewDisplay(surfaceHolder);
            this.myCamera.startPreview();

        } catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     *Method calculates the size of its current view/layout. (Parent layout in this case)
     *I use this to choose the best preview size from the supported list of my camera
     *this is in order to avoid choosing preview sizes which will make images appear stretched.
     * The principle for using this is trying to determinw which preview Size comes
     * close enough to the actual screen size in which this view is contained, so that
     * as less stretching as possible will be performed also taking into consideration the  aspect ratio
     * @param widthMeasureSpec the width of the view
     * @param heightMeasureSpec the height of the view
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        this.height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }


    /**
     *Below code calculates optimal preview size of current camera based on screen size
     *Code below is used in combination with onMeasure, which is called when a a program needs to calculate
     *the size of its layout
     *Since the constructor is called before onMeasure, that assures me that the camera is isSet before the next
     *first method which is called is onMeasure above
     * @param sizes represents the camera's supported preview sizes
     * @param w represents the layout's width
     * @param h represents the layout's height
     * @return returns the best camera preview size to use based on the layout in which this is
     * contained in
     */
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

