package com.events.hanle.events.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.events.hanle.events.Model.Message;
import com.events.hanle.events.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;


public class ChatRoomThreadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = ChatRoomThreadAdapter.class.getSimpleName();

    private String userId;
    private int SELF = 100;
    private int OTHER = 101;
    private static String today;
    private Context mContext;
    private LinkedList<Message> messageArrayList;
    private String finalTimestamp;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message, timestamp, name;

        public ViewHolder(View view) {
            super(view);
            message = (TextView) itemView.findViewById(R.id.message);
            name = (TextView) itemView.findViewById(R.id.otheruser_name);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp_time);

        }
    }


    public ChatRoomThreadAdapter(Context mContext, LinkedList<Message> messageArrayList, String userId) {
        this.mContext = mContext;
        this.messageArrayList = messageArrayList;
        this.userId = userId;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_user2_item, parent, false);
        } else if (viewType == OTHER) {
            // others message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chatuser1item, parent, false);

        }

        return new ViewHolder(itemView);

    }

    private static boolean isValidDate(String input) {
        String formatString = "yyyy-MM-dd HH:mm:ss";

        try {
            SimpleDateFormat format = new SimpleDateFormat(formatString);
            format.setLenient(false);
            format.parse(input);
        } catch (ParseException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);

        if (message.getUser().getId().equals(userId)) {
            return SELF;
        } else if (!message.getUser().getId().equals(userId)) {
            return OTHER;
        }

        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messageArrayList.get(position);
        ((ViewHolder) holder).message.setText(message.getMessage());

        String timestamp = message.getCreatedAt();
        String name = message.getUser().getName();
        if (isValidDate(timestamp)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d;

            try {
                d = sdf.parse(timestamp);
                sdf.applyPattern("MMM dd HH:mm");
                finalTimestamp = sdf.format(d);

                System.out.println("Conversiondatee****:" + finalTimestamp);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            finalTimestamp = timestamp;
        }

        if (name != null) {
            if (message.getUser().getId().equals(userId)) {
                ((ViewHolder) holder).timestamp.setText(finalTimestamp);
            } else {
                ((ViewHolder) holder).name.setText(name);
                ((ViewHolder) holder).timestamp.setText(finalTimestamp);
            }

        }

    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }


}

