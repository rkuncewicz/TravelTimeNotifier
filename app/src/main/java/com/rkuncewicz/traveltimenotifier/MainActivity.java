package com.rkuncewicz.traveltimenotifier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import com.rkuncewicz.traveltimenotifier.HelperClasses.TravelNotificationArrivalTime;
import com.rkuncewicz.traveltimenotifier.HelperClasses.TravelNotificationContract.NotificationModel;
import com.rkuncewicz.traveltimenotifier.HelperClasses.TravelNotificationDBHelper;

import java.lang.reflect.Array;
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
            Cursor cursor = TravelNotificationDBHelper.getAllNotifications(getBaseContext(), null, null, null, null);
            ArrayList<TravelNotification> results = new ArrayList<TravelNotification>();
            cursor.moveToFirst();
            Log.e("Count", Objects.toString(cursor.getCount()));

            while (cursor.getCount() > 0) {
                try {
                    int id = cursor.getInt(
                            cursor.getColumnIndexOrThrow(NotificationModel._ID)
                    );
                    String name = cursor.getString(
                            cursor.getColumnIndexOrThrow(NotificationModel.COLUMN_NAME_NAME)
                    );
                    String destinationName = cursor.getString(
                            cursor.getColumnIndexOrThrow(NotificationModel.COLUMN_NAME_DESTINATION_NAME)
                    );
                    String destinationId = cursor.getString(
                            cursor.getColumnIndexOrThrow(NotificationModel.COLUMN_NAME_DESTINATION_ID)
                    );
                    String startingName = cursor.getString(
                            cursor.getColumnIndexOrThrow(NotificationModel.COLUMN_NAME_STARTING_NAME)
                    );
                    String startingId = cursor.getString(
                            cursor.getColumnIndexOrThrow(NotificationModel.COLUMN_NAME_STARTING_ID)
                    );
                    int arrivalTime = cursor.getInt(
                            cursor.getColumnIndexOrThrow(NotificationModel.COLUMN_NAME_ARRIVAL_TIME)
                    );

                    results.add(new TravelNotification(id, name, destinationId, destinationName, startingId, startingName, arrivalTime));

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

            Log.e("ArraySize", Objects.toString(notifications.size()));

            for(TravelNotification notification : notifications) {
                Log.e("NotificationName", Objects.toString(notification.getName()));

                View linearLayout = findViewById(R.id.notificationList);
                TextView notificationTV = new TextView(getBaseContext());
                LinearLayout notificationContainer = new LinearLayout(getBaseContext());
                Button deleteNotification = new Button(getBaseContext());
                TextView arrivalTimeTV = new TextView(getBaseContext());

                new TravelNotificationArrivalTime(getBaseContext(), arrivalTimeTV, notification.getStartingId(), notification.getDestinationId()).execute();

                notificationContainer.setOrientation(LinearLayout.HORIZONTAL);
                notificationContainer.setTag(notification);

                deleteNotification.setText("Delete");
                deleteNotification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout container = (LinearLayout) v.getParent();
                        TravelNotification notification = (TravelNotification) container.getTag();
                        TravelNotificationDBHelper.deleteNotification(getBaseContext(), notification.getId());
                        LinearLayout notificationsContainer = (LinearLayout) container.getParent();
                        notificationsContainer.removeView(container);
                    }
                });

                notificationTV.setText(notification.getName());
                notificationTV.setTextColor(getResources().getColor(android.R.color.black));
                arrivalTimeTV.setTextColor(getResources().getColor(android.R.color.black));
                notificationTV.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                notificationContainer.addView(notificationTV);
                notificationContainer.addView(arrivalTimeTV);
                notificationContainer.addView(deleteNotification);
                ((LinearLayout)linearLayout).addView(notificationContainer);
            }
        }
    }
}
