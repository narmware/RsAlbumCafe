package com.rohitsavant.curlexample.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohitsavant.curlexample.R;
import com.rohitsavant.curlexample.activity.AlbumDetailedActivity;
import com.rohitsavant.curlexample.curl.CurlActivity;
import com.rohitsavant.curlexample.helper.Constants;
import com.rohitsavant.curlexample.pojo.AlbumList;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Lincoln on 31/03/16.
 */

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.MyViewHolder> {

     List<AlbumList> photos;
    Context mContext;

    /* FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
*/
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mthumb_title;
       ImageView mImgFrame;
        AlbumList mItem;
        LinearLayout mLinearItem;
        String videoId;

        public MyViewHolder(View view) {
            super(view);
            mthumb_title= view.findViewById(R.id.thumb_title);
            mImgFrame=view.findViewById(R.id.thumb_img);
            mLinearItem=view.findViewById(R.id.linear_item);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(mContext, AlbumDetailedActivity.class);
                    intent.putExtra(Constants.TITLE,mItem.getTitle());
                    intent.putExtra(Constants.ALBUM_IMAGE,mItem.getUrl());
                    mContext.startActivity(intent);

                   //Toast.makeText(mContext, mItem.getTitle(), Toast.LENGTH_SHORT).show();

                    int position= (int) mLinearItem.getTag();


                    }
            });

        }
    }

    public AlbumListAdapter(Context context, List<AlbumList> photos) {
        this.mContext = context;
        this.photos = photos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_albums, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AlbumList photo = photos.get(position);

      /*  String videoId = VideoPojo2.getVideoId(photo.getUrl());
        holder.videoId=videoId;

        if(SharedPreferencesHelper.getTopType(mContext).equals(Constants.IMAGE_TYPE)) {
            Picasso.with(mContext)
                    .load(photo.getUrl())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.mImgFrame);
        }
        if(SharedPreferencesHelper.getTopType(mContext).equals(Constants.VIDEO_TYPE)) {
            Picasso.with(mContext)
                    .load("https://img.youtube.com/vi/" + videoId + "/0.jpg")
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.mImgFrame);

        }*/
        holder.mLinearItem.setTag(position);
        Picasso.with(mContext)
                .load(photo.getUrl())
                .fit()
                .noFade()
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.mImgFrame);
        holder.mthumb_title.setText(photo.getTitle());
        holder.mItem=photo;
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

}