package com.rohitsavant.curlexample.curl;

/**
 * Created by savvy on 4/19/2018.
 */

/*
   Copyright 2011 Harri Smått

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.rohitsavant.curlexample.MyApplication;
import com.rohitsavant.curlexample.R;
import com.rohitsavant.curlexample.activity.AlbumDetailedActivity;
import com.rohitsavant.curlexample.adapter.ImageAdapter;
import com.rohitsavant.curlexample.helper.Constants;
import com.rohitsavant.curlexample.helper.DatabaseAccess;
import com.rohitsavant.curlexample.helper.SupportFunctions;
import com.rohitsavant.curlexample.helper.ZoomLayout;
import com.rohitsavant.curlexample.pojo.AlbumImages;
import com.rohitsavant.curlexample.pojo.AlbumImagesResponse;
import com.rohitsavant.curlexample.pojo.BitmapImages;
import com.rohitsavant.curlexample.pojo.PhotographerDetails;
import com.rohitsavant.curlexample.pojo.PhotographerResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Simple Activity for curl testing.
 *
 * @author harism
 */
public class CurlActivity extends Activity {

    public static CurlView mCurlView;
    Bitmap image;
    ArrayList<Bitmap> imgUrls=new ArrayList<>();
    Bitmap[] bit;
    String[] mBitmapIds;
    SweetAlertDialog sweetAlertDialog;
    LinearLayout mLinearZoom;
    ImageView mImgZoomIn,mImgZoomOut;
    LinearLayout mLinearBack;
    ImageView mImgLeft,mImgRight,mImgGrid;
    ZoomLayout zoomLayout;
    LinearLayout relativeLayout;
    static Bitmap bitmap;
    public static DialogPlus dialog;
    RecyclerView mRecyclerView;
    String mAid;
    RequestQueue mVolleyRequest;
    ArrayList<AlbumImages> albumImages=new ArrayList<>();
    MediaPlayer mp;
    DatabaseAccess databaseAccess;
    ProgressDialog progress;

    int mProgress=0;
    int mpFlag=0;
    int mpLength=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mVolleyRequest = Volley.newRequestQueue(CurlActivity.this);

        databaseAccess = DatabaseAccess.getInstance(CurlActivity.this);
        databaseAccess.open();

        Intent intent=getIntent();
        mAid=intent.getStringExtra(Constants.ALBUM_SERVER_ID);

