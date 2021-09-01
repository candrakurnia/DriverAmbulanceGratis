package com.apps.driverambulancegratis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.apps.driverambulancegratis.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HomeActivity extends AppCompatActivity {

    TextView nama;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);
        nama = findViewById(R.id.tv_nama);
//        nama.setText(sessionManager.getUser(this).getDriver_id());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Log.e("COBA", gson.toJson(sessionManager.getDriver(this)));
//        String name = sessionManager.getDriver(this).getFullname();
//        if (name != null) {
//            nama.setText(name);
//        }
    }
}