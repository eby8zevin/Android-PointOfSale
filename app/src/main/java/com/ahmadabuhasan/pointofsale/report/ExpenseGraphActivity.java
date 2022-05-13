package com.ahmadabuhasan.pointofsale.report;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.databinding.ActivityExpenseGraphBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;

import java.util.Objects;

public class ExpenseGraphActivity extends BaseActivity {

    private ActivityExpenseGraphBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpenseGraphBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.monthly_expense_in_graph);

    }
}