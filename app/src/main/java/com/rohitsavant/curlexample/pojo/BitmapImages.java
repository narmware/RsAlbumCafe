package com.rohitsavant.curlexample.pojo;

import android.graphics.Bitmap;

/**
 * Created by rohitsavant on 01/05/18.
 */

public class BitmapImages {
    Bitmap bitmap;

    public BitmapImages(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getImages() {
        return bitmap;
    }

    public void setImages(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
