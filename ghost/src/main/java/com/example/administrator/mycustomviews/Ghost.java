package com.example.administrator.mycustomviews;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 移动小鬼动画
 */
public class Ghost extends View {
    // 三只不同颜色的画笔
    Paint mBodyPaint, mEyesPaint, mShadowPaint;
    private Context context;
    // View宽高
    private int mWidth, mHeight;
    /**
     * 默认宽高(WRAP_CONTENT)
     */
    private int mDefaultWidth;
    private int mDefaultHeight;
    /**
     * 头部的半径
     */
    private int mHeadRadius;
    /**
     * 圆心(头部)的X坐标
     */
    private int mHeadCentreX;
    /**
     * 圆心(头部)的Y坐标
     */
    private int mHeadCentreY;
    /**
     * 头部最左侧的坐标
     */
    private int mHeadLeftX;
    /**
     * 头部最右侧的坐标
     */
    private int mHeadRightX;
    /**
     * 距离View顶部的内边距
     */
    private int mPaddingTop;
    /**
     * 影子所占区域
     */
    private RectF mRectShadow;
    /**
     * 小鬼身体和影子之间的距离
     */
    private int paddingShadow;
    /**
     * 画小鬼的身体的线
     */
    private Path mPath = new Path();
    /**
     * 小鬼身体胖过头部的宽度
     */
    private int mGhostBodyWSpace;
    /**
     * 单个裙褶的宽高
     */
    private int mSkirtWidth, mSkirtHeight;
    /**
     * 裙褶的个数
     */
    private int mSkirtCount = 7;

    public Ghost(Context context) {
        this(context, null);
    }

    public Ghost(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Ghost(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Ghost(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        this.context = context;
        mDefaultHeight = DpSpPxUtils.dp2px(180, context);
        mDefaultWidth = DpSpPxUtils.dp2px(120, context);
        mPaddingTop = DpSpPxUtils.dp2px(20, context);

        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHead(canvas);
        drawShadow(canvas);
        drawBody(canvas);
        drawEyes(canvas);

        startAnim();
    }

    /**
     * 小鬼移动的动画
     */
    private void startAnim() {
        AnimatorSet aSet = new AnimatorSet();

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "translationX", 0, DpSpPxUtils.getContentWidth(context) - mWidth);
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setDuration(2000);
//        objectAnimator.start();

        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(this, "translationY", 0, 10, -10, 0);
        objectAnimator2.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator2.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator2.setDuration(200);
//        objectAnimator2.start();

        // 同时执行两个动画
        aSet.play(objectAnimator).with(objectAnimator2);
        aSet.start();
    }

    /**
     * 画眼睛
     */
    private void drawEyes(Canvas canvas) {
        canvas.drawCircle(mHeadCentreX, mHeadCentreY, mHeadRadius / 6, mEyesPaint);
        canvas.drawCircle(mHeadCentreX + mHeadRadius / 2, mHeadCentreY, mHeadRadius / 6, mEyesPaint);
    }

    /**
     * 画身子
     */
    private void drawBody(Canvas canvas) {
        /**
         * 右边的部分
         * */
        mGhostBodyWSpace = mHeadRadius * 2 / 15;
        // 画右边的身体
        mPath.moveTo(mHeadLeftX, mHeadCentreY);
        mPath.lineTo(mHeadRightX, mHeadCentreY);
        mPath.quadTo(mHeadRightX + mGhostBodyWSpace,
                mRectShadow.top - paddingShadow,
                mHeadRightX - mGhostBodyWSpace,
                mRectShadow.top - paddingShadow);


        /**
         * 从右向左画褶皱
         * */
        mSkirtWidth = (mHeadRadius * 2 - mGhostBodyWSpace * 2) / mSkirtCount;
        mSkirtHeight = mHeight / 16;
        for (int i = 1; i <= mSkirtCount; i++) {
            if (i % 2 != 0) {
                mPath.quadTo(mHeadRightX - mGhostBodyWSpace - mSkirtWidth * i + (mSkirtWidth / 2),
                        mRectShadow.top - paddingShadow - mSkirtHeight,
                        mHeadRightX - mGhostBodyWSpace - (mSkirtWidth * i),
                        mRectShadow.top - paddingShadow);
            } else {
                mPath.quadTo(mHeadRightX - mGhostBodyWSpace - mSkirtWidth * i + (mSkirtWidth / 2),
                        mRectShadow.top - paddingShadow + mSkirtHeight,
                        mHeadRightX - mGhostBodyWSpace - (mSkirtWidth * i),
                        mRectShadow.top - paddingShadow);
            }
        }


        /**
         * 左侧平滑过渡
         * */
        mPath.quadTo(mHeadLeftX - mGhostBodyWSpace,
                mRectShadow.top - paddingShadow,
                mHeadLeftX,
                mHeadCentreY);
        canvas.drawPath(mPath, mBodyPaint);
    }

    /**
     * 画影子，影子是椭圆的
     */
    private void drawShadow(Canvas canvas) {
        paddingShadow = mHeight / 10;
        mRectShadow = new RectF();
        mRectShadow.top = mHeight * 8 / 10;
        mRectShadow.bottom = mHeight * 9 / 10;
        mRectShadow.left = mWidth / 4;
        mRectShadow.right = mWidth * 3 / 4;
        canvas.drawArc(mRectShadow, 0, 360, true, mShadowPaint);
    }

    /**
     * 画头部,头是圆的
     */
    private void drawHead(Canvas canvas) {
        mHeadRadius = mWidth / 3;
        mHeadCentreX = mWidth / 2;
        mHeadCentreY = mWidth / 3 + mPaddingTop;
        mHeadLeftX = mHeadCentreX - mHeadRadius;
        mHeadRightX = mHeadCentreX + mHeadRadius;
        canvas.drawCircle(mHeadCentreX, mHeadCentreY, mHeadRadius, mBodyPaint);
    }

    /**
     * 测量宽
     * <p>
     * 当View的宽高指定为MATCH_PARENT或者明确的值的时候，就使用实际的值。
     * 当View的宽高指定为WRAP_CONTENT时，宽度为默认的120dp，高度则为180dp。
     */
    private int measureWidth(int widthMeasureSpec) {
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mWidth = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            mWidth = Math.min(mDefaultWidth, specSize);
        }
        return mWidth;
    }

    /**
     * 测量高
     * <p>
     * 当View的宽高指定为MATCH_PARENT或者明确的值的时候，就使用实际的值。
     * 当View的宽高指定为WRAP_CONTENT时，宽度为默认的120dp，高度则为180dp。
     */
    private int measureHeight(int heightMeasureSpec) {
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mHeight = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            mHeight = Math.min(mDefaultHeight, specSize);
        }
        return mHeight;
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mBodyPaint = new Paint();
        mBodyPaint.setAntiAlias(true);
        mBodyPaint.setStyle(Paint.Style.FILL);
        mBodyPaint.setColor(Color.WHITE);

        mEyesPaint = new Paint();
        mEyesPaint.setAntiAlias(true);
        mEyesPaint.setStyle(Paint.Style.FILL);
        mEyesPaint.setColor(Color.BLACK);

        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setColor(Color.argb(60, 0, 0, 0));
    }
}
