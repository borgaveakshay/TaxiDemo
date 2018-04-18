package com.mytaxidemo.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.mytaxidemo.network.networkinterfaces.APIServices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit mRetrofit;
    private static final String BASE_URL = "https://fake-poi-api.mytaxi.com";

    private RetrofitClient() {
        // making constructor private for not able to create instance.
    }

    public static Retrofit getInstance() {

        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return mRetrofit;

    }

    public static APIServices getAPIService() {

        return getInstance().create(APIServices.class);
    }


}
