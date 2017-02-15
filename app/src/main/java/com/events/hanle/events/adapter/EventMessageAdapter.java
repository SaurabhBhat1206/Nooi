package com.events.hanle.events.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Model.EventMessage;
import com.events.hanle.events.Model.Message;
import com.events.hanle.events.R;

import java.util.ArrayList;

/**
 * Created by Hanle on 5/23/2016.
 */
public class EventMessageAdapter extends RecyclerView.Adapter<EventMessageAdapter.EventMessageViewHolder> {

    private Context mContext;
    private ArrayList<EventMessage> eventmessageArrayList;
    String sm;
    String event_status;
    private int PARTNER = 100;
    private int ORGNAISER = 101;

    public class EventMessageViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, timestamp;
        CardView cv;

        public EventMessageViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            cv = (CardView) itemView.findViewById(R.id.cardlist_item);
        }
    }

    public EventMessageAdapter(Context context, ArrayList<EventMessage> eventmessagelist, String s) {
        this.mContext = context;
        this.eventmessageArrayList = eventmessagelist;
        this.sm = s;

    }


    @Override
    public EventMessageAdapter.EventMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        if (viewType == PARTNER) {
            // self message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_fragemnt_row_three, parent, false);

        } else {
            // others message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_fragment_three_organiser, parent, false);

        }
        return new EventMessageViewHolder(itemView);

    }

    @Override
    public int getItemViewType(int position) {
        EventMessage eventMessage = eventmessageArrayList.get(position);
        if (eventMessage.getPushtype()!=null && eventMessage.getPushtype().equalsIgnoreCase("partner")) {
            return PARTNER;

        } else{
            return ORGNAISER;
        }

    }

    @Override
    public void onBindViewHolder(EventMessageAdapter.EventMessageViewHolder holder, int position) {
        EventMessage eventMessage = eventmessageArrayList.get(position);

        if (sm != null) {
            if (sm.equalsIgnoreCase("cancelledevent")) {
                event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getEvent_status();

            } else if (sm.equalsIgnoreCase("completedevent")) {
                event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getEvent_status();

            } else if (sm.equalsIgnoreCase("from_notifications")) {
                event_status = ((UserTabView) mContext).getIntent().getExtras().getString("chat_room_id");

            } else if (sm.equalsIgnoreCase("from_partner")) {
                event_status = ((UserTabView) mContext).getIntent().getExtras().getString("eventId");

            } else if (sm.equalsIgnoreCase("from_organiser")) {
                event_status = ((UserTabView) mContext).getIntent().getExtras().getString("eventId");

            }
        } else {
            event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEvent_status();

        }

        int es = Integer.parseInt(event_status);
        System.out.println("oota******" + event_status);

        if (es == 2 || es == 3) {
            holder.title.setText(eventMessage.getTitle());
            holder.description.setText(eventMessage.getDescription());
            holder.description.setTextColor(Color.RED);
        } else if (es == 1) {
            if (eventMessage.getTimestamp() != null) {
                if (eventMessage.getPushtype().equalsIgnoreCase("partner")) {
                    holder.title.setText("Message from Partner: " + eventMessage.getTitle());
                    holder.description.setText(eventMessage.getPush_message());
                    holder.timestamp.setText(eventMessage.getTimestamp().substring(0, 24));
                } else if (eventMessage.getPushtype().equalsIgnoreCase("organiser")) {
                    holder.title.setText("Message from Organiser: " + eventMessage.getTitle());
                    holder.description.setText(eventMessage.getPush_message());
                    holder.timestamp.setText(eventMessage.getTimestamp().substring(0, 24));

                }


            } else {
                holder.timestamp.setVisibility(View.GONE);
                holder.timestamp.setText("");

            }

        }


    }

    @Override
    public int getItemCount() {
        return eventmessageArrayList.size();
    }


}
