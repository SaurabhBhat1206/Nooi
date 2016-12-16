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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Activity.ListOfEvent1;
import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.SimpleDividerItemDecoration;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.Attending;
import com.events.hanle.events.R;
import com.events.hanle.events.adapter.ListAttending;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hanle on 8/30/2016.
 */
public class AttendingDialogFragment extends DialogFragment {

    RecyclerView rv;
    ListAttending adapter;
    private List<Attending> listevent = new ArrayList<>();
    Context ctx;
    private String TAG = UserTabView.class.getSimpleName();
    String event_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle("Attending:");

        View v = inflater.inflate(R.layout.attending_invitee, container, false);
        rv = (RecyclerView) v.findViewById(R.id.recyclerView_for_attending);


        String s = getActivity().getIntent().getStringExtra("classcheck");
        if (s != null) {
            if (s.equalsIgnoreCase("cancelledevent")) {
                event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getId();

            } else if (s.equalsIgnoreCase("completedevent")) {
                event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getId();
            }
        } else {
            event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getId();

        }


        adapter = new ListAttending(getActivity(), listevent);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(ctx));
        rv.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity()
        ));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);


        if (ConnectionDetector.isInternetAvailable(getActivity())) {
            fetchattendinglist();
        } else {
            Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
        }

        return v;

    }


    private void fetchattendinglist() {
        String endpoint = WebUrl.LIST_ATTENDING_CONTACT + event_id;
        Log.e(TAG, "end point: " + endpoint);


        StringRequest strReq = new StringRequest(Request.Method.GET,
                endpoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);


                try {
                    JSONObject res = new JSONObject(response);
                    System.out.println("response:" + response);
                    JSONArray jsonArray = res.optJSONArray("server_response");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Attending item = new Attending();
                        item.setNsme(jsonObject.getString("inviter_name"));
                        listevent.add(item);


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
                Toast.makeText(getActivity(), "Server did not respond!!", Toast.LENGTH_SHORT).show();
            }
        });


        strReq.setRetryPolicy(new DefaultRetryPolicy(
                WebUrl.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to request queue
        com.events.hanle.events.app.MyApplication.getInstance().addToRequestQueue(strReq);
    }
}
