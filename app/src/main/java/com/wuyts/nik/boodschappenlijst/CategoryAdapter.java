package com.wuyts.nik.boodschappenlijst;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private ArrayList<String> mArrayList;
    private Context mContext;
    private final ListItemClickListener mOnClickListener;

    CategoryAdapter(ArrayList<String> arrayList, ListItemClickListener listener) {
        mArrayList = arrayList;
        mOnClickListener = listener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        String category = mArrayList.get(position);
        holder.mNameTV.setText(category);
    }

    @Override
    public int getItemCount() {
        return mArrayList != null ? mArrayList.size() : 0;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // Declaration of views in view holder
        TextView mNameTV;

        CategoryViewHolder(View categoryView) {
            super(categoryView);
            mNameTV = categoryView.findViewById(R.id.tv_category_name);
            categoryView.setOnClickListener(this);
        }

        // Set click listener on CategoryViewHolder
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
