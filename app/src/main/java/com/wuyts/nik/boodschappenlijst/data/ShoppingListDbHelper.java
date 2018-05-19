package com.wuyts.nik.boodschappenlijst.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.wuyts.nik.boodschappenlijst.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by Veronique Wuyts on 26/04/2018.
 */

public class ShoppingListDbHelper extends SQLiteOpenHelper {

    // Database name and version
    public static final String DATABASE_NAME = "ShoppingList.db";
    public static final int DATABASE_VERSION = 1;

    // Context required to access resources
    private final Context mContext;

    // SQL statements to create tables
    private static final String SQL_CREATE_SHOP =
        "CREATE TABLE " + shoppingListContract.Shop.TABLE_NAME + " (" +
            shoppingListContract.Shop._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            shoppingListContract.Shop.COLUMN_NAME + " TEXT NOT NULL, " +
            shoppingListContract.Shop.COLUMN_IMAGE_ID + " INTEGER)";
    private static final String SQL_CREATE_CATEGORY =
        "CREATE TABLE " + shoppingListContract.Category.TABLE_NAME + " (" +
            shoppingListContract.Category._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            shoppingListContract.Category.COLUMN_NAME + " TEXT NOT NULL)";
    private static final String SQL_CREATE_UNIT =
        "CREATE TABLE " + shoppingListContract.Unit.TABLE_NAME + " (" +
            shoppingListContract.Unit._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            shoppingListContract.Unit.COLUMN_NAME + " TEXT NOT NULL)";
    private static final String SQL_CREATE_LIST =
        "CREATE TABLE " + shoppingListContract.List.TABLE_NAME + " (" +
            shoppingListContract.List._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            shoppingListContract.List.COLUMN_NAME + " TEXT NOT NULL, " +
            shoppingListContract.List.COLUMN_IS_RECIPE + " INTEGER NOT NULL DEFAULT 0)";
    private static final String SQL_CREATE_SHOPPING_ORDER = "CREATE TABLE " +
            shoppingListContract.ShoppingOrder.TABLE_NAME + " (" +
            shoppingListContract.ShoppingOrder._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            shoppingListContract.ShoppingOrder.COLUMN_CATEGORY_ID + " INTEGER NOT NULL, " +
            shoppingListContract.ShoppingOrder.COLUMN_SHOP_ID + " INTEGER NOT NULL, " +
            shoppingListContract.ShoppingOrder.COLUMN_SEQUENCE + " INTEGER NOT NULL, " +
            "FOREIGN KEY(" + shoppingListContract.ShoppingOrder.COLUMN_CATEGORY_ID +
                ") REFERENCES " + shoppingListContract.Category.TABLE_NAME + "(" +
                shoppingListContract.Category._ID + "), " +
            "FOREIGN KEY(" + shoppingListContract.ShoppingOrder.COLUMN_SHOP_ID +
                ") REFERENCES " + shoppingListContract.Shop.TABLE_NAME + "(" +
                shoppingListContract.Shop._ID + "))";
    private static final String SQL_CREATE_ITEM =
        "CREATE TABLE " + shoppingListContract.Item.TABLE_NAME + " (" +
             shoppingListContract.Item._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
             shoppingListContract.Item.COLUMN_CATEGORY_ID + " INTEGER NOT NULL, " +
             shoppingListContract.Item.COLUMN_SHOP_ID + " INTEGER, " +
             shoppingListContract.Item.COLUMN_UNIT_ID + " INTEGER, " +
             shoppingListContract.Item.COLUMN_NAME + " TEXT NOT NULL," +
             shoppingListContract.Item.COLUMN_IMAGE + " BLOB, " +
             shoppingListContract.Item.COLUMN_NOTE + " TEXT, " +
             shoppingListContract.Item.COLUMN_FIXED_SHOP + " INTEGER NOT NULL DEFAULT 0, " +
             shoppingListContract.Item.COLUMN_FAVORITE + " INTEGER NOT NULL DEFAULT 0, " +
             "FOREIGN KEY(" + shoppingListContract.Item.COLUMN_CATEGORY_ID +
                ") REFERENCES " + shoppingListContract.Category.TABLE_NAME + "(" +
                shoppingListContract.Category._ID + "), " +
             "FOREIGN KEY(" + shoppingListContract.Item.COLUMN_SHOP_ID +
                ") REFERENCES " + shoppingListContract.Shop.TABLE_NAME + "(" +
                shoppingListContract.Shop._ID + "), " +
             "FOREIGN KEY(" + shoppingListContract.Item.COLUMN_UNIT_ID +
                ") REFERENCES " + shoppingListContract.Unit.TABLE_NAME + "(" +
                shoppingListContract.Unit._ID + "))";
    private static final String SQL_CREATE_ITEM_ON_LIST =
        "CREATE TABLE " + shoppingListContract.ListItem.TABLE_NAME + " (" +
             shoppingListContract.ListItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
             shoppingListContract.ListItem.COLUMN_LIST_ID + " INTEGER NOT NULL, " +
             shoppingListContract.ListItem.COLUMN_ITEM_ID + " INTEGER NOT NULL, " +
             shoppingListContract.ListItem.COLUMN_AMOUNT + " INTEGER, " +
             shoppingListContract.ListItem.COLUMN_PROMOTION + " INTEGER NOT NULL DEFAULT 0, " +
             shoppingListContract.ListItem.COLUMN_BOUGHT + " INTEGER NOT NULL DEFAULT 0, " +
             "FOREIGN KEY(" + shoppingListContract.ListItem.COLUMN_LIST_ID +
                ") REFERENCES " + shoppingListContract.List.TABLE_NAME + "(" +
                shoppingListContract.List._ID + "), " +
             "FOREIGN KEY(" + shoppingListContract.ListItem.COLUMN_LIST_ID +
                ") REFERENCES " + shoppingListContract.Item.TABLE_NAME + "(" +
                shoppingListContract.Item._ID + ")) ";

