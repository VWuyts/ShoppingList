package com.wuyts.nik.boodschappenlijst.data;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.wuyts.nik.boodschappenlijst.R;

import java.io.ByteArrayOutputStream;

public class ListItem {
    private long id;
    private String name;
    private byte[] image;
    private String note;
    private String category;
    private int shop;
    private String unit;
    private boolean isFixedShop;
    private boolean isFavorite;
    private long listId;
    private int amount;
    private boolean isPromotion;
    private boolean isBought;

    private ListItem(long id, String name, byte[] image, String note, String category, int shop,
                    String unit, boolean isFixedShop, boolean isFavorite, long listId, int amount,
                    boolean isPromotion, boolean isBought) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.note = note;
        this.category = category;
        this.shop = shop;
        this.unit = unit;
        this.isFixedShop = isFixedShop;
        this.isFavorite = isFavorite;
        this.listId = listId;
        this.amount = amount;
        this.isPromotion = isPromotion;
        this.isBought = isBought;
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

    public byte[] getImage(Context context) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.bananas);
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, out);

        return out.toByteArray();
    }

    public String getNote() {
        return note;
    }

    public String getCategory() {
        return category;
    }

    public int getShop() {
        return shop;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isFixedShop() {
        return isFixedShop;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public long getListId() {
        return listId;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    public boolean isBought() {
        return isBought;
    }

    public String getCompleteNote() {
        /*String completeNote = (note == null ? "" : note);
        if (amount > 0) {
            if (note != null) {
                completeNote += " - ";
            }
            completeNote += Integer.toString(amount);
            completeNote += (unit == null ? "" : " " + unit);
        }*/
        String completeNote = Long.toString(id) + category;

        return completeNote;
    }

    public static ListItem fromCursor(Cursor cursor) {
        if (cursor == null)
            return null;

        long id = cursor.getLong(cursor.getColumnIndex(shoppingListContract.ListItem._ID));
        String name = cursor.getString(cursor.getColumnIndex(shoppingListContract.Item.COLUMN_NAME));
        byte[] image = cursor.getBlob(cursor.getColumnIndex(shoppingListContract.Item.COLUMN_IMAGE));
        String note = cursor.getString(cursor.getColumnIndex(shoppingListContract.Item.COLUMN_NOTE));
        String category = cursor.getString
                (cursor.getColumnIndex(shoppingListContract.Category.COLUMN_NAME));
        int shop = cursor.getInt(cursor.getColumnIndex(shoppingListContract.Shop.COLUMN_IMAGE_ID));
        String unit = cursor.getString(cursor.getColumnIndex(shoppingListContract.Unit.COLUMN_NAME));
        boolean isFixedShop = cursor.getInt
                (cursor.getColumnIndex(shoppingListContract.Item.COLUMN_FIXED_SHOP)) > 0;
        boolean isFavorite = cursor.getInt
                (cursor.getColumnIndex(shoppingListContract.Item.COLUMN_FAVORITE)) > 0;
        long listId = cursor.getLong
                (cursor.getColumnIndex(shoppingListContract.ListItem.COLUMN_LIST_ID));
        int amount = cursor.getInt
                (cursor.getColumnIndex(shoppingListContract.ListItem.COLUMN_AMOUNT));
        boolean isPromotion = cursor.getInt
                (cursor.getColumnIndex(shoppingListContract.ListItem.COLUMN_PROMOTION)) > 0;
        boolean isBought = cursor.getInt
                (cursor.getColumnIndex(shoppingListContract.ListItem.COLUMN_BOUGHT)) > 0;

        return new ListItem(id, name, image, note, category, shop, unit, isFixedShop, isFavorite,
                listId, amount, isPromotion, isBought);
    }
}
