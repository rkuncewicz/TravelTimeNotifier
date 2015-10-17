package com.rkuncewicz.traveltimenotifier.HelperClasses;

/**
 * Created by rkuncewicz on 10/10/15.
 */
public class TravelNotification {
    private int mId;
    private String mName;
    private int mDestinationId;
    private String mDestinationName;
    private int mStartingId;
    private String mStartingName;
    private int mArrivalTime;

    public TravelNotification(
            int id,
            String name,
            int destinationId,
            String destinationName,
            int startingId,
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
    public int getDestinationId() { return mDestinationId; }
    public String getStartingName() { return mStartingName; }
    public int getStartingId() { return mStartingId; }
    public int getArrivalTime() { return mArrivalTime; }
}