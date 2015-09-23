package com.rkuncewicz.traveltimenotifier;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.common.api.PendingResult;

/**
 * Created by rkuncewicz on 9/20/15.
 */
public class AddNewNotifier extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_notifier);

        findViewById(R.id.addNewNotifierButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        EditText addressText = (EditText)findViewById(R.id.editNotifierAddress);
        addressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LatLngBounds strictBounds = new LatLngBounds(
                        new LatLng(-85, -180),
                        new LatLng(85, 180)
                );

                PendingResult result =
                        (PendingResult) Places.GeoDataApi.getAutocompletePredictions(
                                mGoogleApiClient,
                                s.toString(),
                                strictBounds,
                                null
                        );
                result.setResultCallback(new ResultCallback() {
                    @Override
                    public void onResult(Result result) {
                        AutocompletePredictionBuffer buffer = (AutocompletePredictionBuffer) result;
                        Log.e("result", buffer.toString());
                        buffer.release();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    protected void onResume() {
        super.onResume();
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
