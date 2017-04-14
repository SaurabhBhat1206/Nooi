package com.events.hanle.events.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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
import com.events.hanle.events.interf.P;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hanle on 3/8/2017.
 */

public class InviteeListAdapter extends RecyclerView.Adapter<InviteeListAdapter.InviteeListAdapterviewholder> {
    private static final String TAG = "ListAttending";
    private List<Attending> inviteelist = new ArrayList<>();
    private List<Integer> inviteelistTemp = new ArrayList<>();
    private List<AlreadyInvitedUser> alreadyinvited;

    private Context mContext;
    AppCompatButton invitee;
    List<String> checkedist = new ArrayList<>();
    Attending attending;
    AlreadyInvitedUser alredyinvi;
    int temp;
    Dialog d;

    public InviteeListAdapter(Context context, List<Attending> inviteelist, List<AlreadyInvitedUser> alreadyinvited, AppCompatButton invitee, Dialog dialog) {
        this.inviteelist = inviteelist;
        this.alreadyinvited = alreadyinvited;
        this.mContext = context;
        this.invitee = invitee;
        this.d = dialog;
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


        if (attending.getNsme().length() < 15) {
            holder.attendingname.setText(attending.getNsme());
        } else {
            holder.attendingname.setText(attending.getNsme().substring(0, 12) + "..");
        }
        holder.attendingmobile.setText(attending.getMobile());
        holder.ac.setId(attending.getId());
        holder.ac.setTag(holder);


        for (int i = 0; i < alreadyinvited.size(); i++) {
            for (int j = 0; j < inviteelist.size(); j++) {
                if (alreadyinvited.get(i).getAlreadyinvited() == (inviteelist.get(j).getId())) {
                    inviteelistTemp.add(inviteelist.get(j).getId());
                    System.out.println("Duplicates are" + inviteelist.get(j).getId());
                }
            }

        }
        for (int k = 0; k < inviteelistTemp.size(); k++) {
            if (inviteelistTemp.get(k) == attending.getId()) {
                holder.ac.setChecked(true);
                holder.ac.setEnabled(false);
            }
        }


        holder.ac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    checkedist.add(String.valueOf(holder.ac.getId()));
                    System.out.println("Checked ID is" + String.valueOf(holder.ac.getId()));
                    invitee.setVisibility(View.VISIBLE);

                } else {
                    System.out.println("deleted is " + String.valueOf(holder.ac.getId()));
                    checkedist.remove(String.valueOf(holder.ac.getId()));
                    invitee.setVisibility(View.GONE);

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
                    if (checkedist.size() > 1) {
                        Toast.makeText(mContext, "You cannot invite more than one user!!", Toast.LENGTH_LONG).show();
                    } else {
                        d.dismiss();
                        ((UserTabView) mContext).Inviteepost(checkedist);

                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != inviteelist ? inviteelist.size() : 0);
    }

    public void setFilter(List<Attending> attendings) {
        notifyDataSetChanged();
    }


    public class InviteeListAdapterviewholder extends RecyclerView.ViewHolder {
        TextView attendingname, attendingmobile;
        AppCompatCheckBox ac;
        TableLayout ll;
        private ArrayList<Attending> inviteelist;


        public InviteeListAdapterviewholder(View itemView, Context mContext, List<Attending> attendinglist) {
            super(itemView);
            ll = (TableLayout) itemView.findViewById(R.id.tb);
            attendingname = (TextView) itemView.findViewById(R.id.name_invitee);
            attendingmobile = (TextView) itemView.findViewById(R.id.phoneno);
            ac = (AppCompatCheckBox) itemView.findViewById(R.id.checkBox);


        }


    }
}
