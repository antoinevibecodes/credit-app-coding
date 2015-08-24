package com.applications.tinytonwe.drivermodificationappversion2.ShowDriverInfo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.applications.tinytonwe.drivermodificationappversion2.AppData;
import com.applications.tinytonwe.drivermodificationappversion2.R;

import java.util.ArrayList;

public class SDIDrivingSessionActivity extends ActionBarActivity {

    private ArrayList<String> test;
    private ArrayList<Violation> violations_;
    private RecyclerView drivingSession_;

    private DrivingSession session_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdidriving_session);

        int drivingSession = this.getIntent().getExtras().getInt("sessionId");
        session_ = AppData.getAppDataInstance_().getDrivingHistory().get(drivingSession);


        initialize();
        registerListeners();
    }


    private void initialize(){
        getViolations();
    }
    private void getViolations(){

        violations_ = new ArrayList<>();

        if(session_.v1 != null) {
            int freq = Integer.parseInt(session_.v1);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v1);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v2 != null) {
            int freq = Integer.parseInt(session_.v2);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v2);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v3 != null) {
            int freq = Integer.parseInt(session_.v3);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v3);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v4 != null) {
            int freq = Integer.parseInt(session_.v4);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v4);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v5 != null) {
            int freq = Integer.parseInt(session_.v5);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v5);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v6 != null) {
            int freq = Integer.parseInt(session_.v6);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v6);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v7 != null) {
            int freq = Integer.parseInt(session_.v7);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v7);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v8 != null) {
            int freq = Integer.parseInt(session_.v8);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v8);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v9 != null) {
            int freq = Integer.parseInt(session_.v9);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v9);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v10 != null) {
            int freq = Integer.parseInt(session_.v10);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v10);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v11 != null) {
            int freq = Integer.parseInt(session_.v11);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v11);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v12 != null) {
            int freq = Integer.parseInt(session_.v12);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v12);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v13 != null) {
            int freq = Integer.parseInt(session_.v13);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v13);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v14 != null) {
            int freq = Integer.parseInt(session_.v14);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v14);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v15 != null) {
            int freq = Integer.parseInt(session_.v15);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v15);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v16 != null) {
            int freq = Integer.parseInt(session_.v16);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v16);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v17 != null) {
            int freq = Integer.parseInt(session_.v17);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v17);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }

        if(session_.v18 != null) {
            int freq = Integer.parseInt(session_.v18);
            if (freq > 0) {
                Violation violation = new Violation();
                violation.violationImage = this.getResources().getDrawable(R.drawable.v18);
                violation.frequency = freq;
                violation.description = "Description Here";
                violations_.add(violation);
            }
        }
    }

    private void registerListeners(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("Session Details");
        toolbar.setNavigationIcon(this.getResources().getDrawable(R.drawable.ic_arrow_back_black_36dp));
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drivingSession_ = (RecyclerView)findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);

        drivingSession_.addItemDecoration(new DividerItemDecoration(this,R.drawable.divider));

        RecyclerView.Adapter adapter = new RecyclerViewAdapterDrivingSession(violations_,this);

        drivingSession_.setLayoutManager(layoutManager);
        drivingSession_.setAdapter(adapter);
    }
}
