package com.events.hanle.events.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.events.hanle.events.Model.Attending;
import com.events.hanle.events.R;

import java.util.List;

/**
 * Created by Hanle on 8/30/2016.
 */
public class ListAttending extends RecyclerView.Adapter<ListAttending.ListAttendingviewholder> {
    private static final String TAG = "ListAttending";
    private List<Attending> attendinglist;
    private Context mContext;

    public ListAttending(Context context, List<Attending> listevent) {

        this.attendinglist = listevent;
        this.mContext = context;
    }


    @Override
    public ListAttendingviewholder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_attending, null);
        ListAttendingviewholder viewHolder = new ListAttendingviewholder(view, mContext, attendinglist);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListAttendingviewholder holder, int position) {
        Attending listAttending = attendinglist.get(position);

        holder.attendng_list.setText(Html.fromHtml(listAttending.getNsme()));


    }

    @Override
    public int getItemCount() {
        return (null != attendinglist ? attendinglist.size() : 0);
    }

    public class ListAttendingviewholder extends RecyclerView.ViewHolder {
        TextView attendng_list;
        CardView mcardView;

        public ListAttendingviewholder(View itemView, Context mContext, List<Attending> attendinglist) {
            super(itemView);
            attendng_list = (TextView) itemView.findViewById(R.id.attendng_name);
            mcardView = (CardView) itemView.findViewById(R.id.cardlist_item);


        }
    }
}
