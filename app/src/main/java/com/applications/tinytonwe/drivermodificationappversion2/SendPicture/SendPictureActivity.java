package com.applications.tinytonwe.drivermodificationappversion2.SendPicture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.applications.tinytonwe.drivermodificationappversion2.AppActions;
import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.Camera.CameraActivity;
import com.applications.tinytonwe.drivermodificationappversion2.Main.MainActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.RealServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;

public class SendPictureActivity extends AppCompatActivity implements TaskListener {

    private ProgressBar progressBar;
    private LinearLayout waitLayout_;

    private ImageView pictureTaken_;
    ImageButton exitBtn;
    ImageButton cameraBtn;
    ImageButton sendBtn;

    private RealServer server_;

    private CardView successCard_;
    private CardView errorCard_;
    private TextView errorMessage_;
    private CardView pictureCard_;

    private Toolbar toolbar_;


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
        waitLayout_ = (LinearLayout)findViewById(R.id.waitLayout);

        contentLayout = (LinearLayout)findViewById(R.id.layoutContent);

        toolbar_ = (Toolbar)findViewById(R.id.tool_bar);
        toolbar_.setTitle("Review & Submit Information");

        pictureTaken_ = (ImageView)findViewById(R.id.pictureTaken);
        pictureCard_ = (CardView)findViewById(R.id.cardPictureTemplate);

        successCard_ = (CardView)findViewById(R.id.successCard);
        errorCard_ = (CardView)findViewById(R.id.errorCard);
        errorMessage_ = (TextView)findViewById(R.id.errorMessage);

        exitBtn = (ImageButton)findViewById(R.id.cancelBtn);
        cameraBtn = (ImageButton)findViewById(R.id.retakeBtn);
        sendBtn = (ImageButton)findViewById(R.id.sendBtn);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActions appActions = AppActions.CANCEL;
                buttonHandler(appActions);
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
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
        AppData appData = AppData.getAppDataInstance_();
        pictureTaken_.setImageBitmap(appData.getCroppedImage_());
    }


    private void buttonHandler(AppActions appActions){
        switch (appActions){
            case CANCEL:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case CAMERA:
                startActivity(new Intent(this, CameraActivity.class));
                break;
            case SEND_PICTURE_TO_SERVER:
                startServerRequest();
                break;
        }
    }


    private void startServerRequest(){
        pictureCard_.setVisibility(View.GONE);
        successCard_.setVisibility(View.GONE);
        errorCard_.setVisibility(View.GONE);
        contentLayout.setVisibility(View.GONE);
        waitLayout_.setVisibility(View.VISIBLE);
        server_ = new RealServer(this);
        RealServer.Upload upload = server_.new Upload();
    }

    public void onTaskFinished(Response response){

        waitLayout_.setVisibility(View.GONE);

        if(response.responseOk)
            requestEndedResponseOk();
        else
            requestEndedResponseError(response.responseMessage);
    }

    private void requestEndedResponseOk(){
        successCard_.setVisibility(View.VISIBLE);
        errorCard_.setVisibility(View.INVISIBLE);
        contentLayout.setVisibility(View.VISIBLE);

        LinearLayout cameraLayout = (LinearLayout)findViewById(R.id.camera_btn_layout);
        LinearLayout sendLayout = (LinearLayout)findViewById(R.id.send_btn_layout);

        cameraLayout.setVisibility(View.GONE);
        sendLayout.setVisibility(View.GONE);
    }

    private void requestEndedResponseError(String errorMessage){
        errorMessage_.setText(errorMessage);
        errorCard_.setVisibility(View.VISIBLE);
        successCard_.setVisibility(View.INVISIBLE);
        contentLayout.setVisibility(View.VISIBLE);
    }
}
