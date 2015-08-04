package com.applications.tinytonwe.drivermodificationappversion2.SelectOption;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.applications.tinytonwe.drivermodificationappversion2.AppActions;
import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.Camera.CameraActivity;
import com.applications.tinytonwe.drivermodificationappversion2.Main.MainActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;

public class OptionsActivity extends AppCompatActivity {

    private Context context_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        context_ = this;

        registerListeners();
        initialize();
    }

    private void registerListeners(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("Select Option");
        toolbar.setNavigationIcon(this.getResources().getDrawable(R.drawable.ic_arrow_back_black_36dp));
        toolbar.inflateMenu(R.menu.menu_options);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initialize(){
        TextView driverId_ = (TextView)findViewById(R.id.driverIdValue);
        TextView firstName_ = (TextView)findViewById(R.id.firstNameValue);
        TextView lastName_ = (TextView)findViewById(R.id.lastNameValue);
        TextView dobValue_ = (TextView)findViewById(R.id.dobValue);
        ImageView driverImage_ = (ImageView)findViewById(R.id.driverImage);

        TextView totalCredits = (TextView)findViewById(R.id.totalCredits);
        TextView generalCredits = (TextView)findViewById(R.id.generalCredits);
        TextView foodCredits = (TextView)findViewById(R.id.foodCredits);
        TextView trafficTrackCredits = (TextView)findViewById(R.id.trafficTrackCredits);
        TextView tinyTrackCredits = (TextView)findViewById(R.id.tinyCredits);
        TextView trainCredits = (TextView)findViewById(R.id.trainCredits);
        TextView arcadeCredits = (TextView)findViewById(R.id.arcadeCredits);

        AppData appData = AppData.getAppDataInstance_();

        driverId_.setText(Long.toString(appData.getDriverId_()));
        firstName_.setText(appData.getDriverFirstName());
        lastName_.setText(appData.getDriverLastName());
        dobValue_.setText(appData.getDob());
        driverImage_.setImageBitmap(appData.getDriverImage());

        totalCredits.setText(appData.getTotalCredits());
        generalCredits.setText(appData.getGeneralCredits());
        foodCredits.setText(appData.getFoodCredits());
        trafficTrackCredits.setText(appData.getTrafficTrackCredits());
        tinyTrackCredits.setText(appData.getTinyTrackCredits());
        trainCredits.setText(appData.getTrainCredits());
        arcadeCredits.setText(appData.getArcadeCredits());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.camera:
                startActivity(new Intent(this, CameraActivity.class));
                return true;
            case R.id.cameraOverflow:
                startActivity(new Intent(this, CameraActivity.class));
                return true;
            case R.id.train:
                return true;
            case R.id.trainForce:
                return true;
            case R.id.track:
                return true;
            case R.id.trackForce:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
