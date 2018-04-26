package com.wuyts.nik.boodschappenlijst.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Veronique Wuyts on 26/04/2018.
 */

public class ShoppingListDbHelper extends SQLiteOpenHelper {
    // Database name and version
    public static final String DATABASE_NAME = "ShoppingList.db";
    public static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + shoppingListContract.Shop.TABLE_NAME + " (" +
                shoppingListContract.Shop._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                shoppingListContract.Shop.COLUMN_NAME_NAME + " TEXT NOT NULL); " +
        "CREATE TABLE " + shoppingListContract.Category.TABLE_NAME + " (" +
                shoppingListContract.Category._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                shoppingListContract.Category.COLUMN_NAME_NAME + " TEXT NOT NULL); " +
        "CREATE TABLE " + shoppingListContract.Unit.TABLE_NAME + " (" +
                shoppingListContract.Unit._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                shoppingListContract.Unit.COLUMN_NAME_NAME + " TEXT NOT NULL); " +
        "CREATE TABLE " + shoppingListContract.ShoppingList.TABLE_NAME + " (" +
                shoppingListContract.ShoppingList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                shoppingListContract.ShoppingList.COLUMN_NAME_NAME + " TEXT NOT NULL, " +
                shoppingListContract.ShoppingList.COLUMN_NAME_IS_RECIPE + " INTEGER NOT NULL DEFAULT 0); " +
        "CREATE TABLE " + shoppingListContract.Product.TABLE_NAME + " (" +
                shoppingListContract.Product._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                shoppingListContract.Product.COLUMN_NAME_CATEGOTY_ID + " INTEGER NOT NULL, " +
                shoppingListContract.Product.COLUMN_NAME_SHOP_ID + " INTEGER NOT NULL, " +
                shoppingListContract.Product.COLUMN_NAME_UNIT_ID + " INTEGER NOT NULL, " +
                shoppingListContract.Product.COLUMN_NAME_NAME + " TEXT NOT NULL," +
                shoppingListContract.Product.COLUMN_NAME_IMAGE + " BLOB, " +
                shoppingListContract.Product.COLUMN_NAME_NOTE + " TEXT, " +
                shoppingListContract.Product.COLUMN_NAME_FIXED_SHOP + " INTEGER NOT NULL DEFAULT 0, " +
                "FOREIGN KEY(" + shoppingListContract.Product.COLUMN_NAME_CATEGOTY_ID +
                    ") REFERENCES " + shoppingListContract.Category.TABLE_NAME + "(" + shoppingListContract.Category._ID + "), " +
                "FOREIGN KEY(" + shoppingListContract.Product.COLUMN_NAME_SHOP_ID +
                    ") REFERENCES " + shoppingListContract.Shop.TABLE_NAME + "(" + shoppingListContract.Shop._ID + "), " +
                "FOREIGN KEY(" + shoppingListContract.Product.COLUMN_NAME_UNIT_ID +
                    ") REFERENCES " + shoppingListContract.Unit.TABLE_NAME + "(" + shoppingListContract.Unit._ID + ")); " +
        "CREATE TABLE " + shoppingListContract.ItemOnList.TABLE_NAME + " (" +
                shoppingListContract.ItemOnList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                shoppingListContract.ItemOnList.COLUMN_NAME_SHOPPING_LIST_ID + " INTEGER NOT NULL, " +
                shoppingListContract.ItemOnList.COLUMN_NAME_PRODUCT_ID + " INTEGER NOT NULL, " +
                shoppingListContract.ItemOnList.COLUMN_NAME_AMOUNT + " INTEGER, " +
                shoppingListContract.ItemOnList.COLUMN_NAME_PROMOTION + " INTEGER NOT NULL DEFAULT 0, " +
                shoppingListContract.ItemOnList.COLUMN_NAME_BOUGHT + " INTEGER NOT NULL DEFAULT 0, " +
                "FOREIGN KEY(" + shoppingListContract.ItemOnList.COLUMN_NAME_SHOPPING_LIST_ID +
                ") REFERENCES " + shoppingListContract.ShoppingList.TABLE_NAME + "(" + shoppingListContract.ShoppingList._ID + "), " +
                "FOREIGN KEY(" + shoppingListContract.ItemOnList.COLUMN_NAME_PRODUCT_ID +
                ") REFERENCES " + shoppingListContract.Product.TABLE_NAME + "(" + shoppingListContract.Product._ID + ")) ";

    // Constructor
    public ShoppingListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO: implement onUpgrade
    }
}
