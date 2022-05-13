package com.ahmadabuhasan.pointofsale.expense;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityExpenseBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ExpenseActivity extends BaseActivity {

    private ActivityExpenseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_expense);

        this.binding.expenseRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.binding.expenseRecyclerview.setHasFixedSize(true);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        List<HashMap<String, String>> expenseData = databaseAccess.getAllExpense();
        Log.d("data", "" + expenseData.size());
        if (expenseData.size() <= 0) {
            Toasty.info(this, R.string.no_data_found, Toasty.LENGTH_SHORT).show();
            this.binding.ivNoExpense.setImageResource(R.drawable.no_data);
        } else {
            this.binding.ivNoExpense.setVisibility(View.GONE);

            ExpenseAdapter adapter = new ExpenseAdapter(this, expenseData);
            this.binding.expenseRecyclerview.setAdapter(adapter);
        }

        this.binding.etExpenseSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                DatabaseAccess databaseAccess1 = DatabaseAccess.getInstance(ExpenseActivity.this);
                databaseAccess1.open();

                List<HashMap<String, String>> searchExpenseList = databaseAccess1.searchExpense(charSequence.toString());
                if (searchExpenseList.size() <= 0) {
                    ExpenseActivity.this.binding.expenseRecyclerview.setVisibility(View.GONE);
                    ExpenseActivity.this.binding.ivNoExpense.setVisibility(View.VISIBLE);
                    ExpenseActivity.this.binding.ivNoExpense.setImageResource(R.drawable.no_data);
                    return;
                }
                ExpenseActivity.this.binding.ivNoExpense.setVisibility(View.GONE);
                ExpenseActivity.this.binding.expenseRecyclerview.setVisibility(View.VISIBLE);

                ExpenseAdapter adapter1 = new ExpenseAdapter(ExpenseActivity.this, searchExpenseList);
                ExpenseActivity.this.binding.expenseRecyclerview.setAdapter(adapter1);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        this.binding.fabAdd.setOnClickListener(view -> ExpenseActivity.this.startActivity(new Intent(ExpenseActivity.this, AddExpenseActivity.class)));
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