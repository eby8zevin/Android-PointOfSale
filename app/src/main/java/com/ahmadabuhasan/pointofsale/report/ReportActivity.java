package com.ahmadabuhasan.pointofsale.report;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.databinding.ActivityReportBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;

import java.util.Objects;

public class ReportActivity extends BaseActivity {

    private ActivityReportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.report);

        this.binding.cvSalesReport.setOnClickListener(view -> ReportActivity.this.startActivity(new Intent(ReportActivity.this, SalesReportActivity.class)));
        this.binding.cvGraphReport.setOnClickListener(view -> ReportActivity.this.startActivity(new Intent(ReportActivity.this, GraphReportActivity.class)));
        this.binding.cvExpenseReport.setOnClickListener(view -> ReportActivity.this.startActivity(new Intent(ReportActivity.this, ExpenseReportActivity.class)));
        this.binding.cvGraphExpense.setOnClickListener(view -> ReportActivity.this.startActivity(new Intent(ReportActivity.this, ExpenseGraphActivity.class)));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}