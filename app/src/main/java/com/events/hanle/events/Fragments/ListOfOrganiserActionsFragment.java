package com.events.hanle.events.Fragments;

/**
 * Created by Hanle on 2/21/2017.
 */
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.events.hanle.events.Activity.OrganiserContactForm;
import com.events.hanle.events.R;

public class ListOfOrganiserActionsFragment extends DialogFragment {

    CardView inviteemanagemnt,inviteelist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_list_of_organiser_actions, container, false);

        inviteemanagemnt = (CardView) v.findViewById(R.id.invitee_management);
        inviteelist = (CardView)v.findViewById(R.id.cardlist_invited);

        inviteemanagemnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), OrganiserContactForm.class);
                startActivity(i);
            }
        });

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
