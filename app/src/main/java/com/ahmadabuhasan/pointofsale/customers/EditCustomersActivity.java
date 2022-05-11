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

        this.binding.tvUpdateCustomer.setVisibility(View.INVISIBLE);
        this.binding.tvEditCustomer.setOnClickListener(view -> {
            EditCustomersActivity.this.binding.etCustomerName.setEnabled(true);
            EditCustomersActivity.this.binding.etCustomerCell.setEnabled(true);
            EditCustomersActivity.this.binding.etCustomerEmail.setEnabled(true);
            EditCustomersActivity.this.binding.etCustomerAddress.setEnabled(true);

            EditCustomersActivity.this.binding.etCustomerName.setTextColor(SupportMenu.CATEGORY_MASK);
            EditCustomersActivity.this.binding.etCustomerCell.setTextColor(SupportMenu.CATEGORY_MASK);
            EditCustomersActivity.this.binding.etCustomerEmail.setTextColor(SupportMenu.CATEGORY_MASK);
            EditCustomersActivity.this.binding.etCustomerAddress.setTextColor(SupportMenu.CATEGORY_MASK);

            EditCustomersActivity.this.binding.tvUpdateCustomer.setVisibility(View.VISIBLE);
        });

        this.binding.tvUpdateCustomer.setOnClickListener(view -> {
            String customer_name = EditCustomersActivity.this.binding.etCustomerName.getText().toString().trim();
            String customer_cell = EditCustomersActivity.this.binding.etCustomerCell.getText().toString().trim();
            String customer_email = EditCustomersActivity.this.binding.etCustomerEmail.getText().toString().trim();
            String customer_address = EditCustomersActivity.this.binding.etCustomerAddress.getText().toString().trim();

            if (customer_name.isEmpty()) {
                EditCustomersActivity.this.binding.etCustomerName.setError(EditCustomersActivity.this.getString(R.string.enter_customer_name));
                EditCustomersActivity.this.binding.etCustomerName.requestFocus();
            } else if (customer_cell.isEmpty()) {
                EditCustomersActivity.this.binding.etCustomerCell.setError(EditCustomersActivity.this.getString(R.string.enter_customer_cell));
                EditCustomersActivity.this.binding.etCustomerCell.requestFocus();
            } else if (customer_email.isEmpty() || !customer_email.contains("@") || !customer_email.contains(".")) {
                EditCustomersActivity.this.binding.etCustomerEmail.setError(EditCustomersActivity.this.getString(R.string.enter_valid_email));
                EditCustomersActivity.this.binding.etCustomerEmail.requestFocus();
            } else if (customer_address.isEmpty()) {
                EditCustomersActivity.this.binding.etCustomerAddress.setError(EditCustomersActivity.this.getString(R.string.enter_customer_address));
                EditCustomersActivity.this.binding.etCustomerAddress.requestFocus();
            } else {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(EditCustomersActivity.this);
                databaseAccess.open();

                if (databaseAccess.updateCustomer(getCustomer_id, customer_name, customer_cell, customer_email, customer_address)) {
                    Toasty.success(EditCustomersActivity.this, R.string.update_successfully, Toasty.LENGTH_SHORT).show();
                    Intent i = new Intent(EditCustomersActivity.this, CustomersActivity.class);
                    //i.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE;
                    EditCustomersActivity.this.startActivity(i);
                    return;
                }
                Toasty.error(EditCustomersActivity.this, R.string.failed, Toasty.LENGTH_SHORT).show();
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