package com.events.hanle.events.Fragments;

/**
 * Created by Hanle on 2/21/2017.
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.events.hanle.events.Activity.OrganiserContactForm;
import com.events.hanle.events.Model.Invitee;
import com.events.hanle.events.R;
import com.events.hanle.events.app.MyApplication;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ListOfOrganiserActionsFragment extends DialogFragment {

    CardView inviteemanagemnt, inviteelist,pushmessage,coupondisplay,cancelevent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_list_of_organiser_actions, container, false);

        inviteemanagemnt = (CardView) v.findViewById(R.id.invitee_management);
        inviteelist = (CardView) v.findViewById(R.id.cardlist_invited);
        pushmessage = (CardView) v.findViewById(R.id.push_message);
        coupondisplay = (CardView) v.findViewById(R.id.coupon_detials);
        cancelevent = (CardView) v.findViewById(R.id.cancel_event);

        ImageButton imageButton = (ImageButton) v.findViewById(R.id.logout);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLocationDialog();

            }
        });


        inviteemanagemnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), OrganiserContactForm.class);
                startActivity(i);
            }
        });

        inviteelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                dismissAllowingStateLoss();
                InviteeList d = new InviteeList();
                d.show(getActivity().getSupportFragmentManager(), "inviteelist");
            }
        });


        pushmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                dismissAllowingStateLoss();
                OrganiserPushMessage d = new OrganiserPushMessage();
                d.show(getActivity().getSupportFragmentManager(), "pushmessage");
            }
        });

        return v;
    }

    private void showLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure you want to logout?");

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                        dismissAllowingStateLoss();
                        Toast.makeText(getActivity(), "Successfully logged out!!", Toast.LENGTH_LONG).show();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLUE);
        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLUE);
    }


    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(android.content.DialogInterface dialog,
                                 int keyCode, android.view.KeyEvent event) {
                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    // To dismiss the fragment when the back-button is pressed.
                    return true;
                }
                // Otherwise, do nothing else
                else return false;
            }
        });

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
}
