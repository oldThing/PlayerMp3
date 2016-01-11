package com.huangyinghao.playermp3.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.huangyinghao.playermp3.R;

/**
 * Created by deny on 2015/12/21.
 */
public class cursorTab extends View {

    private Bitmap mIcon;
    private int mColor;
    private String mText = "音乐";
    private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            20, getResources().getDisplayMetrics());

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;

    private float mAlpah;   //透明度

    private Rect mIconRect;
    private Paint mTextPaint;
    private Rect mTextBound;


    public cursorTab(Context context) {
        this(context, null);
    }

    public cursorTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public cursorTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.cursorTab);

        int num = a.getIndexCount();
        for(int i = 0 ; i < num ; i ++){
            int index = a.getIndex(i);

            switch (index){
                case R.styleable.cursorTab_ic_icon:
                    BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(index);
                    mIcon = drawable.getBitmap();
                    break;

                case R.styleable.cursorTab_text:
                    mText = (String) a.getText(index);
                    break;

                case R.styleable.cursorTab_ic_color:
                    mColor = a.getColor(index, 0xff45c01a);
                    break;

                case R.styleable.cursorTab_text_size:
                    mTextSize = (int) a.getDimension(index,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20,
                                    getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();

        //画出字体
        mTextBound = new Rect();
        mTextPaint = new Paint();

        //设置笔的颜色
        mTextPaint.setColor(0Xff555555);
        mTextPaint.setTextSize(mTextSize);

        mTextPaint.getTextBounds(mText , 0 , mText.length() , mTextBound);

    }

    /**
     * 图片放置的位置
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight() ,
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - mTextBound.height());

        int left = getMeasuredWidth() / 2 - iconWidth / 2;
        int top = getMeasuredHeight() / 2 - iconWidth / 2 - mTextBound.height();

        mIconRect = new Rect(left , top , left + iconWidth , top + iconWidth);

    }

    /**
     * 根据位置画出来
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //把图标显示出来
        canvas.drawBitmap(mIcon , null ,mIconRect , null);

        //颜色的透明度
        int alpha = (int) Math.ceil(255 * mAlpah);

        //画出变颜色的图标
        setupTargetBitmap(alpha);


        // 1、绘制原文本 ； 2、绘制变色的文本
        drawSourceText(canvas, alpha);
        drawTargetText(canvas, alpha);

        canvas.drawBitmap(mBitmap , 0 , 0 , null);
    }

    //变色的文本
    private void drawTargetText(Canvas canvas, int alpha) {

        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);

        //x,y的值代表的是字体的位置
        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        int y = mIconRect.bottom + mTextBound.height();

        canvas.drawText(mText, x, y, mTextPaint);
    }

    //原文本
    private void drawSourceText(Canvas canvas, int alpha) {

        mTextPaint.setColor(0xff333333);
        mTextPaint.setAlpha(255 - alpha);
        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        int y = mIconRect.bottom + mTextBound.height();

        canvas.drawText(mText , x , y , mTextPaint);
    }

    public void setupTargetBitmap(int alpha) {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth() , getMeasuredHeight() ,
                Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();

        mPaint.setColor(mColor);
        mPaint.setAlpha(alpha);
        mPaint.setAntiAlias(true);   //防止边缘的锯齿
        mPaint.setDither(true);

        mCanvas.drawRect(mIconRect, mPaint);

        //两个图层 DstIn
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);

        mCanvas.drawBitmap(mIcon, null, mIconRect, mPaint);

    }


    public void setIconAlpha(float alpha){
        this.mAlpah = alpha;
        invalidateView();
    }

    /**
     * 重绘
     */
    private void invalidateView() {
        if(Looper.getMainLooper() == Looper.myLooper()){
            invalidate();
        } else {
            postInvalidate();
        }
    }


    /**
     * 保存数据
     * @return
     */

    public static final String INSTANCE_STATE = "instance_state";
    public static final String ALPHA_STATE = "alpha_state";
    @Override
    protected Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE , super.onSaveInstanceState() );
        bundle.putFloat(ALPHA_STATE, mAlpah);

        return bundle;
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if(state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            bundle.getFloat(ALPHA_STATE);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
        }
        super.onRestoreInstanceState(state);
    }




}
