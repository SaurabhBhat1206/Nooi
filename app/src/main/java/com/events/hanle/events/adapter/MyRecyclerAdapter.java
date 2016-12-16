package com.events.hanle.events.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.events.hanle.events.Fragments.OneFragment;
import com.events.hanle.events.Model.FeedItem;
import com.events.hanle.events.R;

import java.util.List;


/**
 * Created by saurabh on 4/29/2016.
 */


public class MyRecyclerAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private List<FeedItem> feedItemList;
    private OneFragment mContext;

    public MyRecyclerAdapter(OneFragment context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder c, int position) {
        final FeedItem feedItem = feedItemList.get(position);
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
        c.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+feedItem.getOrgnaserphone()));
                mContext.startActivity(callIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);

    }


}
