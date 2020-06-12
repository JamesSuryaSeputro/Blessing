package com.example.blessing.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SoalModel {

    @Expose
    @SerializedName("nama_jenjang")
    private String namaJenjang;
    @Expose
    @SerializedName("kelas")
    private String kelas;
    @Expose
    @SerializedName("datecreated")
    private String datecreated;
    @Expose
    @SerializedName("nama_soal")
    private String namaSoal;
    @Expose
    @SerializedName("id_kelas")
    private String idKelas;
    @Expose
    @SerializedName("id_mapelsoal")
    private String idMapelsoal;
    @Expose
    @SerializedName("id_jenjang")
    private String idJenjang;
    @Expose
    @SerializedName("id_soal")
    private String idSoal;

    public String getNamaJenjang() {
        return namaJenjang;
    }

    public void setNamaJenjang(String namaJenjang) {
        this.namaJenjang = namaJenjang;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getNamaSoal() {
        return namaSoal;
    }

    public void setNamaSoal(String namaSoal) {
        this.namaSoal = namaSoal;
    }

    public String getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(String idKelas) {
        this.idKelas = idKelas;
    }

    public String getIdMapelsoal() {
        return idMapelsoal;
    }

    public void setIdMapelsoal(String idMapelsoal) {
        this.idMapelsoal = idMapelsoal;
    }

    public String getIdJenjang() {
        return idJenjang;
    }

    public void setIdJenjang(String idJenjang) {
        this.idJenjang = idJenjang;
    }

    public String getIdSoal() {
        return idSoal;
    }

    public void setIdSoal(String idSoal) {
        this.idSoal = idSoal;
    }
}
