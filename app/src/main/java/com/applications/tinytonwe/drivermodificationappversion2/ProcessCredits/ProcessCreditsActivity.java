package com.applications.tinytonwe.drivermodificationappversion2.ProcessCredits;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applications.tinytonwe.drivermodificationappversion2.R;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.RealServer;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.Response;
import com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication.TaskListener;

public class ProcessCreditsActivity extends AppCompatActivity implements TaskListener {


    private LinearLayout waitLayout;
    private CardView successCard;
    private CardView errorCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_credits);

        waitLayout = (LinearLayout)findViewById(R.id.waitLayout);
        successCard = (CardView)findViewById(R.id.successCard);
        errorCard = (CardView)findViewById(R.id.errorCard);

        Bundle bundle = this.getIntent().getExtras();

        String firstName = bundle.getString("firstName");
        String lastName = bundle.getString("lastName");
        boolean force = bundle.getBoolean("force");
        int entitlementType = bundle.getInt("entitlementType");


        Toolbar toolbar = (Toolbar)findViewById(R.id.process_tool_bar);
        toolbar.setTitle(firstName + " " + lastName);
        toolbar.setNavigationIcon(this.getResources().getDrawable(R.drawable.ic_arrow_back_black_36dp));
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boolean checkCredits = false;
        boolean useDriverId = true;

        RealServer server_ = new RealServer(this);
        RealServer.ChargeCard chargeCard;
        chargeCard = server_.new ChargeCard(entitlementType,force,checkCredits,useDriverId);
    }



    public void onTaskFinished(Response response){

        waitLayout.setVisibility(View.GONE);

        if(response.responseOk){
            TextView success = (TextView)findViewById(R.id.successMessage);
            success.setText(response.responseMessage);
            successCard.setVisibility(View.VISIBLE);
        }
        else {
            TextView error = (TextView)findViewById(R.id.errorMessage);
            error.setText(response.responseMessage);
            errorCard.setVisibility(View.VISIBLE);
        }
    }
}
