package com.gdu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.TypedValue;
import android.view.PixelCopy;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.amap.api.maps2d.model.LatLng;
import com.gdu.baselibrary.BaseApplication;

/**
 * 界面相关的工具类
 * @author: wixche
 */
public class MyUiUtils {
    private MyUiUtils() {
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public static Context getContext() {
        return BaseApplication.getInstance();
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * dip转px
     *
     * @param dip
     * @return
     */
    public static int dip2px(float dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    /**
     * px转dip
     *
     * @param px
     * @return
     */
    public static int px2dp(float px) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取字符数组.
     */
    public static String[] getStringArray(int id) {
        return getResources().getStringArray(id);
    }

    /**
     * 获取颜色id.
     */
    public static int getColor(int colorId) {
        return getResources().getColor(colorId);
    }

    public static String getString(int id) {
        return getResources().getString(id);
    }

    public static Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }

    /**
     * 根据id获取尺寸.
     */
    public static int getDimens(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    /**
     * 扩展布局.
     * @param id 控件id.
     * @return view.
     */
    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    // 不重复显示的Toast.
    /** 弹出提示信息的实例化变量Toast. */
    private static Toast mToast;

    /**
     * 显示Toast消息.
     * @param strId
     */
    public static void showToast(final int strId) {
        showToast(strId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示Toast消息.
     * @param strId
     * @param duration
     */
    public static void showToast(final int strId, final int duration) {
        String msg = getString(strId);
        showToast(msg, duration);
    }

    /**
     * 显示Toast消息.
     * @param content
     */
    public static void showToast(final String content) {
        showToast(content, Toast.LENGTH_SHORT);
    }

    /**
     * 显示Toast消息.
     * @param content
     * @param duration
     */
    public static void showToast(final String content, final int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), content, duration);
        } else {
            mToast.setText(content);
        }
        mToast.show();
    }

    /**
     * 显示居中的Toast消息.
     * @param strId
     */
    public static void showCenterToast(final int strId) {
        final String msg = getString(strId);
        showToast(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 显示居中的Toast消息.
     * @param content
     */
    public static void showCenterToast(final String content) {
        showToast(content, Toast.LENGTH_SHORT);
    }

    /**
     * View已经在界面上展示了，可以直接获取View的缓存
     * 对View进行量测，布局后生成View的缓存
     * View为固定大小的View，包括TextView,ImageView,LinearLayout,FrameLayout,RelativeLayout等
     * @param view 截取的View,View必须有固定的大小，不然drawingCache返回null
     * @return 生成的Bitmap
     */
    public static Bitmap getBitmapByViewOld(View view){
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        // 重新测量一遍View的宽高
        view.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(view.getHeight(), View.MeasureSpec.EXACTLY));
        // 确定View的位置
        view.layout((int) view.getX(), (int) view.getY(),
                (int) view.getX() + view.getMeasuredWidth(),
                (int) view.getY() + view.getMeasuredHeight());
        // 生成View宽高一样的Bitmap
        final Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0,
                view.getMeasuredWidth(),
                view.getMeasuredHeight());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 截取Activity布局转位Bitmap.
     * @param view 要截取的布局大小.
     * @param activity activity.
     * @param listener 截取结果回调.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void getBitmapByView(View view, Activity activity,
                                       IBitmapCopyCallBack listener) {
        final Window window = activity.getWindow();
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        final int[] locationOfViewInWindow = new int[2];
        view.getLocationInWindow(locationOfViewInWindow);
        final HandlerThread handlerThread = new HandlerThread("captureView");
        handlerThread.start();

        try {
            PixelCopy.request(window, new Rect(locationOfViewInWindow[0], locationOfViewInWindow[1],
                            locationOfViewInWindow[0] + view.getWidth(),
                            locationOfViewInWindow[1] + view.getHeight()),
                    bitmap, copyResult -> {
                        if (copyResult == PixelCopy.SUCCESS) {
                            listener.onSuccessCallBack(bitmap);
                        } else {
                            listener.onErrorCallback();
                        }
                    }, new Handler(handlerThread.getLooper()));
        } catch (IllegalArgumentException e) {
            // PixelCopy may throw IllegalArgumentException, make sure to handle it
            e.printStackTrace();
        }

    }

    public interface IBitmapCopyCallBack{
        void onSuccessCallBack(Bitmap bitmap);

        void onErrorCallback();
    }

    /** 默认城市-长沙市经纬度 */
    public static final LatLng WUHAN = new LatLng(30.4597498, 114.4395089);

//    /**
//     * 屏幕中心marker跳动
//     */
//    public static void startJumpAnimation(AMap aMap, Marker centerMarker) {
//        if (centerMarker != null ) {
//            //根据屏幕距离计算需要移动的目标点
//            final LatLng latLng = centerMarker.getPosition();
//            Point point =  aMap.getProjection().toScreenLocation(latLng);
//            point.y -= dip2px(25);
//            LatLng target = aMap.getProjection()
//                    .fromScreenLocation(point);
//            //使用TranslateAnimation,填写一个需要移动的目标点
//            Animation animation = new TranslateAnimation(target);
//            animation.setInterpolator(input -> {
//                // 模拟重加速度的interpolator
//                if(input <= 0.5) {
//                    return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
//                } else {
//                    return (float) (0.5f - Math.sqrt((input - 0.5f)*(1.5f - input)));
//                }
//            });
//            //整个移动所需要的时间
//            animation.setDuration(500);
//            //设置动画
//            centerMarker.setAnimation(animation);
//            //开始动画
//            centerMarker.startAnimation();
//
//        } else {
//            MyLogUtils.d("screenMarker is null");
//        }
//    }

}
