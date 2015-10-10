package com.rkuncewicz.traveltimenotifier.HelperClasses;

import android.provider.BaseColumns;

/**
 * Created by rkuncewicz on 10/6/15.
 */
public class TravelNotificationContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public TravelNotificationContract() {}

    /* Inner class that defines the table contents */
    public static abstract class NotificationModel implements BaseColumns {
        public static final String TABLE_NAME = "notification";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_STARTING_NAME = "startingname";
        public static final String COLUMN_NAME_DESTINATION_NAME = "destinationname";
        public static final String COLUMN_NAME_STARTING_ID = "startingid";
        public static final String COLUMN_NAME_DESTINATION_ID = "destinationid";
        public static final String COLUMN_NAME_ARRIVAL_TIME = "arrivaltime";
    }
}
