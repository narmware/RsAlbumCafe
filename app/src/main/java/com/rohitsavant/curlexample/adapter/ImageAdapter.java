package com.rohitsavant.curlexample.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rohitsavant.curlexample.R;
import com.rohitsavant.curlexample.curl.CurlActivity;


/**
 * Created by Lincoln on 31/03/16.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    Bitmap[] bit;
    static Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mImgFrame;


        public MyViewHolder(View view) {
            super(view);

            mImgFrame=view.findViewById(R.id.singlephoto);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position= (int) mImgFrame.getTag();

                   // Toast.makeText(mContext,position+"" , Toast.LENGTH_SHORT).show();
                    CurlActivity.mCurlView.setCurrentIndex(position);
                    CurlActivity.dialog.dismiss();
                    }
            });


        }
    }
    public ImageAdapter(Context context,  Bitmap[] bit) {
        this.mContext = context;
        this.bit = bit;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Bitmap photo = bit[position];


        holder.mImgFrame.setImageBitmap(photo);
        holder.mImgFrame.setTag(position);

    }

    @Override
    public int getItemCount() {

        return bit.length;
    }

}