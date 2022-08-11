package com.gdu.baselibrary.utils.imageloade;

/**
 * Created by JohnsonFan on 2017/6/9.
 */
public interface ILoaderStrategy {

	/**
	 * 加载配置.
	 * @param options 加载图片的框架配置.
	 */
	void loaderImage(LoaderOptions options);

	/**
	 * 清理内存缓存
	 */
	void clearMemoryCache();

	/**
	 * 清理磁盘缓存
	 */
	void clearDiskCache();

}
