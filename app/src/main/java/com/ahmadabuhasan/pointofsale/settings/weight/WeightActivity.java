package com.ahmadabuhasan.pointofsale.settings.weight;

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
import com.ahmadabuhasan.pointofsale.databinding.ActivityWeightBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class WeightActivity extends BaseActivity {

    private ActivityWeightBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWeightBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.weight);

        this.binding.weightRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.binding.weightRecyclerview.setHasFixedSize(true);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        List<HashMap<String, String>> weightData = databaseAccess.getProductWeight();
        Log.d("data", "" + weightData.size());
        if (weightData.size() <= 0) {
            Toasty.info(this, R.string.no_data_found, Toasty.LENGTH_SHORT).show();
            this.binding.ivNoWeight.setImageResource(R.drawable.no_data);
        } else {
            this.binding.ivNoWeight.setVisibility(View.GONE);
            this.binding.weightRecyclerview.setAdapter(new WeightAdapter(this, weightData));
        }

        this.binding.etWeUnNameSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                DatabaseAccess databaseAccess1 = DatabaseAccess.getInstance(WeightActivity.this);
                databaseAccess1.open();

                List<HashMap<String, String>> searchWeightList = databaseAccess1.searchProductWeight(charSequence.toString());
                if (searchWeightList.size() <= 0) {
                    WeightActivity.this.binding.weightRecyclerview.setVisibility(View.GONE);
                    WeightActivity.this.binding.ivNoWeight.setVisibility(View.VISIBLE);
                    WeightActivity.this.binding.ivNoWeight.setImageResource(R.drawable.no_data);
                    return;
                }
                WeightActivity.this.binding.ivNoWeight.setVisibility(View.GONE);
                WeightActivity.this.binding.weightRecyclerview.setVisibility(View.VISIBLE);
                WeightActivity.this.binding.weightRecyclerview.setAdapter(new WeightAdapter(WeightActivity.this, searchWeightList));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        this.binding.fabAdd.setOnClickListener(view -> WeightActivity.this.startActivity(new Intent(WeightActivity.this, AddWeightActivity.class)));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}