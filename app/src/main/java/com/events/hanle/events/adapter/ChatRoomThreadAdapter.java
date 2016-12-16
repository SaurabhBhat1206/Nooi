package com.events.hanle.events.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.events.hanle.events.Model.Message;
import com.events.hanle.events.R;
import com.events.hanle.events.interf.BackPressListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.events.hanle.events.R.id.recyclerView;

public class ChatRoomThreadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = ChatRoomThreadAdapter.class.getSimpleName();

    private String userId;
    private int SELF = 100;
    private static String today;
    private Context mContext;
    private ArrayList<Message> messageArrayList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message, timestamp, name;
        RecyclerView recyclerView;

        public ViewHolder(View view) {
            super(view);
            message = (TextView) itemView.findViewById(R.id.message);
            name = (TextView) itemView.findViewById(R.id.timestamp);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp_time);

        }
    }


    public ChatRoomThreadAdapter(Context mContext, ArrayList<Message> messageArrayList, String userId) {
        this.mContext = mContext;
        this.messageArrayList = messageArrayList;
        this.userId = userId;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_other1, parent, false);
        } else {
            // others message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_other, parent, false);
        }


        return new ViewHolder(itemView);
    }


    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        if (message.getUser().getId().equals(userId)) {
            return SELF;
        }

        return position;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Message message = messageArrayList.get(position);
        ((ViewHolder) holder).message.setText(message.getMessage());

        String timestamp = message.getCreatedAt();
        String name = message.getUser().getName();

        if (message.getUser().getName() != null)

            ((ViewHolder) holder).name.setText(name);
        ((ViewHolder) holder).timestamp.setText(timestamp);
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }


}

