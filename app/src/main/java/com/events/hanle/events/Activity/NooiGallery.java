package com.events.hanle.events.Activity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.events.hanle.events.R;
import com.events.hanle.events.adapter.CustomFragmentPageAdapter;

/**
 * Created by Hanle on 7/31/2017.
 */

public class NooiGallery extends AppCompatActivity {

    private static final String TAG = NooiGallery.class.getSimpleName();

    private TabLayout tabLayout;
    public static ViewPager viewPagerGallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nooigallery);
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        assert t != null;
        t.setTitleTextColor(Color.WHITE);
        setSupportActionBar(t);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("nooi gallery");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewPagerGallery = (ViewPager) findViewById(R.id.viewpager);
        assert viewPagerGallery != null;
        viewPagerGallery.setAdapter(new CustomFragmentPageAdapter(getSupportFragmentManager()));
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPagerGallery);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
