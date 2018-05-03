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
        public static final String COLUMN_NAME_NAME = "Name";
    }

    // Inner class to define table Category
    public static class Category implements BaseColumns {
        public static final String TABLE_NAME = "Category";
        public static final String COLUMN_NAME_NAME = "Name";
    }

    // Inner class to define table Unit
    public static class Unit implements BaseColumns {
        public static final String TABLE_NAME = "Unit";
        public static final String COLUMN_NAME_NAME = "Name";
    }

    // Inner class to define table List
    public static class List implements BaseColumns {
        public static final String TABLE_NAME = "List";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_IS_RECIPE = "IsRecipe";
    }

    // Inner class to define table ShoppingOrder
    public static class ShoppingOrder implements BaseColumns {
        public static final String TABLE_NAME = "ShoppingOrder";
        public static final String COLUMN_NAME_SHOP_ID = "ShopID";
        public static final String COLUMN_NAME_CATEGORY_ID = "CategoryID";
        public static final String COLUMN_NAME_SEQUENCE = "Sequence";
    }

    // Inner class to define table Product
    public static class Product implements BaseColumns {
        public static final String TABLE_NAME = "Product";
        public static final String COLUMN_NAME_CATEGORY_ID = "CategoryID";
        public static final String COLUMN_NAME_SHOP_ID = "ShopID";
        public static final String COLUMN_NAME_UNIT_ID = "UnitID";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_IMAGE = "Image";
        public static final String COLUMN_NAME_NOTE = "Note";
        public static final String COLUMN_NAME_FIXED_SHOP = "FixedShop";
        public static final String COLUMN_NAME_FAVORITE= "IsFavorite";
    }

    // Inner class to define table ItemOnList
    public static class ItemOnList implements BaseColumns {
        public static final String TABLE_NAME = "ItemOnList";
        public static final String COLUMN_NAME_SHOPPING_LIST_ID = "ShoppingLisID";
        public static final String COLUMN_NAME_PRODUCT_ID = "ProductID";
        public static final String COLUMN_NAME_AMOUNT = "Amount";
        public static final String COLUMN_NAME_PROMOTION = "Promotion";
        public static final String COLUMN_NAME_BOUGHT = "Bought";
    }
}
