package com.events.hanle.events.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.events.hanle.events.R;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class OrganiserContactForm extends AppCompatActivity {

    AppCompatButton pickphonecontact, createinvitee;
    EditText firstname, lastname, phone, countrycode;
    private static final int RESULT_PICK_CONTACT = 1;
    private CountryPicker countryPicker;
    private String country_code = null;
    private CoordinatorLayout coordinatorLayout;
    TextView hlp;

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

        firstname = (EditText) findViewById(R.id.input_name_organiser_contact_form);
        lastname = (EditText) findViewById(R.id.input_lastname);
        phone = (EditText) findViewById(R.id.input_mobileno);
        countrycode = (EditText) findViewById(R.id.input_countrycode_organiser);
        hlp = (TextView) findViewById(R.id.help);
        countrycode.setKeyListener(null);
        countryPicker = CountryPicker.newInstance("Select Country");

        createinvitee = (AppCompatButton) findViewById(R.id.create_invitee);
        pickphonecontact = (AppCompatButton) findViewById(R.id.create_invitee_from_phone);
        pickphonecontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });
        countrycodepicker();

        createinvitee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createinvitee();
            }
        });

       hlp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               new SimpleTooltip.Builder(OrganiserContactForm.this)
                       .anchorView(hlp)
                       .text("Texto do Tooltip")
                       .gravity(Gravity.LEFT)
                       .animated(true)
                       .transparentOverlay(false)
                       .build()
                       .show();
           }
       });

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
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();

            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);

            firstname.setText(name);
            phone.setText(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void createinvitee() {
        String fn,ln,cc,pn;

        fn = firstname.getText().toString().trim();
        ln = lastname.getText().toString().trim();
        cc = countrycode.getText().toString().trim();
        pn = phone.getText().toString().trim();

        if(fn.equals("") || ln.equals("") || cc.equals("") || pn.equals("")){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "All fields are required!!", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if(fn.equals("")){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Please give First Name", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if(ln.equals("")){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Please give Last Name", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if(cc.equals("")){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Please give Country Code", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if(pn.equals("")){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Please give Mobile Number", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if(pn.contains(" ")){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Please remove the space from Mobile Number", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if(pn.contains("+")){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Please remove the country code from Mobile Number", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}




