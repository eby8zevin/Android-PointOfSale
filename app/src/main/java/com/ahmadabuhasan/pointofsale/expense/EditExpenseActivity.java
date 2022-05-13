package com.ahmadabuhasan.pointofsale.expense;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.internal.view.SupportMenu;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityEditExpenseBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.util.Calendar;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class EditExpenseActivity extends BaseActivity {

    private ActivityEditExpenseBinding binding;

    int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.edit_expense);

        final String get_ExpenseID = getIntent().getExtras().getString(Constant.EXPENSE_ID);

        this.binding.etExpenseName.setText(getIntent().getExtras().getString(Constant.EXPENSE_NAME));
        this.binding.etExpenseNote.setText(getIntent().getExtras().getString(Constant.EXPENSE_NOTE));
        this.binding.etExpenseAmount.setText(getIntent().getExtras().getString(Constant.EXPENSE_AMOUNT));
        this.binding.etExpenseDate.setText(getIntent().getExtras().getString(Constant.EXPENSE_DATE));
        this.binding.etExpenseTime.setText(getIntent().getExtras().getString(Constant.EXPENSE_TIME));

        this.binding.etExpenseName.setEnabled(false);
        this.binding.etExpenseNote.setEnabled(false);
        this.binding.etExpenseAmount.setEnabled(false);
        this.binding.etExpenseDate.setEnabled(false);
        this.binding.etExpenseTime.setEnabled(false);

        this.binding.tvUpdateExpense.setVisibility(View.GONE);
        this.binding.tvEditExpense.setOnClickListener(view -> {
            EditExpenseActivity.this.binding.etExpenseName.setEnabled(false);
            EditExpenseActivity.this.binding.etExpenseNote.setEnabled(false);
            EditExpenseActivity.this.binding.etExpenseAmount.setEnabled(false);
            EditExpenseActivity.this.binding.etExpenseDate.setEnabled(false);
            EditExpenseActivity.this.binding.etExpenseTime.setEnabled(false);

            EditExpenseActivity.this.binding.etExpenseName.setTextColor(SupportMenu.CATEGORY_MASK);
            EditExpenseActivity.this.binding.etExpenseNote.setTextColor(SupportMenu.CATEGORY_MASK);
            EditExpenseActivity.this.binding.etExpenseAmount.setTextColor(SupportMenu.CATEGORY_MASK);
            EditExpenseActivity.this.binding.etExpenseDate.setTextColor(SupportMenu.CATEGORY_MASK);
            EditExpenseActivity.this.binding.etExpenseTime.setTextColor(SupportMenu.CATEGORY_MASK);

            EditExpenseActivity.this.binding.tvEditExpense.setVisibility(View.GONE);
            EditExpenseActivity.this.binding.tvUpdateExpense.setVisibility(View.VISIBLE);
        });

        this.binding.etExpenseDate.setOnClickListener(view -> EditExpenseActivity.this.datePicker());
        this.binding.etExpenseTime.setOnClickListener(view -> EditExpenseActivity.this.timePicker());

        this.binding.tvUpdateExpense.setOnClickListener(view -> {
            String expense_name = EditExpenseActivity.this.binding.etExpenseName.getText().toString();
            String expense_note = EditExpenseActivity.this.binding.etExpenseName.getText().toString();
            String expense_amount = EditExpenseActivity.this.binding.etExpenseName.getText().toString();
            String expense_date = EditExpenseActivity.this.binding.etExpenseName.getText().toString();
            String expense_time = EditExpenseActivity.this.binding.etExpenseName.getText().toString();

            if (expense_name.isEmpty()) {
                EditExpenseActivity.this.binding.etExpenseName.setError(EditExpenseActivity.this.getString(R.string.expense_name_cannot_be_empty));
                EditExpenseActivity.this.binding.etExpenseName.requestFocus();
            } else if (expense_amount.isEmpty()) {
                EditExpenseActivity.this.binding.etExpenseAmount.setError(EditExpenseActivity.this.getString(R.string.expense_amount_cannot_be_empty));
                EditExpenseActivity.this.binding.etExpenseAmount.requestFocus();
            } else {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(EditExpenseActivity.this);
                databaseAccess.open();

                if (databaseAccess.updateExpense(get_ExpenseID, expense_name, expense_amount, expense_note, expense_date, expense_time)) {
                    Toasty.success(EditExpenseActivity.this, R.string.expense_successfully_added, Toasty.LENGTH_SHORT).show();
                    Intent i = new Intent(EditExpenseActivity.this, ExpenseActivity.class);
                    //i.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                    EditExpenseActivity.this.startActivity(i);
                    return;
                }
                Toasty.error(EditExpenseActivity.this, R.string.failed, Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void datePicker() {
        Calendar calendar = Calendar.getInstance();
        this.mYear = calendar.get(Calendar.YEAR);
        this.mMonth = calendar.get(Calendar.MONTH);
        this.mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, monthOfYear, dayOfMonth) -> {
            int month = monthOfYear + 1;
            String fm = "" + month;
            String fd = "" + dayOfMonth;
            if (monthOfYear < 10) {
                fm = "0" + month;
            }
            if (dayOfMonth < 10) {
                fd = "0" + dayOfMonth;
            }
            String date_time = year + "-" + fm + "-" + fd;
            EditExpenseActivity.this.binding.etExpenseDate.setText(date_time);
        }, this.mYear, this.mMonth, this.mDay);
        datePickerDialog.show();
    }

    private void timePicker() {
        Calendar calendar = Calendar.getInstance();
        this.mHour = calendar.get(Calendar.HOUR_OF_DAY);
        this.mMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hourOfDay, minute) -> {
            String am_pm;
            EditExpenseActivity.this.mHour = hourOfDay;
            EditExpenseActivity.this.mMinute = minute;
            if (EditExpenseActivity.this.mHour < 12) {
                am_pm = "AM";
                EditExpenseActivity.this.mHour = hourOfDay;
            } else {
                am_pm = "PM";
                EditExpenseActivity.this.mHour = hourOfDay - 12;
            }
            String time_date = this.mHour + ":" + minute + " " + am_pm;
            EditExpenseActivity.this.binding.etExpenseTime.setText(time_date);
        }, this.mHour, this.mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }
}