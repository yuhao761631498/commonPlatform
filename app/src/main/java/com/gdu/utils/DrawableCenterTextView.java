package com.gdu.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.noober.background.view.BLTextView;

/**
 * drawable与文本一起居中显示
 */
public class DrawableCenterTextView extends BLTextView {
    public DrawableCenterTextView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterTextView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableLeft = drawables[0];
            if (drawableLeft != null) {
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = drawableLeft.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                final int halfSpacing = (int) ((getWidth() - bodyWidth) / 2);
                if (halfSpacing == 0) {
                    return;
                }
                setPadding(halfSpacing, 0, halfSpacing, 0);
            }

            Drawable drawableTop = drawables[1];
            if (drawableTop != null) {
                final Paint.FontMetrics dm  = getPaint().getFontMetrics();
                float textHeight = dm.descent - dm.ascent + dm.leading;
                int drawablePadding = getCompoundDrawablePadding();
                int drawableHeight = drawableTop.getIntrinsicWidth();
                float bodyHeight = textHeight + drawableHeight + drawablePadding;
                final int halfSpacing = (int) ((getWidth() - bodyHeight) / 2);
                if (halfSpacing == 0) {
                    return;
                }
                setPadding(0, halfSpacing, 0, halfSpacing);
            }

            Drawable drawableRight = drawables[2];
            if (drawableRight != null) {
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = drawableRight.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                final int halfSpacing = (int) ((getWidth() - bodyWidth) / 2);
                if (halfSpacing == 0) {
                    return;
                }
                setPadding(halfSpacing, 0, halfSpacing, 0);
            }

            Drawable drawableBottom = drawables[3];
            if (drawableBottom != null) {
                final Paint.FontMetrics dm  = getPaint().getFontMetrics();
                float textHeight = dm.descent - dm.ascent + dm.leading;
                int drawablePadding = getCompoundDrawablePadding();
                int drawableHeight = drawableBottom.getIntrinsicWidth();
                float bodyHeight = textHeight + drawableHeight + drawablePadding;
                final int halfSpacing = (int) ((getWidth() - bodyHeight) / 2);
                if (halfSpacing == 0) {
                    return;
                }
                setPadding(0, halfSpacing, 0, halfSpacing);
            }

        }
        super.onDraw(canvas);
    }
}