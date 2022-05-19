package com.ahmadabuhasan.pointofsale.pos;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Base64;
import android.util.Log;
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
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

/*
 * Created by Ahmad Abu Hasan (C) 2022
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private final Context context;
    private final List<HashMap<String, String>> cartProduct;
    ImageView ivNoCart;
    TextView tvNoCart;
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
        this.ivNoCart = ivNoData1;
        this.tvNoCart = tvNoData1;
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

        final int getStock = Integer.parseInt(Objects.requireNonNull(stock));

        databaseAccess.open();
        String base64Image = databaseAccess.getProductImage(product_id);

        databaseAccess.open();
        String weight_UnitName = databaseAccess.getWeightUnitName(weight_UnitID);

        databaseAccess.open();
        String currency = databaseAccess.getCurrency();

        databaseAccess.open();
        total_price = databaseAccess.getTotalPrice();

        this.tvTotalPrice.setText(String.format("%s%s%s", this.context.getString(R.string.total_price), currency, this.decimalFormat.format(total_price)));

        if (base64Image != null) {
            if (base64Image.isEmpty() || base64Image.length() < 6) {
                holder.binding.ivCartProduct.setImageResource(R.drawable.image_placeholder);
            } else {
                byte[] bytes = Base64.decode(base64Image, Base64.DEFAULT);
                holder.binding.ivCartProduct.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        }

        double dPrice = Double.parseDouble(Objects.requireNonNull(price));
        double dQty = Integer.parseInt(Objects.requireNonNull(qty));
        Double.isNaN(dQty);
        double getPrice = dQty * dPrice;

        holder.binding.tvItemName.setText(product_name);
        holder.binding.tvWeight.setText(String.format("%s %s", weight, weight_UnitName));
        holder.binding.tvQtyNumber.setText(qty);
        holder.binding.tvPrice.setText(String.format("%s%s", currency, this.decimalFormat.format(getPrice)));

        holder.binding.ivDelete.setOnClickListener(view -> {
            databaseAccess.open();
            if (databaseAccess.deleteProductFromCart(cart_id)) {
                Toasty.success(context, R.string.product_removed_from_cart, Toasty.LENGTH_SHORT).show();
                sound.start();
                cartProduct.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());

                databaseAccess.open();
                total_price = databaseAccess.getTotalPrice();
                tvTotalPrice.setText(MessageFormat.format("{0}{1}{2}", R.string.total_price, currency, decimalFormat.format(total_price)));
            } else {
                Toasty.error(context, R.string.failed, Toasty.LENGTH_SHORT).show();
            }

            databaseAccess.open();
            int itemCount = databaseAccess.getCartItemCount();
            Log.d("itemCount", "" + itemCount);
            if (itemCount < 0) {
                tvTotalPrice.setVisibility(View.GONE);
                btnSubmitOrder.setVisibility(View.GONE);
                ivNoCart.setVisibility(View.VISIBLE);
                tvNoCart.setVisibility(View.VISIBLE);
            }
        });

        holder.binding.tvMinus.setOnClickListener(view -> {
            String qty1 = holder.binding.tvQtyNumber.getText().toString();
            int get_qty = Integer.parseInt(qty1);
            if (get_qty >= 2) {
                int getQty = get_qty - 1;
                double parsePrice = Double.parseDouble(price);
                double d = getQty;
                Double.isNaN(d);
                double cost = parsePrice * d;

                holder.binding.tvPrice.setText(String.format("%s%s", currency, decimalFormat.format(cost)));
                holder.binding.tvQtyNumber.setText(MessageFormat.format("{0}", getQty));

                databaseAccess.open();
                databaseAccess.updateProductQty(cart_id, "" + getQty);
                total_price = total_price - Double.parseDouble(price);
                tvTotalPrice.setText(String.format("%s%s%s", this.context.getString(R.string.total_price), currency, decimalFormat.format(total_price)));
            }
        });

        holder.binding.tvPlus.setOnClickListener(view -> {
            String qty12 = holder.binding.tvQtyNumber.getText().toString();
            int get_qty = Integer.parseInt(qty12);
            if (get_qty >= getStock) {
                Toasty.error(context, R.string.available_stock, Toasty.LENGTH_SHORT).show();
                return;
            }
            int getQty = get_qty + 1;
            double parsePrice = Double.parseDouble(price);
            double d = getQty;
            Double.isNaN(d);
            double cost = parsePrice * d;

            holder.binding.tvPrice.setText(String.format("%s%s", currency, decimalFormat.format(cost)));
            holder.binding.tvQtyNumber.setText(MessageFormat.format("{0}", getQty));

            databaseAccess.open();
            databaseAccess.updateProductQty(cart_id, "" + getQty);
            total_price = total_price + Double.parseDouble(price);
            tvTotalPrice.setText(String.format("%s%s%s", this.context.getString(R.string.total_price), currency, decimalFormat.format(total_price)));
        });
    }

    @Override
    public int getItemCount() {
        return this.cartProduct.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final CartProductItemsBinding binding;

        public MyViewHolder(@NonNull CartProductItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}