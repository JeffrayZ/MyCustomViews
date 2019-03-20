package com.example.drag;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 通过layout方式去移动控件
 * */
public class Grag01 extends View {
    // 上次位置的X点
    private int mLastX;
    // 上次位置的Y点
    private int mLastY;

    public Grag01(Context context) {
        super(context);
        init();
    }

    public Grag01(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Grag01(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Grag01(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
                    layout(
                            getLeft() + offsetX,
                            getTop() + offsetY,
                            getRight() + offsetX,
                            getBottom() + offsetY
                    );
                    break;
                default:
                    break;
        }

        return true;
    }

    public void init() {
        setBackgroundColor(Color.BLUE);
    }
}
