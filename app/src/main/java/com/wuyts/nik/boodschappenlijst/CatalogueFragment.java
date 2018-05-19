package com.wuyts.nik.boodschappenlijst;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class CatalogueFragment extends Fragment {

    private View mView;
    //private static final String TAG = "CatalogueFragment";

    public CatalogueFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_catalogue, container, false);
        // Retrieve ArrayList
        Bundle args = getArguments();
        ArrayList<String> arrayList = args.getStringArrayList("categoryList");
        setRecyclerView(arrayList);

        return mView;
    }

    // Set RecyclerView
    private void setRecyclerView(ArrayList<String> arrayList) {
        RecyclerView recyclerView = mView.findViewById(R.id.rv_catalogue);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new CategoryAdapter(arrayList));
    }
}
