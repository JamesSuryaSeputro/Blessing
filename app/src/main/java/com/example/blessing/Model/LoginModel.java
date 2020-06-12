package com.example.blessing.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @Expose
    @SerializedName("nama_role")
    private String namaRole;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("nama")
    private String nama;
    @Expose
    @SerializedName("roleid")
    private String roleid;
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("status")
    private String status;

    public String getNamaRole() {
        return namaRole;
    }

    public void setNamaRole(String namaRole) {
        this.namaRole = namaRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LoginModel{" +
                ", nama='" + namaRole + '\'' +
                "email='" + email + '\'' +
                ", nama='" + nama + '\'' +
                ", idRole='" + roleid + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
