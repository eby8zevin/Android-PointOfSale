package com.ahmadabuhasan.pointofsale.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ahmadabuhasan.pointofsale.Constant;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseAccess {

    private static DatabaseAccess INSTANCE;
    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseAccess(context);
        }
        return INSTANCE;
    }

    public void open() {
        this.database = this.openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            database.close();
        }
    }

    public String getCurrency() {
        String currency = "n/a";
        Cursor cursor = this.database.rawQuery("SELECT * FROM shop", null);
        if (cursor.moveToFirst()) {
            do {
                currency = cursor.getString(5);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return currency;
    }

    public String getSupplierName(String supplier_id) {
        String supplier_name = "n/a";
        Cursor cursor = database.rawQuery("SELECT * FROM suppliers WHERE suppliers_id=" + supplier_id + "", null);
        if (cursor.moveToFirst()) {
            do {
                supplier_name = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return supplier_name;
    }

    public Boolean deleteProduct(String product_id) {
        long check = this.database.delete("products", "product_id=?", new String[]{product_id});
        long delete = this.database.delete("product_cart", "product_id=?", new String[]{product_id});
        close();
        return check == 1;
    }

    public ArrayList<HashMap<String, String>> getProducts() {
        ArrayList<HashMap<String, String>> product = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM products ORDER BY product_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(Constant.PRODUCT_ID, cursor.getString(0));
                map.put(Constant.PRODUCT_NAME, cursor.getString(1));
                map.put(Constant.PRODUCT_CODE, cursor.getString(2));
                map.put(Constant.PRODUCT_CATEGORY, cursor.getString(3));
                map.put(Constant.PRODUCT_DESCRIPTION, cursor.getString(4));
                map.put(Constant.PRODUCT_BUY_PRICE, cursor.getString(5));
                map.put(Constant.PRODUCT_SELL_PRICE, cursor.getString(6));
                map.put(Constant.PRODUCT_SUPPLIER, cursor.getString(7));
                map.put(Constant.PRODUCT_IMAGE, cursor.getString(8));
                map.put(Constant.PRODUCT_STOCK, cursor.getString(9));
                map.put(Constant.PRODUCT_WEIGHT_UNIT_ID, cursor.getString(10));
                map.put(Constant.PRODUCT_WEIGHT, cursor.getString(11));

                product.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return product;
    }

}