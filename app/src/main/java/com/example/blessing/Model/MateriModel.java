package com.example.blessing.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MateriModel {

    @Expose
    @SerializedName("nama_materi")
    private String namaMateri;
    @Expose
    @SerializedName("judul_materi")
    private String judulMateri;
    @Expose
    @SerializedName("id_mapel")
    private String idMapel;
    @Expose
    @SerializedName("id_materi")
    private String idMateri;

    public String getNamaMateri() {
        return namaMateri;
    }

    public void setNamaMateri(String namaMateri) {
        this.namaMateri = namaMateri;
    }

    public String getJudulMateri() {
        return judulMateri;
    }

    public void setJudulMateri(String judulMateri) {
        this.judulMateri = judulMateri;
    }

    public String getIdMapel() {
        return idMapel;
    }

    public void setIdMapel(String idMapel) {
        this.idMapel = idMapel;
    }

    public String getIdMateri() {
        return idMateri;
    }

    public void setIdMateri(String idMateri) {
        this.idMateri = idMateri;
    }
}
