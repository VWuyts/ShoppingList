package com.wuyts.nik.boodschappenlijst;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.wuyts.nik.boodschappenlijst.data.ShoppingListDbHelper;

public class AddCategoryItemActivity extends AppCompatActivity {

    private ShoppingListDbHelper mDbHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category_item);

        // Database
        mDbHelper = new ShoppingListDbHelper(this);
        mDb = mDbHelper.getReadableDatabase();

        // Toolbar
        Toolbar addCategoryItemToolbar = findViewById(R.id.add_category_item_toolbar);
        setSupportActionBar(addCategoryItemToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDb.close();
        mDbHelper.close();
    }
}
