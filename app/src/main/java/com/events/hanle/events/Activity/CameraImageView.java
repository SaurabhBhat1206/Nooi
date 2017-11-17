package com.events.hanle.events.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.events.hanle.events.Constants.ConnectionDetector;
import com.events.hanle.events.Constants.MyPreferenceManager;
import com.events.hanle.events.Model.ImageUpload;
import com.events.hanle.events.R;
import com.events.hanle.events.app.MyApplication;
import com.events.hanle.events.helper.ApiClient;
import com.events.hanle.events.interf.ApiInterface;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hanle on 8/3/2017.
 */

public class CameraImageView extends AppCompatActivity {
    private String filePath = null;
    private String flag;
    private ImageView imgPreview;
    private Button imageupload;
    Bitmap bitmap;
    File file;
    String event_id;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        pDialog = new ProgressDialog(CameraImageView.this);

        imgPreview = (ImageView) findViewById(R.id.cmaeraimage);
        imageupload = (Button) findViewById(R.id.imageupload);
        // Receiving the data from previous activity
        Intent i = getIntent();

        // image or video path that is captured in previous activity
        filePath = i.getStringExtra("filePath");
        flag = i.getStringExtra("flag");

        if (flag != null && flag.equals("cameracapture") && filePath != null) {
            // Displaying the image or video on the screen
            file = new File(filePath);

            previewMedia();
        } else if (flag != null && flag.equals("gallery") && filePath != null) {

            String fp = getRealPathFromURIPath(Uri.parse(filePath), CameraImageView.this);
            if (fp != null) {
                file = new File(fp);
            }

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(filePath));
                imgPreview.setImageBitmap(bitmap);
                imageupload.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else {

            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }
        String s = getIntent().getStringExtra("classcheck");

        if (s != null) {
            if (s.equalsIgnoreCase("cancelledevent")) {
                event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCancelledEventID().getId();
            } else if (s.equalsIgnoreCase("completedevent")) {
                event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getCompletedEventId().getId();
            }
        } else {
            event_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getEventId().getEventId();

        }


        imageupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionDetector.isInternetAvailable(getApplicationContext())) {
                    uploadImagetotheserver();
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet connection!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    private void uploadImagetotheserver() {

        pDialog.setMessage("Uploading images please wait...");
        pDialog.show();

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("artwork", file.getName(), requestBody);
        RequestBody userId = RequestBody.create(MediaType.parse("multipart/form-data"), com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUserId().getId());
        RequestBody eventId = RequestBody.create(MediaType.parse("multipart/form-data"), event_id);

        ApiInterface getResponse = ApiClient.getApiclient().create(ApiInterface.class);
        Call<ImageUpload> call = getResponse.uploadImage(userId, eventId, fileToUpload);
        call.enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(Call<ImageUpload> call, Response<ImageUpload> response) {
                pDialog.hide();

                ImageUpload serverResponse = response.body();
                Log.e("Success", String.valueOf(response.body()));

                if (serverResponse != null) {
                    if (serverResponse.getSuccess() == 1) {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        MyApplication.getInstance().getPrefManager().addtabmovement("1");
                        Intent ii = new Intent(getApplicationContext(), NooiGallery.class);
                        startActivity(ii);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //assert serverResponse != null;
                   // Log.v("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Call<ImageUpload> call, Throwable t) {
                //Log.e("Error", t.getMessage());
                pDialog.hide();
                if (t.getMessage() != null) {
                    Toast.makeText(CameraImageView.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void previewMedia() {
        // Checking whether captured media is image or video

        // bimatp factory
        BitmapFactory.Options options = new BitmapFactory.Options();

        // down sizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = 8;

        final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        imgPreview.setImageBitmap(bitmap);
        imageupload.setVisibility(View.VISIBLE);
    }
}

