package com.ahmadabuhasan.pointofsale.product;

import android.os.Bundle;

import com.ahmadabuhasan.pointofsale.databinding.ActivityEditProductBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;

public class EditProductActivity extends BaseActivity {

    private ActivityEditProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}