package com.ahmadabuhasan.pointofsale.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.databinding.ActivitySettingsBinding;
import com.ahmadabuhasan.pointofsale.settings.backup.BackupActivity;
import com.ahmadabuhasan.pointofsale.settings.categories.CategoriesActivity;
import com.ahmadabuhasan.pointofsale.settings.order_type.DeliveryActivity;
import com.ahmadabuhasan.pointofsale.settings.payment_method.PaymentMethodActivity;
import com.ahmadabuhasan.pointofsale.settings.shop.ShopInformationActivity;
import com.ahmadabuhasan.pointofsale.settings.weight.WeightActivity;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;
import com.ahmadabuhasan.pointofsale.utils.Utils;

import java.util.Objects;

/*
 * Created by Ahmad Abu Hasan (C) 2022
 */

public class SettingsActivity extends BaseActivity {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.action_settings);

        new Utils().interstitialAdsShow(this);

        this.binding.cvShopInfo.setOnClickListener(view -> this.startActivity(new Intent(SettingsActivity.this, ShopInformationActivity.class)));
        this.binding.cvCategory.setOnClickListener(view -> this.startActivity(new Intent(SettingsActivity.this, CategoriesActivity.class)));
        this.binding.cvWeight.setOnClickListener(view -> this.startActivity(new Intent(SettingsActivity.this, WeightActivity.class)));
        this.binding.cvDelivery.setOnClickListener(view -> this.startActivity(new Intent(SettingsActivity.this, DeliveryActivity.class)));
        this.binding.cvPaymentMethod.setOnClickListener(view -> this.startActivity(new Intent(SettingsActivity.this, PaymentMethodActivity.class)));
        this.binding.cvBackup.setOnClickListener(view -> this.startActivity(new Intent(SettingsActivity.this, BackupActivity.class)));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}