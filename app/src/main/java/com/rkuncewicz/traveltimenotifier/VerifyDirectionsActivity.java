package com.rkuncewicz.traveltimenotifier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by rkuncewicz on 9/20/15.
 */
public class VerifyDirectionsActivity extends Activity {

    public void addNewNotifier(){
        startActivity(new Intent(this, AddNewNotifierActivity.class));
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_directions);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Log.e("name", extras.getString("name").toString());
            Log.e("from", extras.getString("from").toString());
            Log.e("to", extras.getString("to").toString());
            Log.e("to", extras.getString("to").toString());
        }
    }

    protected void onResume() {
        super.onResume();
    }
}
