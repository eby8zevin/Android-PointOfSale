package com.ahmadabuhasan.pointofsale.settings.payment_method;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityAddPaymentMethodBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class AddPaymentMethodActivity extends BaseActivity {

    private ActivityAddPaymentMethodBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPaymentMethodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_payment_method);

        this.binding.tvAddPaymentMethod.setOnClickListener(view -> {
            String payment_method_name = AddPaymentMethodActivity.this.binding.etPaymentMethodName.getText().toString().trim();
            if (payment_method_name.isEmpty()) {
                AddPaymentMethodActivity.this.binding.etPaymentMethodName.setError(AddPaymentMethodActivity.this.getString(R.string.enter_payment_method_name));
                AddPaymentMethodActivity.this.binding.etPaymentMethodName.requestFocus();
                return;
            }

            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(AddPaymentMethodActivity.this);
            databaseAccess.open();

            if (databaseAccess.addPaymentMethod(payment_method_name)) {
                Toasty.success(AddPaymentMethodActivity.this, R.string.successfully_added, Toasty.LENGTH_SHORT).show();
                Intent i = new Intent(AddPaymentMethodActivity.this, PaymentMethodActivity.class);
                //i.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                AddPaymentMethodActivity.this.startActivity(i);
                return;
            }
            Toasty.error(AddPaymentMethodActivity.this, R.string.failed, Toasty.LENGTH_SHORT).show();
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