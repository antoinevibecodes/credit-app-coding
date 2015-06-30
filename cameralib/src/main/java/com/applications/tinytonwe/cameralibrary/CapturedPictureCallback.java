package com.applications.tinytonwe.cameralibrary;

import android.graphics.Bitmap;

/**
 * Created by admin on 6/24/2015.
 */
public interface CapturedPictureCallback {

    public void pictureTaken(Bitmap originalImage, Bitmap croppedImage);
}
