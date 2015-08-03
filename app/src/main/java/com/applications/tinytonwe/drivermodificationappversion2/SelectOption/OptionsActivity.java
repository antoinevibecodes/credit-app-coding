package com.applications.tinytonwe.drivermodificationappversion2.SelectOption;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.applications.tinytonwe.drivermodificationappversion2.AppActions;
import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.Camera.CameraActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;

public class OptionsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        registerListeners();
        initialize();
    }

    private void registerListeners(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("Select Option");
        setSupportActionBar(toolbar);

        ImageButton cameraBtn_ = (ImageButton)findViewById(R.id.cameraBtn);

        cameraBtn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(AppActions.CAMERA);
            }
        });
    }

    private void initialize(){
        TextView driverId_ = (TextView)findViewById(R.id.driverIdValue);
        TextView firstName_ = (TextView)findViewById(R.id.firstNameValue);
        TextView lastName_ = (TextView)findViewById(R.id.lastNameValue);
        TextView dobValue_ = (TextView)findViewById(R.id.dobValue);
        ImageView driverImage_ = (ImageView)findViewById(R.id.driverImage);

        AppData appData = AppData.getAppDataInstance_();

        driverId_.setText(Long.toString(appData.getDriverId_()));
        firstName_.setText(appData.getDriverFirstName());
        lastName_.setText(appData.getDriverLastName());
        dobValue_.setText(appData.getDob());
        driverImage_.setImageBitmap(appData.getDriverImage());

    }

    private void buttonHandler(AppActions appActions){

        switch (appActions){
            case CAMERA:
                startActivity(new Intent(this, CameraActivity.class));
                break;
        }
    }
}
