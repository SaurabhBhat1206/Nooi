package com.events.hanle.events.helper;

import com.events.hanle.events.Constants.WebUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hanle on 8/4/2017.
 */

public class ApiClient {

    private static Retrofit retroft;

    public static Retrofit getApiclient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retroft == null) {
            retroft = new Retrofit.Builder().baseUrl(WebUrl.ORGANISER_URL).addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retroft;
    }


}
