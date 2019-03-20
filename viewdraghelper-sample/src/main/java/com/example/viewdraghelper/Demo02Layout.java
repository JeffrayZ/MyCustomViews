package com.example.viewdraghelper;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class Demo02Layout extends ConstraintLayout {
    private ViewDragHelper mViewDragHelper;
    private Context mContext;
    private View mHomeView;
    private View mReadView;
    private View mUnlockView;
    private View mRedbagView;

    Point mHomeCenterPoint = new Point();
    Point mReadCenterPoint = new Point();
    Point mUnlockCenterPoint = new Point();
    Point mRedbagCenterPoint = new Point();
    Point mHomeOriginalPoint = new Point();

    private int mTopBound;
    private int mBottomBound;
    private int mLeftBound;
    private int mRightBound;

    /**
     * 表示当前是否正在水平拖拽
     * */
    private boolean mIsHorizontalDrag = false;

    /**
     * 表示当前是否是竖直拖拽
     * */
    private boolean mIsVerticalDrag = false;

    /**
     * 是否到达边界的标记
     */
    private boolean mIsReachBound = false;

    private int mDx;
    private int mDy;

    public Demo02Layout(Context context) {
        this(context,null);
    }

    public Demo02Layout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Demo02Layout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mViewDragHelper = ViewDragHelper.create(this,new ViewDragHelperCallback());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHomeView = findViewById(R.id.iv_home);
        mReadView = findViewById(R.id.iv_read);
        mUnlockView = findViewById(R.id.iv_unlock);
        mRedbagView = findViewById(R.id.iv_redbag);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mHomeOriginalPoint.x = mHomeView.getLeft();
        mHomeOriginalPoint.y = mHomeView.getTop();

        mHomeCenterPoint.x = mHomeView.getLeft() + mHomeView.getMeasuredWidth() / 2;
        mHomeCenterPoint.y = mHomeView.getTop() + mHomeView.getMeasuredHeight() / 2;

        mReadCenterPoint.x = mReadView.getLeft() + mReadView.getMeasuredWidth() / 2;
        mReadCenterPoint.y = mReadView.getTop() + mReadView.getMeasuredHeight() / 2;

        mUnlockCenterPoint.x = mUnlockView.getLeft() + mUnlockView.getMeasuredWidth() / 2;
        mUnlockCenterPoint.y = mUnlockView.getTop() + mUnlockView.getMeasuredHeight() / 2;

        mRedbagCenterPoint.x = mRedbagView.getLeft() + mRedbagView.getMeasuredWidth() / 2;
        mRedbagCenterPoint.y = mRedbagView.getTop() + mRedbagView.getMeasuredHeight() / 2;

        mTopBound = mRedbagCenterPoint.y - mHomeView.getMeasuredHeight() / 2;
        mLeftBound = mReadCenterPoint.x - mHomeView.getMeasuredWidth() / 2;
        mRightBound = mUnlockCenterPoint.x - mHomeView.getMeasuredWidth() / 2;
        mBottomBound = mHomeCenterPoint.y - mHomeView.getMeasuredHeight() / 2;
    }

    class ViewDragHelperCallback extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mHomeView;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            mDx = Math.abs(dx);

            if(mIsVerticalDrag){
                return mHomeView.getLeft();
            }

            /*if(Math.abs(dx) > 0){
                mIsHorizontalDrag = true;
            }*/

            if(mDy < mDx){
                mIsHorizontalDrag = true;
            }

            int mLeft = Math.min(Math.max(mLeftBound, left), mRightBound);
            return mLeft;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            mDy = Math.abs(dy);

            if (mIsHorizontalDrag) {
                return mHomeView.getTop();
            }

            /*if (Math.abs(dy) > 0) {
                mIsVerticalDrag = true;
            }*/

            if(mDy > mDx){
                mIsVerticalDrag = true;
            }

            int mTop = Math.min(Math.max(mTopBound, top), mBottomBound);
            return mTop;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if(releasedChild.equals(mHomeView)){
                mViewDragHelper.settleCapturedViewAt(mHomeOriginalPoint.x, mHomeOriginalPoint.y);
                invalidate();
            }
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if(mIsReachBound){
                return;
            }

            if(left <= mLeftBound){
                mIsReachBound = true;
                Toast.makeText(mContext, "到达左边界", Toast.LENGTH_LONG).show();
            }

            if(left >= mRightBound){
                mIsReachBound = true;
                Toast.makeText(mContext, "到达右边界", Toast.LENGTH_LONG).show();
            }

            if(top <= mTopBound){
                mIsReachBound = true;
                Toast.makeText(mContext, "到达上边界", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mViewDragHelper.continueSettling(true)){
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        resetFlags();
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    /**
     * 重置标记
     * */
    private void resetFlags() {
        mIsHorizontalDrag = false;
        mIsVerticalDrag = false;
        mIsReachBound = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }
}
