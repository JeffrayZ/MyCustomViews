package com.example.viewdraghelper;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 点击事件与拖动事件的冲突处理
 * */
public class VDHLinearLayout04 extends LinearLayout {
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
    private Context context;
    /**平滑滚动的view*/
    private View smooth;

    public VDHLinearLayout04(Context context) {
        this(context, null);
    }

    public VDHLinearLayout04(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VDHLinearLayout04(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initDrag();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VDHLinearLayout04(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initDrag();
    }

    private void initDrag() {
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
                return top;
            }

            /**
             * clampViewPositionHorizontal：与clampViewPositionVertical类似，只不过是控制水平方向的位置
             * */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            /**
             * 处理点击事件和滑动事件的冲突
             *
             * 设置
             * */
            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            /**
             * 处理点击事件和滑动事件的冲突
             * */
            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            /**
             * 当前被捕获的View释放之后回调
             * */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == autoBackView) {
                    // 设置回到指定位置，这里是回到初始位置
                    dragHelper.settleCapturedViewAt(autoBackViewOriginLeft, autoBackViewOriginTop);
                } else if(releasedChild == dragView){
                    // 快速滚动的意思，一般手指离开后 view 还会由于惯性继续滑动。
                    dragHelper.flingCapturedView(getPaddingLeft(),getPaddingTop(),
                            getWidth()-getPaddingRight()-releasedChild.getWidth(),
                            getHeight()-getPaddingBottom()-releasedChild.getHeight());
                }
                invalidate();
            }

            /**
             * 与setEdgeTrackingEnabled配合使用，边缘拖拽
             * */
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                // 捕获 edgeDragView
                dragHelper.captureChildView(edgeDragView, pointerId);
            }

            /**
             * 边缘被点击
             * */
            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
                // 平滑滚动到指定位置
                dragHelper.smoothSlideViewTo(smooth, 610, 1500);
                invalidate();
            }

            /**
             * 当状态改变的时候回调，返回相应的状态（这里有三种状态）
             *
             * STATE_IDLE 闲置状态
             * STATE_DRAGGING 正在拖动
             * STATE_SETTLING 放置到某个位置
             * */
            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
            }

            /**
             * 当你拖动的View位置发生改变的时候回调
             * */
            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
            }

            /**
             * 捕获View的时候调用的方法
             * */
            @Override
            public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
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
        smooth = findViewById(R.id.smooth);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        autoBackViewOriginLeft = autoBackView.getLeft();
        autoBackViewOriginTop = autoBackView.getTop();
//        Log.e("onLayout", autoBackViewOriginLeft+"");
//        Log.e("onLayout", autoBackViewOriginTop+"");
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}
