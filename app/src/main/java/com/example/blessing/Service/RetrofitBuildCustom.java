package com.example.blessing.Service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuildCustom {

   public static final String BASE_URL = "https://krisjaya-2020.et.r.appspot.com/";
   private static RetrofitBuildCustom instance = null;
   private static Retrofit retrofit;
   private API service;
   //https://blessingme.herokuapp.com/
    //http://192.168.1.7/blessingAPIremote/public/
   //http://192.168.1.7/blessing/blessingAPI/public/

    public RetrofitBuildCustom() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getInterceptor())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(API.class);
    }

    private static OkHttpClient getInterceptor(){
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
//        builder.readTimeout(10, TimeUnit.SECONDS);
//        builder.connectTimeout(5, TimeUnit.SECONDS);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        builder.addInterceptor(interceptor);
        return builder.build();
    }

    public static RetrofitBuildCustom getInstance(){
        if (instance==null){
            synchronized (RetrofitBuildCustom.class){
                instance =  new RetrofitBuildCustom();
            }
        }
        return instance;
    }

  public static Retrofit getRetrofit() {
    return retrofit;
  }

  public API getService() {
    return service;
  }

}
