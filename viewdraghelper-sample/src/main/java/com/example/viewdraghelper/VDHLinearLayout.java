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

public class VDHLinearLayout extends LinearLayout {
    private ViewDragHelper dragHelper;
    // 可以任意拖拽的view
    private View dragView;
    // 只能从边缘拖拽的view
    private View edgeDragView;
    // 拖拽自动返回的view
    private View autoBackView;

    public VDHLinearLayout(Context context) {
        this(context,null);
    }

    public VDHLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VDHLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VDHLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * 初始化
     * */
    private void init(){
        dragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {

            /**
             * tryCaptureView：如果返回true表示捕获相关View，你可以根据第一个参数child决定捕获哪个View
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
        });
    }

    /**
     * 如果你希望拖拽的子View是不可点击的，可以不重写onInterceptTouchEvent方法
     * */
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
    protected void onFinishInflate() {
        super.onFinishInflate();
        dragView = findViewById(R.id.dragView);
        edgeDragView = findViewById(R.id.edgeDragView);
        autoBackView = findViewById(R.id.autoBackView);
    }
}
