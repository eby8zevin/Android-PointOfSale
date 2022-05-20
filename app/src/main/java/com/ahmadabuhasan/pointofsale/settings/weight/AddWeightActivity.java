package com.ahmadabuhasan.pointofsale.settings.weight;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityAddWeightBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan (C) 2022
 */

public class AddWeightActivity extends BaseActivity {

    private ActivityAddWeightBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddWeightBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_weight);

        this.binding.tvAddWeight.setOnClickListener(view -> {
            String weight_name = AddWeightActivity.this.binding.etWeUnNameName.getText().toString().trim();
            if (weight_name.isEmpty()) {
                AddWeightActivity.this.binding.etWeUnNameName.setError(AddWeightActivity.this.getString(R.string.enter_weight_name));
                AddWeightActivity.this.binding.etWeUnNameName.requestFocus();
                return;
            }

            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(AddWeightActivity.this);

            databaseAccess.open();
            if (databaseAccess.addWeight(weight_name)) {
                Toasty.success(AddWeightActivity.this, R.string.weight_added_successfully, Toasty.LENGTH_SHORT).show();
                Intent i = new Intent(AddWeightActivity.this, WeightActivity.class);
                //i.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                AddWeightActivity.this.startActivity(i);
                return;
            }
            Toasty.error(AddWeightActivity.this, R.string.failed, Toasty.LENGTH_SHORT).show();
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