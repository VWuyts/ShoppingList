package com.wuyts.nik.boodschappenlijst;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.wuyts.nik.boodschappenlijst.data.ListItem;
import com.wuyts.nik.boodschappenlijst.data.ShoppingListDbHelper;
import com.wuyts.nik.boodschappenlijst.data.ShoppingListContract;

public class EditItemActivity extends AppCompatActivity {

    private Cursor mShopsCursor;
    private Cursor mUnitsCursor;
    private ListItem mListItem;
    private Menu mMenu;
    private MenuItem mFavAction;
    private ShoppingListDbHelper mdbHelper;
    private SQLiteDatabase mDb;
    private long mItemId = 0;
    private static final String TAG = "EditItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Database: get units and shops
        mdbHelper = new ShoppingListDbHelper(this);
        mDb = mdbHelper.getWritableDatabase();
        mUnitsCursor = mdbHelper.getUnits(mDb);
        mShopsCursor = mdbHelper.getShops(mDb);

        // Get units spinner
        View includeGeneral = findViewById(R.id.include_item_general);
        Spinner unitSpinner = includeGeneral.findViewById(R.id.sp_unit_edit_item);

        // Set cursor adapter for units spinner
        SimpleCursorAdapter spUnitAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item,
                mUnitsCursor, new String[] {ShoppingListContract.Unit.COLUMN_NAME},
                new int[] {android.R.id.text1}, 0);
        spUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(spUnitAdapter);

        // Get shops spinner
        View includeShopPromo = findViewById(R.id.include_item_shop_promotion);
        Spinner shopSpinner = includeShopPromo.findViewById(R.id.sp_shop_edit_item);

        // Set cursor adapter for shops spinner
        SimpleCursorAdapter spShopAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item,
                mShopsCursor, new String[] {ShoppingListContract.Shop.COLUMN_NAME},
                new int[] {android.R.id.text1}, 0);
        shopSpinner.setAdapter(spShopAdapter);

        // Retrieve itemId from intent
        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.ITEM_ID_KEY)) {
            mItemId = intent.getLongExtra(MainActivity.ITEM_ID_KEY, 0);
        }
        //Log.d(TAG, "itemId = " + mItemId);

        // Set data of ListItem
        Cursor itemCursor = mdbHelper.getListItem(mDb, mItemId);
        if (itemCursor.moveToNext()) {
            mListItem = ListItem.fromCursor(itemCursor);
            Log.d(TAG, "ListItem name = " + mListItem.getName());
        }

        // Setup views
        if (mListItem != null) {
            ImageView imageIV = includeGeneral.findViewById(R.id.iv_edit_item);
            TextView nameTV = includeGeneral.findViewById(R.id.tv_name_edit_item);
            TextView categoryTV = includeGeneral.findViewById(R.id.tv_category_edit_item);
            EditText amountET = includeGeneral.findViewById(R.id.et_amount_edit_item);
            EditText noteET = includeGeneral.findViewById(R.id.et_note_edit_item);
            CheckBox shopCB = includeShopPromo.findViewById(R.id.cb_shop_item_edit);
            CheckBox promoCB = includeShopPromo.findViewById(R.id.cb_promotion_item_edit);

            byte[] image = mListItem.getImage();
            if (image != null) {
                imageIV.setImageBitmap (BitmapFactory.decodeByteArray(image, 0, image.length));
            }
            else {
                imageIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_a_photo));
            }
            nameTV.setText(mListItem.getName());
            categoryTV.setText(mListItem.getCategory());
            int amount = mListItem.getAmount();
            if (amount > 0) {
                amountET.setText(amount);
            }
            String unit = mListItem.getUnit();
            int index = getIndex(unitSpinner, unit);
            if (unit != null && index >= 0) {
                unitSpinner.setSelection(index);
            }
            String note = mListItem.getNote();
            if (note != null) {
                noteET.setText(note);
            }
            boolean isFixedShop = mListItem.isFixedShop();
            String shop = mListItem.getShop();
            index = getIndex(shopSpinner, shop);
            if (shop != null && index >= 0 && isFixedShop) {
                shopSpinner.setSelection(index);
                shopCB.setChecked(true);
            }
            boolean isPromotion = mListItem.isPromotion();
            if (isPromotion) {
                promoCB.setChecked(true);
            }
        }

        // Toolbar
        Toolbar editItemToolbar = findViewById(R.id.edit_item_toolbar);
        setSupportActionBar(editItemToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.menu_edit_item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnitsCursor.close();
        mShopsCursor.close();
        mDb.close();
        mdbHelper.close();
    }

    // Show menu in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item_menu, menu);
        mMenu = menu;
        mFavAction = mMenu.findItem(R.id.action_favorite);
        setFavoriteIcon(mListItem.isFavorite());

        return true;
    }

    // React to selection of items in the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                 boolean isFavorite = mdbHelper.toggleFavorite(mDb, mListItem.getItemId());
                 setFavoriteIcon(isFavorite);
                return true;
            default:
                // Invoke the superclass to handle the action
                return super.onOptionsItemSelected(item);
        }
    }

    // Helper function to get index of value in spinner
    private int getIndex(@NonNull Spinner spinner, String value) {
        for (int index = 0; index < spinner.getCount(); index++) {
            if (spinner.getItemAtPosition(index).equals(value)) {
                return index;
            }
        }

        return -1;
    }

    // Helper function to set correct favorites icon in menu
    private void setFavoriteIcon(boolean isFavorite) {
        mFavAction = mMenu.findItem(R.id.action_favorite);
        if (isFavorite) {
            mFavAction.setIcon(R.drawable.ic_favorite);
        }
        else {
            mFavAction.setIcon(R.drawable.ic_favorite_border);
        }
    }
}
