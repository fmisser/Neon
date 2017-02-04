package com.fmisser.neon.common;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.NonNull;

/**
 * Date: 2017/2/4.
 * Author: fmisser
 * Description: 含 badge 的 menu item, @see http://www.javarticles.com//2015/09/android-icon-badge-example-using-layer-list-drawable.html
 */

public class BadgeDrawable extends Drawable {

    private Paint mBadgePaint;
    private Paint mTextPaint;
    private Rect mTextRect = new Rect();

    private String mCount;
    private boolean mWillDraw;

    public BadgeDrawable() {

        mBadgePaint = new Paint();
        mBadgePaint.setColor(Color.RED);
        mBadgePaint.setAntiAlias(true);
        mBadgePaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(28);
    }

    public void setCount(int c) {
        mCount = String.valueOf(Math.max(0, c));
        mWillDraw = !"0".equals(mCount);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        if (!mWillDraw) {
            return;
        }

        Rect bounds = getBounds();
        //radius=width/4+2.5 looks better
        float radius = Math.min(bounds.width(), bounds.height())/4.0f + 2.5f;
        //x=width-radius+6.2f, y=radius-7.0f looks better
        float centerX = bounds.width()- radius + 6.2f;
        float centerY = radius - 7.0f;
        if (mCount.length() <= 2) {
            canvas.drawCircle(centerX, centerY, radius, mBadgePaint);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                float radiusX = radius + 5.0f;
                centerX += 4.0f;
                //绘制圆角
                canvas.drawRoundRect(centerX - radiusX, centerY - radius, centerX + radiusX, centerY + radius, radius, radius, mBadgePaint);
            } else {
                canvas.drawCircle(centerX, centerY, radius + 4.5f, mBadgePaint);
            }
        }

        mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTextRect);
        float textY = centerY + (mTextRect.height() / 2.0f);
        if (mCount.length() > 2) {
            canvas.drawText("99+", centerX, textY, mTextPaint);
        } else {
            canvas.drawText(mCount, centerX, textY, mTextPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        //do nothing
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        //do nothing
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }


    /**
     * 设置badge数量
     * @param icon menu item 的 LayerDrawable 对象
     * @param count 显示的badge数量,等于0时不显示, 大于99时会显示成99+
     */
    public static void setBadgeCount(LayerDrawable icon, int id, int count) {
        BadgeDrawable badgeDrawable;
        //reuse
        Drawable drawable = icon.findDrawableByLayerId(id);
        if (drawable != null && drawable instanceof BadgeDrawable) {
            badgeDrawable = (BadgeDrawable) drawable;
        } else {
            badgeDrawable = new BadgeDrawable();
        }

        badgeDrawable.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(id, badgeDrawable);
    }
}

