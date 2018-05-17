package com.wuyts.nik.boodschappenlijst;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyts.nik.boodschappenlijst.data.Favorite;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private Context mContext;
    private ArrayList<Favorite> mArrayList;

    public FavoritesAdapter(ArrayList<Favorite> arrayList) {
        mArrayList = arrayList;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        Favorite favorite = mArrayList.get(position);
        byte[] favImage = favorite.getImage();
        if (favImage == null) {
            holder.mItemIV.setVisibility(View.INVISIBLE);
        } else {
            holder.mItemIV.setImageBitmap
                    (BitmapFactory.decodeByteArray(favImage, 0, favImage.length));
        }
        holder.mNameTV.setText(favorite.getName());
        if (favorite.isFixedShop()) {
            int shop = favorite.getShop();
            Drawable shopDrawable = mContext.getResources().getDrawable(shop);
            holder.mShopIV.setImageDrawable(shopDrawable);
        } else {
            holder.mShopIV.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList != null ? mArrayList.size() : 0;
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        // Declaration of views in view holder
        ImageView mItemIV;
        TextView mNameTV;
        ImageView mShopIV;

        FavoriteViewHolder(View favoriteView){
            super(favoriteView);
            mItemIV = favoriteView.findViewById(R.id.iv_fav_item);
            mNameTV = favoriteView.findViewById(R.id.tv_fav_name);
            mShopIV = favoriteView.findViewById(R.id.iv_fav_shop);

            // TODO: set click listener on FavoriteViewHolder
        }
    }
}
