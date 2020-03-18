package com.example.blessing.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuildCustom {

    public API generateAPI() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5/blessing/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API service = retrofit.create(API.class);
        return service;
    }
}
