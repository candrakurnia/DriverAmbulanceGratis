package com.apps.driverambulancegratis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.apps.driverambulancegratis.utils.SessionManager;

public class SplashScreen extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        sessionManager = new SessionManager(this);

        final Boolean isLogin = sessionManager.getStatusLogin(this);
        Log.e("IS LOGIN", isLogin.toString());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin) {
                    SplashScreen.this.startActivity(new Intent(SplashScreen.this, MainActivity.class));

                } else {
                    SplashScreen.this.startActivity(new Intent(SplashScreen.this, GetStarted.class));
                }
                SplashScreen.this.finish();
            }
        }, 2000);
    }
}