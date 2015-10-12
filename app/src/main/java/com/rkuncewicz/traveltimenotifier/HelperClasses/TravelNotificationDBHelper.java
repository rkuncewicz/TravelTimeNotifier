package com.rkuncewicz.traveltimenotifier.HelperClasses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.rkuncewicz.traveltimenotifier.HelperClasses.TravelNotificationContract.NotificationModel;

/**
 * Created by rkuncewicz on 10/6/15.
 */
public class TravelNotificationDBHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String TIME_TYPE = " INTEGER";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TravelNotificationContract.NotificationModel.TABLE_NAME + " (" +
                    NotificationModel._ID + " INTEGER PRIMARY KEY," +
                    NotificationModel.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    NotificationModel.COLUMN_NAME_STARTING_ID + INT_TYPE + COMMA_SEP +
                    NotificationModel.COLUMN_NAME_DESTINATION_ID + INT_TYPE + COMMA_SEP +
                    NotificationModel.COLUMN_NAME_STARTING_NAME + TEXT_TYPE + COMMA_SEP +
                    NotificationModel.COLUMN_NAME_DESTINATION_NAME + TEXT_TYPE + COMMA_SEP +
                    NotificationModel.COLUMN_NAME_ARRIVAL_TIME + TIME_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NotificationModel.TABLE_NAME;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "TravelTimeNotifier.db";

    public TravelNotificationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        Log.e("CreatingDB", SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static int delete (SQLiteDatabase db, int id) {
        return db.delete(NotificationModel.TABLE_NAME, NotificationModel._ID + "=" + id, null);
    }

    public static Cursor getAllNotifications (Context context, String[] projection, String sortOrder, String selection, String[] selectionArgs) {
        String[] defaultProjection = {
                TravelNotificationContract.NotificationModel._ID,
                TravelNotificationContract.NotificationModel.COLUMN_NAME_NAME,
                TravelNotificationContract.NotificationModel.COLUMN_NAME_DESTINATION_ID,
                TravelNotificationContract.NotificationModel.COLUMN_NAME_DESTINATION_NAME,
                TravelNotificationContract.NotificationModel.COLUMN_NAME_STARTING_ID,
                TravelNotificationContract.NotificationModel.COLUMN_NAME_STARTING_NAME,
                TravelNotificationContract.NotificationModel.COLUMN_NAME_ARRIVAL_TIME
        };
        String defaultSortOrder = TravelNotificationContract.NotificationModel.COLUMN_NAME_NAME + " DESC";
        String defaultSelection = TravelNotificationContract.NotificationModel.COLUMN_NAME_NAME + " =?";
        String[] defaultSelectionArgs = {
                TravelNotificationContract.NotificationModel.COLUMN_NAME_NAME + " =?"
        };

        projection = projection != null ? projection : defaultProjection;
        sortOrder = sortOrder != null ? sortOrder : defaultSortOrder;
        selection = selection != null ? selection : defaultSelection;
        selectionArgs = selectionArgs != null ? selectionArgs : defaultSelectionArgs;

        TravelNotificationDBHelper dbHelper = new TravelNotificationDBHelper(context);
        SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
        return dbRead.query(
                TravelNotificationContract.NotificationModel.TABLE_NAME,  // The table to query
                projection,                    // The columns to return
                null,                          // The columns for the WHERE clause
                null,                          // The values for the WHERE clause
                null,                          // don't group the rows
                null,                          // don't filter by row groups
                sortOrder                      // The sort order
        );
    }

    public static boolean deleteNotification(Context context, int notificationId) {
        TravelNotificationDBHelper dbHelper = new TravelNotificationDBHelper(context);
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        delete(dbWrite, notificationId);
        return true;
    }
}
