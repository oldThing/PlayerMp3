package com.huangyinghao.playermp3.ui;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

/**
 * Created by deny on 2016/1/1.
 */
public class CircleBitmapDisplayer implements BitmapDisplayer {
    private int mMargin;

    public CircleBitmapDisplayer(){
        this(0);
    }

    public CircleBitmapDisplayer(int margin){
        mMargin = margin;
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        imageAware.setImageDrawable(new cursorImg(bitmap , mMargin));
    }
}
