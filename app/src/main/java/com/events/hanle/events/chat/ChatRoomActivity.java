package com.events.hanle.events.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.events.hanle.events.Activity.ListOfEvent1;
import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Model.Message;
import com.events.hanle.events.Model.User;
import com.events.hanle.events.R;
import com.events.hanle.events.adapter.ChatRoomThreadAdapter;
import com.events.hanle.events.app.Config;
import com.events.hanle.events.app.EndPoints;
import com.events.hanle.events.app.MyApplication;
import com.events.hanle.events.gcm.NotificationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;


@RequiresApi(api = Build.VERSION_CODES.M)
public class ChatRoomActivity extends Fragment {

    private String TAG = ChatRoomActivity.class.getSimpleName();

    private String chatRoomId;
    private RecyclerView recyclerView;
    private ChatRoomThreadAdapter mAdapter;
    private LinkedList<Message> messageArrayList;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private EditText inputMessage;
    private ImageView btnSend;
    private RecyclerView.LayoutManager layoutManager;
    private int requestCount = 1;
    private RequestQueue requestQueue;
    private String event_status, event_title, invitername, s;
    private int es;
    private LinearLayout lin;
    private TextView tv;
    private String chatwindow, eventtype;
    private ImageView img;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_chat_room, container, false);
        inputMessage = (EditText) v.findViewById(R.id.message);
        btnSend = (ImageView) v.findViewById(R.id.btn_send);
        lin = (LinearLayout) v.findViewById(R.id.ln);
        tv = (TextView) v.findViewById(R.id.nochattoshow);
        s = getActivity().getIntent().getStringExtra("classcheck");
        img = (ImageView) v.findViewById(R.id.nointernet);
        Config.typeface = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Regular.ttf");
        tv.setTypeface(Config.typeface);

        if (s != null) {
            if (s.equalsIgnoreCase("cancelledevent")) {
                event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getEvent_status();
                event_title = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getEvent_title();
                invitername = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getInvitername();
                chatRoomId = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getId();
                eventtype = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getEventtype();
                chatwindow = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getChatw();

            } else if (s.equalsIgnoreCase("completedevent")) {
                event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getEvent_status();
                event_title = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getEvent_title();
                invitername = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getInvitername();
                chatRoomId = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getId();
                eventtype = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getEvent_type();
                chatwindow = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getChat_window();

            }
        } else {
            event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEventStatus();
            event_title = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEventTitle();
            invitername = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getInviterName();
            chatRoomId = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEventId();
            chatwindow = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getChatW();
            eventtype = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getType();
        }

        if (event_status != null) {
            es = Integer.parseInt(event_status);
        } else {
            es = 2;
        }

        if (chatRoomId == null) {
            Toast.makeText(getActivity(), "Chat room not found!", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        if (es == 2 || es == 3) {
            btnSend.setEnabled(false);
        }
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);


        int cW = Integer.parseInt(chatwindow);
        int event_type = Integer.parseInt(eventtype);
        if (cW == 0 && event_type == 2) {
            lin.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
        }

        requestQueue = Volley.newRequestQueue(getActivity());

        messageArrayList = new LinkedList<>();

        // self user id is to identify the message owner
        String selfUserId = MyApplication.getInstance().getPrefManager().getUser().getId();

        mAdapter = new ChatRoomThreadAdapter(getActivity(), messageArrayList, selfUserId);
        recyclerView.setAdapter(mAdapter);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push message is received
                    handlePushNotification(intent);
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION_FROM_PARTNER)) {
                    // new push notification is received
                    String desc = intent.getStringExtra("description");
                    calldialogfrombroadcast(desc);
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION_FROM_ORGANISER)) {
                    // new push notification is received
                    String desc = intent.getStringExtra("description");
                    calldialogfrombroadcastfororganiser(desc);
                }
            }
        };

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionDetector.isInternetAvailable(getActivity())) {
                    sendMessage();

                } else {
                    Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (ConnectionDetector.isInternetAvailable(getActivity())) {
            //fetchChatThread();
            getData();
        } else {
            lin.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
            tv.setText("No Internet!!");
            img.setVisibility(View.VISIBLE);

        }

        doheavyopeation();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION_FROM_PARTNER));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION_FROM_ORGANISER));

        NotificationUtils.clearNotifications();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Handling new push message, will add the message to
     * recycler view and scroll it to bottom
     */
    private void calldialogfrombroadcast(String desc) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Message from partner")
                .setContentText(desc)
                .setCustomImage(R.drawable.images)
                .show();
    }

    private void calldialogfrombroadcastfororganiser(String desc) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Message from partner")
                .setContentText(desc)
                .setCustomImage(R.drawable.images)
                .show();
    }

    private void handlePushNotification(Intent intent) {
        Message message = (Message) intent.getSerializableExtra("message");
        String chatRoomId = intent.getStringExtra("chat_room_id");

        if (message != null && chatRoomId != null) {
            messageArrayList.add(message);
            recyclerView.getRecycledViewPool().clear();
            mAdapter.notifyDataSetChanged();
            if (mAdapter.getItemCount() > 1) {
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
            }
        }
    }

    /**
     * Posting a new message in chat room
     * will make an http call to our server. Our server again sends the message
     * to all the devices as push notification
     */
    private void sendMessage() {
        final String message = this.inputMessage.getText().toString().trim();
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("MMM dd HH:mm");
        date.setTimeZone(TimeZone.getDefault());
        final String localTime = date.format(currentLocalTime);

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getActivity(), "Enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        String endPoint = EndPoints.CHAT_ROOM_MESSAGE.replace("_ID_", chatRoomId);

        Log.e(TAG, "endpoint: " + endPoint);

        this.inputMessage.setText("");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {
                        JSONObject commentObj = obj.getJSONObject("message");

                        String commentId = commentObj.getString("message_id");
                        String commentText = commentObj.getString("message");
                        String createdAt = commentObj.getString("created_at");

                        JSONObject userObj = obj.getJSONObject("user");
                        String userId = userObj.getString("user_id");
                        String userName = userObj.getString("name");
                        User user = new User(userId, userName, null);

                        Message message = new Message();
                        message.setId(commentId);
                        message.setMessage(commentText);
                        message.setCreatedAt(createdAt);
                        message.setUser(user);

                        messageArrayList.add(message);

                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.getItemCount() > 1) {
                            // scrolling to bottom of the recycler view
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                        }

                    } else {
                        Toast.makeText(getActivity(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                inputMessage.setText(message);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", MyApplication.getInstance().getPrefManager().getUser().getId());
                params.put("message", message);
                params.put("timestamp", localTime);

                Log.e(TAG, "Params: " + params.toString());

                return params;
            }


        };


        // disabling retry policy so that it won't make
        // multiple http calls
        int socketTimeout = 0;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        strReq.setRetryPolicy(policy);

        //Adding request to request queue
        requestQueue.add(strReq);
    }


    private void getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        fetchChatThread(requestCount);
        //Incrementing the request counter
        requestCount++;
        System.out.println("chatcount is:" + requestCount);
    }

    /**
     * Fetching all the messages of a single chat room
     */
    private void fetchChatThread(int requestCount) {

        String endPoint = EndPoints.CHAT_THREAD.replace("_ID_", chatRoomId);
        Log.e(TAG, "endPoint: " + endPoint);
        String endpoint1 = endPoint.replace("_PAGECOUNT_", String.valueOf(requestCount));

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endpoint1, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);


                    // check for error
                    if (obj.getBoolean("error") == false) {
                        JSONArray commentsObj = obj.getJSONArray("messages");

                        for (int i = 0; i < commentsObj.length(); i++) {
                            JSONObject commentObj = (JSONObject) commentsObj.get(i);

                            String commentId = commentObj.getString("message_id");
                            String commentText = commentObj.getString("message");
                            String createdAt = commentObj.getString("created_at");


                            JSONObject userObj = commentObj.getJSONObject("user");
                            String userId = userObj.getString("user_id");
                            String userName = userObj.getString("username");
                            User user = new User(userId, userName, null);

                            Message message = new Message();
                            message.setId(commentId);
                            message.setMessage(commentText);
                            message.setCreatedAt(createdAt);
                            message.setUser(user);

                            messageArrayList.add(message);
                        }

                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.getItemCount() > 1) {
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                        }

                    } else {
                        Toast.makeText(getActivity(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    //Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getActivity(), "Server did not respond", Toast.LENGTH_SHORT).show();
            }
        });


        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }


    //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    private void doheavyopeation() {


        new Thread(new Runnable() {
            public void run() {

                recyclerView.post(new Runnable() {
                    public void run() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                    if (isLastItemDisplaying(recyclerView)) {
                                        getData();
                                        //myHandler.sendEmptyMessage(DO_UPDATE_TEXT);

                                    }
                                }
                            });
                        } else {

                            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                }

                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);

                                    if (isLastItemDisplaying(recyclerView)) {
                                        getData();
                                        //myHandler.sendEmptyMessage(DO_UPDATE_TEXT);

                                    }
                                }
                            });
                        }
                    }
                });
            }
        }).start();


    }


}
