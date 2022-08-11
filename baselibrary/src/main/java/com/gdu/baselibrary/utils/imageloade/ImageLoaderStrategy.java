package com.gdu.baselibrary.utils.imageloade;

import android.content.Context;

import java.io.ObjectStreamException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 图片加载类 策略或者静态代理模式，开发者只需要关心ImageLoaderStrategy + LoaderOptions
 *
 * @author wixche
 */
public class ImageLoaderStrategy {
	private static ILoaderStrategy sLoader;
	private static volatile ImageLoaderStrategy sInstance;
	private static Lock sLock = new ReentrantLock();

	private ImageLoaderStrategy() {
	}

	//单例模式
	public static ImageLoaderStrategy getInstance() {
		if (sInstance == null) {
			sLock.lock();
			try {
				if (sInstance == null) {
					//若切换其它图片加载框架，可以实现一键替换
					sInstance = new ImageLoaderStrategy();
				}
			} finally {
				sLock.unlock();
			}
		}
		return sInstance;
	}

	/**
	 * 防止反序列化.
	 * @return
	 * @throws ObjectStreamException
	 */
	private ImageLoaderStrategy readResolve() throws ObjectStreamException {
		return sInstance;
	}

	//提供实时替换图片加载框架的接口
	public void setImageLoader(ILoaderStrategy loader) {
		if (loader != null) {
			sLoader = loader;
		}
	}

    public LoaderOptions with(Context context) {
        return new LoaderOptions(context);
    }

//	public LoaderOptions load(String path) {
//		return new LoaderOptions(path);
//	}
//
//    public LoaderOptions load(int drawable) {
//		return new LoaderOptions(drawable);
//	}
//
//	public LoaderOptions load(File file) {
//		return new LoaderOptions(file);
//	}
//
//	public LoaderOptions load(Uri uri) {
//		return new LoaderOptions(uri);
//	}

	public void loadOptions(LoaderOptions options) {
		try {
			if (sLoader == null) {
				return;
			}
			sLoader.loaderImage(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearMemoryCache() {
		if (sLoader == null) {
			return;
		}
		sLoader.clearMemoryCache();
	}

	public void clearDiskCache() {
		if (sLoader == null) {
			return;
		}
		sLoader.clearDiskCache();
	}
}
