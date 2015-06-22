package com.applications.tinytonwe.drivermodificationappversion2;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TestServer;


public class MainActivity extends AppCompatActivity implements TaskListener{

    private Toolbar toolbar;
    private ProgressBar progressBar;

    private FrameLayout contentLayout;
    private CardView driverData;

    private String cardReadByNFC = "";
    private String prevCardReadByNFC = "";

    private TestServer server;

    private TextView driverId;
    private TextView firstName;
    private TextView lastName;
    private TextView dobValue;
    private ImageButton cameraBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        server = new TestServer(this);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("App Hub");
        setSupportActionBar(toolbar);

        driverData = (CardView)findViewById(R.id.cardDataTemplate);

        driverId = (TextView)findViewById(R.id.driverIdValue);
        firstName = (TextView)findViewById(R.id.firstNameValue);
        lastName = (TextView)findViewById(R.id.lastNameValue);
        dobValue = (TextView)findViewById(R.id.dobValue);
        cameraBtn = (ImageButton)findViewById(R.id.cameraBtn);


    }




    public void onResume() {

        super.onResume();

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
                cardReadByNFC = b3X + b2X + b1X + b0X;

                //To avoid intent firing with previous values when device wakes from sleep
                if(prevCardReadByNFC.equals(cardReadByNFC)) {
                    //resetAll();
                    return;
                }
                prevCardReadByNFC =cardReadByNFC;

                long driverId = convertIdToLong(cardReadByNFC);
                server.setDriverRfId(driverId);
                startServerQuery();

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

    public void startServerQuery(){
        requestStarted();
        server.start();
    }

    private void requestStarted(){
        disableAllViews(true);
        showWaitDialog(true);
    }

    private void requestEnded(){
        disableAllViews(false);
        showWaitDialog(false);
    }
    private void disableAllViews(boolean value){
        if(value){
            driverData.setVisibility(View.INVISIBLE);
            cameraBtn.setVisibility(View.INVISIBLE);
            cameraBtn.setEnabled(false);
        }
        else{
            driverData.setVisibility(View.VISIBLE);
            cameraBtn.setVisibility(View.VISIBLE);
            cameraBtn.setEnabled(true);
        }
    }

    private void showWaitDialog(boolean value){
        if(value)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onTaskFinished(Response response){


        String id = response.driverId + "";
        driverId.setText(id);

        String fName = response.firstName;
        firstName.setText(fName);

        String lName = response.lastName;
        lastName.setText(lName);

        String dob = response.dateOfBirth;
        dobValue.setText(dob);

        requestEnded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
