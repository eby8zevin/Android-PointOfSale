package com.ahmadabuhasan.pointofsale.pos;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityProductCartBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/*
 * Created by Ahmad Abu Hasan (C) 2022
 */

public class ProductCartActivity extends BaseActivity {

    private ActivityProductCartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.product_cart);

        this.binding.tvNoProduct.setVisibility(View.GONE);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

        this.binding.cartRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.binding.cartRecyclerview.setHasFixedSize(true);
        databaseAccess.open();
        List<HashMap<String, String>> cartProductList = databaseAccess.getCartProduct();
        if (cartProductList.isEmpty()) {
            this.binding.tvTotalPrice.setVisibility(View.GONE);
            this.binding.btnSubmitOrder.setVisibility(View.GONE);
            this.binding.linearLayout.setVisibility(View.GONE);
            this.binding.cartRecyclerview.setVisibility(View.GONE);
            this.binding.tvNoProduct.setVisibility(View.VISIBLE);
            this.binding.ivNoProduct.setVisibility(View.VISIBLE);
            this.binding.ivNoProduct.setImageResource(R.drawable.empty_cart);
        } else {
            this.binding.ivNoProduct.setVisibility(View.GONE);

            CartAdapter adapter = new CartAdapter(this, cartProductList, binding.ivNoProduct, binding.tvNoProduct, binding.tvTotalPrice, binding.btnSubmitOrder);
            this.binding.cartRecyclerview.setAdapter(adapter);
        }

        this.binding.btnSubmitOrder.setOnClickListener(view -> dialog());
    }

    public void dialog() {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, PosActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}