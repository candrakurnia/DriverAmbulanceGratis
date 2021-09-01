package com.apps.driverambulancegratis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.apps.driverambulancegratis.model.Driver;
import com.apps.driverambulancegratis.model.LoginResponse;
import com.apps.driverambulancegratis.rest.Api;
import com.apps.driverambulancegratis.rest.ApiInterface;
import com.apps.driverambulancegratis.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    ProgressDialog progressDialog;
    private TextInputEditText nama, pass;
    Button login;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        nama = findViewById(R.id.edt_username);
        pass = findViewById(R.id.edt_password);

        apiInterface = Api.getClient().create(ApiInterface.class);
        login = findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nm = nama.getText().toString();
                String pw = pass.getText().toString();
                try {
                    if (nm.length() < 1) {
                        Toast.makeText(LoginActivity.this, "Fullname tidak boleh kosong", Toast.LENGTH_SHORT).show();

                    } else if (pw.length() < 1) {
                        Toast.makeText(LoginActivity.this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    } else {
                        submitLogin(nm, pw);
                    }
                } catch (Exception e) {
                    Log.e("Error", "error" + e.getLocalizedMessage());
                }
            }
        });

    }

    private void submitLogin(final String fullname, String password) {
        progressDialog = ProgressDialog.show(LoginActivity.this, null, "Harap tunggu..", true, false);
        apiInterface.loginrequest(fullname, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (!(response.body().isError())) {
                            Driver driver = response.body().getDriver();
                            sessionManager.saveUser(LoginActivity.this, driver);
                            sessionManager.storeLogin(true);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                            progressDialog.dismiss();
                        } else {
                            handlingError("Mohon periksa Kembali fullname dan sandi anda");
                        }
                    } else {
                        handlingError("Mohon isi kolom yang kosong");
                    }
                } else {
                    handlingError("gagal Login");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("error login", "error" + t.getLocalizedMessage());
                handlingError("tidak ada jaringan");
            }
        });
    }

    private void handlingError(String gagal_login) {
    Toast.makeText(LoginActivity.this, gagal_login,Toast.LENGTH_SHORT).show();
    progressDialog.dismiss();
    }
}