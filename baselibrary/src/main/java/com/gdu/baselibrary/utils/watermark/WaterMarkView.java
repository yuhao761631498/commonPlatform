package com.gdu.baselibrary.utils.watermark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gdu.baselibrary.R;

/**
 * @author Leon (wshk729@163.com)
 * @date 2018/8/24
 * <p>
 */
public class WaterMarkView extends View {

    private static final String DEFAULT_SEPARATOR = "///";
    private TextPaint mTextPaint = new TextPaint();

    private String[] mText;
    private int mDegrees;
    private int mTextColor;
    private int mTextSize;
    private boolean mTextBold;
    private int mDx;
    private int mDy;
    private Paint.Align mAlign;
    private boolean mSync;
    private int textWidth, textHeight;

    public WaterMarkView(Context context) {
        this(context, null);
    }

    public WaterMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaterMarkView);
        mDegrees = typedArray.getInt(R.styleable.WaterMarkView_water_mark_degree, WaterMarkManager.INFO != null ? WaterMarkManager.INFO.getDegrees() : -30);
        String text = typedArray.getString(R.styleable.WaterMarkView_water_mark_text);
        if (text != null) {
            mText = text.split(DEFAULT_SEPARATOR);
        }
        mTextColor = typedArray.getColor(R.styleable.WaterMarkView_water_mark_textColor, WaterMarkManager.INFO != null ? WaterMarkManager.INFO.getTextColor() : Color.parseColor("#33000000"));
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.WaterMarkView_water_mark_textSize, WaterMarkManager.INFO != null ? WaterMarkManager.INFO.getTextSize() : 42);
        mTextBold = typedArray.getBoolean(R.styleable.WaterMarkView_water_mark_textBold, WaterMarkManager.INFO != null && WaterMarkManager.INFO.isTextBold());
        mDx = typedArray.getDimensionPixelSize(R.styleable.WaterMarkView_water_mark_dx, WaterMarkManager.INFO != null ? WaterMarkManager.INFO.getDx() : 100);
        mDy = typedArray.getDimensionPixelSize(R.styleable.WaterMarkView_water_mark_dy, WaterMarkManager.INFO != null ? WaterMarkManager.INFO.getDy() : 240);
        int align = typedArray.getInt(R.styleable.WaterMarkView_water_mark_align, WaterMarkManager.INFO != null ? WaterMarkManager.INFO.getAlignInt() : 1);
        mAlign = align == 0 ? Paint.Align.LEFT : align == 2 ? Paint.Align.RIGHT : Paint.Align.CENTER;
        mSync = typedArray.getBoolean(R.styleable.WaterMarkView_water_mark_sync, true);
        typedArray.recycle();

        setBackgroundColor(Color.TRANSPARENT);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTypeface(mTextBold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        mTextPaint.setTextAlign(mAlign);

        mText = mText == null && mSync ? WaterMarkManager.CONTENT : mText;

        textWidth = 0;
        textHeight = 0;
        if (mText != null && mText.length > 0) {
            for (String s : mText) {
                Rect tvRect = new Rect();
                mTextPaint.getTextBounds(s, 0, s.length(), tvRect);
                textWidth = textWidth > tvRect.width() ? textWidth : tvRect.width();
                textHeight += (tvRect.height() + 10);
            }
        }

        if (mSync) {
            WaterMarkManager.LIST.add(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mText != null && mText.length > 0) {
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();

            if (measuredWidth == 0 || measuredHeight == 0) {
                return;
            }

            int canvasLength = measuredWidth > measuredHeight ? measuredWidth : measuredHeight;

            canvas.save();
            canvas.rotate(mDegrees, measuredWidth / 2, measuredHeight / 2);

            canvas.save();
            int y = 0;
            boolean odd = true;
            while (y < canvasLength + textHeight) {
                int x = odd ? 0 : -(textWidth + mDx) / 2;
                while (x < canvasLength + textWidth) {
                    drawTexts(mText, mTextPaint, canvas, x, y);
                    x = x + textWidth + mDx;
                }
                y = y + textHeight + mDy;
                odd = !odd;
            }
            canvas.restore();
        }
    }

    private void drawTexts(String[] ss, Paint paint, Canvas canvas, int x, int y) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int length = ss.length;
        float total = (length - 1) * (bottom - top) + (fontMetrics.descent - fontMetrics.ascent);
        float offset = total / 2 - bottom;
        for (int i = 0; i < length; i++) {
            float yAxis = -(length - i - 1) * (bottom - top) + offset;
            canvas.drawText(ss[i], x, y + yAxis + 10, paint);
        }
    }

    /**
     * ????????????????????????
     *
     * @param text ????????????
     */
    public void setText(String... text) {
        mText = text;

        textWidth = 0;
        textHeight = 0;
        if (mText != null && mText.length > 0) {
            for (String s : mText) {
                Rect tvRect = new Rect();
                mTextPaint.getTextBounds(s, 0, s.length(), tvRect);
                textWidth = textWidth > tvRect.width() ? textWidth : tvRect.width();
                textHeight += (tvRect.height() + 10);
            }
        }
        postInvalidate();
    }

    /**
     * ??????????????????????????????
     *
     * @param text ????????????
     */
    void setSyncText(String... text) {
        if (mSync) {
            setText(text);
        }
    }

    /**
     * ????????????????????????
     *
     * @param degrees ????????????(??????:-30)
     */
    public void setDegrees(int degrees) {
        mDegrees = degrees;
        postInvalidate();
    }

    /**
     * ??????????????????????????????
     *
     * @param degrees ????????????(??????:-30)
     */
    void setSyncDegrees(int degrees) {
        if (mSync) {
            setDegrees(degrees);
        }
    }

    /**
     * ????????????????????????
     *
     * @param textColor ????????????(??????:#33000000)
     */
    public void setTextColor(int textColor) {
        mTextColor = textColor;
        mTextPaint.setColor(mTextColor);
        postInvalidate();
    }

    /**
     * ??????????????????????????????
     *
     * @param textColor ????????????(??????:#33000000)
     */
    void setSyncTextColor(int textColor) {
        if (mSync) {
            setTextColor(textColor);
        }
    }

    /**
     * ????????????????????????????????????px???
     *
     * @param textSize ????????????(??????:42px)
     */
    public void setTextSize(int textSize) {
        mTextSize = textSize;
        mTextPaint.setTextSize(mTextSize);
        postInvalidate();
    }

    /**
     * ??????????????????????????????????????????px???
     *
     * @param textSize ????????????(??????:42px)
     */
    void setSyncTextSize(int textSize) {
        if (mSync) {
            setTextSize(textSize);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param textBold ????????????(??????:false)
     */
    public void setTextBold(boolean textBold) {
        mTextBold = textBold;
        mTextPaint.setTypeface(mTextBold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        postInvalidate();
    }

    /**
     * ????????????????????????????????????
     *
     * @param textBold ????????????(??????:false)
     */
    void setSyncTextBold(boolean textBold) {
        if (mSync) {
            setTextBold(textBold);
        }
    }

    /**
     * ????????????X????????????????????????px???
     *
     * @param dx X????????????(??????:100px)
     */
    public void setDx(int dx) {
        this.mDx = dx;
        postInvalidate();
    }

    /**
     * ??????????????????X????????????????????????px???
     *
     * @param dx X????????????(??????:100px)
     */
    void setSyncDx(int dx) {
        if (mSync) {
            setDx(dx);
        }
    }

    /**
     * ????????????Y????????????????????????px???
     *
     * @param dy Y????????????(??????:240px)
     */
    public void setDy(int dy) {
        this.mDy = dy;
        postInvalidate();
    }

    /**
     * ??????????????????Y????????????????????????px???
     *
     * @param dy Y????????????(??????:240px)
     */
    void setSignDy(int dy) {
        if (mSync) {
            setDy(dy);
        }
    }

    /**
     * ????????????????????????
     *
     * @param align ????????????(??????:Center)
     */
    public void setAlign(Paint.Align align) {
        this.mAlign = align;
        postInvalidate();
    }

    /**
     * ??????????????????????????????
     *
     * @param align ????????????(??????:Center)
     */
    void setSignAlign(Paint.Align align) {
        if (mSync) {
            setAlign(align);
        }
    }

    /**
     * ???????????????????????????????????????
     */
    public void onDestroy() {
        if (mSync) {
            WaterMarkManager.LIST.remove(this);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}