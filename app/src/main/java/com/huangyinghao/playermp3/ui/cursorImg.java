package com.huangyinghao.playermp3.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by deny on 2015/12/30.
 */
public class cursorImg extends Drawable{

    //图片资源
    Bitmap mBitmap ;
    //边距
    int mMargin;
    private BitmapShader bitmapShader;
    private Paint paint;
    private Paint mPaint;
    private float radious;

    public cursorImg(Bitmap bitmap){
        this(bitmap , 0);
    }

    public cursorImg(Bitmap bitmap , int margin){
        mBitmap = bitmap;
        mMargin = margin;
        //声明bitmapShader
        bitmapShader = new BitmapShader(mBitmap , Shader.TileMode.CLAMP , Shader.TileMode.CLAMP);
        //定义画笔
        mPaint = new Paint();
        //防锯齿
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        computeBitmapShaderSize();

        computeRadious();
    }

    /**
     * 计算出圆的半径
     */
    private void computeRadious() {
        Rect bounds = getBounds();
        radious = bounds.width() < bounds.height() ?
                bounds.width() / 2f - mMargin : bounds.height() / 2f - mMargin;
    }

    /**
     * 计算bItmap的大小
     */
    private void computeBitmapShaderSize() {
        Rect bounds = getBounds();
        if(bounds == null) return;
        //选择缩放比较多的缩放，这样图片就不会有图片拉伸失衡
        Matrix matrix = new Matrix();

        float scaleX = bounds.width() /(float) mBitmap.getWidth();
        float scaleY = bounds.height() /(float) mBitmap.getHeight();
        float scale = scaleX < scaleY ? scaleX : scaleY;
//        float scale = Math.min(scaleX , scaleY);
        matrix.setScale(scale , scale);
        bitmapShader.setLocalMatrix(matrix);
    }

    /**
     * 画圆
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.drawCircle(bounds.width() / 2f , bounds.height() / 2f , radious , mPaint );
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