    // SQL statements to delete tables
    private static final String SQL_DELETE_SHOP = "DROP TABLE IF EXISTS " +
            shoppingListContract.Shop.TABLE_NAME;
    private static final String SQL_DELETE_CATEGORY = "DROP TABLE IF EXISTS " +
            shoppingListContract.Category.TABLE_NAME;
    private static final String SQL_DELETE_UNIT = "DROP TABLE IF EXISTS " +
            shoppingListContract.Unit.TABLE_NAME;
    private static final String SQL_DELETE_LIST = "DROP TABLE IF EXISTS " +
            shoppingListContract.List.TABLE_NAME;
    private static final String SQL_DELETE_SHOPPING_ORDER = "DROP TABLE IF EXISTS " +
            shoppingListContract.ShoppingOrder.TABLE_NAME;
    private static final String SQL_DELETE_ITEM = "DROP TABLE IF EXISTS " +
            shoppingListContract.Item.TABLE_NAME;
    private static final String SQL_DELETE_ITEM_ON_LIST = "DROP TABLE IF EXISTS " +
            shoppingListContract.ListItem.TABLE_NAME;

    // Array for storing IDs
    private static long[] categoryIDs;


    // Constructor
    public ShoppingListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(SQL_CREATE_SHOP);
        db.execSQL(SQL_CREATE_CATEGORY);
        db.execSQL(SQL_CREATE_UNIT);
        db.execSQL(SQL_CREATE_LIST);
        db.execSQL(SQL_CREATE_SHOPPING_ORDER);
        db.execSQL(SQL_CREATE_ITEM);
        db.execSQL(SQL_CREATE_ITEM_ON_LIST);

        // Insert data into tables
        insertShops(db);
        int catLength = insertCategory(db);
        insertUnit(db);
        insertList(db);
        insertItem(db, catLength);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //TODO: implement onUpgrade according to the upgrade policy

