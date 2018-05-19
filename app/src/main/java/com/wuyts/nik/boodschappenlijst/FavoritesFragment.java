package com.wuyts.nik.boodschappenlijst;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuyts.nik.boodschappenlijst.data.Favorite;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment
        implements FavoritesAdapter.ListItemClickListener {

    private View mView;

    public FavoritesFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_favorites, container, false);
        // Retrieve ArrayList
        Bundle args = getArguments();
        ArrayList<Favorite> arrayList = args.getParcelableArrayList("favoritesList");
        setRecyclerView(arrayList);

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
        // TODO: implement listener function
    }
}
