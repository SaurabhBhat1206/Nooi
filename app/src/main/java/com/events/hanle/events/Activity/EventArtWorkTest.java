package com.events.hanle.events.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.events.hanle.events.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Hanle on 3/17/2017.
 */

public class EventArtWorkTest extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artwork);
        imageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this)
                .load("https://www.simplifiedcoding.net/wp-content/uploads/2015/10/advertise.png")
                .placeholder(R.drawable.placeholder)   // optional
                .error(android.R.drawable.ic_dialog_alert)      // optional
                .resize(400, 400)
                .into(imageView);

    }
}

