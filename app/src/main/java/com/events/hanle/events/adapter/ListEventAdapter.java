package com.events.hanle.events.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.events.hanle.events.Activity.UserAttendingStatus;
import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Model.ListEvent;
import com.events.hanle.events.R;
import com.events.hanle.events.app.EndPoints;
import com.events.hanle.events.app.MyApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by Hanle on 6/15/2016.
 */
public class ListEventAdapter extends RecyclerView.Adapter<ListEventAdapter.ListEventViewHolder> {
    private static final String TAG = "ListEventAdapter";
    private List<ListEvent> feedItemList;
    private Context mContext;
    private static String today;

    public class ListEventViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, time, detials, weekday, yr, rsvp;
        ArrayList<ListEvent> listevent = new ArrayList<>();
        Context ctx;
        CardView mCardView;
        LinearLayout linearLayout;
        RelativeLayout relativeLayout;
        ImageView rightarrow, counter, mailbag;

        public ListEventViewHolder(View itemView, Context ctx, ArrayList<ListEvent> listevent) {
            super(itemView);
            this.listevent = listevent;
            this.ctx = ctx;
            //emView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.title);
            mCardView = (CardView) itemView.findViewById(R.id.cardlist_item);
            rightarrow = (ImageView) itemView.findViewById(R.id.arr);
            counter = (ImageView) itemView.findViewById(R.id.chat);
            mailbag = (ImageView) itemView.findViewById(R.id.mail_bag);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            detials = (TextView) itemView.findViewById(R.id.details);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.ln);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.right_arrow);
            weekday = (TextView) itemView.findViewById(R.id.weekday);
            yr = (TextView) itemView.findViewById(R.id.year);
            rsvp = (TextView) itemView.findViewById(R.id.rsvp);

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
        String eventId = prefs.getString("chateventID" + feedItem.getEventId(), null);
        String mailbagorgnisereventId = prefs.getString("organisereventID" + feedItem.getEventId(), null);
        String mailbagpartnereventId = prefs.getString("partnereventID" + feedItem.getEventId(), null);
        int status = Integer.parseInt(feedItem.getUserAttendingStatus());
        //customViewHolder.message.setText(feedItem.getLastMessage());
        int co = feedItem.getUnreadCount();


        if (status == 1) {
            customViewHolder.relativeLayout.setVisibility(View.VISIBLE);
            customViewHolder.mCardView.setCardBackgroundColor(Color.parseColor("#ffffff")); // will change the background color of the card view to sky blue
            customViewHolder.title.setTextColor(Color.parseColor("#000000"));
            customViewHolder.detials.setTextColor(Color.parseColor("#3b4673"));
            customViewHolder.linearLayout.setBackgroundColor(Color.parseColor("#FEA700"));

            CardView.LayoutParams params = (CardView.LayoutParams) customViewHolder.linearLayout.getLayoutParams();
            params.setMargins(0, 0, 80, 0);
            customViewHolder.linearLayout.setLayoutParams(params);
            customViewHolder.rightarrow.setVisibility(View.VISIBLE);

            if (feedItem.getRsvpdate() != null && !feedItem.getRsvpdate().isEmpty() && feedItem.getRsvptime() != null && !feedItem.getRsvptime().isEmpty()) {
                customViewHolder.rsvp.setText("RSVP : " + feedItem.getRsvpdate() + " : " + feedItem.getRsvptime());
                customViewHolder.rsvp.setVisibility(View.VISIBLE);
            }


        } else if (eventId != null && feedItem.getEventId().equals(eventId) && mailbagorgnisereventId != null && feedItem.getEventId().equals(mailbagorgnisereventId)) {
            customViewHolder.relativeLayout.setVisibility(View.VISIBLE);
            customViewHolder.counter.setVisibility(View.VISIBLE);
            customViewHolder.mailbag.setVisibility(View.VISIBLE);
            customViewHolder.relativeLayout.setVisibility(View.VISIBLE);
            CardView.LayoutParams params = (CardView.LayoutParams) customViewHolder.linearLayout.getLayoutParams();
            params.setMargins(0, 0, 80, 0);

            customViewHolder.linearLayout.setLayoutParams(params);
            System.out.println("Totoal count is:" + co);
        } else if (eventId != null && feedItem.getEventId().equals(eventId) && mailbagpartnereventId != null && feedItem.getEventId().equals(mailbagpartnereventId)) {
            customViewHolder.relativeLayout.setVisibility(View.VISIBLE);
            customViewHolder.counter.setVisibility(View.VISIBLE);
            customViewHolder.mailbag.setVisibility(View.VISIBLE);
            customViewHolder.relativeLayout.setVisibility(View.VISIBLE);
            CardView.LayoutParams params = (CardView.LayoutParams) customViewHolder.linearLayout.getLayoutParams();
            params.setMargins(0, 0, 80, 0);
            customViewHolder.linearLayout.setLayoutParams(params);
            System.out.println("Totoal count is:" + co);
        } else if (feedItem.getUnreadCount() > 0 || eventId != null && feedItem.getEventId().equals(eventId)) {
            customViewHolder.counter.setVisibility(View.VISIBLE);
            customViewHolder.relativeLayout.setVisibility(View.VISIBLE);
            CardView.LayoutParams params = (CardView.LayoutParams) customViewHolder.linearLayout.getLayoutParams();
            params.setMargins(0, 0, 80, 0);
            customViewHolder.linearLayout.setLayoutParams(params);
            System.out.println("organisereventID" + mailbagorgnisereventId);
        } else if (feedItem.getUnreadcount1() > 0 || mailbagorgnisereventId != null && feedItem.getEventId().equals(mailbagorgnisereventId)) {
            customViewHolder.mailbag.setVisibility(View.VISIBLE);
            customViewHolder.relativeLayout.setVisibility(View.VISIBLE);
            CardView.LayoutParams params = (CardView.LayoutParams) customViewHolder.linearLayout.getLayoutParams();
            params.setMargins(0, 0, 80, 0);
            customViewHolder.linearLayout.setLayoutParams(params);
            System.out.println("organ" + feedItem.getEventId().equals(mailbagorgnisereventId));
        } else if (mailbagpartnereventId != null && feedItem.getEventId().equals(mailbagpartnereventId)) {
            customViewHolder.mailbag.setVisibility(View.VISIBLE);
            customViewHolder.relativeLayout.setVisibility(View.VISIBLE);
            CardView.LayoutParams params = (CardView.LayoutParams) customViewHolder.linearLayout.getLayoutParams();
            params.setMargins(0, 0, 80, 0);
            customViewHolder.linearLayout.setLayoutParams(params);
            System.out.println("organ" + feedItem.getEventId().equals(mailbagorgnisereventId));
        } else {
            customViewHolder.relativeLayout.setVisibility(View.GONE);
            customViewHolder.mCardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
            customViewHolder.title.setTextColor(Color.parseColor("#000000"));
            customViewHolder.detials.setTextColor(Color.parseColor("#3b4673"));
            customViewHolder.linearLayout.setBackgroundColor(Color.parseColor("#3B4673"));

            CardView.LayoutParams params = (CardView.LayoutParams) customViewHolder.linearLayout.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            customViewHolder.linearLayout.setLayoutParams(params);
            customViewHolder.rightarrow.setVisibility(View.GONE);
        }


        String tims = feedItem.getDate();
        Date date = new Date();

        if (tims != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d;
            try {
                d = sdf.parse(tims);
                sdf.applyPattern("yyyy");
                String n = sdf.format(d);
                Calendar cal = Calendar.getInstance();
                Calendar cal1 = Calendar.getInstance();
                cal.setTime(d);
                cal1.setTime(date);
                if ((cal.get(Calendar.YEAR) != cal1.get(Calendar.YEAR))) {
                    customViewHolder.yr.setVisibility(View.VISIBLE);
                    customViewHolder.yr.setText(n);
                }

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        customViewHolder.title.setText(feedItem.getEventTitle());
        customViewHolder.detials.setText("Invitation from " + feedItem.getInviterName());


        String[] monthName = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov", "Dec"};
        String s1 = feedItem.getDate1();
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
