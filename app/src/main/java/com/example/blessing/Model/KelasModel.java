package com.example.blessing.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KelasModel {

    @Expose
    @SerializedName("kelas")
    private String kelas;
    @Expose
    @SerializedName("id_jenjang")
    private String idJenjang;
    @Expose
    @SerializedName("id_kelas")
    private String idKelas;

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

    public String getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(String idKelas) {
        this.idKelas = idKelas;
    }
}
