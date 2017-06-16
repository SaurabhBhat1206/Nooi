package com.events.hanle.events.Fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.events.hanle.events.Activity.ListOfEvent1;
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.SimpleDividerItemDecoration;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.CanceledEvent;
import com.events.hanle.events.Model.CompletedEvent;
import com.events.hanle.events.R;
import com.events.hanle.events.SqlliteDB.DBController;
import com.events.hanle.events.adapter.CompletedEventAdapter;
import com.events.hanle.events.app.EndPoints;
import com.events.hanle.events.app.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hanle on 10/18/2016.
 */


public class CompletedFragments extends DialogFragment {
    CompletedEventAdapter adapter;
    RecyclerView rv;
    private List<CompletedEvent> completedevent = new ArrayList<>();
    Context ctx;
    private String TAG = CompletedFragments.class.getSimpleName();
    String mobileno, countrycode;
    TextView t;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Concluded Events");
        View v = inflater.inflate(R.layout.attending_invitee, container, false);
        rv = (RecyclerView) v.findViewById(R.id.recyclerView_for_attending);
        t = (TextView) v.findViewById(R.id.text_message);

        mobileno = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getMobile();
        countrycode = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getCountrycode();
        adapter = new CompletedEventAdapter(getActivity(), completedevent, getDialog());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(ctx));
        rv.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity()
        ));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        if (ConnectionDetector.isInternetAvailable(getActivity())) {
            fetchcompletedddata();
        } else {
            //Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
            callingOfflineMethod();
        }

        return v;
    }

    private void callingOfflineMethod() {
        DBController dbController;
        dbController = new DBController(getActivity());
        ArrayList<HashMap<String, String>> listEvents = dbController.getAllConcludedEvents();
        if (listEvents.size() != 0) {
            listEvents = dbController.getAllConcludedEvents();
            for (HashMap<String, String> entry : listEvents) {
                CompletedEvent cr = new CompletedEvent();
                cr.setUser_status(entry.get("user_attending_status"));
                cr.setId(entry.get("eID"));
                cr.setEvent_title(entry.get("event_title"));
                cr.setInvitername(entry.get("inviter_name"));
                cr.setEvent_status(entry.get("event_status"));
                cr.setShare_detail(entry.get("share_detial"));
                cr.setArtwork(entry.get("artwork"));
                cr.setEvent_type(entry.get("type"));
                cr.setChat_window(entry.get("chatW"));
                cr.setCountrycode(entry.get("countrycode"));
                cr.setPhone(entry.get("phone"));
                cr.setLastMessage("");
                cr.setUnreadCount(0);
                cr.setTimestamp(entry.get("created_at"));
                completedevent.add(cr);

            }
            adapter = new CompletedEventAdapter(getActivity(), completedevent, getDialog());
            rv.setHasFixedSize(true);
            rv.setLayoutManager(new LinearLayoutManager(ctx));
            rv.setAdapter(adapter);
        }
    }

    private void fetchcompletedddata() {
        String endpoint = EndPoints.LIST_COMPLETED.replace("COUNTRY_CODE", countrycode);
        String endpoint1 = endpoint.replace("_USERID_", mobileno);

        Log.e(TAG, "end point: " + endpoint);


        StringRequest strReq = new StringRequest(Request.Method.GET,
                endpoint1, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray chatRoomsArray = obj.getJSONArray("chat_rooms");
                        if (chatRoomsArray.length() > 0) {
                            for (int i = 0; i < chatRoomsArray.length(); i++) {
                                JSONObject chatRoomsObj = (JSONObject) chatRoomsArray.get(i);
                                CompletedEvent cr = new CompletedEvent();
                                cr.setUser_status(chatRoomsObj.getString("user_attending_status"));
                                cr.setId(chatRoomsObj.getString("event_id"));
                                cr.setEvent_title(chatRoomsObj.getString("event_title"));
                                cr.setInvitername(chatRoomsObj.getString("inviter_name"));
                                cr.setEvent_status(chatRoomsObj.getString("event_status"));
                                cr.setShare_detail(chatRoomsObj.getString("share_detail"));
                                cr.setArtwork(chatRoomsObj.getString("artwork"));
                                cr.setEvent_type(chatRoomsObj.getString("type"));
                                cr.setChat_window(chatRoomsObj.getString("chatW"));
                                cr.setCountrycode(chatRoomsObj.getString("countrycode"));
                                cr.setPhone(chatRoomsObj.getString("phone"));
                                cr.setLastMessage("");
                                cr.setUnreadCount(0);
                                cr.setTimestamp(chatRoomsObj.getString("created_at"));
                                completedevent.add(cr);
                            }

                        } else {
                            t.setVisibility(View.VISIBLE);
                            t.setText("No Concluded events");
                            rv.setVisibility(View.GONE);
                        }
                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getActivity(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity(), "Server did not respond!!", Toast.LENGTH_LONG).show();
                }

                adapter.notifyDataSetChanged();

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

}
