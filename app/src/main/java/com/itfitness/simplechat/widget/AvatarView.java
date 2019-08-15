package com.itfitness.simplechat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class AvatarView extends View {
    private Paint mPaint;
    private float mTextSize = 50;
    private int mTextColor = Color.BLUE;
    private String mAvText = "";
    public AvatarView(Context context) {
        this(context,null);
    }

    public AvatarView(Context context,AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AvatarView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL);
    }
    public void setAvtarText(String avtarText){
        mAvText = avtarText.substring(0, 1);
        invalidate();//重绘
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTextSize = Math.min(w,h)/3*2;
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int top = mPaint.getFontMetricsInt().top;
        int bottom = mPaint.getFontMetricsInt().bottom;
        int baselineY =getHeight()/2 + (bottom-top)/2 - bottom;
        canvas.drawColor(Color.WHITE);
        canvas.drawText(mAvText,getWidth()/2,baselineY,mPaint);
    }
}
