package com.events.hanle.events.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.events.hanle.events.Constants.SimpleDividerItemDecoration;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.CanceledEvent;
import com.events.hanle.events.Model.ListEvent;
import com.events.hanle.events.R;
import com.events.hanle.events.SqlliteDB.DBController;
import com.events.hanle.events.adapter.CanceledEventAdapter;
import com.events.hanle.events.adapter.RecyclerTouchListener;
import com.events.hanle.events.app.EndPoints;
import com.events.hanle.events.app.MyApplication;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

/**
 * Created by Hanle on 8/28/2017.
 */

public class CancelledEvent extends AppCompatActivity {
    CanceledEventAdapter adapter;
    RecyclerView rv;
    private ArrayList<CanceledEvent> canceledevent = new ArrayList<>();
    Context ctx;
    private String TAG = CancelledEvent.class.getSimpleName();
    String mobileno, countrycode;
    TextView t;
    private AVLoadingIndicatorView avi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancelled_event_view);
        TextView t1 = (TextView) findViewById(R.id.toolbar_title);
        t1.setText("Cancelled | NA Events");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");

        t = (TextView) findViewById(R.id.no_events_to_show);
        rv = (RecyclerView) findViewById(R.id.recyclerView_for_cancelledEvent);
        mobileno = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getMobile();
        countrycode = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getCountrycode();
        adapter = new CanceledEventAdapter(getApplicationContext(), canceledevent);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(ctx));
        rv.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);

        if (ConnectionDetector.isInternetAvailable(getApplicationContext())) {
            fetchcanceleddata();
        } else {
            //Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
            callingOfflineMethod();
        }

        rv.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                CanceledEvent canevt = canceledevent.get(position);

                canevt = new CanceledEvent(canevt.getId(), canevt.getEvent_title(), canevt.getUser_status(), canevt.getInvitername(), canevt.getEvent_status(), null, canevt.getShare_detail(), canevt.getArtwork(), canevt.getEventtype(), canevt.getChatw(), canevt.getCountrycode(), canevt.getPhone(),canevt.getAcknw());

                MyApplication.getInstance().getPrefManager().storeEventIdCanceledEvent(canevt);
                int user_Status = Integer.parseInt(canevt.getUser_status());

                if (user_Status == 1) {
                    //Toasty.error(getApplicationContext(), "You did not respond to this Event!!", Toast.LENGTH_SHORT, true).show();
                    Toasty.custom(getApplicationContext(), "You did not respond to this Event!!", R.drawable.ic_cancel_white_24dp, ContextCompat.getColor(getApplicationContext(), R.color.said_not_Attening), Toast.LENGTH_SHORT,true,true).show();
                    //Toast.makeText(getApplicationContext(), "You did not respond to this Event!!", Toast.LENGTH_LONG).show();
                } else if (user_Status == 3) {
                    //Toast.makeText(getApplicationContext(), "You said you are not attending this Event!!", Toast.LENGTH_LONG).show();
                    Toasty.error(getApplicationContext(), "You said you are not attending this Event!!", Toast.LENGTH_SHORT, true).show();

                } else {

                    Intent i = new Intent(getApplicationContext(), UserTabView.class);
                    i.putExtra("event_title", canevt.getEvent_title());
                    i.putExtra("share_detail", canevt.getShare_detail());
                    i.putExtra("artwork", canevt.getArtwork());
                    i.putExtra("eventtype", canevt.getEventtype());
                    i.putExtra("chatw", canevt.getChatw());
                    i.putExtra("acknw", canevt.getAcknw());

                    i.putExtra("classcheck", "cancelledevent");
                    startActivity(i);

                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));
    }

    private void callingOfflineMethod() {
        avi.hide();
        avi.setVisibility(View.GONE);
        DBController dbController;
        dbController = new DBController(getApplicationContext());
        ArrayList<HashMap<String, String>> listEvents = dbController.getCancelledEvents();
        if (listEvents.size() != 0) {
            listEvents = dbController.getCancelledEvents();
            for (HashMap<String, String> entry : listEvents) {
                CanceledEvent cr = new CanceledEvent();
                cr.setUser_status(entry.get("user_attending_status"));
                cr.setId(entry.get("eID"));
                cr.setEvent_title(entry.get("event_title"));
                cr.setInvitername(entry.get("inviter_name"));
                cr.setEvent_status(entry.get("event_status"));
                cr.setShare_detail(entry.get("share_detial"));
                cr.setArtwork(entry.get("artwork"));
                cr.setEventtype(entry.get("type"));
                cr.setChatw(entry.get("chatW"));
                cr.setCountrycode(entry.get("countrycode"));
                cr.setPhone(entry.get("phone"));
                cr.setLastMessage("");
                cr.setUnreadCount(0);
                cr.setTimestamp(entry.get("created_at"));
                canceledevent.add(cr);

            }
            adapter = new CanceledEventAdapter(getApplicationContext(), canceledevent);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(new LinearLayoutManager(ctx));
            rv.setAdapter(adapter);
        }
    }

    private void fetchcanceleddata() {

        String endpoint = WebUrl.CANCELLED_EVENT.replace("COUNTRY_CODE", countrycode);
        String endpoint1 = endpoint.replace("_USERID_", mobileno);

        Log.e(TAG, "end point: " + endpoint1);

        avi.show();
        avi.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                endpoint1, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
                avi.hide();
                avi.setVisibility(View.GONE);
                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray chatRoomsArray = obj.getJSONArray("event_response");
                        if (chatRoomsArray.length() > 0) {
                            for (int i = 0; i < chatRoomsArray.length(); i++) {
                                JSONObject chatRoomsObj = (JSONObject) chatRoomsArray.get(i);
                                CanceledEvent cr = new CanceledEvent();
                                cr.setUser_status(chatRoomsObj.getString("user_attending_status"));
                                cr.setId(chatRoomsObj.getString("event_id"));
                                cr.setEvent_title(chatRoomsObj.getString("event_title"));
                                cr.setInvitername(chatRoomsObj.getString("inviter_name"));
                                cr.setEvent_status(chatRoomsObj.getString("event_status"));
                                cr.setShare_detail(chatRoomsObj.getString("share_detail"));
                                cr.setArtwork(chatRoomsObj.getString("artwork"));
                                cr.setEventtype(chatRoomsObj.getString("type"));
                                cr.setChatw(chatRoomsObj.getString("chatW"));
                                cr.setCountrycode(chatRoomsObj.getString("countrycode"));
                                cr.setPhone(chatRoomsObj.getString("phone"));
                                cr.setLastMessage("");
                                cr.setUnreadCount(0);
                                cr.setTimestamp(chatRoomsObj.getString("created_at"));
                                canceledevent.add(cr);
                            }
                        } else {
                            t.setVisibility(View.VISIBLE);
                            t.setText("No canceled events");
                            rv.setVisibility(View.GONE);
                        }

                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Server did not respond!!", Toast.LENGTH_LONG).show();
                }

                adapter.notifyDataSetChanged();

                // subscribing to all chat room topics
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                avi.hide();
                avi.setVisibility(View.GONE);
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
                    Toast.makeText(getApplicationContext(), "Server did not respond!!", Toast.LENGTH_SHORT).show();

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
