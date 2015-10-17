package com.rkuncewicz.traveltimenotifier.HelperClasses;

import java.sql.Time;

/**
 * Created by rkuncewicz on 10/10/15.
 */
public class TravelNotification {
    private int mId;
    private String mName;
    private String mDestinationId;
    private String mDestinationName;
    private String mStartingId;
    private String mStartingName;
    private int mArrivalTime;

    public TravelNotification(
            int id,
            String name,
            String destinationId,
            String destinationName,
            String startingId,
            String startingName,
            int arrivalTime) {
        mId = id;
        mName = name;
        mDestinationId = destinationId;
        mDestinationName = destinationName;
        mStartingId = startingId;
        mStartingName = startingName;
        mArrivalTime = arrivalTime;
    }

    public int getId() { return mId; }
    public String getName() { return mName; }
    public String getDestinationName() { return mDestinationName; }
    public String getDestinationId() { return mDestinationId; }
    public String getStartingName() { return mStartingName; }
    public String getStartingId() { return mStartingId; }
    public int getArrivalTime() { return mArrivalTime; }

    public Time getEstimatedArrivalTime() {
        Time estimatedTime = new Time(0);
        return estimatedTime;
    }
}
