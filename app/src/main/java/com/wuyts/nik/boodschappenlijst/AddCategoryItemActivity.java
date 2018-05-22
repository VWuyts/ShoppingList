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
//import android.util.Log;

import com.wuyts.nik.boodschappenlijst.data.Favorite;
import com.wuyts.nik.boodschappenlijst.data.ShoppingListDbHelper;

import java.util.ArrayList;

public class AddCategoryItemActivity extends AppCompatActivity
        implements FavoritesAdapter.ListItemClickListener {

    private ArrayList<Favorite> mArrayList;
    private ShoppingListDbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private long mListId;
    //private static final String TAG = "AddCategoryItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category_item);

        // Retrieve list id
        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.LIST_ID_KEY)) {
            mListId = intent.getLongExtra(MainActivity.LIST_ID_KEY, 1);
        }

        // Database: get items of given category
        mDbHelper = new ShoppingListDbHelper(this);
        mDb = mDbHelper.getWritableDatabase(); // write needed to add item to shopping list
        String category = getResources().getString(R.string.cat_fruit);
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            category = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        Cursor cursor = mDbHelper.getItemData(mDb, category);
        mArrayList = Favorite.fromCursor(cursor);
        cursor.close();

        // Set RecyclerView
        RecyclerView rvItemList = findViewById(R.id.rv_category_items);
        rvItemList.setLayoutManager(new LinearLayoutManager(this));
        rvItemList.setHasFixedSize(true);
        rvItemList.setAdapter(new FavoritesAdapter(mArrayList, this));

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

    // Implement listener function
    @Override
    public void onListItemClick(int clickedIndexItem) {
        // Add clicked item to list
        Favorite itemToAdd = mArrayList.get(clickedIndexItem);
        long itemId = itemToAdd.getId();
        mDbHelper.addListItem(mDb, itemId, mListId);
        // TODO: do not add 2x same item to list
        // TODO: add view to show which items are already on shopping list

        // Go back to main activity
        Intent intent = new Intent(AddCategoryItemActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
