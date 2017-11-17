package com.events.hanle.events.adapter;

/**
 * Created by Hanle on 10/18/2016.
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.events.hanle.events.Model.CompletedEvent;
import com.events.hanle.events.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Hanle on 8/30/2016.
 */
public class CompletedEventAdapter extends RecyclerView.Adapter<CompletedEventAdapter.CompletedEventAdapterviewholder> {
    private static final String TAG = "CompletedEventAdapter";
    private List<CompletedEvent> attendinglist;
    private Context mContext;
    private int CON = 023;
    private int NA = 013;
    private int DIDRES = 001;

    public CompletedEventAdapter(Context context, List<CompletedEvent> listevent) {
        this.attendinglist = listevent;
        this.mContext = context;
    }


    @Override
    public CompletedEventAdapterviewholder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        CompletedEventAdapter.CompletedEventAdapterviewholder viewHolder;
        View view = null;

        if (viewType == CON) {
            // self message
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.concluded_event, null);

        } else if (viewType == NA) {
            // others message
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cancelled_noattending, null);

        } else if (viewType == DIDRES) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.didnot_response, null);
        }
        viewHolder = new CompletedEventAdapter.CompletedEventAdapterviewholder(view, mContext, attendinglist);

        return viewHolder;

    }

    @Override
    public int getItemViewType(int position) {
        CompletedEvent listAttending = attendinglist.get(position);

        int status = Integer.parseInt(listAttending.getUser_status());

        if (status == 1) {
            return DIDRES;
        } else if (status == 2) {
            return CON;

        } else if (status == 3) {
            return NA;

        }

        return position;
    }

    @Override
    public void onBindViewHolder(CompletedEventAdapterviewholder holder, int position) {
        CompletedEvent listAttending = attendinglist.get(position);
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

    public class CompletedEventAdapterviewholder extends RecyclerView.ViewHolder {
        TextView name, event_title;
        CardView mcardView;
        ArrayList<CompletedEvent> listevent = new ArrayList<>();
        Context ctx;

        public CompletedEventAdapterviewholder(View itemView, Context mContext, List<CompletedEvent> listevent) {
            super(itemView);
            this.listevent = (ArrayList<CompletedEvent>) listevent;
            this.ctx = mContext;
            name = (TextView) itemView.findViewById(R.id.name);
            event_title = (TextView) itemView.findViewById(R.id.event_title);
            mcardView = (CardView) itemView.findViewById(R.id.cardlist_item);

        }

    }
}