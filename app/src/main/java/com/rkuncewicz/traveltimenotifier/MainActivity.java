package com.rkuncewicz.traveltimenotifier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by rkuncewicz on 9/20/15.
 */
public class MainActivity extends Activity {

    public void addNewNotifier(){
        startActivity(new Intent(this, AddNewNotifierActivity.class));
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        String[] values = new String[] { "Android List View" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        //Add list and listener
        LinearLayout ll = (LinearLayout) findViewById(R.id.list);
        for (int i=0; i < adapter.getCount(); i++) {
            View v = adapter.getView(i, null, null);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //addNewNotifier();
                }
            });
            ll.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        //Add listener for new notifiers
        Button b = (Button) findViewById(R.id.addNotifierButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewNotifier();
            }
        });
    }

    protected void onResume() {
        super.onResume();
    }
}
