package com.gdu.command.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.gdu.command.R;
import com.gdu.utils.MyUiUtils;

/**
 * 自定义方向盘控件.
 * @author wixche
 */
public class SteeringWheelView extends View {
    private final String TAG = "SteeringWheelView";
    /** 最小宽高 */
    private final int MIN_WIDTH_HEIGHT = 90;
    private Paint mPaint;
    private Path mRightPath, mBottomPath, mLeftPath, mTopPath ;
    /** 背景矩形宽高 */
    private int viewWH;
    /** 包裹外园的矩形宽高 */
    private int outsideRFWH;

    private int strokeWidth = MyUiUtils.dip2px(1);
    private String normalBgColor = "#33FFFFFF";
    private String selectBgColor = "#3f000000";
    private String normalStrokeColor = "#99FFFFFF";
    private String selectStrokeColor = "#33FFFFFF";
    private PorterDuffXfermode mXfermode;

    private Bitmap arrowBitmap;
    private Matrix mMatrix;
    /** 外圆半径 */
    private int outsideCircleRadius;
    /** 内圆半径 */
    private int insideCircleRadius;
    /** 外园半径及圆心点左边 */
    private int centerXY;
    private RectF outsideRf;
    private RectF insideRf;
    /** 四个方向按钮背景的宽度 */
    private int btnWidth;
    private Region regRight, regBot, regLeft, regTop;

    private int clickOrientation = -1;

    /** 长按最长等待时间 */
    private static final int MAX_LONG_PRESS_TIME = 1000;
    /** 单击后等待的时间 */
    private static final int MAX_SINGLE_CLICK_TIME = 220;
    private long lastDownTime = 0;
    private long lastUpTime = 0;

    private OnItemClickListener mListener;

//    private HandlerThread mHandlerThread = new HandlerThread("SteeringWheelThread");
//    private Handler mHandler = new Handler(mHandlerThread.getLooper());
    private Handler mHandler = new Handler();

    public SteeringWheelView(Context context) {
        super(context);
        init();
    }

    public SteeringWheelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SteeringWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initPaint();
        regRight = new Region();
        regBot = new Region();
        regLeft = new Region();
        regTop = new Region();
        arrowBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_direction_arrow);
        mMatrix = new Matrix();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX, pointY;
