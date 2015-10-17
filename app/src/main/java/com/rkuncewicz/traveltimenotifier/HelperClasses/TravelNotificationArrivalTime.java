package com.rkuncewicz.traveltimenotifier.HelperClasses;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.rkuncewicz.traveltimenotifier.R;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rkuncewicz on 10/12/15.
 */
public class TravelNotificationArrivalTime extends AsyncTask<Object, Object, Object> {
    Context m_context;
    TextView m_arrivalTimeTV;
    String m_originID;
    String m_destinationID;
    Exception m_exception;

    final String gMapsDirectionsAPIUrl = "https://maps.googleapis.com/maps/api/directions/json";
    final String placeIdPrefix = "place_id:";

    public TravelNotificationArrivalTime(Context context, TextView arrivalTimeTV, String originID, String destinationID){
        super();
        m_context = context;
        m_arrivalTimeTV = arrivalTimeTV;
        m_originID = originID;
        m_destinationID = destinationID;
    }

    protected Object doInBackground(Object[] params) {
        HttpURLConnection urlConnection = null;
        JSONObject object = null;
        InputStream inStream = null;
        URL url;

        String origin = placeIdPrefix + m_originID;
        String destination = placeIdPrefix + m_destinationID;
        String key = m_context.getString(R.string.google_maps_key);

        try {
            URI gMapsURI = new URIBuilder(gMapsDirectionsAPIUrl)
                    .setParameter("origin", origin)
                    .setParameter("destination", destination)
                    .setParameter("key", key)
                    .build();
            url = new URL(gMapsURI.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            String temp, response = "";
            while ((temp = bReader.readLine()) != null) {
                response += temp;
            }
            object = (JSONObject) new JSONTokener(response).nextValue();
            Log.e("Blee", object.toString());
            return object;
        } catch (Exception e) {
            Log.e("Bloo", e.toString());
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    // this will close the bReader as well
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            return null;
        }

    }

    @Override
    protected void onPostExecute(Object results) {
    }
}
