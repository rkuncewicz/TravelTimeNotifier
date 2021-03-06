package com.rkuncewicz.traveltimenotifier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.Objects;

public class AddNewNotifierActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private ArrayList<AutocompletePrediction> mPredictionList;
    private AutocompletePrediction mDestinationAddress;
    private AutocompletePrediction mStartingAddress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_notifier);
        final Intent notifierIntent = new Intent(this, AddArrivalTimeActivity.class);
        mPredictionList = new ArrayList<>();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        AdapterView.OnItemClickListener fromItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AutoCompleteTextView fromTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_to_address);
                String fromText = fromTextView.getText().toString();
                Log.e("ID2", Objects.toString(R.id.autocomplete_from_address));
                if (fromText != null && !fromText.isEmpty()) {
                    mStartingAddress = mPredictionList.get(position);
                }
                Log.e("yo", fromTextView.getText().toString());
            }
        };

        AdapterView.OnItemClickListener toItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AutoCompleteTextView toTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_from_address);
                String toText = toTextView.getText().toString();
                Log.e("ID1", Objects.toString(R.id.autocomplete_to_address));

                if(toText != null && !toText.isEmpty()) {
                    mDestinationAddress = mPredictionList.get(position);
                }
                if(mStartingAddress != null) {
                    Log.e("Address", mStartingAddress.getDescription().toString());
                }
                if (mDestinationAddress != null) {
                    Log.e("AddressTo", mDestinationAddress.getDescription().toString());
                }
                Log.e("position", position + " blah " + id);
                Log.e("oy", toTextView.getText().toString());
            }
        };

        final AutoCompleteTextView fromTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_to_address);
        fromTextView.addTextChangedListener(getAutoCompleteTextWatcher(R.id.autocomplete_to_address));
        fromTextView.setOnItemClickListener(fromItemClickListener);

        final AutoCompleteTextView toTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_from_address);
        toTextView.addTextChangedListener(getAutoCompleteTextWatcher(R.id.autocomplete_from_address));
        toTextView.setOnItemClickListener(toItemClickListener);

        Button goToArrivalTime = (Button) findViewById(R.id.goToArrivalTimeButton);
        goToArrivalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if required fields are completed
                String fromTextValue = fromTextView.getText().toString();
                boolean invalidFromText = fromTextValue == null || fromTextValue.isEmpty();
                String toTextValue = toTextView.getText().toString();
                boolean invalidToText = toTextValue == null || toTextValue.isEmpty();

                if (invalidFromText || invalidToText) {
                    if(invalidFromText) fromTextView.setBackground(getDrawable(R.drawable.backgroundshape));
                    if (invalidToText) toTextView.setBackground(getDrawable(R.drawable.backgroundshape));
                } else {
                    final TextView name = (TextView) findViewById(R.id.editNotifierName);
                    notifierIntent.putExtra("name", name.getText().toString());
                    notifierIntent.putExtra("startingAddressName", mStartingAddress.getDescription());
                    notifierIntent.putExtra("destinationAddressName", mDestinationAddress.getDescription());
                    notifierIntent.putExtra("startingAddressId", mStartingAddress.getPlaceId());
                    notifierIntent.putExtra("destinationAddressId", mDestinationAddress.getPlaceId());
                    startActivity(notifierIntent);
                }
            }
        });
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
                        mPredictionList.clear();
                        Log.e("result", "Halo");
                        Log.e("result", buffer.toString());
                        Log.e("count", Integer.toString(buffer.getCount()));
                        for (AutocompletePrediction location: buffer) {
                            Log.e("desc", location.getDescription());
                            Log.e("id", location.getPlaceId());
                            // TODO: What do I do with this return?
                            Boolean addressAdded = addresses.add(location.getDescription());
                            mPredictionList.add(location.freeze());
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
