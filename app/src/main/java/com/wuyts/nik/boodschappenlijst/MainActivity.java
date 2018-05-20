package com.wuyts.nik.boodschappenlijst;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wuyts.nik.boodschappenlijst.data.ShoppingListDbHelper;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity
        implements ItemAdapter.ListItemClickListener {

    private Button mAddBtn;
    private Cursor mListItemCursor;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mFab;
    private ItemAdapter mItemAdapter;
    private RecyclerView mItemList;
    private ShoppingListDbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private View  mEmptyList;
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
        mItemList = findViewById(R.id.rv_list_items);
        mItemList.setLayoutManager(new LinearLayoutManager(this));
        mItemList.setHasFixedSize(true);
        mItemAdapter = new ItemAdapter(mListItemCursor, this);
        mItemList.setAdapter(mItemAdapter);
        // Hide FAB when scrolling down
        mItemList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        // Empty list message
        mEmptyList = findViewById(R.id.include_empty_list);
        TextView message = mEmptyList.findViewById(R.id.tv_empty_list);
        message.setText(R.string.empty_list);
        mAddBtn = mEmptyList.findViewById(R.id.btn_empty_list);
        // Add click listener to button
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                intent.putExtra(LIST_ID_KEY, mListId);
                startActivity(intent);
            }
        });

        // Set correct visibility
        setVisibility();

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
                    mDrawerLayout.closeDrawers();
                    switch (menuItem.getItemId()) {
                        case R.id.drawer_list:
                            // if drawer closes, user is back in MainActivity
                            return true;
                        case R.id.drawer_recipe:
                        case R.id.drawer_shops:
                        case R.id.drawer_help:
                        case R.id.drawer_about:
                            Toast.makeText(MainActivity.this,
                                    getResources().getString(R.string.not_implemented),
                                    Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.drawer_settings:

                            return true;
                        case R.id.drawer_feedback:
                            // send mail
                            String[] addresses = {getResources().getString(R.string.email)};
                            Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                            mailIntent.setData(Uri.parse(getResources().getString(R.string.mailto)));
                            mailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
                            mailIntent.putExtra(Intent.EXTRA_SUBJECT,
                                    getResources().getString(R.string.email_subject));
                            if (mailIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(mailIntent);
                            }
                            return true;
                    }
                    return true;
                }
            });
        // React to selection of TextView in header of the navigation drawer
        View header = navigationView.getHeaderView(0);
        header.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,
                        getResources().getString(R.string.not_implemented),
                        Toast.LENGTH_SHORT).show();
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
        Toast toast = Toast.makeText(this,
                getResources().getString(R.string.not_implemented), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        switch (item.getItemId()) {
            case R.id.action_go_shopping:
                toast.show();
                // TODO: go shopping activity
                return true;
            case R.id.action_sortAbc:
                mSortCategory = false;
                changeCursor();
                return true;
            case R.id.action_sortCategory:
                mSortCategory = true;
                changeCursor();
                return true;
            case R.id.action_sortManual:
                toast.show();
                // TODO: sort manually
                return true;
            case R.id.action_share_list:
                toast.show();
                // TODO: share list activity
            case R.id.action_clear_list:
                mDbHelper.clearList(mDb, mListId);
                changeCursor();
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

    // Change the cursor
    private void changeCursor() {
        mListItemCursor = mDbHelper.getListItemData(mDb, mSortCategory);
        Cursor oldCursor = mItemAdapter.swapCursor(mListItemCursor);
        if (oldCursor != null) {
            oldCursor.close();
        }
        setVisibility();
    }

    // Set visibility: RecyclerView or EmptyList message
    private void setVisibility() {
        if (mListItemCursor.getCount() > 0) {
            mItemList.setVisibility(View.VISIBLE);
            mEmptyList.setVisibility(View.GONE);

        } else {
            mItemList.setVisibility(View.GONE);
            mAddBtn.setVisibility(View.VISIBLE);
            mEmptyList.setVisibility(View.VISIBLE);
        }
    }
}
