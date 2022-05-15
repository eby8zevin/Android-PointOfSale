package com.ahmadabuhasan.pointofsale.pos;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ProductCategoryItemBinding;
import com.ahmadabuhasan.pointofsale.settings.categories.EditCategoryActivity;

import java.util.HashMap;
import java.util.List;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.MyViewHolder> {

    private List<HashMap<String, String>> categoryData;
    private Context context;
    TextView tvNoData;
    ImageView ivNoData;
    RecyclerView recyclerView;
    MediaPlayer sound;

    public ProductCategoryAdapter(Context context1,
                                  List<HashMap<String, String>> categoryData1,
                                  TextView tvNoData1,
                                  ImageView ivNoData1,
                                  RecyclerView recyclerView1) {
        this.context = context1;
        this.categoryData = categoryData1;
        this.tvNoData = tvNoData1;
        this.ivNoData = ivNoData1;
        this.recyclerView = recyclerView1;
        this.sound = MediaPlayer.create(context1, R.raw.delete_sound);
    }

    @NonNull
    @Override
    public ProductCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductCategoryItemBinding binding = ProductCategoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategoryAdapter.MyViewHolder holder, int position) {

        final String category_id = this.categoryData.get(position).get(Constant.CATEGORY_ID);
        String category_name = this.categoryData.get(position).get(Constant.CATEGORY_NAME);

        holder.binding.tvCategoryName.setText(category_name);
        holder.binding.cvCategory.setOnClickListener(view -> {
            ProductCategoryAdapter.this.sound.start();
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ProductCategoryAdapter.this.context);
            databaseAccess.open();

            List<HashMap<String, String>> fromCategoryList = databaseAccess.getTabProducts(category_id);
            if (fromCategoryList.size() <= 0) {
                ProductCategoryAdapter.this.recyclerView.setVisibility(View.INVISIBLE);
                ProductCategoryAdapter.this.recyclerView.setVisibility(View.GONE);
                ProductCategoryAdapter.this.tvNoData.setVisibility(View.VISIBLE);
                ProductCategoryAdapter.this.ivNoData.setVisibility(View.VISIBLE);
                ProductCategoryAdapter.this.ivNoData.setImageResource(R.drawable.not_found);
                return;
            }
            ProductCategoryAdapter.this.tvNoData.setVisibility(View.GONE);
            ProductCategoryAdapter.this.ivNoData.setVisibility(View.GONE);
            ProductCategoryAdapter.this.recyclerView.setVisibility(View.VISIBLE);

            PosProductAdapter adapter = new PosProductAdapter(ProductCategoryAdapter.this.context, fromCategoryList);
            ProductCategoryAdapter.this.recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public int getItemCount() {
        return categoryData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ProductCategoryItemBinding binding;

        public MyViewHolder(@NonNull ProductCategoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(ProductCategoryAdapter.this.context, EditCategoryActivity.class);
            i.putExtra(Constant.CATEGORY_ID, (String) ((HashMap) ProductCategoryAdapter.this.categoryData.get(getAdapterPosition())).get(Constant.CATEGORY_ID));
            i.putExtra(Constant.CATEGORY_NAME, (String) ((HashMap) ProductCategoryAdapter.this.categoryData.get(getAdapterPosition())).get(Constant.CATEGORY_NAME));
        }
    }
}