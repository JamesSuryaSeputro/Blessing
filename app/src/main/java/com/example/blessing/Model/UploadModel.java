package com.example.blessing.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadModel {

    @Expose
    @SerializedName("file")
    private String file;
    @Expose
    @SerializedName("materi")
    private String materi;
    @Expose
    @SerializedName("status")
    private int status;

    public String getFile() {
        return file;
    }

    public void setFile(String  file) {
        this.file = file;
    }

    public String getMateri() {
        return materi;
    }

    public void setMateri(String materi) {
        this.materi = materi;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
