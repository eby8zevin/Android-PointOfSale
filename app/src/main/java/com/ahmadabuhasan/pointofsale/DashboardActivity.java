package com.ahmadabuhasan.pointofsale;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ahmadabuhasan.pointofsale.customers.CustomersActivity;
import com.ahmadabuhasan.pointofsale.databinding.ActivityDashboardBinding;
import com.ahmadabuhasan.pointofsale.expense.ExpenseActivity;
import com.ahmadabuhasan.pointofsale.pos.PosActivity;
import com.ahmadabuhasan.pointofsale.product.ProductActivity;
import com.ahmadabuhasan.pointofsale.report.ReportActivity;
import com.ahmadabuhasan.pointofsale.settings.SettingsActivity;
import com.ahmadabuhasan.pointofsale.suppliers.SuppliersActivity;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;
import com.ahmadabuhasan.pointofsale.utils.LocaleManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class DashboardActivity extends BaseActivity {

    private ActivityDashboardBinding binding;
    private static long backPressed;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().setElevation(0.0f);

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermission();
        }

        MobileAds.initialize(this, initializationStatus -> {

        });
        this.binding.adView.loadAd(new AdRequest.Builder().build());

        this.binding.cardCustomers.setOnClickListener(view -> this.startActivity(new Intent(DashboardActivity.this, CustomersActivity.class)));
        this.binding.cardSuppliers.setOnClickListener(view -> this.startActivity(new Intent(DashboardActivity.this, SuppliersActivity.class)));
        this.binding.cardProducts.setOnClickListener(view -> this.startActivity(new Intent(DashboardActivity.this, ProductActivity.class)));
        this.binding.cardPos.setOnClickListener(view -> this.startActivity(new Intent(DashboardActivity.this, PosActivity.class)));
        this.binding.cardExpense.setOnClickListener(view -> this.startActivity(new Intent(DashboardActivity.this, ExpenseActivity.class)));
        this.binding.cardReport.setOnClickListener(view -> this.startActivity(new Intent(DashboardActivity.this, ReportActivity.class)));
        this.binding.cardSettings.setOnClickListener(view -> this.startActivity(new Intent(DashboardActivity.this, SettingsActivity.class)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.language_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.local_english:
                setNewLocale(this, LocaleManager.ENGLISH);
                Toast.makeText(getApplicationContext(), "English", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.local_indonesian:
                setNewLocale(this, LocaleManager.INDONESIAN);
                Toast.makeText(getApplicationContext(), "Indonesian", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // https://androidwave.com/android-multi-language-support-best-practices/
    private void setNewLocale(AppCompatActivity appCompatActivity, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(this, language);
        Intent intent = appCompatActivity.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    // En

    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            finishAffinity();
        } else {
            Toasty.info(this, R.string.press_once_again_to_exit, Toasty.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

    private void requestPermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                multiplePermissionsReport.areAllPermissionsGranted();
                multiplePermissionsReport.isAnyPermissionPermanentlyDenied();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).withErrorListener(dexterError -> Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show()).onSameThread().check();
    }
}