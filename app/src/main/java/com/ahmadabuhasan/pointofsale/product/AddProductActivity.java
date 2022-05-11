package com.ahmadabuhasan.pointofsale.product;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.DashboardActivity;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.database.DatabaseOpenHelper;
import com.ahmadabuhasan.pointofsale.databinding.ActivityAddProductBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;
import com.ajts.androidmads.library.ExcelToSQLite;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class AddProductActivity extends BaseActivity {

    public static EditText etProductCode;
    private ActivityAddProductBinding binding;

    ArrayAdapter<String> categoryAdapter;
    ArrayAdapter<String> weightUnitAdapter;
    ArrayAdapter<String> supplierAdapter;

    List<String> categoryNames;
    List<String> weightUnitNames;
    List<String> supplierNames;

    String encodedImage = "N/A";
    String selectedCategoryID;
    String selectedWeightUnitID;
    String selectedSupplierID;
    String mediaPath;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_product);

        etProductCode = findViewById(R.id.et_product_code);

        this.binding.ivScanCode.setOnClickListener(view -> AddProductActivity.this.startActivity(new Intent(AddProductActivity.this, ScannerViewActivity.class)));

        this.binding.tvChooseImage.setOnClickListener(view -> {
            Intent i = new Intent(AddProductActivity.this, ImageSelectActivity.class);
            i.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);
            i.putExtra(ImageSelectActivity.FLAG_CAMERA, true);
            i.putExtra(ImageSelectActivity.FLAG_GALLERY, true);
            AddProductActivity.this.startActivityForResult(i, 1213);
        });

        this.binding.ivProduct.setOnClickListener(view -> {
            Intent i = new Intent(AddProductActivity.this, ImageSelectActivity.class);
            i.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);
            i.putExtra(ImageSelectActivity.FLAG_CAMERA, true);
            i.putExtra(ImageSelectActivity.FLAG_GALLERY, true);
            AddProductActivity.this.startActivityForResult(i, 1213);
        });

        this.categoryNames = new ArrayList<>();
        this.weightUnitNames = new ArrayList<>();
        this.supplierNames = new ArrayList<>();

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

        databaseAccess.open();
        final List<HashMap<String, String>> productCategory = databaseAccess.getProductCategory();
        for (int i = 0; i < productCategory.size(); i++) {
            this.categoryNames.add(productCategory.get(i).get(Constant.CATEGORY_NAME));
        }

        databaseAccess.open();
        final List<HashMap<String, String>> weightUnit = databaseAccess.getWeightUnit();
        for (int i1 = 0; i1 < weightUnit.size(); i1++) {
            this.weightUnitNames.add(weightUnit.get(i1).get(Constant.WEIGHT_UNIT));
        }

        databaseAccess.open();
        final List<HashMap<String, String>> productSupplier = databaseAccess.getProductSupplier();
        for (int i2 = 0; i2 < productSupplier.size(); i2++) {
            this.supplierNames.add(productSupplier.get(i2).get(Constant.SUPPLIERS_NAME));
        }

        this.binding.etProductCategory.setOnClickListener(view -> {
            AddProductActivity.this.categoryAdapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_list_item_1);
            AddProductActivity.this.categoryAdapter.addAll(AddProductActivity.this.categoryNames);

            AlertDialog.Builder dialog = new AlertDialog.Builder(AddProductActivity.this);
            View dialogView = AddProductActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, (ViewGroup) null);
            dialog.setView(dialogView);
            dialog.setCancelable(false);

            TextView title = dialogView.findViewById(R.id.tv_dialog_title);
            EditText search = dialogView.findViewById(R.id.et_dialog_search);
            ListView dialogListView = dialogView.findViewById(R.id.dialog_listView);
            Button btnCancel = dialogView.findViewById(R.id.btn_dialog_cancel);

            title.setText(R.string.product_category);
            dialogListView.setVerticalScrollBarEnabled(true);
            dialogListView.setAdapter((ListAdapter) AddProductActivity.this.categoryAdapter);
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    AddProductActivity.this.categoryAdapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            final AlertDialog alertDialog = dialog.create();
            btnCancel.setOnClickListener(view1 -> alertDialog.dismiss());
            alertDialog.show();
            dialogListView.setOnItemClickListener((adapterView, view1, i, l) -> {
                alertDialog.dismiss();
                String selectedItem = AddProductActivity.this.categoryAdapter.getItem(i);
                String category_id = "0";
                AddProductActivity.this.binding.etProductCategory.setText(selectedItem);
                for (int i3 = 0; i3 < AddProductActivity.this.categoryNames.size(); i3++) {
                    if (AddProductActivity.this.categoryNames.get(i3).equalsIgnoreCase(selectedItem)) {
                        category_id = (String) ((HashMap) productCategory.get(i3)).get(Constant.CATEGORY_ID);
                    }
                }
                AddProductActivity.this.selectedCategoryID = category_id;
                Log.d(Constant.CATEGORY_ID, category_id);
            });
        });

        this.binding.etProductWeightUnit.setOnClickListener(view -> {
            AddProductActivity.this.weightUnitAdapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_list_item_1);
            AddProductActivity.this.weightUnitAdapter.addAll(AddProductActivity.this.weightUnitNames);

            AlertDialog.Builder dialog = new AlertDialog.Builder(AddProductActivity.this);
            View dialogView = AddProductActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, (ViewGroup) null);
            dialog.setView(dialogView);
            dialog.setCancelable(false);

            TextView title = dialogView.findViewById(R.id.tv_dialog_title);
            EditText search = dialogView.findViewById(R.id.et_dialog_search);
            ListView dialogListView = dialogView.findViewById(R.id.dialog_listView);
            Button btnCancel = dialogView.findViewById(R.id.btn_dialog_cancel);

            title.setText(R.string.product_weight_unit);
            dialogListView.setVerticalScrollBarEnabled(true);
            dialogListView.setAdapter((ListAdapter) AddProductActivity.this.weightUnitAdapter);
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    AddProductActivity.this.weightUnitAdapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            final AlertDialog alertDialog = dialog.create();
            btnCancel.setOnClickListener(view13 -> alertDialog.dismiss());
            alertDialog.show();
            dialogListView.setOnItemClickListener((adapterView, view11, i, l) -> {
                alertDialog.dismiss();
                String selectedItem = AddProductActivity.this.weightUnitAdapter.getItem(i);
                String weight_unit_id = "0";
                AddProductActivity.this.binding.etProductWeightUnit.setText(selectedItem);
                for (int i4 = 0; i4 < AddProductActivity.this.weightUnitNames.size(); i4++) {
                    if (AddProductActivity.this.weightUnitNames.get(i4).equalsIgnoreCase(selectedItem)) {
                        weight_unit_id = (String) ((HashMap) weightUnit.get(i4)).get(Constant.WEIGHT_ID);
                    }
                }
                AddProductActivity.this.selectedWeightUnitID = weight_unit_id;
                Log.d(Constant.WEIGHT_UNIT, AddProductActivity.this.selectedWeightUnitID);
            });
        });

        this.binding.etSupplier.setOnClickListener(view -> {
            AddProductActivity.this.supplierAdapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_list_item_1);
            AddProductActivity.this.supplierAdapter.addAll(AddProductActivity.this.supplierNames);

            AlertDialog.Builder dialog = new AlertDialog.Builder(AddProductActivity.this);
            View dialogView = AddProductActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, (ViewGroup) null);
            dialog.setView(dialogView);
            dialog.setCancelable(false);

            TextView title = dialogView.findViewById(R.id.tv_dialog_title);
            EditText search = dialogView.findViewById(R.id.et_dialog_search);
            ListView dialogListView = dialogView.findViewById(R.id.dialog_listView);
            Button btnCancel = dialogView.findViewById(R.id.btn_dialog_cancel);

            title.setText(R.string.suppliers);
            dialogListView.setVerticalScrollBarEnabled(true);
            dialogListView.setAdapter((ListAdapter) AddProductActivity.this.supplierAdapter);
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    AddProductActivity.this.supplierAdapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            final AlertDialog alertDialog = dialog.create();
            btnCancel.setOnClickListener(view12 -> alertDialog.dismiss());
            alertDialog.show();
            dialogListView.setOnItemClickListener((adapterView, view111, i, l) -> {
                alertDialog.dismiss();
                String selectedItem = AddProductActivity.this.supplierAdapter.getItem(i);
                String supplier_id = "0";
                AddProductActivity.this.binding.etSupplier.setText(selectedItem);
                for (int i5 = 0; i5 < AddProductActivity.this.supplierNames.size(); i5++) {
                    if (AddProductActivity.this.supplierNames.get(i5).equalsIgnoreCase(selectedItem)) {
                        supplier_id = (String) ((HashMap) productSupplier.get(i5)).get(Constant.SUPPLIERS_ID);
                    }
                }
                AddProductActivity.this.selectedSupplierID = supplier_id;
                Log.d(Constant.SUPPLIERS_ID, selectedSupplierID);
            });
        });

        this.binding.tvAddProduct.setOnClickListener(view -> {
            String product_name = AddProductActivity.this.binding.etProductName.getText().toString();
            String product_code = AddProductActivity.this.binding.etProductCode.getText().toString();
            String product_categoryName = AddProductActivity.this.binding.etProductCategory.getText().toString();
            String product_categoryID = AddProductActivity.this.selectedCategoryID;
            String product_description = AddProductActivity.this.binding.etProductDescription.getText().toString();
            String product_buyPrice = AddProductActivity.this.binding.etProductBuyPrice.getText().toString();
            String product_sellPrice = AddProductActivity.this.binding.etProductSellPrice.getText().toString();
            String product_stock = AddProductActivity.this.binding.etProductStock.getText().toString();
            String product_weight = AddProductActivity.this.binding.etProductWeight.getText().toString();
            String product_weightUnitName = AddProductActivity.this.binding.etProductWeightUnit.getText().toString();
            String product_weightUnitID = AddProductActivity.this.selectedWeightUnitID;
            String product_supplierName = AddProductActivity.this.binding.etSupplier.getText().toString();
            String product_supplierID = AddProductActivity.this.selectedSupplierID;

            if (product_name.isEmpty()) {
                AddProductActivity.this.binding.etProductName.setError(AddProductActivity.this.getString(R.string.product_name_cannot_be_empty));
                AddProductActivity.this.binding.etProductName.requestFocus();
                return;
            }
            if (!product_categoryName.isEmpty() && !product_categoryID.isEmpty()) {
                if (product_sellPrice.isEmpty()) {
                    AddProductActivity.this.binding.etProductSellPrice.setError(AddProductActivity.this.getString(R.string.product_sell_price_cannot_be_empty));
                    AddProductActivity.this.binding.etProductSellPrice.requestFocus();
                    return;
                }
                if (!product_weightUnitName.isEmpty() && !product_weight.isEmpty()) {
                    if (product_stock.isEmpty()) {
                        AddProductActivity.this.binding.etProductStock.setError(AddProductActivity.this.getString(R.string.product_stock_cannot_be_empty));
                        AddProductActivity.this.binding.etProductStock.requestFocus();
                        return;
                    }
                    if (!product_supplierName.isEmpty() && !product_supplierID.isEmpty()) {
                        DatabaseAccess databaseAccess1 = DatabaseAccess.getInstance(AddProductActivity.this);
                        databaseAccess1.open();

                        boolean check = databaseAccess1.addProduct(product_name, product_code, product_categoryID, product_description, product_buyPrice, product_sellPrice, product_stock, product_supplierID, AddProductActivity.this.encodedImage, product_weightUnitID, product_weight);
                        if (check) {
                            Toasty.success(AddProductActivity.this, R.string.product_successfully_added, Toasty.LENGTH_SHORT).show();
                            Intent i = new Intent(AddProductActivity.this, ProductActivity.class);
                            //i.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                            AddProductActivity.this.startActivity(i);
                            return;
                        }
                        Toasty.error(AddProductActivity.this, R.string.failed, Toasty.LENGTH_SHORT).show();
                        return;
                    }
                    AddProductActivity.this.binding.etSupplier.setError(AddProductActivity.this.getString(R.string.product_supplier_cannot_be_empty));
                    AddProductActivity.this.binding.etSupplier.requestFocus();
                    return;
                }
                AddProductActivity.this.binding.etProductWeight.setError(AddProductActivity.this.getString(R.string.product_weight_cannot_be_empty));
                AddProductActivity.this.binding.etProductWeight.requestFocus();
                return;
            }
            AddProductActivity.this.binding.etProductCategory.setError(AddProductActivity.this.getString(R.string.product_category_cannot_be_empty));
            AddProductActivity.this.binding.etProductCategory.requestFocus();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK && data != null) {
            try {
                String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
                this.mediaPath = filePath;
                Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                this.binding.ivProduct.setImageBitmap(selectedImage);
                this.encodedImage = encodeImage(selectedImage);
            } catch (Exception e) {
                Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
            }
        }
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_product_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_import) {
            fileChooser();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void fileChooser() {
        new ChooserDialog((Activity) this)
                .displayPath(true)
                .withFilter(false, false, "xls")
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String dir, File dirFile) {
                        AddProductActivity.this.onImport(dir);
                    }
                }).withOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.cancel();
                Log.d("CANCEL", "CANCEL");
            }
        }).build().show();
    }

    public void onImport(String path) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(this, R.string.no_file_found, Toast.LENGTH_SHORT).show();
            return;
        }
        ExcelToSQLite excelToSQLite = new ExcelToSQLite(getApplicationContext(), DatabaseOpenHelper.DATABASE_NAME, false);
        excelToSQLite.importFromFile(path, new ExcelToSQLite.ImportListener() {
            @Override
            public void onStart() {
                AddProductActivity.this.loading = new ProgressDialog(AddProductActivity.this);
                AddProductActivity.this.loading.setMessage(AddProductActivity.this.getString(R.string.data_importing_please_wait));
                AddProductActivity.this.loading.setCancelable(false);
                AddProductActivity.this.loading.show();
            }

            @Override
            public void onCompleted(String dbName) {
                Handler mHand = new Handler();
                mHand.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AddProductActivity.this.loading.dismiss();
                        Toasty.success(AddProductActivity.this, R.string.data_successfully_imported, Toasty.LENGTH_SHORT).show();
                        Intent i = new Intent(AddProductActivity.this, DashboardActivity.class);
                        AddProductActivity.this.startActivity(i);
                        AddProductActivity.this.finish();
                    }
                }, 5000L);
            }

            @Override
            public void onError(Exception e) {
                AddProductActivity.this.loading.dismiss();
                Toasty.error(AddProductActivity.this, R.string.data_import_fail, Toasty.LENGTH_SHORT).show();
                Log.d("Error : ", "" + e.getMessage());
            }
        });
    }
}