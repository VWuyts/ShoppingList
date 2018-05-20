package com.wuyts.nik.boodschappenlijst.data;

import android.database.Cursor;

import java.util.ArrayList;

public class Category {

    private final long id;
    private final String name;

    private Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ArrayList<String> categoriesFromCursor(Cursor cursor) {
        ArrayList<String> arrayList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex
                    (shoppingListContract.Category.COLUMN_NAME));
            arrayList.add(name);
        }

        return arrayList;
    }
}
