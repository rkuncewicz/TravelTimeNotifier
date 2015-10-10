package com.rkuncewicz.traveltimenotifier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rkuncewicz.traveltimenotifier.HelperClasses.TravelNotification;
import com.rkuncewicz.traveltimenotifier.HelperClasses.TravelNotificationContract.NotificationModel;
import com.rkuncewicz.traveltimenotifier.HelperClasses.TravelNotificationDBHelper;

import java.util.ArrayList;
import java.util.Objects;

import javax.xml.transform.TransformerException;

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
        LinearLayout ll = (LinearLayout) findViewById(R.id.notificationList);
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

        new readDBAsync().execute();
    }

    protected void onResume() {
        super.onResume();
    }

    public class readDBAsync extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            TravelNotificationDBHelper dbHelper = new TravelNotificationDBHelper(getBaseContext());
            SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
            String[] projection = {
                    NotificationModel.COLUMN_NAME_NAME,
                    NotificationModel.COLUMN_NAME_DESTINATION_ID,
                    NotificationModel.COLUMN_NAME_DESTINATION_NAME,
                    NotificationModel.COLUMN_NAME_STARTING_ID,
                    NotificationModel.COLUMN_NAME_STARTING_NAME,
                    NotificationModel.COLUMN_NAME_ARRIVAL_TIME
            };

            String sortOrder =
                    NotificationModel.COLUMN_NAME_NAME + " DESC";

            String selection =  NotificationModel.COLUMN_NAME_NAME + " =?";
            String[] selectionArgs = {
                    NotificationModel.COLUMN_NAME_NAME + " =?"
            };

            Cursor cursor = dbRead.query(
                    NotificationModel.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            ArrayList<TravelNotification> results = new ArrayList<TravelNotification>();
            cursor.moveToFirst();
            Log.e("Count", Objects.toString(cursor.getCount()));

            while (cursor.getCount() > 0) {
                try {
                    String name = cursor.getString(
                            cursor.getColumnIndexOrThrow(NotificationModel.COLUMN_NAME_NAME)
                    );
                    String destinationName = cursor.getString(
                            cursor.getColumnIndexOrThrow(NotificationModel.COLUMN_NAME_DESTINATION_NAME)
                    );
                    int destinationId = cursor.getInt(
                            cursor.getColumnIndexOrThrow(NotificationModel.COLUMN_NAME_DESTINATION_ID)
                    );
                    String startingName = cursor.getString(
                            cursor.getColumnIndexOrThrow(NotificationModel.COLUMN_NAME_STARTING_NAME)
                    );
                    int startingId = cursor.getInt(
                            cursor.getColumnIndexOrThrow(NotificationModel.COLUMN_NAME_STARTING_ID)
                    );
                    int arrivalTime = cursor.getInt(
                            cursor.getColumnIndexOrThrow(NotificationModel.COLUMN_NAME_ARRIVAL_TIME)
                    );

                    results.add(new TravelNotification(name, destinationId, destinationName, startingId, startingName, arrivalTime));

                    Log.e("Woo", name);
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }

                if (!cursor.moveToNext()) break;
            }

            return results;
        }

        @Override
        protected void onPostExecute(Object results) {
            ArrayList<TravelNotification> notifications = (ArrayList<TravelNotification>) results;

            Log.e("Hey", Objects.toString(notifications.size()));

            for(TravelNotification notification : notifications) {
                Log.e("Hey", Objects.toString(notification.getName()));

                View linearLayout = findViewById(R.id.notificationList);
                TextView notificationTV = new TextView(getBaseContext());

                notificationTV.setText(notification.getName());
                notificationTV.setTextColor(getResources().getColor(android.R.color.black));
                notificationTV.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                
                ((LinearLayout)linearLayout).addView(notificationTV);
            }
        }
    }
}
