package com.example.blessing.Service;

import com.example.blessing.Model.DetailTryoutModel;
import com.example.blessing.Model.ImageModel;
import com.example.blessing.Model.JenjangModel;
import com.example.blessing.Model.KelasModel;
import com.example.blessing.Model.KuisModel;
import com.example.blessing.Model.LoginModel;
import com.example.blessing.Model.MapelModel;
import com.example.blessing.Model.MapelSoalModel;
import com.example.blessing.Model.MateriModel;
import com.example.blessing.Model.NilaiSoalModel;
import com.example.blessing.Model.NilaiTryoutModel;
import com.example.blessing.Model.RegisterModel;
import com.example.blessing.Model.SoalModel;
import com.example.blessing.Model.TryoutModel;
import com.example.blessing.Model.UploadModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface API {

    @POST("api_post_loginuser")
    @FormUrlEncoded
    Call<LoginModel> checklogin(@Field("email") String email, @Field("password") String password);

    @POST("api_post_user")
    @FormUrlEncoded
    Call<RegisterModel> checkregister(@Field("id_role") String idrole,
                                      @Field("nama") String nama,
                                      @Field("email") String email,
                                      @Field("password") String password,
                                      @Field("confirmpass") String confirmpassword);

    @PUT("api_update_user/{id}")
    Call<LoginModel> updatedatauser(@Path("id") String id,
                                    @Body LoginModel loginModel);


    @GET("api_get_mapel")
    Call<List<MapelModel>> getdatamapel();

    @POST("api_post_mapel")
    @FormUrlEncoded
    Call<MapelModel> postdatamapel(@Field("nama_mapel") String namamapel);

    @DELETE("api_delete_mapel/{id}")
    Call<MapelModel> deletedatamapel(@Path("id") String id);

    @PUT("api_update_mapel/{id}")
    Call<MapelModel> updatedatamapel(@Path("id") String id,
                                     @Body MapelModel mapelModel);

    @GET("api_get_materi/{id}")
    Call<List<MateriModel>> getmateribymapel(@Path("id") String idmapel);

    @Multipart
    @POST("api_post_materi")
    Call<UploadModel> uploadmateri(@Part MultipartBody.Part file,
                                   @Part("judul_materi") RequestBody judulmateri,
                                   @Part("id_mapel") RequestBody idmapel,
                                   @Part("id_kelas") RequestBody idkelas);

    @Multipart
    @POST("api_update_materi/{id}")
    Call<UploadModel> updatedatamateri(@Path("id") String id,
                                       @Part MultipartBody.Part file,
                                       @Part("judul_materi") RequestBody judulmateri,
                                       @Part("id_kelas") RequestBody idkelas);

    @DELETE("api_delete_materi/{id}")
    Call<MateriModel> deletedatamateri(@Path("id") String id);

    @GET("api_get_jenjang")
    Call<List<JenjangModel>> getdatajenjang();

    @GET("api_get_mapelsoal")
    Call<List<MapelSoalModel>> getdatamapelsoal();

    @POST("api_post_mapelsoal")
    @FormUrlEncoded
    Call<MapelSoalModel> postdatamapelsoal(@Field("id_jenjang") String idjenjang,
                                           @Field("nama_mapelsoal") String namamapelsoal);

    @PUT("api_update_mapelsoal/{id}")
    Call<MapelSoalModel> updatedatamapelsoal(@Path("id") String id,
                                             @Body MapelSoalModel mapelSoalModel);

    @DELETE("api_delete_mapelsoal/{id}")
    Call<MapelSoalModel> deletedatamapelsoal(@Path("id") String id);

    @GET("api_get_kelas")
    Call<List<KelasModel>> getdatakelas();

    @GET("api_get_kelas/{id}")
    Call<List<KelasModel>> getdatakelasbyidjenjang(@Path("id") String id);

    @GET("api_get_soal")
    Call<List<SoalModel>> getdatasoal();

    @GET("api_get_soal/{id}")
    Call<List<SoalModel>> getsoalbymapel(@Path("id") String idmapelsoal);

    @POST("api_post_soal")
    @FormUrlEncoded
    Call<SoalModel> postdatasoal(@Field("id_jenjang") String idjenjang,
                                 @Field("id_mapelsoal") String idmapelsoal,
                                 @Field("id_kelas") String idkelas,
                                 @Field("nama_soal") String namasoal);

    @PUT("api_update_soal/{id}")
    @FormUrlEncoded
    Call<SoalModel> updatedatasoal(@Path("id") String id,
                                   @Field("id_kelas") String id_kelas,
                                   @Field("nama_soal") String namasoal);

    @DELETE("api_delete_soal/{id}")
    Call<SoalModel> deletedatasoal(@Path("id") String id);

    @POST("api_post_kuis")
    @FormUrlEncoded
    Call<KuisModel> postdatakuis(@Field("id_soal") String idsoal);

    @DELETE("api_delete_kuis/{id}")
    Call<KuisModel> deletedatakuis(@Path("id") String id);

    @GET("api_get_detailkuis")
    Call<List<KuisModel>> getdetailkuis();

    @GET("api_get_detailkuisbysoal/{id}")
    Call<List<KuisModel>> getdetailkuisbysoal(@Path("id") String idsoal);

    @GET("api_get_detailkuisbysoal/{id}")
    Call<List<KuisModel>> getdetailkuisbyidkuis(@Path("id") String idkuis);

    @Multipart
    @POST("api_post_detailkuis")
    Call<ImageModel> postdatadetailkuis(@Part MultipartBody.Part file,
                                        @Part("jawaban") RequestBody jawaban,
                                        @Part("id_kuis") RequestBody idkuis);

    @Multipart
    @POST("api_update_detailkuis/{id}")
    Call<ImageModel> updatedatadetailkuis(@Path("id") String id,
                                          @Part MultipartBody.Part file,
                                          @Part("jawaban") RequestBody jawaban);

    @GET("api_get_tryout")
    Call<List<TryoutModel>> getdatatryout();

    @POST("api_post_tryout")
    @FormUrlEncoded
    Call<TryoutModel> postdatatryout(@Field("judul") String judul,
                                     @Field("deskripsi") String deskripsi,
                                     @Field("timer") String timer,
                                     @Field("id_jenjang") String idjenjang);

    @PUT("api_update_tryout/{id}")
    @FormUrlEncoded
    Call<TryoutModel> updatedatatryout(@Path("id") String id,
                                       @Field("judul") String deskripsi,
                                       @Field("deskripsi") String namasoal,
                                       @Field("timer") String timer,
                                       @Field("id_jenjang") String idjenjang);

    @DELETE("api_delete_tryout/{id}")
    Call<TryoutModel> deletedatatryout(@Path("id") String id);

    @GET("api_get_detailtryout/{id}")
    Call<List<DetailTryoutModel>> getdetailtryout(@Path("id") String id);

    @Multipart
    @POST("api_post_detailtryout")
    Call<ImageModel> postdatadetailtryout(@Part MultipartBody.Part file,
                                          @Part("jawaban_to") RequestBody jawaban,
                                          @Part("id_tryout") RequestBody idtryout);

    @Multipart
    @POST("api_update_detailtryout/{id}")
    Call<ImageModel> updatedatadetailtryout(@Path("id") String id,
                                            @Part MultipartBody.Part file,
                                            @Part("jawaban_to") RequestBody jawaban);

    @DELETE("api_delete_detailtryout/{id}")
    Call<DetailTryoutModel> deletedatadetailtryout(@Path("id") String id);

    @GET("api_get_nilaisoal/{id}")
    Call<List<NilaiSoalModel>> getnilaisoal(@Path("id") String id);

    @POST("api_post_nilaisoal")
    @FormUrlEncoded
    Call<NilaiSoalModel> postdatanilaisoal(@Field("id_soal") String idsoal,
                                           @Field("id") String id,
                                           @Field("nilai_soal") String nilaisoal,
                                           @Field("jumlah_soal") String jumlahsoal);

    @POST("api_update_nilaisoal/{id}")
    @FormUrlEncoded
    Call<NilaiSoalModel> updatedatanilaisoal(@Path("id") String id,
                                             @Field("nilai_soal") String nilaisoal,
                                             @Field("jumlah_soal") String jumlahsoal);

    @GET("api_get_nilaitryout/{id}")
    Call<List<NilaiTryoutModel>> getnilaitryout(@Path("id") String id);


    @POST("api_post_nilaitryout")
    @FormUrlEncoded
    Call<NilaiTryoutModel> postdatanilaitryout(@Field("id_tryout") String idtryout,
                                               @Field("id") String id,
                                               @Field("nilai_tryout") String nilaitryout,
                                               @Field("jumlah_tryout") String jumlahtryout);

    @POST("api_update_nilaitryout/{id}")
    @FormUrlEncoded
    Call<NilaiTryoutModel> updatedatanilaitryout(@Path("id") String id,
                                                 @Field("nilai_tryout") String nilaitryout,
                                                 @Field("jumlah_tryout") String jumlahtryout);
}