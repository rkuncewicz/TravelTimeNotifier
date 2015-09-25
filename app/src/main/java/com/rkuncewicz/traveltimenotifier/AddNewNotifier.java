package com.rkuncewicz.traveltimenotifier;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.common.api.PendingResult;

import org.w3c.dom.Text;

import java.util.ArrayList;

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

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Check to see if both views have addresses
                AutoCompleteTextView fromTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_to_address);
                AutoCompleteTextView toTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_from_address);

                Log.e("oy", toTextView.getText().toString());
                Log.e("yo", fromTextView.getText().toString());
            }
        };

        AutoCompleteTextView fromTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_to_address);
        fromTextView.addTextChangedListener(getAutoCompleteTextWatcher(R.id.autocomplete_to_address));
        fromTextView.setOnItemClickListener(itemClickListener);

        AutoCompleteTextView toTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_from_address);
        toTextView.addTextChangedListener(getAutoCompleteTextWatcher(R.id.autocomplete_from_address));
        toTextView.setOnItemClickListener(itemClickListener);
    }

    private TextWatcher getAutoCompleteTextWatcher(final int autoCompleteTextView) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LatLngBounds strictBounds = new LatLngBounds(
                        new LatLng(28.70, -127.50),
                        new LatLng(48.85, -55.90)
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
                        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(autoCompleteTextView);
                        ArrayList addresses = new ArrayList();

                        AutocompletePredictionBuffer buffer = (AutocompletePredictionBuffer) result;
                        Log.e("result", "Halo");
                        Log.e("result", buffer.toString());
                        Log.e("count", Integer.toString(buffer.getCount()));
                        for (AutocompletePrediction location: buffer) {
                            Log.e("desc", location.getDescription());
                            Log.e("id", location.getPlaceId());
                            addresses.add(location.getDescription());
                        }

                        ArrayAdapter<String> adapter =
                                new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, addresses);
                        textView.setAdapter(adapter);

                        buffer.release();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("1", s.toString());
            }
        };
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
