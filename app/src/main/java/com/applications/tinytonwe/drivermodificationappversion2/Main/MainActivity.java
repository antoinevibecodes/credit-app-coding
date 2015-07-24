package com.applications.tinytonwe.drivermodificationappversion2.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.applications.tinytonwe.drivermodificationappversion2.AppActions;
import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.Camera.CameraActivity;
import com.applications.tinytonwe.drivermodificationappversion2.DisplayErrorMessages;
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.RealServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;


public class MainActivity extends AppCompatActivity implements TaskListener{


    private ProgressBar progressBar_;
    private LinearLayout waitLayout_;
    private TextView errorMessage_;

    private String cardReadByNFC_ = "";
    private String prevCardReadByNFC_ = "";

    private RealServer server_;

    private TextView driverId_;
    private TextView firstName_;
    private TextView lastName_;
    private TextView dobValue_;
    private ImageView driverImage_;

    private LinearLayout promptCardsLayout_;
    private LinearLayout errorCardLayout_;
    private LinearLayout layoutContent_;

    private final int QUERY_RFID = 0;
    private final int QUERY_DRIVERID = 1;

    private AppData appData_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeActivity();
        registerListeners();
    }

    private void initializeActivity(){
        appData_ = AppData.getAppDataInstance_();
        appData_.reset();
    }

    private void registerListeners(){

        promptCardsLayout_ = (LinearLayout)findViewById(R.id.promptCardsLayout);
        layoutContent_ = (LinearLayout)findViewById(R.id.layoutContent);
        waitLayout_ = (LinearLayout)findViewById(R.id.waitLayout);

        Toolbar toolbar_ = (Toolbar)findViewById(R.id.tool_bar);
        toolbar_.setTitle("Collect TinyTowne Driver ID");

        CardView errorCard = (CardView)findViewById(R.id.errorCard);
        errorCard.setVisibility(View.VISIBLE);
        errorCardLayout_ = (LinearLayout)findViewById(R.id.errorCardLayout);
        errorCardLayout_.setVisibility(View.GONE);
        errorMessage_ = (TextView)findViewById(R.id.errorMessage);

        progressBar_ = (ProgressBar)findViewById(R.id.progressbar);
        progressBar_.getIndeterminateDrawable().setColorFilter(0xFFFFC000, PorterDuff.Mode.SRC_ATOP);

        setSupportActionBar(toolbar_);
        CardView driverData = (CardView)findViewById(R.id.cardDataTemplate);

        driverId_ = (TextView)findViewById(R.id.driverIdValue);
        firstName_ = (TextView)findViewById(R.id.firstNameValue);
        lastName_ = (TextView)findViewById(R.id.lastNameValue);
        dobValue_ = (TextView)findViewById(R.id.dobValue);
        driverImage_ = (ImageView)findViewById(R.id.driverImage);

        ImageButton cameraBtn_ = (ImageButton)findViewById(R.id.cameraBtn);

        cameraBtn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(AppActions.CAMERA);
            }
        });

        Button sendDriverIdBtn = (Button)findViewById(R.id.sendDriverId);

        sendDriverIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(AppActions.PROCESS_ID_ENTERED);
            }
        });

        ImageButton backBtn = (ImageButton)findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(AppActions.BACK_TO_HOME_SCREEN);
            }
        });
    }

    private void buttonHandler(AppActions appActions){

        switch (appActions){
            case CAMERA:
                startActivity(new Intent(this, CameraActivity.class));
                break;
            case PROCESS_ID_ENTERED:
                processDriverId();
                break;
            case BACK_TO_HOME_SCREEN:
                this.recreate();
                break;
        }
    }


    private void processDriverId() {

        EditText driverIdString = (EditText)findViewById(R.id.driverID);

        //Dismissing the keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(driverIdString.getWindowToken(), 0);


        try{
            long driverId = Long.parseLong(driverIdString.getText().toString());
            appData_.setDriverId(driverId);
            startServerRequest(QUERY_DRIVERID);
        }
        catch (Exception e){
            //Invalid Id entered
            displayErrorMessage(MErrors.INVALID_ID_ENTERED);
        }
    }


    public void displayErrorMessage(MErrors sErrors){

        String message = "";
        switch (sErrors){
            case INVALID_ID_ENTERED:
                message = "Enter digits only";
                break;
        }

        DisplayErrorMessages.displayError(this, message);

    }


    public void onResume() {

        super.onResume();
        AppData.getAppDataInstance_().reset();

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
                startServerRequest(QUERY_RFID);

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


    private void prepareRequest(){
        disableContentAndPromptViews();
        showWaitDialog(true);
    }

    private void startServerRequest(int queryType){
        prepareRequest();
        server_ = new RealServer(this);
        RealServer.GetDriverInformation getDriverInformation;

        switch (queryType) {
            case QUERY_RFID:
                getDriverInformation = server_.new GetDriverInformation(true);
                break;
            case QUERY_DRIVERID:
                getDriverInformation = server_.new GetDriverInformation(false);
                break;
        }
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
        errorCardLayout_.setVisibility(View.VISIBLE);
    }

    private void disableContentAndPromptViews(){
            errorCardLayout_.setVisibility(View.GONE);
            layoutContent_.setVisibility(View.GONE);
            promptCardsLayout_.setVisibility(View.GONE);
    }

    private void enableContentView(){
        layoutContent_.setVisibility(View.VISIBLE);
    }

    private void showWaitDialog(boolean value){
        if(value)
            waitLayout_.setVisibility(View.VISIBLE);
        else
            waitLayout_.setVisibility(View.GONE);
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
