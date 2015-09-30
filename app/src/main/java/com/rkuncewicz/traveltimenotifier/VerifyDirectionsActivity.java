package com.rkuncewicz.traveltimenotifier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import java.sql.Time;
import java.util.Date;
import java.util.Objects;

/**
 * Created by rkuncewicz on 9/20/15.
 */
public class VerifyDirectionsActivity extends Activity {

    public void addNewNotifier(){
        startActivity(new Intent(this, AddNewNotifierActivity.class));
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.verify_directions);

        Bundle extras = getIntent().getExtras();
        Date arrivalTime;
        String name, from, to;
        if (extras != null) {
            name = extras.getString("name");
            from = extras.getString("from").toString();
            to = extras.getString("to").toString();
            int arrivalTime_ms = extras.getInt("arrival_time");

            Log.e("name", name);
            Log.e("from", from);
            Log.e("to", to);
            Log.e("arrival", Objects.toString(arrivalTime_ms));
        }
    }

    protected void onResume() {
        super.onResume();
    }
}
