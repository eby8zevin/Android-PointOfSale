package com.ahmadabuhasan.pointofsale.settings.payment_method;

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
import com.ahmadabuhasan.pointofsale.databinding.PaymentMethodItemBinding;

import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.MyViewHolder> {

    private Context context;
    private List<HashMap<String, String>> paymentMethodData;

    public PaymentMethodAdapter(Context context1, List<HashMap<String, String>> paymentMethodData1) {
        this.context = context1;
        this.paymentMethodData = paymentMethodData1;
    }

    @NonNull
    @Override
    public PaymentMethodAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PaymentMethodItemBinding binding = PaymentMethodItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentMethodAdapter.MyViewHolder holder, int position) {

        final String payment_method_id = this.paymentMethodData.get(position).get(Constant.PAYMENT_METHOD_ID);

        holder.binding.tvPaymentMethodName.setText(this.paymentMethodData.get(position).get(Constant.PAYMENT_METHOD_NAME));
        holder.binding.ivDelete.setOnClickListener(view -> new AlertDialog.Builder(PaymentMethodAdapter.this.context)
                .setMessage(R.string.want_to_delete)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(PaymentMethodAdapter.this.context);
                    databaseAccess.open();

                    if (databaseAccess.deletePaymentMethod(payment_method_id)) {
                        Toasty.success(PaymentMethodAdapter.this.context, R.string.payment_method_deleted, Toasty.LENGTH_SHORT).show();
                        PaymentMethodAdapter.this.paymentMethodData.remove(holder.getAdapterPosition());
                        PaymentMethodAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                    } else {
                        Toasty.error(PaymentMethodAdapter.this.context, R.string.failed, Toasty.LENGTH_SHORT).show();
                    }
                    dialogInterface.cancel();
                }).setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.cancel()).show());
    }

    @Override
    public int getItemCount() {
        return paymentMethodData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private PaymentMethodItemBinding binding;

        public MyViewHolder(@NonNull PaymentMethodItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(PaymentMethodAdapter.this.context, EditPaymentMethodActivity.class);
            i.putExtra(Constant.PAYMENT_METHOD_ID, (String) ((HashMap) PaymentMethodAdapter.this.paymentMethodData.get(getAdapterPosition())).get(Constant.PAYMENT_METHOD_ID));
            i.putExtra(Constant.PAYMENT_METHOD_NAME, (String) ((HashMap) PaymentMethodAdapter.this.paymentMethodData.get(getAdapterPosition())).get(Constant.PAYMENT_METHOD_NAME));
            PaymentMethodAdapter.this.context.startActivity(i);
        }
    }
}