package com.ahmadabuhasan.pointofsale.suppliers;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.internal.view.SupportMenu;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityEditSuppliersBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class EditSuppliersActivity extends BaseActivity {

    private ActivityEditSuppliersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditSuppliersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.edit_suppliers);

        final String getSupplier_id = getIntent().getExtras().getString(Constant.SUPPLIERS_ID);

        this.binding.etSupplierName.setText(getIntent().getExtras().getString(Constant.SUPPLIERS_NAME));
        this.binding.etSupplierContactName.setText(getIntent().getExtras().getString(Constant.SUPPLIERS_CONTACT_PERSON));
        this.binding.etSupplierCell.setText(getIntent().getExtras().getString(Constant.SUPPLIERS_CELL));
        this.binding.etSupplierEmail.setText(getIntent().getExtras().getString(Constant.SUPPLIERS_EMAIL));
        this.binding.etSupplierAddress.setText(getIntent().getExtras().getString(Constant.SUPPLIERS_ADDRESS));

        this.binding.etSupplierName.setEnabled(false);
        this.binding.etSupplierContactName.setEnabled(false);
        this.binding.etSupplierCell.setEnabled(false);
        this.binding.etSupplierEmail.setEnabled(false);
        this.binding.etSupplierAddress.setEnabled(false);

        this.binding.tvUpdateSupplier.setVisibility(View.GONE);
        this.binding.tvEditSupplier.setOnClickListener(view -> {
            EditSuppliersActivity.this.binding.etSupplierName.setEnabled(true);
            EditSuppliersActivity.this.binding.etSupplierContactName.setEnabled(true);
            EditSuppliersActivity.this.binding.etSupplierCell.setEnabled(true);
            EditSuppliersActivity.this.binding.etSupplierEmail.setEnabled(true);
            EditSuppliersActivity.this.binding.etSupplierAddress.setEnabled(true);

            EditSuppliersActivity.this.binding.etSupplierName.setTextColor(SupportMenu.CATEGORY_MASK);
            EditSuppliersActivity.this.binding.etSupplierContactName.setTextColor(SupportMenu.CATEGORY_MASK);
            EditSuppliersActivity.this.binding.etSupplierCell.setTextColor(SupportMenu.CATEGORY_MASK);
            EditSuppliersActivity.this.binding.etSupplierEmail.setTextColor(SupportMenu.CATEGORY_MASK);
            EditSuppliersActivity.this.binding.etSupplierAddress.setTextColor(SupportMenu.CATEGORY_MASK);

            EditSuppliersActivity.this.binding.tvUpdateSupplier.setVisibility(View.VISIBLE);
            EditSuppliersActivity.this.binding.tvEditSupplier.setVisibility(View.GONE);
        });

        this.binding.tvUpdateSupplier.setOnClickListener(view -> {
            String supplier_name = EditSuppliersActivity.this.binding.etSupplierName.getText().toString().trim();
            String supplier_contact_name = EditSuppliersActivity.this.binding.etSupplierContactName.getText().toString().trim();
            String supplier_cell = EditSuppliersActivity.this.binding.etSupplierCell.getText().toString().trim();
            String supplier_email = EditSuppliersActivity.this.binding.etSupplierEmail.getText().toString().trim();
            String supplier_address = EditSuppliersActivity.this.binding.etSupplierAddress.getText().toString().trim();

            if (supplier_name.isEmpty()) {
                EditSuppliersActivity.this.binding.etSupplierName.setError(EditSuppliersActivity.this.getString(R.string.enter_suppliers_name));
                EditSuppliersActivity.this.binding.etSupplierName.requestFocus();
            } else if (supplier_contact_name.isEmpty()) {
                EditSuppliersActivity.this.binding.etSupplierContactName.setError(EditSuppliersActivity.this.getString(R.string.enter_suppliers_contact_person_name));
                EditSuppliersActivity.this.binding.etSupplierContactName.requestFocus();
            } else if (supplier_cell.isEmpty()) {
                EditSuppliersActivity.this.binding.etSupplierCell.setError(EditSuppliersActivity.this.getString(R.string.enter_suppliers_cell));
                EditSuppliersActivity.this.binding.etSupplierCell.requestFocus();
            } else if (supplier_email.isEmpty() || !supplier_email.contains("@") || !supplier_email.contains(".")) {
                EditSuppliersActivity.this.binding.etSupplierEmail.setError(EditSuppliersActivity.this.getString(R.string.enter_valid_email));
                EditSuppliersActivity.this.binding.etSupplierEmail.requestFocus();
            } else if (supplier_address.isEmpty()) {
                EditSuppliersActivity.this.binding.etSupplierAddress.setError(EditSuppliersActivity.this.getString(R.string.enter_suppliers_address));
                EditSuppliersActivity.this.binding.etSupplierAddress.requestFocus();
            } else {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(EditSuppliersActivity.this);
                databaseAccess.open();

                if (databaseAccess.updateSuppliers(getSupplier_id, supplier_name, supplier_contact_name, supplier_cell, supplier_email, supplier_address)) {
                    Toasty.success(EditSuppliersActivity.this, R.string.update_successfully, Toasty.LENGTH_SHORT).show();
                    Intent i = new Intent(EditSuppliersActivity.this, SuppliersActivity.class);
                    //i.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                    EditSuppliersActivity.this.startActivity(i);
                    return;
                }
                Toasty.error(EditSuppliersActivity.this, R.string.failed, Toasty.LENGTH_SHORT).show();
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