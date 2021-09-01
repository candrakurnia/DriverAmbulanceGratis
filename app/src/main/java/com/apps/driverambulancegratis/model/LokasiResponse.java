package com.apps.driverambulancegratis.model;

import com.google.gson.annotations.SerializedName;

public class LokasiResponse {

    @SerializedName("error")
    private boolean error;
    @SerializedName("user")
    private Driver driver;

    public boolean isError() {
        return error;
    }

    public Driver getDriver() {
        return driver;
    }
}
