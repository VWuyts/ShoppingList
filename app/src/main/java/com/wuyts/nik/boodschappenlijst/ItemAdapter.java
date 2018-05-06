package com.wuyts.nik.boodschappenlijst;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Cursor mCursor;
    private boolean mDataValid;
    private int mIdColumn;

    public ItemAdapter(Cursor cursor) {
        mCursor = cursor;
        mDataValid = cursor != null;
        mIdColumn = mDataValid ? mCursor.getColumnIndex("_id") : -1;
        setHasStableIds(true);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);

        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.mNameTV.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return mDataValid ? mCursor.getCount() : 0;
    }

    public long getItemId(int position) {
        if (mDataValid && mCursor.moveToPosition(position))
            return mCursor.getLong(mIdColumn);
        return 0;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        // Declaration of views in view holder
        ImageView mItemIV;
        TextView mNameTV;
        TextView mNoteTV;
        ImageView mPromotionIV;
        ImageView mShopIV;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mItemIV = itemView.findViewById(R.id.iv_item);
            mNameTV = itemView.findViewById(R.id.tv_name);
            mNoteTV = itemView.findViewById(R.id.tv_note);
            mPromotionIV = itemView.findViewById(R.id.iv_promotion);
            mShopIV = itemView.findViewById(R.id.iv_shop);

            // TODO: set click listener on itemView
        }
    }
}
