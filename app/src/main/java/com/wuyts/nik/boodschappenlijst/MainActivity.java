package com.wuyts.nik.boodschappenlijst;

import android.database.Cursor;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Cursor mItemCursor;
    private RecyclerView mItemList;
    private RecyclerView.Adapter mItemAdapter;
    private RecyclerView.LayoutManager mlayoutManager;
    private DrawerLayout mDrawerLayout;
    //TODO: Create Shop, Unit, Category and Item class
    //TODO: Create ArrayLists for Shop, Unit and Category and populate them
    /*private ArrayList<String> shopArray;
    private ArrayList<Long> shopIdArray;
    private ArrayList<Integer> shopImageArray;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Database: get shop, category and unit data
        // mItemCursor =

        // RecyclerView
        mItemList = findViewById(R.id.rv_list_items);
        mlayoutManager = new LinearLayoutManager(this);
        mItemList.setLayoutManager(mlayoutManager);
        mItemList.setHasFixedSize(true);
        mItemAdapter = new ItemAdapter(mItemCursor);
        mItemList.setAdapter(mItemAdapter);

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

    // Show menu toolbar
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
                //TODO: sort alphabetically
                return true;
            case R.id.action_sortCategory:
                //TODO: sort per category
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
}
