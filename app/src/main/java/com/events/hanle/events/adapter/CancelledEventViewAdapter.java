package com.events.hanle.events.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.events.hanle.events.Activity.ListOfCancelledEvent;
import com.events.hanle.events.Model.CancelledEventView;
import com.events.hanle.events.Model.ConcludedEventView;
import com.events.hanle.events.R;

import java.util.List;

/**
 * Created by Hanle on 10/26/2016.
 */

public class CancelledEventViewAdapter extends RecyclerView.Adapter<ConcludedEventViewHolder> {
    private List<CancelledEventView> feedItemList;
    private ListOfCancelledEvent mContext;

    public CancelledEventViewAdapter(ListOfCancelledEvent context, List<CancelledEventView> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }


    @Override
    public ConcludedEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_concluded_event_view, null);

        ConcludedEventViewHolder viewHolder = new ConcludedEventViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ConcludedEventViewHolder c, int position) {
        final CancelledEventView feedItem = feedItemList.get(position);
        String dresscode = feedItem.getDresscode();
        String payme = feedItem.getPayment();
        if(dresscode.equals("")){
            c.dresscode.setVisibility(View.GONE);
            c.dresscodelabel.setVisibility(View.GONE);
        }
        if(payme.equals("")){
            c.payment.setVisibility(View.GONE);
            c.paymentlabel.setVisibility(View.GONE);

        }
        //Setting text view title
        c.event_heading.setText(Html.fromHtml(feedItem.getEventname()));
        c.tim.setText(Html.fromHtml(feedItem.getTime()) +" ( "+"GMT "+feedItem.getTimezone()+")");
        c.date.setText(Html.fromHtml(feedItem.getDate())+" - "+feedItem.getWeekday().substring(0,3));
        c.venue.setText(Html.fromHtml(feedItem.getAddress()));
        c.description.setText(Html.fromHtml(feedItem.getEventdesc()));
        c.event_cretor.setText(Html.fromHtml(feedItem.getEvent_creator_name()));
        c.dresscode.setText(Html.fromHtml(feedItem.getDresscode()));
        c.payment.setText(Html.fromHtml(feedItem.getPayment()));
        c.phone.setText(Html.fromHtml("+"+feedItem.getOrgnaserphone()));

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);

    }

}
