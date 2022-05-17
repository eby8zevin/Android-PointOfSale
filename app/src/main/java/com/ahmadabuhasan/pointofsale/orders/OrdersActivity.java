package com.ahmadabuhasan.pointofsale.orders;

import android.os.Bundle;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.databinding.ActivityOrdersBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;

import java.util.Objects;

public class OrdersActivity extends BaseActivity {

    private ActivityOrdersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);

    }
}