package com.applications.tinytonwe.drivermodificationappversion2.Validation;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.applications.tinytonwe.cameralibrary.SingletonCameraData;
import com.applications.tinytonwe.drivermodificationappversion2.AppData.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.R;

public class ValidationActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ProgressBar progressBar;

    private ImageButton cancelBtn;
    private ImageButton cameraBtn;
    private ImageButton sendBtn;


    private CardView pictureCard;
    private FrameLayout pictureFrame;
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

        cancelBtn = (ImageButton)findViewById(R.id.cancelBtn);
        cameraBtn = (ImageButton)findViewById(R.id.cameraBtn);
        sendBtn = (ImageButton)findViewById(R.id.sendBtn);

        pictureCard = (CardView)findViewById(R.id.cardPictureTemplate);
        pictureFrame = (FrameLayout)findViewById(R.id.pictureFrame);
        pictureTaken = new ImageView(this);
    }

    private void displayPictureTaken(){
        AppData appData = AppData.getAppDataInstance();

        pictureTaken.setImageBitmap(appData.getCroppedImage());

        pictureFrame.removeAllViews();
        pictureFrame.addView(pictureTaken);
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
