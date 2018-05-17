package com.wuyts.nik.boodschappenlijst;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wuyts.nik.boodschappenlijst.data.CatalogueItem;
import com.wuyts.nik.boodschappenlijst.data.Favorite;
import com.wuyts.nik.boodschappenlijst.data.ShoppingListDbHelper;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ShoppingListDbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String TAG = "AddItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Database
        mDbHelper = new ShoppingListDbHelper(this);
        mDb = mDbHelper.getReadableDatabase();

        // Toolbar
        Toolbar addItemToolbar = findViewById(R.id.add_item_toolbar);
        setSupportActionBar(addItemToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        // TODO: search action, see "Creating a Search Interface"
        return true;
    }

    // React to selection of items in the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // TODO: search items
                return true;
            default:
                // Invoke the superclass to handle the action
                return super.onOptionsItemSelected(item);
        }
    }

    // Set up ViewPager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Get favorites data
        Cursor favoritesCursor = mDbHelper.getFavorites(mDb);
        Fragment favFragment;
        Bundle bundle = new Bundle();

        // Setup favorites fragment
        Log.i(TAG, Integer.toString(favoritesCursor.getCount()));
        if (favoritesCursor.getCount() > 0)
        {
            ArrayList<Favorite> favoritesList = Favorite.fromCursor(favoritesCursor);
            favoritesCursor.close();
            favFragment = new FavoritesFragment();
            bundle.putParcelableArrayList("favoritesList", favoritesList);
            favFragment.setArguments(bundle);
        } else {
            favoritesCursor.close();
            favFragment = new EmptyListFragment();
            //favBundle.putString("message", getResources().getString(R.string.empty_favorites));
        }

        adapter.addFragment(favFragment, getResources().getString(R.string.favorites));

        // Get category data
        Cursor categoryCursor = mDbHelper.getCategories(mDb);
        ArrayList<String> categoryList = CatalogueItem.categoriesFromCursor(categoryCursor);
        categoryCursor.close();
        bundle.clear();
        bundle.putStringArrayList("categoryList", categoryList);
        Fragment catalogueFragment = new CatalogueFragment();
        catalogueFragment.setArguments(bundle);
        adapter.addFragment(catalogueFragment, getResources().getString(R.string.catalogue));

        viewPager.setAdapter(adapter);

    }

    // ViewPager fragment setting adapter class
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mTitleList.add(title);
        }
    }
}
