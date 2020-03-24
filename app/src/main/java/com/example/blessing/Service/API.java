package com.example.blessing.Service;

import com.example.blessing.Model.LoginModel;
import com.example.blessing.Model.MapelModel;
import com.example.blessing.Model.RegisterModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface API {
        @POST("api_post_loginsiswa")
        @FormUrlEncoded
        Call<LoginModel> checklogin(@Field("email") String email, @Field("password") String password);

        @POST("api_post_daftarsiswa")
        @FormUrlEncoded
        Call<RegisterModel> checkregister(@Field("nama") String nama,
                                          @Field("email") String email,
                                          @Field("password") String password,
                                          @Field("confirmpass") String confirmpassword);

        @POST("api_post_mapel")
        @FormUrlEncoded
        Call<MapelModel> postdatamapel(@Field("nama_mapel") String namaMapel);

        @Streaming
        @GET
        Call<ResponseBody> downloadFileByUrl(@Url String url);

        @GET("api_get_mapel")
        Call<List<MapelModel>> getdatamapel();



}
