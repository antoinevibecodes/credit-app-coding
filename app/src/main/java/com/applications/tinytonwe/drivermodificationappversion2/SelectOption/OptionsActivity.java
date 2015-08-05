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
import com.applications.tinytonwe.drivermodificationappversion2.ProcessCredits.ProcessCreditsActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.EntitlementTypes;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.RealServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;

public class OptionsActivity extends AppCompatActivity {

    private Context context_;
    private String firstName_;
    private String lastName_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        context_ = this;

        initialize();
        registerListeners();
    }

    private void registerListeners(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle(firstName_ + " " + lastName_);
        toolbar.setNavigationIcon(this.getResources().getDrawable(R.drawable.ic_arrow_back_black_36dp));
        toolbar.inflateMenu(R.menu.menu_options);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initialize(){
        TextView driverId = (TextView)findViewById(R.id.driverIdValue);
        TextView firstName = (TextView)findViewById(R.id.firstNameValue);
        TextView lastName = (TextView)findViewById(R.id.lastNameValue);
        TextView dobValue = (TextView)findViewById(R.id.dobValue);
        ImageView driverImage = (ImageView)findViewById(R.id.driverImage);

        TextView totalCredits = (TextView)findViewById(R.id.totalCredits);
        TextView generalCredits = (TextView)findViewById(R.id.generalCredits);
        TextView foodCredits = (TextView)findViewById(R.id.foodCredits);
        TextView trafficTrackCredits = (TextView)findViewById(R.id.trafficTrackCredits);
        TextView tinyTrackCredits = (TextView)findViewById(R.id.tinyCredits);
        TextView trainCredits = (TextView)findViewById(R.id.trainCredits);
        TextView arcadeCredits = (TextView)findViewById(R.id.arcadeCredits);

        AppData appData = AppData.getAppDataInstance_();

        firstName_ = appData.getDriverFirstName();
        lastName_ = appData.getDriverLastName();

        firstName.setText(firstName_);
        lastName.setText(lastName_);

        driverId.setText(Long.toString(appData.getDriverId_()));
        firstName.setText(appData.getDriverFirstName());
        lastName.setText(appData.getDriverLastName());
        dobValue.setText(appData.getDob());
        driverImage.setImageBitmap(appData.getDriverImage());

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
                processEntitlementRequest(EntitlementTypes.TRAIN, false);
                return true;
            case R.id.trainForce:
                processEntitlementRequest(EntitlementTypes.TRAIN, true);
                return true;
            case R.id.track:
                processEntitlementRequest(EntitlementTypes.TRAFFICTRACK, false);
                return true;
            case R.id.trackForce:
                processEntitlementRequest(EntitlementTypes.TRAFFICTRACK, true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void processEntitlementRequest(EntitlementTypes entitlementType, boolean force){

        Intent intent = new Intent(this, ProcessCreditsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("force", force);
        bundle.putInt("entitlementType", entitlementType.getEnumValue());
        bundle.putString("firstName",firstName_);
        bundle.putString("lastName", lastName_);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    public void onBackPressed(){
        this.finish();
        startActivity(new Intent(this,MainActivity.class));
    }

}
