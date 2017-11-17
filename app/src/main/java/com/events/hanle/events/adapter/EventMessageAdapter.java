package com.events.hanle.events.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Model.EventMessage;
import com.events.hanle.events.Model.Message;
import com.events.hanle.events.R;
import com.events.hanle.events.app.MyApplication;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        ImageView attachment;

        public EventMessageViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            cv = (CardView) itemView.findViewById(R.id.cardlist_item);
            attachment = (ImageView) itemView.findViewById(R.id.attachment);

            description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager myClipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);


                    ClipData myClip = ClipData.newPlainText("label", description.getText().toString());
                    myClipboard.setPrimaryClip(myClip);
                    Toast.makeText(v.getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();


                }
            });
            if(attachment!=null){
                attachment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        EventMessage eventMessage = eventmessageArrayList.get(position);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(eventMessage.getAttachment()));
                        mContext.startActivity(browserIntent);
                    }
                });

            }


        }
    }

    public EventMessageAdapter(Context context, ArrayList<EventMessage> eventmessagelist, String s) {
        this.mContext = context;
        this.eventmessageArrayList = eventmessagelist;
        this.sm = s;

    }


    @Override
    public EventMessageAdapter.EventMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;

        if (viewType == PARTNER) {
            // self message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_fragemnt_row_three, parent, false);

        } else if (viewType == ORGNAISER) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_fragment_three_organiser, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_fragment_three_organiser, parent, false);
        }
        // others message
        return new EventMessageViewHolder(itemView);

    }

    @Override
    public int getItemViewType(int position) {
        EventMessage eventMessage = eventmessageArrayList.get(position);
        if (eventMessage.getPushtype() != null && eventMessage.getPushtype().equalsIgnoreCase("partner")) {
            return PARTNER;

        } else if (eventMessage.getPushtype() != null && eventMessage.getPushtype().equalsIgnoreCase("organiser")) {
            return ORGNAISER;
        }
        return position;

    }

    @Override
    public void onBindViewHolder(EventMessageAdapter.EventMessageViewHolder holder, int position) {
        EventMessage eventMessage = eventmessageArrayList.get(position);
        ArrayList links = new ArrayList();

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
            event_status = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEventStatus();

        }

        int es = Integer.parseInt(event_status);
        System.out.println("oota******" + event_status);

        if (es == 2 || es == 3) {
            holder.title.setText(eventMessage.getTitle());
            holder.title.setTypeface(null, Typeface.BOLD);
            holder.description.setText(eventMessage.getDescription());
            holder.description.setMovementMethod(LinkMovementMethod.getInstance());
            holder.attachment.setVisibility(View.GONE);

            holder.description.setTextColor(Color.WHITE);
            holder.timestamp.setVisibility(View.GONE);
        } else if (es == 1) {
            if (eventMessage.getTimestamp() != null) {
                if (eventMessage.getPushtype().equalsIgnoreCase("partner") && eventMessage.getAttachment() == null) {
                    holder.title.setText(eventMessage.getTitle());
                    holder.attachment.setVisibility(View.GONE);
                    holder.description.setText(eventMessage.getPush_message());
                    if (eventMessage.getTimestamp().length() > 24) {
                        holder.timestamp.setText(eventMessage.getTimestamp().substring(0, 24));

                    } else {
                        holder.timestamp.setText(eventMessage.getTimestamp());

                    }
                } else if (eventMessage.getPushtype().equalsIgnoreCase("organiser") && eventMessage.getAttachment() == null) {
                    holder.title.setText(com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getInviterName());
                    holder.description.setText(eventMessage.getPush_message());
                    holder.attachment.setVisibility(View.GONE);
                    if (eventMessage.getTimestamp().length() > 24) {
                        holder.timestamp.setText(eventMessage.getTimestamp().substring(0, 24));

                    } else {
                        holder.timestamp.setText(eventMessage.getTimestamp());

                    }
                } else if (eventMessage.getPushtype().equalsIgnoreCase("organiser") && eventMessage.getAttachment() != null) {
                    holder.title.setText(com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getInviterName());
                    holder.description.setText(eventMessage.getPush_message());
                    holder.attachment.setVisibility(View.VISIBLE);
                    if (eventMessage.getTimestamp().length() > 24) {
                        holder.timestamp.setText(eventMessage.getTimestamp().substring(0, 24));

                    } else {
                        holder.timestamp.setText(eventMessage.getTimestamp());

                    }
                    if (eventMessage.getAttachment().contains(".pdf")) {
                        holder.attachment.setBackgroundResource(R.drawable.file_pdf);

                    } else if (eventMessage.getAttachment().contains(".xls") || eventMessage.getAttachment().contains(".xlsx")) {
                        holder.attachment.setBackgroundResource(R.drawable.file_excel);

                    } else if (eventMessage.getAttachment().contains(".ppt") || eventMessage.getAttachment().contains(".pptx")) {
                        holder.attachment.setBackgroundResource(R.drawable.file_powerpoint);

                    } else if (eventMessage.getAttachment().contains(".doc") || eventMessage.getAttachment().contains(".docx")) {
                        holder.attachment.setBackgroundResource(R.drawable.file_word);

                    } else if (eventMessage.getAttachment().contains(".jpg") || eventMessage.getAttachment().contains(".jpeg") || eventMessage.getAttachment().contains(".png")) {
                        holder.attachment.setBackgroundResource(R.drawable.file_image);

                    }
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
