package com.gdu.baselibrary.utils;

public class MyConstants {
    private MyConstants() {
    }

    /** 成功回调状态码(0) */
    public static final int CALLBACK_SUCCESS = 0;

    /** 错误回调(401)_token失效 */
    public static final int ERROR_CODE_401 = 401;
    /** 鲁班压缩忽略指定大小 */
    public static final int LUBAN_IGONE_SIZE = 500;

    /**
     * 传递是否显示批示信息的key
     */
    public final static String IS_SHOW_WAIT_VIEW_KEY = "showWaitView";

    /** 普通底部弹窗点击内容回掉对应的key. */
    public static final String CLICK_ITEM_NAME = "clickItemName";
    /** 回掉拍照或相册剪切后的图片路径对应的key. */
    public static final String PHOTO_FILE_PATH = "photoFilePath";
    /** Bundle传输地图目标地点坐标的key. */
    public static final String MAP_COORDINATE = "mapCoordinate";
    /** Bundle传输地图地址信息的key. */
    public static final String MAP_ADDRESS_CONTENT = "mapAddressContent";
    /** Bugly的AppId. */
    public static final String BUGLY_APPID = "3782a6f5dd";
    /** Bugly的AppKey. */
    public static final String BUGLY_APPKEY = "a6e4a146-912d-4842-b45e-a2900c524bff";

    /** 存储忽略更新版本号的Key. */
    public static final String IGONRE_VERSION = "ignoreUpVersion";
    /** 传递预览照片路径的key. */
    public static final String SEND_PIC_PATH_KEY = "sendPicPath";

    /** 手动定位地址内容. */
    public static final String RESULT_ADDRESS = "resultAddress";
    /** 手动定位地址坐标内容. */
    public static final String RESULT_ADDRESS_COORDINATE = "resultAddressCoordinate";
    /** 保存授权码的key. */
    public static final String SAVE_AUTH_CODE_KEY = "saveAuthCode";
    /** 保存通过授权码请求到的信息. */
    public static final String SAVE_AUTH_CODE_CONTENT_KEY = "saveAuthCodeContent";

    // <<<--------------------------------- Event key ----------------------------------
    /** 传递事件默认的key_1. */
    public static final int DEFAULT_EVENT_MESSAGE_KEY_1 = 101;
    /** 传递事件默认的key_2. */
    public static final int DEFAULT_EVENT_MESSAGE_KEY_2 = 102;
    /** 传递事件默认的key_3. */
    public static final int DEFAULT_EVENT_MESSAGE_KEY_3 = 103;
    /** token失效处理. */
    public static final int TOKEN_LOSE_EFFICACY = 104;
    /** 传递手动定位地址的Intent. */
    public static final int RESULT_ADDRESS_INTENT = 105;
    /** 发送跳转personFragment界面事件的key. */
    public static final int JUMP_TO_PERSON_FRAGMENT = 106;
    /** 设置授权码失败. */
    public static final int SET_AUTH_CODE_FAIL = 107;
    /** 设置授权码成功. */
    public static final int SET_AUTH_CODE_SUC = 108;
    /** 预警处理(补充处理)/分派/接警成功. */
    public static final int ALARM_HANDLE_SUC = 109;
    // --------------------------------- Event key ---------------------------------->>>

    // <<<--------------------------------- RequestCode ----------------------------------
    /** 打开界面的默认RequestCode_1. */
    public static final int OPEN_ACTIVITY_DEFAULT_REQUEST_CODE_1 = 201;
    /** 打开界面的默认RequestCode_2. */
    public static final int OPEN_ACTIVITY_DEFAULT_REQUEST_CODE_2 = 202;
    /** 打开界面的默认RequestCode_3. */
    public static final int OPEN_ACTIVITY_DEFAULT_REQUEST_CODE_3 = 203;
    /** 图片请求 */
    public static final int REQUEST_PIC_CODE = 204;
    /** 视频请求 */
    public static final int REQUEST_VIDEO_CODE = 205;
    /** 显示手动定位界面的RequestCode */
    public static final int SHOW_MANUAL_LOCATION_REQUEST_CODE = 206;
    // --------------------------------- RequestCode ---------------------------------->>>

    // <<<--------------------------------- PreferencesKey ----------------------------------
    /** 传递参数用的默认Key1. */
    public static final String DEFAULT_PARAM_KEY_1 = "defaultParam1";
    /** 传递参数用的默认Key1. */
    public static final String DEFAULT_PARAM_KEY_2 = "defaultParam2";
    /** 传递参数用的默认Key1. */
    public static final String DEFAULT_PARAM_KEY_3 = "defaultParam3";
    // --------------------------------- PreferencesKey ---------------------------------->>>
}
