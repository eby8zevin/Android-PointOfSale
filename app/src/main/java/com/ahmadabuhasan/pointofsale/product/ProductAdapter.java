package com.ahmadabuhasan.pointofsale.product;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.DashboardActivity;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ProductItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context context;
    private List<HashMap<String, String>> productData;

    public ProductAdapter(Context context1, List<HashMap<String, String>> productData1) {
        this.context = context1;
        this.productData = productData1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductItemBinding binding = ProductItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this.context);

        final String product_id = this.productData.get(position).get(Constant.PRODUCT_ID);
        String base64Image = this.productData.get(position).get(Constant.PRODUCT_IMAGE);
        databaseAccess.open();
        String currency = databaseAccess.getCurrency();
        databaseAccess.open();
        String supplier_name = databaseAccess.getSupplierName(this.productData.get(position).get(Constant.PRODUCT_SUPPLIER));

        holder.binding.tvProductName.setText(this.productData.get(position).get(Constant.PRODUCT_NAME));
        holder.binding.tvProductSupplier.setText(this.context.getString(R.string.product_supplier) + supplier_name);
        holder.binding.tvProductBuyPrice.setText(this.context.getString(R.string.buy_price) + currency + this.productData.get(position).get("product_buy_price"));
        holder.binding.tvProductSellPrice.setText(this.context.getString(R.string.sell_price) + currency + this.productData.get(position).get(Constant.PRODUCT_SELL_PRICE));

        if (base64Image != null) {
            if (base64Image.length() < 6) {
                Log.d("64base", base64Image);
                //holder.binding.ivProductImage.setImageResource(R.drawable.image_placeholder);
                Glide.with(holder.itemView.getContext())
                        .load(base64Image)
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.image_placeholder))
                        .into(holder.binding.ivProductImage);
            } else {
                byte[] bytes = Base64.decode(base64Image, Base64.DEFAULT);
                holder.binding.ivProductImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        }

        holder.binding.ivDelete.setOnClickListener(view -> new AlertDialog.Builder(ProductAdapter.this.context)
                .setMessage(R.string.want_to_delete_product)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    databaseAccess.open();
                    if (databaseAccess.deleteProduct(product_id)) {
                        Toasty.error(context, R.string.product_deleted, Toasty.LENGTH_SHORT).show();
                        ProductAdapter.this.productData.remove(holder.getAdapterPosition());
                        ProductAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                    } else {
                        Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                    dialogInterface.cancel();
                }).setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.cancel()).show());
    }

    @Override
    public int getItemCount() {
        return productData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ProductItemBinding binding;

        public MyViewHolder(@NonNull ProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(ProductAdapter.this.context, DashboardActivity.class);
            i.putExtra(Constant.PRODUCT_ID, (String) ((HashMap) ProductAdapter.this.productData.get(getAdapterPosition())).get(Constant.PRODUCT_ID));
            ProductAdapter.this.context.startActivity(i);
        }
    }
}