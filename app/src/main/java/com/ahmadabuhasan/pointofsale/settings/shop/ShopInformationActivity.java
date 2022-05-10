package com.ahmadabuhasan.pointofsale.settings.shop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.internal.view.SupportMenu;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityShopInformationBinding;
import com.ahmadabuhasan.pointofsale.settings.SettingsActivity;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ShopInformationActivity extends BaseActivity {

    private ActivityShopInformationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.shop_information);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        List<HashMap<String, String>> shopData = databaseAccess.getShopInformation();

        this.binding.etShopName.setText(shopData.get(0).get(Constant.SHOP_NAME));
        this.binding.etShopContact.setText(shopData.get(0).get(Constant.SHOP_CONTACT));
        this.binding.etShopEmail.setText(shopData.get(0).get(Constant.SHOP_EMAIL));
        this.binding.etShopAddress.setText(shopData.get(0).get(Constant.SHOP_ADDRESS));
        this.binding.etShopCurrency.setText(shopData.get(0).get(Constant.SHOP_CURRENCY));
        this.binding.etTax.setText(shopData.get(0).get(Constant.TAX));

        this.binding.etShopName.setEnabled(false);
        this.binding.etShopContact.setEnabled(false);
        this.binding.etShopEmail.setEnabled(false);
        this.binding.etShopAddress.setEnabled(false);
        this.binding.etShopCurrency.setEnabled(false);
        this.binding.etTax.setEnabled(false);

        this.binding.tvShopUpdate.setVisibility(View.GONE);
        this.binding.tvShopEdit.setOnClickListener(view -> {
            ShopInformationActivity.this.binding.etShopName.setEnabled(true);
            ShopInformationActivity.this.binding.etShopContact.setEnabled(true);
            ShopInformationActivity.this.binding.etShopEmail.setEnabled(true);
            ShopInformationActivity.this.binding.etShopAddress.setEnabled(true);
            ShopInformationActivity.this.binding.etShopCurrency.setEnabled(true);
            ShopInformationActivity.this.binding.etTax.setEnabled(true);

            ShopInformationActivity.this.binding.etShopName.setTextColor(SupportMenu.CATEGORY_MASK);
            ShopInformationActivity.this.binding.etShopContact.setTextColor(SupportMenu.CATEGORY_MASK);
            ShopInformationActivity.this.binding.etShopEmail.setTextColor(SupportMenu.CATEGORY_MASK);
            ShopInformationActivity.this.binding.etShopAddress.setTextColor(SupportMenu.CATEGORY_MASK);
            ShopInformationActivity.this.binding.etShopCurrency.setTextColor(SupportMenu.CATEGORY_MASK);
            ShopInformationActivity.this.binding.etTax.setTextColor(SupportMenu.CATEGORY_MASK);

            ShopInformationActivity.this.binding.tvShopUpdate.setVisibility(View.VISIBLE);
            ShopInformationActivity.this.binding.tvShopEdit.setVisibility(View.GONE);
        });

        this.binding.tvShopUpdate.setOnClickListener(view -> {
            Toasty.warning(ShopInformationActivity.this, Html.fromHtml("<small>Please purchase for</small><br> <big><b>Point Of Sale PRO</b></big>.<br> Thank you"), Toasty.LENGTH_LONG).show();

            /*String shop_name = ShopInformationActivity.this.binding.etShopName.getText().toString().trim();
            String shop_contact = ShopInformationActivity.this.binding.etShopContact.getText().toString().trim();
            String shop_email = ShopInformationActivity.this.binding.etShopEmail.getText().toString().trim();
            String shop_address = ShopInformationActivity.this.binding.etShopAddress.getText().toString().trim();
            String shop_currency = ShopInformationActivity.this.binding.etShopCurrency.getText().toString().trim();
            String tax = ShopInformationActivity.this.binding.etTax.getText().toString().trim();

            if (shop_name.isEmpty()) {
                ShopInformationActivity.this.binding.etShopName.setError(ShopInformationActivity.this.getString(R.string.shop_name_cannot_be_empty));
                ShopInformationActivity.this.binding.etShopName.requestFocus();
            } else if (shop_contact.isEmpty()) {
                ShopInformationActivity.this.binding.etShopContact.setError(ShopInformationActivity.this.getString(R.string.shop_contact_cannot_be_empty));
                ShopInformationActivity.this.binding.etShopContact.requestFocus();
            } else if (shop_email.isEmpty() || !shop_email.contains("@") || !shop_email.contains(".")) {
                ShopInformationActivity.this.binding.etShopEmail.setError(ShopInformationActivity.this.getString(R.string.enter_valid_email));
                ShopInformationActivity.this.binding.etShopEmail.requestFocus();
            } else if (shop_address.isEmpty()) {
                ShopInformationActivity.this.binding.etShopAddress.setError(ShopInformationActivity.this.getString(R.string.shop_address_cannot_be_empty));
                ShopInformationActivity.this.binding.etShopAddress.requestFocus();
            } else if (shop_currency.isEmpty()) {
                ShopInformationActivity.this.binding.etShopCurrency.setError(ShopInformationActivity.this.getString(R.string.shop_currency_cannot_be_empty));
                ShopInformationActivity.this.binding.etShopCurrency.requestFocus();
            } else if (tax.isEmpty()) {
                ShopInformationActivity.this.binding.etTax.setError(ShopInformationActivity.this.getString(R.string.tax_in_percentage));
                ShopInformationActivity.this.binding.etTax.requestFocus();
            } else {
                DatabaseAccess databaseAccess1 = DatabaseAccess.getInstance(ShopInformationActivity.this);
                databaseAccess1.open();

                if (databaseAccess1.updateShopInformation(shop_name, shop_contact, shop_email, shop_address, shop_currency, tax)) {
                    Toasty.success(ShopInformationActivity.this, R.string.shop_information_updated_successfully, Toasty.LENGTH_SHORT).show();
                    Intent i = new Intent(ShopInformationActivity.this, SettingsActivity.class);
                    //i.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                    ShopInformationActivity.this.startActivity(i);
                    return;
                }
                Toasty.error(ShopInformationActivity.this, R.string.failed, Toasty.LENGTH_SHORT).show();
            }*/
        });
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