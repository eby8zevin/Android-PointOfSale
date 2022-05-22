package com.ahmadabuhasan.pointofsale.orders;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityOrderDetailsBinding;
import com.ahmadabuhasan.pointofsale.pdf_report.BarCodeEncoder;
import com.ahmadabuhasan.pointofsale.pdf_report.TemplatePDF;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan (C) 2022
 */

public class OrderDetailsActivity extends BaseActivity {

    private static final int REQUEST_CONNECT = 100;

    private ActivityOrderDetailsBinding binding;

    String order_id, customer_name, order_date, order_time, tax, discount;
    String shop_name, shop_contact, shop_email, shop_address, shop_currency;
    String receiptCustomerName, receiptThanks;
    double getTax, getDiscount, total_price, calculated_TotalPrice;

    Bitmap bm = null;
    DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.order_details);

        this.binding.ivNoOrder.setVisibility(View.GONE);
        this.binding.tvNoOrder.setVisibility(View.GONE);

        this.order_id = getIntent().getExtras().getString(Constant.ORDER_ID);
        this.customer_name = getIntent().getExtras().getString(Constant.CUSTOMER_NAME);
        this.order_date = getIntent().getExtras().getString(Constant.ORDER_DATE);
        this.order_time = getIntent().getExtras().getString(Constant.ORDER_TIME);
        this.tax = getIntent().getExtras().getString(Constant.TAX);
        this.discount = getIntent().getExtras().getString(Constant.DISCOUNT);

        this.binding.orderDetailsRecyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.binding.orderDetailsRecyclerview.setHasFixedSize(true);

        databaseAccess.open();
        List<HashMap<String, String>> orderDetailsList = databaseAccess.getOrderDetailsList(this.order_id);
        if (orderDetailsList.isEmpty()) {
            Toasty.info(this, R.string.no_data_found, Toasty.LENGTH_SHORT).show();
        } else {
            OrderDetailsAdapter adapter = new OrderDetailsAdapter(OrderDetailsActivity.this, orderDetailsList);
            this.binding.orderDetailsRecyclerview.setAdapter(adapter);
        }

        databaseAccess.open();
        List<HashMap<String, String>> shopData = databaseAccess.getShopInformation();
        this.shop_name = shopData.get(0).get(Constant.SHOP_NAME);
        this.shop_contact = shopData.get(0).get(Constant.SHOP_CONTACT);
        this.shop_email = shopData.get(0).get(Constant.SHOP_EMAIL);
        this.shop_address = shopData.get(0).get(Constant.SHOP_ADDRESS);
        this.shop_currency = shopData.get(0).get(Constant.SHOP_CURRENCY);

        databaseAccess.open();
        this.total_price = databaseAccess.totalOrderPrice(this.order_id);
        this.getTax = Double.parseDouble(this.tax);
        this.getDiscount = Double.parseDouble(this.discount);
        this.calculated_TotalPrice = (this.total_price + this.getTax) - getDiscount;

        this.binding.tvTotalPrice.setText(String.format("%s%s%s", getString(R.string.sub_total), this.shop_currency, this.decimalFormat.format(this.total_price)));
        this.binding.tvTax.setText(String.format("%s : %s%s", getString(R.string.total_tax), this.shop_currency, this.decimalFormat.format(this.getTax)));
        this.binding.tvDiscount.setText(String.format("%s : %s%s", getString(R.string.discount), this.shop_currency, this.decimalFormat.format(getDiscount)));
        this.binding.tvTotalCost.setText(String.format("%s%s%s", getString(R.string.total_price), this.shop_currency, this.decimalFormat.format(this.calculated_TotalPrice)));

        this.receiptCustomerName = "Customer Name: Mr/Mrs. " + this.customer_name;
        this.receiptThanks = "Thanks for purchase. Visit again";
        TemplatePDF templatePDF = new TemplatePDF(getApplication());
        templatePDF.openDocument();
        templatePDF.addMetaData("Order Receipt", "Order Receipt", "Point Of Sale");
        templatePDF.addTitle(
                this.shop_name,
                this.shop_address
                        + "\n Email: " + this.shop_email
                        + "\n Contact: " + this.shop_contact
                        + "\n Invoice ID: " + this.order_id,
                this.order_time + " " + this.order_date
        );
        templatePDF.addParagraph(this.receiptCustomerName);

        BarCodeEncoder barCodeEncoder = new BarCodeEncoder();
        try {
            bm = barCodeEncoder.encodeAsBitmap(this.order_id, BarcodeFormat.CODE_128, 600, 300);
        } catch (WriterException e) {
            Log.d("Data", e.toString());
        }

        String[] header = {"Description", "Price"};
        this.binding.btnPdfReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                templatePDF.createTable(header, getOrdersData());
                templatePDF.addRightParagraph(receiptThanks);
                templatePDF.addImage(bm);
                templatePDF.closeDocument();
                templatePDF.viewPDF();
            }
        });

        this.binding.btnThermalPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public ArrayList<String[]> getOrdersData() {
        ArrayList<String[]> rows = new ArrayList<>();
        databaseAccess.open();
        List<HashMap<String, String>> orderDetailsList = databaseAccess.getOrderDetailsList(this.order_id);
        for (int i = 0; i < orderDetailsList.size(); i++) {
            String name = orderDetailsList.get(i).get(Constant.PRODUCT_NAME);
            String weight = orderDetailsList.get(i).get(Constant.PRODUCT_WEIGHT);
            String price = orderDetailsList.get(i).get(Constant.PRODUCT_PRICE);
            String qty = orderDetailsList.get(i).get(Constant.PRODUCT_QTY);

            double getQty = Integer.parseInt(qty);
            double getPrice = Double.parseDouble(price);
            Double.isNaN(getQty);
            double cost_total = getQty * getPrice;

            rows.add(new String[]{
                    name + "\n" +
                            weight + "\n("
                            + qty + "x" +
                            this.shop_currency + price + ")",
                    this.shop_currency + this.decimalFormat.format(cost_total)
            });
        }
        rows.add(new String[]{"..........................................", ".................................."});
        rows.add(new String[]{"Sub Total: ", this.shop_currency + this.decimalFormat.format(this.total_price)});
        rows.add(new String[]{"Total Tax: ", this.shop_currency + this.decimalFormat.format(this.getTax)});
        rows.add(new String[]{"Discount: ", this.shop_currency + this.decimalFormat.format(this.getDiscount)});
        rows.add(new String[]{"..........................................", ".................................."});
        rows.add(new String[]{"Total Price: ", this.shop_currency + this.decimalFormat.format(this.calculated_TotalPrice)});
        return rows;
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