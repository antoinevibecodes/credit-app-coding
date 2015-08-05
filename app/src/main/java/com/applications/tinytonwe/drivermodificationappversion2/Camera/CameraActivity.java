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
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.SelectOption.OptionsActivity;
import com.applications.tinytonwe.drivermodificationappversion2.SendPicture.SendPictureActivity;

public class CameraActivity extends AppCompatActivity implements CapturedPictureCallback{



    private CapturedPicture capturedPicture_;
    private CameraSettings cameraSettings_;
    private CameraPreview cameraPreview_;
    private CropWindow cropWindow_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        registerListeners();
        startCamera();
    }


    private void registerListeners(){

        FrameLayout cameraView = (FrameLayout)findViewById(R.id.cameraFrame);

        cameraSettings_ = new CameraSettings(this, CameraSelected.FRONT);
        cameraSettings_.useOptimalPreviewSettings();

        cameraPreview_ = new CameraPreview(this, cameraSettings_, cameraView);

        capturedPicture_ = new CapturedPicture(cameraSettings_);

        cropWindow_ = new CropWindow(this, 0.8f, "#ffc000",cameraView);

        ImageButton cancelBtn = (ImageButton)findViewById(R.id.cancelBtn);
        ImageButton switchBtn = (ImageButton)findViewById(R.id.switchBtn);
        ImageButton shutterBtn = (ImageButton)findViewById(R.id.shutterBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(AppActions.CANCEL);
            }
        });

        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(AppActions.FLIP_CAMERA);
            }
        });

        shutterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(AppActions.TAKE_PICTURE);
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
                startActivity(new Intent(this, OptionsActivity.class));
                break;
            case FLIP_CAMERA:
                cameraPreview_.flipCamera();
                break;
            case  TAKE_PICTURE:
                capturedPicture_.takePicture();
                break;
        }
    }



    public void pictureTaken(Bitmap originalImage, Bitmap croppedImage){

        AppData appData = AppData.getAppDataInstance_();
        appData.setCroppedImage_(croppedImage);
        appData.setOriginalImage_(originalImage);

        startActivity(new Intent(this, SendPictureActivity.class));
    }

    public void onBackPressed(){
        this.finish();
        startActivity(new Intent(this,OptionsActivity.class));
    }
}
