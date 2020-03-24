package com.example.blessing.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterModel {

  @Expose
  @SerializedName("email")
  private String email;
  @Expose
  @SerializedName("password")
  private String password;
  @Expose
  @SerializedName("nama")
  private String nama;
  @Expose
  @SerializedName("id")
  private String id;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getNama() {
    return nama;
  }

  public void setNama(String nama) {
    this.nama = nama;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
