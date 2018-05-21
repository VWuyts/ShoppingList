package com.wuyts.nik.boodschappenlijst.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

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
        "CREATE TABLE " + ShoppingListContract.Shop.TABLE_NAME + " (" +
            ShoppingListContract.Shop._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ShoppingListContract.Shop.COLUMN_NAME + " TEXT NOT NULL, " +
            ShoppingListContract.Shop.COLUMN_IMAGE_ID + " INTEGER)";
    private static final String SQL_CREATE_CATEGORY =
        "CREATE TABLE " + ShoppingListContract.Category.TABLE_NAME + " (" +
            ShoppingListContract.Category._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ShoppingListContract.Category.COLUMN_NAME + " TEXT NOT NULL)";
    private static final String SQL_CREATE_UNIT =
        "CREATE TABLE " + ShoppingListContract.Unit.TABLE_NAME + " (" +
            ShoppingListContract.Unit._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ShoppingListContract.Unit.COLUMN_NAME + " TEXT NOT NULL)";
    private static final String SQL_CREATE_LIST =
        "CREATE TABLE " + ShoppingListContract.List.TABLE_NAME + " (" +
            ShoppingListContract.List._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ShoppingListContract.List.COLUMN_NAME + " TEXT NOT NULL, " +
            ShoppingListContract.List.COLUMN_IS_RECIPE + " INTEGER NOT NULL DEFAULT 0)";
    private static final String SQL_CREATE_SHOPPING_ORDER = "CREATE TABLE " +
            ShoppingListContract.ShoppingOrder.TABLE_NAME + " (" +
            ShoppingListContract.ShoppingOrder._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ShoppingListContract.ShoppingOrder.COLUMN_CATEGORY_ID + " INTEGER NOT NULL, " +
            ShoppingListContract.ShoppingOrder.COLUMN_SHOP_ID + " INTEGER NOT NULL, " +
            ShoppingListContract.ShoppingOrder.COLUMN_SEQUENCE + " INTEGER NOT NULL, " +
            "FOREIGN KEY(" + ShoppingListContract.ShoppingOrder.COLUMN_CATEGORY_ID +
                ") REFERENCES " + ShoppingListContract.Category.TABLE_NAME + "(" +
                ShoppingListContract.Category._ID + "), " +
            "FOREIGN KEY(" + ShoppingListContract.ShoppingOrder.COLUMN_SHOP_ID +
                ") REFERENCES " + ShoppingListContract.Shop.TABLE_NAME + "(" +
                ShoppingListContract.Shop._ID + "))";
    private static final String SQL_CREATE_ITEM =
        "CREATE TABLE " + ShoppingListContract.Item.TABLE_NAME + " (" +
             ShoppingListContract.Item._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
             ShoppingListContract.Item.COLUMN_CATEGORY_ID + " INTEGER NOT NULL, " +
             ShoppingListContract.Item.COLUMN_SHOP_ID + " INTEGER, " +
             ShoppingListContract.Item.COLUMN_UNIT_ID + " INTEGER, " +
             ShoppingListContract.Item.COLUMN_NAME + " TEXT NOT NULL," +
             ShoppingListContract.Item.COLUMN_IMAGE + " BLOB, " +
             ShoppingListContract.Item.COLUMN_NOTE + " TEXT, " +
             ShoppingListContract.Item.COLUMN_FIXED_SHOP + " INTEGER NOT NULL DEFAULT 0, " +
             ShoppingListContract.Item.COLUMN_FAVORITE + " INTEGER NOT NULL DEFAULT 0, " +
             "FOREIGN KEY(" + ShoppingListContract.Item.COLUMN_CATEGORY_ID +
                ") REFERENCES " + ShoppingListContract.Category.TABLE_NAME + "(" +
                ShoppingListContract.Category._ID + "), " +
             "FOREIGN KEY(" + ShoppingListContract.Item.COLUMN_SHOP_ID +
                ") REFERENCES " + ShoppingListContract.Shop.TABLE_NAME + "(" +
                ShoppingListContract.Shop._ID + "), " +
             "FOREIGN KEY(" + ShoppingListContract.Item.COLUMN_UNIT_ID +
                ") REFERENCES " + ShoppingListContract.Unit.TABLE_NAME + "(" +
                ShoppingListContract.Unit._ID + "))";
    private static final String SQL_CREATE_ITEM_ON_LIST =
        "CREATE TABLE " + ShoppingListContract.ListItem.TABLE_NAME + " (" +
             ShoppingListContract.ListItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
             ShoppingListContract.ListItem.COLUMN_LIST_ID + " INTEGER NOT NULL, " +
             ShoppingListContract.ListItem.COLUMN_ITEM_ID + " INTEGER NOT NULL, " +
             ShoppingListContract.ListItem.COLUMN_AMOUNT + " INTEGER, " +
             ShoppingListContract.ListItem.COLUMN_PROMOTION + " INTEGER NOT NULL DEFAULT 0, " +
             ShoppingListContract.ListItem.COLUMN_BOUGHT + " INTEGER NOT NULL DEFAULT 0, " +
             "FOREIGN KEY(" + ShoppingListContract.ListItem.COLUMN_LIST_ID +
                ") REFERENCES " + ShoppingListContract.List.TABLE_NAME + "(" +
                ShoppingListContract.List._ID + "), " +
             "FOREIGN KEY(" + ShoppingListContract.ListItem.COLUMN_LIST_ID +
                ") REFERENCES " + ShoppingListContract.Item.TABLE_NAME + "(" +
                ShoppingListContract.Item._ID + ")) ";

    // SQL statements to delete tables
    private static final String SQL_DELETE_SHOP = "DROP TABLE IF EXISTS " +
            ShoppingListContract.Shop.TABLE_NAME;
    private static final String SQL_DELETE_CATEGORY = "DROP TABLE IF EXISTS " +
            ShoppingListContract.Category.TABLE_NAME;
    private static final String SQL_DELETE_UNIT = "DROP TABLE IF EXISTS " +
            ShoppingListContract.Unit.TABLE_NAME;
    private static final String SQL_DELETE_LIST = "DROP TABLE IF EXISTS " +
            ShoppingListContract.List.TABLE_NAME;
    private static final String SQL_DELETE_SHOPPING_ORDER = "DROP TABLE IF EXISTS " +
            ShoppingListContract.ShoppingOrder.TABLE_NAME;
    private static final String SQL_DELETE_ITEM = "DROP TABLE IF EXISTS " +
            ShoppingListContract.Item.TABLE_NAME;
    private static final String SQL_DELETE_ITEM_ON_LIST = "DROP TABLE IF EXISTS " +
            ShoppingListContract.ListItem.TABLE_NAME;

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

    // Retrieve default List id
    public long getDefaultListId(SQLiteDatabase db) {
        String[] columns = {ShoppingListContract.List._ID};
        String selection = ShoppingListContract.List.COLUMN_IS_RECIPE + " = ?";
        String[] selectionArgs = {Integer.toString(0)};
        Cursor cursor = db.query(ShoppingListContract.List.TABLE_NAME, columns, selection,
                selectionArgs, null, null, null);
        cursor.moveToNext();
        long listId = cursor.getLong(cursor.getColumnIndex(ShoppingListContract.List._ID));
        cursor.close();

        return listId;
    }

    // Retrieve ListItem data
    public Cursor getListItemData(SQLiteDatabase db, boolean sortCategory) {
        String table = ShoppingListContract.ListItem.TABLE_NAME +
                " INNER JOIN " + ShoppingListContract.Item.TABLE_NAME +
                " ON " + ShoppingListContract.ListItem.COLUMN_ITEM_ID +
                "=" + ShoppingListContract.Item.TABLE_NAME +
                "." + ShoppingListContract.Item._ID +
                " LEFT JOIN " + ShoppingListContract.Category.TABLE_NAME +
                " ON " + ShoppingListContract.Item.COLUMN_CATEGORY_ID +
                "=" + ShoppingListContract.Category.TABLE_NAME +
                "." + ShoppingListContract.Category._ID +
                " LEFT JOIN " + ShoppingListContract.Shop.TABLE_NAME +
                " ON " + ShoppingListContract.Item.COLUMN_SHOP_ID +
                "=" + ShoppingListContract.Shop.TABLE_NAME +
                "." + ShoppingListContract.Shop._ID +
                " LEFT JOIN " + ShoppingListContract.Unit.TABLE_NAME +
                " ON " + ShoppingListContract.Item.COLUMN_UNIT_ID +
                "=" + ShoppingListContract.Unit.TABLE_NAME +
                "." + ShoppingListContract.Unit._ID;
        String[] columns = {
                ShoppingListContract.ListItem.TABLE_NAME + "." + ShoppingListContract.ListItem._ID,
                ShoppingListContract.Item.COLUMN_NAME,
                ShoppingListContract.Item.COLUMN_IMAGE,
                ShoppingListContract.Item.COLUMN_NOTE,
                ShoppingListContract.Category.COLUMN_NAME,
                ShoppingListContract.Shop.COLUMN_NAME,
                ShoppingListContract.Shop.COLUMN_IMAGE_ID,
                ShoppingListContract.Unit.COLUMN_NAME,
                ShoppingListContract.Item.COLUMN_FIXED_SHOP,
                ShoppingListContract.Item.COLUMN_FAVORITE,
                ShoppingListContract.ListItem.COLUMN_LIST_ID,
                ShoppingListContract.ListItem.COLUMN_ITEM_ID,
                ShoppingListContract.ListItem.COLUMN_AMOUNT,
                ShoppingListContract.ListItem.COLUMN_PROMOTION,
                ShoppingListContract.ListItem.COLUMN_BOUGHT
        };
        String orderBy = (sortCategory ? ShoppingListContract.Category.TABLE_NAME +
                "." + ShoppingListContract.Category._ID + ", " : "") +
                ShoppingListContract.Item.COLUMN_NAME;

        return db.query(true, table, columns, null, null,
                null, null, orderBy, null);
    }

    // Retrieve Item data per category
    public Cursor getItemData(SQLiteDatabase db, String category) {
        String table = ShoppingListContract.Item.TABLE_NAME +
                " LEFT JOIN " + ShoppingListContract.Category.TABLE_NAME +
                " ON " + ShoppingListContract.Item.COLUMN_CATEGORY_ID +
                "=" + ShoppingListContract.Category.TABLE_NAME +
                "." + ShoppingListContract.Category._ID +
                " LEFT JOIN " + ShoppingListContract.Shop.TABLE_NAME +
                " ON " + ShoppingListContract.Item.COLUMN_SHOP_ID +
                "=" + ShoppingListContract.Shop.TABLE_NAME +
                "." + ShoppingListContract.Shop._ID;
        String[] columns = {
                ShoppingListContract.Item.TABLE_NAME + "." + ShoppingListContract.Item._ID,
                ShoppingListContract.Item.COLUMN_NAME,
                ShoppingListContract.Item.COLUMN_IMAGE,
                ShoppingListContract.Shop.COLUMN_IMAGE_ID,
                ShoppingListContract.Item.COLUMN_FIXED_SHOP
        };
        String selection = ShoppingListContract.Category.COLUMN_NAME + " = ?";
        String[] selectionArgs = {category};
        String orderBY = ShoppingListContract.Item.COLUMN_NAME;

        return db.query(table, columns, selection, selectionArgs, null, null, orderBY);
    }

    // Retrieve favorite items
    public Cursor getFavorites(SQLiteDatabase db) {
        String table = ShoppingListContract.Item.TABLE_NAME +
                " LEFT JOIN " + ShoppingListContract.Shop.TABLE_NAME +
                " ON " + ShoppingListContract.Item.COLUMN_SHOP_ID +
                "=" + ShoppingListContract.Shop.TABLE_NAME +
                "." + ShoppingListContract.Shop._ID;
        String[] columns = {
                ShoppingListContract.Item.TABLE_NAME + "." + ShoppingListContract.Item._ID,
                ShoppingListContract.Item.COLUMN_NAME,
                ShoppingListContract.Item.COLUMN_IMAGE,
                ShoppingListContract.Shop.COLUMN_IMAGE_ID,
                ShoppingListContract.Item.COLUMN_FIXED_SHOP
        };
        String selection = ShoppingListContract.Item.COLUMN_FAVORITE + " = ?";
        String[] selectionArgs = {Integer.toString(1)};
        String orderBy = ShoppingListContract.Item.COLUMN_NAME;

        return db.query(table, columns, selection, selectionArgs, null, null, orderBy);
    }

    // Retrieve category names
    public Cursor getCategories(SQLiteDatabase db) {
        String table = ShoppingListContract.Category.TABLE_NAME;
        String[] columns = {
                ShoppingListContract.Category.COLUMN_NAME
        };

        return db.query(table, columns, null,null, null, null, null);
    }

    // Retrieve units and their id
    public Cursor getUnits(SQLiteDatabase db) {
        String table = ShoppingListContract.Unit.TABLE_NAME;
        String[] columns = {
                ShoppingListContract.Unit._ID,
                ShoppingListContract.Unit.COLUMN_NAME
        };

        return db.query(table, columns, null, null, null, null, null);
    }

    // Retrieve shops, their id and image id
    public Cursor getShops(SQLiteDatabase db) {
        String table = ShoppingListContract.Shop.TABLE_NAME;
        String[] columns = {
                ShoppingListContract.Shop._ID,
                ShoppingListContract.Shop.COLUMN_NAME,
                ShoppingListContract.Shop.COLUMN_IMAGE_ID
        };

        return db.query(table, columns, null, null, null, null, null);
    }

    // Retrieve ListItem based on id
    public Cursor getListItem(SQLiteDatabase db, long id) {
        String table = ShoppingListContract.ListItem.TABLE_NAME +
                " INNER JOIN " + ShoppingListContract.Item.TABLE_NAME +
                " ON " + ShoppingListContract.ListItem.COLUMN_ITEM_ID +
                "=" + ShoppingListContract.Item.TABLE_NAME +
                "." + ShoppingListContract.Item._ID +
                " LEFT JOIN " + ShoppingListContract.Category.TABLE_NAME +
                " ON " + ShoppingListContract.Item.COLUMN_CATEGORY_ID +
                "=" + ShoppingListContract.Category.TABLE_NAME +
                "." + ShoppingListContract.Category._ID +
                " LEFT JOIN " + ShoppingListContract.Shop.TABLE_NAME +
                " ON " + ShoppingListContract.Item.COLUMN_SHOP_ID +
                "=" + ShoppingListContract.Shop.TABLE_NAME +
                "." + ShoppingListContract.Shop._ID +
                " LEFT JOIN " + ShoppingListContract.Unit.TABLE_NAME +
                " ON " + ShoppingListContract.Item.COLUMN_UNIT_ID +
                "=" + ShoppingListContract.Unit.TABLE_NAME +
                "." + ShoppingListContract.Unit._ID;
        String[] columns = {
                ShoppingListContract.ListItem.TABLE_NAME + "." + ShoppingListContract.ListItem._ID,
                ShoppingListContract.Item.COLUMN_NAME,
                ShoppingListContract.Item.COLUMN_IMAGE,
                ShoppingListContract.Item.COLUMN_NOTE,
                ShoppingListContract.Category.COLUMN_NAME,
                ShoppingListContract.Shop.COLUMN_NAME,
                ShoppingListContract.Shop.COLUMN_IMAGE_ID,
                ShoppingListContract.Unit.COLUMN_NAME,
                ShoppingListContract.Item.COLUMN_FIXED_SHOP,
                ShoppingListContract.Item.COLUMN_FAVORITE,
                ShoppingListContract.ListItem.COLUMN_LIST_ID,
                ShoppingListContract.ListItem.COLUMN_ITEM_ID,
                ShoppingListContract.ListItem.COLUMN_AMOUNT,
                ShoppingListContract.ListItem.COLUMN_PROMOTION,
                ShoppingListContract.ListItem.COLUMN_BOUGHT
        };
        String selection = ShoppingListContract.ListItem.TABLE_NAME +
                "." + ShoppingListContract.ListItem._ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};

        return db.query(table, columns, selection, selectionArgs, null, null, null);
    }

    // Add catalogue item to shopping list
    public void addListItem(SQLiteDatabase db, long itemId, long listId) {
        ContentValues values = new ContentValues();
        values.put(ShoppingListContract.ListItem.COLUMN_ITEM_ID, itemId);
        values.put(ShoppingListContract.ListItem.COLUMN_LIST_ID, listId);

        db.insert(ShoppingListContract.ListItem.TABLE_NAME, null, values);
    }

    // Delete ListItem
    public void deleteListItem(SQLiteDatabase db, long listItemId) {
        String selection = ShoppingListContract.ListItem._ID + " = ?";
        String[] selectionArgs = {Long.toString(listItemId)};

        db. delete(ShoppingListContract.ListItem.TABLE_NAME, selection, selectionArgs);
    }

    // Edit ListItem
    public void editListItem(SQLiteDatabase db, long listItemId, long itemId, int amount,
                             long unitId, String note, long shopId, int isFixedShop, int promotion) {
        String itemSelection = ShoppingListContract.Item._ID + " = ?";
        String[] itemSelectionArgs = {Long.toString(itemId)};
        String listItemSelection = ShoppingListContract.ListItem._ID + " = ?";
        String[] listItemSelectionArgs = {Long.toString(listItemId)};
        ContentValues itemValues = new ContentValues();
        ContentValues listItemValues = new ContentValues();
        if (amount > 0) {
            listItemValues.put(ShoppingListContract.ListItem.COLUMN_AMOUNT, amount);
            if (unitId > -1) {
                itemValues.put(ShoppingListContract.Item.COLUMN_UNIT_ID, unitId);
            }
        }
        if (note != null) {
            itemValues.put(ShoppingListContract.Item.COLUMN_NOTE, note);
        }
        if (shopId > -1) {
            itemValues.put(ShoppingListContract.Item.COLUMN_SHOP_ID, shopId);
        }
        itemValues.put(ShoppingListContract.Item.COLUMN_FIXED_SHOP, isFixedShop);
        listItemValues.put(ShoppingListContract.ListItem.COLUMN_PROMOTION, promotion);

        db.update(ShoppingListContract.Item.TABLE_NAME, itemValues, itemSelection,
                itemSelectionArgs);
        db.update(ShoppingListContract.ListItem.TABLE_NAME, listItemValues, listItemSelection,
                listItemSelectionArgs);
    }

    // Change state of isFavorite of ListItem
    public boolean toggleFavorite(SQLiteDatabase db, long itemId) {
        boolean isFavorite = true;
        String table = ShoppingListContract.Item.TABLE_NAME;
        String[] columns = {ShoppingListContract.Item.COLUMN_FAVORITE};
        String selection = ShoppingListContract.List._ID + " = ?";
        String[] selectionArgs = {Long.toString(itemId)};

        Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToNext()) {
            isFavorite = cursor.getInt
                    (cursor.getColumnIndex(ShoppingListContract.Item.COLUMN_FAVORITE)) > 0;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(ShoppingListContract.Item.COLUMN_FAVORITE, !isFavorite);
        db.update(table, values, selection, selectionArgs);

        return !isFavorite;
    }

    // Clear list
    public void clearList(SQLiteDatabase db, long listId) {
        String selection = ShoppingListContract.ListItem.COLUMN_LIST_ID + " = ?";
        String[] selectionArgs = {Long.toString(listId)};

        db.delete(ShoppingListContract.ListItem.TABLE_NAME, selection, selectionArgs);
    }

    // Helper function to populate table Shop
    private void insertShops(SQLiteDatabase db) {
        String[] shopData = mContext.getResources().getStringArray(R.array.shop_data);
        int[] shopImageData = {
                -1,
                R.drawable.logo_albert_heijn,
                R.drawable.logo_aldi,
                R.drawable.logo_bio_planet,
                R.drawable.logo_carrefour,
                R.drawable.logo_colruyt,
                R.drawable.logo_delhaize,
                R.drawable.logo_lidl,
                R.drawable.logo_okay,
                R.drawable.logo_spar
        };

        ContentValues values = new ContentValues();
        values.put(ShoppingListContract.Shop.COLUMN_NAME, shopData[0]);
        db.insert(ShoppingListContract.Shop.TABLE_NAME, null, values);

        for (int i = 1; i < shopData.length; i++) {
            values = new ContentValues();
            values.put(ShoppingListContract.Shop.COLUMN_NAME, shopData[i]);
            values.put(ShoppingListContract.Shop.COLUMN_IMAGE_ID, shopImageData[i]);
            db.insert(ShoppingListContract.Shop.TABLE_NAME, null, values);
        }
    }

    // Helper function to populate table Category
    private int insertCategory(SQLiteDatabase db) {
        String[] categoryData = mContext.getResources().getStringArray(R.array.category_data);
        categoryIDs = new long[categoryData.length];

        for (int i = 0; i < categoryData.length; i++) {
            ContentValues values = new ContentValues();
            values.put(ShoppingListContract.Category.COLUMN_NAME, categoryData[i]);
            categoryIDs[i] = db.insert(ShoppingListContract.Category.TABLE_NAME, null, values);
        }

        return categoryData.length;
    }

    // Helper function to populate table Unit
    private void insertUnit(SQLiteDatabase db) {
        String[] unitData = mContext.getResources().getStringArray(R.array.unit_data);

        for (String unit : unitData) {
            ContentValues values = new ContentValues();
            values.put(ShoppingListContract.Unit.COLUMN_NAME, unit);
            db.insert(ShoppingListContract.Unit.TABLE_NAME, null, values);
        }
    }

    // Helper function to populate table List with a default list
    private void insertList(@NonNull SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(ShoppingListContract.List.COLUMN_NAME,
                mContext.getResources().getString(R.string.list_default));
        values.put(ShoppingListContract.List.COLUMN_IS_RECIPE, 0);
        db.insert(ShoppingListContract.List.TABLE_NAME, null, values);
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
                values.put(ShoppingListContract.Item.COLUMN_CATEGORY_ID, categoryIDs[i]);
                values.put(ShoppingListContract.Item.COLUMN_NAME, item);
                db.insert(ShoppingListContract.Item.TABLE_NAME, null, values);
            }
        }
        // Add picture to bananas
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bananas);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] image = bos.toByteArray();
        ContentValues values = new ContentValues();
        values.put(ShoppingListContract.Item.COLUMN_IMAGE, image);
        String selection = ShoppingListContract.Item.COLUMN_NAME + " = ?";
        String[] selectionArgs = {mContext.getResources().getString(R.string.item_fruit_bananas)};
        db.update(ShoppingListContract.Item.TABLE_NAME, values, selection, selectionArgs);
    }
}
