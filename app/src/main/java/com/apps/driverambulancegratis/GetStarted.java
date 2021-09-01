package com.apps.driverambulancegratis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apps.driverambulancegratis.utils.SessionManager;

public class GetStarted extends AppCompatActivity {

    TextView judul;
    Button mulai;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        sessionManager = new SessionManager(GetStarted.this);

        judul = findViewById(R.id.tv_judul);
        mulai = findViewById(R.id.btn_mulai);
        mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent q = new Intent(GetStarted.this,LoginActivity.class);
                startActivity(q);
                finish();
            }
        });
    }
}