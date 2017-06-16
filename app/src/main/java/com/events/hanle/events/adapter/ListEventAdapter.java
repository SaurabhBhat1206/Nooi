package com.events.hanle.events.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.events.hanle.events.Activity.UserAttendingStatus;
import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Model.ListEvent;
import com.events.hanle.events.Model.ListEventCopy;
import com.events.hanle.events.R;
import com.events.hanle.events.app.MyApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by Hanle on 6/15/2016.
 */
public class ListEventAdapter extends RecyclerView.Adapter<ListEventAdapter.ListEventViewHolder> {
    private static final String TAG = "ListEventAdapter";
    private List<ListEvent> feedItemList;
    private Context mContext;
    private static String today;
    public static final String MyPREFERENCES = "PrefChat";
    SharedPreferences sharedpreferences;

    public class ListEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, message, date, time, detials, weekday;
        //ImageView counter, mailbag;
        ArrayList<ListEvent> listevent = new ArrayList<>();
        Context ctx;
        CardView mCardView;
        LinearLayout linearLayout;

        public ListEventViewHolder(View itemView, Context ctx, ArrayList<ListEvent> listevent) {
            super(itemView);
            this.listevent = listevent;
            this.ctx = ctx;
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.title);
            mCardView = (CardView) itemView.findViewById(R.id.cardlist_item);
            //message = (TextView) itemView.findViewById(R.id.message);
            //counter = (ImageView) itemView.findViewById(R.id.counter);
            // mailbag = (ImageView) itemView.findViewById(R.id.mail_bag);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            detials = (TextView) itemView.findViewById(R.id.details);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.ln);
            weekday = (TextView) itemView.findViewById(R.id.weekday);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ListEvent listEvent = this.listevent.get(position);


            listEvent = new ListEvent(listEvent.getId(), listEvent.getEvent_title(), listEvent.getUser_status(), listEvent.getInvitername(), listEvent.getEvent_status(), null, listEvent.getShare_detail(), listEvent.getArtwork(), listEvent.getEvent_type(), listEvent.getChat_window(), listEvent.getCountrycode(), listEvent.getPhone(), listEvent.getOrganiserId());

