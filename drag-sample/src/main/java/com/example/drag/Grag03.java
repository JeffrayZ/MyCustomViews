package com.example.drag;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * LayoutParams保存了一个View的布局参数，因此我们可以通过动态改变LayoutParams中的布局参数来达到改变View的位置效果。
 *
 * 通过getLayoutParams()方法来获取View的LayoutParams，
 * 这里获取到的LayoutParams需要根据View所在父布局的类型来设置不同的类型，
 * 比如，我们这个自定义View是放在LinearLayout中的，那么通过getLayoutParams()获取到的就是LinearLayout.LayoutParams。
 * 因此，通过getLayoutParams()获取到LayoutParams的前提就是这个View需要有一个父布局。
 *
 * 注意不能固定控件的位置，android:layout_centerInParent="true" 类似于这种是千万不能有的
 * */
public class Grag03 extends View {
    // 上次位置的X点
    private int mLastX;
    // 上次位置的Y点
    private int mLastY;

    public Grag03(Context context) {
        super(context);
        init();
    }

    public Grag03(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Grag03(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Grag03(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
                    params.leftMargin = getLeft() + offsetX;
                    params.topMargin = getTop() + offsetY;
                    setLayoutParams(params);
                    break;
                default:
                    break;
        }

        return true;
    }

    public void init() {
        setBackgroundColor(Color.GREEN);
    }
}
