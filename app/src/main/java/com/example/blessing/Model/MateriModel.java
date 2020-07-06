package com.example.blessing.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MateriModel {

    @Expose
    @SerializedName("kelas")
    private String kelas;
    @Expose
    @SerializedName("id_jenjang")
    private String idJenjang;
    @Expose
    @SerializedName("datecreated")
    private String datecreated;
    @Expose
    @SerializedName("nama_materi")
    private String namaMateri;
    @Expose
    @SerializedName("judul_materi")
    private String judulMateri;
    @Expose
    @SerializedName("id_kelas")
    private String idKelas;
    @Expose
    @SerializedName("id_mapel")
    private String idMapel;
    @Expose
    @SerializedName("id_materi")
    private String idMateri;

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getIdJenjang() {
        return idJenjang;
    }

    public void setIdJenjang(String idJenjang) {
        this.idJenjang = idJenjang;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

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

    public String getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(String idKelas) {
        this.idKelas = idKelas;
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
