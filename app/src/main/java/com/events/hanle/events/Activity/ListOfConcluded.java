package com.events.hanle.events.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.CanceledEvent;
import com.events.hanle.events.Model.ConcludedEventView;
import com.events.hanle.events.Model.FeedItem;
import com.events.hanle.events.R;
import com.events.hanle.events.adapter.ConcludedEventViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ListOfConcluded extends AppCompatActivity {
    private List<ConcludedEventView> feedsList = new ArrayList<ConcludedEventView>();
    private RecyclerView mRecyclerView;
    private ConcludedEventViewAdapter adapter;
    Context ctx;
    private AlertDialog progressDialog;
    TextView t;
    String event_id, mobileno, countrycode;
    private static final String TAG = "ListOfConcluded";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragments_concluded_view);
        String event_title = getIntent().getStringExtra("event_title");
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        assert t != null;
        t.setTitleTextColor(Color.WHITE);
        setSupportActionBar(t);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(event_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        mRecyclerView.setHasFixedSize(true);

        event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getId();
        mobileno = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getMobile();
        countrycode = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getCountrycode();

        if (ConnectionDetector.isInternetAvailable(ListOfConcluded.this)) {
            String endpoint = WebUrl.USER_EVENT_URL.replace("MOBILE_NO", mobileno);
            String endpoint1 = endpoint.replace("EVENT_ID", event_id);
            System.out.println("One fragment:" + endpoint1);
            new AsyncHttpTask().execute(endpoint1 + countrycode);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet!!", Toast.LENGTH_SHORT).show();
        }


    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ListOfConcluded.this);
            progressDialog.setMessage("Loading Please Wait...");
            progressDialog.show();

        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    System.out.println("TAG:" + response);
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fe
                    // tch data!";
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI


            if (result == 1) {
                adapter = new ConcludedEventViewAdapter(ListOfConcluded.this, feedsList);
                mRecyclerView.setAdapter(adapter);
                progressDialog.hide();

            } else {
                progressDialog.hide();
                Toast.makeText(ListOfConcluded.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            System.out.println("TAG:" + response);
            String user_status;
            user_status = response.getString("user_status");

            System.out.println("TAG:" + user_status);
            int status = Integer.parseInt(user_status);
            if (status == 3 || status == 1) {
                RunSeparteThread(status);

            } else {
                feedsList = new ArrayList<>();
                ConcludedEventView item = new ConcludedEventView();
                item.setEvent_creator_name(response.optString("event_creater_username"));
                item.setOrgnaserphone(response.optString("event_creator_phone"));
                item.setEventdesc(response.optString("description"));
                item.setAddress(response.optString("event_address"));
                item.setDate(response.optString("event_date"));
                item.setTime(response.optString("event_time"));
                item.setEventname(response.optString("event_type"));
                item.setPayment(response.optString("payment"));
                item.setDresscode(response.optString("dresscode"));
                item.setTimezone(response.optString("timezone"));
                item.setWeekday(response.optString("weekday"));
                feedsList.add(item);
                FeedItem feedItem = new FeedItem(response.getString("event_status"));
                com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().storeEventInfoID(feedItem);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void RunSeparteThread(final int status) {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.hide();
                t.setVisibility(View.VISIBLE);
                if (status == 3) {
                    t.setText("You have cancelled this event!!!");
                    mRecyclerView.setVisibility(View.GONE);
                } else if (status == 1) {
                    t.setText("You are invited please confirm!");
                    mRecyclerView.setVisibility(View.GONE);
                }

            }
        });
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

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
