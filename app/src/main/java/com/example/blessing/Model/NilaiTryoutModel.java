package com.example.blessing.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NilaiTryoutModel {

    @Expose
    @SerializedName("judul")
    private String judul;
    @Expose
    @SerializedName("datecreated")
    private String datecreated;
    @Expose
    @SerializedName("jumlah_tryout")
    private String jumlahTryout;
    @Expose
    @SerializedName("nilai_tryout")
    private String nilaiTryout;
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("id_tryout")
    private String idTryout;
    @Expose
    @SerializedName("id_nilaitryout")
    private String idNilaitryout;

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getJumlahTryout() {
        return jumlahTryout;
    }

    public void setJumlahTryout(String jumlahTryout) {
        this.jumlahTryout = jumlahTryout;
    }

    public String getNilaiTryout() {
        return nilaiTryout;
    }

    public void setNilaiTryout(String nilaiTryout) {
        this.nilaiTryout = nilaiTryout;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdTryout() {
        return idTryout;
    }

    public void setIdTryout(String idTryout) {
        this.idTryout = idTryout;
    }

    public String getIdNilaitryout() {
        return idNilaitryout;
    }

    public void setIdNilaitryout(String idNilaitryout) {
        this.idNilaitryout = idNilaitryout;
    }
}
