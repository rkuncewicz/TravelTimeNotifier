package com.rkuncewicz.traveltimenotifier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.util.Date;
import java.util.Objects;

/**
 * Created by rkuncewicz on 9/20/15.
 */
public class AddArrivalTimeActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_arrival_time);

        Button goToDirectionsButton = (Button) findViewById(R.id.goToDirectionsButton);
        goToDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker arrivalTime = (TimePicker) findViewById(R.id.arrivalTimePicker);

                int hour, minute;
                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
                    hour = arrivalTime.getHour() * (60 * 60 * 1000);
                    minute = arrivalTime.getMinute() * (60 * 1000);
                } else {
                    hour = arrivalTime.getCurrentHour() * (60 * 60 * 1000);
                    minute = arrivalTime.getCurrentMinute() * (60 * 1000);
                }

                Intent intent = new Intent(getBaseContext(), VerifyDirectionsActivity.class);
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    Log.e("bleh", extras.getString("name"));
                    intent.putExtra("name", extras.getString("name"));
                    intent.putExtra("startingAddressName", extras.getString("startingAddressName"));
                    intent.putExtra("destinationAddressName", extras.getString("destinationAddressName"));
                    intent.putExtra("startingAddressId", extras.getString("startingAddressId"));
                    intent.putExtra("destinationAddressId", extras.getString("destinationAddressId"));
                }
                intent.putExtra("arrivalTime", hour + minute);
                startActivity(intent);
            }
        });
    }

    protected void onResume() {
        super.onResume();
    }
}
