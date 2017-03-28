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

import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Fragments.InviteeList;
import com.events.hanle.events.Model.AlreadyInvitedUser;
import com.events.hanle.events.Model.Attending;
import com.events.hanle.events.R;

import java.util.ArrayList;

/**
 * Created by Hanle on 3/8/2017.
 */

public class InviteeListAdapter extends RecyclerView.Adapter<InviteeListAdapter.InviteeListAdapterviewholder> {
    private static final String TAG = "ListAttending";
    private ArrayList<Attending> inviteelist;
    private ArrayList<AlreadyInvitedUser> alreadyinvited;
    private Context mContext;
    AppCompatButton invitee;
    ArrayList<String> checkedist = new ArrayList<>();
    Attending attending;
    AlreadyInvitedUser alredyinvi;

    public InviteeListAdapter(Context context, ArrayList<Attending> inviteelist, ArrayList<AlreadyInvitedUser> alreadyinvited, AppCompatButton invitee) {
        this.inviteelist = inviteelist;
        this.alreadyinvited = alreadyinvited;
        this.mContext = context;
        this.invitee = invitee;
    }


    @Override
    public InviteeListAdapterviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_test1, null);
        InviteeListAdapterviewholder viewHolder = new InviteeListAdapterviewholder(view, mContext, inviteelist);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final InviteeListAdapterviewholder holder, int position) {
        attending = inviteelist.get(position);
        alredyinvi = alreadyinvited.get(position);


//        if (alredyinvi.getAlreadyinvited().equals(attending.getId())) {
//            holder.ac.setChecked(true);
//            holder.ac.setEnabled(false);
//            System.out.println("Comparision"+alredyinvi.getAlreadyinvited().equals(attending.getId()));
//            System.out.println("AlreadyInvitedList"+alredyinvi.getAlreadyinvited());
//            System.out.println("InvitedList"+attending.getId());
//        }

//        for (int i = 0; i < inviteelist.size(); i++) {
//            for (int j = 0; j < alreadyinvited.size(); j++) {
//                if (inviteelist.get(i).getId().equals(alreadyinvited.get(j).getAlreadyinvited()))
//                    holder.ac.setEnabled(false);
//                holder.ac.setChecked(true);
//            }
//        }

        for (int counter = 0; counter < inviteelist.size(); counter++) {
            if (alreadyinvited.get(counter).getAlreadyinvited().contains(inviteelist.get(counter).getId())) {
                holder.ac.setEnabled(false);
                holder.ac.setChecked(true);
            }
        }

        holder.attendingname.setText(attending.getNsme());
        holder.attendingmobile.setText(attending.getMobile());
        holder.ac.setId(Integer.parseInt(attending.getId()));

        holder.ac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    checkedist.add(String.valueOf(holder.ac.getId()));
                    System.out.println("Checked ID is" + String.valueOf(holder.ac.getId()));
                } else {
                    System.out.println("deleted is " + String.valueOf(holder.ac.getId()));
                    checkedist.remove(String.valueOf(holder.ac.getId()));

                }
            }
        });

        invitee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                for (int i = 0; i < checkedist.size(); i++) {
//                    System.out.println("The total value is" + checkedist.get(i));
//                }

                if (mContext instanceof UserTabView) {
                    ((UserTabView) mContext).Inviteepost(checkedist);
                }
            }
        });


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
            attendingname = (TextView) itemView.findViewById(R.id.name_invitee);
            attendingmobile = (TextView) itemView.findViewById(R.id.phoneno);
            ac = (AppCompatCheckBox) itemView.findViewById(R.id.checkBox);


        }


    }
}
