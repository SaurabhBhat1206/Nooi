package com.events.hanle.events.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.R;
import com.events.hanle.events.gcm.GcmIntentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class UserChangeOfDescision extends AppCompatActivity {
    private static final String TAG = "UserChangeOfDescision";
    Button next;
    RadioGroup rg;
    AppCompatRadioButton yes, no;
    private AlertDialog progressDialog;
    private String selection_strng, eventinfoID, user_id, mobileno, countrycode;
    private TextView caption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapdetails);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(t);
        assert t != null;
        t.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewsById();
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
            eventinfoID = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEventId();
        }

        user_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getId();
        //Toast.makeText(UserChangeOfDescision.this, user_id, Toast.LENGTH_SHORT).show();
        String eventype = getIntent().getStringExtra("eventtype");
        if(eventype!=null){
            int et = Integer.parseInt(eventype);
            if(et==1){
                caption.setText(getResources().getString(R.string.tell_the_organiser_that_you_are_not_attending));
            } else{
                caption.setText(R.string.sure);
            }
        }


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                radiobuttonAction();
            }


        });

    }


    private void radiobuttonAction() {
        if (rg.getCheckedRadioButtonId() != -1) {
            int id = rg.getCheckedRadioButtonId();
            View radioButton = rg.findViewById(id);
            int radioId = rg.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) rg.getChildAt(radioId);
            selection_strng = (String) btn.getText();
            mobileno = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getMobile();
            countrycode = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getCountrycode();

            if (selection_strng.equalsIgnoreCase("yes")) {
                NotifyAdmin();
            } else {
                onBackPressed();
            }
        }
    }

    private void NotifyAdmin() {

        progressDialog = new ProgressDialog(UserChangeOfDescision.this);
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebUrl.USER_CHNAGE_DECISION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                progressDialog.hide();

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.length() != 0) {
                        String message;
                        int success;
                        // user successfully logged in
                        success = obj.getInt("success");
                        message = obj.getString("message");

                        if (success == 1) {
                            unsubscribeTopic();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            Toast.makeText(UserChangeOfDescision.this, message, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(UserChangeOfDescision.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // login error - simply toast the message
                        Toast.makeText(UserChangeOfDescision.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(UserChangeOfDescision.this, "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(UserChangeOfDescision.this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("status", selection_strng);
                params.put("eventinfoID", eventinfoID);
                params.put("phone", mobileno);
                params.put("countrycode", countrycode);
                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                WebUrl.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to request queue
        com.events.hanle.events.app.MyApplication.getInstance().addToRequestQueue(strReq);

    }

    private void unsubscribeTopic(){

        Intent intent = new Intent(UserChangeOfDescision.this, GcmIntentService.class);
        intent.putExtra(GcmIntentService.KEY, GcmIntentService.UNSUBSCRIBE);
        intent.putExtra(GcmIntentService.TOPIC, "topic_" + eventinfoID);
        startService(intent);
    }

    private void findViewsById() {

        next = (Button) findViewById(R.id.next);
        rg = (RadioGroup) findViewById(R.id.rgroup1);
        yes = (AppCompatRadioButton) findViewById(R.id.yes_r);
        no = (AppCompatRadioButton) findViewById(R.id.no_r);
        caption = (TextView) findViewById(R.id.caption);
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