        sweetAlertDialog=new SweetAlertDialog(CurlActivity.this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading...");
        sweetAlertDialog.setCancelable(false);
        //sweetAlertDialog.show();


       /* try {
            MediaPlayer mp =new MediaPlayer();
            mp.setDataSource(CurlActivity.this,Uri.parse("https://gaana.com/song/tere-sang-yaara-1"));
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepare();
            mp.start();

        } catch (IOException e) {
            e.printStackTrace();
        }*/
        int index = 0;
        if (getLastNonConfigurationInstance() != null) {
            index = (Integer) getLastNonConfigurationInstance();
        }

        mCurlView = (CurlView) findViewById(R.id.curl);
        mCurlView.setCurrentIndex(index);
        mCurlView.setSizeChangedObserver(new SizeChangedObserver());
        mCurlView.setBackgroundColor(getResources().getColor(R.color.photoBackground));

        // This is something somewhat experimental. Before uncommenting next
        // line, please see method comments in CurlView.
        mCurlView.setEnableTouchPressure(true);

        // CAGS: This is to allow 2 pages landscape mode, set to false for legacy mode
        mCurlView.set2PagesLandscape(true);

        ArrayList<BitmapImages> bitmapImages=databaseAccess.getImage(mAid);
        Log.e("Database size",bitmapImages.size()+"");

        if(bitmapImages.size()!=0)
        {
            bit=new Bitmap[bitmapImages.size()];
            for(int b=0;b<bitmapImages.size();b++)
            {
                bit[b]=bitmapImages.get(b).getImages();
                if(b==bitmapImages.size()-1) {
                    mCurlView.setBitmapProvider(new MyProvider());
                    mp = MediaPlayer.create(CurlActivity.this, R.raw.songs);
                    mp.start();

                    if(mpFlag==1)
                    {
                        mp.seekTo(mpLength);
                        mp.start();
                    }
                    /*try{
                        sweetAlertDialog.dismissWithAnimation();}catch (Exception e){}*/

                }
                Log.e("Database images",bitmapImages.get(b).getImages().toString());
            }
        }

        if(bitmapImages.size()==0) {
            GetAlbumImages();
        }
       Log.e("Album size",albumImages.size()+"");

        mImgLeft=findViewById(R.id.img_left);
        mImgRight=findViewById(R.id.img_right);
        zoomLayout=findViewById(R.id.zoomlay);
        relativeLayout=findViewById(R.id.relative);

        mImgZoomIn=findViewById(R.id.btn_zoom_in);
        mImgZoomOut=findViewById(R.id.btn_zoom_out);
        mImgGrid=findViewById(R.id.btn_grid);
        mLinearZoom=findViewById(R.id.linear_zoom);

        mLinearZoom.setVisibility(View.VISIBLE);
        mImgZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int currentIndex=mCurlView.getCurrentIndex();
                int single=0;
                Log.e("Bitmap image",bit[0]+"");
                zoomLayout.setVisibility(View.VISIBLE);
                mImgGrid.setVisibility(View.GONE);
                mImgZoomIn.setVisibility(View.GONE);
                mImgZoomOut.setVisibility(View.VISIBLE);

                if(currentIndex==bit.length)
                {
                    //Toast.makeText(CurlActivity.this, currentIndex+"  "+bit.length,Toast.LENGTH_SHORT).show();
                    if(bit.length % 2==0) {
                        single = currentIndex-1;
                        mImgLeft.setVisibility(View.GONE);
                        mImgRight.setLayoutParams(new LinearLayout.LayoutParams(1000, 600));
                        mImgRight.setImageBitmap(bit[single]);
                    }

                }
                if(currentIndex==0)
                {
                    single=currentIndex;
                    mImgLeft.setVisibility(View.GONE);
                    mImgRight.setLayoutParams(new LinearLayout.LayoutParams(1000, 600));
                    mImgRight.setImageBitmap(bit[single]);
                }
                 if(currentIndex!=0 && currentIndex!=bit.length){
                    int right = currentIndex;
                    int left = currentIndex - 1;

                    if(mImgLeft.getVisibility()==View.GONE)
                    {
                        mImgLeft.setVisibility(View.VISIBLE);
                    }
                    mImgLeft.setImageBitmap(bit[left]);
                    mImgRight.setImageBitmap(bit[right]);
                }

            }
        });

        mImgZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomLayout.setVisibility(View.INVISIBLE);
                mImgGrid.setVisibility(View.VISIBLE);
                mImgZoomIn.setVisibility(View.VISIBLE);
                mImgZoomOut.setVisibility(View.GONE);
            }
        });

        mImgGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = DialogPlus.newDialog(CurlActivity.this)
                        .setExpanded(true,RelativeLayout.LayoutParams.WRAP_CONTENT)
                        .setContentHolder(new ViewHolder(R.layout.lay_image))
                        .setGravity(Gravity.TOP)
                        .setBackgroundColorResId(R.color.dialogplus_black_overlay)
                        .create();
                setAlbumAdapter(new GridLayoutManager(CurlActivity.this,5));

                dialog.show();
            }
        });

        mLinearBack=findViewById(R.id.linear_back);
        mLinearBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                if(mp.isPlaying()==true)
                {
                    mp.stop();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mp.isPlaying()==true)
        {
            mp.stop();
        }
    }

    public void setAlbumAdapter(RecyclerView.LayoutManager mLayoutManager){

        View view=dialog.getHolderView();
        mRecyclerView=view.findViewById(R.id.recycler);

        ImageAdapter imageAdapter=new ImageAdapter(CurlActivity.this,bit);
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(GalleryActivity.this,2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(imageAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setFocusable(false);

        imageAdapter.notifyDataSetChanged();

    }

    public Bitmap createBitmapFromView(Context context, View view) {

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
            view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
            view.buildDrawingCache();
            Bitmap b = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(b);
             view.draw(canvas);
            b.eraseColor(getResources().getColor(R.color.amber_100));

        return b;
    }
    @Override
    public void onPause() {
        super.onPause();
        if(mp.isPlaying()==true)
        {
            mp.pause();
            mpLength=mp.getCurrentPosition();
            mpFlag=1;
        }
       // mCurlView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mCurlView.onResume();

        if(mpFlag==1)
        {
            mp.seekTo(mpLength);
           // mp.start();
        }
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
       // Toast.makeText(CurlActivity.this,mCurlView.getCurrentIndex()+"",Toast.LENGTH_SHORT).show();
        return mCurlView.getCurrentIndex();
    }

    /**
     * Bitmap provider for int values
     */
/*
    private class BitmapProvider implements CurlView.BitmapProvider {

        private int[] mBitmapIds = { R.drawable.obama, R.drawable.road_rage,
                R.drawable.taipei_101, R.drawable.world };

        @Override
        public Bitmap getBitmap(int width, int height, int index) {
            Bitmap b = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            b.eraseColor(0xFFFFFFFF);
            Canvas c = new Canvas(b);
            Drawable d = getResources().getDrawable(mBitmapIds[index]);

            int margin = 7;
            int border = 3;
            Rect r = new Rect(margin, margin, width - margin, height - margin);

            int imageWidth = r.width() - (border * 2);
            int imageHeight = imageWidth * d.getIntrinsicHeight()
                    / d.getIntrinsicWidth();
            if (imageHeight > r.height() - (border * 2)) {
                imageHeight = r.height() - (border * 2);
                imageWidth = imageHeight * d.getIntrinsicWidth()
                        / d.getIntrinsicHeight();
            }

            r.left += ((r.width() - imageWidth) / 2) - border;
            r.right = r.left + imageWidth + border + border;
            r.top += ((r.height() - imageHeight) / 2) - border;
            r.bottom = r.top + imageHeight + border + border;

            Paint p = new Paint();
            p.setColor(0xFFC0C0C0);
            c.drawRect(r, p);
            r.left += border;
            r.right -= border;
            r.top += border;
            r.bottom -= border;

            d.setBounds(r);
            d.draw(c);
            return b;
        }

        @Override
        public int getBitmapCount() {
            return mBitmapIds.length;
        }
    }
*/

    /**
     * Bitmap provider for string values
     */
    private class MyProvider implements CurlView.BitmapProvider {



public MyProvider(){
    //Log.e("Const Data",mBitmapIds.length+"");
}
        @Override
        public Bitmap getBitmap(int width, int height, int index) {
            Bitmap b = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            b.eraseColor(getResources().getColor(R.color.photoBackground));
            Canvas c = new Canvas(b);
            //Drawable d = getResources().getDrawable(mBitmapIds[index]);

            Log.e("Data",imgUrls.size()+"");
            Log.e("Resolution","w: "+width+" h: "+height);

            Drawable d=new BitmapDrawable(getResources(),bit[index]);
            int margin = 0;
            int border = 0;
            Rect r = new Rect(margin, margin, width-margin, height-margin);

            int imageWidth = r.width() - (border * 2);
            int imageHeight = imageWidth * d.getIntrinsicHeight()
                    / d.getIntrinsicWidth();
            if (imageHeight > r.height() - (border * 2)) {
                imageHeight = r.height() - (border * 2);
                imageWidth = imageHeight * d.getIntrinsicWidth()
                        / d.getIntrinsicHeight();
            }

           /* r.left += ((r.width() - imageWidth) / 2) - border;
            r.right = r.left + imageWidth + border + border;
            r.top += ((r.height() - imageHeight) / 2) - border;
            r.bottom = r.top + imageHeight + border + border;
*/

            r.left +=4;
            r.right += -4;
            r.top += 4;
            r.bottom += -4;

            Paint p = new Paint();
            p.setColor(getResources().getColor(R.color.photoBackground));
            c.drawRect(r, p);
            r.left += border;
            r.right += border;
            r.top += border;
            r.bottom += border;

            d.setBounds(r);
            d.draw(c);
            return b;
        }

        @Override
        public int getBitmapCount() {
            return bit.length;
        }

    }

    /**
     * CurlView size changed observer.
     */
    private class SizeChangedObserver implements CurlView.SizeChangedObserver {
        @Override
        public void onSizeChanged(int w, int h) {
            if (w > h) {
                mCurlView.setViewMode(CurlView.SHOW_TWO_PAGES);
               // mCurlView.setMargins(.1f, .05f, .1f, .05f);
                mCurlView.setMargins(0,.17f,0,.17f);
            } else {
                mCurlView.setViewMode(CurlView.SHOW_ONE_PAGE);
               // mCurlView.setMargins(.1f, .1f, .1f, .1f);
                mCurlView.setMargins(0,0,0,0);

            }
        }
    }

    class drawableFromUrl extends AsyncTask<String,String,Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... strings) {
            String s=strings[0];
            int pos= Integer.parseInt(strings[1]);

            HttpURLConnection connection=null;

            try {
                connection= (HttpURLConnection) new URL(s).openConnection();
                connection.connect();
                InputStream input=connection.getInputStream();
                //image= BitmapFactory.decodeStream(input);

               /* BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                image= BitmapFactory.decodeStream(input,null,options);*/

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize=8;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inDither = true;
                image= BitmapFactory.decodeStream(input,null,options);
                Log.e("Background images",image.toString());
                mProgress++;
                progress.setProgress(mProgress);
                imgUrls.add(image);

                bit[pos]=image;

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 0, stream);
                byte[] bytes= stream.toByteArray();
                databaseAccess.setAlbumImages(mAid,bytes);

                if(pos==mBitmapIds.length-1)
                {
                    progress.dismiss();
                    mCurlView.setBitmapProvider(new MyProvider());
                    mp = MediaPlayer.create(CurlActivity.this, R.raw.songs);
                    mp.start();
                  /*  try{
                    sweetAlertDialog.dismissWithAnimation();}catch (Exception e){}*/
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }
    }


    private void GetAlbumImages() {
        final ProgressDialog dialog = new ProgressDialog(CurlActivity.this);
        dialog.setMessage("getting album ...");
        dialog.setCancelable(false);
        dialog.show();

        HashMap<String,String> param = new HashMap();
        param.put(Constants.ALBUM_ID,mAid);

        String url= SupportFunctions.appendParam(MyApplication.URL_GET_ALBUM_IMAGES,param);
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

                            Log.e("Photos Json_string",response.toString());
                            Gson gson = new Gson();

                            AlbumImagesResponse photoResponse= gson.fromJson(response.toString(), AlbumImagesResponse.class);

                            if(photoResponse.getResponse().equals("100")) {
                                AlbumImages[] photo = photoResponse.getData();
                                for (AlbumImages item : photo) {
                                    Log.e("Featured img title", item.getPhoto_path());
                                    AlbumImages singleImage=new AlbumImages(item.getPhoto_path(),item.getType());
                                    albumImages.add(singleImage);
                                }
                                Log.e("Album size",albumImages.size()+"");
                                showProgress(albumImages.size());
                                mBitmapIds=new String[albumImages.size()];
                                for(int a=0;a<albumImages.size();a++)
                                {
                                    mBitmapIds[a]=albumImages.get(a).getPhoto_path();
                                }
                                /*mBitmapIds = new String[]{
                                        "https://onehdwallpaper.com/wp-content/uploads/2015/07/Punjabi-Wedding-Couple-in-Rain-HD-Picture.jpg",
                                        "http://www.coolhdwallpapers.net/gallery/romantic-park-wedding-hd-wallpaper.jpg",
                                        "https://i.ytimg.com/vi/xsjGKpsDjgU/maxresdefault.jpg",
                                        "http://ak5.picdn.net/shutterstock/videos/6261785/thumb/1.jpg",
                                        "http://www.coolhdwallpapers.net/gallery/romantic-park-wedding-hd-wallpaper.jpg",
                                        "http://www.wallcoo.net/photography/SZ_221%20Garden%20Weddings%2001/wallpapers/1920x1200/Garden_Wedding_photography_022_Bride%20and%20bridegroom%20holding%20hands.jpg",
                                };*/

                                bit=new Bitmap[mBitmapIds.length];

                                for(int i=0;i<mBitmapIds.length;i++)
                                {
                                    try {
                                        new drawableFromUrl().execute(mBitmapIds[i],String.valueOf(i));
                                    }catch (Exception e)
                                    {

                                    }
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

    public void showProgress(int end)
    {
        progress=new ProgressDialog(this);
        progress.setMessage("Downloading Album");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progress.setMax(end);
        progress.setCancelable(false);
        progress.show();

        /*final int totalProgressTime = end;
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;

                while(jumpTime < totalProgressTime) {
                    try {
                        sleep(200);

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();*/
    }

}
