package com.events.hanle.events.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Activity.CameraImageView;
import com.events.hanle.events.Activity.ListOfEvent1;
import com.events.hanle.events.Activity.NooiGallery;
import com.events.hanle.events.BuildConfig;
import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.SpacesItemDecoration;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.Image;
import com.events.hanle.events.R;
import com.events.hanle.events.adapter.GalleryAdapter;
import com.events.hanle.events.app.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.support.v4.content.FileProvider.getUriForFile;

/**
 * Created by Hanle on 7/31/2017.
 */
public class Gallery extends Fragment implements View.OnClickListener {
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    protected static final int CAMERA_PIC_REQUEST = 0;
    private Uri fileUri;
    private static final String TAG = Gallery.class.getSimpleName();
    protected static final int PICK_GALLERY = 777;
    private TextView warningmessage;
    private String event_id;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._5sdp);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        pDialog = new ProgressDialog(getContext());
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getContext(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        warningmessage = (TextView) view.findViewById(R.id.warningtext);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        String s = getActivity().getIntent().getStringExtra("classcheck");

        if (s != null) {
            if (s.equalsIgnoreCase("cancelledevent")) {
                event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getId();
            } else if (s.equalsIgnoreCase("completedevent")) {
                event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getId();
            }
        } else {
            event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEventId();

        }


        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        if (ConnectionDetector.isInternetAvailable(getContext())) {
            fetchImages();
        } else {
            Toast.makeText(getContext(), "No Internet connection!!", Toast.LENGTH_SHORT).show();
        }
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (NooiGallery.viewPagerGallery != null && MyApplication.getInstance().getPrefManager().getTabmovedId() != null) {
            NooiGallery.viewPagerGallery.setCurrentItem(1);
            MyApplication.getInstance().getPrefManager().removetabmovement();
        }
    }


    private void fetchImages() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pDialog.setMessage("Downloading images");
                pDialog.show();

                String endpoint = WebUrl.GETGALLERYIMAGES.replace("EVENT_ID", event_id);

                StringRequest strReq = new StringRequest(endpoint, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "response: " + response);
                        pDialog.hide();
                        images.clear();

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("image_url");
                            int uploadlimit = jsonArray.length();
                            if (uploadlimit >= 15) {
                                fab.setVisibility(View.INVISIBLE);
                                fab1.setVisibility(View.INVISIBLE);
                                fab2.setVisibility(View.INVISIBLE);
                            }

                            if (jsonArray.length() <= 0) {
                                recyclerView.setVisibility(View.GONE);
                                warningmessage.setVisibility(View.VISIBLE);

                            } else {
                                for (int i = 0; i < uploadlimit; i++) {
                                    JSONObject jsonarrayobject = jsonArray.getJSONObject(i);
                                    Image image = new Image();
                                    image.setImageID(jsonarrayobject.getString("id"));
                                    image.setUploadername(jsonarrayobject.getString("uploader_name"));
                                    image.setUrl(jsonarrayobject.getString("url"));
                                    image.setTimestamp(jsonarrayobject.getString("timestamp"));
                                    images.add(image);

                                }
                            }


                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.hide();
                        NetworkResponse networkResponse = error.networkResponse;
                        Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                        //Toast.makeText(ListOfEvent1.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getContext(),
                                    getContext().getString(R.string.error_network_timeout),
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getContext(),
                                    getContext().getString(R.string.error_network_server),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Something went wrong!!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


                strReq.setRetryPolicy(new DefaultRetryPolicy(
                        WebUrl.MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //Adding request to request queue
                MyApplication.getInstance().addToRequestQueue(strReq);
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:

                animateFAB();
                break;
            case R.id.fab1:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.System.canWrite(getContext())) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 23676);
                    } else {
                        // continue with your code
                        chooseFromGallery();

                    }
                } else {
                    // continue with your code
                    chooseFromGallery();

                }
                break;
            case R.id.fab2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.System.canWrite(getContext())) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2365);
                    } else {
                        // continue with your code
                        captureImage();

                    }
                }  else {
                    // continue with your code
                    captureImage();

                }
                Log.d("Raj", "Fab 2");
                break;
        }
    }

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                 fileUri = FileProvider.getUriForFile(getContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }

    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2365: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                    //Toast.makeText(getApplicationContext(), "Permission Granted!!", Toast.LENGTH_LONG).show();
                    captureImage();

                } else {
                    Log.e("Permission", "Denied");
                    Toast.makeText(getContext(), "Permission Denied!!", Toast.LENGTH_LONG).show();

                }
                return;
            }

            case 23676: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                    //Toast.makeText(getApplicationContext(), "Permission Granted!!", Toast.LENGTH_LONG).show();
                    chooseFromGallery();

                } else {
                    Log.e("Permission", "Denied");
                    Toast.makeText(getContext(), "Permission Denied!!", Toast.LENGTH_LONG).show();

                }
                return;
            }
        }
    }

    private void chooseFromGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_GALLERY);
    }


    public void animateFAB() {

        if (isFabOpen) {

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Raj", "open");

        }
    }

    private boolean isDeviceSupportCamera() {
        if (getContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == RESULT_OK) {

                Intent i = new Intent(getContext(), CameraImageView.class);
                i.putExtra("filePath", mCurrentPhotoPath);
                i.putExtra("flag", "cameracapture");
                startActivity(i);
                getActivity().finish();

            } else if (requestCode == RESULT_CANCELED) {
                Toast.makeText(getContext(), "Pressed back!!!!", Toast.LENGTH_SHORT).show();

            }

        } else if (requestCode == PICK_GALLERY) {
            if (resultCode == RESULT_OK && data != null) {

                Uri path = data.getData();
                Intent i = new Intent(getContext(), CameraImageView.class);
                i.putExtra("flag", "gallery");
                i.putExtra("filePath", path.toString());
                startActivity(i);
                getActivity().finish();


            } else if (requestCode == RESULT_CANCELED) {
                Toast.makeText(getContext(), "Pressed back!!!!", Toast.LENGTH_SHORT).show();

            }
        }

    }
}
