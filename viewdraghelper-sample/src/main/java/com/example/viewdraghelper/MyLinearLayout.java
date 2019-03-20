package com.example.viewdraghelper;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {
    private ViewDragHelper dragHelper;

    public MyLinearLayout(Context context) {
        this(context, null);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        dragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {

            /**
             * tryCaptureView：如果返回true表示捕获相关View
             *
             * 你可以根据第一个参数child决定捕获哪个View，这样 return child == yourView;
             */
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            /**
             * clampViewPositionVertical：计算child垂直方向的位置，top表示y轴坐标（相对于ViewGroup），默认返回0（如果不复写该方法）
             * 这里，你可以控制垂直方向可移动的范围
             * */
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                // 左边距
                if(top < 0){
                    top = 0;
                }
                // 右边距
                if(top > getHeight() - child.getMeasuredHeight()){
                    top = getHeight() - child.getMeasuredHeight();
                }
                return top;
            }

            /**
             * clampViewPositionHorizontal：与clampViewPositionVertical类似，只不过是控制水平方向的位置
             * */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                // 左边距
                if(left < 0){
                    left = 0;
                }
                // 右边距
                if(left > getWidth() - child.getMeasuredWidth()){
                    left = getWidth() - child.getMeasuredWidth();
                }
                return left;
            }

            /**
             * 当前被捕获的View释放之后回调
             * */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                // 设置回到指定位置，这里是回到初始位置
                dragHelper.settleCapturedViewAt(releasedChild.getLeft(), releasedChild.getTop());
                invalidate();
            }
        });
    }

    /**
     * 如果你希望拖拽的子View是不可点击的，可以不重写onInterceptTouchEvent方法
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}
