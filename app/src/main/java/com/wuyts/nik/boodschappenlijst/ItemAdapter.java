package com.wuyts.nik.boodschappenlijst;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyts.nik.boodschappenlijst.data.ListItem;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Cursor mCursor;
    private boolean mDataValid;
    private int mIdColumn;
    //private static final String TAG = "ItemAdapter";

    public ItemAdapter(Cursor cursor) {
        mCursor = cursor;
        mDataValid = mCursor != null;
        mIdColumn = mDataValid ? mCursor.getColumnIndex("_id") : -1;
        setHasStableIds(true);
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

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("only to be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("cursor could not move to position " + position);
        }

        ListItem listItem = ListItem.fromCursor(mCursor);
        byte[] itemImage = listItem.getImage();
        if (itemImage == null) {
            holder.mItemIV.setVisibility(View.INVISIBLE);
        } else {
            holder.mItemIV.setImageBitmap
                    (BitmapFactory.decodeByteArray(itemImage, 0, itemImage.length));
        }
        holder.mNameTV.setText(listItem.getName());
        if (listItem.isBought()) {
            holder.mNameTV.setPaintFlags
                    (holder.mNameTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        String note = listItem.getCompleteNote();
        if (note == null) {
            holder.mNoteTV.setVisibility(View.INVISIBLE);
        } else {
            holder.mNoteTV.setText(note);
        }
        if (!listItem.isPromotion()) {
            holder.mPromotionIV.setVisibility(View.INVISIBLE);
        }
        if (listItem.isFixedShop()) {
            byte[] shop = listItem.getShop();
            holder.mShopIV.setImageBitmap(BitmapFactory.decodeByteArray(shop, 0, shop.length));
        } else {
            holder.mShopIV.setVisibility(View.INVISIBLE);
        }

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
}
