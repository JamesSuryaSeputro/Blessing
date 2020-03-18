package com.example.blessing.Service;

import com.example.blessing.Model.LoginModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface API {
        @POST("loginsiswa.php")
        @FormUrlEncoded
        Call<LoginModel> checklogin(@Field("email") String email, @Field("password") String password);

        @POST("daftarsiswa.php")
        @FormUrlEncoded
        Call<LoginModel> checkregister(@Field("nama") String nama,
                                       @Field("email") String email,
                                       @Field("password") String password,
                                       @Field("confirmpass") String confirmpassword);

        @POST("routes.php")
        @FormUrlEncoded
        Call<LoginModel> checkmapel(@Field("nama_mapel") String nama_mapel);

        @Streaming
        @GET
        Call<ResponseBody> downloadFileByUrl(@Url String url);
}
