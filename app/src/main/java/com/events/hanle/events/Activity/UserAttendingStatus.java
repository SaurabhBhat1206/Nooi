package com.events.hanle.events.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.R;
import com.events.hanle.events.SqlliteDB.DBController;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class UserAttendingStatus extends AppCompatActivity {

    private static final String TAG = "UserAttendingStatus";
    private String eventinfoID, selection_strng, user_id, username, mobileno;
    private TextView textView, msg, displayevents;
    private ProgressDialog pDialog;
    private String total_value, countrycode;
    private Button next;
    private RadioGroup rg;
    private RadioButton yes, no;
    private TextInputLayout textInputLayout;
    private AppCompatEditText appCompatEditText;
    private DBController dbController;
    private ArrayList<String> mylist = new ArrayList<>();
    private AVLoadingIndicatorView avi;
    private ImageView imageView;
    private String event_date, eventtype, noodays, enddate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_attening_status_new);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        textView = (TextView) findViewById(R.id.textView4);
        TextView t1 = (TextView) findViewById(R.id.toolbar_title);
        if (getIntent().getStringExtra("event_title") != null) {
            t1.setText(getIntent().getStringExtra("event_title"));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");

        eventtype = getIntent().getStringExtra("event_type");
        noodays = getIntent().getStringExtra("noofdays");
        enddate = getIntent().getStringExtra("enddate");

        next = (Button) findViewById(R.id.next);
        rg = (RadioGroup) findViewById(R.id.rgroup);
        yes = (RadioButton) findViewById(R.id.attending);
        no = (RadioButton) findViewById(R.id.not_attending);
        eventinfoID = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEventId();
        username = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getName();
        countrycode = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getCountrycode();
        textInputLayout = (TextInputLayout) findViewById(R.id.not_attending_reason);
        appCompatEditText = (AppCompatEditText) findViewById(R.id.reason);
        msg = (TextView) findViewById(R.id.t1);
        //displayevents = (TextView) findViewById(R.id.display_events);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        imageView = (ImageView) findViewById(R.id.bell);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        UpdateUI();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                String checked = (String) checkedRadioButton.getText();
                if (checked.equalsIgnoreCase("Not Attending") && eventtype != null && Integer.parseInt(eventtype) == 1) {
                    textInputLayout.setVisibility(View.VISIBLE);
                    next.setText("Send");

                } else if (checked.equalsIgnoreCase("Attending")) {
                    textInputLayout.setVisibility(View.GONE);
                    next.setText("Confirm");

                }

            }
        });

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
            eventinfoID = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEventId();
            mobileno = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getMobile();
            String user_reason = appCompatEditText.getText().toString().trim();
            if (selection_strng != null) {
                sendDataAttending(selection_strng, user_reason);

            } else {
                Toast.makeText(getApplicationContext(), "Please choose the option!!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void sendDataAttending(final String selectionStrng, final String user_Reason) {

        pDialog = new ProgressDialog(UserAttendingStatus.this);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebUrl.USER_ATTENDING_NEW, new Response.Listener<String>() {

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


                        if (selectionStrng.equalsIgnoreCase("attending")) {
                            if (success == 1) {

                                Intent intent = new Intent(UserAttendingStatus.this, UserTabView.class);
                                intent.putExtra("event_title", getIntent().getStringExtra("event_title"));
                                intent.putExtra("share_detail", getIntent().getStringExtra("share_detail"));
                                intent.putExtra("artwork", getIntent().getStringExtra("artwork"));
                                intent.putExtra("eventtype", getIntent().getStringExtra("eventtype"));
                                intent.putExtra("chatw", getIntent().getStringExtra("chatw"));
                                intent.putExtra("acknw", getIntent().getStringExtra("acknw"));
                                startActivity(intent);
                                finish();
                                Toast.makeText(UserAttendingStatus.this, message, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(UserAttendingStatus.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } else if (selectionStrng.equalsIgnoreCase("Not Attending")) {
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
                params.put("eventId", eventinfoID);
                params.put("phone", mobileno);
                params.put("countrycode", countrycode);
                params.put("user_response", selectionStrng);
                if ((user_Reason.length() > 0) && (user_Reason != null) && (!user_Reason.equals(""))) {
                    params.put("user_reasons", user_Reason);
                } else {
                    params.put("user_reasons", "");
                }
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

    private class DbParser extends AsyncTask<Void, Void, ArrayList<String>> {


        @Override
        protected ArrayList<String> doInBackground(Void... args) {

            // new push notification is received
            dbController = new DBController(getApplicationContext());

            //Toast.makeText(context, "Schedled push from broadcast", Toast.LENGTH_SHORT).show();
            ArrayList<HashMap<String, String>> animalList = dbController.getActiveEvents();
//            for (HashMap<String, String> entry : animalList)
//            {
//
//                System.out.println("Data base date"+entry.get("dat"));
//
//            }

            int et = Integer.parseInt(noodays);

            if (animalList.size() != 0) {
                animalList = dbController.getActiveEvents();
                for (HashMap<String, String> entry : animalList) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date dbdate = formatter.parse(entry.get("dat"));
                        String dbdates = formatter.format(dbdate);
                        Date dateplus2, dateplus3;
                        Calendar c = Calendar.getInstance();
                        c.setTime(dbdate);
                        //adding one day
                        c.add(Calendar.DATE, 1);
                        dateplus2 = c.getTime();
                        String dateplustwo = formatter.format(dateplus2);
                        //adding 2 days
                        c.add(Calendar.DATE, 2);
                        dateplus3 = c.getTime();
                        String dateplusthree = formatter.format(dateplus3);
                        Date eventdate = null;
                        String ed = null;
                        if (event_date != null) {
                            eventdate = formatter.parse(event_date);
                            ed = formatter.format(eventdate);

                        }

//                        System.out.println("db date:" + formatter.format(dbdate));
//                        System.out.println("db date:" + dateplustwo);
//                        System.out.println("db date:" + dateplus2);
//                        System.out.println("db date:" + dateplusthree);
                        System.out.println("db date:" + ed);
                        System.out.println("db date:" + dbdate);
                        System.out.println("db date:" + dateplustwo);
                        System.out.println("db date:" + dateplusthree);



                        if ((ed != null) && (ed.equals(dbdate)) && !(eventinfoID.equals(entry.get("eID")))) {
                            //System.out.println("today in" + entry.get("event_title"));
                            mylist.add(entry.get("event_title"));

                        } else if (dateplustwo != null && eventdate != null && ed.equals(dateplustwo) && !eventinfoID.equals(entry.get("eID"))) {
                            mylist.add(entry.get("event_title"));
                            System.out.println("db date 2:" + dateplus2);

                        } else if (dateplusthree != null && eventdate != null && ed.equals(dateplusthree) && !eventinfoID.equals(entry.get("eID"))) {
                            mylist.add(entry.get("event_title"));
                            System.out.println("db date 3:" + dateplus2);

                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

            }
            return mylist;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(ArrayList<String> events) {
            if (events.size() > 0) {
                int k = 0;
                StringBuilder sb = new StringBuilder();
                sb.append("You have event/s on the same day!");
                sb.append("\n");
                for (String event : events) {
                    sb.append(++k);
                    sb.append(".");
                    sb.append(event);
                    sb.append("\n");

                }

                msg.setText(sb);
                msg.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);

            }

        }
    }


    private void UpdateUI() {
        // Tag used to cancel the request
        avi.show();
        avi.setVisibility(View.VISIBLE);
        String url = WebUrl.ATTENDING_OR_NOT + eventinfoID;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                avi.hide();
                avi.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                Log.d(TAG, response.toString());
                Log.d(TAG, response.toString());
                try {

                    String organiser_name = response.getString("event_creater_username");
                    String user_name = response.getString("user_name");
                    String event_name = response.getString("event_type");
                    event_date = response.getString("event_date");
                    String address = response.getString("address");
                    String event_time = response.getString("event_time");
                    String rsvp_date = response.getString("rsvp_date");
                    String rsvp_timr = response.getString("rsvp_time");

                    if (rsvp_date.equals("") && rsvp_date.equals("") && event_date != null) {
                        total_value = "Dear " + username + ", You are cordailly invited for the event -" + event_name + " on " + event_date + " at " + address + " by " + organiser_name;
                        textView.setText(total_value);
                    } else {
                        total_value = "Dear " + username + ", You are cordailly invited for the event -" + event_name + " on " + event_date + " at " + address + " by " + organiser_name + "\n" + " RSVP Date: " + rsvp_date + " Time " + rsvp_timr;
                        textView.setText(total_value);
                    }
                    new DbParser().execute();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                avi.hide();
                avi.setVisibility(View.GONE);
                VolleyLog.d(TAG, "Error: " + error.getMessage());
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