        // During development
        db.execSQL(SQL_DELETE_SHOP);
        db.execSQL(SQL_DELETE_CATEGORY);
        db.execSQL(SQL_DELETE_UNIT);
        db.execSQL(SQL_DELETE_LIST);
        db.execSQL(SQL_DELETE_SHOPPING_ORDER);
        db.execSQL(SQL_DELETE_ITEM);
        db.execSQL(SQL_DELETE_ITEM_ON_LIST);
        onCreate(db);
    }

    // Retrieve ListItem data
    public Cursor getListItemData(SQLiteDatabase db, boolean sortCategory) {
        String table = shoppingListContract.Item.TABLE_NAME +
                " LEFT JOIN " + shoppingListContract.Category.TABLE_NAME +
                " ON " + shoppingListContract.Item.COLUMN_CATEGORY_ID +
                "=" + shoppingListContract.Category.TABLE_NAME +
                "." + shoppingListContract.Category._ID +
                " LEFT JOIN " + shoppingListContract.Shop.TABLE_NAME +
                " ON " + shoppingListContract.Item.COLUMN_SHOP_ID +
                "=" + shoppingListContract.Shop.TABLE_NAME +
                "." + shoppingListContract.Shop._ID +
                " LEFT JOIN " + shoppingListContract.Unit.TABLE_NAME +
                " ON " + shoppingListContract.Item.COLUMN_UNIT_ID +
                "=" + shoppingListContract.Unit.TABLE_NAME +
                "." + shoppingListContract.Unit._ID +
                " LEFT JOIN " + shoppingListContract.ListItem.TABLE_NAME +
                " ON " + shoppingListContract.Item.TABLE_NAME +
                "." + shoppingListContract.Item._ID +
                "=" + shoppingListContract.ListItem.COLUMN_ITEM_ID;
        String[] columns = {
                shoppingListContract.Item.TABLE_NAME + "." + shoppingListContract.Item._ID,
                //shoppingListContract.ListItem.TABLE_NAME + "." + shoppingListContract.ListItem._ID,
                shoppingListContract.Item.COLUMN_NAME,
                shoppingListContract.Item.COLUMN_IMAGE,
                shoppingListContract.Item.COLUMN_NOTE,
                shoppingListContract.Category.COLUMN_NAME,
                shoppingListContract.Shop.COLUMN_IMAGE_ID,
                shoppingListContract.Unit.COLUMN_NAME,
                shoppingListContract.Item.COLUMN_FIXED_SHOP,
                shoppingListContract.Item.COLUMN_FAVORITE,
                shoppingListContract.ListItem.COLUMN_LIST_ID,
                shoppingListContract.ListItem.COLUMN_AMOUNT,
                shoppingListContract.ListItem.COLUMN_PROMOTION,
                shoppingListContract.ListItem.COLUMN_BOUGHT
        };
        String orderBy = (sortCategory ? shoppingListContract.Category.TABLE_NAME +
                "." + shoppingListContract.Category._ID + ", " : "") +
                shoppingListContract.Item.COLUMN_NAME;

        return db.query(true, table, columns, null, null,
                null, null, orderBy, null);
    }

    // Retrieve Item data per category
    public Cursor getItemData(SQLiteDatabase db, String category) {
        String table = shoppingListContract.Item.TABLE_NAME +
                " LEFT JOIN " + shoppingListContract.Category.TABLE_NAME +
                " ON " + shoppingListContract.Item.COLUMN_CATEGORY_ID +
                "=" + shoppingListContract.Category.TABLE_NAME +
                "." + shoppingListContract.Category._ID +
                " LEFT JOIN " + shoppingListContract.Shop.TABLE_NAME +
                " ON " + shoppingListContract.Item.COLUMN_SHOP_ID +
                "=" + shoppingListContract.Shop.TABLE_NAME +
                "." + shoppingListContract.Shop._ID;
        String[] columns = {
                shoppingListContract.Item.TABLE_NAME + "." + shoppingListContract.Item._ID,
                shoppingListContract.Item.COLUMN_NAME,
                shoppingListContract.Item.COLUMN_IMAGE,
                shoppingListContract.Shop.COLUMN_IMAGE_ID,
                shoppingListContract.Item.COLUMN_FIXED_SHOP
        };
        String selection = shoppingListContract.Category.COLUMN_NAME + " = ?";
        String[] selectionArgs = {category};
        String orderBY = shoppingListContract.Item.COLUMN_NAME;

        return db.query(table, columns, selection, selectionArgs, null, null, orderBY);
    }

    // Retrieve favorite items
    public Cursor getFavorites(SQLiteDatabase db) {
        String table = shoppingListContract.Item.TABLE_NAME +
                " LEFT JOIN " + shoppingListContract.Shop.TABLE_NAME +
                " ON " + shoppingListContract.Item.COLUMN_SHOP_ID +
                "=" + shoppingListContract.Shop.TABLE_NAME +
                "." + shoppingListContract.Shop._ID;
        String[] columns = {
                shoppingListContract.Item.TABLE_NAME + "." + shoppingListContract.Item._ID,
                shoppingListContract.Item.COLUMN_NAME,
                shoppingListContract.Item.COLUMN_IMAGE,
                shoppingListContract.Shop.COLUMN_IMAGE_ID,
                shoppingListContract.Item.COLUMN_FIXED_SHOP
        };
        String selection = shoppingListContract.Item.COLUMN_FIXED_SHOP + "=?";
        String[] selectionArgs = { "true" };
        String orderBy = shoppingListContract.Item.COLUMN_NAME;

        return db.query(table, columns, selection, selectionArgs, null, null, orderBy);
    }

    // Retrieve category names
    public Cursor getCategories(SQLiteDatabase db) {
        String table = shoppingListContract.Category.TABLE_NAME;
        String[] columns = {
                shoppingListContract.Category.COLUMN_NAME
        };

        return db.query(table, columns, null,null, null, null, null);
    }

    // Helper function to populate table Shop
    private void insertShops(SQLiteDatabase db) {
        String[] shopData = mContext.getResources().getStringArray(R.array.shop_data);
        int[] shopImageData = mContext.getResources().getIntArray(R.array.shop_image_dara);

        for (int i = 0; i < shopData.length; i++) {
            ContentValues values = new ContentValues();
            values.put(shoppingListContract.Shop.COLUMN_NAME, shopData[i]);
            values.put(shoppingListContract.Shop.COLUMN_IMAGE_ID, shopImageData[i]);
            db.insert(shoppingListContract.Shop.TABLE_NAME, null, values);
        }
    }

    // Helper function to populate table Category
    private int insertCategory(SQLiteDatabase db) {
        String[] categoryData = mContext.getResources().getStringArray(R.array.category_data);
        categoryIDs = new long[categoryData.length];

        for (int i = 0; i < categoryData.length; i++) {
            ContentValues values = new ContentValues();
            values.put(shoppingListContract.Category.COLUMN_NAME, categoryData[i]);
            categoryIDs[i] = db.insert(shoppingListContract.Category.TABLE_NAME, null, values);
        }

        return categoryData.length;
    }

    // Helper function to populate table Unit
    private void insertUnit(SQLiteDatabase db) {
        String[] unitData = mContext.getResources().getStringArray(R.array.unit_data);

        for (String unit : unitData) {
            ContentValues values = new ContentValues();
            values.put(shoppingListContract.Unit.COLUMN_NAME, unit);
            db.insert(shoppingListContract.Unit.TABLE_NAME, null, values);
        }
    }

    // Helper function to populate table List with a default list
    private void insertList(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(shoppingListContract.List.COLUMN_NAME,
                mContext.getResources().getString(R.string.list_default));
        values.put(shoppingListContract.List.COLUMN_IS_RECIPE, 0);
        db.insert(shoppingListContract.List.TABLE_NAME, null, values);
    }

    // Helper function to populate table CatalogueItem
    private void insertItem(SQLiteDatabase db, int catLength) {
        String[][] itemData = {
                mContext.getResources().getStringArray(R.array.fruit_data),
                mContext.getResources().getStringArray(R.array.veg_data),
                mContext.getResources().getStringArray(R.array.meat_data),
                mContext.getResources().getStringArray(R.array.fish_data),
                mContext.getResources().getStringArray(R.array.cheese_data),
                mContext.getResources().getStringArray(R.array.dairy_data),
                mContext.getResources().getStringArray(R.array.pasta_data),
                mContext.getResources().getStringArray(R.array.breakfast_data),
                mContext.getResources().getStringArray(R.array.bakery_data),
                mContext.getResources().getStringArray(R.array.spices_data),
                mContext.getResources().getStringArray(R.array.canned_data),
                mContext.getResources().getStringArray(R.array.sauces_data),
                mContext.getResources().getStringArray(R.array.frozen_data)
        };
        for (int i = 0; i < catLength; i++) {
            for (String item : itemData[i]) {
                ContentValues values = new ContentValues();
                values.put(shoppingListContract.Item.COLUMN_CATEGORY_ID, categoryIDs[i]);
                values.put(shoppingListContract.Item.COLUMN_NAME, item);
                db.insert(shoppingListContract.Item.TABLE_NAME, null, values);
            }
        }
        // Add picture to bananas
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bananas);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] image = bos.toByteArray();
        ContentValues values = new ContentValues();
        values.put(shoppingListContract.Item.COLUMN_IMAGE, image);
        String selection = shoppingListContract.Item.COLUMN_NAME + " = ?";
        String[] selectionArgs = {mContext.getResources().getString(R.string.item_fruit_bananas)};
        db.update(shoppingListContract.Item.TABLE_NAME, values, selection, selectionArgs);
    }
}
