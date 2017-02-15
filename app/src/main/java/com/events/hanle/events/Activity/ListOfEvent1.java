package com.events.hanle.events.Activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Fragments.CanceledFragments;
import com.events.hanle.events.Fragments.CompletedFragments;
import com.events.hanle.events.Fragments.CreateEvent;
import com.events.hanle.events.Model.ListEvent;
import com.events.hanle.events.Model.ListEventCopy;
import com.events.hanle.events.Model.Message;
import com.events.hanle.events.R;
import com.events.hanle.events.adapter.ListEventAdapter;
import com.events.hanle.events.app.Config;
import com.events.hanle.events.app.EndPoints;
import com.events.hanle.events.app.MyApplication;
import com.events.hanle.events.gcm.GcmIntentService;
import com.events.hanle.events.gcm.NotificationUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Hanle on 8/2/2016.
 */

public class ListOfEvent1 extends AppCompatActivity {
    private ArrayList<ListEvent> listevent = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ListEventAdapter adapter;
    Context ctx;
    TextView noEvent, listEventID;
    String user_id, mobileno, countrycode;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    private String TAG = ListOfEvent1.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private AlertDialog progressDialog;
    TextView tv;
    private ArrayList<ListEventCopy> copylistevent = new ArrayList<>();
    Double VersionCOde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_event);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_for_listevent);

        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(t);
        assert t != null;
        t.setTitleTextColor(Color.WHITE);
        t.setTitle("nooi");
        tv = (TextView) findViewById(R.id.list_event_id);
        noEvent = (TextView) findViewById(R.id.no_events_to_show);
        listEventID = (TextView) findViewById(R.id.list_event_id);
        user_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUserId().getId();
        mobileno = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getMobile();
        countrycode = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getCountrycode();


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Refreshing data on server
                if (ConnectionDetector.isInternetAvailable(ListOfEvent1.this)) {
                    listevent.clear();
                    adapter.notifyDataSetChanged();
                    fetchChatRooms();
                } else {
                    Toast.makeText(ListOfEvent1.this, "No Internet!!!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        //String messagecount = sharedpreferences.getString(MESSAGE,null);

        /**
         * Broadcast receiver calls in two scenarios
         * 1. gcm registration is completed
         * 2. when new push notification is received
         * */
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    subscribeToGlobalTopic();

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL
                    Log.e(TAG, "GCM registration id is sent to our server");

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION_FROM_PARTNER)) {
                    // new push notification is received
                    String desc = intent.getStringExtra("description");
                    calldialogfrombroadcastforpartner(desc);
                    handlePushNotification(intent);
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION_FROM_ORGANISER)) {
                    // new push notification is received
                    String desc = intent.getStringExtra("description");
                    calldialogfrombroadcastfororganiser(desc);
                }


            }
        };
        adapter = new ListEventAdapter(ListOfEvent1.this, listevent);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));

        mRecyclerView.setAdapter(adapter);
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }


    }

    /**
     * Handles new push notification
     */
    public void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == Config.PUSH_TYPE_CHATROOM) {
            Message message = (Message) intent.getSerializableExtra("message");
            String chatRoomId = intent.getStringExtra("chat_room_id");

            if (chatRoomId != null) {
                updateRow(chatRoomId, message);
            }
        } else if(type == Config.PUSH_TYPE_ORGANISER || type == Config.PUSH_TYPE_PARTNER){
            String chatRoomId = intent.getStringExtra("eventId");
            if(chatRoomId!=null){
                updateRowwehninbackgroundforparneeorganiser(chatRoomId);

            }

        }else if (type == Config.PUSH_TYPE_USER) {
            // push belongs to user alone
            // just showing the message in a toast
            Message message = (Message) intent.getSerializableExtra("message");
        }


    }

    /**
     * Updates the chat list unread count and the last message
     */

    private void updateRow(String chatRoomId, Message message) {
        for (ListEvent cr : listevent) {
            if (cr.getId().equals(chatRoomId)) {
                int index = listevent.indexOf(cr);
                cr.setLastMessage(message.getMessage());
                cr.setUnreadCount(cr.getUnreadCount() + 1);
                listevent.remove(index);
                cr.setUnreadCount(1);
                listevent.add(index, cr);
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void updateRowwehninbackgroundforparneeorganiser(String chatRoomId) {
        for (ListEvent lc : listevent) {
            if (lc.getId().equals(chatRoomId)) {
                int index = listevent.indexOf(lc);
                lc.setUnreadcount1(lc.getUnreadcount1() + 1);
                listevent.remove(index);
                lc.setUnreadcount1(1);
                listevent.add(index, lc);

                break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void fetchChatRooms() {

        String endpoint = EndPoints.CHAT_ROOMS_LIST.replace("COUNTRY_CODE", countrycode);
        String endpoint1 = endpoint.replace("_USERID_", mobileno);

        Log.e(TAG, "end point: " + endpoint);

        progressDialog = new ProgressDialog(ListOfEvent1.this);
        progressDialog.setMessage("Loading Events please wait...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endpoint1, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
                mSwipeRefreshLayout.setRefreshing(false);
                progressDialog.hide();

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("error") == false) {
                        JSONArray chatRoomsArray = obj.getJSONArray("chat_rooms");
                        Log.d("Array length", String.valueOf(chatRoomsArray.length()));
                        if (chatRoomsArray.length() <= 0) {
                            tv.setText("No Active Events!!");

                        } else {
                            for (int i = 0; i < chatRoomsArray.length(); i++) {
                                JSONObject chatRoomsObj = (JSONObject) chatRoomsArray.get(i);
                                ListEvent cr = new ListEvent();
                                cr.setUser_status(chatRoomsObj.getString("user_attending_status"));
                                cr.setId(chatRoomsObj.getString("event_id"));
                                cr.setEvent_title(chatRoomsObj.getString("event_title"));
                                cr.setInvitername(chatRoomsObj.getString("inviter_name"));
                                cr.setEvent_status(chatRoomsObj.getString("event_status"));
                                cr.setShare_detail(chatRoomsObj.getString("share_detail"));
                                cr.setArtwork(chatRoomsObj.getString("artwork"));
                                cr.setLastMessage("");
                                cr.setUnreadCount(0);
                                cr.setTimestamp(chatRoomsObj.getString("created_at"));
                                listevent.add(cr);
                                tv.setText(getString(R.string.list));

                            }
                        }


                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(ListOfEvent1.this, "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }


                    // check for error flag


                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(ListOfEvent1.this, "Server did not respond!!", Toast.LENGTH_LONG).show();
                }

                adapter.notifyDataSetChanged();

                // subscribing to all chat room topics
                //subscribeToAllTopics();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                mSwipeRefreshLayout.setRefreshing(false);
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
                    Toast.makeText(ListOfEvent1.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();

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

    public void subscribeToGlobalTopic() {
        Intent intent = new Intent(ListOfEvent1.this, GcmIntentService.class);
        intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
        intent.putExtra(GcmIntentService.TOPIC, Config.TOPIC_GLOBAL);
        startService(intent);
    }


    @Override
    public void onResume() {
        super.onResume();


        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(ListOfEvent1.this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(ListOfEvent1.this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        LocalBroadcastManager.getInstance(ListOfEvent1.this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.SCHEDULEDPUSH));

        LocalBroadcastManager.getInstance(ListOfEvent1.this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION_FROM_PARTNER));

        LocalBroadcastManager.getInstance(ListOfEvent1.this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION_FROM_ORGANISER));


        // clearing the notification tray
        NotificationUtils.clearNotifications();

        if (checkPlayServices()) {
            if (ConnectionDetector.isInternetAvailable(ListOfEvent1.this)) {
                listevent.clear();
                adapter.notifyDataSetChanged();
                registerGCM();
                fetchChatRooms();

            } else {
                Toast.makeText(ListOfEvent1.this, "No Internet!!", Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(ListOfEvent1.this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    private void registerGCM() {
        Intent intent = new Intent(ListOfEvent1.this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(ListOfEvent1.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(ListOfEvent1.this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(ListOfEvent1.this, "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void sendFeedbackalert() {

        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Hanle Solutions")
                .setContentText("Please share your Feedback to info@hanlesolutions.com")
                .setCustomImage(R.drawable.images)
                .show();

    }

    private void calldialogue() {


        CreateEvent dialogFragment= new CreateEvent();
        dialogFragment.show(getSupportFragmentManager(),"missiles");
    }

    private void calldialogfrombroadcastforpartner(String desc) {
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Message from partner")
                .setContentText(desc)
                .setCustomImage(R.drawable.images)
                .show();
    }

    private void calldialogfrombroadcastfororganiser(String desc) {
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Message from partner")
                .setContentText(desc)
                .setCustomImage(R.drawable.images)
                .show();
    }

    private void update(String app_ver) {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Version:" + app_ver)
                .setContentText("A newer version of nooi is available")
                .setConfirmText("Update")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        sDialog.setCanceledOnTouchOutside(true);
                        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/details?id=com.events.hanle.events&hl=en")));

                    }
                })
                .show();

    }

    private void noupdate(String app_ver) {

        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Version:" + app_ver)
                .setContentText("You are using the latest version")
                .setCustomImage(R.drawable.images)
                .show();

    }

    private void aboutus() {

        String app_ver = null;
        try {
            app_ver = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        getVersioncode(app_ver);

    }

    private void getVersioncode(final String app_ver) {

        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.VERSION_CHECK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);


                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("error") == false) {
                        String v = obj.getString("version_code");
                        VersionCOde = Double.parseDouble(v);
                        System.out.println("VERSIONCODEVOLLEY" + VersionCOde);
                        if (app_ver != null) {
                            if (VersionCOde > 0 && (VersionCOde > Double.parseDouble(app_ver))) {
                                update(app_ver);
                            } else {
                                noupdate(app_ver);
                            }
                        }
                    }


                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                mSwipeRefreshLayout.setRefreshing(false);
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
                    Toast.makeText(ListOfEvent1.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();

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

    private void callCanceled() {
        android.app.FragmentManager manager = getFragmentManager();
        CanceledFragments dialog = new CanceledFragments();
        dialog.show(manager, "dialog");
    }

    private void callcompleted() {
        android.app.FragmentManager manager = getFragmentManager();
        CompletedFragments dialog = new CompletedFragments();
        dialog.show(manager, "dialog");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_event, menu);
        menu.getItem(0).setIcon(android.R.drawable.ic_delete);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.cancelled:
                if (ConnectionDetector.isInternetAvailable(ListOfEvent1.this)) {
                    callCanceled();
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet!!", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.concluded:
                if (ConnectionDetector.isInternetAvailable(ListOfEvent1.this)) {
                    callcompleted();
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet!!", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.create_event:
                calldialogue();
                return true;

            case R.id.feedback:
                sendFeedbackalert();
                return true;

            case R.id.about_us:
                if (ConnectionDetector.isInternetAvailable(ListOfEvent1.this)) {
                    aboutus();

                } else {
                    Toast.makeText(ListOfEvent1.this, "No Internet!!!", Toast.LENGTH_SHORT).show();
                }

                return true;


        }
        return super.onOptionsItemSelected(item);

    }


}
