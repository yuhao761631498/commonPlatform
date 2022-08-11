package com.gdu.model.config;

import android.Manifest;
import android.os.Environment;

public class StorageConfig {

    /**
     * 项目文件保存目录
     */
    public static String BaseDirectory= Environment.getExternalStorageDirectory()+"/gdu/cp/";

    public static String OUT_IMAGE_PATH = BaseDirectory + "Temp/image/";//本地副本的保存路径

    public static String CacheFileName= "CacheFileName";

    public static boolean isOnChatPage = false;  //是否在聊天界面

    public static String sCurrentUserName;

    /**
     * <p>小缩略图的宽，高是等比例压缩后的高</p>
     */
    public static final int SmallThumbnailWidth = 100;

    /**
     * 高低设备信息
     */
    public static String DEVICE_INFO = "deviceInfo";

    /**
     * 当前光类型
     */
    public static String DEVICE_LIGHT_TYPE = "deviceLightType";

    /**
     * 要播放的光通道类型
     */
    public static String DEVICE_CHANNEL_TYPE = "deviceChannelType";

    /**
     * 获取天气的appid
     */
    public static final String APP_ID = "&appid=d1dfc4be5c774b4f2ad89fb19bca0361";

    /**
     * 当前位置的纬度
     */
    public static double sGPSLat = 0;

    /**
     * 当前位置的经度
     */
    public static double sGPSLng = 0;

    /**
     * 标记是否是正常启动，非正常启动方式目前有两种：
     * 1.长时间挂在后台，导致系统回收资源而杀掉进程
     * 2.app运行时，去改变app的权限而导致体统杀掉进程
     * 系统会保持app的最后一个activity及部分资源，
     */
    public static boolean isNormalBoot = false;

    /**
     * 当前城市
     */
    public static String GD_CITY = "GDcity";

    /********************************************--ron
     * 测试服务器MODE,是否启动测试服务器的模式
     */
    public static String TestServerMode = "TestServerMode";

    public static String ImageFileName;


    public static final String[] getAppNeedPermissions() {
        String[] permissions = new String[5];
        permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        permissions[1] = Manifest.permission.READ_EXTERNAL_STORAGE;
        permissions[2] = Manifest.permission.ACCESS_FINE_LOCATION;
        permissions[3] = Manifest.permission.ACCESS_NETWORK_STATE;
        permissions[4] = Manifest.permission.CALL_PHONE;

//        permissions[4] = Manifest.permission.RECORD_AUDIO;//saga 不需要录音权限
        return permissions;
    }
}
