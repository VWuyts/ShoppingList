package com.wuyts.nik.boodschappenlijst.data;

//import android.content.Context;
import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;

//import java.io.ByteArrayOutputStream;

public class ListItem {
    private final  long id;
    private final String name;
    private final byte[] image;
    private final String note;
    private final String category;
    private final String shop;
    private final int shopImageId;
    private final String unit;
    private final boolean isFixedShop;
    private final boolean isFavorite;
    private final long listId;
    private final long itemId;
    private final int amount;
    private final boolean isPromotion;
    private final boolean isBought;

    private ListItem(long id, String name, byte[] image, String note, String category, String shop,
                     int shopImageId, String unit, boolean isFixedShop, boolean isFavorite,
                     long listId, long itemId, int amount, boolean isPromotion, boolean isBought) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.note = note;
        this.category = category;
        this.shop = shop;
        this.shopImageId = shopImageId;
        this.unit = unit;
        this.isFixedShop = isFixedShop;
        this.isFavorite = isFavorite;
        this.listId = listId;
        this.itemId = itemId;
        this.amount = amount;
        this.isPromotion = isPromotion;
        this.isBought = isBought;
    }

    public String getName() {
        return name;
    }

    public byte[] getImage() {
        return image;
    }

    /*public byte[] getImage(Context context) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.bananas);
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, out);

        return out.toByteArray();
    }*/

    public String getNote() {
        return note;
    }

    public String getCategory() {
        return category;
    }

    public String getShop() {
        return shop;
    }

    public int getShopImageId() {
        return shopImageId;
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

    public long getItemId() {
        return itemId;
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
        String completeNote = (note == null ? "" : note);
        if (amount > 0) {
            if (note != null) {
                completeNote += " - ";
            }
            completeNote += Integer.toString(amount);
            completeNote += (unit == null ? "" : " " + unit);
        }
        //String completeNote = Long.toString(id) + category;

        return completeNote;
    }

    public static ListItem fromCursor(Cursor cursor) {
        if (cursor == null)
            return null;

        long id = cursor.getLong(cursor.getColumnIndex(ShoppingListContract.ListItem._ID));
        String name = cursor.getString(cursor.getColumnIndex(ShoppingListContract.Item.COLUMN_NAME));
        byte[] image = cursor.getBlob(cursor.getColumnIndex(ShoppingListContract.Item.COLUMN_IMAGE));
        String note = cursor.getString(cursor.getColumnIndex(ShoppingListContract.Item.COLUMN_NOTE));
        String category = cursor.getString
                (cursor.getColumnIndex(ShoppingListContract.Category.COLUMN_NAME));
        String shop = cursor.getString(cursor.getColumnIndex(ShoppingListContract.Shop.COLUMN_NAME));
        int shopImageId = cursor.getInt(cursor.getColumnIndex(ShoppingListContract.Shop.COLUMN_IMAGE_ID));
        String unit = cursor.getString(cursor.getColumnIndex(ShoppingListContract.Unit.COLUMN_NAME));
        boolean isFixedShop = cursor.getInt
                (cursor.getColumnIndex(ShoppingListContract.Item.COLUMN_FIXED_SHOP)) > 0;
        boolean isFavorite = cursor.getInt
                (cursor.getColumnIndex(ShoppingListContract.Item.COLUMN_FAVORITE)) > 0;
        long listId = cursor.getLong
                (cursor.getColumnIndex(ShoppingListContract.ListItem.COLUMN_LIST_ID));
        long itemId = cursor.getLong
                (cursor.getColumnIndex(ShoppingListContract.ListItem.COLUMN_ITEM_ID));
        int amount = cursor.getInt
                (cursor.getColumnIndex(ShoppingListContract.ListItem.COLUMN_AMOUNT));
        boolean isPromotion = cursor.getInt
                (cursor.getColumnIndex(ShoppingListContract.ListItem.COLUMN_PROMOTION)) > 0;
        boolean isBought = cursor.getInt
                (cursor.getColumnIndex(ShoppingListContract.ListItem.COLUMN_BOUGHT)) > 0;

        return new ListItem(id, name, image, note, category, shop, shopImageId, unit, isFixedShop,
                isFavorite, listId, itemId, amount, isPromotion, isBought);
    }
}
