package com.rohitsavant.curlexample.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.rohitsavant.curlexample.R;

/**
 * Created by savvy on 4/28/2018.
 */

public class PhotoAdapter extends BaseAdapter {

    Bitmap[] bit;
    Context mContext;

    public PhotoAdapter(Bitmap[] bit, Context mContext) {
        this.bit = bit;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return bit.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_photo_adapter, viewGroup, false);
        ImageView imageView=itemView.findViewById(R.id.singlephoto);
        imageView.setImageBitmap(bit[i]);
        return itemView;
    }
}
