package com.events.hanle.events.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.SimpleDividerItemDecoration;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.Attending;
import com.events.hanle.events.Model.Invitee;
import com.events.hanle.events.R;
import com.events.hanle.events.adapter.InviteeListAdapter;
import com.events.hanle.events.adapter.ListAttending;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hanle on 3/8/2017.
 */

public class InviteeList extends DialogFragment {
    RecyclerView rv;
    private ArrayList<Attending> inviteelist = new ArrayList<>();
    InviteeListAdapter adapter;
    private String TAG = "InviteeList";
    Context ctx;
    TextView t;
    AppCompatButton invite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.invitee_list, container, false);
        rv = (RecyclerView) v.findViewById(R.id.recyclerView_for_invitee_list);
        invite = (AppCompatButton) v.findViewById(R.id.invite_btn);
        t = (TextView) v.findViewById(R.id.tm);

        adapter = new InviteeListAdapter(getActivity(), inviteelist,invite);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(ctx));

        rv.setAdapter(adapter);

        if (ConnectionDetector.isInternetAvailable(getActivity())) {
            fetchattendinglist();
        } else {
            Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    private void fetchattendinglist() {
        String endpoint = WebUrl.ORGANISER_Invitee_list ;
        Log.e(TAG, "end point: " + endpoint);


        StringRequest strReq = new StringRequest(Request.Method.GET,
                endpoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);


                try {
                    JSONObject res = new JSONObject(response);
                    System.out.println("response:" + response);
                    JSONArray jsonArray = res.getJSONArray("server_response");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Attending item = new Attending();
                        item.setId(jsonObject.getString("id"));
                        item.setNsme(jsonObject.getString("name"));
                        item.setMobile(jsonObject.getString("phone"));
                        inviteelist.add(item);


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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.dismiss();

        return dialog;
    }

}
