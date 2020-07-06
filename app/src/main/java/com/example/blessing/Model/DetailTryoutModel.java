package com.example.blessing.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailTryoutModel {

    @Expose
    @SerializedName("datecreated")
    private String datecreated;
    @Expose
    @SerializedName("jawaban_to")
    private String jawabanTo;
    @Expose
    @SerializedName("img_to")
    private String imgTo;
    @Expose
    @SerializedName("id_tryout")
    private String idTryout;
    @Expose
    @SerializedName("id_detailtryout")
    private String idDetailtryout;

    private String userAnswer;

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String Answer) {
        userAnswer = Answer;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getJawabanTo() {
        return jawabanTo;
    }

    public void setJawabanTo(String jawabanTo) {
        this.jawabanTo = jawabanTo;
    }

    public String getImgTo() {
        return imgTo;
    }

    public void setImgTo(String imgTo) {
        this.imgTo = imgTo;
    }

    public String getIdTryout() {
        return idTryout;
    }

    public void setIdTryout(String idTryout) {
        this.idTryout = idTryout;
    }

    public String getIdDetailtryout() {
        return idDetailtryout;
    }

    public void setIdDetailtryout(String idDetailtryout) {
        this.idDetailtryout = idDetailtryout;
    }
}
