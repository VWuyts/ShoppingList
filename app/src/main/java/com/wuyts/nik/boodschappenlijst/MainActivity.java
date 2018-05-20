package com.wuyts.nik.boodschappenlijst;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
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


public class MainActivity extends AppCompatActivity
        implements ItemAdapter.ListItemClickListener {

    private Cursor mListItemCursor;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mFab;
    private ItemAdapter mItemAdapter;
    private ShoppingListDbHelper mDbHelper;
    private SQLiteDatabase mDb;
    public static final String LIST_ID_KEY = "ListId";
    private boolean mSortCategory = true;
    private long mListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Database: get list id and list items
        mDbHelper = new ShoppingListDbHelper(this) ;
        mDb = mDbHelper.getReadableDatabase();
        mListId = mDbHelper.getDefaultListId(mDb);
        mListItemCursor = mDbHelper.getListItemData(mDb, mSortCategory);

        // RecyclerView
        RecyclerView rvItemList = findViewById(R.id.rv_list_items);
        View emptyList = findViewById(R.id.include_empty_list);
        if (mListItemCursor.getCount() > 0) {
            rvItemList.setVisibility(View.VISIBLE);
            emptyList.setVisibility(View.GONE);
            rvItemList.setLayoutManager(new LinearLayoutManager(this));
            rvItemList.setHasFixedSize(true);
            mItemAdapter = new ItemAdapter(mListItemCursor, this);
            rvItemList.setAdapter(mItemAdapter);
            // Hide FAB when scrolling down
            rvItemList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        } else {
            rvItemList.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
        }


        // Toolbar
        Toolbar mainToolbar  = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        // Navigation drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);

        // React to selection of items in the navigation drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    // close drawer when item is tapped
                    mDrawerLayout.closeDrawers();

                    // Add code here to update the UI based on the item selected
                    //TODO: react to navigation items

                    return true;
                }
            });

        // Floating action button
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                intent.putExtra(LIST_ID_KEY, mListId);
                startActivity(intent);
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
        Cursor oldCursor;
        switch (item.getItemId()) {
            case R.id.action_sortAbc:
                mSortCategory = false;
                mListItemCursor = mDbHelper.getListItemData(mDb, mSortCategory);
                oldCursor = mItemAdapter.swapCursor(mListItemCursor);
                if (oldCursor != null) {
                    oldCursor.close();
                }
                return true;
            case R.id.action_sortCategory:
                mSortCategory = true;
                mListItemCursor = mDbHelper.getListItemData(mDb, mSortCategory);
                oldCursor = mItemAdapter.swapCursor(mListItemCursor);
                if (oldCursor != null) {
                    oldCursor.close();
                }
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

    // Implement listener function
    @Override
    public void onListItemClick(int clickedIndexItem) {
        // TODO: implement listener function
    }
}
