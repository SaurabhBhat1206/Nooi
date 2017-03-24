package com.events.hanle.events.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.events.hanle.events.Model.Attending;
import com.events.hanle.events.R;

import java.util.ArrayList;

/**
 * Created by Hanle on 3/8/2017.
 */

public class InviteeListAdapter extends RecyclerView.Adapter<InviteeListAdapter.InviteeListAdapterviewholder> {
    private static final String TAG = "ListAttending";
    private ArrayList<Attending> inviteelist;
    private Context mContext;
    AppCompatButton invitee;
    ArrayList<Integer> selectedStrings = new ArrayList<>();

    public InviteeListAdapter(Context context, ArrayList<Attending> inviteelist, AppCompatButton invitee) {
        this.inviteelist = inviteelist;
        this.mContext = context;
        this.invitee = invitee;
    }


    @Override
    public InviteeListAdapterviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_list_adapter, null);
        InviteeListAdapterviewholder viewHolder = new InviteeListAdapterviewholder(view, mContext, inviteelist);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final InviteeListAdapterviewholder holder, int position) {
        Attending attending = inviteelist.get(position);


        TableRow row = new TableRow(mContext);

        //TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        //row.setLayoutParams(lp);

        holder.ac = new AppCompatCheckBox(mContext);
        holder.attendingname = new TextView(mContext);
        holder.attendingmobile = new TextView(mContext);
        row.addView(holder.ac);
        row.addView(holder.attendingname);
        row.addView(holder.attendingmobile);

        holder.attendingname.setText(attending.getNsme());
        holder.attendingmobile.setText(attending.getMobile());
        holder.ac.setId(Integer.parseInt(attending.getId()));
        holder.ll.addView(row);

        //System.out.println("Size of array"+inviteelist.get(i).getNsme());
        System.out.println("Size of array" + inviteelist.size());

        invitee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < selectedStrings.size(); i++) {
                    System.out.println("Selected Id's are" + selectedStrings.get(i));
                }

            }
        });

        holder.ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkBoxId = v.getId();
                if (((CheckBox) v).isChecked()) {
                    selectedStrings.add(holder.ac.getId());

                } else {
                    selectedStrings.remove(holder.ac.getId());
                }

                Toast.makeText(mContext, "Clicked id is" + String.valueOf(checkBoxId), Toast.LENGTH_SHORT).show();
            }
        });

//        holder.ac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    selectedStrings.add(holder.ac.getId());
//                } else {
//                    selectedStrings.remove(holder.ac.getId());
//                }
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return (null != inviteelist ? inviteelist.size() : 0);
    }

    public class InviteeListAdapterviewholder extends RecyclerView.ViewHolder {
        TextView attendingname, attendingmobile;
        AppCompatCheckBox ac;
        TableLayout ll;
        private ArrayList<Attending> inviteelist;


        public InviteeListAdapterviewholder(View itemView, Context mContext, ArrayList<Attending> attendinglist) {
            super(itemView);
            ll = (TableLayout) itemView.findViewById(R.id.tb);
//            attendingname = (TextView) itemView.findViewById(R.id.name_invitee);
//            attendingmobile = (TextView) itemView.findViewById(R.id.phoneno);
            //ac = (AppCompatCheckBox) itemView.findViewById(R.id.checkBox);


        }


    }
}
