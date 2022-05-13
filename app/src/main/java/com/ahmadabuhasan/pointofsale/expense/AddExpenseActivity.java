package com.ahmadabuhasan.pointofsale.expense;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityAddExpenseBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class AddExpenseActivity extends BaseActivity {

    private ActivityAddExpenseBinding binding;

    int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_expense);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date());

        this.binding.etExpenseDate.setText(currentDate);
        this.binding.etExpenseTime.setText(currentTime);

        this.binding.etExpenseDate.setOnClickListener(view -> AddExpenseActivity.this.datePicker());
        this.binding.etExpenseTime.setOnClickListener(view -> AddExpenseActivity.this.timePicker());

        this.binding.tvAddExpense.setOnClickListener(view -> {
            String expense_name = AddExpenseActivity.this.binding.etExpenseName.getText().toString();
            String expense_note = AddExpenseActivity.this.binding.etExpenseName.getText().toString();
            String expense_amount = AddExpenseActivity.this.binding.etExpenseName.getText().toString();
            String expense_date = AddExpenseActivity.this.binding.etExpenseName.getText().toString();
            String expense_time = AddExpenseActivity.this.binding.etExpenseName.getText().toString();

            if (expense_name.isEmpty()) {
                AddExpenseActivity.this.binding.etExpenseName.setError(AddExpenseActivity.this.getString(R.string.expense_name_cannot_be_empty));
                AddExpenseActivity.this.binding.etExpenseName.requestFocus();
            } else if (expense_amount.isEmpty()) {
                AddExpenseActivity.this.binding.etExpenseAmount.setError(AddExpenseActivity.this.getString(R.string.expense_amount_cannot_be_empty));
                AddExpenseActivity.this.binding.etExpenseAmount.requestFocus();
            } else {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(AddExpenseActivity.this);
                databaseAccess.open();

                if (databaseAccess.addExpense(expense_name, expense_amount, expense_note, expense_date, expense_time)) {
                    Toasty.success(AddExpenseActivity.this, R.string.expense_successfully_added, Toasty.LENGTH_SHORT).show();
                    Intent i = new Intent(AddExpenseActivity.this, ExpenseActivity.class);
                    //i.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                    AddExpenseActivity.this.startActivity(i);
                    return;
                }
                Toasty.error(AddExpenseActivity.this, R.string.failed, Toasty.LENGTH_SHORT).show();
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
            AddExpenseActivity.this.binding.etExpenseDate.setText(date_time);
        }, this.mYear, this.mMonth, this.mDay);
        datePickerDialog.show();
    }

    private void timePicker() {
        Calendar calendar = Calendar.getInstance();
        this.mHour = calendar.get(Calendar.HOUR_OF_DAY);
        this.mMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hourOfDay, minute) -> {
            String am_pm;
            AddExpenseActivity.this.mHour = hourOfDay;
            AddExpenseActivity.this.mMinute = minute;
            if (AddExpenseActivity.this.mHour < 12) {
                am_pm = "AM";
                AddExpenseActivity.this.mHour = hourOfDay;
            } else {
                am_pm = "PM";
                AddExpenseActivity.this.mHour = hourOfDay - 12;
            }
            String time_date = this.mHour + ":" + minute + " " + am_pm;
            AddExpenseActivity.this.binding.etExpenseTime.setText(time_date);
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