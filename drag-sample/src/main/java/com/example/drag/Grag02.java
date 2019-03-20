package com.example.drag;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 通过offsetLeftAndRight()和offsetTopAndBottom()方式去移动控件
 *
 * 实际上是对layout的封装
 * */
public class Grag02 extends View {
    // 上次位置的X点
    private int mLastX;
    // 上次位置的Y点
    private int mLastY;

    public Grag02(Context context) {
        super(context);
        init();
    }

    public Grag02(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Grag02(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Grag02(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                break;
                case MotionEvent.ACTION_MOVE:
                    int offsetX = x - mLastX;
                    int offsetY = y - mLastY;
                    offsetLeftAndRight(offsetX);
                    offsetTopAndBottom(offsetY);
                    break;
                default:
                    break;
        }

        return true;
    }

    public void init() {
        setBackgroundColor(Color.RED);
    }
}
