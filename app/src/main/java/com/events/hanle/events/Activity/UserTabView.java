package com.events.hanle.events.Activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Fragments.AttendingDialogFragment;
import com.events.hanle.events.Fragments.CreateEvent;
import com.events.hanle.events.Fragments.InviteeList;
import com.events.hanle.events.Fragments.ListOfOrganiserActionsFragment;
import com.events.hanle.events.Fragments.MuteDialog;
import com.events.hanle.events.Fragments.OneFragment;
import com.events.hanle.events.Fragments.OrganiserLoginDialog;
import com.events.hanle.events.Fragments.Three;
import com.events.hanle.events.Fragments.TwoFragment;
import com.events.hanle.events.Model.ListEvent;
import com.events.hanle.events.R;
import com.events.hanle.events.app.EndPoints;
import com.events.hanle.events.app.MyApplication;
import com.events.hanle.events.chat.ChatRoomActivity;
import com.events.hanle.events.gcm.GcmIntentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.ContentValues.TAG;

public class UserTabView extends AppCompatActivity {

    private static TabLayout tabLayout;
    private static ViewPager viewPager;

    String eventtype;
    ProgressDialog pDialog;
    private static final String TAG = "UserTabView";
    String event_id;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_tab_view);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String event_title = getIntent().getStringExtra("event_title");
        // Toast.makeText(UserTabView.this, event_title, Toast.LENGTH_SHORT).show();
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        assert t != null;
        t.setTitleTextColor(Color.WHITE);
        setSupportActionBar(t);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(event_title);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        assert viewPager != null;
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);

        eventtype = getIntent().getStringExtra("eventtype");

        if (eventtype == null) {
            eventtype = "1";
        }

        String s = getIntent().getStringExtra("classcheck");
        if (s != null) {
            if (s.equalsIgnoreCase("cancelledevent")) {
                event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getId();
            } else if (s.equalsIgnoreCase("completedevent")) {
                event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getId();
            }
        } else {
            event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getId();
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int et = Integer.parseInt(eventtype);


        UserTabView.this.invalidateOptionsMenu();
        MenuInflater inflater = getMenuInflater();


        inflater.inflate(R.menu.menu_user_tab, menu);

        MenuItem notattending, organiserlogin,invite,coupon_details;
        notattending = menu.findItem(R.id.not_attending);
        organiserlogin = menu.findItem(R.id.organiser_login);
        invite = menu.findItem(R.id.invite);
        coupon_details = menu.findItem(R.id.coupon_detials);

        if(!(MyApplication.getInstance().getPrefManager().getEventId().getCountrycode().equals(MyApplication.getInstance().getPrefManager().getUser().getCountrycode()
        )&&(MyApplication.getInstance().getPrefManager().getEventId().getPhone().equals(MyApplication.getInstance().getPrefManager().getUser().getMobile())))){
            invite.setEnabled(false);
            coupon_details.setEnabled(false);
        }

        if (et == 2) {
            notattending.setVisible(false);
            organiserlogin.setVisible(false);
        }
        if (getIntent().getStringExtra("classcheck") != null || getIntent().getStringExtra("classcheck") != null) {
            if (getIntent().getStringExtra("classcheck").equals("cancelledevent") || getIntent().getStringExtra("classcheck").equals("completedevent")) {
                notattending.setVisible(false);
                organiserlogin.setVisible(false);
            }
        }
        return true;
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
                startActivity(i);


                return true;

            case R.id.create_event:
                calldialogue();
                return true;

            case R.id.organiser_login:
                //startActivity(new Intent(getApplicationContext(),OrganiserLogin.class));
                et = Integer.parseInt(eventtype);
                if (et == 2) {
                    Toast.makeText(UserTabView.this, "This feature is not available for this Event", Toast.LENGTH_SHORT).show();

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
                String artwork = getIntent().getStringExtra("artwork");
                et = Integer.parseInt(eventtype);
                if (artwork != null && artwork.equals("") && et == 1) {
                    Toast.makeText(UserTabView.this, "Organiser has not uploaded the image", Toast.LENGTH_SHORT).show();
                } else if (artwork != null && artwork.equals("") && et == 2) {
                    Toast.makeText(UserTabView.this, "Organiser has not uploaded the image", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(getApplicationContext(), EventArtwork.class);
                    intent.putExtra("classcheck", getIntent().getStringExtra("classcheck"));
                    intent.putExtra("chat_room_id", getIntent().getExtras().getString("chat_room_id"));
                    intent.putExtra("eventId", getIntent().getExtras().getString("eventId"));
                    startActivity(intent);
                }

                return true;

            case R.id.list_invitee:
                String sharedetails = getIntent().getStringExtra("share_detail");
                eventtype = getIntent().getStringExtra("eventtype");

                et = Integer.parseInt(eventtype);
                //Toast.makeText(UserTabView.this, sharedetails, Toast.LENGTH_SHORT).show();
                int s = Integer.parseInt(sharedetails);


                if (sharedetails != null) {
                    if (s == 1 && et == 1) {
                        showDialog();

                    } else if (et == 2) {
                        Toast.makeText(UserTabView.this, "This feature is not available for this Event", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(UserTabView.this, "Organiser has not enabled this feature", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                }

                return true;
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

//        for (int i = 0; i < checkedist.size(); i++) {
//            System.out.println("Usertab : The total value is" + checkedist.get(i));
//        }

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


                            Toast.makeText(UserTabView.this, "Successfully Invited ", Toast.LENGTH_SHORT).show();
                            ListOfOrganiserActionsFragment dialogFragment = new ListOfOrganiserActionsFragment();
                            dialogFragment.show(getSupportFragmentManager(), "missiles");

                        } else {
                            Toast.makeText(UserTabView.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // login error - simply toast the message
                        Toast.makeText(UserTabView.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(UserTabView.this, "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("invite_EventId", com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getId());
                params.put("organiser_id", MyApplication.getInstance().getPrefManager().getOrganiserID());
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
