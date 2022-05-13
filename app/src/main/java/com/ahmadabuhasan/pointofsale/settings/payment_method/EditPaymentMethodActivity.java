package com.ahmadabuhasan.pointofsale.settings.payment_method;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.internal.view.SupportMenu;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityEditPaymentMethodBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class EditPaymentMethodActivity extends BaseActivity {

    private ActivityEditPaymentMethodBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditPaymentMethodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.update_payment_method);

        final String payment_method_id = getIntent().getExtras().getString(Constant.PAYMENT_METHOD_ID);

        this.binding.etPaymentMethodName.setText(getIntent().getExtras().getString(Constant.PAYMENT_METHOD_NAME));
        this.binding.etPaymentMethodName.setEnabled(false);

        this.binding.tvUpdatePaymentMethod.setVisibility(View.GONE);
        this.binding.tvEditPaymentMethod.setOnClickListener(view -> {
            EditPaymentMethodActivity.this.binding.etPaymentMethodName.setEnabled(true);
            EditPaymentMethodActivity.this.binding.etPaymentMethodName.setTextColor(SupportMenu.CATEGORY_MASK);
            EditPaymentMethodActivity.this.binding.tvEditPaymentMethod.setVisibility(View.GONE);
            EditPaymentMethodActivity.this.binding.tvUpdatePaymentMethod.setVisibility(View.VISIBLE);
        });

        this.binding.tvUpdatePaymentMethod.setOnClickListener(view -> {
            String payment_method_name = EditPaymentMethodActivity.this.binding.etPaymentMethodName.getText().toString().trim();
            if (payment_method_name.isEmpty()) {
                EditPaymentMethodActivity.this.binding.etPaymentMethodName.setError(EditPaymentMethodActivity.this.getString(R.string.payment_method_name));
                EditPaymentMethodActivity.this.binding.etPaymentMethodName.requestFocus();
                return;
            }

            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(EditPaymentMethodActivity.this);
            databaseAccess.open();

            if (databaseAccess.updatePaymentMethod(payment_method_id, payment_method_name)) {
                Toasty.success(EditPaymentMethodActivity.this, R.string.successfully_added, Toasty.LENGTH_SHORT).show();
                Intent i = new Intent(EditPaymentMethodActivity.this, PaymentMethodActivity.class);
                //i.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                EditPaymentMethodActivity.this.startActivity(i);
                return;
            }
            Toasty.error(EditPaymentMethodActivity.this, R.string.failed, Toasty.LENGTH_SHORT).show();
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