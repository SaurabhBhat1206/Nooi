package com.events.hanle.events.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.AlreadyInvitedUser;
import com.events.hanle.events.Model.Attending;
import com.events.hanle.events.R;
import com.events.hanle.events.adapter.InviteeListAdapter;
import com.events.hanle.events.app.MyApplication;

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
    private List<Attending> inviteelist = new ArrayList<>();
    private List<AlreadyInvitedUser> alreadyinvited = new ArrayList<>();
    InviteeListAdapter adapter;
    private String TAG = "InviteeList";
    Context ctx;
    TextView t;
    AppCompatButton invite;
    AppCompatEditText ed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.invitee_list, container, false);
        rv = (RecyclerView) v.findViewById(R.id.recyclerView_for_invitee_list);
        invite = (AppCompatButton) v.findViewById(R.id.invite_btn);
        t = (TextView) v.findViewById(R.id.tm);
        ed = (AppCompatEditText) v.findViewById(R.id.toolbar_title);


        if (ConnectionDetector.isInternetAvailable(getActivity())) {
            fetchattendinglist();
        } else {
            Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
        }

        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (count < 0 || count ==0) {
                    rv.setVisibility(View.GONE);

                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String s1 = "" + s;

                if (count < 0 || count ==0) {
                    rv.setVisibility(View.GONE);


                } else {
                    final List<Attending> filteredModelList = filter(inviteelist, s1);
                    adapter.setFilter(filteredModelList);
                    adapter.setFilter(inviteelist);
                    rv.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return v;
    }


    private List<Attending> filter(List<Attending> models, String query) {
        query = query.toLowerCase();
        final List<Attending> filteredModelList = new ArrayList<>();
        rv.setLayoutManager(new LinearLayoutManager(ctx));
        adapter = new InviteeListAdapter(getActivity(), filteredModelList, alreadyinvited, invite, getDialog());
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        for (Attending model : models) {
            final String text = model.getNsme().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


    private void fetchattendinglist() {


        String endpoint = WebUrl.ORGANISER_Invitee_list.replace("ORGANISER_ID", MyApplication.getInstance().getPrefManager().getEventId().getOrganiserId());
        String endpoint1 = endpoint.replace("EVENT_ID", com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEventId());
        Log.e(TAG, "end point: " + endpoint1);


        StringRequest strReq = new StringRequest(Request.Method.GET,
                endpoint1, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);


                try {
                    JSONObject res = new JSONObject(response);
                    System.out.println("response:" + response);
                    JSONArray jsonArray = res.getJSONArray("server_response");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Attending item = new Attending();

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        item.setId(jsonObject.getInt("id"));
                        item.setNsme(jsonObject.getString("name") + " " + jsonObject.getString("last_name"));
                        item.setMobile("+" + jsonObject.getString("countrycode") + " " + jsonObject.getString("phone"));
                        inviteelist.add(item);

                        JSONArray invitedarray = res.getJSONArray("alreadyinvited");
                        for (int j = 0; j < invitedarray.length(); j++) {

                            AlreadyInvitedUser alreadyInvitedUser = new AlreadyInvitedUser();
                            JSONObject jsonO = invitedarray.getJSONObject(j);
                            alreadyInvitedUser.setAlreadyinvited(jsonO.getInt("userid"));
                            alreadyinvited.add(alreadyInvitedUser);

                            System.out.println("alredyinvi" + alreadyinvited.get(j).getAlreadyinvited());
                        }

                        System.out.println("Invited" + inviteelist.get(i).getId());

                    }


                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity(), "Server did not respond!!", Toast.LENGTH_LONG).show();
                }

//                adapter.notifyDataSetChanged();

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
    public void onResume() {
        super.onResume();

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.


        Dialog dialog = super.onCreateDialog(savedInstanceState);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.TOP | Gravity.CENTER;
        wmlp.x = -5;   //x position
        wmlp.y = 0;


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        //dialog.dismiss();

        return dialog;
    }

}
