package com.applications.tinytonwe.drivermodificationappversion2.Validation;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.applications.tinytonwe.cameralibrary.SingletonCameraData;
import com.applications.tinytonwe.drivermodificationappversion2.AppActions;
import com.applications.tinytonwe.drivermodificationappversion2.AppData.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.Camera.CameraActivity;
import com.applications.tinytonwe.drivermodificationappversion2.MainActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;

public class ValidationActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ProgressBar progressBar;

    private ImageButton cancelBtn;
    private ImageButton retakeBtn;
    private ImageButton sendBtn;

    private ImageView pictureTaken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        registerListeners();
        displayPictureTaken();
    }


    private void registerListeners(){
        progressBar = (ProgressBar)findViewById(R.id.progressbar);

        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("Validation Process");

        pictureTaken = (ImageView)findViewById(R.id.pictureTaken);

        cancelBtn = (ImageButton)findViewById(R.id.cancelBtn);
        retakeBtn = (ImageButton)findViewById(R.id.retakeBtn);
        sendBtn = (ImageButton)findViewById(R.id.sendBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActions appActions = AppActions.CANCEL;
                buttonHandler(appActions);
            }
        });

        retakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActions appActions = AppActions.CAMERA;
                buttonHandler(appActions);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActions appActions = AppActions.SEND_PICTURE_TO_SERVER;
                buttonHandler(appActions);
            }
        });

    }

    private void displayPictureTaken(){
        AppData appData = AppData.getAppDataInstance();
        pictureTaken.setImageBitmap(appData.getCroppedImage());
    }


    private void buttonHandler(AppActions appActions){
        switch (appActions){
            case CANCEL:
                startHomeActivity();
                break;
            case CAMERA:
                startCameraActivity();
                break;
            case SEND_PICTURE_TO_SERVER:
                sendDataToServer();
                break;
        }
    }


    private void startHomeActivity(){
        Intent homeIntent = new Intent(this, MainActivity.class);
        startActivity(homeIntent);
    }

    private void startCameraActivity(){
        Intent cameraIntent = new Intent(this, CameraActivity.class);
        startActivity(cameraIntent);
    }

    private void sendDataToServer(){

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_validation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
