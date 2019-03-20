package com.example.drag;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 使用scrollTo()和scrollBy()方法需要注意的一点是，scrollTo()和scrollBy()方法移动的是View的content，即让View的内容移动，

 如果在ViewGroup中使用scrollTo()和scrollBy()方法，那么移动的将是所有的子View，如果在View中使用，那么移动的将是View的内容。
 例如，TextView，content就是它的文本，ImageView，content就是它的drawable对象。
 * */
public class Grag04 extends View {
    // 上次位置的X点
    private int mLastX;
    // 上次位置的Y点
    private int mLastY;

    public Grag04(Context context) {
        super(context);
        init();
    }

    public Grag04(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Grag04(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Grag04(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

                    // 使用者两个方法进行移动的时候，注意此时的坐标方向与平常是相反的

                    ((View)getParent()).scrollBy(-offsetX,-offsetY);
                    break;
                default:
                    break;
        }

        return true;
    }

    public void init() {
        setBackgroundColor(Color.BLACK);
    }
}