//        Log.i(TAG, "onTouchEvent() action = " + event.getAction());
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastDownTime = System.currentTimeMillis();
                pointX = event.getX();
                pointY = event.getY();

                if (regRight.contains((int) pointX, (int) pointY)) {
                    clickOrientation = 1;
                } else if (regBot.contains((int) pointX, (int) pointY)) {
                    clickOrientation = 2;
                } else if (regLeft.contains((int) pointX, (int) pointY)) {
                    clickOrientation = 3;
                } else if (regTop.contains((int) pointX, (int) pointY)) {
                    clickOrientation = 4;
                }
                if (mListener != null) {
                    mHandler.postDelayed(longRunnable, MAX_LONG_PRESS_TIME);
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                lastUpTime = System.currentTimeMillis();
                if (lastUpTime - lastDownTime <= MAX_SINGLE_CLICK_TIME) {
                    mHandler.removeCallbacks(longRunnable);
                    if (mListener != null && clickOrientation != -1) {
                        mListener.onSingleClick(clickOrientation);
                        clickOrientation = -1;
                        invalidate();
                    }
                } else {
                    mListener.onActionUp();
                    clickOrientation = -1;
                    invalidate();
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
        final int width = getViewWidth(widthMeasureSpec);
        final int height = getViewHeight(heightMeasureSpec);
        viewWH = Math.min(width, height);
        outsideRFWH = viewWH - strokeWidth * 2;
        // 获取中心点位置(内/外园共同的)这同时也是半径的大小
        centerXY = viewWH / 2;
        outsideCircleRadius = viewWH / 2 - strokeWidth;
        // 获取内园半径
        insideCircleRadius = outsideCircleRadius / 2 - strokeWidth;
        btnWidth = outsideCircleRadius - insideCircleRadius;
        outsideRf = new RectF(strokeWidth, strokeWidth, outsideRFWH, outsideRFWH);
        final float insideLT = centerXY - insideCircleRadius;
        final float insideRB = centerXY + insideCircleRadius;
        insideRf = new RectF(insideLT, insideLT, insideRB, insideRB);
        setMeasuredDimension(viewWH, viewWH);
    }

    private int getViewWidth(int widthMeasureSpec) {
        final int widthType = MeasureSpec.getMode(widthMeasureSpec);
        int width = 0;
        switch (widthType) {
            case MeasureSpec.EXACTLY:
            case MeasureSpec.AT_MOST:
                width = MeasureSpec.getSize(widthMeasureSpec);
                break;
            default:
                width = MyUiUtils.dip2px(MIN_WIDTH_HEIGHT);
                break;
        }
        return width;
    }

    private int getViewHeight(int heightMeasureSpec) {
        final int heightType = MeasureSpec.getMode(heightMeasureSpec);
        int height = 0;
        switch (heightType) {
            case MeasureSpec.EXACTLY:
            case MeasureSpec.AT_MOST:
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
            default:
                height = MyUiUtils.dip2px(MIN_WIDTH_HEIGHT);
                break;
        }
        return height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画右边方向键背景
        mRightPath = drawBtnBg(canvas, outsideRf, insideRf, -42, 38, 1,
                clickOrientation);
        final RectF rightRF = new RectF();
        mRightPath.computeBounds(rightRF, false);
        regRight.setPath(mRightPath, new Region((int) rightRF.left, (int) rightRF.top, (int) rightRF.right,
                (int) rightRF.bottom));

        // 画底部方向键背景
        mBottomPath = drawBtnBg(canvas, outsideRf, insideRf, 48, 128, 2,
                clickOrientation);
        final RectF bottomRF = new RectF();
        mBottomPath.computeBounds(bottomRF, false);
        regBot.setPath(mBottomPath, new Region((int) bottomRF.left, (int) bottomRF.top, (int) bottomRF.right,
                (int) bottomRF.bottom));

        // 画左边方向键背景
        mLeftPath = drawBtnBg(canvas, outsideRf, insideRf, 138, 218, 3,
                clickOrientation);
        final RectF leftRF = new RectF();
        mLeftPath.computeBounds(leftRF, false);
        regLeft.setPath(mLeftPath, new Region((int) leftRF.left, (int) leftRF.top, (int) leftRF.right,
                (int) leftRF.bottom));

        // 画顶部方向键背景
        mTopPath = drawBtnBg(canvas, outsideRf, insideRf, 228, 308, 4,
                clickOrientation);
        final RectF topRF = new RectF();
        mTopPath.computeBounds(topRF, false);
        regTop.setPath(mTopPath, new Region((int) topRF.left, (int) topRF.top, (int) topRF.right,
                (int) topRF.bottom));

        drawRightArrow(canvas);
        drawBottomArrow(canvas);
        drawLeftArrow(canvas);
        drawTopArrow(canvas);
    }

    /**
     * 画右边的方向键背景区域
     * @param canvas
     * @param outsideRf
     * @param insideRf
     * @param outStartAngle
     * @param inStartAngle
     * @param orientation
     * @param selectOrientation
     */
    private Path drawBtnBg(Canvas canvas, RectF outsideRf, RectF insideRf, int outStartAngle,
                           int inStartAngle, int orientation, int selectOrientation) {
//        Log.i(TAG, "drawBtnBg()");
        Path mPath = new Path();
        mPath.arcTo(outsideRf, outStartAngle, 84, true);
        mPath.arcTo(insideRf, inStartAngle, -80, false);
        mPath.close();

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor(orientation == selectOrientation ? selectBgColor : normalBgColor));
        canvas.drawPath(mPath, mPaint);

        mPaint.setColor(Color.parseColor(orientation == selectOrientation ? selectStrokeColor : normalStrokeColor));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN);
        mPaint.setXfermode(mXfermode);
        canvas.drawPath(mPath, mPaint);
        mPaint.setXfermode(null);
        return mPath;
    }

    private void drawRightArrow(Canvas canvas) {
        final int bitMapCenterX = centerXY + insideCircleRadius + btnWidth / 2;
        final int bitWidth = arrowBitmap.getWidth();
        final int bitHeight = arrowBitmap.getHeight();
        mMatrix.setRotate(90);
        final Bitmap newBit = Bitmap.createBitmap(arrowBitmap, 0, 0, bitWidth, bitHeight, mMatrix, true);
        canvas.drawBitmap(newBit, bitMapCenterX - bitWidth / 2, centerXY - bitHeight / 2,  mPaint);
    }

    private void drawBottomArrow(Canvas canvas) {
        final int bitMapCenterX = centerXY + insideCircleRadius + btnWidth / 2;
        final int bitWidth = arrowBitmap.getWidth();
        final int bitHeight = arrowBitmap.getHeight();
        mMatrix.setRotate(180);
        final Bitmap newBit = Bitmap.createBitmap(arrowBitmap, 0, 0, bitWidth, bitHeight, mMatrix, true);
        canvas.drawBitmap(newBit, centerXY - bitWidth / 2, bitMapCenterX - bitHeight / 2,  mPaint);
    }

    private void drawLeftArrow(Canvas canvas) {
        final int bitMapCenterX = centerXY - (insideCircleRadius + btnWidth / 2);
        final int bitWidth = arrowBitmap.getWidth();
        final int bitHeight = arrowBitmap.getHeight();
        mMatrix.setRotate(-90);
        final Bitmap newBit = Bitmap.createBitmap(arrowBitmap, 0, 0, bitWidth, bitHeight, mMatrix, true);
        canvas.drawBitmap(newBit, bitMapCenterX - bitWidth / 2, centerXY - bitHeight / 2,  mPaint);
    }

    private void drawTopArrow(Canvas canvas) {
        final int bitMapCenterX = centerXY - (insideCircleRadius + btnWidth / 2);
        final int bitWidth = arrowBitmap.getWidth();
        final int bitHeight = arrowBitmap.getHeight();
        mMatrix.setRotate(0);
        final Bitmap newBit = Bitmap.createBitmap(arrowBitmap, 0, 0, bitWidth, bitHeight, mMatrix, true);
        canvas.drawBitmap(newBit, centerXY - bitWidth / 2, bitMapCenterX - bitHeight / 2,  mPaint);
    }

    public void setListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        /**
         * 单击处理
         * @param position
         */
        void onSingleClick(int position);

        /**
         * 长按处理
         * @param position
         */
        void onLongClick(int position);

        /**
         * 取消点击处理
         */
        void onActionUp();
    }

    private Runnable longRunnable = new Runnable() {
        @Override
        public void run() {
            if (mListener != null && clickOrientation != -1) {
                mListener.onLongClick(clickOrientation);
            }
        }
    };

}
