package com.ahmadabuhasan.pointofsale.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityDashboardBinding;
import com.ahmadabuhasan.pointofsale.databinding.ActivityProductBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ProductActivity extends AppCompatActivity {

    private ActivityProductBinding binding;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_product);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.productRecyclerview.setLayoutManager(layoutManager);
        binding.productRecyclerview.setHasFixedSize(true);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<HashMap<String, String>> productData = databaseAccess.getProducts();
        Log.d("data", "" + productData.size());
        if (productData.size() <= 0) {
            Toasty.info(this, R.string.no_product_found, Toasty.LENGTH_SHORT).show();
            this.binding.ivNoProduct.setImageResource(R.drawable.no_data);
        } else {
            this.binding.ivNoProduct.setVisibility(View.GONE);
            this.binding.productRecyclerview.setAdapter(adapter);
        }

    }
}