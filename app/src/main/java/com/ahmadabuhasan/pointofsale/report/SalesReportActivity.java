package com.ahmadabuhasan.pointofsale.report;

import android.os.Bundle;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.databinding.ActivitySalesReportBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;

import java.util.Objects;

public class SalesReportActivity extends BaseActivity {

    private ActivitySalesReportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySalesReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_sales);

    }
}