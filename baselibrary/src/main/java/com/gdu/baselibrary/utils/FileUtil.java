package com.gdu.baselibrary.utils;

import android.content.Context;
import android.media.audiofx.EnvironmentalReverb;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.gdu.baselibrary.BaseApplication;
import com.gdu.baselibrary.R;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by zhaoyong on 2016/6/21.
 * Glide缓存工具类
 */
public class FileUtil {

    /**
     * 可根据项目需求自行更改
     */
    public static final String APP_PATH = BaseApplication.getInstance().getResources().getString(R.string.app_name);
    public static final String IMAGE_CACHE_PATH = "imageCache";

    /**
     * 是否存在SD卡
     *
     * @return
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return TextUtils.equals(status, Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取应用SD卡缓存的根路径
     *
     * @return
     */
    public static File getExternalStorageDir() {
        File RootDir;
        //判断是否有SD卡
        if (hasSDCard()) {
            RootDir = Environment.getExternalStorageDirectory();
            return RootDir;
        } else {
            ToastUtil.s("没有发现SD卡");
        }
        return null;
    }

    public static String getExternalStoragePath() {
        String path = "";
        //判断是否有SD卡
        if (hasSDCard()) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
            return path;
        }
        return path;
    }

    /**
     * 获取应用SD卡缓存的根路径
     *
     * @return
     */
    public static File getAppRootDir() {
        File RootDir;
        //判断是否有SD卡
        if (hasSDCard()) {
            RootDir = new File(Environment.getExternalStorageDirectory(), APP_PATH);
            if (!RootDir.exists())
                RootDir.mkdirs();
            return RootDir;
        } else {
            ToastUtil.s("没有发现SD卡");
        }
        return null;
    }

    /**
     * Glide 缓存路径
     *
     * @return
     */
    public static File getGlideCacheDir() {
        File file = new File(getDiskCacheDir(), IMAGE_CACHE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 缓存路径
     * 卸载应用时会一并删除
     * /data/data/包名/cache
     * /mnt/sdcard/Android/data/包名/cache
     *
     * @return
     */
    public static File getDiskCacheDir() {
        File dir = null;
        if (hasSDCard()) {
            dir = BaseApplication.getInstance().getExternalCacheDir();
        } else {
            dir = BaseApplication.getInstance().getCacheDir();
        }
        return dir;
    }

    /**
     * Files路径
     * 卸载应用时会一并删除
     * /data/data/包名/files
     * /mnt/sdcard/Android/data/包名/files
     *
     * @return
     */
    public static File getDiskFilesDir() {
        File dir = null;
        if (hasSDCard()) {
            dir = BaseApplication.getInstance().getExternalFilesDir(null);
        } else {
            dir = BaseApplication.getInstance().getFilesDir();
        }
        return dir;
    }

    public static File getAppDir(String dirName) {
        if (getAppRootDir() != null) {
            File file = new File(getAppRootDir(), dirName);
            if (!file.exists())
                file.mkdirs();
            return file;
        }
        return null;
    }

    /**
     * 获取应用SD卡缓存的根路径
     *
     * @return
     */
    public static File getRootDir() {
        File RootDir;
        String status = Environment.getExternalStorageState();
        //判断是否有SD卡
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            RootDir = new File(Environment.getExternalStorageDirectory(), APP_PATH);
            if (!RootDir.exists())
                RootDir.mkdirs();
            return RootDir;
        } else {
            ToastUtil.s("没有发现SD卡");
        }
        return null;
    }

    /**
     * 保存bitmap为图片
     *
     * @param bitmap   要保存的bitmap
     * @param dirFile  要保存的文件夹
     * @param saveName 要保存的文件名称
     */
//    public static void saveBitmap2File(Context context, Bitmap bitmap, File dirFile, String saveName) {
//        ImageUtil.saveBitmap2File(context,bitmap, dirFile, saveName);
//        // 通知图库更新
//      //  BaseApplication.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(dirFile, saveName))));
//    }

    /**
     * 清除图片磁盘缓存
     */
    public static void clearImageDiskCache() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(BaseApplication.getInstance()).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(BaseApplication.getInstance()).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public static void clearImageMemoryCache() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(BaseApplication.getInstance()).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片所有缓存
     */
    public static void clearImageAllCache() {
        clearImageDiskCache();
        clearImageMemoryCache();
        deleteFolderFile(getGlideCacheDir().getPath(), true);
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public static String getCacheSize() {
        try {
            return getFormatSize(getFolderSize(getGlideCacheDir()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取缓存值
     */
    public static String getTotalCacheSize(Context context) {
        long cacheSize = 0;
        try {
            cacheSize = getFolderSize(context.getCacheDir());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                cacheSize += getFolderSize(context.getExternalCacheDir());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 清除所有缓存
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    /**
     * 删除某个文件
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        if (dir != null) {
            return dir.delete();
        } else {
            return false;
        }
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    public static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        //        if (kiloByte < 1) {
        //            return size + "Byte";
        //        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 文件是否存在
     * @param fileName
     * @return
     */
    public static boolean isFileExist(String fileName) {
        if (fileName != null) {
            File file = new File(fileName);
            return file.exists();
        } else {
            return false;
        }
    }

    /**
     * 创建文件夹
     * @param filePath
     */
    public static void createFolder(String filePath){
        if (filePath != null) {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    return ;
                }
            }
            if (!file.exists()) {
                file.mkdir();
            }
        }
    }

    /**
     * 获取文件大小(字节byte)
     */
    public static long getFileLength(File file){
        long fileLength = 0L;
        if(file.exists() && file.isFile()){
            fileLength = file.length();
        }
        return fileLength;
    }

}