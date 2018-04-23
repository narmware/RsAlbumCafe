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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.rohitsavant.curlexample.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Simple Activity for curl testing.
 *
 * @author harism
 */
public class CurlActivity extends Activity {

    private CurlView mCurlView;
    Bitmap image;
    ArrayList<Bitmap> imgUrls=new ArrayList<>();
    Bitmap[] bit;
    String[] mBitmapIds;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


       /* try {
            MediaPlayer mp =new MediaPlayer();
            mp.setDataSource(CurlActivity.this,Uri.parse("https://gaana.com/song/tere-sang-yaara-1"));
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepare();
            mp.start();

        } catch (IOException e) {
            e.printStackTrace();
        }*/
         mBitmapIds = new String[]{"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNRFtYWvXfhButlyLbmRu_9XCea0P1aDxOesxykmkDbMhDeiZx",
                 "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_xW6BPnyIKNoOrB15uTjjtE34BOjNJOdIOA7VsYWne37BuEmQCw",
                 "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRDy_ZvkpUn8qyJuYXCai88AYF4P5-glPquXyjhwowHffqKtVhkiQ",
                 "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUAlc0pdzthsqzLbG9mhDKXEYkutKPehMZlAgYcsSScFkAqj4-",
                 "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNRFtYWvXfhButlyLbmRu_9XCea0P1aDxOesxykmkDbMhDeiZx",
                 "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_xW6BPnyIKNoOrB15uTjjtE34BOjNJOdIOA7VsYWne37BuEmQCw",};

        bit=new Bitmap[mBitmapIds.length];

        for(int i=0;i<mBitmapIds.length;i++)
         {
             try {
                 new drawableFromUrl().execute(mBitmapIds[i],String.valueOf(i));
             }catch (Exception e)
             {

             }
         }

        int index = 0;
        if (getLastNonConfigurationInstance() != null) {
            index = (Integer) getLastNonConfigurationInstance();
        }
        mCurlView = (CurlView) findViewById(R.id.curl);
        mCurlView.setCurrentIndex(index);
        mCurlView.setSizeChangedObserver(new SizeChangedObserver());
        mCurlView.setBackgroundColor(0x191919);

        // This is something somewhat experimental. Before uncommenting next
        // line, please see method comments in CurlView.
        mCurlView.setEnableTouchPressure(true);

        // CAGS: This is to allow 2 pages landscape mode, set to false for legacy mode
        mCurlView.set2PagesLandscape(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCurlView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurlView.onResume();
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return mCurlView.getCurrentIndex();
    }

    /**
     * Bitmap provider.
     */
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

    private class MyProvider implements CurlView.BitmapProvider {

     /*   private int[] mBitmapIds = { R.drawable.obama, R.drawable.road_rage,
                R.drawable.taipei_101, R.drawable.world };*/

public MyProvider(){
    Log.e("Const Data",mBitmapIds.length+"");
}
        @Override
        public Bitmap getBitmap(int width, int height, int index) {
            Bitmap b = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            b.eraseColor(getResources().getColor(R.color.photoBackground));
            Canvas c = new Canvas(b);
            //Drawable d = getResources().getDrawable(mBitmapIds[index]);

            Log.e("Data",imgUrls.size()+"");
            Drawable d=new BitmapDrawable(getResources(),bit[index]);
            int margin = 5;
            int border = 10;
            Rect r = new Rect(margin, margin, 1000, 1000);

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
                mCurlView.setMargins(0,0,0,0);
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
                image= BitmapFactory.decodeStream(input);

                imgUrls.add(image);
                bit[pos]=image;

                if(pos==mBitmapIds.length-1)
                {
                    mCurlView.setBitmapProvider(new MyProvider());
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }
    }
}