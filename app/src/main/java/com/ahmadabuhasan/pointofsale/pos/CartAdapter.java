package com.ahmadabuhasan.pointofsale.pos;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.CartProductItemsBinding;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private Context context;
    private List<HashMap<String, String>> cartProduct;
    ImageView ivNoData;
    TextView tvNoData;
    TextView tvTotalPrice;
    Button btnSubmitOrder;

    public static Double total_price;
    DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    MediaPlayer sound;

    public CartAdapter(Context context1,
                       List<HashMap<String, String>> cartProduct1,
                       ImageView ivNoData1,
                       TextView tvNoData1,
                       TextView tvTotalPrice1,
                       Button btnSubmitOrder1) {
        this.context = context1;
        this.cartProduct = cartProduct1;
        this.ivNoData = ivNoData1;
        this.tvNoData = tvNoData1;
        this.tvTotalPrice = tvTotalPrice1;
        this.btnSubmitOrder = btnSubmitOrder1;
        this.sound = MediaPlayer.create(context1, R.raw.delete_sound);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartProductItemsBinding binding = CartProductItemsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this.context);

        final String cart_id = this.cartProduct.get(position).get(Constant.CART_ID);
        String product_id = this.cartProduct.get(position).get(Constant.PRODUCT_ID);
        databaseAccess.open();
        String product_name = databaseAccess.getProductName(product_id);
        String weight = this.cartProduct.get(position).get(Constant.PRODUCT_WEIGHT);
        String weight_UnitID = this.cartProduct.get(position).get(Constant.PRODUCT_WEIGHT_UNIT);
        final String price = this.cartProduct.get(position).get(Constant.PRODUCT_PRICE);
        String qty = this.cartProduct.get(position).get(Constant.PRODUCT_QTY);
        String stock = this.cartProduct.get(position).get(Constant.STOCK);

        final int getStock = Integer.parseInt(stock);

        databaseAccess.open();
        String base64Image = databaseAccess.getProductImage(product_id);

        databaseAccess.open();
        String weight_UnitName = databaseAccess.getWeightUnitName(weight_UnitID);

        databaseAccess.open();
        String currency = databaseAccess.getCurrency();

        databaseAccess.open();
        total_price = Double.valueOf(databaseAccess.getTotalPrice());

        this.tvTotalPrice.setText(String.format("%s%s%s", this.context.getString(R.string.total_price), currency, this.decimalFormat.format(total_price)));

        if (base64Image != null) {
            if (base64Image.isEmpty() || base64Image.length() < 6) {
                holder.binding.ivCartProduct.setImageResource(R.drawable.image_placeholder);
            } else {
                byte[] bytes = Base64.decode(base64Image, Base64.DEFAULT);
                holder.binding.ivCartProduct.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        }

        double dPrice = Double.parseDouble(price);
        double dQty = Integer.parseInt(qty);
        Double.isNaN(dQty);
        double getPrice = dQty * dPrice;

        holder.binding.tvItemName.setText(product_name);
        holder.binding.tvWeight.setText(String.format("%s %s", weight, weight_UnitName));
        holder.binding.tvQtyNumber.setText(qty);
        holder.binding.tvPrice.setText(String.format("%s%s", currency, this.decimalFormat.format(getPrice)));

        holder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.binding.tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.binding.tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.cartProduct.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CartProductItemsBinding binding;

        public MyViewHolder(@NonNull CartProductItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}