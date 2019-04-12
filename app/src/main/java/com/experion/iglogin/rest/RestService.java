package com.experion.iglogin.rest;

import android.provider.SyncStateContract;


import com.experion.iglogin.util.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Rest Service implementation class
 */

public class RestService extends Constants {

    private static final int MINUTES = 1;
    private static final int CONNECTION_TIMEOUT = 60000 * MINUTES;
    private static final int READ_TIMEOUT = 60000 * MINUTES;

    public static ApiInterface getApi(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(getClient())
                .build();
        return retrofit.create(ApiInterface.class);
    }

    private static OkHttpClient getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient().newBuilder()
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new CommonRequestInterceptor())
                .addInterceptor(logging)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS).retryOnConnectionFailure(true).build();
    }



}
