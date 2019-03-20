package com.example.colorfill;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.Random;
import java.util.Stack;

/**
 * @author Jeffray
 * <p>
 * 扫描线填充法
 */
public class ColorImageView extends AppCompatImageView {
    private Bitmap mBitmap;
    /**
     * 边界的颜色
     */
    private int mBorderColor = -1;
    private boolean hasBorderColor = false;
    private Stack<Point> mStacks = new Stack<Point>();

    public ColorImageView(Context context) {
        this(context, null);
    }

    public ColorImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, -1);
    }

    public ColorImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ColorImageView);
        mBorderColor = ta.getColor(R.styleable.ColorImageView_border_color, -1);
        hasBorderColor = (mBorderColor != -1);

        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();

        // 以宽度为标准，等比例缩放view的高度
        setMeasuredDimension(viewWidth, getDrawable().getIntrinsicHeight() * viewWidth / getDrawable().getIntrinsicWidth());
        // 根据drawable，去得到一个和view一样大小的bitmap
        BitmapDrawable drawable = (BitmapDrawable) getDrawable();
        Bitmap bm = drawable.getBitmap();
        mBitmap = Bitmap.createScaledBitmap(bm, getMeasuredWidth(), getMeasuredHeight(), false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 填色
            fillColorToSameArea(x, y);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 根据x,y获得改点颜色，进行填充
     *
     * @param x
     * @param y
     */
    private void fillColorToSameArea(int x, int y) {
        Bitmap bm = mBitmap;

        int pixel = bm.getPixel(x, y);
        if (pixel == Color.TRANSPARENT || (hasBorderColor && mBorderColor == pixel)) {
            return;
        }
        int newColor = randomColor();

        int w = bm.getWidth();
        int h = bm.getHeight();
        //拿到该bitmap的颜色数组
        int[] pixels = new int[w * h];
        bm.getPixels(pixels, 0, w, 0, 0, w, h);
        //填色
        fillColor(pixels, w, h, pixel, newColor, x, y);
        //重新设置bitmap
        bm.setPixels(pixels, 0, w, 0, 0, w, h);
        setImageDrawable(new BitmapDrawable(getResources(), bm));

    }

    /**
     * @param pixels   像素数组
     * @param w        宽度
     * @param h        高度
     * @param pixel    当前点的颜色
     * @param newColor 填充色
     * @param i        横坐标
     * @param j        纵坐标
     */
    private void fillColor(int[] pixels, int w, int h, int pixel, int newColor, int i, int j) {
        // 步骤一：将种子点x,y入栈
        mStacks.push(new Point(i, j));

        // 步骤二：判断栈是否为空
        // 如果为空，则结束算法，否则取出栈顶元素作为当前扫描线的种子点x,y
        // y是当前的扫描线
        while (!mStacks.isEmpty()) {
            // 步骤三：从种子点x,y出发，沿当前扫描向左向右两个方向填充，直到边界
            // 分别标记区域的左端点，右端点坐标为 xLeft 和 xRight
            Point seed = mStacks.pop();
            // 左边的填色数量
            int count = fillLineLeft(pixels, pixel, w, h, newColor, seed.x, seed.y);
            int xLeft = seed.x - count + 1;
            count = fillLineRight(pixels, pixel, w, h, newColor, seed.x + 1, seed.y);
            int xRight = seed.x + count;

            // 步骤四：分别检查与当前扫描线相邻的 y-1 和 y+1 两条扫描线在区间[xLeft,xRight]中的像素
            // 从 xRight 开始向 xLeft 方向搜索，假设扫描的区间为 AAABAAC（A为种子点颜色）
            // 那么将B、C作为新的种子点入栈，并执行 步骤二
            if (seed.y - 1 >= 0) {
                findSeedInNewLine(pixels, pixel, w, h, seed.y - 1, xLeft, xRight);
            }

            if (seed.y + 1 < h) {
                findSeedInNewLine(pixels, pixel, w, h, seed.y + 1, xLeft, xRight);
            }
        }
    }

    /**
     * 在新行找种子节点
     *
     * @param pixels
     * @param pixel
     * @param w
     * @param h
     * @param i
     * @param left
     * @param right
     */
    private void findSeedInNewLine(int[] pixels, int pixel, int w, int h, int i, int left, int right) {
        /**
         * 获得该行的开始索引
         */
        int begin = i * w + left;
        /**
         * 获得该行的结束索引
         */
        int end = i * w + right;

        boolean hasSeed = false;

        int rx = -1, ry = -1;

        ry = i;

        /**
         * 从end到begin，找到种子节点入栈（AAABAAAB，则B前的A为种子节点）
         */
        while (end >= begin) {
            if (pixels[end] == pixel) {
                if (!hasSeed) {
                    rx = end % w;
                    mStacks.push(new Point(rx, ry));
                    hasSeed = true;
                }
            } else {
                hasSeed = false;
            }
            end--;
        }
    }

    /**
     * 往右填色，返回填充的个数
     *
     * @return
     */
    private int fillLineRight(int[] pixels, int pixel, int w, int h, int newColor, int x, int y) {
        int count = 0;

        while (x < w) {
            //拿到索引
            int index = y * w + x;
            if (needFillPixel(pixels, pixel, index)) {
                pixels[index] = newColor;
                count++;
                x++;
            } else {
                break;
            }

        }

        return count;
    }

    /**
     * 往左填色，返回填色的数量值
     *
     * @return
     */
    private int fillLineLeft(int[] pixels, int pixel, int w, int h, int newColor, int x, int y) {
        int count = 0;
        while (x >= 0) {
            //计算出索引
            int index = y * w + x;

            if (needFillPixel(pixels, pixel, index)) {
                pixels[index] = newColor;
                count++;
                x--;
            } else {
                break;
            }

        }
        return count;
    }

    private boolean needFillPixel(int[] pixels, int pixel, int index) {
        if (hasBorderColor) {
            return pixels[index] != mBorderColor;
        } else {
            return pixels[index] == pixel;
        }
    }

    /**
     * 返回一个随机颜色
     *
     * @return
     */
    private int randomColor() {
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return color;
    }
}
