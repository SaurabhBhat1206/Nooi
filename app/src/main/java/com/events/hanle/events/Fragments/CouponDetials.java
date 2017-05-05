package com.events.hanle.events.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hanle on 4/19/2017.
 */

public class CouponDetials extends DialogFragment {

    AppCompatTextView content, title;
    CardView card, card1;
    private String TAG = "coouponDetals";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.coupon_detials, container, false);

        card = (CardView) v.findViewById(R.id.cv);
        content = (AppCompatTextView) v.findViewById(R.id.content);
        title = (AppCompatTextView) v.findViewById(R.id.title);

         updateConent();

        return v;


    }

    private void updateConent() {
        String endpoint1 = WebUrl.GET_COUPON_DETIALS.replace("EVENTID", com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getId());
        Log.e(TAG, "end point: " + endpoint1);


        StringRequest strReq = new StringRequest(Request.Method.GET,
                endpoint1, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);


                try {
                    JSONObject res = new JSONObject(response);
                    System.out.println("response:" + response);
                    JSONArray jsonArray = res.getJSONArray("coupon_data");
                    StringBuilder sb = new StringBuilder();

                    if (res.getInt("result") == 1) {


                        if (jsonArray.length() > 0) {


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                sb.append("Hello ").append(jsonObject.getString("organiser_firstname")).append(" ");
                                sb.append(jsonObject.getString("organiser_lname")).append(",");
                                sb.append(" show this message to the cashier at ");
                                sb.append(jsonObject.getString("establishment"));
                                sb.append(" to get ");
                                if (jsonObject.getInt("coupontype") == 1) {
                                    sb.append("value ").append(jsonObject.getString("amount")).append("/- ");
                                } else {
                                    sb.append(jsonObject.getString("amount")).append("%").append(" ");
                                }
                                sb.append("off as per Coupon Code ");
                                sb.append("'").append(jsonObject.getString("couponcode")).append("'").append(".");
                                sb.append("This is valid for your ");
                                sb.append(jsonObject.getString("eventname")).append(",");
                                sb.append(" scheduled on ");
                                sb.append(jsonObject.getString("eventdate")).append(" at ").append(jsonObject.getString("eventtime")).append("hrs.");


                                content.setText(sb);
                                content.setTextColor(Color.BLACK);
                                content.setGravity(Gravity.CENTER);


                            }
                        }
                    } else {

                        title.setVisibility(View.GONE);
                        content.setText("No Coupon Found");

                    }


                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity(), "Server did not respond!!", Toast.LENGTH_LONG).show();
                }

//                adapter.notifyDataSetChanged();

                // subscribing to all chat room topics
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getActivity(), "Server did not respond!!", Toast.LENGTH_SHORT).show();
            }
        });


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

        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
}
