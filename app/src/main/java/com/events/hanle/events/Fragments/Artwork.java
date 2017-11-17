package com.events.hanle.events.Fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Activity.EventArtwork;
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.R;
import com.events.hanle.events.app.MyApplication;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Hanle on 7/31/2017.
 */

public class Artwork extends Fragment {
    String eventinfoID;
    private ImageView artwork;
    private TextView warning;
    private String TAG = EventArtwork.class.getSimpleName();
    FloatingActionButton fab;
    String url1;
    static final String appDirectoryName = "/nooi";
    static final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), appDirectoryName);
    PhotoViewAttacher mAttacher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artwork_new, container, false);
        artwork = (ImageView) view.findViewById(R.id.artwork);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        warning = (TextView) view.findViewById(R.id.warning);
        fab.setVisibility(View.GONE);

        String s = getActivity().getIntent().getStringExtra("classcheck");
        if (s != null) {
            if (s.equalsIgnoreCase("cancelledevent")) {
                eventinfoID = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getId();
            } else if (s.equalsIgnoreCase("completedevent")) {
                eventinfoID = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getId();
            } else if (s.equalsIgnoreCase("from_notifications")) {
                eventinfoID = getActivity().getIntent().getStringExtra("chat_room_id");
            } else if (s.equalsIgnoreCase("from_partner")) {
                eventinfoID = getActivity().getIntent().getStringExtra("eventId");
            } else if (s.equalsIgnoreCase("from_organiser")) {
                eventinfoID = getActivity().getIntent().getStringExtra("eventId");
            }
        } else {
            eventinfoID = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEventId();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.System.canWrite(getContext())) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
                    } else {
                        // continue with your code
                        if (ConnectionDetector.isInternetAvailable(getContext())) {
                            downloadImage();

                        } else {
                            Toast.makeText(getContext(), "No Internet connection!!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    // continue with your code
                    downloadImage();

                }
            }
        });
        //getAllImages();




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ConnectionDetector.isInternetAvailable(getContext())) {
            getStringUrl();

        } else {
            Toast.makeText(getContext(), "No Internet connection!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                    //Toast.makeText(getApplicationContext(), "Permission Granted!!", Toast.LENGTH_LONG).show();
                    downloadImage();

                } else {
                    Log.e("Permission", "Denied");
                    Toast.makeText(getContext(), "Permission Denied!!", Toast.LENGTH_LONG).show();

                }
                return;
            }
        }
    }


    private void getStringUrl() {

        StringRequest strReq = new StringRequest(Request.Method.GET,
                WebUrl.ORGANISER_ARTWORK + eventinfoID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getString("check").length()==0) {
                        warning.setVisibility(View.VISIBLE);
                        artwork.setVisibility(View.GONE);
                        fab.setVisibility(View.GONE);
                    } else {
                        String url = obj.getString("artwork");
                        url1 = url;
                        System.out.println("url" + url);
                        loadImage(url);
                        mAttacher = new PhotoViewAttacher(artwork);

                    }


                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getContext(), "Server did not respond!!", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                //Toast.makeText(ListOfEvent1.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getContext(),
                            getContext().getString(R.string.error_network_timeout),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getContext(),
                            getContext().getString(R.string.error_network_server),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Server did not respond!!", Toast.LENGTH_SHORT).show();

                }
            }
        });


        strReq.setRetryPolicy(new DefaultRetryPolicy(
                WebUrl.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void downloadImage() {
        ImageRequest im = new ImageRequest(url1, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if (!imageRoot.exists()) {
                    if (!imageRoot.mkdirs()) {
                        Log.d(TAG, "Oops! Failed create "
                                + Artwork.appDirectoryName + " directory");
                    }
                }

                OutputStream fOut = null;
                final File file = new File(imageRoot, "nooi_" + System.currentTimeMillis() + ".jpg");
                try {
                    fOut = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                response.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                try {
                    fOut.flush();
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "nooi");
                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
                values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));
                values.put("_data", file.getAbsolutePath());

                ContentResolver cr = getContext().getContentResolver();
                cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Toast.makeText(getContext(), "Image downloaded successfully!!", Toast.LENGTH_SHORT).show();
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                //Toast.makeText(ListOfEvent1.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getContext(),
                            getContext().getString(R.string.error_network_timeout),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getContext(),
                            getContext().getString(R.string.error_network_server),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Server did not respond!!", Toast.LENGTH_SHORT).show();

                }
            }
        });
        im.setRetryPolicy(new DefaultRetryPolicy(
                WebUrl.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(im);

    }

    private void loadImage(String url) {

        fab.setVisibility(View.VISIBLE);


        Callback imageLoadedCallback = new Callback() {

            @Override
            public void onSuccess() {
                if (mAttacher != null) {
                    mAttacher.update();

                } else {
                    mAttacher = new PhotoViewAttacher(artwork);

                }
            }

            @Override
            public void onError() {
                // TODO Auto-generated method stub

            }
        };

        Picasso.with(getContext())
                .load(url)
                .placeholder(R.drawable.nooismall)
                .error(android.R.drawable.ic_dialog_alert)
                .into(artwork, imageLoadedCallback);


    }


}
