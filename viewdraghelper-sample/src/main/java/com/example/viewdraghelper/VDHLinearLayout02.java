package com.example.viewdraghelper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class VDHLinearLayout02 extends LinearLayout {
    private ViewDragHelper dragHelper;
    // 可以任意拖拽的view
    private View dragView;
    // 只能从边缘拖拽的view
    private View edgeDragView;
    // 拖拽自动返回的view
    private View autoBackView;
    // 弹回去的左边距
    private int autoBackViewOriginLeft;
    // 弹回去的上边距
    private int autoBackViewOriginTop;

    public VDHLinearLayout02(Context context) {
        this(context, null);
    }

    public VDHLinearLayout02(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VDHLinearLayout02(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public VDHLinearLayout02(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        dragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {

            /**
             * tryCaptureView：如果返回true表示捕获相关View，你可以根据第一个参数child决定捕获哪个View
             */
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == dragView || child == autoBackView;
            }

            /**
             * clampViewPositionVertical：计算child垂直方向的位置，top表示y轴坐标（相对于ViewGroup），默认返回0（如果不复写该方法）
             * 这里，你可以控制垂直方向可移动的范围
             * */
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                // 左边距
                if (top < 0) {
                    top = 0;
                }
                // 右边距
                if (top > getHeight() - child.getMeasuredHeight()) {
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
                if (left < 0) {
                    left = 0;
                }
                // 右边距
                if (left > getWidth() - child.getMeasuredWidth()) {
                    left = getWidth() - child.getMeasuredWidth();
                }
                return left;
            }

            /**
             * 当前被捕获的View释放之后回调
             * */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == autoBackView) {
                    // 设置回到指定位置，这里是回到初始位置
                    dragHelper.settleCapturedViewAt(autoBackViewOriginLeft, autoBackViewOriginTop);
                    invalidate();
                }
            }

            /**
             * 与setEdgeTrackingEnabled配合使用
             * */
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                // 捕获 edgeDragView
                dragHelper.captureChildView(edgeDragView, pointerId);
            }

        });

        // 设置左边缘可以被Drag
        dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
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
    protected void onFinishInflate() {
        super.onFinishInflate();
        dragView = findViewById(R.id.dragView);
        edgeDragView = findViewById(R.id.edgeDragView);
        autoBackView = findViewById(R.id.autoBackView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        autoBackViewOriginLeft = autoBackView.getLeft();
        autoBackViewOriginTop = autoBackView.getTop();
        Log.e("onLayout", autoBackViewOriginLeft+"");
        Log.e("onLayout", autoBackViewOriginTop+"");
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}
