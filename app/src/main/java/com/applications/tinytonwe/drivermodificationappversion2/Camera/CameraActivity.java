package com.applications.tinytonwe.drivermodificationappversion2.Camera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.applications.tinytonwe.cameralibrary.CameraModule;
import com.applications.tinytonwe.cameralibrary.PictureTakenNotification;
import com.applications.tinytonwe.cameralibrary.SingletonCameraData;
import com.applications.tinytonwe.drivermodificationappversion2.AppActions;
import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.MainActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.Validation.ValidationActivity;

public class CameraActivity extends AppCompatActivity implements PictureTakenNotification{

    private CameraModule cameraModule;

    private FrameLayout cameraFrame;

    private ImageButton cancelBtn;
    private ImageButton switchBtn;
    private ImageButton shutterBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        registerListeners();
        startCamera();
    }


    private void registerListeners(){

        cameraFrame = (FrameLayout)findViewById(R.id.cameraFrame);

        cancelBtn = (ImageButton)findViewById(R.id.cancelBtn);
        switchBtn = (ImageButton)findViewById(R.id.switchBtn);
        shutterBtn = (ImageButton)findViewById(R.id.shutterBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActions appActions = AppActions.CANCEL;
                buttonHandler(appActions);
            }
        });

        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActions appActions = AppActions.FLIP_CAMERA;
                buttonHandler(appActions);
            }
        });

        shutterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActions appActions = AppActions.TAKE_PICTURE;
                buttonHandler(appActions);
            }
        });
    }

    private void startCamera(){
        cameraModule = new CameraModule(this,cameraFrame,true,this);
        cameraModule.startCameraPreview();
    }

    private void buttonHandler(AppActions appActions){
        switch(appActions){
            case CANCEL:
                cancelProcess();
                break;
            case FLIP_CAMERA:
                flipCamera();
                break;
            case  TAKE_PICTURE:
                takePicture();
                break;
        }
    }


    private void cancelProcess(){
        Intent previousActivity = new Intent(this, MainActivity.class);
        startActivity(previousActivity);
    }

    private void flipCamera(){
        cameraModule.flipCamera();
    }

    private void takePicture(){
            cameraModule.takePicture();
    }

    private void retrieveImages(){
        AppData appData = AppData.getAppDataInstance();
        SingletonCameraData singletonCameraData = SingletonCameraData.getUniqueInstance();

        appData.setCroppedImage(singletonCameraData.getCroppedBitmap());
        appData.setOriginalImage(singletonCameraData.getOriginalBitmap());
    }

    private void startValidationActivity(){
        Intent validation = new Intent(this, ValidationActivity.class);
        startActivity(validation);
    }


   public void pictureTaken(){
       retrieveImages();
       startValidationActivity();
   }
}
