package com.ahmadabuhasan.pointofsale;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.auth.api.credentials.CredentialsApi;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    public static int splashTimeOut = CredentialsApi.CREDENTIAL_PICKER_REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();

        new Handler().postDelayed(() -> {
            SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, DashboardActivity.class));
            SplashScreenActivity.this.finish();
        }, splashTimeOut);
    }
}