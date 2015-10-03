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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Date;
import java.util.Objects;

/**
 * Created by rkuncewicz on 9/20/15.
 */
public class VerifyDirectionsActivity extends FragmentActivity implements RoutingListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private String mName = "";
    private String mStartingAddressName = "";
    private String mDestinationAddressName = "";
    private String mStartingAddressId = "";
    private String mDestinationAddressId = "";
    private LatLng mDestinationLatLng;
    private LatLng mStartingLatLng;

    public void addNewNotifier() {
        startActivity(new Intent(this, AddNewNotifierActivity.class));
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_directions);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mName = extras.getString("name");
            mStartingAddressName = extras.getString("startingAddressName").toString();
            mDestinationAddressName = extras.getString("destinationAddressName").toString();
            mStartingAddressId = extras.getString("startingAddressId").toString();
            mDestinationAddressId = extras.getString("destinationAddressId").toString();
            //int arrivalTime_ms = extras.getInt("arrival_time");

            Log.e("name", mName);
            Log.e("startingAddressName", mStartingAddressName);
            Log.e("destinationAddressName", mDestinationAddressName);
            Log.e("startingAddressId", mStartingAddressId);
            Log.e("destinationAddressId", mDestinationAddressId);
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
        GoogleApiClient googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();

        PendingResult result =
                (PendingResult) Places.GeoDataApi.getPlaceById(googleApiClient, mStartingAddressId, mDestinationAddressId);

        result.setResultCallback(new ResultCallback() {
            @Override
            public void onResult(Result result) {
                PlaceBuffer placeResults = (PlaceBuffer) result;
                Log.e("bloo", placeResults.get(0).getLatLng().toString());
                Log.e("bloo", placeResults.get(1).getLatLng().toString());
                if (placeResults.get(0).getLatLng() != null) {
                    mStartingLatLng = placeResults.get(0).getLatLng();
                }
                if (placeResults.get(1).getLatLng() != null) {
                    mDestinationLatLng = placeResults.get(1).getLatLng();
                }
                setMarkerOnMap();
                placeResults.release();
            }
        });
    }

    private void setMarkerOnMap() {
        LatLng start = mStartingLatLng; //new LatLng(18.015365, -77.499382);
        LatLng end = mDestinationLatLng; //new LatLng(18.012590, -77.500659);

        Log.e("bloo1", start.toString());
        Log.e("bloo2", end.toString());

        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(start, end)
                .build();
        routing.execute();
        mMap.addMarker(new MarkerOptions().position(mDestinationLatLng).title("Destination"));
        mMap.addMarker(new MarkerOptions().position(mStartingLatLng).title("Starting"));

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
        Log.e("Options", polylineOptions.getPoints().toString());
        PolylineOptions polyoptions = new PolylineOptions();
        polyoptions.color(Color.BLUE);
        polyoptions.width(15);
        polyoptions.addAll(polylineOptions.getPoints());
        mMap.addPolyline(polyoptions);

        LatLngBounds zoomTo = mDestinationLatLng.latitude > mStartingLatLng.latitude
                ? new LatLngBounds(mStartingLatLng, mDestinationLatLng)
                : new LatLngBounds(mDestinationLatLng, mStartingLatLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(zoomTo, 150));
    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
