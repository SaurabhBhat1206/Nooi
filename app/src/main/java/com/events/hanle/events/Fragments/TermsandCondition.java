package com.events.hanle.events.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.events.hanle.events.R;


public class TermsandCondition extends Fragment {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_termsand_condition, container, false);
        TextView t = (TextView) v.findViewById(R.id.tandc);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            t.setText(Html.fromHtml(getString(R.string.hello_blank_fragment), Html.FROM_HTML_MODE_LEGACY));
        } else {
            t.setText(Html.fromHtml(getString(R.string.hello_blank_fragment)));
        }


        return v;
    }


}
