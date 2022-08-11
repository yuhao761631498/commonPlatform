package com.gdu.baselibrary.utils.imageloade;

import android.content.Context;
import android.widget.ImageView;

import com.gdu.baselibrary.R;


/**
 * 图片加载工具类
 * 为方便以后随时更换图片加载库
 * 并且统一配置图片加载方式
 *
 * @author Hunter
 */
public class MyImageLoadUtils {

    public static final int DEFAULT_PLACEHOLDER_RESID = R.mipmap.default_photo_video;

    public static void loadImage(Context context, String imageUrl, int resId, ImageView imageView) {
        ImageLoaderStrategy.getInstance().with(context).load(imageUrl).placeholder(resId).into(imageView);
    }

    public static void loadImageWithCenterCrop(Context context, String imageUrl,
                                               ImageView imageView) {
        loadImageWithCenterCrop(context, imageUrl, DEFAULT_PLACEHOLDER_RESID, imageView);
    }

    public static void loadImageWithCenterCrop(Context context, String imageUrl, int resId,
                                               ImageView imageView) {
        ImageLoaderStrategy.getInstance().with(context).load(imageUrl).centerCrop().placeholder(resId).into(imageView);
    }

    public static void loadImage(Context context, String imageUrl, ImageView imageView) {
        loadImage(context, imageUrl, DEFAULT_PLACEHOLDER_RESID, imageView);
    }

    public static void loadImage(Context context, int drawable, ImageView imageView) {
        loadImage(context, drawable, DEFAULT_PLACEHOLDER_RESID, imageView);
    }

    public static void loadImage(Context context, int drawable, int resId, ImageView imageView) {
        ImageLoaderStrategy.getInstance().with(context).load(drawable).placeholder(resId).into(imageView);
    }

    public static void loadCircleImageWithUrl(Context context, String imageUrl, int resId,
                                              ImageView imageView) {
        ImageLoaderStrategy.getInstance().with(context).circleImage().load(imageUrl).placeholder(resId).into(imageView);
    }

    public static void loadCircleImageWithDrawable(Context context, int drawable, int resId,
                                                   ImageView imageView) {
        ImageLoaderStrategy.getInstance().with(context).circleImage().load(drawable).placeholder(resId).into(imageView);
    }

    public static void loadRoundedImageWithDrawable(Context context, int drawable,
                                                    int bitmapAngle, int resId,
                                                    ImageView imageView) {
        ImageLoaderStrategy.getInstance().with(context).roundedImage().load(drawable).angle(bitmapAngle).placeholder(resId).into(imageView);
    }

    public static void loadRoundedImageWithUrl(Context context, String imageUrl,
                                               int bitmapAngle, int resId,
                                               ImageView imageView) {
        ImageLoaderStrategy.getInstance().with(context).roundedImage().load(imageUrl).angle(bitmapAngle).placeholder(resId).into(imageView);
    }

    public static void loadImageNoDefault(Context context, String imageUrl, ImageView imageView) {
        ImageLoaderStrategy.getInstance().with(context).load(imageUrl).into(imageView);
    }

    public static void clearImageCache() {
        ImageLoaderStrategy.getInstance().clearDiskCache();//清除磁盘缓存
        ImageLoaderStrategy.getInstance().clearMemoryCache();//清除内存缓存
    }
}
