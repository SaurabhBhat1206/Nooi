package com.events.hanle.events.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Activity.UserChangeOfDescision;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.R;
import com.events.hanle.events.app.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Hanle on 4/14/2017.
 */

public class OrganiserPushMessage extends DialogFragment {

    private AppCompatEditText pushmessage;
    private AppCompatButton cancel, send;
    private TextInputLayout push;
    private static final String TAG = "OrganiserPushMessage";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.organiser_push_message, container, false);

        pushmessage = (AppCompatEditText) v.findViewById(R.id.push_message);
        push = (TextInputLayout) v.findViewById(R.id.push);
        cancel = (AppCompatButton) v.findViewById(R.id.cancel);
        send = (AppCompatButton) v.findViewById(R.id.send);
        send.setEnabled(false);

        push.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 100) {

                    push.setError(getString(R.string.push_error));
                    push.setErrorEnabled(true);
                } else if (s.length() < 0) {
                    send.setEnabled(false);

                } else {
                    send.setEnabled(true);
                    push.setErrorEnabled(false);

                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendPushMessage(pushmessage.getText().toString());
                        }
                    });
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                ListOfOrganiserActionsFragment dialogFragment = new ListOfOrganiserActionsFragment();
                dialogFragment.show(getActivity().getSupportFragmentManager(), "missiles");


            }
            // Otherwise, do nothing else


        });


        return v;
    }

    private void sendPushMessage(final String pushmessage) {
        final ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Sending Push Please wait....");
        progressDialog.show();


        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
        date.setTimeZone(TimeZone.getDefault());
        final String localTime = date.format(currentLocalTime);
        System.out.println("checking date format" + localTime);


        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebUrl.PUSH_MESSAGE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                progressDialog.hide();

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.length() != 0) {
                        String message;
                        int success;
                        success = obj.getInt("result");
                        message = obj.getString("message");

                        if (success == 1) {
                            dismiss();
                            dismissAllowingStateLoss();
                            ListOfOrganiserActionsFragment dialogFragment = new ListOfOrganiserActionsFragment();
                            dialogFragment.show(getActivity().getSupportFragmentManager(), "missiles");

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // login error - simply toast the message
                        Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("organiser_id", MyApplication.getInstance().getPrefManager().getOrganiserID());
                params.put("message", pushmessage);
                params.put("createdDate", localTime);
                params.put("event_id", com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getId());

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                WebUrl.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to request queue
        com.events.hanle.events.app.MyApplication.getInstance().addToRequestQueue(strReq);
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(android.content.DialogInterface dialog,
                                 int keyCode, android.view.KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // To dismiss the fragment when the back-button is pressed.
                    dismiss();
                    ListOfOrganiserActionsFragment dialogFragment = new ListOfOrganiserActionsFragment();
                    dialogFragment.show(getActivity().getSupportFragmentManager(), "missiles");

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
