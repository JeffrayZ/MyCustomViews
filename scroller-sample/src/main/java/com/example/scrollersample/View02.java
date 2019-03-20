package com.example.scrollersample;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class View02 extends View {
    private static final String TAG = View02.class.getSimpleName();
    private Paint mPaint;

    public View02(Context context) {
        this(context,null);
    }

    public View02(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public View02(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public View02(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        canvas.drawCircle(0,0,40.0f,mPaint);
    }


    /**
     * 动画平滑滚动
     * */
    public void startScroll(int i, int i1) {
        int startX = getScrollX();
        int startY = getScrollY();
        PropertyValuesHolder xholder = PropertyValuesHolder.ofInt("scrollX", startX, startX + i);
        PropertyValuesHolder yholder = PropertyValuesHolder.ofInt("scrollY", startY, startY + i1);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this,xholder,yholder);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        animator.start();
    }
}
