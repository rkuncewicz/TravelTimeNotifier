package com.rkuncewicz.traveltimenotifier;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.support.v4.app.FragmentActivity;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Date;
import java.util.Objects;

/**
 * Created by rkuncewicz on 9/20/15.
 */
public class VerifyDirectionsActivity extends FragmentActivity implements RoutingListener {

    private GoogleMap mMap;
    private String name = "";
    private String startingAddressName = "";
    private String destinationAddressName = "";

    public void addNewNotifier() {
        startActivity(new Intent(this, AddNewNotifierActivity.class));
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_directions);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            startingAddressName = extras.getString("startingAddressName").toString();
            destinationAddressName = extras.getString("destinationAddressName").toString();
            //int arrivalTime_ms = extras.getInt("arrival_time");

            Log.e("name", name);
            Log.e("startingAddressName", startingAddressName);
            Log.e("destinationAddressName", destinationAddressName);
            //Log.e("arrival", Objects.toString(arrivalTime_ms));
        }

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        LatLng start = new LatLng(18.015365, -77.499382);
        LatLng waypoint= new LatLng(18.01455, -77.499333);
        LatLng end = new LatLng(18.012590, -77.500659);

        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(start, waypoint, end)
                .build();
        routing.execute();
        mMap.addMarker(new MarkerOptions().position(new LatLng(18.015365, -77.499382)).title("Marker"));
    }

    @Override
    public void onRoutingFailure() {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(PolylineOptions polylineOptions, Route route) {
        Log.e("Success?", "Yes");
        PolylineOptions polyoptions = new PolylineOptions();
        polyoptions.color(Color.BLUE);
        polyoptions.width(10);
        polyoptions.addAll(polylineOptions.getPoints());
        mMap.addPolyline(polyoptions);
    }

    @Override
    public void onRoutingCancelled() {

    }
}
