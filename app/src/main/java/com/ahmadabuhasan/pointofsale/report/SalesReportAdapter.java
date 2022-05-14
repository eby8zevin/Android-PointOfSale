package com.ahmadabuhasan.pointofsale.report;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.SalesReportItemBinding;

import java.util.HashMap;
import java.util.List;

public class SalesReportAdapter extends RecyclerView.Adapter<SalesReportAdapter.MyViewHolder> {

    private Context context;
    private List<HashMap<String, String>> orderData;

    public SalesReportAdapter(Context context1, List<HashMap<String, String>> orderData1) {
        this.context = context1;
        this.orderData = orderData1;
    }

    @NonNull
    @Override
    public SalesReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SalesReportItemBinding binding = SalesReportItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesReportAdapter.MyViewHolder holder, int position) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this.context);
        databaseAccess.open();

        String currency = databaseAccess.getCurrency();
        holder.binding.tvProductName.setText(this.orderData.get(position).get(Constant.PRODUCT_NAME));
        holder.binding.tvDate.setText(String.format("%s%s", this.context.getString(R.string.date), this.orderData.get(position).get(Constant.PRODUCT_ORDER_DATE)));
        holder.binding.tvQty.setText(String.format("%s%s", this.context.getString(R.string.quantity), this.orderData.get(position).get(Constant.PRODUCT_QTY)));
        holder.binding.tvWeight.setText(String.format("%s%s", this.context.getString(R.string.weight), this.orderData.get(position).get(Constant.PRODUCT_WEIGHT)));

        String unit_price = this.orderData.get(position).get(Constant.PRODUCT_PRICE);
        String qty = this.orderData.get(position).get(Constant.PRODUCT_QTY);
        double price = Double.parseDouble(unit_price);
        int quantity = Integer.parseInt(qty);
        double d = quantity;
        Double.isNaN(d);
        double cost = d * price;
        holder.binding.tvTotalCost.setText(String.format("%s%s x %s = %s%s", currency, unit_price, qty, currency, cost));

        String base64Image = this.orderData.get(position).get(Constant.PRODUCT_IMAGE);
        if (base64Image == null) {
            return;
        }
        if (base64Image.isEmpty() || base64Image.length() < 6) {
            holder.binding.ivSalesReport.setImageResource(R.drawable.expense);
            return;
        }
        byte[] bytes = Base64.decode(base64Image, 0);
        holder.binding.ivSalesReport.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private SalesReportItemBinding binding;

        public MyViewHolder(@NonNull SalesReportItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}