package com.events.hanle.events.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UserAttendingStatus extends AppCompatActivity {

    private static final String TAG = "UserAttendingStatus";
    String eventinfoID, selection_strng, user_id, username, mobileno;
    TextView textView;
    ProgressDialog pDialog;
    String total_value, countrycode;
    Button next;
    RadioGroup rg;
    RadioButton yes, no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_attending_status);
        textView = (TextView) findViewById(R.id.textView4);
        next = (Button) findViewById(R.id.next);
        rg = (RadioGroup) findViewById(R.id.rgroup);
        yes = (RadioButton) findViewById(R.id.attending);
        no = (RadioButton) findViewById(R.id.not_attending);
        eventinfoID = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getId();
        username = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getName();
        countrycode = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getCountrycode();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        //Toast.makeText(UserAttendingStatus.this, String.valueOf(eventinfoID), Toast.LENGTH_SHORT).show();
        UpdateUI();

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
            user_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUserId().getId();
            eventinfoID = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getId();
            mobileno = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getMobile();

            if (selection_strng.equalsIgnoreCase("Not Attending")) {
                if (ConnectionDetector.isInternetAvailable(UserAttendingStatus.this)) {
                    sendDataNotAttending();

                }

            } else {
                sendDataAttending();

            }
        }
    }

    private void sendDataAttending() {

        pDialog = new ProgressDialog(UserAttendingStatus.this);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebUrl.USER_ATTENDING, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                pDialog.hide();

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

                            Intent intent = new Intent(UserAttendingStatus.this, UserTabView.class);
                            intent.putExtra("event_title", getIntent().getStringExtra("event_title"));
                            intent.putExtra("share_detail", getIntent().getStringExtra("share_detail"));
                            startActivity(intent);
                            finish();
                            Toast.makeText(UserAttendingStatus.this, message, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(UserAttendingStatus.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // login error - simply toast the message
                        Toast.makeText(UserAttendingStatus.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(UserAttendingStatus.this, "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.error_network_timeout),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.error_network_server),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UserAttendingStatus.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
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

    private void sendDataNotAttending() {

        pDialog = new ProgressDialog(UserAttendingStatus.this);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebUrl.USER_NOTATTENDING, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                pDialog.hide();

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

                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            Toast.makeText(UserAttendingStatus.this, message, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(UserAttendingStatus.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // login error - simply toast the message
                        Toast.makeText(UserAttendingStatus.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(UserAttendingStatus.this, "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.error_network_timeout),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.error_network_server),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UserAttendingStatus.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("eventinfoID", eventinfoID);
                params.put("phone", mobileno);

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

    private void UpdateUI() {
        // Tag used to cancel the request
        String tag_string_req = "string_req";
        showpDialog();
        String url = WebUrl.LIST_CONFIRMATION + eventinfoID;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.d(TAG, response.toString());
                pDialog.hide();
                try {

                    String organiser_name = response.getString("event_creater_username");
                    String user_name = response.getString("user_name");
                    String event_name = response.getString("event_type");
                    String event_date = response.getString("event_date");
                    String address = response.getString("address");
                    String event_time = response.getString("event_time");
                    String rsvp_date = response.getString("rsvp_date");
                    String rsvp_timr = response.getString("rsvp_time");

                    if (rsvp_date.equals("") && rsvp_date.equals("")) {
                        total_value = "Dear " + username + ", You are cordailly invited for the event -" + event_name + " on " + event_date + " at " + address + " by " + organiser_name;
                        textView.setText(total_value);
                    } else {
                        total_value = "Dear " + username + ", You are cordailly invited for the event -" + event_name + " on " + event_date + " at " + address + " by " + organiser_name + "\n" + " RSVP Date: " + rsvp_date + " Time " + rsvp_timr;
                        textView.setText(total_value);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidepDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.error_network_timeout),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(),
                            getApplicationContext().getString(R.string.error_network_server),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UserAttendingStatus.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        // Adding request to request queue
        queue.add(jsonObjReq);

    }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
