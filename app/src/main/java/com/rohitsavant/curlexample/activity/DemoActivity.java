package com.rohitsavant.curlexample.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rohitsavant.curlexample.R;
import com.rohitsavant.curlexample.curl.CurlActivity;
import com.rohitsavant.curlexample.helper.ZoomLayout;

public class DemoActivity extends AppCompatActivity {

    LinearLayout relativeLayout;   //Define Any Layout
    Bitmap bitmap;
    ImageView mImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

       mImg=findViewById(R.id.screen_shot);
       relativeLayout=findViewById(R.id.rel_screen);

        bitmap = Bitmap.createBitmap(1000,1000, Bitmap.Config.ARGB_8888);
        Log.e("Bitmap image",bitmap+"");
       /* Canvas c = new Canvas(bitmap);
        //c.drawBitmap(bitmap,null,null);
        mImg.layout(0, 0, 1000, 1000);
        mImg.draw(c);*/

        Bitmap map=createBitmapFromView(DemoActivity.this,relativeLayout);

        mImg.setImageBitmap(map);
    }


    private Bitmap createBitmapFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}
