package com.wuyts.nik.boodschappenlijst;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.wuyts.nik.boodschappenlijst.data.ListItem;
import com.wuyts.nik.boodschappenlijst.data.ShoppingListDbHelper;
import com.wuyts.nik.boodschappenlijst.data.ShoppingListContract;

public class EditItemActivity extends AppCompatActivity {

    private CheckBox mPromoCB;
    private CheckBox mShopCB;
    private Cursor mShopsCursor;
    private Cursor mUnitsCursor;
    private EditText mAmountET;
    private EditText mNoteET;
    private ImageView mItemIV;
    private ListItem mListItem;
    private Menu mMenu;
    private MenuItem mFavAction;
    private ShoppingListDbHelper mdbHelper;
    private SimpleCursorAdapter mUnitAdapter;
    private SimpleCursorAdapter mShopAdapter;
    private Spinner mShopSpinner;
    private Spinner mUnitSpinner;
    private SQLiteDatabase mDb;
    private long mListItemId;
    private static final String TAG = "EditItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Retrieve data from intent
        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.LIST_ITEM_ID_KEY)) {
            mListItemId = intent.getLongExtra(MainActivity.LIST_ITEM_ID_KEY, 0);
        }

        // Retrieve Views
        View includeGeneral = findViewById(R.id.include_item_general);
        View includeShopPromo = findViewById(R.id.include_item_shop_promotion);
        mItemIV = includeGeneral.findViewById(R.id.iv_edit_item);
        mAmountET = includeGeneral.findViewById(R.id.et_amount_edit_item);
        mUnitSpinner = includeGeneral.findViewById(R.id.sp_unit_edit_item);
        mNoteET = includeGeneral.findViewById(R.id.et_note_edit_item);
        mShopSpinner = includeShopPromo.findViewById(R.id.sp_shop_edit_item);
        mShopCB = includeShopPromo.findViewById(R.id.cb_shop_item_edit);
        mPromoCB = includeShopPromo.findViewById(R.id.cb_promotion_item_edit);

        // Database: get units and shops
        mdbHelper = new ShoppingListDbHelper(this);
        mDb = mdbHelper.getWritableDatabase();
        mUnitsCursor = mdbHelper.getUnits(mDb);
        mShopsCursor = mdbHelper.getShops(mDb);

        // Set cursor adapter for units spinner
        mUnitAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item,
                mUnitsCursor, new String[] {ShoppingListContract.Unit.COLUMN_NAME},
                new int[] {android.R.id.text1}, 0);
        mUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUnitSpinner.setAdapter(mUnitAdapter);

        // Set cursor adapter for shops spinner
        mShopAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item,
                mShopsCursor, new String[] {ShoppingListContract.Shop.COLUMN_NAME},
                new int[] {android.R.id.text1}, 0);
        mShopAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mShopSpinner.setAdapter(mShopAdapter);

        // Set data of ListItem
        Cursor itemCursor = mdbHelper.getListItem(mDb, mListItemId);
        if (itemCursor.moveToNext()) {
            mListItem = ListItem.fromCursor(itemCursor);
            Log.d(TAG, "ListItem name = " + mListItem.getName());
        }

        // Setup views
        if (mListItem != null) {
            TextView nameTV = includeGeneral.findViewById(R.id.tv_name_edit_item);
            TextView categoryTV = includeGeneral.findViewById(R.id.tv_category_edit_item);

            byte[] image = mListItem.getImage();
            if (image != null) {
                mItemIV.setImageBitmap (BitmapFactory.decodeByteArray(image, 0, image.length));
            }
            else {
                mItemIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_a_photo));
            }
            nameTV.setText(mListItem.getName());
            categoryTV.setText(mListItem.getCategory());
            int amount = mListItem.getAmount();
            if (amount > 0) {
                mAmountET.setText(Integer.toString(amount));
            }
            String unit = mListItem.getUnit();
            int index = getUnitIndex(unit);
            if (unit != null && index >= 0) {
                mUnitSpinner.setSelection(index);
            }
            String note = mListItem.getNote();
            if (note != null) {
                mNoteET.setText(note);
            }
            String shop = mListItem.getShop();
            index = getShopIndex(shop);
            if (shop != null && index >= 0) {
                mShopSpinner.setSelection(index);
            }
            if (mListItem.isFixedShop()) {
                mShopCB.setChecked(true);
            }
            if (mListItem.isPromotion()) {
                mPromoCB.setChecked(true);
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

    // OnClick handler for remove button
    public void removeFromList(View view) {
        // Delete item from list
        mdbHelper.deleteListItem(mDb, mListItemId);

        // Go back to main activity
        Intent intent = new Intent(EditItemActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // onClick handler for done button
    public void editListItem(View view) {
        // TODO: change picture

        // Get values which may be changed
        String amountString = mAmountET.getText().toString().trim();
        int amount = amountString.length() > 0 ? Integer.parseInt(amountString) : 0;
        long unitId = -1;
        if (amount > 0) {
            int position = mUnitSpinner.getSelectedItemPosition();
            Cursor cursor = (Cursor) mUnitAdapter.getItem(position);
            unitId = cursor.getLong(cursor.getColumnIndex(ShoppingListContract.Unit._ID));
            cursor.close();
        }
        String note = mNoteET.getText().toString().trim();
        if (note.length() == 0) {
            note = null;
        }
        long shopId = -1;
        int position = mShopSpinner.getSelectedItemPosition();
        if (position > 0) {
            Cursor cursor = (Cursor) mShopAdapter.getItem(position);
            shopId = cursor.getLong(cursor.getColumnIndex(ShoppingListContract.Shop._ID));
            cursor.close();
        }
        int isFixedShop = mShopCB.isChecked() ? 1 : 0;
        int promotion = mPromoCB.isChecked() ? 1 : 0;

        // Edit item in database
        mdbHelper.editListItem(mDb, mListItemId, mListItem.getItemId(), amount, unitId, note,
                shopId, isFixedShop, promotion);

        // Go back to main activity
        Intent intent = new Intent(EditItemActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // onClick handler for ImageView with picture of ListItem
    public void getPicture(View view) {
        Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
    }

    // Helper function to get index of value in UnitSpinner
    private int getUnitIndex(String value) {
        if (value == null)
            return -1;

        Cursor cursor;
        for (int index = 0; index < mUnitAdapter.getCount(); index++) {
            cursor = (Cursor) mUnitAdapter.getItem(index);
            String unit = cursor.getString(cursor
                    .getColumnIndex(ShoppingListContract.Unit.COLUMN_NAME));
            if (unit.equals(value)) {
                return index;
            }
        }

        return -1;
    }

    // Helper function to get index of value in ShopSpinner
    private int getShopIndex(String value) {
        if (value == null)
            return -1;

        Cursor cursor;
        for (int index = 0; index < mShopAdapter.getCount(); index++) {
            cursor = (Cursor) mShopAdapter.getItem(index);
            String unit = cursor.getString(cursor
                    .getColumnIndex(ShoppingListContract.Shop.COLUMN_NAME));
            if (unit.equals(value)) {
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
