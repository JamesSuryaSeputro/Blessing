package com.example.blessing.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KuisModel {

    @Expose
    @SerializedName("id_soal")
    private String idSoal;
    @Expose
    @SerializedName("datecreated")
    private String datecreated;
    @Expose
    @SerializedName("img_jawaban")
    private String imgJawaban;
    @Expose
    @SerializedName("jawaban")
    private String jawaban;
    @Expose
    @SerializedName("img_pertanyaan")
    private String imgPertanyaan;
    @Expose
    @SerializedName("id_kuis")
    private String idKuis;
    @Expose
    @SerializedName("id_detailkuis")
    private String idDetailkuis;

    private String JawabanUser;

    public String getJawabanUser() {
        return JawabanUser;
    }

    public void setJawabanUser(String jawabanUser) {
        JawabanUser = jawabanUser;
    }

    public String getIdSoal() {
        return idSoal;
    }

    public void setIdSoal(String idSoal) {
        this.idSoal = idSoal;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getImgJawaban() {
        return imgJawaban;
    }

    public void setImgJawaban(String imgJawaban) {
        this.imgJawaban = imgJawaban;
    }

    public String getJawaban() {
        return jawaban;
    }

    public void setJawaban(String jawaban) {
        this.jawaban = jawaban;
    }

    public String getImgPertanyaan() {
        return imgPertanyaan;
    }

    public void setImgPertanyaan(String imgPertanyaan) {
        this.imgPertanyaan = imgPertanyaan;
    }

    public String getIdKuis() {
        return idKuis;
    }

    public void setIdKuis(String idKuis) {
        this.idKuis = idKuis;
    }

    public String getIdDetailkuis() {
        return idDetailkuis;
    }

    public void setIdDetailkuis(String idDetailkuis) {
        this.idDetailkuis = idDetailkuis;
    }
}
