package com.events.hanle.events.Activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Fragments.AttendingDialogFragment;
import com.events.hanle.events.Fragments.CouponDetials;
import com.events.hanle.events.Fragments.InviteeList;
import com.events.hanle.events.Fragments.MuteDialog;
import com.events.hanle.events.Fragments.OneFragment;
import com.events.hanle.events.Fragments.OrganiserLoginDialog;
import com.events.hanle.events.Fragments.Three;
import com.events.hanle.events.Fragments.TwoFragment;
import com.events.hanle.events.R;
import com.events.hanle.events.app.Config;
import com.events.hanle.events.app.MyApplication;
import com.events.hanle.events.chat.ChatRoomActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class UserTabView extends AppCompatActivity {

    private static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static TextView badge, badge1;

    String eventtype;
    ProgressDialog pDialog;
    private static final String TAG = "UserTabView";
    private String event_id, countrycode, mobileno, acknw;
    private TextView eventtitle;
    public static final String MyPREFERENCES = "PrefChat";
    SharedPreferences sharedpreferences;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tab_view);
        String event_title = getIntent().getStringExtra("event_title");
        // Toast.makeText(UserTabView.this, event_title, Toast.LENGTH_SHORT).show();
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        eventtitle = (TextView) findViewById(R.id.event_title);
        assert t != null;
        t.setTitleTextColor(Color.WHITE);
        setSupportActionBar(t);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        t.setTitle("");
        if (event_title != null) {
            eventtitle.setText(event_title);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        assert viewPager != null;
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);
        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        eventtype = getIntent().getStringExtra("eventtype");
        acknw = getIntent().getStringExtra("acknw");

        if (eventtype == null) {
            eventtype = "1";
        }

        String s = getIntent().getStringExtra("classcheck");
        if (s != null) {
            if (s.equalsIgnoreCase("cancelledevent")) {
                event_id = MyApplication.getInstance().getPrefManager().getCancelledEventID().getId();
                countrycode = MyApplication.getInstance().getPrefManager().getCancelledEventID().getCountrycode();
                mobileno = MyApplication.getInstance().getPrefManager().getCancelledEventID().getPhone();
            } else if (s.equalsIgnoreCase("completedevent")) {
                event_id = MyApplication.getInstance().getPrefManager().getCompletedEventId().getId();
                countrycode = MyApplication.getInstance().getPrefManager().getCompletedEventId().getCountrycode();
                mobileno = MyApplication.getInstance().getPrefManager().getCompletedEventId().getPhone();
            }
        } else {
            event_id = MyApplication.getInstance().getPrefManager().getEventId().getEventId();
            countrycode = MyApplication.getInstance().getPrefManager().getEventId().getCountrycode();
            mobileno = MyApplication.getInstance().getPrefManager().getEventId().getPhone();
        }


        setupTabIcons();


    }

    @Override
    protected void onResume() {
        super.onResume();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                SharedPreferences.Editor editor = sharedpreferences.edit();

                if (position == 3) {
                    editor.remove("chateventID" + event_id);
                    editor.apply();
                    if (badge != null) {
                        badge.setVisibility(View.GONE);
                    }

                } else if (position == 2) {
                    editor.remove("organisereventID" + event_id);
                    editor.remove("partnereventID" + event_id);
                    editor.apply();
                    if (badge1 != null) {
                        badge1.setVisibility(View.GONE);

                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int ackflag = 0;

        if (acknw != null) {
            ackflag = Integer.parseInt(acknw);
        }
        int et = Integer.parseInt(eventtype);
        UserTabView.this.invalidateOptionsMenu();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_tab, menu);
        MenuItem notattending, organiserlogin, invite, coupon_details,mute;
        notattending = menu.findItem(R.id.not_attending);
        organiserlogin = menu.findItem(R.id.organiser_login);
        invite = menu.findItem(R.id.invite);
        coupon_details = menu.findItem(R.id.coupon_detials);
        mute = menu.findItem(R.id.mute);
        if (countrycode != null && MyApplication.getInstance().getPrefManager().getUser().getCountrycode() != null && mobileno != null && MyApplication.getInstance().getPrefManager().getUser().getMobile() != null) {
            if (!(countrycode.equals(MyApplication.getInstance().getPrefManager().getUser().getCountrycode()
            ) && (mobileno.equals(MyApplication.getInstance().getPrefManager().getUser().getMobile())))) {
                invite.setEnabled(false);
                coupon_details.setEnabled(false);
                organiserlogin.setEnabled(false);
            }
        }

        if (et == 2) {
            organiserlogin.setVisible(false);
            invite.setVisible(false);
            coupon_details.setVisible(false);
        }

        if (et == 2 && ackflag == 1) {
            notattending.setVisible(false);

        } else if (et == 2 && ackflag == 0) {
            notattending.setVisible(true);

        }


        if (getIntent().getStringExtra("classcheck") != null || getIntent().getStringExtra("classcheck") != null) {
            if (getIntent().getStringExtra("classcheck").equals("cancelledevent") || getIntent().getStringExtra("classcheck").equals("completedevent")) {
                notattending.setVisible(false);
                organiserlogin.setVisible(false);
                invite.setVisible(false);
                coupon_details.setVisible(false);
                mute.setVisible(false);
            }
        }
        return true;
    }


    private void setupTabIcons() {


        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        Config.typeface = Typeface.createFromAsset(getAssets(), "font/Verdana.ttf");
        tabOne.setTypeface(Config.typeface);
        tabOne.setText("Event");
        tabOne.setTextColor(Color.parseColor("#3B4673"));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_events, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        Config.typeface = Typeface.createFromAsset(getAssets(), "font/Verdana.ttf");
        tabOne.setTypeface(Config.typeface);
        tabTwo.setText("Venue");
        tabTwo.setTextColor(Color.parseColor("#3B4673"));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_map, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        String partnerID = sharedpreferences.getString("organisereventID" + event_id, null);
        String organiserId = sharedpreferences.getString("partnereventID" + event_id, null);
        if (partnerID != null || organiserId != null) {
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab2, null);
            TextView tabo = (TextView) v.findViewById(R.id.tab_mailbag);
            Config.typeface = Typeface.createFromAsset(getAssets(), "font/Verdana.ttf");
            badge1 = (TextView) v.findViewById(R.id.tab_counter_mailbag);
            //ImageView img = (ImageView) v.findViewById(R.id.tabm);
            badge1.setVisibility(View.VISIBLE);
            tabo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_mailbag, 0, 0);
            tabo.setTypeface(Config.typeface);
            tabo.setText("Mailbag");
            tabo.setTextColor(Color.parseColor("#3B4673"));
            tabLayout.getTabAt(2).setCustomView(v);
        } else {
            TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            Config.typeface = Typeface.createFromAsset(getAssets(), "font/Verdana.ttf");
            tabOne.setTypeface(Config.typeface);
            tabThree.setText("Mailbag");
            tabThree.setTextColor(Color.parseColor("#3B4673"));
            tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_mailbag, 0, 0);
            tabLayout.getTabAt(2).setCustomView(tabThree);
        }

        String eventId = sharedpreferences.getString("chateventID" + event_id, null);
        if (eventId != null) {
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab1, null);
            Config.typeface = Typeface.createFromAsset(getAssets(), "font/Verdana.ttf");
            TextView tabon = (TextView) v.findViewById(R.id.tab);
            badge = (TextView) v.findViewById(R.id.tab_counter);
            //ImageView img = (ImageView) v.findViewById(R.id.tab_indicator);
            tabon.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_chat, 0, 0);
            badge.setVisibility(View.VISIBLE);
            tabon.setTypeface(Config.typeface);
            tabon.setText("Chat");
            tabon.setTextColor(Color.parseColor("#3B4673"));
            tabLayout.getTabAt(3).setCustomView(v);

        } else {
            TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);

            Config.typeface = Typeface.createFromAsset(getAssets(), "font/Verdana.ttf");
            tabOne.setTypeface(Config.typeface);
            tabFour.setText("Chat");
            tabFour.setTextColor(Color.parseColor("#3B4673"));
            tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.selector_chat, 0, 0);
            tabLayout.getTabAt(3).setCustomView(tabFour);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int et, cW;

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.not_attending:


                Intent i = new Intent(getApplicationContext(), UserChangeOfDescision.class);
                i.putExtra("classcheck", getIntent().getStringExtra("classcheck"));
                i.putExtra("chat_room_id", getIntent().getExtras().getString("chat_room_id"));
                i.putExtra("eventId", getIntent().getExtras().getString("eventId"));
                i.putExtra("eventtype", eventtype);
                startActivity(i);

                return true;

            case R.id.create_event:
                calldialogue();
                return true;

            case R.id.organiser_login:
                //startActivity(new Intent(getApplicationContext(),OrganiserLogin.class));
                et = Integer.parseInt(eventtype);
                if (et == 2) {
                    Toasty.info(getApplicationContext(), "This feature is not available for this Event", Toast.LENGTH_SHORT, true).show();

                } else {
                    OrganiserLoginDialog dialogFragment = new OrganiserLoginDialog();
                    dialogFragment.show(getSupportFragmentManager(), "missiles");

                }
                return true;

            case R.id.feedback:
                sendFeedbackalert();
                return true;

            case R.id.about_us:
                aboutus();
                return true;

            case R.id.mute:
                mute();
                return true;
            case R.id.invite_image:

                et = Integer.parseInt(eventtype);

                if (ConnectionDetector.isInternetAvailable(UserTabView.this)) {
                    if (getIntent().getStringExtra("classcheck") != null) {
                        if (getIntent().getStringExtra("classcheck").equalsIgnoreCase("cancelledevent") || getIntent().getStringExtra("classcheck").equalsIgnoreCase("completedevent"))

                        {
                            Intent intent = new Intent(getApplicationContext(), EventArtwork.class);
                            intent.putExtra("classcheck", getIntent().getStringExtra("classcheck"));
                            intent.putExtra("chat_room_id", getIntent().getExtras().getString("chat_room_id"));
                            intent.putExtra("eventId", getIntent().getExtras().getString("eventId"));
                            intent.putExtra("artwork", getIntent().getExtras().getString("artwork"));
                            startActivity(intent);
                        }
                    } else if (et == 1) {
                        Intent intent = new Intent(getApplicationContext(), NooiGallery.class);
                        intent.putExtra("classcheck", getIntent().getStringExtra("classcheck"));
                        intent.putExtra("chat_room_id", getIntent().getExtras().getString("chat_room_id"));
                        intent.putExtra("eventId", getIntent().getExtras().getString("eventId"));
                        intent.putExtra("artwork", getIntent().getExtras().getString("artwork"));
                        startActivity(intent);
                    } else if (et == 2) {
                        Intent intent = new Intent(getApplicationContext(), EventArtwork.class);
                        intent.putExtra("classcheck", getIntent().getStringExtra("classcheck"));
                        intent.putExtra("chat_room_id", getIntent().getExtras().getString("chat_room_id"));
                        intent.putExtra("eventId", getIntent().getExtras().getString("eventId"));
                        intent.putExtra("artwork", getIntent().getExtras().getString("artwork"));
                        startActivity(intent);
                    }
                } else{
                    Toasty.warning(getApplicationContext(), "No Internet!!!!", Toast.LENGTH_SHORT, true).show();

                }


                return true;

            case R.id.list_invitee:
                if (ConnectionDetector.isInternetAvailable(UserTabView.this)) {

                    String sharedetails = getIntent().getStringExtra("share_detail");
                    eventtype = getIntent().getStringExtra("eventtype");

                    et = Integer.parseInt(eventtype);
                    //Toast.makeText(UserTabView.this, sharedetails, Toast.LENGTH_SHORT).show();
                    int s = Integer.parseInt(sharedetails);


                    if (sharedetails != null) {
                        if (s == 1 && et == 1) {
                            showDialog();

                        } else if (et == 2) {
                            Toasty.info(getApplicationContext(), "This feature is not available for this Event", Toast.LENGTH_SHORT, true).show();

                        } else {
                            Toasty.info(getApplicationContext(), "Organiser has not enabled this feature", Toast.LENGTH_SHORT, true).show();

                        }
                    } else {
                        Toasty.warning(getApplicationContext(), "Something went wrong!!", Toast.LENGTH_SHORT, true).show();

                    }
                } else {
                    Toasty.warning(getApplicationContext(), "No Internet!!!!", Toast.LENGTH_SHORT, true).show();

                }

                return true;

            case R.id.invite:
                InviteeList d = new InviteeList();
                d.show(getSupportFragmentManager(), "inviteelist");
                return true;

            case R.id.coupon_detials:
                if (ConnectionDetector.isInternetAvailable(UserTabView.this)) {
                    CouponDetials couponDetials = new CouponDetials();
                    couponDetials.show(getSupportFragmentManager(), "couponDetials");
                } else {
                    Toasty.warning(getApplicationContext(), "No Internet!!!!", Toast.LENGTH_SHORT, true).show();

                }


        }
        return super.onOptionsItemSelected(item);

    }

    private void mute() {

        android.app.FragmentManager man = getFragmentManager();
        MuteDialog muteDialog = new MuteDialog();
        muteDialog.show(man, "dialog");

    }

    private void aboutus() {
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Hanle Solutions")
                .setContentText("Version 1.04")
                .setCustomImage(R.drawable.images)
                .show();

    }


    private void showDialog() {

        android.app.FragmentManager manager = getFragmentManager();

        AttendingDialogFragment dialog = new AttendingDialogFragment();
        dialog.show(manager, "dialog");

    }

    private void calldialogue() {

        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Hanle Solutions")
                .setContentText("To create Event visit www.nooitheinviteapp.com")
                .setCustomImage(R.drawable.images)
                .show();

    }

    private void sendFeedbackalert() {

        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Hanle Solutions")
                .setContentText("Please share your Feedback to info@hanlesolutions.com")
                .setCustomImage(R.drawable.images)
                .show();

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "Event");
        adapter.addFragment(new TwoFragment(), "Venue");
        adapter.addFragment(new Three(), "Mailbag");
        adapter.addFragment(new ChatRoomActivity(), "Chat");
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);

    }

    public void Inviteepost(final List<String> checkedist) {

        pDialog = new ProgressDialog(UserTabView.this);
        pDialog.setMessage("Inviting please wait....");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebUrl.INVITE_USER, new Response.Listener<String>() {

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

                            Toasty.success(getApplicationContext(), "Successfully Invited ", Toast.LENGTH_SHORT, true).show();

                        } else {
                            Toasty.warning(getApplicationContext(), message, Toast.LENGTH_SHORT, true).show();
                        }

                    } else {
                        Toast.makeText(UserTabView.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toasty.error(getApplicationContext(), "Something went wrong!!!!", Toast.LENGTH_SHORT, true).show();
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
                    Toast.makeText(UserTabView.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                for (int i = 0; i < checkedist.size(); i++) {
                    params.put("UserValues", checkedist.get(i));
                }
                params.put("invite_EventId", MyApplication.getInstance().getPrefManager().getEventId().getEventId());
                params.put("organiser_id", MyApplication.getInstance().getPrefManager().getEventId().getOrganiserId());
                Log.e(TAG, "params: " + params.toString());


                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                WebUrl.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
