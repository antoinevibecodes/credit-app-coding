package com.applications.tinytonwe.drivermodificationappversion2.Camera;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.applications.tinytonwe.cameralibrary.CameraModule;
import com.applications.tinytonwe.drivermodificationappversion2.R;

public class CameraActivity extends AppCompatActivity {

    private FrameLayout cameraFrame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        registerListeners();
        startCamera();
    }


    private void registerListeners(){
        cameraFrame = (FrameLayout)findViewById(R.id.cameraFrame);
    }

    private void startCamera(){
        final CameraModule cameraModule = new CameraModule(this,cameraFrame,true);
        cameraModule.startCameraPreview();
    }

}
