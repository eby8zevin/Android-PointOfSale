package com.ahmadabuhasan.pointofsale.report;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivitySalesReportBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class SalesReportActivity extends BaseActivity {

    private ActivitySalesReportBinding binding;

    DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySalesReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_sales);

        this.binding.ivNoData.setVisibility(View.GONE);
        this.binding.tvNoData.setVisibility(View.GONE);

        this.binding.salesReportRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.binding.salesReportRecyclerview.setHasFixedSize(true);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        ArrayList<HashMap<String, String>> allSalesItems = databaseAccess.getAllSalesItems();
        if (allSalesItems.size() <= 0) {
            Toasty.info(this, R.string.no_data_found, Toasty.LENGTH_SHORT).show();
            this.binding.salesReportRecyclerview.setVisibility(View.GONE);
            this.binding.tvTotalPrice.setVisibility(View.GONE);
            this.binding.tvNoData.setVisibility(View.VISIBLE);
            this.binding.ivNoData.setVisibility(View.VISIBLE);
            this.binding.ivNoData.setImageResource(R.drawable.not_found);
        } else {
            SalesReportAdapter adapter = new SalesReportAdapter(this, allSalesItems);
            this.binding.salesReportRecyclerview.setAdapter(adapter);
        }

        databaseAccess.open();
        String currency = databaseAccess.getCurrency();
        databaseAccess.open();
        double sub_total = databaseAccess.getTotalOrderPrice("all");
        this.binding.tvTotalPrice.setText(String.format("%s%s%s", getString(R.string.total_sales), currency, this.decimalFormat.format(sub_total)));

        databaseAccess.open();
        double get_tax = databaseAccess.getTotalTax("all");
        this.binding.tvTotalTax.setText(String.format("%s(+) : %s%s", getString(R.string.tax), currency, this.decimalFormat.format(get_tax)));

        databaseAccess.open();
        double get_discount = databaseAccess.getTotalDiscount("all");
        this.binding.tvTotalDiscount.setText(String.format("%s(-) : %s%s", getString(R.string.total_discount), currency, this.decimalFormat.format(get_discount)));

        double net_sales = (sub_total + get_tax) - get_discount;
        this.binding.tvNetSales.setText(String.format("%s: %s%s", getString(R.string.net_sales), currency, this.decimalFormat.format(net_sales)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_sales_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_all_sales:
                getReport("all");
                return true;
            case R.id.menu_daily:
                getReport(Constant.DAILY);
                return true;
            case R.id.menu_monthly:
                getReport(Constant.MONTHLY);
                return true;
            case R.id.menu_yearly:
                getReport(Constant.YEARLY);
                return true;
            case R.id.menu_export_data:
                folderChooser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getReport(String type) {

    }

    public void folderChooser() {

    }
}