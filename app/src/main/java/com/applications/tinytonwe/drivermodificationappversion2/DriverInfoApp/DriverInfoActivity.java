package com.applications.tinytonwe.drivermodificationappversion2.DriverInfoApp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.applications.tinytonwe.drivermodificationappversion2.AppActions;
import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.Camera.CameraData;
import com.applications.tinytonwe.drivermodificationappversion2.DisplayErrorMessages;
import com.applications.tinytonwe.drivermodificationappversion2.Main.MainActivity;
import com.applications.tinytonwe.drivermodificationappversion2.ShowDriverInfo.SDIMainActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.RealServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;


public class DriverInfoActivity extends AppCompatActivity implements TaskListener{


    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;


    private ProgressBar progressBar_;
    private LinearLayout waitLayout_;
    private TextView errorMessage_;

    private String cardReadByNFC_ = "";
    private String prevCardReadByNFC_ = "";

    private LinearLayout promptCardsLayout_;
    private EditText driverIdString;
    private LinearLayout errorCardLayout_;

    private final int QUERY_RFID = 0;
    private final int QUERY_DRIVERID = 1;

    private AppData appData_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_info);

        initialize();
        registerListeners();
    }

    private void initialize(){
        mAdapter = NfcAdapter.getDefaultAdapter(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mPendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE);
        }else{
            mPendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT);

        }

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[] {
                ndef,
        };

        // Setup a tech list for all NfcF tags
        mTechLists = new String[][] { new String[] { NfcA.class.getName() } };


        appData_ = AppData.getAppDataInstance_();
        appData_.reset();
    }

    private void registerListeners(){

        promptCardsLayout_ = (LinearLayout)findViewById(R.id.promptCardsLayout);
        driverIdString = (EditText)findViewById(R.id.driverID);
        waitLayout_ = (LinearLayout)findViewById(R.id.waitLayout);

        Toolbar toolbar_ = (Toolbar)findViewById(R.id.tool_bar);
        toolbar_.setTitle("Driver Information");

        CardView errorCard = (CardView)findViewById(R.id.errorCard);
        errorCard.setVisibility(View.VISIBLE);
        errorCardLayout_ = (LinearLayout)findViewById(R.id.errorCardLayout);
        errorCardLayout_.setVisibility(View.GONE);
        errorMessage_ = (TextView)findViewById(R.id.errorMessage);

        progressBar_ = (ProgressBar)findViewById(R.id.progressbar);
        progressBar_.getIndeterminateDrawable().setColorFilter(0xFFFFC000, PorterDuff.Mode.SRC_ATOP);

        setSupportActionBar(toolbar_);

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
            case PROCESS_ID_ENTERED:
                processDriverId();
                break;
            case BACK_TO_HOME_SCREEN:
                this.finish();
                startActivity(new Intent(this,DriverInfoActivity.class));
                break;
        }
    }


    private void processDriverId() {
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
            displayErrorMessage(DIErrors.INVALID_ID_ENTERED);
        }
    }


    public void displayErrorMessage(DIErrors sErrors){

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
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        resetApplication();

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
        RealServer server_ = new RealServer(this);
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

    private void resetApplication(){
        AppData.getAppDataInstance_().reset();
        CameraData.getInstance().reset();
        driverIdString.setText("");
        promptCardsLayout_.setVisibility(View.VISIBLE);
        errorCardLayout_.setVisibility(View.GONE);
        showWaitDialog(false);
    }

    private void requestEndedResponseOk(){
        startActivity(new Intent(this, SDIMainActivity.class));
    }

    private void requestEndedResponseError(String errorMessage){
        //Show error card prompting to retry
        errorMessage_.setText(errorMessage);
        errorCardLayout_.setVisibility(View.VISIBLE);
    }

    private void disableContentAndPromptViews(){
            errorCardLayout_.setVisibility(View.GONE);
            promptCardsLayout_.setVisibility(View.GONE);
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

    public void onNewIntent(Intent intent) {

        //I set the launch mode to singleTask in the manifest
        //so this is why I am able to intecept a new instace
        //of the activity which wishes to be created in this method


        //I clear the stack of the current activity which wishes to be
        //created so that android will permit its creation since I set
        //my lauch mode to singeTask
        super.onNewIntent(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        //I can now start the intent as I am sure all its old instances
        // will first be destroyed
        startActivity(intent);
    }

    public void onBackPressed(){
        this.finish();
        startActivity(new Intent(this, MainActivity.class));
    }

}
