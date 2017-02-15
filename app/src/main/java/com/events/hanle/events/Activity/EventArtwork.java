package com.events.hanle.events.Activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.R;
import com.events.hanle.events.app.CustomVolleyRequestQueue;
import com.events.hanle.events.app.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import uk.co.senab.photoview.PhotoViewAttacher;

public class EventArtwork extends AppCompatActivity   {
    String eventinfoID;
    private NetworkImageView artwork;
    private ImageLoader imageLoader;
    private String TAG = EventArtwork.class.getSimpleName();
    FloatingActionButton fab;
    String url1;
    static final String appDirectoryName = "/nooi";
    static final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), appDirectoryName);
    PhotoViewAttacher mAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_artwork);
        artwork = (NetworkImageView) findViewById(R.id.artwork);
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        setSupportActionBar(t);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        assert t != null;
        t.setTitleTextColor(Color.WHITE);
        t.setTitle("nooi");
        String s = getIntent().getStringExtra("classcheck");
        if (s != null) {
            if (s.equalsIgnoreCase("cancelledevent")) {
                eventinfoID = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getId();
            } else if (s.equalsIgnoreCase("completedevent")) {
                eventinfoID = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getId();
            } else if (s.equalsIgnoreCase("from_notifications")) {
                eventinfoID = getIntent().getStringExtra("chat_room_id");
            } else if (s.equalsIgnoreCase("from_partner")) {
                eventinfoID = getIntent().getStringExtra("eventId");
            } else if (s.equalsIgnoreCase("from_organiser")) {
                eventinfoID = getIntent().getStringExtra("eventId");
            }
        } else {
            eventinfoID = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getId();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage();
            }
        });
        mAttacher = new PhotoViewAttacher(artwork);
        //getAllImages();
        getStringUrl();
    }

    private void getStringUrl() {

        StringRequest strReq = new StringRequest(Request.Method.GET,
                WebUrl.ORGANISER_ARTWORK + eventinfoID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    String url = obj.getString("artwork");
                    url1 = url;
                    System.out.println("url" + url);
                    loadImage(url);

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(EventArtwork.this, "Server did not respond!!", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                //Toast.makeText(ListOfEvent1.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.error_network_timeout),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.error_network_server),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(EventArtwork.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();

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

    private void loadImage(String url) {

        imageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(artwork,
                R.drawable.nooismall, android.R.drawable
                        .ic_dialog_alert));
        artwork.setImageUrl(url, imageLoader);
        fab.setVisibility(View.VISIBLE);
        mAttacher.update();

    }

    private void downloadImage() {
        ImageRequest im = new ImageRequest(url1, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageRoot.mkdir();

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

                ContentResolver cr = getContentResolver();
                cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Toast.makeText(EventArtwork.this, "Image downloaded successfully!!", Toast.LENGTH_SHORT).show();
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                //Toast.makeText(ListOfEvent1.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.error_network_timeout),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.error_network_server),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(EventArtwork.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();

                }
            }
        });
        im.setRetryPolicy(new DefaultRetryPolicy(
                WebUrl.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(im);

    }
//     private void downloadImage() {
//            ImageRequest im = new ImageRequest(url1, new Response.Listener<Bitmap>() {
//                @Override
//                public void onResponse(Bitmap response) {
//                    File pictureFile = getOutputMediaFile();
//                    if (pictureFile == null) {
//                        Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
//                        return;
//                    }
//                    try {
//                        FileOutputStream fos = new FileOutputStream(pictureFile);
//                        response.compress(Bitmap.CompressFormat.PNG, 100, fos);
//                        fos.close();
//                    } catch (FileNotFoundException e) {
//                        Log.d(TAG, "File not found: " + e.getMessage());
//                    } catch (IOException e) {
//                        Log.d(TAG, "Error accessing file: " + e.getMessage());
//                    }
//                }
//            },0,0, ImageView.ScaleType.CENTER_CROP,null, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    NetworkResponse networkResponse = error.networkResponse;
//                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
//                    //Toast.makeText(ListOfEvent1.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();
//                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                        Toast.makeText(getApplicationContext(),
//                                getApplicationContext().getString(R.string.error_network_timeout),
//                                Toast.LENGTH_LONG).show();
//                    } else if (error instanceof ServerError) {
//                        Toast.makeText(getApplicationContext(),
//                                getApplicationContext().getString(R.string.error_network_server),
//                                Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(EventArtwork.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            });
//            im.setRetryPolicy(new DefaultRetryPolicy(
//                    WebUrl.MY_SOCKET_TIMEOUT_MS,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            MyApplication.getInstance().addToRequestQueue(im);
//
//        }
//
//    private File getOutputMediaFile() {
//
//
//
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//                + appDirectoryName);
//
//
//        // This location works best if you want the created images to be shared
//        // between applications and persist after your app has been uninstalled.
//
//        // Create the storage directory if it does not exist
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                return null;
//            }
//        }
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
//        File mediaFile;
//        String mImageName = "MI_" + timeStamp + ".jpg";
//        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
//        return mediaFile;
//    }
//

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
