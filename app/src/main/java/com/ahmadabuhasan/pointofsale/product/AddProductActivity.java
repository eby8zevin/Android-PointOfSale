package com.ahmadabuhasan.pointofsale.product;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.databinding.ActivityAddProductBinding;

import java.util.Objects;

public class AddProductActivity extends AppCompatActivity {

    public static EditText etProductCode;
    private ActivityAddProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_product);

        etProductCode = binding.etProductCode;

        this.binding.ivScanCode.setOnClickListener(view -> AddProductActivity.this.startActivity(new Intent(AddProductActivity.this, ScannerViewActivity.class)));

    }
}