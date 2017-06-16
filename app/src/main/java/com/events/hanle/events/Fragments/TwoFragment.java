package com.events.hanle.events.Fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.R;
import com.events.hanle.events.app.Config;
import com.events.hanle.events.app.EndPoints;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TwoFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private boolean mapsSupported = true;
    int mCurCheckPosition;
    private TextView t;
    public String event_id;
    String mobileno, countrycode;
    ImageView img;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RelativeLayout parent = (RelativeLayout) inflater.inflate(R.layout.activity_two_fragment, container, false);
        mapView = (MapView) parent.findViewById(R.id.map);
        mobileno = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getMobile();
        countrycode = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getCountrycode();

        t = (TextView) parent.findViewById(R.id.textView8);
        img = (ImageView) parent.findViewById(R.id.nointernet);

         Config.typeface = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Regular.ttf");
        t.setTypeface(Config.typeface);

        return parent;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UserTabView activity = (UserTabView) getActivity();

        String s = getActivity().getIntent().getStringExtra("classcheck");
        if (s != null) {
            if (s.equalsIgnoreCase("cancelledevent")) {
                event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getId();

            } else if (s.equalsIgnoreCase("completedevent")) {
                event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getId();
            } else if (s.equalsIgnoreCase("from_notifications")) {
                event_id = activity.getIntent().getStringExtra("chat_room_id");
            } else if (s.equalsIgnoreCase("from_partner")) {
                event_id = activity.getIntent().getStringExtra("eventId");
            } else if (s.equalsIgnoreCase("from_organiser")) {
                event_id = activity.getIntent().getStringExtra("eventId");
            }
        } else {
            event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getId();

        }
        if (savedInstanceState != null) {
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }
        MapsInitializer.initialize(getActivity());

        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }

    }


    private void initializeMap() {
        if (mMap == null && mapsSupported) {
            mapView = (MapView) getActivity().findViewById(R.id.map);
            //googleMap = mapView.getMap();
            mapView.getMapAsync(this);

            //setup markers etc...


        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ConnectionDetector.isInternetAvailable(getActivity())) {
            //new MarkerTask().execute();
            if (EndPoints.LATITUDE != null && EndPoints.LONGITUDE != null && EndPoints.EVENTTIME != null && EndPoints.EVENTNAME != null) {

                LatLng latlng = new LatLng(Double.parseDouble(EndPoints.LATITUDE), Double.parseDouble(EndPoints.LONGITUDE));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latlng).zoom(13).build();
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title("Event: " + EndPoints.EVENTNAME)
                        .snippet("Time of the Event: " + EndPoints.EVENTTIME)
                        .position(latlng))
                        .showInfoWindow();
            }
        } else {
            mapView.setVisibility(View.GONE);
            t.setVisibility(View.VISIBLE);
            img.setVisibility(View.VISIBLE);

        }

    }

    private class MarkerTask extends AsyncTask<Void, Void, String> {

        private static final String LOG_TAG = "ExampleApp";

//        private static final String SERVICE_URL = "https://api.myjson.com/bins/4jb09";


        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(Void... args) {

            HttpURLConnection conn = null;
            final StringBuilder json = new StringBuilder();
            try {

                String endpoint = WebUrl.USER_EVENT_URL.replace("MOBILE_NO", mobileno);
                String endpoint1 = endpoint.replace("EVENT_ID", event_id);
                // Connect to the web service
                URL url = new URL(endpoint1 + countrycode);
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                // Read the JSON data into the StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    json.append(buff, 0, read);

                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error connecting to service", e);
                //throw new IOException("Error connecting to service", e); //uncaught
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return json.toString();
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String json) {

            try {


                JSONObject jsonObject = new JSONObject(json);
                // user_status = jsonObject.getInt("user_status");
                LatLng latlng = new LatLng(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latlng).zoom(13).build();

                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title("Event: " + jsonObject.getString("event_type"))
                        .snippet("Time of the Event: " + jsonObject.getString("event_time"))
                        .position(latlng))
                        .showInfoWindow();


                // Create a marker for each city in the JSON data.
//                Marker marker = mMap.addMarker(new MarkerOptions()
//
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//                        .title("Event:" + jsonObject.getString("event_type"))
//                        .snippet("Time of the Event: " + jsonObject.getString("event_time"))
//                        .position(latlng));

//                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//
//                    @Override
//                    public View getInfoWindow(Marker arg0) {
//                        return null;
//                    }
//
//                    @Override
//                    public View getInfoContents(Marker arg0) {
//                        Log.e("InfoContent", "InfoContent");
//                        View v = getActivity().getLayoutInflater().inflate(R.layout.mapdetails, null);
//                        TextView title = (TextView) v.findViewById(R.id.eventtitle);
//                        TextView eventdetials = (TextView) v.findViewById(R.id.eventdetials);
//                        title.setText(arg0.getTitle());
//                        eventdetials.setText(arg0.getSnippet());
//
//                        return v;
//
//                    }
//                });


            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error processing JSON", e);
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        initializeMap();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
