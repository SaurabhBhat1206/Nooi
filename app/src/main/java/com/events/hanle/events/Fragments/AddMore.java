package com.events.hanle.events.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.events.hanle.events.Activity.ListOfEvent1;
import com.events.hanle.events.Activity.OrganiserContactForm;
import com.events.hanle.events.R;
import com.events.hanle.events.app.MyApplication;

/**
 * Created by Hanle on 5/10/2017.
 */

public class AddMore extends DialogFragment {

    private AppCompatButton addmore, logout;


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.addmore, container, false);
        addmore = (AppCompatButton) v.findViewById(R.id.addmore);
        logout = (AppCompatButton) v.findViewById(R.id.logout);

        addmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getInstance().getPrefManager().listEventgetorganiserId() != null) {
                    dismiss();
                    dismissAllowingStateLoss();
                    Intent i = new Intent(getActivity(), OrganiserContactForm.class);
                    startActivity(i);
                    getActivity().finish();

                } else {
                    Toast.makeText(getActivity(), "Something went wrong please logout and login!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                dismissAllowingStateLoss();
                Intent i = new Intent(getActivity(), ListOfEvent1.class);
                startActivity(i);
                getActivity().finish();
                Toast.makeText(getActivity(), "Successfully logged out", Toast.LENGTH_SHORT).show();

            }
        });

        return v;
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
        return dialog;
    }
}
