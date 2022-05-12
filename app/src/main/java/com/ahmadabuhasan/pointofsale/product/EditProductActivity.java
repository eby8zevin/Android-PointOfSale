package com.ahmadabuhasan.pointofsale.product;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.internal.view.SupportMenu;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.databinding.ActivityEditProductBinding;
import com.ahmadabuhasan.pointofsale.utils.BaseActivity;
//import com.itextpdf.text.io.PagedChannelRandomAccessSource;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class EditProductActivity extends BaseActivity {

    public static EditText etProductCode;
    private ActivityEditProductBinding binding;

    ArrayAdapter<String> categoryAdapter;
    ArrayAdapter<String> weightUnitAdapter;
    ArrayAdapter<String> supplierAdapter;

    List<String> categoryNames;
    List<String> weightUnitNames;
    List<String> supplierNames;

    String encodedImage = "N/A";
    String productID;
    String selectedCategoryID;
    String selectedWeightUnitID;
    String selectedSupplierID;
    String mediaPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.product_details);

        etProductCode = findViewById(R.id.et_product_code);
        this.productID = getIntent().getExtras().getString(Constant.PRODUCT_ID);

        this.binding.etProductName.setEnabled(false);
        etProductCode.setEnabled(false);
        this.binding.ivScanCode.setEnabled(false);
        this.binding.etProductCategory.setEnabled(false);
        this.binding.etProductDescription.setEnabled(false);
        this.binding.etProductBuyPrice.setEnabled(false);
        this.binding.etProductSellPrice.setEnabled(false);
        this.binding.etProductStock.setEnabled(false);
        this.binding.etProductWeight.setEnabled(false);
        this.binding.etProductWeightUnit.setEnabled(false);
        this.binding.etSupplier.setEnabled(false);
        this.binding.tvChooseImage.setEnabled(false);
        this.binding.ivProduct.setEnabled(false);

        this.binding.tvUpdateProduct.setVisibility(View.GONE);
        this.binding.tvEditProduct.setOnClickListener(view -> {
            EditProductActivity.this.binding.etProductName.setEnabled(true);
            EditProductActivity.etProductCode.setEnabled(true);
            EditProductActivity.this.binding.ivScanCode.setEnabled(true);
            EditProductActivity.this.binding.etProductCategory.setEnabled(true);
            EditProductActivity.this.binding.etProductDescription.setEnabled(true);
            EditProductActivity.this.binding.etProductBuyPrice.setEnabled(true);
            EditProductActivity.this.binding.etProductSellPrice.setEnabled(true);
            EditProductActivity.this.binding.etProductStock.setEnabled(true);
            EditProductActivity.this.binding.etProductWeight.setEnabled(true);
            EditProductActivity.this.binding.etProductWeightUnit.setEnabled(true);
            EditProductActivity.this.binding.etSupplier.setEnabled(true);
            EditProductActivity.this.binding.tvChooseImage.setEnabled(true);
            EditProductActivity.this.binding.ivProduct.setEnabled(true);

            EditProductActivity.this.binding.etProductName.setText(SupportMenu.CATEGORY_MASK);
            EditProductActivity.etProductCode.setText(SupportMenu.CATEGORY_MASK);
            EditProductActivity.this.binding.etProductCategory.setText(SupportMenu.CATEGORY_MASK);
            EditProductActivity.this.binding.etProductDescription.setText(SupportMenu.CATEGORY_MASK);
            EditProductActivity.this.binding.etProductBuyPrice.setText(SupportMenu.CATEGORY_MASK);
            EditProductActivity.this.binding.etProductSellPrice.setText(SupportMenu.CATEGORY_MASK);
            EditProductActivity.this.binding.etProductStock.setText(SupportMenu.CATEGORY_MASK);
            EditProductActivity.this.binding.etProductWeight.setText(SupportMenu.CATEGORY_MASK);
            EditProductActivity.this.binding.etProductWeightUnit.setText(SupportMenu.CATEGORY_MASK);
            EditProductActivity.this.binding.etSupplier.setText(SupportMenu.CATEGORY_MASK);

            EditProductActivity.this.binding.tvEditProduct.setVisibility(View.GONE);
            EditProductActivity.this.binding.tvUpdateProduct.setVisibility(View.VISIBLE);
        });

        this.binding.ivScanCode.setOnClickListener(view -> EditProductActivity.this.startActivity(new Intent(EditProductActivity.this, EditProductScannerViewActivity.class)));

        this.binding.tvChooseImage.setOnClickListener(view -> {
            Intent i = new Intent(EditProductActivity.this, ImageSelectActivity.class);
            i.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);
            i.putExtra(ImageSelectActivity.FLAG_CAMERA, true);
            i.putExtra(ImageSelectActivity.FLAG_GALLERY, true);
            EditProductActivity.this.startActivityForResult(i, 1213);
        });

        this.binding.ivProduct.setOnClickListener(view -> {
            Intent i = new Intent(EditProductActivity.this, ImageSelectActivity.class);
            i.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);
            i.putExtra(ImageSelectActivity.FLAG_CAMERA, true);
            i.putExtra(ImageSelectActivity.FLAG_GALLERY, true);
            startActivityForResult(i, 1213);
        });

        this.categoryNames = new ArrayList<>();
        this.weightUnitNames = new ArrayList<>();
        this.supplierNames = new ArrayList<>();

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        List<HashMap<String, String>> productData = databaseAccess.getProductsInfo(this.productID);
        String product_categoryID = productData.get(0).get(Constant.PRODUCT_CATEGORY);
        String product_weightUnitID = productData.get(0).get(Constant.PRODUCT_WEIGHT_UNIT_ID);
        String product_supplierID = productData.get(0).get(Constant.PRODUCT_SUPPLIER);

        this.binding.etProductName.setText(productData.get(0).get(Constant.PRODUCT_NAME));
        this.binding.etProductCode.setText(productData.get(0).get(Constant.PRODUCT_CODE));
        databaseAccess.open();
        this.binding.etProductCategory.setText(databaseAccess.getCategoryName(product_categoryID));
        this.binding.etProductDescription.setText(productData.get(0).get(Constant.PRODUCT_DESCRIPTION));
        this.binding.etProductBuyPrice.setText(productData.get(0).get(Constant.PRODUCT_BUY_PRICE));
        this.binding.etProductSellPrice.setText(productData.get(0).get(Constant.PRODUCT_SELL_PRICE));
        this.binding.etProductStock.setText(productData.get(0).get(Constant.PRODUCT_STOCK));
        this.binding.etProductWeight.setText(productData.get(0).get(Constant.PRODUCT_WEIGHT));
        databaseAccess.open();
        this.binding.etProductWeightUnit.setText(databaseAccess.getWeightUnitName(product_weightUnitID));
        databaseAccess.open();
        this.binding.etSupplier.setText(databaseAccess.getSupplierName(product_supplierID));

        String product_image = productData.get(0).get(Constant.PRODUCT_IMAGE);
        if (product_image != null) {
            if (product_image.length() < 6) {
                this.binding.ivProduct.setImageResource(R.drawable.image_placeholder);
            } else {
                byte[] bytes = Base64.decode(product_image, Base64.DEFAULT);
                this.binding.ivProduct.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        }
        this.encodedImage = product_image;

        this.selectedCategoryID = product_categoryID;
        this.selectedWeightUnitID = product_weightUnitID;
        this.selectedSupplierID = product_supplierID;

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
            EditProductActivity.this.categoryAdapter = new ArrayAdapter<>(EditProductActivity.this, android.R.layout.simple_list_item_1);
            EditProductActivity.this.categoryAdapter.addAll(EditProductActivity.this.categoryNames);

            AlertDialog.Builder dialog = new AlertDialog.Builder(EditProductActivity.this);
            View dialogView = EditProductActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, null);
            dialog.setView(dialogView);
            dialog.setCancelable(false);

            TextView title = dialogView.findViewById(R.id.tv_dialog_title);
            EditText search = dialogView.findViewById(R.id.et_dialog_search);
            ListView dialogListView = dialogView.findViewById(R.id.dialog_listView);
            Button btnCancel = dialogView.findViewById(R.id.btn_dialog_cancel);

            title.setText(R.string.product_category);
            dialogListView.setVerticalScrollBarEnabled(true);
            dialogListView.setAdapter(EditProductActivity.this.categoryAdapter);
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    EditProductActivity.this.categoryAdapter.getFilter().filter(charSequence);
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
                String selectedItem = EditProductActivity.this.categoryAdapter.getItem(i);
                String category_id = "0";
                EditProductActivity.this.binding.etProductCategory.setText(selectedItem);
                for (int i3 = 0; i3 < EditProductActivity.this.categoryNames.size(); i3++) {
                    if (EditProductActivity.this.categoryNames.get(i3).equalsIgnoreCase(selectedItem)) {
                        category_id = (String) ((HashMap) productCategory.get(i3)).get(Constant.CATEGORY_ID);
                    }
                }
                EditProductActivity.this.selectedCategoryID = category_id;
                Log.d(Constant.CATEGORY_ID, category_id);
            });
        });

        this.binding.etProductWeightUnit.setOnClickListener(view -> {
            EditProductActivity.this.weightUnitAdapter = new ArrayAdapter<>(EditProductActivity.this, android.R.layout.simple_list_item_1);
            EditProductActivity.this.weightUnitAdapter.addAll(EditProductActivity.this.weightUnitNames);

            AlertDialog.Builder dialog = new AlertDialog.Builder(EditProductActivity.this);
            View dialogView = EditProductActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, null);
            dialog.setView(dialogView);
            dialog.setCancelable(false);

            TextView title = dialogView.findViewById(R.id.tv_dialog_title);
            EditText search = dialogView.findViewById(R.id.et_dialog_search);
            ListView dialogListView = dialogView.findViewById(R.id.dialog_listView);
            Button btnCancel = dialogView.findViewById(R.id.btn_dialog_cancel);

            title.setText(R.string.product_weight_unit);
            dialogListView.setVerticalScrollBarEnabled(true);
            dialogListView.setAdapter(EditProductActivity.this.weightUnitAdapter);
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    EditProductActivity.this.weightUnitAdapter.getFilter().filter(charSequence);
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
                String selectedItem = EditProductActivity.this.weightUnitAdapter.getItem(i);
                String weight_unit_id = "0";
                EditProductActivity.this.binding.etProductWeightUnit.setText(selectedItem);
                for (int i4 = 0; i4 < EditProductActivity.this.weightUnitNames.size(); i4++) {
                    if (EditProductActivity.this.weightUnitNames.get(i4).equalsIgnoreCase(selectedItem)) {
                        weight_unit_id = (String) ((HashMap) weightUnit.get(i4)).get(Constant.WEIGHT_ID);
                    }
                }
                EditProductActivity.this.selectedWeightUnitID = weight_unit_id;
                Log.d(Constant.WEIGHT_UNIT, EditProductActivity.this.selectedWeightUnitID);
            });
        });

        this.binding.etSupplier.setOnClickListener(view -> {
            EditProductActivity.this.supplierAdapter = new ArrayAdapter<>(EditProductActivity.this, android.R.layout.simple_list_item_1);
            EditProductActivity.this.supplierAdapter.addAll(EditProductActivity.this.supplierNames);

            AlertDialog.Builder dialog = new AlertDialog.Builder(EditProductActivity.this);
            View dialogView = EditProductActivity.this.getLayoutInflater().inflate(R.layout.dialog_list_search, null);
            dialog.setView(dialogView);
            dialog.setCancelable(false);

            TextView title = dialogView.findViewById(R.id.tv_dialog_title);
            EditText search = dialogView.findViewById(R.id.et_dialog_search);
            ListView dialogListView = dialogView.findViewById(R.id.dialog_listView);
            Button btnCancel = dialogView.findViewById(R.id.btn_dialog_cancel);

            title.setText(R.string.suppliers);
            dialogListView.setVerticalScrollBarEnabled(true);
            dialogListView.setAdapter(EditProductActivity.this.supplierAdapter);
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    EditProductActivity.this.supplierAdapter.getFilter().filter(charSequence);
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
                String selectedItem = EditProductActivity.this.supplierAdapter.getItem(i);
                String supplier_id = "0";
                EditProductActivity.this.binding.etSupplier.setText(selectedItem);
                for (int i5 = 0; i5 < EditProductActivity.this.supplierNames.size(); i5++) {
                    if (EditProductActivity.this.supplierNames.get(i5).equalsIgnoreCase(selectedItem)) {
                        supplier_id = (String) ((HashMap) productSupplier.get(i5)).get(Constant.SUPPLIERS_ID);
                    }
                }
                EditProductActivity.this.selectedSupplierID = supplier_id;
                Log.d(Constant.SUPPLIERS_ID, selectedSupplierID);
            });
        });

        this.binding.tvUpdateProduct.setOnClickListener(view -> {
            String product_name = EditProductActivity.this.binding.etProductName.getText().toString();
            String product_code = EditProductActivity.this.binding.etProductCode.getText().toString();
            String product_categoryID1 = EditProductActivity.this.selectedCategoryID;
            String product_description = EditProductActivity.this.binding.etProductDescription.getText().toString();
            String product_buyPrice = EditProductActivity.this.binding.etProductBuyPrice.getText().toString();
            String product_sellPrice = EditProductActivity.this.binding.etProductSellPrice.getText().toString();
            String product_stock = EditProductActivity.this.binding.etProductStock.getText().toString();
            String product_weight = EditProductActivity.this.binding.etProductWeight.getText().toString();
            String product_weightUnitID1 = EditProductActivity.this.selectedWeightUnitID;
            String product_supplierID1 = EditProductActivity.this.selectedSupplierID;

            if (product_name.isEmpty()) {
                EditProductActivity.this.binding.etProductName.setError(EditProductActivity.this.getString(R.string.product_name_cannot_be_empty));
                EditProductActivity.this.binding.etProductName.requestFocus();
            } else if (product_categoryID1.isEmpty()) {
                EditProductActivity.this.binding.etProductCategory.setError(EditProductActivity.this.getString(R.string.product_category_cannot_be_empty));
                EditProductActivity.this.binding.etProductCategory.requestFocus();
            } else if (product_sellPrice.isEmpty()) {
                EditProductActivity.this.binding.etProductSellPrice.setError(EditProductActivity.this.getString(R.string.product_sell_price_cannot_be_empty));
                EditProductActivity.this.binding.etProductSellPrice.requestFocus();
            } else if (product_stock.isEmpty()) {
                EditProductActivity.this.binding.etProductStock.setError(EditProductActivity.this.getString(R.string.product_stock_cannot_be_empty));
                EditProductActivity.this.binding.etProductStock.requestFocus();
            } else if (product_weight.isEmpty()) {
                EditProductActivity.this.binding.etProductWeight.setError(EditProductActivity.this.getString(R.string.product_weight_cannot_be_empty));
                EditProductActivity.this.binding.etProductWeight.requestFocus();
            } else if (product_supplierID1.isEmpty()) {
                EditProductActivity.this.binding.etSupplier.setError(EditProductActivity.this.getString(R.string.product_supplier_cannot_be_empty));
                EditProductActivity.this.binding.etSupplier.requestFocus();
            } else {
                DatabaseAccess databaseAccess1 = DatabaseAccess.getInstance(EditProductActivity.this);
                databaseAccess1.open();

                boolean check = databaseAccess1.updateProduct(product_name, product_code, product_categoryID1, product_description, product_buyPrice, product_sellPrice, product_stock, product_supplierID1, EditProductActivity.this.encodedImage, product_weightUnitID1, product_weight, EditProductActivity.this.productID);
                if (check) {
                    Toasty.success(EditProductActivity.this, R.string.update_successfully, Toasty.LENGTH_SHORT).show();
                    Intent i = new Intent(EditProductActivity.this, ProductActivity.class);
                    //i.addFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
                    EditProductActivity.this.startActivity(i);
                    return;
                }
                Toasty.error(EditProductActivity.this, R.string.failed, Toasty.LENGTH_SHORT).show();
            }
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }
}