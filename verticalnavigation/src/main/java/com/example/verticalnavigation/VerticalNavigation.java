package com.example.verticalnavigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class VerticalNavigation extends ViewGroup {

    private Context mContext;
    private boolean isScrolling;
    private int mLastY;
    /**
     * 加速度检测
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 手指按下时的getScrollY
     */
    private int mScrollStart;
    /**
     * 手指抬起时的getScrollY
     */
    private int mScrollEnd;

    private Scroller mScroller;

    /**
     * 记录当前页
     */
    private int currentPage = 0;

    private OnPageChangeListener mOnPageChangeListener;

    public VerticalNavigation(Context context) {
        this(context, null);
    }

    public VerticalNavigation(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public VerticalNavigation(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;
        // 初始化
        mScroller = new Scroller(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            /**
             * 设置主布局的高度
             *
             * 高度必须能容纳所有的导航页
             * */
            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
            lp.height = DpSpPxUtils.getContentHeight(mContext) * childCount;
            setLayoutParams(lp);

            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != GONE) {
                    child.layout(l, i * DpSpPxUtils.getContentHeight(mContext), r, (i + 1) * DpSpPxUtils.getContentHeight(mContext));
                    if(child instanceof ViewGroup){
                        ViewGroup vg = (ViewGroup) child;
                        vg.getChildAt(0).layout(
                                DpSpPxUtils.getContentWidth(mContext) / 3,
                                DpSpPxUtils.getContentHeight(mContext) / 3,
                                DpSpPxUtils.getContentWidth(mContext) * 2 / 3,
                                DpSpPxUtils.getContentHeight(mContext) * 2/ 3);
                    }
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 测量，使宽高最大值
         * */
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isScrolling) {
            return super.onTouchEvent(event);
        }

        int action = event.getAction();
        int y = (int) event.getY();

        initVelocity(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScrollStart = getScrollY();
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                int dy = mLastY - y;

                int scrollY = getScrollY();
//                Log.e("scrollY", scrollY+"");

                // 已经到达顶端，下拉多少，就往上滚动多少
                if (dy < 0 && scrollY + dy < 0) {
                    dy = -scrollY;
                }

                // 已经到达底部，上拉多少，就往下滚动多少
                if (dy > 0 && scrollY + dy > getHeight() - DpSpPxUtils.getContentHeight(mContext)) {
                    dy = getHeight() - DpSpPxUtils.getContentHeight(mContext) - scrollY;
                }

                scrollBy(0, dy);
                break;
            case MotionEvent.ACTION_UP:
                mScrollEnd = getScrollY();

                int dScrollY = mScrollEnd - mScrollStart;

                // 往上滑动
                if (wantScrollToNext()) {
                    if (shouldScrollToNext()) {
                        mScroller.startScroll(0, getScrollY(), 0, DpSpPxUtils.getContentHeight(mContext) - dScrollY);
                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    }
                }

                if (wantScrollToPre())// 往下滑动
                {
                    if (shouldScrollToPre()) {
                        mScroller.startScroll(0, getScrollY(), 0, -DpSpPxUtils.getContentHeight(mContext) - dScrollY);

                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    }
                }

                isScrolling = true;
                postInvalidate();
                recycleVelocity();
                break;
            default:
                break;
        }

        mLastY = y;
        return true;
    }

    /**
     * 根据滚动距离判断是否能够滚动到上一页
     *
     * @return
     */
    private boolean shouldScrollToPre() {
        return -mScrollEnd + mScrollStart > DpSpPxUtils.getContentHeight(mContext) / 2 || Math.abs(getVelocity()) > 600;
    }

    /**
     * 根据用户滑动，判断用户的意图是否是滚动到上一页
     *
     * @return
     */
    private boolean wantScrollToPre() {
        return mScrollEnd < mScrollStart;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        } else {
            int position  = getScrollY() / DpSpPxUtils.getContentHeight(mContext);
            if(position != currentPage){
                if(mOnPageChangeListener != null){
                    currentPage = position;
                    mOnPageChangeListener.onPageChange(currentPage);
                }
            }
            isScrolling = false;
        }
    }

    /**
     * 根据滚动距离判断是否能够滚动到下一页
     *
     * @return
     */
    private boolean shouldScrollToNext() {
        return mScrollEnd - mScrollStart > DpSpPxUtils.getContentHeight(mContext) / 2 || Math.abs(getVelocity()) > 600;
    }

    /**
     * 根据用户滑动，判断用户的意图是否是滚动到下一页
     *
     * @return
     */
    private boolean wantScrollToNext() {
        return mScrollEnd > mScrollStart;
    }

    /**
     * 释放资源
     */
    private void recycleVelocity() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 初始化加速度检测
     */
    private void initVelocity(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 获取y方向的加速度
     *
     * @return
     */
    private int getVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        return (int) mVelocityTracker.getYVelocity();
    }

    /**
     * 设置回调接口
     *
     * @param onPageChangeListener
     */
    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    /**
     * 回调接口
     *
     * @author zhy
     */
    public interface OnPageChangeListener {
        void onPageChange(int currentPage);
    }

}
