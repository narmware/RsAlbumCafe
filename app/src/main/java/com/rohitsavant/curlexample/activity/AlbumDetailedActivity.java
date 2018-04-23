package com.rohitsavant.curlexample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsavant.curlexample.R;
import com.rohitsavant.curlexample.curl.CurlActivity;
import com.rohitsavant.curlexample.helper.Constants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumDetailedActivity extends AppCompatActivity {

    @BindView(R.id.btn_back) ImageButton mBtnBack;
    @BindView(R.id.title) TextView mTxtTitle;
    @BindView(R.id.album_image) ImageView mImgAlbum;

    String mTitle;
    String mAlbumImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detailed);
        getSupportActionBar().hide();

        Intent intent=getIntent();
        mTitle=intent.getStringExtra(Constants.TITLE);
        mAlbumImageUrl=intent.getStringExtra(Constants.ALBUM_IMAGE);
        init();
    }

    private void init() {

        ButterKnife.bind(this);

        if(mTitle!=null)
        {
            mTxtTitle.setText(mTitle);
        }

        Picasso.with(AlbumDetailedActivity.this)
                .load(mAlbumImageUrl)
                .fit()
                .noFade()
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(mImgAlbum);

        mImgAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AlbumDetailedActivity.this, CurlActivity.class);
                startActivity(intent);
            }
        });

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
