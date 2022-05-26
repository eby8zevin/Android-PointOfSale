package com.ahmadabuhasan.pointofsale.pos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityProductCartBinding;
import com.ahmadabuhasan.pointofsale.orders.OrdersActivity;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;
import com.github.mikephil.charting.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan (C) 2022
 */

public class ProductCartActivity extends BaseActivity {

    private ActivityProductCartBinding binding;

    ArrayAdapter<String> customerAdapter;
    List<String> customerNames;
    ArrayAdapter<String> orderTypeAdapter;
    List<String> orderTypeNames;
    ArrayAdapter<String> paymentMethodAdapter;
    List<String> paymentMethodNames;

    DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.product_cart);

        this.binding.tvNoProduct.setVisibility(View.GONE);

        this.binding.cartRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.binding.cartRecyclerview.setHasFixedSize(true);

        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<HashMap<String, String>> cartProductList = databaseAccess.getCartProduct();
        if (cartProductList.isEmpty()) {
            this.binding.tvTotalPrice.setVisibility(View.GONE);
            this.binding.btnSubmitOrder.setVisibility(View.GONE);
            this.binding.linearLayout.setVisibility(View.GONE);
            this.binding.cartRecyclerview.setVisibility(View.GONE);
            this.binding.tvNoProduct.setVisibility(View.VISIBLE);
            this.binding.ivNoProduct.setVisibility(View.VISIBLE);
            this.binding.ivNoProduct.setImageResource(R.drawable.empty_cart);
        } else {
            this.binding.ivNoProduct.setVisibility(View.GONE);

            CartAdapter adapter = new CartAdapter(this, cartProductList, binding.ivNoProduct, binding.tvNoProduct, binding.tvTotalPrice, binding.btnSubmitOrder);
            this.binding.cartRecyclerview.setAdapter(adapter);
        }

        this.binding.btnSubmitOrder.setOnClickListener(view -> dialog());
    }

    public void dialog() {
        databaseAccess.open();
        List<HashMap<String, String>> shopData = databaseAccess.getShopInformation();
        final String shop_currency = shopData.get(0).get(Constant.SHOP_CURRENCY);
        String tax = shopData.get(0).get(Constant.TAX);
        double getTax = Double.parseDouble(Objects.requireNonNull(tax));

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_payment, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);

        ImageButton ibClose = dialogView.findViewById(R.id.ib_close);
        final Button btnSubmit = dialogView.findViewById(R.id.btn_submit);
        final TextView tvDialogCustomer = dialogView.findViewById(R.id.tv_dialog_customer);
        ImageButton ibCustomer = dialogView.findViewById(R.id.ib_dialog_customer);
        final TextView tvDialogOrderType = dialogView.findViewById(R.id.tv_dialog_order_type);
        ImageButton ibOrderType = dialogView.findViewById(R.id.ib_dialog_order_type);
        final TextView tvDialogPaymentMethod = dialogView.findViewById(R.id.tv_dialog_order_status);
        ImageButton ibPaymentMethod = dialogView.findViewById(R.id.ib_dialog_payment_method);
        TextView tvDialogTotal = dialogView.findViewById(R.id.tv_dialog_total);
        TextView tvDialogTax = dialogView.findViewById(R.id.tv_dialog_tax);
        TextView tvTotalTax = dialogView.findViewById(R.id.tv_dialog_total_tax);
        final EditText etDialogDiscount = dialogView.findViewById(R.id.et_dialog_discount);
        final TextView tvDialogTotalCost = dialogView.findViewById(R.id.tv_dialog_total_cost);

        final double total_cost = CartAdapter.total_price;
        String sb = shop_currency +
                this.decimalFormat.format(total_cost);
        tvDialogTotal.setText(sb);
        tvDialogTax.setText(String.format("%s( %s%%) : ", getString(R.string.tax), tax));

        final double calculated_tax = (total_cost * getTax) / 100.d;
        tvTotalTax.setText(String.format("%s%s", shop_currency, this.decimalFormat.format(calculated_tax)));

        double calculated_totalCost = (total_cost + calculated_tax) - Utils.DOUBLE_EPSILON;
        tvDialogTotalCost.setText(String.format("%s%s", shop_currency, this.decimalFormat.format(calculated_totalCost)));

        etDialogDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String getDiscount = charSequence.toString();
                if (getDiscount.isEmpty() || getDiscount.equals(".")) {
                    double calculated = (total_cost + calculated_tax) - Utils.DOUBLE_EPSILON;
                    tvDialogTotalCost.setText(String.format("%s%s", shop_currency, decimalFormat.format(calculated)));
                    return;
                }

                double discount_bigger = total_cost + calculated_tax;
                double discount = Double.parseDouble(getDiscount);
                if (discount > discount_bigger) {
                    etDialogDiscount.setError(getString(R.string.discount_cant_be_greater_than_total_price));
                    etDialogDiscount.requestFocus();
                    btnSubmit.setVisibility(View.INVISIBLE);
                    return;
                }

                btnSubmit.setVisibility(View.VISIBLE);
                double calculated_withDiscount = (total_cost + calculated_tax) - discount;
                tvDialogTotalCost.setText(String.format("%s%s", shop_currency, decimalFormat.format(calculated_withDiscount)));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        this.customerNames = new ArrayList<>();
        databaseAccess.open();
        List<HashMap<String, String>> customer = databaseAccess.getCustomers();
        for (int i = 0; i < customer.size(); i++) {
            this.customerNames.add(customer.get(i).get(Constant.CUSTOMER_NAME));
        }

        this.orderTypeNames = new ArrayList<>();
        databaseAccess.open();
        List<HashMap<String, String>> order_type = databaseAccess.getOrderType();
        for (int i1 = 0; i1 < order_type.size(); i1++) {
            this.orderTypeNames.add(order_type.get(i1).get(Constant.ORDER_TYPE_NAME));
        }

        this.paymentMethodNames = new ArrayList<>();
        databaseAccess.open();
        List<HashMap<String, String>> payment_method = databaseAccess.getPaymentMethod();
        for (int i2 = 0; i2 < payment_method.size(); i2++) {
            this.paymentMethodNames.add(payment_method.get(i2).get(Constant.PAYMENT_METHOD_NAME));
        }

        ibCustomer.setOnClickListener(view -> {
            customerAdapter = new ArrayAdapter<>(ProductCartActivity.this, android.R.layout.simple_list_item_1);
            customerAdapter.addAll(customerNames);

            AlertDialog.Builder dialog1 = new AlertDialog.Builder(ProductCartActivity.this);
            View dialogView1 = getLayoutInflater().inflate(R.layout.dialog_list_search, null);
            dialog1.setView(dialogView1);
            dialog1.setCancelable(false);

            TextView title = dialogView1.findViewById(R.id.tv_dialog_title);
            EditText search = dialogView1.findViewById(R.id.et_dialog_search);
            ListView dialogListView = dialogView1.findViewById(R.id.dialog_listView);
            Button btnCancel = dialogView1.findViewById(R.id.btn_dialog_cancel);

            title.setText(R.string.select_customer);
            dialogListView.setVerticalScrollBarEnabled(true);
            dialogListView.setAdapter(customerAdapter);
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    customerAdapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            final AlertDialog alertDialog = dialog1.create();
            btnCancel.setOnClickListener(view1 -> alertDialog.dismiss());
            alertDialog.show();
            dialogListView.setOnItemClickListener((adapterView, view12, position, l) -> {
                alertDialog.dismiss();
                String selectedItem = customerAdapter.getItem(position);
                tvDialogCustomer.setText(selectedItem);
            });
        });

        ibOrderType.setOnClickListener(view -> {
            orderTypeAdapter = new ArrayAdapter<>(ProductCartActivity.this, android.R.layout.simple_list_item_1);
            orderTypeAdapter.addAll(orderTypeNames);

            AlertDialog.Builder dialog2 = new AlertDialog.Builder(ProductCartActivity.this);
            View dialogView2 = getLayoutInflater().inflate(R.layout.dialog_list_search, null);
            dialog2.setView(dialogView2);
            dialog2.setCancelable(false);

            TextView title = dialogView2.findViewById(R.id.tv_dialog_title);
            EditText search = dialogView2.findViewById(R.id.et_dialog_search);
            ListView dialogListView = dialogView2.findViewById(R.id.dialog_listView);
            Button btnCancel = dialogView2.findViewById(R.id.btn_dialog_cancel);

            title.setText(R.string.select_order_type);
            dialogListView.setVerticalScrollBarEnabled(true);
            dialogListView.setAdapter(orderTypeAdapter);
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    orderTypeAdapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            final AlertDialog alertDialog = dialog2.create();
            btnCancel.setOnClickListener(view1 -> alertDialog.dismiss());
            alertDialog.show();
            dialogListView.setOnItemClickListener((adapterView, view12, position, l) -> {
                alertDialog.dismiss();
                String selectedItem = orderTypeAdapter.getItem(position);
                tvDialogOrderType.setText(selectedItem);
            });
        });

        ibPaymentMethod.setOnClickListener(view -> {
            paymentMethodAdapter = new ArrayAdapter<>(ProductCartActivity.this, android.R.layout.simple_list_item_1);
            paymentMethodAdapter.addAll(paymentMethodNames);

            AlertDialog.Builder dialog3 = new AlertDialog.Builder(ProductCartActivity.this);
            View dialogView3 = getLayoutInflater().inflate(R.layout.dialog_list_search, null);
            dialog3.setView(dialogView3);
            dialog3.setCancelable(false);

            TextView title = dialogView3.findViewById(R.id.tv_dialog_title);
            EditText search = dialogView3.findViewById(R.id.et_dialog_search);
            ListView dialogListView = dialogView3.findViewById(R.id.dialog_listView);
            Button btnCancel = dialogView3.findViewById(R.id.btn_dialog_cancel);

            title.setText(R.string.select_payment_method);
            dialogListView.setVerticalScrollBarEnabled(true);
            dialogListView.setAdapter(paymentMethodAdapter);
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    paymentMethodAdapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            final AlertDialog alertDialog = dialog3.create();
            btnCancel.setOnClickListener(view1 -> alertDialog.dismiss());
            alertDialog.show();
            dialogListView.setOnItemClickListener((adapterView, view12, position, l) -> {
                alertDialog.dismiss();
                String selectedItem = paymentMethodAdapter.getItem(position);
                tvDialogPaymentMethod.setText(selectedItem);
            });
        });

        final AlertDialog closeSubmit = dialog.create();
        ibClose.setOnClickListener(view -> closeSubmit.dismiss());
        btnSubmit.setOnClickListener(view -> {
            String discount;
            String customer_name = tvDialogCustomer.getText().toString().trim();
            String submit_orderType = tvDialogOrderType.getText().toString().trim();
            String submit_paymentMethod = tvDialogPaymentMethod.getText().toString().trim();
            String disc = etDialogDiscount.getText().toString().trim();
            if (disc.isEmpty()) {
                discount = "0.00";
            } else {
                discount = disc;
            }
            proceedOrder(customer_name, submit_orderType, submit_paymentMethod, calculated_tax, discount);
            closeSubmit.dismiss();
        });
        closeSubmit.show();
    }

    public void proceedOrder(String customerName, String orderType, String paymentMethod, double tax, String discount) {
        String productID = Constant.PRODUCT_ID;
        databaseAccess.open();
        if (databaseAccess.getCartItemCount() > 0) {
            databaseAccess.open();
            List<HashMap<String, String>> lines = databaseAccess.getCartProduct();
            if (lines.isEmpty()) {
                Toasty.error(this, R.string.no_product_found, Toasty.LENGTH_SHORT).show();
            } else {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
                String currentTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date());
                String timeStamp = Long.valueOf(System.currentTimeMillis() / 1000).toString();
                Log.d("Time", timeStamp);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(Constant.ORDER_DATE, currentDate);
                    jsonObject.put(Constant.ORDER_TIME, currentTime);
                    jsonObject.put(Constant.ORDER_TYPE, orderType);
                    jsonObject.put(Constant.ORDER_PAYMENT_METHOD, paymentMethod);
                    jsonObject.put(Constant.CUSTOMER_NAME, customerName);
                    jsonObject.put(Constant.TAX, tax);
                    jsonObject.put(Constant.DISCOUNT, discount);

                    JSONArray array = new JSONArray();
                    for (int i = 0; i < lines.size(); i++) {
                        databaseAccess.open();
                        String product_id = lines.get(i).get(productID);
                        String product_name = databaseAccess.getProductName(product_id);

                        databaseAccess.open();
                        String weight_unit_id = lines.get(i).get(Constant.PRODUCT_WEIGHT_UNIT);
                        String weight_unit = databaseAccess.getWeightUnitName(weight_unit_id);

                        databaseAccess.open();
                        String product_image = databaseAccess.getProductImage(product_id);

                        JSONObject obj = new JSONObject();
                        obj.put(productID, product_id);
                        obj.put(Constant.PRODUCT_NAME, product_name);
                        obj.put(Constant.PRODUCT_WEIGHT, lines.get(i).get(Constant.PRODUCT_WEIGHT) + " " + weight_unit);
                        obj.put(Constant.PRODUCT_QTY, lines.get(i).get(Constant.PRODUCT_QTY));
                        obj.put(Constant.STOCK, lines.get(i).get(Constant.STOCK));
                        obj.put(Constant.PRODUCT_PRICE, lines.get(i).get(Constant.PRODUCT_PRICE));
                        obj.put(Constant.PRODUCT_IMAGE, product_image);
                        obj.put(Constant.PRODUCT_ORDER_DATE, currentDate);
                        array.put(obj);
                    }
                    jsonObject.put("lines", array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                saveOrderInOfflineDb(jsonObject);
            }
        } else {
            Toasty.error(this, R.string.no_product_in_cart, Toasty.LENGTH_SHORT).show();
        }
    }

    private void saveOrderInOfflineDb(JSONObject obj) {
        String timeStamp = Long.valueOf(System.currentTimeMillis() / 1000).toString();
        databaseAccess.open();
        databaseAccess.insertOrder(timeStamp, obj);
        Toasty.success(this, R.string.order_done_successful, Toasty.LENGTH_SHORT).show();
        Intent intent = new Intent(this, OrdersActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, PosActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}