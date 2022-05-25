package com.ahmadabuhasan.pointofsale.customers;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.internal.view.SupportMenu;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityEditCustomersBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan (C) 2022
 */

public class EditCustomersActivity extends BaseActivity {

    private ActivityEditCustomersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditCustomersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.edit_customer);

        String getCustomer_id = getIntent().getExtras().getString(Constant.CUSTOMER_ID);

        this.binding.etCustomerName.setText(getIntent().getExtras().getString(Constant.CUSTOMER_NAME));
        this.binding.etCustomerCell.setText(getIntent().getExtras().getString(Constant.CUSTOMER_CELL));
        this.binding.etCustomerEmail.setText(getIntent().getExtras().getString(Constant.CUSTOMER_EMAIL));
        this.binding.etCustomerAddress.setText(getIntent().getExtras().getString(Constant.CUSTOMER_ADDRESS));

        this.binding.etCustomerName.setEnabled(false);
        this.binding.etCustomerCell.setEnabled(false);
        this.binding.etCustomerEmail.setEnabled(false);
        this.binding.etCustomerAddress.setEnabled(false);

        this.binding.tvUpdateCustomer.setVisibility(View.GONE);
        this.binding.tvEditCustomer.setOnClickListener(view -> {
            this.binding.etCustomerName.setEnabled(true);
            this.binding.etCustomerCell.setEnabled(true);
            this.binding.etCustomerEmail.setEnabled(true);
            this.binding.etCustomerAddress.setEnabled(true);

            this.binding.etCustomerName.setTextColor(SupportMenu.CATEGORY_MASK);
            this.binding.etCustomerCell.setTextColor(SupportMenu.CATEGORY_MASK);
            this.binding.etCustomerEmail.setTextColor(SupportMenu.CATEGORY_MASK);
            this.binding.etCustomerAddress.setTextColor(SupportMenu.CATEGORY_MASK);

            this.binding.tvEditCustomer.setVisibility(View.GONE);
            this.binding.tvUpdateCustomer.setVisibility(View.VISIBLE);
        });

        this.binding.tvUpdateCustomer.setOnClickListener(view -> {
            String customer_name = this.binding.etCustomerName.getText().toString().trim();
            String customer_cell = this.binding.etCustomerCell.getText().toString().trim();
            String customer_email = this.binding.etCustomerEmail.getText().toString().trim();
            String customer_address = this.binding.etCustomerAddress.getText().toString().trim();

            if (customer_name.isEmpty()) {
                this.binding.etCustomerName.setError(this.getString(R.string.enter_customer_name));
                this.binding.etCustomerName.requestFocus();
            } else if (customer_cell.isEmpty()) {
                this.binding.etCustomerCell.setError(this.getString(R.string.enter_customer_cell));
                this.binding.etCustomerCell.requestFocus();
            } else if (customer_email.isEmpty() || !customer_email.contains("@") || !customer_email.contains(".")) {
                this.binding.etCustomerEmail.setError(this.getString(R.string.enter_valid_email));
                this.binding.etCustomerEmail.requestFocus();
            } else if (customer_address.isEmpty()) {
                this.binding.etCustomerAddress.setError(this.getString(R.string.enter_customer_address));
                this.binding.etCustomerAddress.requestFocus();
            } else {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

                databaseAccess.open();
                if (databaseAccess.updateCustomer(getCustomer_id, customer_name, customer_cell, customer_email, customer_address)) {
                    Toasty.success(this, R.string.update_successfully, Toasty.LENGTH_SHORT).show();
                    Intent i = new Intent(this, CustomersActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    this.startActivity(i);
                } else {
                    Toasty.error(this, R.string.failed, Toasty.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }
}