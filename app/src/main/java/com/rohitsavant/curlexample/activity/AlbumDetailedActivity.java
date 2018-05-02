package com.rohitsavant.curlexample.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.rohitsavant.curlexample.MyApplication;
import com.rohitsavant.curlexample.R;
import com.rohitsavant.curlexample.curl.CurlActivity;
import com.rohitsavant.curlexample.helper.Constants;
import com.rohitsavant.curlexample.helper.DatabaseAccess;
import com.rohitsavant.curlexample.helper.SupportFunctions;
import com.rohitsavant.curlexample.pojo.PhotographerDetails;
import com.rohitsavant.curlexample.pojo.PhotographerResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumDetailedActivity extends AppCompatActivity {

    @BindView(R.id.linear_back) LinearLayout mBtnBack;
    @BindView(R.id.title) TextView mTxtTitle;
    @BindView(R.id.album_image) ImageView mImgAlbum;
    @BindView(R.id.img_album_logo) ImageView mImgLogo;
    @BindView(R.id.txt_name) TextView mTxtName;
    @BindView(R.id.txt_addr) TextView mTxtAddr;
    @BindView(R.id.txt_mobile) TextView mTxtMobile;

    String mTitle;
    String mAId;
    String mAlbumImageUrl;
    RequestQueue mVolleyRequest;
    DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detailed);
        getSupportActionBar().hide();

        Intent intent=getIntent();
        mTitle=intent.getStringExtra(Constants.TITLE);
        mAlbumImageUrl=intent.getStringExtra(Constants.ALBUM_IMAGE);
        mAId=intent.getStringExtra(Constants.ALBUM_ID);
        init();
    }

    private void init() {

        databaseAccess = DatabaseAccess.getInstance(AlbumDetailedActivity.this);
        databaseAccess.open();

        ButterKnife.bind(this);
        mVolleyRequest = Volley.newRequestQueue(AlbumDetailedActivity.this);

        PhotographerDetails photographerDetails=databaseAccess.getPhotographer(mAId);

        if(photographerDetails!=null) {
            mTxtName.setText(photographerDetails.getName());
            mTxtAddr.setText(photographerDetails.getAddress());
            mTxtMobile.setText(photographerDetails.getMobile());
            Picasso.with(AlbumDetailedActivity.this)
                    .load(photographerDetails.getLogo())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(mImgLogo);
        }else {
            GetPhotographer();
        }
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
                intent.putExtra(Constants.ALBUM_SERVER_ID,mAId);
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

    private void GetPhotographer() {
        final ProgressDialog dialog = new ProgressDialog(AlbumDetailedActivity.this);
        dialog.setMessage("getting details ...");
        dialog.setCancelable(false);
        dialog.show();

        HashMap<String,String> param = new HashMap();
        param.put(Constants.ALBUM_ID,mAId);

        String url= SupportFunctions.appendParam(MyApplication.URL_GET_PHOTOGRAPHER,param);
        Log.e("url",url);
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET,url,null,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            //getting test master array
                            // testMasterDetails = testMasterArray.toString();

                            Log.e("Photog Json_string",response.toString());
                            Gson gson = new Gson();

                            PhotographerResponse photoResponse= gson.fromJson(response.toString(), PhotographerResponse.class);

                            if(photoResponse.getResponse().equals("100")) {
                                PhotographerDetails[] photo = photoResponse.getData();
                                for (PhotographerDetails item : photo) {
                                    Log.e("Featured img title", item.getName());

                                    databaseAccess.setPhotographer(item.getName(),item.getAddress(),item.getEmail(),item.getMobile(),item.getLogo(),mAId);
                                    mTxtName.setText(item.getName());
                                    mTxtAddr.setText(item.getAddress());
                                    mTxtMobile.setText(item.getMobile());
                                    Picasso.with(AlbumDetailedActivity.this)
                                            .load(item.getLogo())
                                            .fit()
                                            .noFade()
                                            .centerCrop()
                                            .placeholder(R.drawable.ic_launcher_background)
                                            .into(mImgLogo);
                                }
                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                            dialog.dismiss();
                        }
                        dialog.dismiss();
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Test Error");
                        // showNoConnectionDialog();
                        dialog.dismiss();

                    }
                }
        );
        mVolleyRequest.add(obreq);
    }

}
