package com.gdu.baselibrary.utils.imageloade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;

import androidx.annotation.DrawableRes;

import java.io.File;

/**
 * Created by JohnsonFan on 2017/7/13.
 * 该类为图片加载框架的通用属性封装，不能耦合任何一方的框架
 */
public class LoaderOptions {
	private int placeholderResId;
	private int errorResId;
	private boolean isCenterCrop;
	private boolean isCenterInside;
	private boolean isCircleImage;
	/** 是否是圆角 */
	private boolean isRoundedImage;

    /** 是否缓存到本地. */
	private boolean skipLocalCache;
	private boolean skipNetCache;
	private Bitmap.Config config = Bitmap.Config.RGB_565;
	private int targetWidth;
	private int targetHeight;
    /** 圆角角度. */
	private int bitmapAngle;
    /** 旋转角度.注意:picasso针对三星等本地图片，默认旋转回0度，即正常位置。此时不需要自己rotate. */
	private float degrees;
	private Drawable placeholder;
    /** targetView展示图片. */
	private View targetView;
//	private BitmapCallBack callBack;
	private String url;
	private File file;
	private int drawableResId;
	private Uri uri;
	private Context mContext;

	public LoaderOptions(String url) {
		this.url = url;
	}

	public LoaderOptions(File file) {
		this.file = file;
	}

	public LoaderOptions(int drawableResId) {
		this.drawableResId = drawableResId;
	}

	public LoaderOptions(Uri uri) {
		this.uri = uri;
	}

    public LoaderOptions(Context context) {
        mContext = context;
    }

    public void into(View targetView) {
		this.targetView = targetView;
        ImageLoaderStrategy.getInstance().loadOptions(this);
	}

//	public void bitmap(BitmapCallBack callBack) {
//		this.callBack = callBack;
//        ImageLoaderStrategy.getInstance().loadOptions(this);
//	}

    public LoaderOptions load(String path) {
		if (path == null) {
			this.url = "";
		} else {
			this.url = path;
		}
		return this;
    }

    public LoaderOptions load(int drawable) {
        this.drawableResId = drawable;
        return this;
    }

    public LoaderOptions load(File file) {
        this.file = file;
        return this;
    }

    public LoaderOptions load(Uri uri) {
        this.uri = uri;
        return this;
    }

	public LoaderOptions placeholder(@DrawableRes int placeholderResId) {
		this.placeholderResId = placeholderResId;
		return this;
	}

	public LoaderOptions placeholder(Drawable placeholder) {
		this.placeholder = placeholder;
		return this;
	}

	public LoaderOptions error(@DrawableRes int errorResId) {
		this.errorResId = errorResId;
		return this;
	}

	public LoaderOptions centerCrop() {
		isCenterCrop = true;
		return this;
	}

	public LoaderOptions centerInside() {
		isCenterInside = true;
		return this;
	}

	public LoaderOptions circleImage() {
		isCircleImage = true;
		return this;
	}

	public LoaderOptions roundedImage() {
		isRoundedImage = true;
		return this;
	}

	public LoaderOptions config(Bitmap.Config config) {
		this.config = config;
		return this;
	}

	public LoaderOptions resize(int targetWidth, int targetHeight) {
		this.targetWidth = targetWidth;
		this.targetHeight = targetHeight;
		return this;
	}

	/**
	 * 圆角
	 * @param bitmapAngle   度数
	 * @return
	 */
	public LoaderOptions angle(int bitmapAngle) {
		this.bitmapAngle = bitmapAngle;
		return this;
	}

	public LoaderOptions skipLocalCache(boolean skipLocalCache) {
		this.skipLocalCache = skipLocalCache;
		return this;
	}

	public LoaderOptions skipNetCache(boolean skipNetCache) {
		this.skipNetCache = skipNetCache;
		return this;
	}

	public LoaderOptions rotate(float degrees) {
		this.degrees = degrees;
		return this;
	}

    public LoaderOptions setContext(Context context) {
        this.mContext = context;
        return this;
    }



    public int getPlaceholderResId() {
        return placeholderResId;
    }

    public int getErrorResId() {
        return errorResId;
    }

    public boolean isCenterCrop() {
        return isCenterCrop;
    }

    public boolean isCenterInside() {
        return isCenterInside;
    }

    public boolean isCircleImage() {
        return isCircleImage;
    }

    public boolean isRoundedImage() {
        return isRoundedImage;
    }

    public boolean isSkipLocalCache() {
        return skipLocalCache;
    }

    public boolean isSkipNetCache() {
        return skipNetCache;
    }

    public Bitmap.Config getConfig() {
        return config;
    }

    public int getTargetWidth() {
        return targetWidth;
    }

    public int getTargetHeight() {
        return targetHeight;
    }

    public int getBitmapAngle() {
        return bitmapAngle;
    }

    public float getDegrees() {
        return degrees;
    }

    public Drawable getPlaceholder() {
        return placeholder;
    }

    public View getTargetView() {
        return targetView;
    }

//    public BitmapCallBack getCallBack() {
//        return callBack;
//    }

    public String getUrl() {
        return url;
    }

    public File getFile() {
        return file;
    }

    public int getDrawableResId() {
        return drawableResId;
    }

    public Uri getUri() {
        return uri;
    }

    public Context getContext() {
        return mContext;
    }

}


