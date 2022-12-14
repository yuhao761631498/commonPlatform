package com.gdu.baselibrary.utils.watermark;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * @author Leon (wshk729@163.com)
 * @date 2018/8/24
 * <p>
 */
public class WaterMarkInfo {

    private int mDegrees;
    private int mTextColor;
    private int mTextSize;
    private boolean mTextBold;
    private int mDx;
    private int mDy;
    private Paint.Align mAlign;

    private WaterMarkInfo(int degrees, int textColor, int textSize, boolean textBold, int dx, int dy, Paint.Align align) {
        mDegrees = degrees;
        mTextColor = textColor;
        mTextSize = textSize;
        mTextBold = textBold;
        mDx = dx;
        mDy = dy;
        mAlign = align;
    }

    public int getDegrees() {
        return mDegrees;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public int getDx() {
        return mDx;
    }

    public int getDy() {
        return mDy;
    }

    public Paint.Align getAlign() {
        return mAlign;
    }

    public int getAlignInt() {
        switch (mAlign) {
            case LEFT:
                return 0;
            case RIGHT:
                return 2;
            default:
                return 1;
        }
    }

    public boolean isTextBold() {
        return mTextBold;
    }

    void setDegrees(int degrees) {
        mDegrees = degrees;
    }

    void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    void setTextBold(boolean textBold) {
        mTextBold = textBold;
    }

    void setDx(int dx) {
        mDx = dx;
    }

    void setDy(int dy) {
        mDy = dy;
    }

    void setAlign(Paint.Align align) {
        this.mAlign = align;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private int mDegrees;
        private int mTextColor;
        private int mTextSize;
        private boolean mTextBold;
        private int mDx;
        private int mDy;
        private Paint.Align mAlign;

        private Builder() {
            mDegrees = -30;
            mTextColor = Color.parseColor("#33000000");
            mTextSize = 42;
            mTextBold = false;
            mDx = 100;
            mDy = 240;
            mAlign = Paint.Align.CENTER;
        }

        /**
         * ???????????????????????????
         *
         * @param degrees ???????????????(??????:-30)
         * @return Builder
         */
        public Builder setDegrees(int degrees) {
            mDegrees = degrees;
            return this;
        }

        /**
         * ????????????????????????
         *
         * @param textColor ????????????(??????:#33000000)
         * @return Builder
         */
        public Builder setTextColor(int textColor) {
            mTextColor = textColor;
            return this;
        }

        /**
         * ????????????????????????????????????px???
         *
         * @param textSize ????????????(??????:42px)
         * @return Builder
         */
        public Builder setTextSize(int textSize) {
            mTextSize = textSize;
            return this;
        }

        /**
         * ??????????????????????????????
         *
         * @param textBold ????????????(??????:false)
         * @return Builder
         */
        public Builder setTextBold(boolean textBold) {
            mTextBold = textBold;
            return this;
        }

        /**
         * ??????????????????X?????????????????????px???
         *
         * @param dx ??????X?????????(??????:100px)
         * @return Builder
         */
        public Builder setDx(int dx) {
            mDx = dx;
            return this;
        }

        /**
         * ??????????????????Y?????????????????????px???
         *
         * @param dy ??????Y?????????(??????:240px)
         * @return Builder
         */
        public Builder setDy(int dy) {
            mDy = dy;
            return this;
        }

        /**
         * ??????????????????????????????
         *
         * @param align ????????????(??????:Center)
         * @return Builder
         */
        public Builder setAlign(Paint.Align align) {
            mAlign = align;
            return this;
        }

        /**
         * ??????????????????????????????
         *
         * @return ????????????
         */
        public WaterMarkInfo generate() {
            return new WaterMarkInfo(mDegrees, mTextColor, mTextSize, mTextBold, mDx, mDy, mAlign);
        }
    }

}
