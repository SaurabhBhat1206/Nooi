package com.events.hanle.events.Fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.CanceledEvent;
import com.events.hanle.events.Model.EventMessage;
import com.events.hanle.events.R;
import com.events.hanle.events.adapter.EventMessageAdapter;
import com.events.hanle.events.app.Config;
import com.events.hanle.events.app.EndPoints;
import com.events.hanle.events.app.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Three extends Fragment {

    private static final String TAG = "Three";
    private ArrayList<EventMessage> feedsList = new ArrayList<EventMessage>();
    private RecyclerView mRecyclerView;
    private EventMessageAdapter eventMessageAdapter;
    TextView tv;
    String s;
    String event_status, event_title, Username, invitername, event_id, eventtype;
    ImageView img;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;
    private boolean isFragmentLoaded=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_three, container, false);
        tv = (TextView) v.findViewById(R.id.no_event);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_fragment_three);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mRecyclerView.setHasFixedSize(true);
        Username = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getName();
        img = (ImageView) v.findViewById(R.id.nointernet);
        UserTabView activity = (UserTabView) getActivity();
        Config.typeface = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Regular.ttf");
        tv.setTypeface(Config.typeface);

        s = getActivity().getIntent().getStringExtra("classcheck");
        if (s != null) {
            if (s.equalsIgnoreCase("cancelledevent")) {
                event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getEvent_status();
                event_title = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getEvent_title();
                invitername = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getInvitername();
                event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getId();
                eventtype = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getEventtype();

            } else if (s.equalsIgnoreCase("completedevent")) {
                event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getEvent_status();
                event_title = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getEvent_title();
                invitername = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getInvitername();
                event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getId();
                eventtype = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getEvent_type();

            } else if (s.equalsIgnoreCase("from_notifications")) {
                event_status = activity.getIntent().getStringExtra("eventstatus");
                event_title = activity.getIntent().getStringExtra("event_title");
                invitername = activity.getIntent().getStringExtra("invitername");
                event_id = activity.getIntent().getStringExtra("chat_room_id");
                eventtype = activity.getIntent().getStringExtra("eventtype");


            } else if (s.equalsIgnoreCase("from_partner")) {
                event_status = activity.getIntent().getStringExtra("eventstatus");
                event_title = activity.getIntent().getStringExtra("event_title");
                event_id = activity.getIntent().getStringExtra("eventId");
                invitername = activity.getIntent().getStringExtra("invitername");
                eventtype = activity.getIntent().getStringExtra("eventtype");
            } else if (s.equalsIgnoreCase("from_organiser")) {
                event_status = activity.getIntent().getStringExtra("eventstatus");
                event_title = activity.getIntent().getStringExtra("event_title");
                invitername = activity.getIntent().getStringExtra("invitername");
                event_id = activity.getIntent().getStringExtra("eventId");
                eventtype = activity.getIntent().getStringExtra("eventtype");
            }

        } else {
            event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEvent_status();
            event_title = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEvent_title();
            invitername = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getInvitername();
            event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getId();
            eventtype = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEvent_type();


        }


        //event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEvent_status();
        //event_title = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEvent_title();
        int es = Integer.parseInt(event_status);
        int et = Integer.parseInt(eventtype);
        feedsList = new ArrayList<>();
        EventMessage eventMessage = new EventMessage();
        if (es == 2 && et == 1) {
            tv.setVisibility(View.GONE);
            eventMessage.setTitle("Dear " + Username + ",");
            eventMessage.setDescription("Please be informed, the event " + event_title + " stands cancelled. The chat feature is now disabled. Regards. " + invitername + ".");
            feedsList.add(eventMessage);
            eventMessageAdapter = new EventMessageAdapter(getActivity(), (ArrayList<EventMessage>) feedsList, s);
            mRecyclerView.setAdapter(eventMessageAdapter);
        } else if (es == 3 && et == 1) {
            tv.setVisibility(View.GONE);
            eventMessage.setTitle("Dear " + Username + ",");
            eventMessage.setDescription("Thank you for participating. This event is now concluded. The chat feature is now disabled. Regards. " + invitername + ".");
            feedsList.add(eventMessage);
            eventMessageAdapter = new EventMessageAdapter(getActivity(), (ArrayList<EventMessage>) feedsList, s);
            mRecyclerView.setAdapter(eventMessageAdapter);

        } else if (es == 2 && et == 2) {
            tv.setVisibility(View.GONE);
            eventMessage.setTitle("Hello there!");
            eventMessage.setDescription("Please be informed, the event " + event_title + " stands cancelled. Regards. " + invitername + ".");
            feedsList.add(eventMessage);
            eventMessageAdapter = new EventMessageAdapter(getActivity(), (ArrayList<EventMessage>) feedsList, s);
            mRecyclerView.setAdapter(eventMessageAdapter);
        } else if (es == 3 && et == 2) {
            tv.setVisibility(View.GONE);
            eventMessage.setTitle("Hello there!");
            eventMessage.setDescription("Thank you. This event is now concluded. Regards." + invitername + ".");
            feedsList.add(eventMessage);
            eventMessageAdapter = new EventMessageAdapter(getActivity(), (ArrayList<EventMessage>) feedsList, s);
            mRecyclerView.setAdapter(eventMessageAdapter);
        } else {
            eventMessageAdapter = new EventMessageAdapter(getActivity(), (ArrayList<EventMessage>) feedsList, s);
            mRecyclerView.setAdapter(eventMessageAdapter);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Checking the eventstatus" + event_status);
                    if (ConnectionDetector.isInternetAvailable(getActivity())) {

                        fetchdata();
                    } else {
                        tv.setVisibility(View.VISIBLE);
                        img.setVisibility(View.VISIBLE);
                        tv.setText("No Internet!!");
                        mRecyclerView.setVisibility(View.GONE);
                    }
                }
            });
        }


        return v;
    }


