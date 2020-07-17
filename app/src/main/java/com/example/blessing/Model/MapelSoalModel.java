package com.example.blessing.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MapelSoalModel {

    @Expose
    @SerializedName("nama_jenjang")
    private String namaJenjang;
    @Expose
    @SerializedName("nama_mapelsoal")
    private String namaMapelsoal;
    @Expose
    @SerializedName("id_jenjang")
    private String idJenjang;
    @Expose
    @SerializedName("id_mapelsoal")
    private String idMapelsoal;

    public String getNamaJenjang() {
        return namaJenjang;
    }

    public void setNamaJenjang(String namaJenjang) {
        this.namaJenjang = namaJenjang;
    }

    public String getNamaMapelsoal() {
        return namaMapelsoal;
    }

    public void setNamaMapelsoal(String namaMapelsoal) {
        this.namaMapelsoal = namaMapelsoal;
    }

    public String getIdJenjang() {
        return idJenjang;
    }

    public void setIdJenjang(String idJenjang) {
        this.idJenjang = idJenjang;
    }

    public String getIdMapelsoal() {
        return idMapelsoal;
    }

    public void setIdMapelsoal(String idMapelsoal) {
        this.idMapelsoal = idMapelsoal;
    }
}
