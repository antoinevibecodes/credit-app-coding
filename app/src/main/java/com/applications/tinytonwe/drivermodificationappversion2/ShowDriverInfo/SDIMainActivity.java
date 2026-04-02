package com.applications.tinytonwe.drivermodificationappversion2.ShowDriverInfo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.Camera.CameraActivity;
import com.applications.tinytonwe.drivermodificationappversion2.DriverInfoApp.DriverInfoActivity;
import com.applications.tinytonwe.drivermodificationappversion2.R;

public class SDIMainActivity extends AppCompatActivity implements RecyclerViewClickListener{

    private String firstName_;
    private String lastName_;

    private CardView driverData_;
    private RecyclerView drivingHistory_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdimain);

        initialize();
        registerListeners();

        drivingHistory_.setVisibility(View.GONE);
        driverData_.setVisibility(View.VISIBLE);

    }

    private void registerListeners(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle(firstName_ + " " + lastName_);
        toolbar.setNavigationIcon(this.getResources().getDrawable(R.drawable.ic_arrow_back_black_36dp));
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        driverData_ = (CardView)findViewById(R.id.cardDataTemplate);

        TestApiDrivingHistory.getDrivingHistory();
        drivingHistory_ = (RecyclerView)findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.Adapter adapter = new RecyclerViewAdapterDrivingHistory(AppData.getAppDataInstance_().getDrivingHistory(),this);

        drivingHistory_.setLayoutManager(layoutManager);
        drivingHistory_.setAdapter(adapter);

        ImageButton credits = (ImageButton)findViewById(R.id.credits);
        ImageButton history = (ImageButton)findViewById(R.id.history);
        ImageButton camera = (ImageButton)findViewById(R.id.camera);

        credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(SDIActions.INFO);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(SDIActions.HISTORY);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHandler(SDIActions.CAMERA);
            }
        });
    }

    private void initialize(){
        TextView driverId = (TextView)findViewById(R.id.driverIdValue);
        TextView firstName = (TextView)findViewById(R.id.firstNameValue);
        TextView lastName = (TextView)findViewById(R.id.lastNameValue);
        TextView dobValue = (TextView)findViewById(R.id.dobValue);
        ImageView driverImage = (ImageView)findViewById(R.id.driverImage);


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

    }

    private void buttonHandler(SDIActions sdiActions){
        switch (sdiActions){
            case HISTORY:
                showHistory();
                break;
            case INFO:
                showInfo();
                break;
            case CAMERA:
                startActivity(new Intent(this, CameraActivity.class));
                break;
        }
    }


    private void showInfo(){
        //Hide driving history
        drivingHistory_.setVisibility(View.GONE);
        driverData_.setVisibility(View.VISIBLE);
    }


    private void showHistory(){
        driverData_.setVisibility(View.GONE);
        drivingHistory_.setVisibility(View.VISIBLE);
    }

    public void onBackPressed(){
        this.finish();
        startActivity(new Intent(this, DriverInfoActivity.class));
    }

    public void recyclerViewListClicked(int position){
        Intent intent = new Intent(this,SDIDrivingSessionActivity.class);
        intent.putExtra("sessionId",position);
        startActivity(intent);
        //Toast.makeText(this, "card at position " + position + " clicked", Toast.LENGTH_LONG).show();
    }

}
