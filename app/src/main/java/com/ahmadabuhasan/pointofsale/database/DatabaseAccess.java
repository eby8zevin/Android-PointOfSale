package com.ahmadabuhasan.pointofsale.database;

import android.content.ContentValues;
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

    /*
        public boolean addCustomer(String customer_name, String customer_cell, String customer_email, String customer_address) {
            ContentValues values = new ContentValues();
            values.put(Constant.CUSTOMER_NAME, customer_name);
            values.put(Constant.CUSTOMER_CELL, customer_cell);
            values.put(Constant.CUSTOMER_EMAIL, customer_email);
            values.put(Constant.CUSTOMER_ADDRESS, customer_address);
            long check = this.database.insert("customers", null, values);
            this.database.close();
            if (check == -1) {
                return false;
            }
            return true;
        }

        public boolean addCategory(String category_name) {
            ContentValues values = new ContentValues();
            values.put(Constant.CATEGORY_NAME, category_name);
            long check = this.database.insert(Constant.PRODUCT_CATEGORY, null, values);
            this.database.close();
            if (check == -1) {
                return false;
            }
            return true;
        }

        public boolean addPaymentMethod(String payment_method_name) {
            ContentValues values = new ContentValues();
            values.put(Constant.PAYMENT_METHOD_NAME, payment_method_name);
            long check = this.database.insert("payment_method", null, values);
            this.database.close();
            if (check == -1) {
                return false;
            }
            return true;
        }

        public boolean updateCategory(String category_id, String category_name) {
            ContentValues values = new ContentValues();
            values.put(Constant.CATEGORY_NAME, category_name);
            long check = (long) this.database.update(Constant.PRODUCT_CATEGORY, values, "category_id=? ", new String[]{category_id});
            this.database.close();
            return check != -1;
        }

        public boolean updatePaymentMethod(String payment_method_id, String payment_method_name) {
            ContentValues values = new ContentValues();
            values.put(Constant.PAYMENT_METHOD_NAME, payment_method_name);
            long check = (long) this.database.update("payment_method", values, "payment_method_id=? ", new String[]{payment_method_id});
            this.database.close();
            return check != -1;
        }

        public boolean updateCustomer(String customer_id, String customer_name, String customer_cell, String customer_email, String customer_address) {
            ContentValues values = new ContentValues();
            values.put(Constant.CUSTOMER_NAME, customer_name);
            values.put(Constant.CUSTOMER_CELL, customer_cell);
            values.put(Constant.CUSTOMER_EMAIL, customer_email);
            values.put(Constant.CUSTOMER_ADDRESS, customer_address);
            long check = (long) this.database.update("customers", values, " customer_id=? ", new String[]{customer_id});
            this.database.close();
            return check != -1;
        }

        public boolean updateShopInformation(String shop_name, String shop_contact, String shop_email, String shop_address, String shop_currency, String tax) {
            ContentValues values = new ContentValues();
            values.put("shop_name", shop_name);
            values.put("shop_contact", shop_contact);
            values.put("shop_email", shop_email);
            values.put("shop_address", shop_address);
            values.put("shop_currency", shop_currency);
            values.put(Constant.TAX, tax);
            long check = (long) this.database.update("shop", values, "shop_id=? ", new String[]{"1"});
            this.database.close();
            return check != -1;
        }

        public boolean addProduct(String product_name, String product_code, String product_category, String product_description, String product_buy_price, String product_sell_price, String product_stock, String product_supplier, String product_image, String weight_unit_id, String product_weight) {
            ContentValues values = new ContentValues();
            values.put(Constant.PRODUCT_NAME, product_name);
            values.put(Constant.PRODUCT_CODE, product_code);
            values.put(Constant.PRODUCT_CATEGORY, product_category);
            values.put(Constant.PRODUCT_DESCRIPTION, product_description);
            values.put("product_buy_price", product_buy_price);
            values.put(Constant.PRODUCT_SELL_PRICE, product_sell_price);
            values.put(Constant.PRODUCT_SUPPLIER, product_supplier);
            values.put(Constant.PRODUCT_IMAGE, product_image);
            values.put("product_stock", product_stock);
            values.put(Constant.PRODUCT_WEIGHT_UNIT_ID, weight_unit_id);
            values.put(Constant.PRODUCT_WEIGHT, product_weight);
            long check = this.database.insert("products", null, values);
            this.database.close();
            if (check == -1) {
                return false;
            }
            return true;
        }

        public boolean updateProduct(String product_name, String product_code, String product_category, String product_description, String product_buy_price, String product_sell_price, String product_stock, String product_supplier, String product_image, String weight_unit_id, String product_weight, String product_id) {
            ContentValues values = new ContentValues();
            values.put(Constant.PRODUCT_NAME, product_name);
            values.put(Constant.PRODUCT_CODE, product_code);
            values.put(Constant.PRODUCT_CATEGORY, product_category);
            values.put(Constant.PRODUCT_DESCRIPTION, product_description);
            values.put("product_buy_price", product_buy_price);
            values.put(Constant.PRODUCT_SELL_PRICE, product_sell_price);
            values.put(Constant.PRODUCT_SUPPLIER, product_supplier);
            values.put(Constant.PRODUCT_IMAGE, product_image);
            values.put("product_stock", product_stock);
            values.put(Constant.PRODUCT_WEIGHT_UNIT_ID, weight_unit_id);
            values.put(Constant.PRODUCT_WEIGHT, product_weight);
            SQLiteDatabase sQLiteDatabase = this.database;
            String[] strArr = {product_id};
            this.database.close();
            if (((long) sQLiteDatabase.update("products", values, "product_id=?", strArr)) == -1) {
                return false;
            }
            return true;
        }

        public boolean addExpense(String expense_name, String expense_amount, String expense_note, String date, String time) {
            ContentValues values = new ContentValues();
            values.put(Constant.EXPENSE_NAME, expense_name);
            values.put(Constant.EXPENSE_AMOUNT, expense_amount);
            values.put(Constant.EXPENSE_NOTE, expense_note);
            values.put(Constant.EXPENSE_DATE, date);
            values.put(Constant.EXPENSE_TIME, time);
            long check = this.database.insert("expense", null, values);
            this.database.close();
            if (check == -1) {
                return false;
            }
            return true;
        }

        public boolean updateExpense(String expense_id, String expense_name, String expense_amount, String expense_note, String date, String time) {
            ContentValues values = new ContentValues();
            values.put(Constant.EXPENSE_NAME, expense_name);
            values.put(Constant.EXPENSE_AMOUNT, expense_amount);
            values.put(Constant.EXPENSE_NOTE, expense_note);
            values.put(Constant.EXPENSE_DATE, date);
            values.put(Constant.EXPENSE_TIME, time);
            long check = (long) this.database.update("expense", values, "expense_id=?", new String[]{expense_id});
            this.database.close();
            return check != -1;
        }

        public boolean addSuppliers(String suppliers_name, String suppliers_contact_person, String suppliers_cell, String suppliers_email, String suppliers_address) {
            ContentValues values = new ContentValues();
            values.put(Constant.SUPPLIERS_NAME, suppliers_name);
            values.put(Constant.SUPPLIERS_CONTACT_PERSON, suppliers_contact_person);
            values.put(Constant.SUPPLIERS_CELL, suppliers_cell);
            values.put(Constant.SUPPLIERS_EMAIL, suppliers_email);
            values.put(Constant.SUPPLIERS_ADDRESS, suppliers_address);
            long check = this.database.insert("suppliers", null, values);
            this.database.close();
            if (check == -1) {
                return false;
            }
            return true;
        }

        public boolean updateSuppliers(String suppliers_id, String suppliers_name, String suppliers_contact_person, String suppliers_cell, String suppliers_email, String suppliers_address) {
            ContentValues values = new ContentValues();
            values.put(Constant.SUPPLIERS_NAME, suppliers_name);
            values.put(Constant.SUPPLIERS_CONTACT_PERSON, suppliers_contact_person);
            values.put(Constant.SUPPLIERS_CELL, suppliers_cell);
            values.put(Constant.SUPPLIERS_EMAIL, suppliers_email);
            values.put(Constant.SUPPLIERS_ADDRESS, suppliers_address);
            long check = (long) this.database.update("suppliers", values, "suppliers_id=?", new String[]{suppliers_id});
            this.database.close();
            return check != -1;
        }

        public String getProductImage(String product_id) {
            String image = "n/a";
            SQLiteDatabase sQLiteDatabase = this.database;
            Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM products WHERE product_id='" + product_id + "'", null);
            if (cursor.moveToFirst()) {
                do {
                    image = cursor.getString(8);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return image;
        }

        public String getWeightUnitName(String weight_unit_id) {
            String weight_unit_name = "n/a";
            SQLiteDatabase sQLiteDatabase = this.database;
            Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM product_weight WHERE weight_id=" + weight_unit_id + "", null);
            if (cursor.moveToFirst()) {
                do {
                    weight_unit_name = cursor.getString(1);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return weight_unit_name;
        }
    */
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

    /*
        public String getCategoryName(String category_id) {
            String product_category = "n/a";
            SQLiteDatabase sQLiteDatabase = this.database;
            Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM product_category WHERE category_id=" + category_id + "", null);
            if (cursor.moveToFirst()) {
                do {
                    product_category = cursor.getString(1);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return product_category;
        }

        public int addToCart(String product_id, String weight, String weight_unit, String price, int qty, String stock) {
            SQLiteDatabase sQLiteDatabase = this.database;
            if (sQLiteDatabase.rawQuery("SELECT * FROM product_cart WHERE product_id='" + product_id + "'", null).getCount() >= 1) {
                return 2;
            }
            ContentValues values = new ContentValues();
            values.put(Constant.PRODUCT_ID, product_id);
            values.put(Constant.PRODUCT_WEIGHT, weight);
            values.put(Constant.PRODUCT_WEIGHT_UNIT, weight_unit);
            values.put(Constant.PRODUCT_PRICE, price);
            values.put(Constant.PRODUCT_QTY, Integer.valueOf(qty));
            values.put("stock", stock);
            long check = this.database.insert("product_cart", null, values);
            this.database.close();
            if (check == -1) {
                return -1;
            }
            return 1;
        }

        public ArrayList<HashMap<String, String>> getCartProduct() {
            ArrayList<HashMap<String, String>> product = new ArrayList<>();
            Cursor cursor = this.database.rawQuery("SELECT * FROM product_cart", null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constant.CART_ID, cursor.getString(0));
                    map.put(Constant.PRODUCT_ID, cursor.getString(1));
                    map.put(Constant.PRODUCT_WEIGHT, cursor.getString(2));
                    map.put(Constant.PRODUCT_WEIGHT_UNIT, cursor.getString(3));
                    map.put(Constant.PRODUCT_PRICE, cursor.getString(4));
                    map.put(Constant.PRODUCT_QTY, cursor.getString(5));
                    map.put("stock", cursor.getString(6));
                    product.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return product;
        }
    */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x00c2 A[Catch:{ JSONException -> 0x0199 }] */
