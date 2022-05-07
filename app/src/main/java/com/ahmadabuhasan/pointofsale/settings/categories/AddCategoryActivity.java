package com.ahmadabuhasan.pointofsale.settings.categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityAddCategoryBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class AddCategoryActivity extends BaseActivity {

    private ActivityAddCategoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_category);

        this.binding.tvAddCategory.setOnClickListener(view -> {
            String category_name = AddCategoryActivity.this.binding.etCategoryName.getText().toString().trim();
            if (category_name.isEmpty()) {
                AddCategoryActivity.this.binding.etCategoryName.setError(AddCategoryActivity.this.getString(R.string.enter_category_name));
                AddCategoryActivity.this.binding.etCategoryName.requestFocus();
                return;
            }

            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(AddCategoryActivity.this);
            databaseAccess.open();

            if (databaseAccess.addCategory(category_name)) {
                Toasty.success(AddCategoryActivity.this, R.string.category_added_successfully, Toasty.LENGTH_SHORT).show();
                Intent i = new Intent(AddCategoryActivity.this, CategoriesActivity.class);
                //i.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                AddCategoryActivity.this.startActivity(i);
                return;
            }
            Toasty.error(AddCategoryActivity.this, R.string.failed, Toasty.LENGTH_SHORT).show();
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