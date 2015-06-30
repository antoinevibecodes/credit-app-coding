package com.applications.tinytonwe.drivermodificationappversion2.Validation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.applications.tinytonwe.drivermodificationappversion2.AppActions;
import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.Camera.CameraActivity;
import com.applications.tinytonwe.drivermodificationappversion2.MainActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TestServer;

public class ValidationActivity extends AppCompatActivity implements TaskListener {

    private Toolbar toolbar;
    private ProgressBar progressBar;

    private ImageButton cancelBtn;
    private ImageButton retakeBtn;
    private ImageButton sendBtn;

    private ImageView pictureTaken;

    private TestServer server;


    private CardView successCard;
    private CardView errorCard;
    private CardView pictureCard;

    private LinearLayout contentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        registerListeners();
        displayPictureTaken();
    }


    private void registerListeners(){
        progressBar = (ProgressBar)findViewById(R.id.progressbar);

        contentLayout = (LinearLayout)findViewById(R.id.layoutContent);

        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("Validation Process");

        pictureTaken = (ImageView)findViewById(R.id.pictureTaken);
        pictureCard = (CardView)findViewById(R.id.cardPictureTemplate);

        successCard = (CardView)findViewById(R.id.successCard);
        errorCard = (CardView)findViewById(R.id.errorCard);

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
        pictureTaken.setImageBitmap(appData.getOriginalImage());
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


        startServerRequest();
        server.start();
    }


    @Override
    public void onTaskFinished(Response response){

        progressBar.setVisibility(View.GONE);

        if(response.serverMessage.equalsIgnoreCase("success"))
            successCard.setVisibility(View.VISIBLE);
        else
            errorCard.setVisibility(View.VISIBLE);

        contentLayout.setVisibility(View.VISIBLE);
    }

    private void startServerRequest(){
        pictureCard.setVisibility(View.GONE);
        successCard.setVisibility(View.GONE);
        errorCard.setVisibility(View.GONE);
        contentLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        server = new TestServer(this,2);
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
