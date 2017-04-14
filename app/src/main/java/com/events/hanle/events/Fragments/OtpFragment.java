package com.events.hanle.events.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Activity.ListOfEvent1;
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.User;
import com.events.hanle.events.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import static com.events.hanle.events.R.id.message;

public class OtpFragment extends Fragment {
    static Button b;
    String user_id, mobile, country_code;
    //EditText otp;

    static EditText otp;
    private TextInputLayout otpnputlayout;
    public static final String TAG = "OtpFragment";
    private AlertDialog progressDialog;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String USER_INPUT = "userinput";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_otp, container, false);
        otp = (EditText) v.findViewById(R.id.otp);
        b = (Button) v.findViewById(R.id.submit_otp);
        otpnputlayout = (TextInputLayout) v.findViewById(R.id.input_otp);
        otp.addTextChangedListener(new MyTextWatcher(otp));


        return v;

    }

    public void recivedSms(String message) {
        try {
            otp.setText(message);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    b.performClick();
                }
            }, 1000);

        } catch (Exception e) {
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getTempUserId().getId();
        mobile = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getTempUserId().getMobile();
        country_code = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getTempUserId().getCountrycode();


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ConnectionDetector.isInternetAvailable(getActivity())) {
                    verifyOtp();
                } else {
                    Toast.makeText(getActivity(), "No internet connection!!!", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }


    private void verifyOtp() {
        final String otpuser = otp.getText().toString();
        if (!validateMobile()) {
            return;
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebUrl.CHECK_OTP, new Response.Listener<String>() {


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

                        // user successfully logged in
                        success = obj.getInt("success");
                        message = obj.getString("message");


                        if (success == 1) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            User user = new User(obj.getString("user_id"), obj.getString("name"), obj.getString("phone"), obj.getString("countrycode"));
                            com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().storeUser(user);
                            sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(USER_INPUT, "OFF");
                            editor.commit();
                            Intent i = new Intent(getActivity(), ListOfEvent1.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            getActivity().finish();
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
                //Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("otp", otpuser);
                params.put("phone", mobile);
                params.put("countrycode", country_code);

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        com.events.hanle.events.app.MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateMobile() {
        String userenteredotp = otp.getText().toString().trim();

        if (userenteredotp.isEmpty()) {
            otpnputlayout.setError(getString(R.string.err_msg_otp));
            requestFocus(otpnputlayout);
            return false;
        } else {
            otpnputlayout.setErrorEnabled(false);
        }

        return true;
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        public MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            switch (view.getId()) {
                case R.id.otp:
                    validateMobile();
                    break;

            }

        }
    }
}
