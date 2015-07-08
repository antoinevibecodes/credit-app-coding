package com.applications.tinytonwe.drivermodificationappversion2.Main;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
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
import android.widget.Toast;

import com.applications.tinytonwe.drivermodificationappversion2.AppActions;
import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.Camera.CameraActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.RealServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;


public class MainActivity extends AppCompatActivity implements TaskListener{

    private Toolbar toolbar_;
    private ProgressBar progressBar_;
    private CardView errorCard_;
    private TextView errorMessage_;

    private String cardReadByNFC_ = "";
    private String prevCardReadByNFC_ = "";

    private RealServer server_;

    private TextView driverId_;
    private TextView firstName_;
    private TextView lastName_;
    private TextView dobValue_;
    private ImageView driverImage_;
    private ImageButton cameraBtn_;

    private CardView promptCard_;
    private LinearLayout layoutContent_;

    private AppData appData_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeVariables();
        registerListeners();
    }

    private void initializeVariables(){
        appData_ = AppData.getAppDataInstance_();
        appData_.reset();
    }

    private void registerListeners(){

        promptCard_ = (CardView)findViewById(R.id.promptCard);
        layoutContent_ = (LinearLayout)findViewById(R.id.layoutContent);

        toolbar_ = (Toolbar)findViewById(R.id.tool_bar);
        toolbar_.setTitle("App Hub");

        errorCard_ = (CardView)findViewById(R.id.errorCard);
        errorMessage_ = (TextView)findViewById(R.id.errorMessage);

        progressBar_ = (ProgressBar)findViewById(R.id.progressbar);
        setSupportActionBar(toolbar_);
        CardView driverData = (CardView)findViewById(R.id.cardDataTemplate);
        driverId_ = (TextView)findViewById(R.id.driverIdValue);
        firstName_ = (TextView)findViewById(R.id.firstNameValue);
        lastName_ = (TextView)findViewById(R.id.lastNameValue);
        dobValue_ = (TextView)findViewById(R.id.dobValue);
        driverImage_ = (ImageView)findViewById(R.id.driverImage);
        cameraBtn_ = (ImageButton)findViewById(R.id.cameraBtn);


        cameraBtn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActions appActions = AppActions.CAMERA;
                buttonHandler(appActions);
            }
        });
    }

    private void buttonHandler(AppActions appActions){

        switch (appActions){
            case CAMERA:
                startCameraActivity();
                break;
        }
    }

    private void startCameraActivity(){
        Intent cameraIntent = new Intent(this, CameraActivity.class);
        startActivity(cameraIntent);
    }

    public void onResume() {

        super.onResume();
        appData_.reset();

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
            //get the tag id
            Intent curIntent = getIntent();
            Tag tagFromIntent = curIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            try
            {
                byte[] tagUid = tagFromIntent.getId();

                String b0X = String.format("%02x", tagUid[0] & 0xff);
                String b1X = String.format("%02x", tagUid[1] & 0xff);
                String b2X = String.format("%02x", tagUid[2] & 0xff);
                String b3X = String.format("%02x", tagUid[3] & 0xff);

                //set global variable
                cardReadByNFC_ = b3X + b2X + b1X + b0X;

                //To avoid intent firing with previous values when device wakes from sleep
                if(prevCardReadByNFC_.equals(cardReadByNFC_)) {
                    //resetAll();
                    return;
                }
                prevCardReadByNFC_ = cardReadByNFC_;

                long driverId = convertIdToLong(cardReadByNFC_);
                appData_.setCardIdReadStringValue_(cardReadByNFC_);
                appData_.setCardIdReadLongValue_(driverId);
                queryServer();

            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }//end try catch
        }
    }

    private  long convertIdToLong(String hexId){
        return (Long.parseLong(hexId,16));
    }

    private void queryServer(){
        prepareRequest();
        startRequest();
    }

    private void prepareRequest(){
        disableContentAndPromptViews();
        showWaitDialog(true);
    }

    private void startRequest(){
        server_ = new RealServer(this);
        RealServer.GetDriverInformation getDriverInformation =
                server_.new GetDriverInformation();
    }

    private void requestEndedResponseOk(){

        driverId_.setText(Long.toString(appData_.getDriverId_()));
        firstName_.setText(appData_.getDriverFirstName());
        lastName_.setText(appData_.getDriverLastName());
        dobValue_.setText(appData_.getDob());
        driverImage_.setImageBitmap(appData_.getDriverImage());

        enableContentView();
    }

    private void requestEndedResponseError(String errorMessage){
        //Show error card prompting to retry
        errorMessage_.setText(errorMessage);
        errorCard_.setVisibility(View.VISIBLE);
    }

    private void disableContentAndPromptViews(){
            errorCard_.setVisibility(View.GONE);
            layoutContent_.setVisibility(View.GONE);
            promptCard_.setVisibility(View.GONE);
    }

    private void enableContentView(){
        layoutContent_.setVisibility(View.VISIBLE);
    }

    private void showWaitDialog(boolean value){
        if(value)
            progressBar_.setVisibility(View.VISIBLE);
        else
            progressBar_.setVisibility(View.GONE);
    }
    @Override
    public void onTaskFinished(Response response){

        showWaitDialog(false);

        if(response.responseOk)
            requestEndedResponseOk();
        else
            requestEndedResponseError(response.responseMessage);

    }
}
