package com.huangyinghao.playermp3.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by deny on 2015/12/25.
 */
public class MyItemDecoration extends RecyclerView.ItemDecoration {

    //系统默认的分隔条ID
    private static final int[] ATTRS = {android.R.attr.listDivider};
    private final Drawable drawable;

    public MyItemDecoration(Context context){

        //获得系统的分隔条对象
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        drawable = a.getDrawable(0);
        a.recycle();   //取完资源记得回收
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int count = parent.getChildCount();
        for(int i = 0 ; i < count ; i ++){
            View view = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

            int top = view.getBottom() + params.bottomMargin;
            int bottom = top + drawable.getIntrinsicHeight();

            drawable.setBounds(left , top , right , bottom);
            drawable.draw(c);
        }
    }
}