//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        if (isVisibleToUser && !isFragmentLoaded ) {
//            // Load your data here or do network operations here
//            fetchdata();
//            isFragmentLoaded = true;
//        }
//    }

    private void fetchdata() {

        String endpoint = EndPoints.PUSH_DATA_PARTNER.replace("EVENT_ID_", event_id);

        Log.e(TAG, "end point: " + endpoint);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endpoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);


                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray pushData = obj.getJSONArray("push");
                        if (pushData.length() > 0) {
                            for (int i = 0; i < pushData.length(); i++) {
                                EventMessage eventMessage = new EventMessage();
                                JSONObject pushdata = (JSONObject) pushData.get(i);
                                if (pushdata.getString("push_from").equalsIgnoreCase("partner")) {
                                    eventMessage.setPushtype(pushdata.getString("push_from"));
                                    eventMessage.setTitle(pushdata.getString("partner_name"));
                                    eventMessage.setPush_message(pushdata.getString("push_message"));
                                    eventMessage.setTimestamp(pushdata.getString("time_stamp"));

                                } else if (pushdata.getString("push_from") != null && pushdata.getString("push_from").equalsIgnoreCase("organiser")) {
                                    eventMessage.setPushtype(pushdata.getString("push_from"));
                                    eventMessage.setTitle(pushdata.getString("organiser_name"));
                                    eventMessage.setPush_message(pushdata.getString("push_message"));
                                    eventMessage.setTimestamp(pushdata.getString("time_stamp"));
                                    System.out.println("Organiser Message******" + feedsList);
                                }
                                feedsList.add(eventMessage);


                            }
                        } else {
                            tv.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getActivity(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity(), "Server did not respond!!", Toast.LENGTH_LONG).show();
                }

                eventMessageAdapter.notifyDataSetChanged();

                // subscribing to all chat room topics
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                //Toast.makeText(ListOfEvent1.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(),
                            getActivity().getString(R.string.error_network_timeout),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getActivity(),
                            getActivity().getString(R.string.error_network_server),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Server did not respond!!", Toast.LENGTH_SHORT).show();

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
    public void onDestroy() {
        super.onDestroy();

    }
}