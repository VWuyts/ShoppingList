package com.wuyts.nik.boodschappenlijst.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
//import android.util.Log;

import java.util.ArrayList;

public class Favorite implements Parcelable {

    private final long id;
    private final String name;
    private final byte[] image;
    private final int shop;
    private final boolean isFixedShop;
    //private static final String TAG = "Favorite";

    private Favorite(long id, String name, byte[] image, int shop, boolean isFixedShop) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.shop = shop;
        this.isFixedShop = isFixedShop;
    }

    public long getId() {
        return id;
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

    public static ArrayList<Favorite> fromCursor(Cursor cursor) {
        ArrayList<Favorite> arrayList = new ArrayList<>();

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(ShoppingListContract.Item._ID));
            String name = cursor.getString
                    (cursor.getColumnIndex(ShoppingListContract.Item.COLUMN_NAME));
            byte[] image = cursor.getBlob
                    (cursor.getColumnIndex(ShoppingListContract.Item.COLUMN_IMAGE));
            int shop = cursor.getInt
                    (cursor.getColumnIndex(ShoppingListContract.Shop.COLUMN_IMAGE_ID));
            boolean isFixedShop = cursor.getInt
                    (cursor.getColumnIndex(ShoppingListContract.Item.COLUMN_FIXED_SHOP)) > 0;
            arrayList.add(new Favorite(id, name, image, shop, isFixedShop));
        }
        //Log.d(TAG, "arrayList count = " + Integer.toString(arrayList.size()));
        return arrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeByteArray(image);
        parcel.writeInt(shop);
        parcel.writeInt(isFixedShop ? 1 : 0);
    }

    public static final Parcelable.Creator<Favorite> CREATOR
            = new Parcelable.Creator<Favorite>() {
        public Favorite createFromParcel(Parcel in) {
            return new Favorite(in);
        }

        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };

    private Favorite(Parcel in) {
        id = in.readLong();
        name = in.readString();
        image = in.createByteArray();
        shop = in.readInt();
        isFixedShop = in.readInt() > 0;
    }
}
