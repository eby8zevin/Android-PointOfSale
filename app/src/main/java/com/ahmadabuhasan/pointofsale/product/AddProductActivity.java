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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ahmadabuhasan.pointofsale.Constant;
import com.ahmadabuhasan.pointofsale.DashboardActivity;
import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseAccess;
import com.ahmadabuhasan.pointofsale.database.DatabaseOpenHelper;
import com.ahmadabuhasan.pointofsale.databinding.ActivityAddProductBinding;
import com.ahmadabuhasan.pointofsale.databinding.DialogListSearchBinding;
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

public class AddProductActivity extends AppCompatActivity {

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

        this.binding.etProductCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                dialogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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
                    }
                });
            }
        });

        this.binding.etProductWeightUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        this.binding.etSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        this.binding.tvAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String encImage = Base64.encodeToString(bytes, Base64.DEFAULT);
        return encImage;
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