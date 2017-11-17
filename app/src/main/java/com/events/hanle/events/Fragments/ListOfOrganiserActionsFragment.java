package com.events.hanle.events.Fragments;

/**
 * Created by Hanle on 2/21/2017.
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Activity.ListOfEvent1;
import com.events.hanle.events.Activity.OrganiserContactForm;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.Invitee;
import com.events.hanle.events.Model.User;
import com.events.hanle.events.R;
import com.events.hanle.events.app.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ListOfOrganiserActionsFragment extends DialogFragment {

    CardView inviteemanagemnt, inviteelist, pushmessage, coupondisplay, cancelevent;

    private static final String TAG = "ListOfOrganis";


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


        cancelevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().hide();
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("A Cancel Message will be sent to the invitee.")
                        .setCancelText("No")
                        .setConfirmText("Yes")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                // reuse previous dialog instance, keep widget user state, reset them if you need
                                sDialog.dismiss();
                                getDialog().show();

                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sendCancelMessage(sDialog);

                            }

                        })
                        .show();
            }


        });

        coupondisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                dismissAllowingStateLoss();
                CouponDetials couponDetials = new CouponDetials();
                couponDetials.show(getActivity().getSupportFragmentManager(), "couponDetials");
            }
        });


        return v;
    }


    private void sendCancelMessage(final SweetAlertDialog sDialog) {


        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebUrl.CANCEL_MESSAGE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.length() != 0) {
                        String message;
                        int success;
                        // user successfully logged in
                        success = obj.getInt("result");
                        message = obj.getString("message");

                        if (success == 1) {


                            dismiss();
                            dismissAllowingStateLoss();

                            sDialog.setTitleText("Cancelled")
                                    .setContentText("Your event has been cancelled.")
                                    .setConfirmText("OK")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                Intent i = new Intent(getActivity(), ListOfEvent1.class);
                                startActivity(i);
                                getActivity().finish();
                                Toast.makeText(getActivity(), "Event cancelled successfully and logged out!!", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // login error - simply toast the message
                        Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());

                    Toast.makeText(getActivity(), "Something went wrong please try after sometime", Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(),
                            getActivity().getString(R.string.error_network_timeout),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getActivity(),
                            getActivity().getString(R.string.error_network_server),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Server did not respond!!", Toast.LENGTH_SHORT).show();

                }

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("EventId", com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEventId());
                params.put("status", "2");
                params.put("organiser_id", MyApplication.getInstance().getPrefManager().getOrganiserID());
                Log.d(TAG, params.toString());

                return params;
            }
        };

//        strReq.setRetryPolicy(new DefaultRetryPolicy(
//                WebUrl.MY_SOCKET_TIMEOUT_MS,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to request queue

        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        com.events.hanle.events.app.MyApplication.getInstance().addToRequestQueue(strReq);
    }


    private void showLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to exit?");

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
