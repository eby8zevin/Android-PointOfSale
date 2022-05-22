package com.ahmadabuhasan.pointofsale.pdf_report;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.databinding.ActivityViewPdfBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;

import java.io.File;
import java.util.Objects;

/*
 * Created by Ahmad Abu Hasan (C) 2022
 */

public class ViewPDFActivity extends BaseActivity {

    private ActivityViewPdfBinding binding;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.order_receipt);

        Bundle bundle = getIntent().getExtras();
        Log.d("location", bundle.toString());
        if (bundle != null) {
            this.file = new File(bundle.getString("path", ""));
        }
        this.binding.pdfView.fromFile(this.file)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .enableAntialiasing(true)
                .load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_pdf, menu);
        return super.onCreateOptionsMenu(menu);
    }

}