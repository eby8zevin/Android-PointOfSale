package com.ahmadabuhasan.pointofsale.expense;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;

public class EditExpenseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);
    }
}