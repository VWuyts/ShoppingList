package com.wuyts.nik.boodschappenlijst;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyts.nik.boodschappenlijst.data.ListItem;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private final ListItemClickListener mOnClickListener;
    private boolean mDataValid;
    private int mIdColumn;
    //private static final String TAG = "ItemAdapter";

    ItemAdapter(Cursor cursor, ListItemClickListener listener) {
        mCursor = cursor;
        mDataValid = mCursor != null;
        mIdColumn = mDataValid ? mCursor.getColumnIndex("_id") : -1;
        mOnClickListener = listener;
        setHasStableIds(true);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
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
        //byte[] itemImage = listItem.getImage(mContext);
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
        int shopImageId = listItem.getShopImageId();
        if (shopImageId > 0) {
            Drawable shopDrawable = mContext.getResources().getDrawable(shopImageId);
            holder.mShopIV.setImageDrawable(shopDrawable);
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

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (mCursor != null) {
            mDataValid = true;
            mIdColumn = mCursor.getColumnIndex("_id");
        } else {
            mDataValid = false;
            mIdColumn = -1;
        }
        notifyDataSetChanged();
        return oldCursor;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // Declaration of views in view holder
        final ImageView mItemIV;
        final TextView mNameTV;
        final TextView mNoteTV;
        final ImageView mPromotionIV;
        final ImageView mShopIV;

        ItemViewHolder(View itemView) {
            super(itemView);
            mItemIV = itemView.findViewById(R.id.iv_item);
            mNameTV = itemView.findViewById(R.id.tv_name);
            mNoteTV = itemView.findViewById(R.id.tv_note);
            mPromotionIV = itemView.findViewById(R.id.iv_promotion);
            mShopIV = itemView.findViewById(R.id.iv_shop);
            itemView.setOnClickListener(this);
        }

        // Set click listener on ItemViewHolder
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
