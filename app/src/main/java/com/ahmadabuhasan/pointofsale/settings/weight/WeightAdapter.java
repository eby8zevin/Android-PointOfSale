package com.ahmadabuhasan.pointofsale.settings.weight;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.WeightItemBinding;
import com.ahmadabuhasan.pointofsale.settings.categories.EditCategoryActivity;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.MyViewHolder> {

    private Context context;
    private List<HashMap<String, String>> weightData;

    public WeightAdapter(Context context1, List<HashMap<String, String>> weightData1) {
        this.context = context1;
        this.weightData = weightData1;
    }

    @NonNull
    @Override
    public WeightAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WeightItemBinding binding = WeightItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightAdapter.MyViewHolder holder, int position) {

        final String weight_id = this.weightData.get(position).get(Constant.WEIGHT_ID);

        holder.binding.tvWeightName.setText(this.weightData.get(position).get(Constant.WEIGHT_UNIT));
        holder.binding.ivDelete.setOnClickListener(view -> new AlertDialog.Builder(WeightAdapter.this.context)
                .setMessage(R.string.want_to_delete)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(WeightAdapter.this.context);
                    databaseAccess.open();

                    if (databaseAccess.deleteWeight(weight_id)) {
                        Toasty.success(WeightAdapter.this.context, R.string.weight_unit_deleted, Toasty.LENGTH_SHORT).show();
                        WeightAdapter.this.weightData.remove(holder.getAdapterPosition());
                        WeightAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                    } else {
                        Toasty.error(WeightAdapter.this.context, R.string.failed, Toasty.LENGTH_SHORT).show();
                    }
                    dialogInterface.cancel();
                }).setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.cancel()).show());
    }

    @Override
    public int getItemCount() {
        return weightData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private WeightItemBinding binding;

        public MyViewHolder(@NonNull WeightItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(WeightAdapter.this.context, EditWeightActivity.class);
            i.putExtra(Constant.WEIGHT_ID, (String) ((HashMap) WeightAdapter.this.weightData.get(getAdapterPosition())).get(Constant.WEIGHT_ID));
            i.putExtra(Constant.WEIGHT_UNIT, (String) ((HashMap) WeightAdapter.this.weightData.get(getAdapterPosition())).get(Constant.WEIGHT_UNIT));
            WeightAdapter.this.context.startActivity(i);
        }
    }
}