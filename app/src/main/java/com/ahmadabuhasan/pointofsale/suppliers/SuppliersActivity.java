package com.ahmadabuhasan.pointofsale.suppliers;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivitySuppliersBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SuppliersActivity extends BaseActivity {

    private ActivitySuppliersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuppliersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_suppliers);

        this.binding.supplierRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.binding.supplierRecyclerview.setHasFixedSize(true);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        List<HashMap<String, String>> supplierData = databaseAccess.getSuppliers();
        Log.d("data", "" + supplierData.size());
    }
}