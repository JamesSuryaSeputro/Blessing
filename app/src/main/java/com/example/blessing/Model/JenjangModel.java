package com.example.blessing.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

<<<<<<< HEAD
=======
import org.jetbrains.annotations.NotNull;

>>>>>>> 73837329fd07894514e9194bf88f89881d1a8dff
public class JenjangModel {

    @Expose
    @SerializedName("nama_jenjang")
    private String namaJenjang;
    @Expose
    @SerializedName("id_jenjang")
    private String idJenjang;

    public String getNamaJenjang() {
        return namaJenjang;
    }

    public void setNamaJenjang(String namaJenjang) {
        this.namaJenjang = namaJenjang;
    }

    public String getIdJenjang() {
        return idJenjang;
    }

    public void setIdJenjang(String idJenjang) {
        this.idJenjang = idJenjang;
    }

}
