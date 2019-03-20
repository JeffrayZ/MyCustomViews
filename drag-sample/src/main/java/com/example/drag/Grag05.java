package com.example.drag;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * 配合 Scroller 使用
 */
public class Grag05 extends View {
    // 上次位置的X点
    private int mLastX;
    // 上次位置的Y点
    private int mLastY;
    private Scroller mScroller;

    public Grag05(Context context) {
        super(context);
        init();
    }

    public Grag05(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Grag05(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Grag05(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - mLastX;
                int offsetY = y - mLastY;

                // 使用者两个方法进行移动的时候，注意此时的坐标方向与平常是相反的

                ((View) getParent()).scrollBy(-offsetX, -offsetY);
                break;
            case MotionEvent.ACTION_UP:
                View viewGroup = (View) getParent();
                mScroller.startScroll(
                        viewGroup.getScrollX(),
                        viewGroup.getScrollY(),
                        -viewGroup.getScrollX(),
                        -viewGroup.getScrollY()
                );

                invalidate();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            ((View)getParent()).scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            //记住，需要不断调用invalidate进行重绘
            invalidate();
        }
    }

    public void init() {
        setBackgroundColor(Color.BLACK);
        mScroller = new Scroller(getContext());
    }
}
