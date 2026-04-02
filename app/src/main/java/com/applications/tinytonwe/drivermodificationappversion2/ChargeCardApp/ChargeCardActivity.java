package com.applications.tinytonwe.drivermodificationappversion2.ChargeCardApp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.CountDownTimer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.Main.MainActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.EntitlementTypes;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.RealServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;

public class ChargeCardActivity extends AppCompatActivity implements TaskListener{


    private AppData appData;

    //TextView for display
    public TextView tv;

    //Global variables used throught
    private Activity activity = this;
    private String promptMessage;
    private String waitMessage;
    private LinearLayout linearLayout;
    private String cardReadByNFC = "";
    private String prevCardReadByNFC = "";
    private String defaultColor =  "#1d1d1d";


    //UI buttons
    private Button trackBn;
    private Button trackForceBn;
    private Button trainBn;
    private Button trainForceBn;
    private Button creditsBn;
    private Button cancelBn;

    //CountDown timer used to set, reset and cancel timeout throughout program
    private CountDownTimer countDownTimer;


    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private TextView mText;
    private int mCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_card);

        initialize();
        registerListeners();
    }


    private void initialize(){

        mAdapter = NfcAdapter.getDefaultAdapter(this);

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE);


        // Setup an intent filter for all MIME based dispatches
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

        appData = AppData.getAppDataInstance_();

        promptMessage = getApplicationContext().getResources().getText(R.string.promptMessage).toString();
        waitMessage = getApplicationContext().getResources().getText(R.string.waitMessage).toString();

        //Setting default layout
        linearLayout = (LinearLayout)findViewById(R.id.layout);
        linearLayout.setBackgroundColor(Color.parseColor(defaultColor));

    }

    private void registerListeners(){

        tv = (TextView) findViewById(R.id.textView);
        trackBn = (Button) findViewById(R.id.track);
        trackForceBn = (Button) findViewById(R.id.trackForce);
        trainBn = (Button) findViewById(R.id.train);
        trainForceBn = (Button) findViewById(R.id.trainForce);
        creditsBn = (Button) findViewById(R.id.credits);
        cancelBn = (Button) findViewById(R.id.cancel);


        //Handling our click events

        //Handler for Track button
        trackBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(CCActions.TRACK);
            }
        });

        //Handler for track force button
        trackForceBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(CCActions.TRACK_FORCE);
            }
        });

        //Handler for train button
        trainBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(CCActions.TRAIN);
            }
        });

        //Handler for train force button
        trainForceBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(CCActions.TRAIN_FORCE);
            }
        });

        //Handler for credits button
        creditsBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(CCActions.CREDITS);
            }
        });

        //Handler for cancel button
        cancelBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetApp();
            }
        });
    }

    public void onResume() {

        super.onResume();
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);

        //Prompt the user to make the card touch the device, disable buttons and erase any existing data
        resetApp();


        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {

            //Once the card has been read, start the timeout
            setInactivityTimeout();

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
                cardReadByNFC = b3X + b2X + b1X + b0X;

                //To avoid intent firing with previous values when device wakes from sleep
                if(prevCardReadByNFC.equals(cardReadByNFC)) {
                    linearLayout.setBackgroundColor(Color.parseColor(defaultColor));
                    tv.setTextColor(Color.parseColor(defaultColor));
                    tv.setBackgroundColor(Color.WHITE);
                    resetApp();
                    return;
                }

                prevCardReadByNFC =cardReadByNFC;

                //update display with card info read
                tv.setText("Card UID read : " + cardReadByNFC + "\n" + "Please select an option");

                //Make all options available
                enableAllButtons();
            }
            catch(Exception ex)
            {
                tv.setText("Error Reading card");
            }//end try catch
        }
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

    private  long convertIdToLong(String hexId){
        return (Long.parseLong(hexId,16));
    }

    private void buttonHandler(CCActions ccActions){
        //Cancel the timeout when a user clicks on a button
        cancelInactivityTimeout();

        //Initialize all local variables
        boolean update = true;
        String rfidUidS = cardReadByNFC.trim();
        long rfidUidL = convertIdToLong(rfidUidS);
        appData.setCardIdReadLongValue_(rfidUidL);

        if(!(cardReadByNFC.equals(""))) {
            boolean checkCredits = false;
            boolean forceCredits = false;
            boolean useDriverId = false;
            EntitlementTypes entitlementType = EntitlementTypes.GENERAL;


            switch (ccActions) {
                case TRACK:
                    entitlementType = EntitlementTypes.TINYTRACK;
                    break;
                case TRACK_FORCE:
                    entitlementType = EntitlementTypes.TINYTRACK;
                    forceCredits = true;
                    break;
                case TRAIN:
                    entitlementType = EntitlementTypes.TRAIN;
                    break;
                case TRAIN_FORCE:
                    entitlementType = EntitlementTypes.TRAIN;
                    forceCredits = true;
                    break;
                case CREDITS:
                    entitlementType = EntitlementTypes.GENERAL;
                    checkCredits = true;
                    break;
            }

            RealServer server_ = new RealServer(this);
            RealServer.ChargeCard chargeCard;
            chargeCard = server_.new ChargeCard(entitlementType.getEnumValue(),forceCredits,checkCredits,useDriverId);

            //Disabling all the buttons during the server call
            disableAllButtons();

            //Displaying wait message
            tv.setText(waitMessage);

        }


    }

    private void setInactivityTimeout(){

        int timeoutDurationInMilli = Integer.parseInt(this.getResources().getText(R.string.inactivityTimeoutDuration).toString());
        int timeoutIntervalInMilli = Integer.parseInt(this.getResources().getText(R.string.timeoutInterval).toString());

        countDownTimer = new CountDownTimer(timeoutDurationInMilli,timeoutIntervalInMilli) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Do nothing just tick
            }

            @Override
            public void onFinish() {
                resetApp();
            }
        }.start();

    }

    private void setSuccessTimeout(){

        int timeoutDurationInMilli = Integer.parseInt(this.getResources().getText(R.string.successTimerDuration).toString());
        int timeoutIntervalInMilli = Integer.parseInt(this.getResources().getText(R.string.successTimerInterval).toString());

        countDownTimer = new CountDownTimer(timeoutDurationInMilli,timeoutIntervalInMilli) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Do nothing just tick
            }

            @Override
            public void onFinish() {
                resetApp();
            }
        }.start();

    }

    private void cancelInactivityTimeout(){

        if(countDownTimer != null)
            countDownTimer.cancel();

        countDownTimer = null;
    }

    private void disableAllButtons(){

        //disable all buttons
        trackBn.setEnabled(false);
        trackForceBn.setEnabled(false);
        trainBn.setEnabled(false);
        trainForceBn.setEnabled(false);
        creditsBn.setEnabled(false);
        cancelBn.setEnabled(false);

        //Make the text color gray to give disabled look to buttons
        greyOutAllBtnColors();
    }

    private void enableAllButtons(){
        //enable all buttons
        trackBn.setEnabled(true);
        trackForceBn.setEnabled(true);
        trainBn.setEnabled(true);
        trainForceBn.setEnabled(true);
        creditsBn.setEnabled(true);
        cancelBn.setEnabled(true);

        //Brighten button colors to give them an enabled look
        reviveAllBtnColors();
    }

    private void greyOutAllBtnColors(){

        //Set the text color of the buttons to gray
        trackBn.setTextColor(Color.GRAY);
        trackForceBn.setTextColor(Color.GRAY);
        trainBn.setTextColor(Color.GRAY);
        trainForceBn.setTextColor(Color.GRAY);
        creditsBn.setTextColor(Color.GRAY);
        cancelBn.setTextColor(Color.GRAY);
    }

    private void reviveAllBtnColors(){

        //set the text color of buttons to white
        trackBn.setTextColor(Color.WHITE);
        trackForceBn.setTextColor(Color.WHITE);
        trainBn.setTextColor(Color.WHITE);
        trainForceBn.setTextColor(Color.WHITE);
        creditsBn.setTextColor(Color.WHITE);
        cancelBn.setTextColor(Color.WHITE);
    }

    private void DisplayMessage(CCMessages messageType){

        switch (messageType){
            case OK:
                linearLayout.setBackgroundColor(Color.GREEN);
                tv.setTextColor(Color.BLACK);
                tv.setBackgroundColor(Color.GREEN);
                break;
            case ERROR:
                linearLayout.setBackgroundColor(Color.RED);
                tv.setTextColor(Color.BLACK);
                tv.setBackgroundColor(Color.RED);
                cancelBn.setEnabled(true);
                cancelBn.setTextColor(Color.WHITE);
                break;
            case DEFAULT:
                tv.setText(promptMessage);
                linearLayout.setBackgroundColor(Color.parseColor(defaultColor));
                tv.setTextColor(Color.parseColor(defaultColor));
                tv.setBackgroundColor(Color.WHITE);
                break;
        }

    }

    private void resetApp(){
        cardReadByNFC = "";
        disableAllButtons();
        appData.reset();
        DisplayMessage(CCMessages.DEFAULT);
    }



    public void onTaskFinished(Response response){

        tv.setText(response.responseMessage);
        disableAllButtons();

        //In case a positive message was received, paint the background green
        if(response.responseOk) {
            DisplayMessage(CCMessages.OK);
            setSuccessTimeout();
        }
        else{
            DisplayMessage(CCMessages.ERROR);
            cancelBn.setEnabled(true);
            cancelBn.setTextColor(Color.WHITE);
        }

    }


    public void onBackPressed(){
        startActivity(new Intent(this, MainActivity.class));
    }
}
