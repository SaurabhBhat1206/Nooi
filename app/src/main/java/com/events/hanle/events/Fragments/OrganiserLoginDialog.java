package com.events.hanle.events.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.User;
import com.events.hanle.events.R;
import com.events.hanle.events.interf.MyDialogCloseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


/**
 * Created by Hanle on 2/20/2017.
 */

public class OrganiserLoginDialog extends DialogFragment {
    EditText email, password;
    TextInputLayout inputLayoutemail, inputpassword;
    Button organiser_login;
    CoordinatorLayout coordinatorLayout;
    String event_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_organiser_login, container, false);

        String s = getActivity().getIntent().getStringExtra("classcheck");

        inputLayoutemail = (TextInputLayout) v.findViewById(R.id.email_input_layout);
        inputpassword = (TextInputLayout) v.findViewById(R.id.password_input_layout);
        email = (EditText) v.findViewById(R.id.email_organiser);
        password = (EditText) v.findViewById(R.id.password_organiser);
        organiser_login = (Button) v.findViewById(R.id.organiser_login);
        coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorLayout);

        intialization();


        organiser_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionDetector.isInternetAvailable(getActivity())) {
                   // checkLogin();
                    getDialog().dismiss();
                    Bundle args = new Bundle();
                    args.putString("orgniser_id", "1");
                    ListOfOrganiserActionsFragment dialogFragment = new ListOfOrganiserActionsFragment();
                    dialogFragment.setArguments(args);
                    dialogFragment.show(getActivity().getSupportFragmentManager(), "missiles");

                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "No Internet!!!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });
        return v;
    }

    private void intialization() {
        UserTabView activity = (UserTabView) getActivity();

        String s = getActivity().getIntent().getStringExtra("classcheck");
        if (s != null) {
            if (s.equalsIgnoreCase("cancelledevent")) {
                event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getId();

            } else if (s.equalsIgnoreCase("completedevent")) {
                event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getId();
            } else if (s.equalsIgnoreCase("from_notifications")) {
                event_id = activity.getIntent().getStringExtra("chat_room_id");
            } else if (s.equalsIgnoreCase("from_partner")) {
                event_id = activity.getIntent().getStringExtra("eventId");
            } else if (s.equalsIgnoreCase("from_organiser")) {
                event_id = activity.getIntent().getStringExtra("eventId");
            }
        } else {
            event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getId();

        }
    }

    private void checkLogin() {

        final String email_id = email.getText().toString();
        final String pwd = password.getText().toString();


//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage("Sending OTP please wait...");
//            progressDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebUrl.ORGANISER_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                //progressDialog.hide();

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.length() != 0) {
                        String message, orgniser_id;
                        int success;
                        // user successfully logged in
                        success = obj.getInt("result");
                        message = obj.getString("message");
                        orgniser_id = obj.getString("orgniser_id");


                        if (success == 1) {

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            getDialog().dismiss();
                            Bundle args = new Bundle();
                            args.putString("orgniser_id", orgniser_id);
                            ListOfOrganiserActionsFragment dialogFragment = new ListOfOrganiserActionsFragment();
                            dialogFragment.setArguments(args);
                            dialogFragment.show(getActivity().getSupportFragmentManager(), "missiles");
                        } else {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                    } else {
                        // login error - simply toast the message
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Something went wrong please try after sometime", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    //Toast.makeText(getActivity(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Something went wrong please try after sometime", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.hide();
                //btnEnter.setEnabled(true);

                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                //Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(), "Something went wrong please try after sometime", Toast.LENGTH_LONG).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getActivity().getString(R.string.error_network_timeout), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (error instanceof ServerError) {

                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getActivity().getString(R.string.error_network_server), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Server did not respond!!", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email_id);
                params.put("password", pwd);
                params.put("eventId", event_id);
                Log.e(TAG, "params: " + params.toString());

                return params;
            }
        };


        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        com.events.hanle.events.app.MyApplication.getInstance().addToRequestQueue(strReq);

    }

    public void onDismiss(DialogInterface dialog)
    {
        Activity activity = getActivity();
        if(activity instanceof MyDialogCloseListener)
            ((MyDialogCloseListener)activity).handleDialogClose(dialog);
        Log.d("check","dialog dismissed");
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
