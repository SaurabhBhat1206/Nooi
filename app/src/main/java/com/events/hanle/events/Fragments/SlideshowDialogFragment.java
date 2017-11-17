package com.events.hanle.events.Fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.events.hanle.events.Model.Image;
import com.events.hanle.events.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import uk.co.senab.photoview.PhotoViewAttacher;

import static com.events.hanle.events.Fragments.Artwork.imageRoot;


public class SlideshowDialogFragment extends DialogFragment {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<Image> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;
    private Bitmap bp;
    PhotoViewAttacher mAttacher;

    static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        lblCount = (TextView) v.findViewById(R.id.lbl_count);
        lblTitle = (TextView) v.findViewById(R.id.title);
        lblDate = (TextView) v.findViewById(R.id.date);

        images = (ArrayList<Image>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + images.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }


    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + images.size());

        Image image = images.get(position);
        lblTitle.setText("Uploaded by : " + image.getUploadername());
        lblDate.setText("Time : " + image.getTimestamp());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    //	adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            final ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);
            final FloatingActionButton download = (FloatingActionButton) view.findViewById(R.id.download);

            Image image = images.get(position);

            Glide.with(getContext()).load(image.getUrl())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);

            download.setVisibility(View.VISIBLE);

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageViewPreview != null) {
                        imageViewPreview.buildDrawingCache();
                        bp = imageViewPreview.getDrawingCache();
                        if (bp != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (!Settings.System.canWrite(getContext())) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_EXTERNAL_STORAGE}, 1303);
                                } else {
                                    // continue with your code
                                    downloadImage(bp);

                                }
                            } else {
                                // continue with your code
                                downloadImage(bp);

                            }
                        }
                    }
                }
            });


            container.addView(view);

            return view;
        }


        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1303: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                    //Toast.makeText(getApplicationContext(), "Permission Granted!!", Toast.LENGTH_LONG).show();
                    if (bp != null) {
                        downloadImage(bp);

                    }

                } else {
                    Log.e("Permission", "Denied");
                    Toast.makeText(getContext(), "Permission Denied!!", Toast.LENGTH_LONG).show();

                }
                return;
            }
        }
    }

    private void downloadImage(Bitmap bp) {

        if (!imageRoot.exists()) {
            if (!imageRoot.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Artwork.appDirectoryName + " directory");
            }
        }
        //imageRoot.mkdir();

        OutputStream fOut = null;
        final File file = new File(imageRoot, "nooi_" + System.currentTimeMillis() + ".jpg");
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Toast.makeText(getContext(), "Error occured. Please try again later.",
                    Toast.LENGTH_SHORT).show();
        }

        bp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "nooi");
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));
        values.put("_data", file.getAbsolutePath());

        ContentResolver cr = getContext().getContentResolver();
        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Toast.makeText(getContext(), "Image downloaded successfully!!", Toast.LENGTH_SHORT).show();

    }


}
