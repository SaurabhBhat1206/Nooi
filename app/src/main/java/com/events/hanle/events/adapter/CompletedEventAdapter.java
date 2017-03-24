package com.events.hanle.events.adapter;

/**
 * Created by Hanle on 10/18/2016.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.events.hanle.events.Activity.ListOfCancelledEvent;
import com.events.hanle.events.Activity.ListOfConcluded;
import com.events.hanle.events.Activity.UserAttendingStatus;
import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Model.CompletedEvent;
import com.events.hanle.events.Model.ListEvent;
import com.events.hanle.events.R;
import com.events.hanle.events.app.MyApplication;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Hanle on 8/30/2016.
 */
public class CompletedEventAdapter extends RecyclerView.Adapter<CompletedEventAdapter.CompletedEventAdapterviewholder> {
    private static final String TAG = "ListAttending";
    private List<CompletedEvent> attendinglist;
    private Context mContext;
    Dialog dialog;

    public CompletedEventAdapter(Context context, List<CompletedEvent> listevent, Dialog d) {
        this.attendinglist = listevent;
        this.mContext = context;
        this.dialog = d;
    }


    @Override
    public CompletedEventAdapterviewholder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_attending, null);
        CompletedEventAdapterviewholder viewHolder = new CompletedEventAdapterviewholder(view, mContext, attendinglist, dialog);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CompletedEventAdapterviewholder holder, int position) {
        CompletedEvent listAttending = attendinglist.get(position);
        int status = Integer.parseInt(listAttending.getUser_status());

        if (status == 1) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.attendng_list.setText(Html.fromHtml(listAttending.getEvent_title() + ":" + listAttending.getEvent_title(), Html.FROM_HTML_MODE_LEGACY));
                holder.mcardView.setCardBackgroundColor(Color.parseColor("#98cbe5")); // will change the background color of the card view to sky blue

            } else {
                holder.attendng_list.setText(Html.fromHtml(listAttending.getEvent_title() + " : " + listAttending.getInvitername()));
                holder.attendng_list.setTextColor(Color.parseColor("#000000"));
                holder.mcardView.setCardBackgroundColor(Color.parseColor("#98cbe5")); // will change the background color of the card view to sky blue

            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.attendng_list.setText(Html.fromHtml(listAttending.getEvent_title() + ":" + listAttending.getEvent_title(), Html.FROM_HTML_MODE_LEGACY));
                holder.mcardView.setCardBackgroundColor(Color.parseColor("#ffff99")); // will change the background color of the card view to red

            } else {
                holder.attendng_list.setText(Html.fromHtml(listAttending.getEvent_title() + " : " + listAttending.getInvitername()));
                holder.attendng_list.setTextColor(Color.parseColor("#000000"));
                holder.mcardView.setCardBackgroundColor(Color.parseColor("#ffff99")); // will change the background color of the card view to red

            }


        }

    }

    @Override
    public int getItemCount() {
        return (null != attendinglist ? attendinglist.size() : 0);
    }

    public class CompletedEventAdapterviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView attendng_list;
        CardView mcardView;
        ArrayList<CompletedEvent> listevent = new ArrayList<>();
        Context ctx;
        Dialog dialog;

        public CompletedEventAdapterviewholder(View itemView, Context mContext, List<CompletedEvent> listevent, Dialog d) {
            super(itemView);
            this.listevent = (ArrayList<CompletedEvent>) listevent;
            this.ctx = mContext;
            this.dialog = d;
            itemView.setOnClickListener(this);
            attendng_list = (TextView) itemView.findViewById(R.id.attendng_name);
            mcardView = (CardView) itemView.findViewById(R.id.cardlist_item);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            CompletedEvent listEvent = this.listevent.get(position);

            listEvent = new CompletedEvent(listEvent.getId(), listEvent.getEvent_title(), listEvent.getUser_status(), listEvent.getInvitername(), listEvent.getEvent_status(), null, listEvent.getShare_detail(),listEvent.getArtwork(),listEvent.getEvent_type(),listEvent.getChat_window());
            MyApplication.getInstance().getPrefManager().storeEventIdCompletedEvent(listEvent);
            int user_Status = Integer.parseInt(listEvent.getUser_status());

            if (user_Status == 1) {
                Toast.makeText(this.ctx, "You did not respond to this Event!!", Toast.LENGTH_LONG).show();
            } else if (user_Status == 3) {
                Toast.makeText(this.ctx, "You said you are not attending this Event!!", Toast.LENGTH_LONG).show();

            } else {
                Intent i = new Intent(mContext, UserTabView.class);
                i.putExtra("event_title", listEvent.getEvent_title());
                i.putExtra("share_detail", listEvent.getShare_detail());
                i.putExtra("artwork", listEvent.getArtwork());
                i.putExtra("eventtype", listEvent.getEvent_type());
                i.putExtra("chatw", listEvent.getChat_window());
                i.putExtra("classcheck", "completedevent");

                this.ctx.startActivity(i);
                dialog.dismiss();

            }


        }
    }


}