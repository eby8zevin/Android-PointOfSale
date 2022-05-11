package com.ahmadabuhasan.pointofsale.suppliers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ahmadabuhasan.pointofsale.DashboardActivity;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.database.DatabaseOpenHelper;
import com.ahmadabuhasan.pointofsale.databinding.ActivityAddSuppliersBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;
import com.ajts.androidmads.library.ExcelToSQLite;
import com.obsez.android.lib.filechooser.ChooserDialog;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.io.File;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class AddSuppliersActivity extends BaseActivity {

    private ActivityAddSuppliersBinding binding;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSuppliersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_supplier);

        this.binding.tvAddSupplier.setOnClickListener(view -> {
            String supplier_name = AddSuppliersActivity.this.binding.etSupplierName.getText().toString().trim();
            String supplier_contact_name = AddSuppliersActivity.this.binding.etSupplierContactName.getText().toString().trim();
            String supplier_cell = AddSuppliersActivity.this.binding.etSupplierCell.getText().toString().trim();
            String supplier_email = AddSuppliersActivity.this.binding.etSupplierEmail.getText().toString().trim();
            String supplier_address = AddSuppliersActivity.this.binding.etSupplierAddress.getText().toString().trim();

            if (supplier_name.isEmpty()) {
                AddSuppliersActivity.this.binding.etSupplierName.setError(AddSuppliersActivity.this.getString(R.string.enter_suppliers_name));
                AddSuppliersActivity.this.binding.etSupplierName.requestFocus();
            } else if (supplier_contact_name.isEmpty()) {
                AddSuppliersActivity.this.binding.etSupplierContactName.setError(AddSuppliersActivity.this.getString(R.string.enter_suppliers_contact_person_name));
                AddSuppliersActivity.this.binding.etSupplierContactName.requestFocus();
            } else if (supplier_cell.isEmpty()) {
                AddSuppliersActivity.this.binding.etSupplierCell.setError(AddSuppliersActivity.this.getString(R.string.enter_suppliers_cell));
                AddSuppliersActivity.this.binding.etSupplierCell.requestFocus();
            } else if (supplier_email.isEmpty() || !supplier_email.contains("@") || !supplier_email.contains(".")) {
                AddSuppliersActivity.this.binding.etSupplierEmail.setError(AddSuppliersActivity.this.getString(R.string.enter_valid_email));
                AddSuppliersActivity.this.binding.etSupplierEmail.requestFocus();
            } else if (supplier_address.isEmpty()) {
                AddSuppliersActivity.this.binding.etSupplierAddress.setError(AddSuppliersActivity.this.getString(R.string.enter_suppliers_address));
                AddSuppliersActivity.this.binding.etSupplierAddress.requestFocus();
            } else {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(AddSuppliersActivity.this);
                databaseAccess.open();

                if (databaseAccess.addSuppliers(supplier_name, supplier_contact_name, supplier_cell, supplier_email, supplier_address)) {
                    Toasty.success(AddSuppliersActivity.this, R.string.suppliers_successfully_added, Toasty.LENGTH_SHORT).show();
                    Intent i = new Intent(AddSuppliersActivity.this, SuppliersActivity.class);
                    //i.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                    AddSuppliersActivity.this.startActivity(i);
                    return;
                }
                Toasty.error(AddSuppliersActivity.this, R.string.failed, Toasty.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_supplier_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_import_supplier) {
            fileChooser();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void fileChooser() {
        new ChooserDialog((Activity) this)
                .displayPath(true)
                .withFilter(false, false, "xls")
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String dir, File dirFile) {
                        AddSuppliersActivity.this.onImport(dir);
                    }
                }).withOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("CANCEL", "CANCEL");
                dialogInterface.cancel();
            }
        }).build().show();
    }

    public void onImport(String path) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(this, R.string.no_file_found, Toast.LENGTH_SHORT).show();
            return;
        }
        ExcelToSQLite excelToSQLite = new ExcelToSQLite(getApplicationContext(), DatabaseOpenHelper.DATABASE_NAME, false);
        excelToSQLite.importFromFile(path, new ExcelToSQLite.ImportListener() {
            @Override
            public void onStart() {
                AddSuppliersActivity.this.loading = new ProgressDialog(AddSuppliersActivity.this);
                AddSuppliersActivity.this.loading.setMessage(AddSuppliersActivity.this.getString(R.string.data_importing_please_wait));
                AddSuppliersActivity.this.loading.setCancelable(false);
                AddSuppliersActivity.this.loading.show();
            }

            @Override
            public void onCompleted(String dbName) {
                Handler mHand = new Handler();
                mHand.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AddSuppliersActivity.this.loading.dismiss();
                        Toasty.success(AddSuppliersActivity.this, R.string.data_successfully_imported, Toasty.LENGTH_SHORT).show();
                        Intent i = new Intent(AddSuppliersActivity.this, DashboardActivity.class);
                        AddSuppliersActivity.this.startActivity(i);
                        AddSuppliersActivity.this.finish();
                    }
                }, 5000L);
            }

            @Override
            public void onError(Exception e) {
                AddSuppliersActivity.this.loading.dismiss();
                Toasty.error(AddSuppliersActivity.this, R.string.data_import_fail, Toasty.LENGTH_SHORT).show();
                Log.d("Error : ", "" + e.getMessage());
            }
        });
    }
}