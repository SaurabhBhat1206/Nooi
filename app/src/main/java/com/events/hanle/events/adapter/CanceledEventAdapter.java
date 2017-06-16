package com.events.hanle.events.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Model.CanceledEvent;
import com.events.hanle.events.R;
import com.events.hanle.events.app.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hanle on 10/18/2016.
 */

public class CanceledEventAdapter extends RecyclerView.Adapter<CanceledEventAdapter.CanceledEventAdapterviewholder> {
    private static final String TAG = "ListAttending";
    private ArrayList<CanceledEvent> attendinglist;
    private Context mContext;
    Dialog d;
    CardView mCardView;

    public CanceledEventAdapter(Context context, ArrayList<CanceledEvent> listevent, Dialog dialog) {

        this.attendinglist = listevent;
        this.mContext = context;
        this.d = dialog;
    }


    @Override
    public CanceledEventAdapterviewholder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_attending, null);
        CanceledEventAdapterviewholder viewHolder = new CanceledEventAdapterviewholder(view, mContext, attendinglist, d);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CanceledEventAdapterviewholder holder, int position) {
        CanceledEvent listAttending = attendinglist.get(position);
        int status = Integer.parseInt(listAttending.getUser_status());

        if (status == 3) {
            holder.mcardView.setCardBackgroundColor(Color.parseColor("#f42d4e")); // will change the background color of the card view to sky blue
            holder.attendng_list.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.mcardView.setCardBackgroundColor(Color.parseColor("#ffff99")); // will change the background color of the card view to red
            holder.attendng_list.setTextColor(Color.parseColor("#000000"));

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.attendng_list.setText(Html.fromHtml(listAttending.getEvent_title() + ":" + listAttending.getInvitername(), Html.FROM_HTML_MODE_LEGACY));

        } else {
            holder.attendng_list.setText(Html.fromHtml(listAttending.getEvent_title() + ":" + listAttending.getInvitername()));

        }


    }

    @Override
    public int getItemCount() {
        return (null != attendinglist ? attendinglist.size() : 0);
    }

    public class CanceledEventAdapterviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView attendng_list;
        CardView mcardView;
        ArrayList<CanceledEvent> listevent = new ArrayList<>();
        Context ctx;
        Dialog d;

        public CanceledEventAdapterviewholder(View itemView, Context mContext, ArrayList<CanceledEvent> listevent, Dialog dialog) {
            super(itemView);
            this.listevent = listevent;
            this.ctx = mContext;
            this.d = dialog;
            itemView.setOnClickListener(this);

            attendng_list = (TextView) itemView.findViewById(R.id.attendng_name);
            mcardView = (CardView) itemView.findViewById(R.id.cardlist_item);


        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            CanceledEvent canceledevent = this.listevent.get(position);

            canceledevent = new CanceledEvent(canceledevent.getId(), canceledevent.getEvent_title(), canceledevent.getUser_status(), canceledevent.getInvitername(), canceledevent.getEvent_status(), null, canceledevent.getShare_detail(),canceledevent.getArtwork(),canceledevent.getEventtype(),canceledevent.getChatw(),canceledevent.getCountrycode(),canceledevent.getPhone());

            MyApplication.getInstance().getPrefManager().storeEventIdCanceledEvent(canceledevent);
            int user_Status = Integer.parseInt(canceledevent.getUser_status());

            if (user_Status == 3) {
                Toast.makeText(this.ctx, "You said you are not attending this Event!!", Toast.LENGTH_LONG).show();

            } else {
                Intent i = new Intent(mContext, UserTabView.class);
                i.putExtra("event_title", canceledevent.getEvent_title());
                i.putExtra("share_detail", canceledevent.getShare_detail());
                i.putExtra("artwork", canceledevent.getArtwork());
                i.putExtra("eventtype", canceledevent.getEventtype());
                i.putExtra("chatw", canceledevent.getChatw());
                i.putExtra("classcheck", "cancelledevent");

                this.ctx.startActivity(i);
                d.dismiss();

            }
            //callalertDialog();
        }
    }
}
