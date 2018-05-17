package com.wuyts.nik.boodschappenlijst.data;

import android.provider.BaseColumns;

/**
 * Created by Veronique Wuyts on 26/04/2018.
 */

public final class shoppingListContract {
    // Private constructor to prevent instantiating the contract class
    private shoppingListContract() {}

    // Inner class to define table Shop
    public static class Shop implements BaseColumns {
        public static final String TABLE_NAME = "Shop";
        public static final String COLUMN_NAME = "SName";
        public static final String COLUMN_IMAGE_ID = "ImageID";
    }

    // Inner class to define table Category
    public static class Category implements BaseColumns {
        public static final String TABLE_NAME = "Category";
        public static final String COLUMN_NAME = "CName";
    }

    // Inner class to define table Unit
    public static class Unit implements BaseColumns {
        public static final String TABLE_NAME = "Unit";
        public static final String COLUMN_NAME = "UName";
    }

    // Inner class to define table List
    public static class List implements BaseColumns {
        public static final String TABLE_NAME = "List";
        public static final String COLUMN_NAME = "LName";
        public static final String COLUMN_IS_RECIPE = "IsRecipe";
    }

    // Inner class to define table ShoppingOrder
    public static class ShoppingOrder implements BaseColumns {
        public static final String TABLE_NAME = "ShoppingOrder";
        public static final String COLUMN_SHOP_ID = "ShopID";
        public static final String COLUMN_CATEGORY_ID = "CategoryID";
        public static final String COLUMN_SEQUENCE = "Sequence";
    }

    // Inner class to define table CatalogueItem
    public static class Item implements BaseColumns {
        public static final String TABLE_NAME = "Item";
        public static final String COLUMN_CATEGORY_ID = "CategoryID";
        public static final String COLUMN_SHOP_ID = "ShopID";
        public static final String COLUMN_UNIT_ID = "UnitID";
        public static final String COLUMN_NAME = "IName";
        public static final String COLUMN_IMAGE = "Image";
        public static final String COLUMN_NOTE = "Note";
        public static final String COLUMN_FIXED_SHOP = "FixedShop";
        public static final String COLUMN_FAVORITE= "IsFavorite";
    }

    // Inner class to define table ListItem
    public static class ListItem implements BaseColumns {
        public static final String TABLE_NAME = "ListItem";
        public static final String COLUMN_LIST_ID = "ListID";
        public static final String COLUMN_ITEM_ID = "ItemID";
        public static final String COLUMN_AMOUNT = "Amount";
        public static final String COLUMN_PROMOTION = "Promotion";
        public static final String COLUMN_BOUGHT = "Bought";
    }
}
