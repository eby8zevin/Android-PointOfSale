package com.ahmadabuhasan.pointofsale.settings.categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.internal.view.SupportMenu;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityEditCategoryBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class EditCategoryActivity extends BaseActivity {

    private ActivityEditCategoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.edit_category);

        final String category_id = getIntent().getExtras().getString(Constant.CATEGORY_ID);

        this.binding.etCategoryName.setText(getIntent().getExtras().getString(Constant.CATEGORY_NAME));
        this.binding.etCategoryName.setEnabled(false);
        this.binding.tvUpdateCategory.setVisibility(View.GONE);

        this.binding.tvEditCategory.setOnClickListener(view -> {
            EditCategoryActivity.this.binding.etCategoryName.setEnabled(true);
            EditCategoryActivity.this.binding.etCategoryName.setTextColor(SupportMenu.CATEGORY_MASK);
            EditCategoryActivity.this.binding.tvEditCategory.setVisibility(View.GONE);
            EditCategoryActivity.this.binding.tvUpdateCategory.setVisibility(View.VISIBLE);
        });

        this.binding.tvUpdateCategory.setOnClickListener(view -> {
            String category_name = EditCategoryActivity.this.binding.etCategoryName.getText().toString().trim();
            if (category_name.isEmpty()) {
                EditCategoryActivity.this.binding.etCategoryName.setError(EditCategoryActivity.this.getString(R.string.enter_category_name));
                EditCategoryActivity.this.binding.etCategoryName.requestFocus();
                return;
            }

            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(EditCategoryActivity.this);
            databaseAccess.open();

            if (databaseAccess.updateCategory(category_id, category_name)) {
                Toasty.success(EditCategoryActivity.this, R.string.category_updated, Toasty.LENGTH_SHORT).show();
                Intent i = new Intent(EditCategoryActivity.this, CategoriesActivity.class);
                //i.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                EditCategoryActivity.this.startActivity(i);
                return;
            }
            Toasty.error(EditCategoryActivity.this, R.string.failed, Toasty.LENGTH_SHORT).show();
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