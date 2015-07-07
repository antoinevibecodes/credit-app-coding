package com.applications.tinytonwe.drivermodificationappversion2.Camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.applications.tinytonwe.cameralibrary.CameraPicture.CapturedPicture;
import com.applications.tinytonwe.cameralibrary.CameraPreview.CameraPreview;
import com.applications.tinytonwe.cameralibrary.CameraSettings.CameraSelected;
import com.applications.tinytonwe.cameralibrary.CameraSettings.CameraSettings;
import com.applications.tinytonwe.cameralibrary.CapturedPictureCallback;
import com.applications.tinytonwe.cameralibrary.CropView.CropWindow;
import com.applications.tinytonwe.drivermodificationappversion2.AppActions;
import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.MainActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.Validation.ValidationActivity;

public class CameraActivity extends AppCompatActivity implements CapturedPictureCallback{

    private FrameLayout cameraView;

    private CapturedPicture capturedPicture_;
    private CameraSettings cameraSettings_;
    private CameraPreview cameraPreview_;
    private CropWindow cropWindow_;

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

        cameraView = (FrameLayout)findViewById(R.id.cameraFrame);

        cameraSettings_ = new CameraSettings(this, CameraSelected.FRONT);
        cameraSettings_.useOptimalPreviewSettings();

        cameraPreview_ = new CameraPreview(this, cameraSettings_, cameraView);

        capturedPicture_ = new CapturedPicture(cameraSettings_);

        cropWindow_ = new CropWindow(this, 0.5f, "#ffc000",cameraView);

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
        cameraPreview_.startPreview();
        cropWindow_.displayCropWindow();
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
        cameraPreview_.flipCamera();
    }

    private void takePicture(){
            capturedPicture_.takePicture();
    }


    private void startValidationActivity(){
        Intent validation = new Intent(this, ValidationActivity.class);
        startActivity(validation);
    }


    public void pictureTaken(Bitmap originalImage, Bitmap croppedImage){

        AppData appData = AppData.getAppDataInstance_();
        appData.setCroppedImage_(croppedImage);
        appData.setOriginalImage_(originalImage);

        startValidationActivity();
    }
}