/*    public void insertOrder(String order_id, JSONObject obj) {
        ContentValues values;
        JSONException e;
        JSONArray result;
        int i;
        String product_order_date;
        String product_id;
        int updated_stock;
        JSONException e2;
        String str = Constant.PENDING;
        String str2 = Constant.ORDER_STATUS;
        String str3 = Constant.PRODUCT_ORDER_DATE;
        String str4 = Constant.PRODUCT_IMAGE;
        String str5 = Constant.PRODUCT_PRICE;
        ContentValues values2 = new ContentValues();
        ContentValues values22 = new ContentValues();
        ContentValues values3 = new ContentValues();
        String str6 = Constant.PRODUCT_QTY;
        try {
            String order_date = obj.getString(Constant.ORDER_DATE);
            String order_time = obj.getString(Constant.ORDER_TIME);
            String order_type = obj.getString(Constant.ORDER_TYPE);
            String order_payment_method = obj.getString(Constant.ORDER_PAYMENT_METHOD);
            String customer_name = obj.getString(Constant.CUSTOMER_NAME);
            String tax = obj.getString(Constant.TAX);
            String discount = obj.getString(Constant.DISCOUNT);
            values = values2;
            try {
                values.put(Constant.INVOICE_ID, order_id);
                values.put(Constant.ORDER_DATE, order_date);
                values.put(Constant.ORDER_TIME, order_time);
                values.put(Constant.ORDER_TYPE, order_type);
                values.put(Constant.ORDER_PAYMENT_METHOD, order_payment_method);
                values.put(Constant.CUSTOMER_NAME, customer_name);
                values.put(Constant.TAX, tax);
                values.put(Constant.DISCOUNT, discount);
                values.put(str2, str);
                this.database.insert("order_list", null, values);
                this.database.delete("product_cart", null, null);
            } catch (JSONException e3) {
                e2 = e3;
            }
        } catch (JSONException e4) {
            e2 = e4;
            values = values2;
            e2.printStackTrace();
            result = obj.getJSONArray("lines");
            i = 0;
            while (i < result.length()) {
            }
            this.database.close();
        }
        try {
            result = obj.getJSONArray("lines");
            i = 0;
            while (i < result.length()) {
                JSONObject jo = result.getJSONObject(i);
                String product_name = jo.getString(Constant.PRODUCT_NAME);
                String product_weight = jo.getString(Constant.PRODUCT_WEIGHT);
                String product_qty = jo.getString(str6);
                String product_price = jo.getString(str5);
                String product_image = jo.getString(str4);
                try {
                    product_order_date = jo.getString(str3);
                    product_id = jo.getString(Constant.PRODUCT_ID);
                    updated_stock = Integer.parseInt(jo.getString("stock")) - Integer.parseInt(product_qty);
                } catch (JSONException e5) {
                    e = e5;
                    e.printStackTrace();
                    this.database.close();
                }
                try {
                    values22.put(Constant.INVOICE_ID, order_id);
                    values22.put(Constant.PRODUCT_NAME, product_name);
                    values22.put(Constant.PRODUCT_WEIGHT, product_weight);
                    values22.put(str6, product_qty);
                    values22.put(str5, product_price);
                    values22.put(str4, product_image);
                    values22.put(str3, product_order_date);
                    values22.put(str2, str);
                } catch (JSONException e6) {
                    e = e6;
                    e.printStackTrace();
                    this.database.close();
                }
                try {
                    values3.put("product_stock", Integer.valueOf(updated_stock));
                    this.database.insert("order_details", null, values22);
                    try {
                        this.database.update("products", values3, "product_id=?", new String[]{product_id});
                        i++;
                        str6 = str6;
                        str5 = str5;
                        result = result;
                        str2 = str2;
                        values22 = values22;
                        values3 = values3;
                        str3 = str3;
                        values = values;
                        str4 = str4;
                        str = str;
                    } catch (JSONException e7) {
                        e = e7;
                        e.printStackTrace();
                        this.database.close();
                    }
                } catch (JSONException e8) {
                    e = e8;
                    e.printStackTrace();
                    this.database.close();
                }
            }
        } catch (JSONException e9) {
            e = e9;
            e.printStackTrace();
            this.database.close();
        }
        this.database.close();
    }

    public ArrayList<HashMap<String, String>> getOrderList() {
        ArrayList<HashMap<String, String>> orderList = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM order_list ORDER BY order_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(Constant.INVOICE_ID, cursor.getString(1));
                map.put(Constant.ORDER_DATE, cursor.getString(2));
                map.put(Constant.ORDER_TIME, cursor.getString(3));
                map.put(Constant.ORDER_TYPE, cursor.getString(4));
                map.put(Constant.ORDER_PAYMENT_METHOD, cursor.getString(5));
                map.put(Constant.CUSTOMER_NAME, cursor.getString(6));
                map.put(Constant.TAX, cursor.getString(7));
                map.put(Constant.DISCOUNT, cursor.getString(8));
                map.put(Constant.ORDER_STATUS, cursor.getString(cursor.getColumnIndex(Constant.ORDER_STATUS)));
                orderList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return orderList;
    }

    public ArrayList<HashMap<String, String>> searchOrderList(String s) {
        ArrayList<HashMap<String, String>> orderList = new ArrayList<>();
        SQLiteDatabase sQLiteDatabase = this.database;
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM order_list WHERE customer_name LIKE '%" + s + "%' OR invoice_id LIKE '%" + s + "%'  OR order_status LIKE '%" + s + "%' ORDER BY order_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(Constant.INVOICE_ID, cursor.getString(1));
                map.put(Constant.ORDER_DATE, cursor.getString(2));
                map.put(Constant.ORDER_TIME, cursor.getString(3));
                map.put(Constant.ORDER_TYPE, cursor.getString(4));
                map.put(Constant.ORDER_PAYMENT_METHOD, cursor.getString(5));
                map.put(Constant.CUSTOMER_NAME, cursor.getString(6));
                map.put(Constant.TAX, cursor.getString(7));
                map.put(Constant.DISCOUNT, cursor.getString(8));
                map.put(Constant.ORDER_STATUS, cursor.getString(cursor.getColumnIndex(Constant.ORDER_STATUS)));
                orderList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return orderList;
    }

    public ArrayList<HashMap<String, String>> getOrderDetailsList(String order_id) {
        ArrayList<HashMap<String, String>> orderDetailsList = new ArrayList<>();
        SQLiteDatabase sQLiteDatabase = this.database;
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM order_details WHERE invoice_id='" + order_id + "' ORDER BY order_details_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(Constant.PRODUCT_NAME, cursor.getString(2));
                map.put(Constant.PRODUCT_WEIGHT, cursor.getString(3));
                map.put(Constant.PRODUCT_QTY, cursor.getString(4));
                map.put(Constant.PRODUCT_PRICE, cursor.getString(5));
                map.put(Constant.PRODUCT_IMAGE, cursor.getString(6));
                orderDetailsList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return orderDetailsList;
    }

    public ArrayList<HashMap<String, String>> getAllSalesItems() {
        ArrayList<HashMap<String, String>> orderDetailsList = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM order_details  WHERE order_status='Completed' ORDER BY order_details_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(Constant.PRODUCT_NAME, cursor.getString(2));
                map.put(Constant.PRODUCT_WEIGHT, cursor.getString(3));
                map.put(Constant.PRODUCT_QTY, cursor.getString(4));
                map.put(Constant.PRODUCT_PRICE, cursor.getString(5));
                map.put(Constant.PRODUCT_IMAGE, cursor.getString(6));
                map.put(Constant.PRODUCT_ORDER_DATE, cursor.getString(7));
                orderDetailsList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return orderDetailsList;
    }

    public ArrayList<HashMap<String, String>> getSalesReport(String type) {
        ArrayList<HashMap<String, String>> orderDetailsList = new ArrayList<>();
        Cursor cursor = null;
        if (type.equals("all")) {
            cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed'   ORDER BY order_details_id DESC", null);
        } else if (type.equals(Constant.DAILY)) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
            SQLiteDatabase sQLiteDatabase = this.database;
            cursor = sQLiteDatabase.rawQuery("SELECT * FROM order_details  WHERE order_status='Completed' AND product_order_date='" + currentDate + "' ORDER BY order_Details_id DESC", null);
        } else if (type.equals(Constant.MONTHLY)) {
            String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM order_details  WHERE order_status='Completed' AND strftime('%m', product_order_date) = '" + currentMonth + "' ", null);
        } else if (type.equals(Constant.YEARLY)) {
            String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
            Log.d("YEAR", currentYear);
            cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed' AND strftime('%Y', product_order_date) = '" + currentYear + "' ", null);
        }
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(Constant.PRODUCT_NAME, cursor.getString(2));
                map.put(Constant.PRODUCT_WEIGHT, cursor.getString(3));
                map.put(Constant.PRODUCT_QTY, cursor.getString(4));
                map.put(Constant.PRODUCT_PRICE, cursor.getString(5));
                map.put(Constant.PRODUCT_IMAGE, cursor.getString(6));
                map.put(Constant.PRODUCT_ORDER_DATE, cursor.getString(7));
                orderDetailsList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return orderDetailsList;
    }

    public ArrayList<HashMap<String, String>> getExpenseReport(String type) {
        ArrayList<HashMap<String, String>> orderDetailsList = new ArrayList<>();
        Cursor cursor = null;
        if (type.equals("all")) {
            cursor = this.database.rawQuery("SELECT * FROM expense  ORDER BY expense_id DESC", null);
        } else if (type.equals(Constant.DAILY)) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
            SQLiteDatabase sQLiteDatabase = this.database;
            cursor = sQLiteDatabase.rawQuery("SELECT * FROM expense WHERE   expense_date='" + currentDate + "' ORDER BY expense_id DESC", null);
        } else if (type.equals(Constant.MONTHLY)) {
            String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM expense WHERE strftime('%m', expense_date) = '" + currentMonth + "' ", null);
        } else if (type.equals(Constant.YEARLY)) {
            String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
            cursor = this.database.rawQuery("SELECT * FROM expense WHERE strftime('%Y', expense_date) = '" + currentYear + "' ", null);
        }
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(Constant.EXPENSE_ID, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_ID)));
                map.put(Constant.EXPENSE_NAME, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_NAME)));
                map.put(Constant.EXPENSE_NOTE, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_NOTE)));
                map.put(Constant.EXPENSE_AMOUNT, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_AMOUNT)));
                map.put(Constant.EXPENSE_DATE, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_DATE)));
                map.put(Constant.EXPENSE_TIME, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_TIME)));
                orderDetailsList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return orderDetailsList;
    }

    public float getMonthlySalesAmount(String month, String getYear) {
        float total_price = 0.0f;
        Cursor cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed'  AND  strftime('%m', product_order_date) = '" + month + "' AND strftime('%Y', product_order_date) = '" + getYear + "'  ", null);
        if (cursor.moveToFirst()) {
            do {
                total_price += ((float) Integer.parseInt(cursor.getString(4))) * Float.parseFloat(cursor.getString(5));
            } while (cursor.moveToNext());
        } else {
            total_price = 0.0f;
        }
        cursor.close();
        this.database.close();
        Log.d("total_price", "" + total_price);
        return total_price;
    }

    public float getMonthlyExpenseAmount(String month, String getYear) {
        float total_cost = 0.0f;
        Cursor cursor = this.database.rawQuery("SELECT * FROM expense WHERE strftime('%m', expense_date) = '" + month + "' AND strftime('%Y', expense_date) = '" + getYear + "'  ", null);
        if (cursor.moveToFirst()) {
            do {
                total_cost += Float.parseFloat(cursor.getString(3));
            } while (cursor.moveToNext());
        } else {
            total_cost = 0.0f;
        }
        cursor.close();
        this.database.close();
        Log.d("total_price", "" + total_cost);
        return total_cost;
    }

    public boolean deleteProductFromCart(String id) {
        long check = (long) this.database.delete("product_cart", "cart_id=?", new String[]{id});
        this.database.close();
        return check == 1;
    }

    public ArrayList<HashMap<String, String>> getTabProducts(String category_id) {
        ArrayList<HashMap<String, String>> product = new ArrayList<>();
        SQLiteDatabase sQLiteDatabase = this.database;
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM products WHERE product_category = '" + category_id + "' ORDER BY product_id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put(Constant.PRODUCT_ID, cursor.getString(0));
                map.put(Constant.PRODUCT_NAME, cursor.getString(1));
                map.put(Constant.PRODUCT_CODE, cursor.getString(2));
                map.put(Constant.PRODUCT_CATEGORY, cursor.getString(3));
                map.put(Constant.PRODUCT_DESCRIPTION, cursor.getString(4));
                map.put("product_buy_price", cursor.getString(5));
                map.put(Constant.PRODUCT_SELL_PRICE, cursor.getString(6));
                map.put(Constant.PRODUCT_SUPPLIER, cursor.getString(7));
                map.put(Constant.PRODUCT_IMAGE, cursor.getString(8));
                map.put("product_stock", cursor.getString(9));
                map.put(Constant.PRODUCT_WEIGHT_UNIT_ID, cursor.getString(10));
                map.put(Constant.PRODUCT_WEIGHT, cursor.getString(11));
                product.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return product;
    }

    public int getCartItemCount() {
        Cursor cursor = this.database.rawQuery("SELECT * FROM product_cart", null);
        int itemCount = cursor.getCount();
        cursor.close();
        this.database.close();
        return itemCount;
    }

    public void updateProductQty(String id, String qty) {
        ContentValues values = new ContentValues();
        values.put(Constant.PRODUCT_QTY, qty);
        long update = (long) this.database.update("product_cart", values, "cart_id=?", new String[]{id});
    }

    public String getProductName(String product_id) {
        String product_name = "n/a";
        SQLiteDatabase sQLiteDatabase = this.database;
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM products WHERE product_id='" + product_id + "'", null);
        if (cursor.moveToFirst()) {
            do {
                product_name = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        cursor.close();
        this.database.close();
        return product_name;
    }
*/
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

    /*
        public double getTotalPrice() {
            double total_price = Utils.DOUBLE_EPSILON;
            Cursor cursor = this.database.rawQuery("SELECT * FROM product_cart", null);
            if (cursor.moveToFirst()) {
                do {
                    double price = Double.parseDouble(cursor.getString(4));
                    double parseInt = (double) Integer.parseInt(cursor.getString(5));
                    Double.isNaN(parseInt);
                    total_price += parseInt * price;
                } while (cursor.moveToNext());
            } else {
                total_price = Utils.DOUBLE_EPSILON;
            }
            cursor.close();
            this.database.close();
            return total_price;
        }

        public double getTotalDiscount(String type) {
            Cursor cursor;
            double total_discount = Utils.DOUBLE_EPSILON;
            if (type.equals(Constant.MONTHLY)) {
                String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
                cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed'  AND strftime('%m', order_date) = '" + currentMonth + "' ", null);
            } else if (type.equals(Constant.YEARLY)) {
                String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
                cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed'  AND strftime('%Y', order_date) = '" + currentYear + "' ", null);
            } else if (type.equals(Constant.DAILY)) {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
                SQLiteDatabase sQLiteDatabase = this.database;
                cursor = sQLiteDatabase.rawQuery("SELECT * FROM order_list WHERE order_status='Completed'  AND   order_date='" + currentDate + "' ORDER BY order_id DESC", null);
            } else {
                cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed'", null);
            }
            if (cursor.moveToFirst()) {
                do {
                    total_discount += Double.parseDouble(cursor.getString(cursor.getColumnIndex(Constant.DISCOUNT)));
                } while (cursor.moveToNext());
            } else {
                total_discount = Utils.DOUBLE_EPSILON;
            }
            cursor.close();
            this.database.close();
            return total_discount;
        }

        public double getTotalDiscountForGraph(String type, int currentYear) {
            Cursor cursor;
            double total_discount = Utils.DOUBLE_EPSILON;
            if (type.equals(Constant.MONTHLY)) {
                String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
                cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed'  AND strftime('%m', order_date) = '" + currentMonth + "' ", null);
            } else if (type.equals(Constant.YEARLY)) {
                cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed'  AND strftime('%Y', order_date) = '" + currentYear + "' ", null);
            } else if (type.equals(Constant.DAILY)) {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
                SQLiteDatabase sQLiteDatabase = this.database;
                cursor = sQLiteDatabase.rawQuery("SELECT * FROM order_list WHERE order_status='Completed'  AND   order_date='" + currentDate + "' ORDER BY order_id DESC", null);
            } else {
                cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed'", null);
            }
            if (cursor.moveToFirst()) {
                do {
                    total_discount += Double.parseDouble(cursor.getString(cursor.getColumnIndex(Constant.DISCOUNT)));
                } while (cursor.moveToNext());
            } else {
                total_discount = Utils.DOUBLE_EPSILON;
            }
            cursor.close();
            this.database.close();
            return total_discount;
        }

        public double getTotalTax(String type) {
            Cursor cursor;
            double total_tax = Utils.DOUBLE_EPSILON;
            if (type.equals(Constant.MONTHLY)) {
                String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
                cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed'  AND  strftime('%m', order_date) = '" + currentMonth + "' ", null);
            } else if (type.equals(Constant.YEARLY)) {
                String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
                cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed'  AND strftime('%Y', order_date) = '" + currentYear + "' ", null);
            } else if (type.equals(Constant.DAILY)) {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
                SQLiteDatabase sQLiteDatabase = this.database;
                cursor = sQLiteDatabase.rawQuery("SELECT * FROM order_list WHERE order_status='Completed'  AND   order_date='" + currentDate + "' ORDER BY order_id DESC", null);
            } else {
                cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed' ", null);
            }
            if (cursor.moveToFirst()) {
                do {
                    total_tax += Double.parseDouble(cursor.getString(cursor.getColumnIndex(Constant.TAX)));
                } while (cursor.moveToNext());
            } else {
                total_tax = Utils.DOUBLE_EPSILON;
            }
            cursor.close();
            this.database.close();
            return total_tax;
        }

        public double getTotalTaxForGraph(String type, int currentYear) {
            Cursor cursor;
            double total_tax = Utils.DOUBLE_EPSILON;
            if (type.equals(Constant.MONTHLY)) {
                String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
                cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed'  AND  strftime('%m', order_date) = '" + currentMonth + "' ", null);
            } else if (type.equals(Constant.YEARLY)) {
                cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed'  AND strftime('%Y', order_date) = '" + currentYear + "' ", null);
            } else if (type.equals(Constant.DAILY)) {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
                SQLiteDatabase sQLiteDatabase = this.database;
                cursor = sQLiteDatabase.rawQuery("SELECT * FROM order_list WHERE order_status='Completed'  AND   order_date='" + currentDate + "' ORDER BY order_id DESC", null);
            } else {
                cursor = this.database.rawQuery("SELECT * FROM order_list WHERE order_status='Completed' ", null);
            }
            if (cursor.moveToFirst()) {
                do {
                    total_tax += Double.parseDouble(cursor.getString(cursor.getColumnIndex(Constant.TAX)));
                } while (cursor.moveToNext());
            } else {
                total_tax = Utils.DOUBLE_EPSILON;
            }
            cursor.close();
            this.database.close();
            return total_tax;
        }

        public double getTotalOrderPrice(String type) {
            Cursor cursor;
            double total_price = Utils.DOUBLE_EPSILON;
            if (type.equals(Constant.MONTHLY)) {
                String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
                cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed'  AND strftime('%m', product_order_date) = '" + currentMonth + "' ", null);
            } else if (type.equals(Constant.YEARLY)) {
                String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
                cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed'  AND  strftime('%Y', product_order_date) = '" + currentYear + "' ", null);
            } else if (type.equals(Constant.DAILY)) {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
                SQLiteDatabase sQLiteDatabase = this.database;
                cursor = sQLiteDatabase.rawQuery("SELECT * FROM order_details WHERE order_status='Completed'  AND   product_order_date='" + currentDate + "' ORDER BY order_Details_id DESC", null);
            } else {
                cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed' ", null);
            }
            if (cursor.moveToFirst()) {
                do {
                    double price = Double.parseDouble(cursor.getString(5));
                    double parseInt = (double) Integer.parseInt(cursor.getString(4));
                    Double.isNaN(parseInt);
                    total_price += parseInt * price;
                } while (cursor.moveToNext());
            } else {
                total_price = Utils.DOUBLE_EPSILON;
            }
            cursor.close();
            this.database.close();
            return total_price;
        }

        public double getTotalOrderPriceForGraph(String type, int currentYear) {
            Cursor cursor;
            double total_price = Utils.DOUBLE_EPSILON;
            if (type.equals(Constant.MONTHLY)) {
                String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
                cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed'  AND strftime('%m', product_order_date) = '" + currentMonth + "' ", null);
            } else if (type.equals(Constant.YEARLY)) {
                cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed'  AND  strftime('%Y', product_order_date) = '" + currentYear + "' ", null);
            } else if (type.equals(Constant.DAILY)) {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
                SQLiteDatabase sQLiteDatabase = this.database;
                cursor = sQLiteDatabase.rawQuery("SELECT * FROM order_details WHERE order_status='Completed'  AND   product_order_date='" + currentDate + "' ORDER BY order_Details_id DESC", null);
            } else {
                cursor = this.database.rawQuery("SELECT * FROM order_details WHERE order_status='Completed' ", null);
            }
            if (cursor.moveToFirst()) {
                do {
                    double price = Double.parseDouble(cursor.getString(5));
                    double parseInt = (double) Integer.parseInt(cursor.getString(4));
                    Double.isNaN(parseInt);
                    total_price += parseInt * price;
                } while (cursor.moveToNext());
            } else {
                total_price = Utils.DOUBLE_EPSILON;
            }
            cursor.close();
            this.database.close();
            return total_price;
        }

        public double totalOrderPrice(String invoice_id) {
            double total_price = Utils.DOUBLE_EPSILON;
            SQLiteDatabase sQLiteDatabase = this.database;
            Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM order_details WHERE invoice_id='" + invoice_id + "'", null);
            if (cursor.moveToFirst()) {
                do {
                    double price = Double.parseDouble(cursor.getString(5));
                    double parseInt = (double) Integer.parseInt(cursor.getString(4));
                    Double.isNaN(parseInt);
                    total_price += parseInt * price;
                } while (cursor.moveToNext());
            } else {
                total_price = Utils.DOUBLE_EPSILON;
            }
            cursor.close();
            this.database.close();
            return total_price;
        }

        public double getTotalExpense(String type) {
            Cursor cursor;
            double total_cost = Utils.DOUBLE_EPSILON;
            if (type.equals(Constant.MONTHLY)) {
                String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
                cursor = this.database.rawQuery("SELECT * FROM expense WHERE strftime('%m', expense_date) = '" + currentMonth + "' ", null);
            } else if (type.equals(Constant.YEARLY)) {
                String currentYear = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date());
                cursor = this.database.rawQuery("SELECT * FROM expense WHERE strftime('%Y', expense_date) = '" + currentYear + "' ", null);
            } else if (type.equals(Constant.DAILY)) {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
                SQLiteDatabase sQLiteDatabase = this.database;
                cursor = sQLiteDatabase.rawQuery("SELECT * FROM expense WHERE   expense_date='" + currentDate + "' ORDER BY expense_id DESC", null);
            } else {
                cursor = this.database.rawQuery("SELECT * FROM expense", null);
            }
            if (cursor.moveToFirst()) {
                do {
                    total_cost += Double.parseDouble(cursor.getString(3));
                } while (cursor.moveToNext());
            } else {
                total_cost = Utils.DOUBLE_EPSILON;
            }
            cursor.close();
            this.database.close();
            return total_cost;
        }

        public double getTotalExpenseForGraph(String type, int currentYear) {
            Cursor cursor;
            double total_cost = Utils.DOUBLE_EPSILON;
            if (type.equals(Constant.MONTHLY)) {
                String currentMonth = new SimpleDateFormat("MM", Locale.ENGLISH).format(new Date());
                cursor = this.database.rawQuery("SELECT * FROM expense WHERE strftime('%m', expense_date) = '" + currentMonth + "' ", null);
            } else if (type.equals(Constant.YEARLY)) {
                cursor = this.database.rawQuery("SELECT * FROM expense WHERE strftime('%Y', expense_date) = '" + currentYear + "' ", null);
            } else if (type.equals(Constant.DAILY)) {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
                SQLiteDatabase sQLiteDatabase = this.database;
                cursor = sQLiteDatabase.rawQuery("SELECT * FROM expense WHERE   expense_date='" + currentDate + "' ORDER BY expense_id DESC", null);
            } else {
                cursor = this.database.rawQuery("SELECT * FROM expense", null);
            }
            if (cursor.moveToFirst()) {
                do {
                    total_cost += Double.parseDouble(cursor.getString(3));
                } while (cursor.moveToNext());
            } else {
                total_cost = Utils.DOUBLE_EPSILON;
            }
            cursor.close();
            this.database.close();
            return total_cost;
        }

        public ArrayList<HashMap<String, String>> getCustomers() {
            ArrayList<HashMap<String, String>> customer = new ArrayList<>();
            Cursor cursor = this.database.rawQuery("SELECT * FROM customers ORDER BY customer_id DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constant.CUSTOMER_ID, cursor.getString(0));
                    map.put(Constant.CUSTOMER_NAME, cursor.getString(1));
                    map.put(Constant.CUSTOMER_CELL, cursor.getString(2));
                    map.put(Constant.CUSTOMER_EMAIL, cursor.getString(3));
                    map.put(Constant.CUSTOMER_ADDRESS, cursor.getString(4));
                    customer.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return customer;
        }

        public ArrayList<HashMap<String, String>> getOrderType() {
            ArrayList<HashMap<String, String>> order_type = new ArrayList<>();
            Cursor cursor = this.database.rawQuery("SELECT * FROM order_type ORDER BY order_type_id DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("order_type_id", cursor.getString(0));
                    map.put("order_type_name", cursor.getString(1));
                    order_type.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return order_type;
        }

        public ArrayList<HashMap<String, String>> getPaymentMethod() {
            ArrayList<HashMap<String, String>> payment_method = new ArrayList<>();
            Cursor cursor = this.database.rawQuery("SELECT * FROM payment_method ORDER BY payment_method_id DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constant.PAYMENT_METHOD_ID, cursor.getString(0));
                    map.put(Constant.PAYMENT_METHOD_NAME, cursor.getString(1));
                    payment_method.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return payment_method;
        }

        public ArrayList<HashMap<String, String>> searchCustomers(String s) {
            ArrayList<HashMap<String, String>> customer = new ArrayList<>();
            SQLiteDatabase sQLiteDatabase = this.database;
            Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM customers WHERE customer_name LIKE '%" + s + "%' ORDER BY customer_id DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constant.CUSTOMER_ID, cursor.getString(0));
                    map.put(Constant.CUSTOMER_NAME, cursor.getString(1));
                    map.put(Constant.CUSTOMER_CELL, cursor.getString(2));
                    map.put(Constant.CUSTOMER_EMAIL, cursor.getString(3));
                    map.put(Constant.CUSTOMER_ADDRESS, cursor.getString(4));
                    customer.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return customer;
        }

        public ArrayList<HashMap<String, String>> searchSuppliers(String s) {
            ArrayList<HashMap<String, String>> customer = new ArrayList<>();
            SQLiteDatabase sQLiteDatabase = this.database;
            Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM suppliers WHERE suppliers_name LIKE '%" + s + "%' ORDER BY suppliers_id DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constant.SUPPLIERS_ID, cursor.getString(0));
                    map.put(Constant.SUPPLIERS_NAME, cursor.getString(1));
                    map.put(Constant.SUPPLIERS_CONTACT_PERSON, cursor.getString(2));
                    map.put(Constant.SUPPLIERS_CELL, cursor.getString(3));
                    map.put(Constant.SUPPLIERS_EMAIL, cursor.getString(4));
                    map.put(Constant.SUPPLIERS_ADDRESS, cursor.getString(5));
                    customer.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return customer;
        }

        public ArrayList<HashMap<String, String>> getShopInformation() {
            ArrayList<HashMap<String, String>> shop_info = new ArrayList<>();
            Cursor cursor = this.database.rawQuery("SELECT * FROM shop", null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("shop_name", cursor.getString(1));
                    map.put("shop_contact", cursor.getString(2));
                    map.put("shop_email", cursor.getString(3));
                    map.put("shop_address", cursor.getString(4));
                    map.put("shop_currency", cursor.getString(5));
                    map.put(Constant.TAX, cursor.getString(6));
                    shop_info.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return shop_info;
        }
    */
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

    /*
        public ArrayList<HashMap<String, String>> getProductsInfo(String product_id) {
            ArrayList<HashMap<String, String>> product = new ArrayList<>();
            SQLiteDatabase sQLiteDatabase = this.database;
            Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM products WHERE product_id='" + product_id + "'", null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constant.PRODUCT_ID, cursor.getString(0));
                    map.put(Constant.PRODUCT_NAME, cursor.getString(1));
                    map.put(Constant.PRODUCT_CODE, cursor.getString(2));
                    map.put(Constant.PRODUCT_CATEGORY, cursor.getString(3));
                    map.put(Constant.PRODUCT_DESCRIPTION, cursor.getString(4));
                    map.put("product_buy_price", cursor.getString(5));
                    map.put(Constant.PRODUCT_SELL_PRICE, cursor.getString(6));
                    map.put(Constant.PRODUCT_SUPPLIER, cursor.getString(7));
                    map.put(Constant.PRODUCT_IMAGE, cursor.getString(8));
                    map.put("product_stock", cursor.getString(9));
                    map.put(Constant.PRODUCT_WEIGHT_UNIT_ID, cursor.getString(10));
                    map.put(Constant.PRODUCT_WEIGHT, cursor.getString(11));
                    product.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return product;
        }

        public ArrayList<HashMap<String, String>> getAllExpense() {
            ArrayList<HashMap<String, String>> product = new ArrayList<>();
            Cursor cursor = this.database.rawQuery("SELECT * FROM expense ORDER BY expense_id DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constant.EXPENSE_ID, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_ID)));
                    map.put(Constant.EXPENSE_NAME, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_NAME)));
                    map.put(Constant.EXPENSE_NOTE, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_NOTE)));
                    map.put(Constant.EXPENSE_AMOUNT, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_AMOUNT)));
                    map.put(Constant.EXPENSE_DATE, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_DATE)));
                    map.put(Constant.EXPENSE_TIME, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_TIME)));
                    product.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return product;
        }

        public ArrayList<HashMap<String, String>> getProductCategory() {
            ArrayList<HashMap<String, String>> product_category = new ArrayList<>();
            Cursor cursor = this.database.rawQuery("SELECT * FROM product_category ORDER BY category_id DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constant.CATEGORY_ID, cursor.getString(0));
                    map.put(Constant.CATEGORY_NAME, cursor.getString(1));
                    product_category.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return product_category;
        }

        public ArrayList<HashMap<String, String>> searchProductCategory(String s) {
            ArrayList<HashMap<String, String>> product_category = new ArrayList<>();
            SQLiteDatabase sQLiteDatabase = this.database;
            Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM product_category WHERE category_name LIKE '%" + s + "%' ORDER BY category_id DESC ", null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constant.CATEGORY_ID, cursor.getString(0));
                    map.put(Constant.CATEGORY_NAME, cursor.getString(1));
                    product_category.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return product_category;
        }

        public ArrayList<HashMap<String, String>> searchPaymentMethod(String s) {
            ArrayList<HashMap<String, String>> payment_method = new ArrayList<>();
            SQLiteDatabase sQLiteDatabase = this.database;
            Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM payment_method WHERE payment_method_name LIKE '%" + s + "%' ORDER BY payment_method_id DESC ", null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constant.PAYMENT_METHOD_ID, cursor.getString(0));
                    map.put(Constant.PAYMENT_METHOD_NAME, cursor.getString(1));
                    payment_method.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return payment_method;
        }

        public ArrayList<HashMap<String, String>> getProductSupplier() {
            ArrayList<HashMap<String, String>> product_suppliers = new ArrayList<>();
            Cursor cursor = this.database.rawQuery("SELECT * FROM suppliers", null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constant.SUPPLIERS_ID, cursor.getString(0));
                    map.put(Constant.SUPPLIERS_NAME, cursor.getString(1));
                    product_suppliers.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return product_suppliers;
        }

        public ArrayList<HashMap<String, String>> getWeightUnit() {
            ArrayList<HashMap<String, String>> product_weight_unit = new ArrayList<>();
            Cursor cursor = this.database.rawQuery("SELECT * FROM product_weight", null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("weight_id", cursor.getString(0));
                    map.put("weight_unit", cursor.getString(1));
                    product_weight_unit.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            this.database.close();
            return product_weight_unit;
        }

        public ArrayList<HashMap<String, String>> searchExpense(String s) {
            ArrayList<HashMap<String, String>> product = new ArrayList<>();
            SQLiteDatabase sQLiteDatabase = this.database;
            Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM expense WHERE expense_name LIKE '%" + s + "%' ORDER BY expense_id DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constant.EXPENSE_ID, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_ID)));
                    map.put(Constant.EXPENSE_NAME, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_NAME)));
                    map.put(Constant.EXPENSE_NOTE, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_NOTE)));
                    map.put(Constant.EXPENSE_AMOUNT, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_AMOUNT)));
                    map.put(Constant.EXPENSE_DATE, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_DATE)));
                    map.put(Constant.EXPENSE_TIME, cursor.getString(cursor.getColumnIndex(Constant.EXPENSE_TIME)));
                    product.add(map);
                } while (cursor.moveToNext());
            }
            this.database.close();
            return product;
        }
    */
    public ArrayList<HashMap<String, String>> getSearchProducts(String search) {
        ArrayList<HashMap<String, String>> product = new ArrayList<>();
        Cursor cursor = this.database.rawQuery("SELECT * FROM products WHERE product_name LIKE '%" + search + "%' OR product_code LIKE '%" + search + "%' ORDER BY product_id DESC", null);
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

    /*
        public int addToCart(String product_name, String price, String weight, int qty, String base64Image, String ref, String tva_tx, String product_id) {
            SQLiteDatabase sQLiteDatabase = this.database;
            if (sQLiteDatabase.rawQuery("SELECT * FROM cart WHERE product_name='" + product_name + "' AND price='" + price + "' AND weight='" + weight + "'", null).getCount() >= 1) {
                return 2;
            }
            ContentValues values = new ContentValues();
            values.put(Constant.PRODUCT_NAME, product_name);
            values.put(Constant.PRICE, price);
            values.put("weight", weight);
            values.put("qty", Integer.valueOf(qty));
            values.put("image", base64Image);
            values.put("ref", ref);
            values.put("tva_tx", tva_tx);
            values.put("fk_product", product_id);
            long check = this.database.insert("cart", null, values);
            this.database.close();
            if (check == -1) {
                return -1;
            }
            return 1;
        }

        public ArrayList<HashMap<String, String>> getSuppliers() {
            ArrayList<HashMap<String, String>> supplier = new ArrayList<>();
            Cursor cursor = this.database.rawQuery("SELECT * FROM suppliers ORDER BY suppliers_id DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(Constant.SUPPLIERS_ID, cursor.getString(0));
                    map.put(Constant.SUPPLIERS_NAME, cursor.getString(1));
                    map.put(Constant.SUPPLIERS_CONTACT_PERSON, cursor.getString(2));
                    map.put(Constant.SUPPLIERS_CELL, cursor.getString(3));
                    map.put(Constant.SUPPLIERS_EMAIL, cursor.getString(4));
                    map.put(Constant.SUPPLIERS_ADDRESS, cursor.getString(5));
                    supplier.add(map);
                } while (cursor.moveToNext());
            }
            this.database.close();
            return supplier;
        }

        public boolean deleteCustomer(String customer_id) {
            long check = (long) this.database.delete("customers", "customer_id=?", new String[]{customer_id});
            this.database.close();
            return check == 1;
        }
    */
    public boolean deleteCategory(String category_id) {
        long check = (long) this.database.delete(Constant.PRODUCT_CATEGORY, "category_id=?", new String[]{category_id});
        close();
        return check == 1;
    }

    /*
        public boolean deletePaymentMethod(String payment_method_id) {
            long check = (long) this.database.delete("payment_method", "payment_method_id=?", new String[]{payment_method_id});
            this.database.close();
            return check == 1;
        }

        public boolean updateOrder(String invoiceId, String orderStatus) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constant.ORDER_STATUS, orderStatus);
            SQLiteDatabase sQLiteDatabase = this.database;
            String str = Constant.orderList;
            String[] strArr = {invoiceId};
            this.database.update(Constant.orderDetails, contentValues, "invoice_id=?", new String[]{invoiceId});
            this.database.close();
            return ((long) sQLiteDatabase.update(str, contentValues, "invoice_id=?", strArr)) == 1;
        }

        public boolean deleteOrder(String invoice_id) {
            long check = (long) this.database.delete("order_list", "invoice_id=?", new String[]{invoice_id});
            long delete = (long) this.database.delete("order_details", "invoice_id=?", new String[]{invoice_id});
            this.database.close();
            return check == 1;
        }
    */
    public Boolean deleteProduct(String product_id) {
        long check = this.database.delete("products", "product_id=?", new String[]{product_id});
        long delete = this.database.delete("product_cart", "product_id=?", new String[]{product_id});
        close();
        return check == 1;
    }
/*
    public boolean deleteExpense(String expense_id) {
        long check = (long) this.database.delete("expense", "expense_id=?", new String[]{expense_id});
        this.database.close();
        return check == 1;
    }

    public boolean deleteSupplier(String customer_id) {
        long check = (long) this.database.delete("suppliers", "suppliers_id=?", new String[]{customer_id});
        this.database.close();
        return check == 1;
    }
*/
}