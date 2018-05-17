package com.wuyts.nik.boodschappenlijst.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

public class CatalogueItem implements Parcelable {

    private long id;
    private long categoryId;
    private String name;
    private byte[] image;
    private int shop;
    private boolean isFixedShop;
    private static final String TAG = "CatalogueItem";

    public CatalogueItem(long id, long categoryId, String name, byte[] image, int shop,
                         boolean isFixedShop) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.image = image;
        this.shop = shop;
        this.isFixedShop = isFixedShop;
    }

    public long getId() {
        return id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public byte[] getImage() {
        return image;
    }

    public int getShop() {
        return shop;
    }

    public boolean isFixedShop() {
        return isFixedShop;
    }

    public static ArrayList<CatalogueItem> fromCursor(Cursor cursor) {
        ArrayList<CatalogueItem> arrayList = new ArrayList<>();

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex
                    (shoppingListContract.Item.TABLE_NAME + "." + shoppingListContract.Item._ID));
            long categoryId = cursor.getLong
                    (cursor.getColumnIndex(shoppingListContract.Item.COLUMN_CATEGORY_ID));
            String name = cursor.getString
                    (cursor.getColumnIndex(shoppingListContract.Item.COLUMN_NAME));
            byte[] image = cursor.getBlob
                    (cursor.getColumnIndex(shoppingListContract.Item.COLUMN_IMAGE));
            int shop = cursor.getInt
                    (cursor.getColumnIndex(shoppingListContract.Shop.COLUMN_IMAGE_ID));
            boolean isFixedShop = cursor.getInt
                    (cursor.getColumnIndex(shoppingListContract.Item.COLUMN_FIXED_SHOP)) > 0;
            arrayList.add(new CatalogueItem(id, categoryId, name, image, shop, isFixedShop));
        }

        return arrayList;
    }

    public static ArrayList<String> categoriesFromCursor(Cursor cursor) {
        ArrayList<String> arrayList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String category = cursor.getString(cursor.getColumnIndex
                    (shoppingListContract.Category.COLUMN_NAME));
            arrayList.add(category);
        }

        Log.i(TAG, Integer.toString(arrayList.size()));
        return arrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(categoryId);
        parcel.writeString(name);
        parcel.writeByteArray(image);
        parcel.writeInt(shop);
        parcel.writeInt(isFixedShop ? 1 : 0);
    }

    public static final Parcelable.Creator<CatalogueItem> CREATOR
            = new Parcelable.Creator<CatalogueItem>() {
        @Override
        public CatalogueItem createFromParcel(Parcel in) {
            return new CatalogueItem(in);
        }

        @Override
        public CatalogueItem[] newArray(int size) {
            return new CatalogueItem[size];
        }
    };

    private CatalogueItem(Parcel in) {
        id = in.readLong();
        categoryId = in.readLong();
        name = in.readString();
        image = in.createByteArray();
        shop = in.readInt();
        isFixedShop = in.readInt() > 0;
    }
}
