package com.applications.tinytonwe.cameralibrary.CameraSettings;

/**
 * This is an enum class representing the cameras on the device. I use this enum across
 * classes in this application to easily determine which camera I am dealing with as by default
 * the various cameras are identified by Integer Constants in Android
 */
public enum CameraSelected {
    FRONT (1),
    BACK (0);

    private int value;
    private CameraSelected(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}
