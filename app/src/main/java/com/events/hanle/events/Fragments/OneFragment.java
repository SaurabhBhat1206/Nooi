package com.events.hanle.events.Fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.DividerItemDecoration;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.FeedItem;
import com.events.hanle.events.R;
import com.events.hanle.events.SqlliteDB.DBController;
import com.events.hanle.events.adapter.MyRecyclerAdapter;
import com.events.hanle.events.app.Config;
import com.events.hanle.events.app.EndPoints;
import com.events.hanle.events.gcm.GcmIntentService;
import com.events.hanle.events.interf.BackPressListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class OneFragment extends Fragment {

    private static final String TAG = "OneFragment";
    private List<FeedItem> feedsList = new ArrayList<FeedItem>();
    //private RecyclerView mRecyclerView;
    // private MyRecyclerAdapter adapter;
    Context ctx;
    View mainview;
    TextView t;
    String event_id, mobileno, countrycode, n;
    //private SwipeRefreshLayout mSwipeRefreshLayout = null;
    //private AVLoadingIndicatorView avi;
    Activity activity;
    DBController dbController;
    HashMap<String, String> eventdetails;
    CardView organisername, card_location, card_date_time, event_detials, card_paid_by_title, card_dresscode;
    TextView orgname, address, date_time, eventdesc, paid, dresscode, org, cnt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        mainview = inflater.inflate(R.layout.list_row_new, container, false);
        // avi = (AVLoadingIndicatorView) mainview.findViewById(R.id.avi);
        //mSwipeRefreshLayout = (SwipeRefreshLayout) mainview.findViewById(R.id.swipeRefreshLayout);
        organisername = (CardView) mainview.findViewById(R.id.card_organisername);
        card_location = (CardView) mainview.findViewById(R.id.card_location);
        card_date_time = (CardView) mainview.findViewById(R.id.card_date_time);
        card_date_time = (CardView) mainview.findViewById(R.id.card_date_time);
        event_detials = (CardView) mainview.findViewById(R.id.event_detials);
        card_paid_by_title = (CardView) mainview.findViewById(R.id.card_paid_by_title);
        card_dresscode = (CardView) mainview.findViewById(R.id.card_dresscode);

        orgname = (TextView) mainview.findViewById(R.id.organiser_name);
        address = (TextView) mainview.findViewById(R.id.address);
        date_time = (TextView) mainview.findViewById(R.id.dat_tim);
        eventdesc = (TextView) mainview.findViewById(R.id.des);
        paid = (TextView) mainview.findViewById(R.id.paidby);
        dresscode = (TextView) mainview.findViewById(R.id.dresscode);
        org = (TextView) mainview.findViewById(R.id.organiser);
        cnt = (TextView) mainview.findViewById(R.id.contact);

        Config.typeface = Typeface.createFromAsset(getActivity().getAssets(), "font/Verdana.ttf");
        orgname.setTypeface(Config.typeface);
        address.setTypeface(Config.typeface);
        date_time.setTypeface(Config.typeface);
        eventdesc.setTypeface(Config.typeface);
        paid.setTypeface(Config.typeface);
        dresscode.setTypeface(Config.typeface);
        org.setTypeface(Config.typeface);
        cnt.setTypeface(Config.typeface);


        String s = getActivity().getIntent().getStringExtra("classcheck");
        dbController = new DBController(getActivity());

        if (s != null) {
            if (s.equalsIgnoreCase("cancelledevent")) {
                event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getId();
                eventdetails = dbController.getfromCancelledEventId(event_id);
            } else if (s.equalsIgnoreCase("completedevent")) {
                event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getId();
                eventdetails = dbController.getfromConcludedEventId(event_id);
            }
        } else {
            event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getId();
            subscribeToAllTopics(event_id);
            eventdetails = dbController.getfromEventId(event_id);

        }
        mobileno = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getMobile();
        countrycode = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getCountrycode();

        return mainview;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ConnectionDetector.isInternetAvailable(getActivity())) {
            String endpoint = WebUrl.EVENT_DETAILS.replace("MOBILE_NO", mobileno);
            String endpoint1 = endpoint.replace("EVENT_ID", event_id);
            System.out.println("One fragment:" + endpoint1);
            new AsyncHttpTask().execute(endpoint1 + countrycode);
        } else {

            callOfflineMethod();

        }
    }

    private void callOfflineMethod() {
        // mSwipeRefreshLayout.setVisibility(View.VISIBLE);

        if (eventdetails.size() != 0) {
            System.out.println("sql op:" + eventdetails.get("inviter_name"));
            //feedsList = new ArrayList<>();
            FeedItem item = new FeedItem();
            item.setEvent_creator_name(eventdetails.get("inviter_name"));
            item.setOrgnaserphone(eventdetails.get("countrycode") + " " + eventdetails.get("phone"));
            item.setEventdesc(eventdetails.get("descriptions"));
            item.setAddress(eventdetails.get("eventaddress"));
            item.setDate(eventdetails.get("dat"));
            item.setTime(eventdetails.get("tim"));
            item.setEventname(eventdetails.get("event_title"));
            item.setPayment(eventdetails.get("payment"));
            item.setDresscode(eventdetails.get("dresscode"));
            item.setTimezone(eventdetails.get("timezone"));
            item.setWeekday(eventdetails.get("weekday"));
            String est = eventdetails.get("establishment");

            assert est != null;
            if (est != null || !est.equals("")) {
                item.setEstablishmantname(est);
            }
            //feedsList.add(item);
            FeedItem feedItem = new FeedItem(eventdetails.get("event_status"));
            com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().storeEventInfoID(feedItem);
            EndPoints.LATITUDE = eventdetails.get("latitude");
            EndPoints.LONGITUDE = eventdetails.get("longitude");
            EndPoints.EVENTNAME = eventdetails.get("event_type");
            EndPoints.EVENTTIME = eventdetails.get("event_time");

        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{

                    Manifest.permission.CALL_PHONE, Manifest.permission.CALL_PHONE

            }, 10);
            return;
        }

    }


    public void subscribeToAllTopics(String eventID) {


        Intent intent = new Intent(getActivity(), GcmIntentService.class);
        intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
        //intent.putExtra(GcmIntentService.TOPIC, "topic_" + eventID);
        intent.putExtra(GcmIntentService.TOPIC, "topic_" + "test_android_ios");
        getActivity().startService(intent);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {

            //avi.show();

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

            } else {

                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(final String result) {

        try {
            final JSONObject response = new JSONObject(result);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder sb = new StringBuilder();
                    orgname.setText(response.optString("event_creater_username"));
                    eventdesc.setText(response.optString("description"));
                    String s = response.optString("event_date");
                    String tim = response.optString("event_time");
                    String tims = s + " " + tim;
                    if (tims != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                        Date d;
                        try {
                            d = sdf.parse(tims);
                            sdf.applyPattern("dd MMM EEE | hh:mm a");
                            n = sdf.format(d);

                            System.out.println("Conversiondatee****:" + n);

                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    sb.append(n).append(" GMT ").append("(").append(response.optString("timezone")).append(")");
                    date_time.setText(sb);

                    if (response.optString("payment").equals("")) {
                        card_paid_by_title.setVisibility(View.GONE);
                    } else {
                        paid.setText(response.optString("payment"));
                    }
                    if (response.optString("dresscode").equals("")) {
                        card_dresscode.setVisibility(View.GONE);
                    } else {
                        dresscode.setText(response.optString("dresscode"));
                    }

                    if (response.has("establishment") && response.optString("establishment") != null) {
                        address.setText(response.optString("establishment") + " , " + response.optString("event_address"));

                    } else {
                        address.setText(response.optString("event_address"));

                    }

                    EndPoints.LATITUDE = response.optString("latitude");
                    EndPoints.LONGITUDE = response.optString("longitude");
                    EndPoints.EVENTNAME = response.optString("event_type");
                    EndPoints.EVENTTIME = response.optString("event_time");

                    cnt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 2909);
                                } else {
                                    // continue with your code
                                    callOrganiser(response.optString("event_creator_phone"));

                                }
                            } else {
                                // continue with your code
                                callOrganiser(response.optString("event_creator_phone"));

                            }
                        }
                    });

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void callOrganiser(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + "+" + phone));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        getActivity().startActivity(callIntent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
