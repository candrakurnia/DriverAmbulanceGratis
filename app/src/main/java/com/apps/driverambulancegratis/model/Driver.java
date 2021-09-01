package com.apps.driverambulancegratis.model;

import com.google.gson.annotations.SerializedName;

public class Driver {

    @SerializedName("driver_id")
    private String driver_id;
    @SerializedName("fullname")
    private String fullname;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("no_telpon")
    private String no_telpon;
    @SerializedName("no_plat")
    private String no_plat;

    public Driver(String driver_id, String fullname, String latitude, String longitude, String no_telpon, String no_plat) {
        this.driver_id = driver_id;
        this.fullname = fullname;
        this.latitude = latitude;
        this.longitude = longitude;
        this.no_telpon = no_telpon;
        this.no_plat = no_plat;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNo_telpon() {
        return no_telpon;
    }

    public void setNo_telpon(String no_telpon) {
        this.no_telpon = no_telpon;
    }

    public String getNo_plat() {
        return no_plat;
    }

    public void setNo_plat(String no_plat) {
        this.no_plat = no_plat;
    }
}
