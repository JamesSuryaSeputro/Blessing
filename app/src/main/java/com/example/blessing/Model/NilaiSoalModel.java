package com.example.blessing.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NilaiSoalModel {

    @Expose
    @SerializedName("nama_soal")
    private String namaSoal;
    @Expose
    @SerializedName("datecreated")
    private String datecreated;
    @Expose
    @SerializedName("jumlah_soal")
    private String jumlahSoal;
    @Expose
    @SerializedName("nilai_soal")
    private String nilaiSoal;
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("id_soal")
    private String idSoal;
    @Expose
    @SerializedName("id_nilaisoal")
    private String idNilaisoal;

    public String getNamaSoal() {
        return namaSoal;
    }

    public void setNamaSoal(String namaSoal) {
        this.namaSoal = namaSoal;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getJumlahSoal() {
        return jumlahSoal;
    }

    public void setJumlahSoal(String jumlahSoal) {
        this.jumlahSoal = jumlahSoal;
    }

    public String getNilaiSoal() {
        return nilaiSoal;
    }

    public void setNilaiSoal(String nilaiSoal) {
        this.nilaiSoal = nilaiSoal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSoal() {
        return idSoal;
    }

    public void setIdSoal(String idSoal) {
        this.idSoal = idSoal;
    }

    public String getIdNilaisoal() {
        return idNilaisoal;
    }

    public void setIdNilaisoal(String idNilaisoal) {
        this.idNilaisoal = idNilaisoal;
    }
}
