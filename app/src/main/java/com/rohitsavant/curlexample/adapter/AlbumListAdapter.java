package com.rohitsavant.curlexample.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohitsavant.curlexample.R;
import com.rohitsavant.curlexample.activity.AlbumDetailedActivity;
import com.rohitsavant.curlexample.activity.NavigationActivity;
import com.rohitsavant.curlexample.helper.Constants;
import com.rohitsavant.curlexample.helper.DatabaseAccess;
import com.rohitsavant.curlexample.helper.SharedPreferencesHelper;
import com.rohitsavant.curlexample.pojo.AlbumList;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by Lincoln on 31/03/16.
 */

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.MyViewHolder> {

     static List<AlbumList> photos;
    static Context mContext;


    /* FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
*/
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mthumb_title;
        ImageView mImgFrame;
        public ImageButton mBtnClose;
        AlbumList mItem;
        CoordinatorLayout mLinearItem;
        String videoId;
        DatabaseAccess databaseAccess;

        public MyViewHolder(View view) {
            super(view);
            databaseAccess = DatabaseAccess.getInstance(mContext);
            databaseAccess.open();

            mBtnClose=view.findViewById(R.id.btn_close);
            mthumb_title= view.findViewById(R.id.thumb_title);
            mImgFrame=view.findViewById(R.id.thumb_img);
            mLinearItem=view.findViewById(R.id.linear_item);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(mContext, AlbumDetailedActivity.class);
                    intent.putExtra(Constants.TITLE,mItem.getTitle());
                    intent.putExtra(Constants.ALBUM_IMAGE,mItem.getUrl());
                    intent.putExtra(Constants.ALBUM_SERVER_ID,mItem.getServer_id());
                    mContext.startActivity(intent);

                   //Toast.makeText(mContext, mItem.getServer_id(), Toast.LENGTH_SHORT).show();

                    int position= (int) mLinearItem.getTag();

                    }
            });

            mBtnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position= (int) mLinearItem.getTag();
                    //Toast.makeText(mContext, mItem.getServer_id(), Toast.LENGTH_SHORT).show();
                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Remove this album")
                            .setConfirmText("Yes")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    photos.remove(position);
                                    databaseAccess.deleteSingleAlbum(mItem.getServer_id().toString());
                                    notifyDataSetChanged();

                                    sDialog
                                            .setTitleText("Deleted!")
                                              .setContentText("Your album has been removed!")
                                            .setConfirmText("OK")
                                            .showCancelButton(false)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    //sDialog.dismiss();
                                }
                            })
                            .showCancelButton(true)
                            .setCancelText("Cancel")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    sDialog.dismissWithAnimation();
                                }
                            })

                            .show();

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

      if(SharedPreferencesHelper.getDeleteFlag(mContext)==false)
      {
          holder.mBtnClose.setVisibility(View.INVISIBLE);
      }
      else {
          holder.mBtnClose.setVisibility(View.VISIBLE);
      }
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
        if(photos.size()==0)
        {
            NavigationActivity.mLinearLeft.setVisibility(View.GONE);
            NavigationActivity.mLinearRight.setLayoutParams(new LinearLayout.LayoutParams(1000, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        else{
            NavigationActivity.mLinearLeft.setVisibility(View.VISIBLE);
            NavigationActivity.mLinearRight.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1.0f));
        }
        return photos.size();
    }

}