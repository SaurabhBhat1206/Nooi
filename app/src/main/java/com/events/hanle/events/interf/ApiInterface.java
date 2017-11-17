package com.events.hanle.events.interf;

import com.events.hanle.events.Model.ImageUpload;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * Created by Hanle on 8/4/2017.
 */

public interface ApiInterface {

    @Multipart
    @POST("event/uploadImage")
    Call<ImageUpload> uploadImage(@Part("userId") RequestBody userId, @Part("eventId") RequestBody eventId, @Part MultipartBody.Part file);

    @GET("event/list_of_events")
    Call<ListEvent> getEvents(
            @QueryMap Map<String, String> options
    );

}
