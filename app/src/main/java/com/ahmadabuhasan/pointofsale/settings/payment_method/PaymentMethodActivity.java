package com.ahmadabuhasan.pointofsale.settings.payment_method;

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
import com.ahmadabuhasan.pointofsale.databinding.ActivityPaymentMethodBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class PaymentMethodActivity extends BaseActivity {

    private ActivityPaymentMethodBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentMethodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_payment_method);

        this.binding.paymentMethodRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.binding.paymentMethodRecyclerview.setHasFixedSize(true);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        List<HashMap<String, String>> paymentMethodData = databaseAccess.getPaymentMethod();
        Log.d("data", "" + paymentMethodData.size());
        if (paymentMethodData.size() <= 0) {
            Toasty.info(this, R.string.no_data_found, Toasty.LENGTH_SHORT).show();
            this.binding.ivNoPaymentMethod.setImageResource(R.drawable.no_data);
        } else {
            this.binding.ivNoPaymentMethod.setVisibility(View.GONE);
            PaymentMethodAdapter adapter = new PaymentMethodAdapter(this, paymentMethodData);
            this.binding.paymentMethodRecyclerview.setAdapter(adapter);
        }

        this.binding.etPaymentMethodSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                DatabaseAccess databaseAccess1 = DatabaseAccess.getInstance(PaymentMethodActivity.this);
                databaseAccess1.open();

                List<HashMap<String, String>> searchPaymentMethodList = databaseAccess1.searchPaymentMethod(charSequence.toString());
                if (searchPaymentMethodList.size() <= 0) {
                    PaymentMethodActivity.this.binding.paymentMethodRecyclerview.setVisibility(View.GONE);
                    PaymentMethodActivity.this.binding.ivNoPaymentMethod.setVisibility(View.VISIBLE);
                    PaymentMethodActivity.this.binding.ivNoPaymentMethod.setImageResource(R.drawable.no_data);
                    return;
                }
                PaymentMethodActivity.this.binding.paymentMethodRecyclerview.setVisibility(View.VISIBLE);
                PaymentMethodActivity.this.binding.ivNoPaymentMethod.setVisibility(View.GONE);
                PaymentMethodAdapter adapter = new PaymentMethodAdapter(PaymentMethodActivity.this, searchPaymentMethodList);
                PaymentMethodActivity.this.binding.paymentMethodRecyclerview.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        this.binding.fabAdd.setOnClickListener(view -> PaymentMethodActivity.this.startActivity(new Intent(PaymentMethodActivity.this, AddPaymentMethodActivity.class)));
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