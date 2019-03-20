package com.example.scrollersample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.OverScroller;

public class View03 extends View {
    private static final String TAG = View03.class.getSimpleName();
    private Paint mPaint;
    private OverScroller scroller;

    public View03(Context context) {
        this(context,null);
    }

    public View03(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public View03(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public View03(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);

        scroller = new OverScroller(context);
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
//        int startX = getScrollX();
//        int startY = getScrollY();
//        PropertyValuesHolder xholder = PropertyValuesHolder.ofInt("scrollX", startX, startX + i);
//        PropertyValuesHolder yholder = PropertyValuesHolder.ofInt("scrollY", startY, startY + i1);
//        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this,xholder,yholder);
//        animator.setDuration(1000);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                invalidate();
//            }
//        });
//        animator.start();

        // 开始之前强制结束动画
        scroller.forceFinished(true);
        /**
         * 参数分别是X坐标，Y坐标，X移动的距离，Y移动的距离
         * */
        scroller.startScroll(getScrollX(),
                getScrollY(),
                i,
                i1);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            Log.d(TAG, "computeScroll: ");

            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            postInvalidate();
        } else {
            Log.d(TAG, "computeScroll is over: ");
        }
    }
}
