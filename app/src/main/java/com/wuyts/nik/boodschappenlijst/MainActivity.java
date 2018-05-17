package com.wuyts.nik.boodschappenlijst;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wuyts.nik.boodschappenlijst.data.ShoppingListDbHelper;
import com.wuyts.nik.boodschappenlijst.data.shoppingListContract;


public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mFab;
    private ShoppingListDbHelper mDbHelper;
    private Cursor mListItemCursor;
    private SQLiteDatabase mDb;
    private DrawerLayout mDrawerLayout;
    private boolean mSortCategory = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Floating action button
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        // Database: get list items
        mDbHelper = new ShoppingListDbHelper(this) ;
        mDb = mDbHelper.getReadableDatabase();
        mListItemCursor = mDbHelper.getListItemData(mDb, mSortCategory);

        // RecyclerView
        RecyclerView itemList = findViewById(R.id.rv_list_items);
        itemList.setLayoutManager(new LinearLayoutManager(this));
        itemList.setHasFixedSize(true);
        itemList.setAdapter(new ItemAdapter(mListItemCursor));
        // Hide FAB when scrolling down
        itemList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mFab.getVisibility() == View.VISIBLE) {
                    mFab.hide();
                } else if (dy < 0 && mFab.getVisibility() != View.VISIBLE) {
                    mFab.show();
                }
            }
        });

        // Toolbar
        Toolbar mainToolbar  = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // Navigation drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);

        // React to selection of items in the navigation drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    // close drawer when item is tapped
                    mDrawerLayout.closeDrawers();

                    // Add code here to update the UI based on the item selected
                    //TODO: react to navigation items

                    return true;
                }
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mListItemCursor.close();
        mDb.close();
        mDbHelper.close();
    }

    // Show menu in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // React to selection of items in the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sortAbc:
                mSortCategory = false;
                return true;
            case R.id.action_sortCategory:
                mSortCategory = true;
                return true;
            case R.id.action_sortManual:
                //TODO: sort manually
                return true;
            // Hamburger selected: show navigation drawer
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                // Invoke the superclass to handle the action
                return super.onOptionsItemSelected(item);
        }
    }

    // Helper function to retrieve ListItem data
    private Cursor getListItemData(SQLiteDatabase db) {
        String table = shoppingListContract.Item.TABLE_NAME +
                " LEFT JOIN " + shoppingListContract.Category.TABLE_NAME +
                " ON " + shoppingListContract.Item.COLUMN_CATEGORY_ID +
                "=" + shoppingListContract.Category.TABLE_NAME +
                "." + shoppingListContract.Category._ID +
                " LEFT JOIN " + shoppingListContract.Shop.TABLE_NAME +
                " ON " + shoppingListContract.Item.COLUMN_SHOP_ID +
                "=" + shoppingListContract.Shop.TABLE_NAME +
                "." + shoppingListContract.Shop._ID +
                " LEFT JOIN " + shoppingListContract.Unit.TABLE_NAME +
                " ON " + shoppingListContract.Item.COLUMN_UNIT_ID +
                "=" + shoppingListContract.Unit.TABLE_NAME +
                "." + shoppingListContract.Unit._ID +
                " LEFT JOIN " + shoppingListContract.ListItem.TABLE_NAME +
                " ON " + shoppingListContract.Item.TABLE_NAME +
                "." + shoppingListContract.Item._ID +
                "=" + shoppingListContract.ListItem.COLUMN_ITEM_ID;
        String[] columns = {
                shoppingListContract.Item.TABLE_NAME + "." + shoppingListContract.Item._ID,
                //shoppingListContract.ListItem.TABLE_NAME + "." + shoppingListContract.ListItem._ID,
                shoppingListContract.Item.COLUMN_NAME,
                shoppingListContract.Item.COLUMN_IMAGE,
                shoppingListContract.Item.COLUMN_NOTE,
                shoppingListContract.Category.COLUMN_NAME,
                shoppingListContract.Shop.COLUMN_NAME,
                shoppingListContract.Unit.COLUMN_NAME,
                shoppingListContract.Item.COLUMN_FIXED_SHOP,
                shoppingListContract.Item.COLUMN_FAVORITE,
                shoppingListContract.ListItem.COLUMN_LIST_ID,
                shoppingListContract.ListItem.COLUMN_AMOUNT,
                shoppingListContract.ListItem.COLUMN_PROMOTION,
                shoppingListContract.ListItem.COLUMN_BOUGHT
        };
        String orderBy = (mSortCategory ? shoppingListContract.Category.TABLE_NAME +
                "." + shoppingListContract.Category._ID + ", " : "") +
                shoppingListContract.Item.COLUMN_NAME;
        return db.query(true, table, columns, null, null,
                null, null, orderBy, null);
    }
}
