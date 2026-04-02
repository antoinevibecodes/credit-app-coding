package com.applications.tinytonwe.drivermodificationappversion2.Main;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.applications.tinytonwe.drivermodificationappversion2.ChargeCardApp.ChargeCardActivity;
import com.applications.tinytonwe.drivermodificationappversion2.DriverInfoApp.DriverInfoActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;

public class MainActivity extends AppCompatActivity {



    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        registerListener();
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
    }

    private void registerListener(){

        Toolbar toolbar_ = (Toolbar)findViewById(R.id.tool_bar);
        toolbar_.setTitle("Select Application");

        CardView chargeCard = (CardView)findViewById(R.id.chargeCard);
        CardView driverInfo = (CardView)findViewById(R.id.driverInfo);


        chargeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(MActions.CHARGE_CARD);
            }
        });

        driverInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(MActions.DRIVER_INFO);
            }
        });
    }


    private void buttonHandler(MActions mActions){
        switch (mActions){
            case CHARGE_CARD:
                startActivity(new Intent(this, ChargeCardActivity.class));
                break;
            case DRIVER_INFO:
                startActivity(new Intent(this, DriverInfoActivity.class));
                break;
        }
    }

    public void onResume() {

        super.onResume();
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
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
}