            int user_Status = Integer.parseInt(listEvent.getUser_status());
            MyApplication.getInstance().getPrefManager().storeEventId(listEvent);
            if (user_Status == 1) {
                //callalertDialog();
                Intent i = new Intent(mContext, UserAttendingStatus.class);
                i.putExtra("event_title", listEvent.getEvent_title());
                i.putExtra("share_detail", listEvent.getShare_detail());
                i.putExtra("artwork", listEvent.getArtwork());
                i.putExtra("eventtype", listEvent.getEvent_type());
                i.putExtra("chatw", listEvent.getChat_window());
                this.ctx.startActivity(i);
            } else if (user_Status == 3) {
                Toast.makeText(this.ctx, "You said you are not attending this Event!!", Toast.LENGTH_LONG).show();
            } else {
                MyApplication.getInstance().getPrefManager().storeEventId(listEvent);
                Log.e(TAG, "Event details is stored in shared preferences. " + listEvent.getId() + ", " + listEvent.getEvent_title() + "," + "," + listEvent.getInvitername() + "," + listEvent.getEvent_status() + "," + listEvent.getUser_status());

                Intent i = new Intent(this.ctx, UserTabView.class);
                i.putExtra("event_title", listEvent.getEvent_title());
                i.putExtra("share_detail", listEvent.getShare_detail());
                i.putExtra("artwork", listEvent.getArtwork());
                i.putExtra("eventtype", listEvent.getEvent_type());
                i.putExtra("chatw", listEvent.getChat_window());
                this.ctx.startActivity(i);


                sharedpreferences = this.ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove("chateventID" + listEvent.getId());
                editor.remove("organisereventID" + listEvent.getId());
                editor.remove("partnereventID" + listEvent.getId());
                editor.apply();

            }
        }
    }


    public ListEventAdapter(Context context, List<ListEvent> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));


    }

    @Override
    public ListEventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.active_events_new, null);
        ListEventViewHolder viewHolder = new ListEventViewHolder(view, mContext, (ArrayList<ListEvent>) feedItemList);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ListEventViewHolder customViewHolder, int i) {
        ListEvent feedItem = feedItemList.get(i);

        SharedPreferences prefs = mContext.getSharedPreferences("PrefChat", Context.MODE_PRIVATE);
        String eventId = prefs.getString("chateventID" + feedItem.getId(), null);
        String mailbagorgnisereventId = prefs.getString("organisereventID" + feedItem.getId(), null);
        String mailbagpartnereventId = prefs.getString("partnereventID" + feedItem.getId(), null);
        int status = Integer.parseInt(feedItem.getUser_status());
        //customViewHolder.message.setText(feedItem.getLastMessage());
        String s = feedItem.getLastMessage();
        int co = feedItem.getUnreadCount();


//        if (feedItem.getUnreadCount() > 0) {
//            customViewHolder.counter.setVisibility(View.VISIBLE);
//            System.out.println("Totoal count is:" + co);
//        } else if (eventId != null && feedItem.getId().equals(eventId)) {
//            customViewHolder.counter.setVisibility(View.VISIBLE);
//            System.out.println("organisereventID" + mailbagorgnisereventId);
//        } else {
//            customViewHolder.counter.setVisibility(View.GONE);
//        }

//        if (feedItem.getUnreadcount1() > 0) {
//            customViewHolder.mailbag.setVisibility(View.VISIBLE);
//        } else if (mailbagorgnisereventId != null && feedItem.getId().equals(mailbagorgnisereventId)) {
//            customViewHolder.mailbag.setVisibility(View.VISIBLE);
//            System.out.println("organ" + feedItem.getId().equals(mailbagorgnisereventId));
//        } else if (mailbagpartnereventId != null && feedItem.getId().equals(mailbagpartnereventId)) {
//            customViewHolder.mailbag.setVisibility(View.VISIBLE);
//            System.out.println("partner" + feedItem.getId().equals(mailbagpartnereventId));
//        } else {
//            customViewHolder.mailbag.setVisibility(View.GONE);
//
//        }


        if (status == 1) {
            customViewHolder.mCardView.setCardBackgroundColor(Color.parseColor("#919DD1")); // will change the background color of the card view to sky blue
            customViewHolder.title.setTextColor(Color.parseColor("#ffffff"));
            customViewHolder.detials.setTextColor(Color.parseColor("#ffffff"));
        } else if (status == 2) {
            customViewHolder.mCardView.setCardBackgroundColor(Color.parseColor("#ffffff")); // will change the background color of the card view to sky blue
            customViewHolder.linearLayout.setBackgroundColor(Color.parseColor("#3B4673"));
            customViewHolder.title.setTextColor(Color.parseColor("#000000"));
            customViewHolder.detials.setTextColor(Color.parseColor("#3b4673"));
        }
//        else if (status == 2) {
//            customViewHolder.mCardView.setCardBackgroundColor(Color.parseColor("#96d796")); // will change the background color of the card view to green
//            customViewHolder.title.setTextColor(Color.parseColor("#000000"));
//
//        } else if (status == 3) {
//            customViewHolder.mCardView.setCardBackgroundColor(Color.parseColor("#f42d4e")); // will change the background color of the card view to red
//
//        }

        customViewHolder.title.setText(feedItem.getEvent_title());
        customViewHolder.detials.setText(feedItem.getEvent_title() + " with " + feedItem.getInvitername());

        String[] monthName = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov", "Dec"};
        String s1 = feedItem.getMonthno();
        if (s1 != null) {
            int m = Integer.parseInt(s1);
            String month = monthName[m - 1];
            String dat = feedItem.getDate().substring(0, 2);
            customViewHolder.date.setText(month + " " + dat);
            customViewHolder.weekday.setText(feedItem.getWeekday());
            customViewHolder.time.setText(feedItem.getTime());
        } else {
            customViewHolder.time.setVisibility(View.GONE);
            customViewHolder.date.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }
}
