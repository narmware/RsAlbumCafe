package com.rohitsavant.curlexample.helper;

import android.view.ScaleGestureDetector;

 public class OnPinchListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    float startingSpan; 
    float endSpan;
    float startFocusX;
    float startFocusY;
     ZoomableRelativeLayout mZoomableRelativeLayout;

     public OnPinchListener(ZoomableRelativeLayout mZoomableRelativeLayout) {
         this.mZoomableRelativeLayout = mZoomableRelativeLayout;
     }

     public boolean onScaleBegin(ScaleGestureDetector detector) {
        startingSpan = detector.getCurrentSpan();
        startFocusX = detector.getFocusX();
        startFocusY = detector.getFocusY();
        return true;
    }


    public boolean onScale(ScaleGestureDetector detector) {
        mZoomableRelativeLayout.scale(detector.getCurrentSpan()/startingSpan, startFocusX, startFocusY);
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector detector) {
        //mZoomableRelativeLayout.restore();
    }
}