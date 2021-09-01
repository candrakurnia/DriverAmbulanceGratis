package com.apps.driverambulancegratis.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("error")
    private boolean error;
    @SerializedName("user")
    private Driver driver;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
