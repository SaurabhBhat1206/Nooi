package com.events.hanle.events.Activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.events.hanle.events.R;

public class OrganiserLogin extends AppCompatActivity {

    EditText email,password;
    TextInputLayout inputLayoutemail, inputpassword;
    Button organiser_login;
    CoordinatorLayout coordinatorLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_login);
        inputLayoutemail = (TextInputLayout) findViewById(R.id.email_input_layout);
        inputpassword = (TextInputLayout) findViewById(R.id.password_input_layout);
        email = (EditText)findViewById(R.id.email_organiser);
        password = (EditText)findViewById(R.id.password_organiser);
        organiser_login = (Button)findViewById(R.id.organiser_login);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);

        organiser_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("")){

                } else{
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Email is empty", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }
        });


    }
}
