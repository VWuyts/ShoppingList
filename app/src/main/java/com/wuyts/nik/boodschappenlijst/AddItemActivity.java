package com.wuyts.nik.boodschappenlijst;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
//import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.wuyts.nik.boodschappenlijst.data.Category;
import com.wuyts.nik.boodschappenlijst.data.Favorite;
import com.wuyts.nik.boodschappenlijst.data.ShoppingListDbHelper;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    private ShoppingListDbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private ViewPager mViewPager;
    private long mListId;
    //private static final String TAG = "AddItemActivity";
    public static final String BUNDLE_CAT_LIST = "categoryList";
    public static final String BUNDLE_FAV_LIST = "favoritesList";
    public static final String BUNDLE_LIST_ID = "listId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Retrieve list id
        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.LIST_ID_KEY)) {
            mListId = intent.getLongExtra(MainActivity.LIST_ID_KEY, 1);
        }

        // Database
        mDbHelper = new ShoppingListDbHelper(this);
        mDb = mDbHelper.getWritableDatabase(); // write needed in favorites fragment

        // ViewPager
        mViewPager = findViewById(R.id.view_pager);
        setupViewPager(mViewPager);

        // Set up TabLayout
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Toolbar
        Toolbar addItemToolbar = findViewById(R.id.add_item_toolbar);
        setSupportActionBar(addItemToolbar);
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

    // Show menu in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item_menu, menu);

        //MenuItem searchItem = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) searchItem.getActionView();
        // TODO: search action, google "android creating a search interface" + option to create new Item if not found
        return true;
    }

    // React to selection of items in the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast toast = Toast.makeText(this,
                getResources().getString(R.string.not_implemented), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        switch (item.getItemId()) {
            case R.id.action_search:
                toast.show();
                // TODO: search items
                return true;
            default:
                // Invoke the superclass to handle the action
                return super.onOptionsItemSelected(item);
        }
    }

    // Getter for ShoppingListDbHelper
    public ShoppingListDbHelper getDbHelper() {
        return mDbHelper;
    }

    // Getter for SQLiteDatabase
    public SQLiteDatabase getDb() {
        return mDb;
    }

    // Set up ViewPager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Get favorites data
        Cursor favoritesCursor = mDbHelper.getFavorites(mDb);
        Fragment favFragment;
        Bundle bundle = new Bundle();

        // Setup favorites fragment
        //Log.i(TAG, Integer.toString(favoritesCursor.getCount()));
        if (favoritesCursor.getCount() > 0)
        {
            ArrayList<Favorite> favoritesList = Favorite.fromCursor(favoritesCursor);
            favoritesCursor.close();
            favFragment = new FavoritesFragment();
            bundle.putParcelableArrayList(BUNDLE_FAV_LIST, favoritesList);
            bundle.putLong(BUNDLE_LIST_ID, mListId);
            favFragment.setArguments(bundle);
        } else {
            favoritesCursor.close();
            favFragment = new EmptyListFragment();
        }

        adapter.addFragment(favFragment, getResources().getString(R.string.favorites));

        // Get category data
        Cursor categoryCursor = mDbHelper.getCategories(mDb);
        ArrayList<String> categoryList = Category.categoriesFromCursor(categoryCursor);
        categoryCursor.close();
        bundle.clear();
        bundle.putStringArrayList(BUNDLE_CAT_LIST, categoryList);
        bundle.putLong(BUNDLE_LIST_ID, mListId);
        Fragment catalogueFragment = new CatalogueFragment();
        catalogueFragment.setArguments(bundle);
        adapter.addFragment(catalogueFragment, getResources().getString(R.string.catalogue));

        viewPager.setAdapter(adapter);

    }

    // ViewPager fragment setting adapter class
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }

        // Add fragment and title
        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mTitleList.add(title);
        }
    }
}
