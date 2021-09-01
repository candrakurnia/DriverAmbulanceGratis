package com.apps.driverambulancegratis.model;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id_loc_user")
    private String id_loc_user;
    @SerializedName("fullname")
    private String fullname;
    @SerializedName("no_ktp")
    private String no_ktp;
    @SerializedName("no_telpon")
    private String no_telpon;
    @SerializedName("alamat")
    private String alamat;

    public User(String id_loc_user, String fullname, String no_ktp, String no_telpon, String alamat) {
        this.id_loc_user = id_loc_user;
        this.fullname = fullname;
        this.no_ktp = no_ktp;
        this.no_telpon = no_telpon;
        this.alamat = alamat;
    }

    public String getId_loc_user() {
        return id_loc_user;
    }

    public void setId_loc_user(String id_loc_user) {
        this.id_loc_user = id_loc_user;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNo_ktp() {
        return no_ktp;
    }

    public void setNo_ktp(String no_ktp) {
        this.no_ktp = no_ktp;
    }

    public String getNo_telpon() {
        return no_telpon;
    }

    public void setNo_telpon(String no_telpon) {
        this.no_telpon = no_telpon;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
