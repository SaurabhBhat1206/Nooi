package com.events.hanle.events.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.R;
import com.events.hanle.events.app.MyApplication;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Hanle on 5/10/2017.
 */


public class OrganiserContactFormListEvent extends AppCompatActivity {

    private AppCompatButton pickphonecontact, createinvitee;
    private AppCompatEditText firstname, lastname, phone, countrycode;
    private static final int RESULT_PICK_CONTACT = 1;
    private CountryPicker countryPicker;
    String country_code = null;
    private CoordinatorLayout coordinatorLayout;
    TextView hlp;
    String organiser_id;
    private AVLoadingIndicatorView avi;
    private FloatingActionButton fab;
    private String TAG = OrganiserContactFormListEvent.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_contact_form);
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);

        assert t != null;
        t.setTitleTextColor(Color.WHITE);
        setSupportActionBar(t);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("nooi");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl);

        firstname = (AppCompatEditText) findViewById(R.id.input_name_organiser_contact_form);
        lastname = (AppCompatEditText) findViewById(R.id.input_lastname);
        phone = (AppCompatEditText) findViewById(R.id.input_mobileno);
        countrycode = (AppCompatEditText) findViewById(R.id.input_countrycode_organiser);
        //hlp = (TextView) findViewById(R.id.help);
        countrycode.setKeyListener(null);
        countryPicker = CountryPicker.newInstance("Select Country");


        createinvitee = (AppCompatButton) findViewById(R.id.create_invitee);
        //pickphonecontact = (AppCompatButton) findViewById(R.id.create_invitee_from_phone);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.System.canWrite(getApplicationContext())) {
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 2909);
                    } else {
                        // continue with your code
                        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);

                    }
                } else {
                    // continue with your code
                    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);

                }

            }
        });
        countrycodepicker();

        createinvitee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createinvitee();
            }
        });


        organiser_id = MyApplication.getInstance().getPrefManager().listEventgetorganiserId();
        System.out.println("organiserID " + organiser_id);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                    //Toast.makeText(getApplicationContext(), "Permission Granted!!", Toast.LENGTH_LONG).show();
                    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);

                } else {
                    Log.e("Permission", "Denied");
                    Toast.makeText(getApplicationContext(), "Permission Denied!!", Toast.LENGTH_LONG).show();

                }
                return;
            }
        }
    }

    private void countrycodepicker() {
        countrycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
                countryPicker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        countrycode.setText(dialCode + " " + name);
                        country_code = dialCode;
                        countryPicker.dismiss();
                    }
                });
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }

        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String name = null;
            String firname, secname, fullname;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();

            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            fullname = cursor.getString(nameIndex);
            if (fullname != null && fullname.contains(" ")) {
                String[] splittingstring = fullname.split("\\s+");
                firname = splittingstring[0];
                secname = splittingstring[1];
            } else {
                firname = fullname;
                secname = "";

            }

            phoneNo = cursor.getString(phoneIndex);
            //name = cursor.getString(nameIndex);

            firstname.setText(firname);
            lastname.setText(secname);
            phone.setText(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createinvitee() {
        String fn, ln, cc, pn;

        fn = firstname.getText().toString().trim();
        ln = lastname.getText().toString().trim();
        cc = countrycode.getText().toString().trim();
        pn = phone.getText().toString().trim();
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
        Matcher matcher = pattern.matcher(pn);

        if (fn.equals("") || ln.equals("") || cc.equals("") || pn.equals("")) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "All fields are required!!", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (fn.equals("")) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Please give First Name", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (ln.equals("")) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Please give Last Name", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (cc.equals("")) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Please give Country Code", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (pn.equals("")) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Please give Mobile Number", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (!matcher.matches()) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Please remove the special characters", Snackbar.LENGTH_LONG);
            snackbar.show();
        }else {
            InsertContact(fn, ln, cc, pn);
        }

    }

    private void InsertContact(final String fn, final String ln, final String cc, final String pn) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebUrl.ORGANISER_CREATE_NEW_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                //progressDialog.hide();

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.length() != 0) {

                        int success, user_exist;
                        // user successfully logged in
                        success = obj.getInt("success");
                        user_exist = obj.getInt("user_exist");


                        if (success == 1 && user_exist == 0) {
                            showLocationDialog();

                        } else if (success == 0 && user_exist == 1) {
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "User already exist!!", Snackbar.LENGTH_LONG);
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
                            .make(coordinatorLayout, getApplicationContext().getString(R.string.error_network_timeout), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (error instanceof ServerError) {

                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getApplicationContext().getString(R.string.error_network_server), Snackbar.LENGTH_LONG);
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
                params.put("fname", fn);
                params.put("lname", ln);
                params.put("country_code", country_code.substring(1));
                params.put("iphone", pn);
                params.put("organiser_id", organiser_id);
                Log.e(TAG, "params: " + params.toString());

                return params;
            }
        };


        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        com.events.hanle.events.app.MyApplication.getInstance().addToRequestQueue(strReq);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrganiserContactFormListEvent.this);
        builder.setMessage("Successfully Added!!");

        String positiveText = "Add More";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        firstname.setText("");
                        lastname.setText("");
                        countrycode.setText("");
                        phone.setText("");
                        firstname.requestFocus();

                    }
                });

        String negativeText = "Done";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Toast.makeText(getApplicationContext(), "Successfully logged out!!", Toast.LENGTH_LONG).show();


                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.parseColor("#2980b9"));
        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) pbutton.getLayoutParams();
        LinearLayout.LayoutParams negativeButtonLL = (LinearLayout.LayoutParams) nbutton.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER;
        pbutton.setLayoutParams(positiveButtonLL);
        negativeButtonLL.gravity = Gravity.CENTER;
        nbutton.setLayoutParams(positiveButtonLL);

        pbutton.setTextColor(Color.parseColor("#2980b9"));
        pbutton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.adduser, 0, 0, 0);
        nbutton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.logoutleft, 0, 0, 0);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        showLocationDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

            case android.R.id.home:
                showLocationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}





