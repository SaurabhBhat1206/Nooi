package com.events.hanle.events.Fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.events.hanle.events.R;

/**
 * Created by Hanle on 9/7/2016.
 */
public class MuteDialog extends DialogFragment {
    SwitchCompat mySwitch;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String USER_INPUT = "userinput";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_mute, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mySwitch = (SwitchCompat) v.findViewById(R.id.mySwitch);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        mySwitch.setChecked(false);

        if (sharedpreferences.getString(USER_INPUT, null).equalsIgnoreCase("on")) {
            mySwitch.setChecked(true);
        }

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    editor.putString(USER_INPUT, "ON");
                    editor.commit();

                    Toast.makeText(getActivity(), "on", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString(USER_INPUT, "OFF");
                    editor.commit();

                    Toast.makeText(getActivity(), "off", Toast.LENGTH_SHORT).show();

                }

            }
        });


        return v;

    }
}
