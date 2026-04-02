package com.applications.tinytonwe.cameralibrary.CameraSettings;

/**
 * This is an enum class representing all the camera orientations. It will
 * be used programmatically at different times to determine in which orientation
 * the camera is in order to either adjust a display preview, or to know which
 * manipulation to perform to an image for it to appear correctly based in which
 * orientation it was taken in.
 */
public enum CameraOrientation {

    PORTRAIT (1),
    LANDSCAPE_RIGHT(2),
    LANDSCAPE_LEFT(3),
    PORTRAIT_DOWN(4);


    private final int value;
    private CameraOrientation(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
