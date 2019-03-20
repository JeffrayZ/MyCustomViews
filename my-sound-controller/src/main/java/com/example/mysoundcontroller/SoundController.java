package com.example.mysoundcontroller;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * 画圆弧
 *
 * 画虚线圆圈
 *
 * 手势监听
 * */
public class SoundController extends View {
    /**
     * 选中的颜色
     */
    private int selectColor;
    /**
     * 未选中的颜色
     */
    private int unSelectColor;
    /**
     * 宽度
     */
    private int mCircleWidth;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 当前进度
     */
    private int mCurrentCount = 3;
    /**
     * 中间的图片
     */
    private Bitmap mImage;
    /**
     * 个数
     */
    private int mCount;
    /**
     * 每个块块间的间隙
     */
    private int mSplitSize;

    private Rect mRect;

    private int mWidth;
    private int mHeight;
    private int mDefaultHeight;
    private int mDefaultWidth;

    public SoundController(Context context) {
        this(context, null);
    }

    public SoundController(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoundController(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SoundController(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomVolumControlBar, defStyleAttr, defStyleRes);

        selectColor = array.getColor(R.styleable.CustomVolumControlBar_firstColor, Color.GREEN);
        unSelectColor = array.getColor(R.styleable.CustomVolumControlBar_secondColor, Color.CYAN);
        mImage = BitmapFactory.decodeResource(getResources(), array.getResourceId(R.styleable.CustomVolumControlBar_bg, 0));
        mCircleWidth = array.getDimensionPixelSize(R.styleable.CustomVolumControlBar_circleWidth, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
        mCount = array.getInt(R.styleable.CustomVolumControlBar_dotCount, 20);
        mSplitSize = array.getInt(R.styleable.CustomVolumControlBar_splitSize, 20);

        array.recycle();
        mPaint = new Paint();
        mRect = new Rect();
        mDefaultHeight = DpSpPxUtils.dp2px(180, context);
        mDefaultWidth = DpSpPxUtils.dp2px(180, context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 设置圆环的宽度
        mPaint.setStrokeWidth(mCircleWidth);
        // 定义线段断点形状为圆头
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        // 设置空心
        mPaint.setStyle(Paint.Style.STROKE);

        // 获取圆心的坐标
        int center = mWidth / 2;
        int radius = center - mCircleWidth / 2;

        // 绘制块
        drawOval(canvas, center, radius);

//        /**
//         * 计算内切正方形的位置
//         */
//        int relRadius = radius - mCircleWidth / 2;// 获得内圆的半径
//        /**
//         * 内切正方形的距离顶部 = mCircleWidth + relRadius - √2 / 2
//         */
//        mRect.left = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth;
//        /**
//         * 内切正方形的距离左边 = mCircleWidth + relRadius - √2 / 2
//         */
//        mRect.top = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth;
//        mRect.bottom = (int) (mRect.left + Math.sqrt(2) * relRadius);
//        mRect.right = (int) (mRect.left + Math.sqrt(2) * relRadius);
//
//        /**
//         * 如果图片比较小，那么根据图片的尺寸放置到正中心
//         */
//        if (mImage.getWidth() < Math.sqrt(2) * relRadius)
//        {
//            mRect.left = (int) (mRect.left + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getWidth() * 1.0f / 2);
//            mRect.top = (int) (mRect.top + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getHeight() * 1.0f / 2);
//            mRect.right = (int) (mRect.left + mImage.getWidth());
//            mRect.bottom = (int) (mRect.top + mImage.getHeight());
//
//        }
//        // 绘图
//        canvas.drawBitmap(mImage, null, mRect, mPaint);
    }

    /**
     * 根据参数画出每个小块
     */
    private void drawOval(Canvas canvas, int center, int radius) {
        /**
         * 根据需要画的个数以及间隙计算每个块块所占的比例
         */
        float itemSize = (360 * 1.0f - mCount * mSplitSize) / mCount;
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        mPaint.setColor(selectColor);
        for (int i = 0; i < mCount; i++) {
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint);
        }

        // 设置圆环的颜色
        mPaint.setColor(unSelectColor);
        for (int i = 0; i < mCurrentCount; i++) {
            // 根据进度画圆弧
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint);
        }
    }

    /**
     * 当前数量+1
     */
    public void up() {
        mCurrentCount++;
        postInvalidate();
    }

    /**
     * 当前数量-1
     */
    public void down() {
        mCurrentCount--;
        postInvalidate();
    }

    private int xDown, xUp;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = (int) event.getY();
                break;

            case MotionEvent.ACTION_UP:
                xUp = (int) event.getY();
                if (xUp > xDown)// 下滑
                {
                    down();
                } else {
                    up();
                }
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
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

}
