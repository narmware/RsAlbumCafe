package com.rohitsavant.curlexample.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rohitsavant.curlexample.R;
import com.rohitsavant.curlexample.curl.CurlActivity;

public class DemoActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;   //Define Any Layout
    Bitmap bitmap;
    ImageView mImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        //provide layout with its id in Xml

        /*relativeLayout=findViewById(R.id.container);

        //initialise layout_to_image object with its parent class and pass parameters as (<Current Activity>,<layout object>)

        layout_to_image=new Layout_to_Image(DemoActivity.this,relativeLayout);

        //now call the main working function ;) and hold the returned image in bitmap
        bitmap=layout_to_image.convert_layout();*/


       /* mImg=findViewById(R.id.screen_top);
        bitmap = Bitmap.createBitmap(80, 100, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        mImg.layout(0, 0, 80, 100);
        mImg.draw(c);

        ImageView iv =findViewById(R.id.screen);
        iv.setImageBitmap(bitmap);*/
        bitmap= CurlActivity.getBitmap();
        ImageView iv =findViewById(R.id.screen_top);
        Canvas c = new Canvas(bitmap);
        iv.layout(0, 0, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.draw(c);

       Log.e("Bitmap image",bitmap+"");
       /* FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(layoutParams);
        iv.setImageBitmap(bitmap);*/
    }


}
