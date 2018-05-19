package com.wuyts.nik.boodschappenlijst;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.wuyts.nik.boodschappenlijst.data.Favorite;
import com.wuyts.nik.boodschappenlijst.data.ShoppingListDbHelper;

import java.util.ArrayList;

public class AddCategoryItemActivity extends AppCompatActivity
        implements FavoritesAdapter.ListItemClickListener {

    private ShoppingListDbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String TAG = "AddCategoryItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category_item);

        // Toolbar
        Toolbar addCategoryItemToolbar = findViewById(R.id.add_category_item_toolbar);
        setSupportActionBar(addCategoryItemToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Database: get items of given category
        mDbHelper = new ShoppingListDbHelper(this);
        mDb = mDbHelper.getReadableDatabase();
        Intent intent = getIntent();
        String category = getResources().getString(R.string.cat_fruit);
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            category = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        Cursor cursor = mDbHelper.getItemData(mDb, category);
        ArrayList<Favorite> arrayList = Favorite.fromCursor(cursor);
        cursor.close();

        // Set RecyclerView
        RecyclerView rvItemList = findViewById(R.id.rv_category_items);
        rvItemList.setLayoutManager(new LinearLayoutManager(this));
        rvItemList.setHasFixedSize(true);
        rvItemList.setAdapter(new FavoritesAdapter(arrayList, this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDb.close();
        mDbHelper.close();
    }

    // Implement listener function
    @Override
    public void onListItemClick(int clickedIndexItem) {
        // TODO: implement listener function
        Log.d(TAG, "click heard");
    }
}
