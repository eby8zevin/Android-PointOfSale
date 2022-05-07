package com.ahmadabuhasan.pointofsale.settings.categories;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityCategoriesBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class CategoriesActivity extends BaseActivity {

    private ActivityCategoriesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.categories);

        this.binding.categoryRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.binding.categoryRecyclerview.setHasFixedSize(true);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        List<HashMap<String, String>> categoryData = databaseAccess.getProductCategory();
        Log.d("data", "" + categoryData.size());
        if (categoryData.size() <= 0) {
            Toasty.info(this, R.string.no_data_found, Toasty.LENGTH_SHORT).show();
            this.binding.ivNoCategory.setImageResource(R.drawable.no_data);
        } else {
            this.binding.ivNoCategory.setVisibility(View.GONE);
            this.binding.categoryRecyclerview.setAdapter(new CategoryAdapter(this, categoryData));
        }

        this.binding.etCategorySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                DatabaseAccess databaseAccess1 = DatabaseAccess.getInstance(CategoriesActivity.this);
                databaseAccess1.open();

                List<HashMap<String, String>> searchCategoryList = databaseAccess1.searchProductCategory(charSequence.toString());
                if (searchCategoryList.size() <= 0) {
                    CategoriesActivity.this.binding.categoryRecyclerview.setVisibility(View.GONE);
                    CategoriesActivity.this.binding.ivNoCategory.setVisibility(View.VISIBLE);
                    CategoriesActivity.this.binding.ivNoCategory.setImageResource(R.drawable.no_data);
                    return;
                }
                CategoriesActivity.this.binding.ivNoCategory.setVisibility(View.GONE);
                CategoriesActivity.this.binding.categoryRecyclerview.setVisibility(View.VISIBLE);
                CategoriesActivity.this.binding.categoryRecyclerview.setAdapter(new CategoryAdapter(CategoriesActivity.this, searchCategoryList));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        this.binding.fabAdd.setOnClickListener(view -> CategoriesActivity.this.startActivity(new Intent(CategoriesActivity.this, AddCategoryActivity.class)));
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