package com.apps.driverambulancegratis;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.driverambulancegratis.adapter.RVAdapterPemesan;
import com.apps.driverambulancegratis.helper.FirebaseHelper;
import com.apps.driverambulancegratis.helper.GoogleMapHelper;
import com.apps.driverambulancegratis.helper.MarkerAnimationHelper;
import com.apps.driverambulancegratis.helper.UiHelper;
import com.apps.driverambulancegratis.interfaces.LatLngInterpolator;
import com.apps.driverambulancegratis.model.Driver;
import com.apps.driverambulancegratis.model.LokasiResponse;
import com.apps.driverambulancegratis.model.LokasiUser;
import com.apps.driverambulancegratis.model.PesananResponse;
import com.apps.driverambulancegratis.model.User;
import com.apps.driverambulancegratis.rest.Api;
import com.apps.driverambulancegratis.rest.ApiInterface;
import com.apps.driverambulancegratis.utils.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2161;
    private final GoogleMapHelper googleMapHelper = new GoogleMapHelper();
    private GoogleMap googleMap;
    private ApiInterface apiInterface;
    private List<User> users = new ArrayList<>();
    private RVAdapterPemesan rvAdapterPemesan;
    RecyclerView recyclerView;
    private FirebaseHelper firebaseHelper;
    private LocationRequest locationRequest;
    private UiHelper uiHelper;
    private Marker currentPositionMarker;
    Driver driver;
    ProgressDialog progressDialog;
    private FusedLocationProviderClient locationProviderClient;
    private boolean locationFlag = true;
    private AtomicBoolean driveronlineflag = new AtomicBoolean(false);
    private TextView nama, alamat, driverstatus;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        nama = findViewById(R.id.tv_user);
//        alamat = findViewById(R.id.tv_alamat);
        recyclerView = findViewById(R.id.rv_pemesan);
        apiInterface = Api.getClient().create(ApiInterface.class);
        rvAdapterPemesan = new RVAdapterPemesan(users,this);
        RecyclerView.LayoutManager mlayoutmanager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mlayoutmanager);
        recyclerView.setAdapter(rvAdapterPemesan);
        sessionManager = new SessionManager(this);
        loadpesanan();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gmaps);
        uiHelper = new UiHelper(this);
        assert mapFragment != null;
        mapFragment.getMapAsync(googleMap -> this.googleMap = googleMap);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = uiHelper.getLocationRequest();
        if (!uiHelper.isPlayServicesAvailable()) {
            Toast.makeText(this, "Play Services did not installed!", Toast.LENGTH_SHORT).show();
            finish();
        } else requestLocationUpdates();

        firebaseHelper = new FirebaseHelper(sessionManager.getDriver(this).getDriver_id());

        driverstatus = findViewById(R.id.driverStatusTextView);
        SwitchCompat switchCompat = findViewById(R.id.switchstatus);
        switchCompat.setOnCheckedChangeListener(((buttonView, b) -> {
            driveronlineflag.set(b);
            if (b) {
                driverstatus.setText(getResources().getString(R.string.online));
//                Call<LokasiResponse> call = apiInterface.lokasiRequest(sessionManager.getDriver(MainActivity.this), driver.getLatitude(), driver.getLongitude());
//                call.enqueue(new Callback<LokasiResponse>() {
//                    @Override
//                    public void onResponse(Call<LokasiResponse> call, Response<LokasiResponse> response) {
//                        if (response.isSuccessful()) {
//                            Toast.makeText(MainActivity.this,"Sudah Online",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<LokasiResponse> call, Throwable t) {
//                        Log.e("ERROR", t.getLocalizedMessage());
//
//                    }
//                });
            } else {
                driverstatus.setText(getResources().getString(R.string.offline));
                firebaseHelper.deleteUser();
            }
        }));
//        mapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                googleMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(0, 0))
//                        .title("marker"));
//            }
//        });
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location location = locationResult.getLastLocation();
            if (location == null) return;
            if (locationFlag) {
                locationFlag = false;
                animateCamera(location);
            }
            if (driveronlineflag.get()) {
                Call<LokasiResponse> lokasiResponseCall = apiInterface.lokasiRequest(sessionManager.getDriver(MainActivity.this).getDriver_id(),location.getLatitude(),location.getLongitude());
                lokasiResponseCall.enqueue(new Callback<LokasiResponse>() {
                    @Override
                    public void onResponse(Call<LokasiResponse> call, Response<LokasiResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
//                                Toast.makeText(MainActivity.this, "Terkirim", Toast.LENGTH_SHORT).show();
                                Log.e("200", "SUKSES");
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<LokasiResponse> call, Throwable t) {

                    }
                });
                firebaseHelper.updateUser(new LokasiUser(sessionManager.getDriver(MainActivity.this).getDriver_id(), location.getLatitude(), location.getLongitude()));
                showOrAnimateMarker(location);
            }


        }

    };

    private void animateCamera(Location location) {
        CameraUpdate cameraUpdate = googleMapHelper.buildCameraUpdate(new LatLng(location.getLatitude(), location.getLongitude()));
        googleMap.animateCamera(cameraUpdate);
    }

    private void showOrAnimateMarker(Location location) {
        if (currentPositionMarker == null)
            currentPositionMarker = googleMap.addMarker(googleMapHelper.getDriverMarkerOptions(location));
        else
            MarkerAnimationHelper.animateMarkerToGB(
                    currentPositionMarker,
                    new LatLng(location.getLatitude(),
                            location.getLongitude()),
                    new LatLngInterpolator.Spherical());
    }


    private void loadpesanan() {

        Call<PesananResponse> call = apiInterface.getpesanan();
        call.enqueue(new Callback<PesananResponse>() {
            @Override
            public void onResponse(Call<PesananResponse> call, Response<PesananResponse> response) {
                progressDialog = ProgressDialog.show(MainActivity.this, null, "Sabar Yahh...", true, false);
                String value = response.body().getValue();
//                String address = "Tidak dapat menemukan lokasi";
                if (value.equals("3")) {
                    users = response.body().getUser();
                    rvAdapterPemesan = new RVAdapterPemesan(users,MainActivity.this);
                    recyclerView.setAdapter(rvAdapterPemesan);
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<PesananResponse> call, Throwable t) {
                Log.e("ERROR", t.getLocalizedMessage());
                progressDialog.dismiss();
            }
        });
    }


    private void handlingError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        if (!uiHelper.isHaveLocationPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        if (uiHelper.isLocationProviderEnabled())
            uiHelper.showPositiveDialogWithListener(this, getResources().getString(R.string.need_location),
                    getResources().getString(R.string.location_content), () ->
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)), "Turn on", false);
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            int value = grantResults[0];
            if (value == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Location Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            } else if (value == PackageManager.PERMISSION_GRANTED) requestLocationUpdates();
        }
    }

}