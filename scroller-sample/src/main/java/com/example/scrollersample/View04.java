package com.example.scrollersample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

public class View04 extends View {
    private static final String TAG = View04.class.getSimpleName();
    private Paint mPaint;
    private OverScroller scroller;
    private int mSlop;
    private VelocityTracker mVelocityTracker;
    private static final int MIN_FING_VELOCITY = 1000;

    public View04(Context context) {
        this(context, null);
    }

    public View04(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public View04(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public View04(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);

        scroller = new OverScroller(context);
        // 获取最小能够识别的滑动距离
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mSlop = viewConfiguration.getScaledPagingTouchSlop();

        setClickable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        canvas.drawCircle(82, 80, 80.0f, mPaint);
    }


    /**
     * 动画平滑滚动
     */
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

            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        } else {
            Log.d(TAG, "computeScroll is over: ");
        }
    }

    private int mLastPointX;
    private int mLastPointY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (mVelocityTracker == null ) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastPointX = (int) event.getX();
                mLastPointY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastPointX;
                int deltaY = y - mLastPointY;
//                if(Math.abs(deltaX) > mSlop || Math.abs(deltaY) > mSlop){
                scrollBy(-deltaX, -deltaY);
//                }
                break;
            case MotionEvent.ACTION_UP:
                // 第一个参数表明时间单位，1000 代表 1000 ms 也就是 1 s，计算 1 s 内滚动多少个像素,后面表示最大速度
                mVelocityTracker.computeCurrentVelocity(1000,2000.0f);
                // 获取速度
                int xVelocity = (int) mVelocityTracker.getXVelocity();
                int yVelocity = (int) mVelocityTracker.getYVelocity();

                if ( Math.abs(xVelocity) > MIN_FING_VELOCITY
                        || Math.abs(yVelocity) > MIN_FING_VELOCITY ) {
                    // 限定只能在一个方向滑动
//                    xVelocity = Math.abs(xVelocity) > Math.abs(yVelocity) ? -xVelocity : 0;
//                    yVelocity = xVelocity == 0 ? -yVelocity : 0;

                    scroller.fling(getScrollX(),getScrollY(),
                            -xVelocity,-yVelocity,-1000,1000,-1000,2000);
                    invalidate();
                }
                break;
            default:
                break;
        }
        mLastPointX = x;
        mLastPointY = y;
        return true;
    }
}
