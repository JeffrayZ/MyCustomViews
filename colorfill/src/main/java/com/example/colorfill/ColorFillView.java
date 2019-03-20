package com.example.colorfill;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class ColorFillView extends View {
    private LayerDrawable mDrawables;

    public ColorFillView(Context context) {
        this(context, null);
    }

    public ColorFillView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorFillView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ColorFillView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mDrawables = (LayerDrawable) getBackground();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mDrawables.getIntrinsicWidth(), mDrawables.getIntrinsicHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Drawable drawable = findDrawable(x, y);
            if (drawable != null) {
                drawable.setColorFilter(randomColor(), PorterDuff.Mode.SRC_IN);
            }
        }
        return super.onTouchEvent(event);
    }

    private int randomColor() {
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return color;
    }

    /**
     * 根据触摸点获取图层
     * */
    private Drawable findDrawable(float x, float y) {
        final int numberOfLayers = mDrawables.getNumberOfLayers();
        Drawable drawable = null;
        Bitmap bitmap = null;
        for(int i = numberOfLayers - 1; i >= 0; i--){
            drawable = mDrawables.getDrawable(i);
            bitmap = ((BitmapDrawable) drawable).getBitmap();

            int pixel = bitmap.getPixel((int)x, (int)y);

            if(pixel == Color.TRANSPARENT){
                continue;
            }
            return drawable;
        }
        return null;
    }
}
