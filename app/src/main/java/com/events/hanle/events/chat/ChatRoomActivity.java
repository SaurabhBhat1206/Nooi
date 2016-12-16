package com.events.hanle.events.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Map;
import java.util.TimeZone;


@RequiresApi(api = Build.VERSION_CODES.M)
public class ChatRoomActivity extends Fragment {

    private String TAG = ChatRoomActivity.class.getSimpleName();

    private String chatRoomId;
    private RecyclerView recyclerView;
    private ChatRoomThreadAdapter mAdapter;
    private ArrayList<Message> messageArrayList;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private EditText inputMessage;
    private Button btnSend;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tv;
    private int requestCount = 1;
    private RequestQueue requestQueue;
    String event_status, event_title, Username, invitername, s;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_chat_room, container, false);
        inputMessage = (EditText) v.findViewById(R.id.message);
        btnSend = (Button) v.findViewById(R.id.btn_send);

        tv = (TextView) v.findViewById(R.id.textView10);
        s = getActivity().getIntent().getStringExtra("classcheck");

        if (s != null) {
            if (s.equalsIgnoreCase("cancelledevent")) {
                event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getEvent_status();
                event_title = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getEvent_title();
                invitername = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getInvitername();
                chatRoomId = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getId();

            } else if (s.equalsIgnoreCase("completedevent")) {
                event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getEvent_status();
                event_title = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getEvent_title();
                invitername = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getInvitername();
                chatRoomId = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getId();

            }
        } else {
            event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEvent_status();
            event_title = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEvent_title();
            invitername = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getInvitername();
            chatRoomId = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getId();

        }

        int es = Integer.parseInt(event_status);


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

        requestQueue = Volley.newRequestQueue(getActivity());

        messageArrayList = new ArrayList<>();

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
            Toast.makeText(getActivity(), "No Internet!!", Toast.LENGTH_SHORT).show();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
//Ifscrolled at last then
                    if (isLastItemDisplaying(recyclerView)) {
//Calling the method getdata again
                        getData();
                    }
                }
            });
        } else {
            recyclerView.setOnScrollChangeListener(new RecyclerView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//Ifscrolled at last then
                    if (isLastItemDisplaying(recyclerView)) {
//Calling the method getdata again
                        getData();
                    }
                }
            });
        }

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

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
    private void handlePushNotification(Intent intent) {
        Message message = (Message) intent.getSerializableExtra("message");
        String chatRoomId = intent.getStringExtra("chat_room_id");

        if (message != null && chatRoomId != null) {
            messageArrayList.add(message);
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
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

}
