package com.events.hanle.events.Fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.events.hanle.events.Model.EventMessage;
import com.events.hanle.events.R;
import com.events.hanle.events.adapter.EventMessageAdapter;

import java.util.ArrayList;
import java.util.List;


public class Three extends Fragment {

    private static final String TAG = "Three";
    private List<EventMessage> feedsList = new ArrayList<EventMessage>();
    private RecyclerView mRecyclerView;
    private EventMessageAdapter eventMessageAdapter;
    TextView t;
    EventMessage mess;
    TextView tv;
    String s;
    String event_status, event_title, Username, invitername;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_three, container, false);
        tv = (TextView) v.findViewById(R.id.no_event);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_fragment_three);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        mRecyclerView.setHasFixedSize(true);

         s = getActivity().getIntent().getStringExtra("classcheck");
        if (s != null) {
            if (s.equalsIgnoreCase("cancelledevent")) {
                event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getEvent_status();
                event_title = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getEvent_title();
                invitername = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getInvitername();

            } else if (s.equalsIgnoreCase("completedevent")) {
                event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getEvent_status();
                event_title = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getEvent_title();
                invitername = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getInvitername();

            }
        } else {
            event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEvent_status();
            event_title = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEvent_title();
            invitername = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getInvitername();


        }


        //event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEvent_status();
        //event_title = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEvent_title();
        Username = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getName();
        int es = Integer.parseInt(event_status);
        feedsList = new ArrayList<>();
        EventMessage eventMessage = new EventMessage();
        if (es == 2) {
            tv.setVisibility(View.GONE);
            eventMessage.setTitle("Dear " + Username + ",");
            eventMessage.setDescription("Please be informed, the event " + event_title + " stands cancelled. The chat page is now inaccessible. Regards. " + invitername + ".");
            feedsList.add(eventMessage);
            eventMessageAdapter = new EventMessageAdapter(getActivity(), (ArrayList<EventMessage>) feedsList, s );
            mRecyclerView.setAdapter(eventMessageAdapter);
        } else if (es == 3) {
            tv.setVisibility(View.GONE);
            eventMessage.setTitle("Dear " + Username + ",");
            eventMessage.setDescription("Thank you for participating. This event is now concluded. The chat page is now inaccessible. Regards. " + invitername + ".");
            feedsList.add(eventMessage);
            eventMessageAdapter = new EventMessageAdapter(getActivity(), (ArrayList<EventMessage>) feedsList, s);
            mRecyclerView.setAdapter(eventMessageAdapter);

        }


        return v;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}