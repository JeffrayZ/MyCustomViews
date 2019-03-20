package com.example.scrollersample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class View01 extends View {
    private static final String TAG = View01.class.getSimpleName();
    private Paint mPaint;

    public View01(Context context) {
        this(context,null);
    }

    public View01(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public View01(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public View01(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        canvas.drawCircle(0,0,40.0f,mPaint);
    }

}
