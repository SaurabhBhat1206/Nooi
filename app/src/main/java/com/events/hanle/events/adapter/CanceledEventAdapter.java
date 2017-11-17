package com.events.hanle.events.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Model.CanceledEvent;
import com.events.hanle.events.Model.Message;
import com.events.hanle.events.R;
import com.events.hanle.events.app.MyApplication;

import java.util.ArrayList;

/**
 * Created by Hanle on 10/18/2016.
 */

public class CanceledEventAdapter extends RecyclerView.Adapter<CanceledEventAdapter.CanceledEventAdapterviewholder> {
    private static final String TAG = "ListAttending";
    private ArrayList<CanceledEvent> attendinglist;
    private Context mContext;
    private int CAN = 023;
    private int NA = 013;
    private int DIDRES = 001;

    public CanceledEventAdapter(Context context, ArrayList<CanceledEvent> listevent) {

        this.attendinglist = listevent;
        this.mContext = context;
    }

    @Override
    public CanceledEventAdapterviewholder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        CanceledEventAdapterviewholder viewHolder;
        View view = null;

        if (viewType == CAN) {
            // self message
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cancelled_events, null);

        } else if (viewType == NA) {
            // others message
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cancelled_noattending, null);

        } else if (viewType == DIDRES) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.didnot_response, null);
        }
        viewHolder = new CanceledEventAdapterviewholder(view, mContext, attendinglist);

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        CanceledEvent canceledEvent = attendinglist.get(position);
        int status = Integer.parseInt(canceledEvent.getUser_status());

        if (status == 1) {
            return DIDRES;
        } else if (status == 2) {
            return CAN;

        } else if (status == 3) {
            return NA;

        }

        return position;
    }

    @Override
    public void onBindViewHolder(CanceledEventAdapterviewholder holder, int position) {
        CanceledEvent listAttending = attendinglist.get(position);

        int status = Integer.parseInt(listAttending.getUser_status());

        if (status == 1) {
            holder.event_title.setText(listAttending.getEvent_title());
            holder.name.setText("By "+listAttending.getInvitername());

        } else if (status == 2) {
            holder.event_title.setText(listAttending.getEvent_title());
            holder.name.setText("By "+listAttending.getInvitername());

        } else if (status == 3) {
            holder.event_title.setText(listAttending.getEvent_title());
            holder.name.setText("By "+listAttending.getInvitername());

        }
    }

    @Override
    public int getItemCount() {
        return (null != attendinglist ? attendinglist.size() : 0);
    }

    public class CanceledEventAdapterviewholder extends RecyclerView.ViewHolder {
        TextView name, event_title;
        CardView mcardView;
        ArrayList<CanceledEvent> listevent = new ArrayList<>();
        Context ctx;

        public CanceledEventAdapterviewholder(View itemView, Context mContext, ArrayList<CanceledEvent> listevent) {
            super(itemView);
            this.listevent = listevent;
            this.ctx = mContext;

            name = (TextView) itemView.findViewById(R.id.name);
            event_title = (TextView) itemView.findViewById(R.id.event_title);
            mcardView = (CardView) itemView.findViewById(R.id.cardlist_item);

        }

    }
}
