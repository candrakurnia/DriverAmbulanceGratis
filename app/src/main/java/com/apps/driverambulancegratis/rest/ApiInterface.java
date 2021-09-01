package com.apps.driverambulancegratis.rest;

import com.apps.driverambulancegratis.model.LoginResponse;
import com.apps.driverambulancegratis.model.LokasiResponse;
import com.apps.driverambulancegratis.model.PesananResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("logindriver.php")
    Call<LoginResponse> loginrequest(@Field("fullname") String fullname,
                                     @Field("password") String password);

    @GET("get_pesanan.php")
    Call<PesananResponse> getpesanan();

    @FormUrlEncoded
    @POST("post_lokasidriver.php")
    Call<LokasiResponse> lokasiRequest(@Field("driver_id") String driver_id,
                                       @Field("latitude") Double latitude,
                                       @Field("longitude") Double longitude);
}
