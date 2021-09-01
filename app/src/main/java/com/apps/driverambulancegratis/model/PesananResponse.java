package com.apps.driverambulancegratis.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PesananResponse {

    @SerializedName("value")
    String value;
    @SerializedName("result")
    List<User> user;

    public String getValue() {
        return value;
    }

    public List<User> getUser() {
        return user;
    }
}
