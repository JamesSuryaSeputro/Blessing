package com.example.blessing.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TryoutModel {

    @Expose
    @SerializedName("datecreated")
    private String datecreated;
    @Expose
    @SerializedName("deskripsi")
    private String deskripsi;
    @Expose
    @SerializedName("judul")
    private String judul;
    @Expose
    @SerializedName("id_tryout")
    private String idTryout;

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIdTryout() {
        return idTryout;
    }

    public void setIdTryout(String idTryout) {
        this.idTryout = idTryout;
    }
}
