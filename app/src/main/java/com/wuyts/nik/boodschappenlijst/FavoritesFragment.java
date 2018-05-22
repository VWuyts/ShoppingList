package com.wuyts.nik.boodschappenlijst;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuyts.nik.boodschappenlijst.data.Favorite;
import com.wuyts.nik.boodschappenlijst.data.ShoppingListDbHelper;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment
        implements FavoritesAdapter.ListItemClickListener {

    private ArrayList<Favorite> mArrayList;
    private View mView;
    private long mListId;
    private static final String TAG = "FavoritesFragment";

    public FavoritesFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_favorites, container, false);
        // Retrieve bundle data
        Bundle args = getArguments();
        mArrayList = args.getParcelableArrayList(AddItemActivity.BUNDLE_FAV_LIST);
        mListId = args.getLong(AddItemActivity.BUNDLE_LIST_ID, 1);
        Log.d(TAG, "mArrayList size = " + Integer.toString(mArrayList.size()));
        setRecyclerView(mArrayList);

        return mView;
    }

    // Set RecyclerView
    private void setRecyclerView(ArrayList<Favorite> arrayList) {
        RecyclerView recyclerView = mView.findViewById(R.id.rv_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new FavoritesAdapter(arrayList, this));
    }

    // Implement listener function
    @Override
    public void onListItemClick(int clickedIndexItem) {
        // Add clicked item to list
        Favorite itemToAdd = mArrayList.get(clickedIndexItem);
        long itemId = itemToAdd.getId();
        ShoppingListDbHelper dbHelper = ((AddItemActivity)getActivity()).getDbHelper();
        SQLiteDatabase db = ((AddItemActivity)getActivity()).getDb();
        dbHelper.addListItem(db, itemId, mListId);
        // TODO: do not add 2x same item to list
        // TODO: add view to show which items are already on shopping list

        // Go back to main activity
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
