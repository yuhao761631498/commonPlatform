package com.gdu.baselibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.gdu.model.config.UrlConfig;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @author wixche
 */
public class CommonUtils {
    private CommonUtils() {
    }

    /**
     * 获取当前的进程名称.
     *
     * @param context 上下文.
     * @return 进程名称.
     */
    public static String getProcessName(Context context) {
        final ActivityManager manager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String processNameStr = "";
        final List<ActivityManager.RunningAppProcessInfo> runningApps =
                manager.getRunningAppProcesses();
        if (runningApps == null) {
            return processNameStr;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    processNameStr = proInfo.processName;
                }
            }
        }
        return processNameStr;
    }

    /**
     * 判断返回的接口名称是否与指定的名称一致.
     *
     * @param localInterfaceName 指定的接口名称.
     * @param backInterfaceName  返回的接口名称.
     * @return true 一致; false 不一致.
     */
    public static boolean interfaceNameIsSame(String localInterfaceName, String backInterfaceName) {
        boolean isSame = false;
        boolean isHaveContent = localInterfaceName != null && !isEmptyString(localInterfaceName)
                && backInterfaceName != null && !isEmptyString(backInterfaceName)
                && localInterfaceName.toLowerCase().equals(backInterfaceName.toLowerCase());
        if (isHaveContent) {
            isSame = true;
        }
        return isSame;
    }

    /**
     * 判断两个String内容是否相同.
     *
     * @param str1 待比对待第一个参数.
     * @param str2  待比对待第二个参数.
     * @return true 一致; false 不一致.
     */
    public static boolean isSameStrContent(String str1, String str2) {
        boolean isSame = false;
        boolean isHaveContent = str1 != null && !str1.isEmpty()
                && str2 != null && !str2.isEmpty()
                && str1.toLowerCase().equals(str2.toLowerCase());
        if (isHaveContent) {
            isSame = true;
        }
        return isSame;
    }

    /**
     * 生成一个0 到 count 之间的随机数
     *
     * @param endNum
     * @return
     */
    public static int getNum(int endNum) {
        if (endNum > 0) {
            final Random random = new Random();
            return random.nextInt(endNum);
        }
        return 0;
    }

//    /**
//     * 获取共享的后转化的Uri.
//     *
//     * @param context 上下文.
//     * @param file    共享文件.
//     * @return 转化后的Uri.
//     */
//    public static Uri getUriFormFile(Context context, File file) {
//        Uri convertUri;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            convertUri = FileProvider.getUriForFile(context, MyConstants.PROVIDER_AUTHORITIES,
//                    file);
//        } else {
//            convertUri = Uri.fromFile(file);
//        }
//        return convertUri;
//    }

    /**
     * 获取手机IMEI号.
     *
     * @return
     */
    public static String getIMEI(Activity activity) {
        TelephonyManager telephonyManager =
                (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
        return imei;
    }

    /**
     * 获取手机IMSI号.
     *
     * @return
     */
    public static String getIMSI(Activity activity) {
        TelephonyManager mTelephonyMgr =
                (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String imsi = mTelephonyMgr.getSubscriberId();
        return imsi;
    }

    /**
     * 如果传入的是Null则转为空字符串.
     * @param content 传入内容.
     * @return
     */
    public static String convertNull2EmptyStr(String content) {
        if (isEmptyString(content)) {
            return "";
        }
        return content;
    }

    /**
     * 判断字符串是否为空.
     * @param str str.
     * @return
     */
    public static boolean isEmptyString(String str) {
        return str == null || str.isEmpty() || str.trim().length() == 0;
    }

    /**
     * 判断列表是否为空.
     * @param data data.
     * @return
     */
    public static <T> boolean isEmptyList(List<T> data) {
        return data == null || data.size() == 0;
    }

    /**
     * 判断字符串是否不为空.
     * @param str str.
     * @return
     */
    public static boolean isNotEmptyString(String str) {
        return !isEmptyString(str);
    }

    /**
     * 判断数组里面是否包含某个元素-通过String.
     * @param array 数组.
     * @param element 元素.
     * @return true: 包含; false: 不包含.
     */
    public static boolean arrayIsConstantElementByString(String[] array, String element) {
        boolean isContains = false;
        for (final String s : array) {
            if (s.equals(element)) {
                isContains = true;
                break;
            }
        }
        return isContains;
    }


    /**
     * 判断当前是否是白天.
     *
     * @param date 当前时间.
     * @return true: 白天; false: 晚上.
     */
    public static boolean isDayTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return hour > 7 && hour < 18;
    }

    /**
     * 隐藏软键盘.
     * @param context 上下文.
     * @param view 要隐藏的EditText
     */
    public static void hideInputSoftKey(Context context, View view) {
        if (view == null ||  context == null) {
            return;
        }
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        if (view.hasFocus()) {
            view.clearFocus();
        }
//        imm.hideSoftInputFromWindow(view.getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(view.getWindowToken(),
                WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
    }

    /**
     * 显示软键盘.
     * @param context 上下文.
     * @param view 要隐藏的EditText
     */
    public static void showInputSoftKey(Context context, View view) {
        if (view == null || context == null) {
            return;
        }
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        if (!view.hasFocus()) {
            view.requestFocus();
        }
        //显示软键盘
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 验证帐号是否符合规则.
     * @param account 帐号.
     * @return true 正确; false 错误.
     */
    public static boolean verifyAccount(String account) {
        boolean isMather = false;
        final String pattern = "^[a-zA-Z0-9_]{1,20}$";
        if (Pattern.matches(pattern, account)) {
            isMather = true;
        }
        return isMather;
    }

    /**
     * 验证是否是中文姓名.
     * @param content 帐号.
     * @return true 正确; false 错误.
     */
    public static boolean verifyChineseName(String content) {
        boolean isMather = false;
        final String pattern = "^[\\u4e00-\\u9fa5]{1,10}$";
        if (Pattern.matches(pattern, content)) {
            isMather = true;
        }
        return isMather;
    }

    /**
     * 含有中文的帐号校验.
     * @param content 帐号.
     * @return true 正确; false 错误.
     */
    public static boolean verifyChineseAccount(String content) {
        boolean isMather = false;
        final String pattern = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{1,20}$";
        if (Pattern.matches(pattern, content)) {
            isMather = true;
        }
        return isMather;
    }

    /**
     * 验证手机号码是否正确.
     * @param mobile 手机号码.
     * @return true 正确; false 错误.
     */
    public static boolean verifyMobile(String mobile) {
        boolean isMather = false;
        final String pattern = "^[1]([3456789])[0-9]{9}$";
        if (Pattern.matches(pattern, mobile)) {
            isMather = true;
        }
        return isMather;
    }

    /**
     * 验证验证码是否正确.
     * @param code 验证码.
     * @return true 正确; false 错误.
     */
    public static boolean verifyCode(String code) {
        final String pattern = "^[0-9]{1,4}$";
        boolean isMather = false;
        if (Pattern.matches(pattern, code) && code.length() > 0) {
            isMather = true;
        }
        return isMather;
    }

    /**
     * 验证密码是否正确.
     * @param pwd 密码.
     * @return true 正确; false 错误.
     */
    public static boolean verifyPwd(String pwd) {
        boolean isPwd = false;
        final String pattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$";
        if (Pattern.matches(pattern, pwd)) {
            isPwd = true;
        }
        return isPwd;
    }

    /**
     * 验证邮箱地址是否正确.
     * @param email 密码.
     * @return true 正确; false 错误.
     */
    public static boolean verifyEmail(String email) {
        boolean isPwd = false;
        final String pattern = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (Pattern.matches(pattern, email)) {
            isPwd = true;
        }
        return isPwd;
    }

//    /**
//     * 通过隐式意图调用系统安装程序安装APK
//     */
//    public static void install(Context context, String saveFilePath) {
//        final File file = new File(saveFilePath);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        // 由于没有在Activity环境下启动Activity,设置下面的标签
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        //判读版本是否在7.0以上
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
//            Uri apkUri =
//                    FileProvider.getUriForFile(context, MyConstants.PROVIDER_AUTHORITIES, file);
//            //添加这一句表示对目标应用临时授权该Uri所代表的文件
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
//        } else {
//            intent.setDataAndType(Uri.fromFile(file),
//                    "application/vnd.android.package-archive");
//        }
//        context.startActivity(intent);
//    }

    /**
     * 新_调用相机/相册界面.
     * @param activity 调用的界面.
     */
    public static void showTakePictureAndAlbumDialogNew(Activity activity) {
//        Matisse.from(activity)
//                .choose(MimeType.ofImage())
//                .showSingleMediaType(true)
//                .capture(true)
//                .countable(false)
//                .maxSelectable(1)
//                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//                .thumbnailScale(0.5f)
//                .imageEngine(new GlideEngine())
//                .forResult(MyConstants.TAKE_PICTURE_OR_ALBUM_REQUEST_CODE);

//        showTakePictureAndAlbumDialogCustomNumber(activity, 1);
    }

//    /**
//     * 调用相册上传图片界面.
//     * @param activity 调用的界面.
//     * @param number   指定可选择数量.
//     */
//    public static void showTakePictureAndAlbumDialogCustomNumber(Activity activity, int number) {
//        showTakePictureAndAlbumDialogCustomNumber(activity, number, -1);
//    }
//
//    /**
//     * 调用相册上传图片界面.
//     * @param activity 调用的界面.
//     * @param number   指定可选择数量.
//     * @param code     requestCode.
//     */
//    public static void showTakePictureAndAlbumDialogCustomNumber(Activity activity, int number, int code) {
//        Matisse.from(activity)
//                .choose(MimeType.ofImage())
//                .countable(true)
//                .maxSelectable(number)
//                .spanCount(4)
//                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//                .imageEngine(new GlideEngine())
//                .forResult(code == -1 ? MyConstants.TAKE_PICTURE_OR_ALBUM_REQUEST_CODE : code);
//    }
//
//    /**
//     * 调用相册上传图片界面.
//     * @param fragment 调用的界面.
//     * @param number   指定可选择数量.
//     */
//    public static void showTakePictureAndAlbumDialogCustomNumber(Fragment fragment, int number) {
//        Matisse.from(fragment)
//                .choose(MimeType.ofImage())
//                .countable(true)
//                .maxSelectable(number)
//                .spanCount(4)
//                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//                .imageEngine(new GlideEngine())
//                .forResult(MyConstants.TAKE_PICTURE_OR_ALBUM_REQUEST_CODE);
//    }

    /**
     * 压缩图片(不大于500KB).
     * @param image bitmap.
     * @return byte[]
     */
    public static byte[] compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        //循环判断如果压缩后图片是否大于500kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 500) {
            baos.reset();//重置baos即清空baos
            //这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            //每次都减少10
            options -= 10;
        }
//        //把压缩后的数据baos存放到ByteArrayInputStream中
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//        //把ByteArrayInputStream数据生成图片
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null)
        // 直接将bitmap转byte[]返回.
        return baos.toByteArray();
    }

    /** 百度地图包名. */
    public static final String PN_BAIDU_MAP = "com.baidu.BaiduMap";
    /** 高德地图包名. */
    public static final String PN_GAODE_MAP = "com.autonavi.minimap";
    /** 腾讯地图包名. */
    public static final String PN_TENCENT_MAP = "com.tencent.map";

    /**
     * 检测是否有安装百度地图.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean baiduMapIsInstalled(Context context) {
        return isAppInstalled(context, PN_BAIDU_MAP);
    }

    /**
     * 检测是否有安装高德地图.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean gaodeMapIsInstalled(Context context) {
        return isAppInstalled(context, PN_GAODE_MAP);
    }

    /**
     * 检测是否有安装腾讯地图.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean tencentMapIsInstalled(Context context) {
        return isAppInstalled(context, PN_TENCENT_MAP);
    }

    /**
     * Return whether the app is installed.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAppInstalled(Context context, @NonNull final String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getApplicationInfo(packageName, 0) != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getJson(Context context,String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    /**
     * 判断对象是否为空,如果为空则抛出异常.
     * @param object 目标对象.
     * @param message 异常消息.
     * @param <T> 目标对象(范型).
     * @return
     */
    public static <T> T requireNonNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    /**
     * 添加List到目标List.
     * @param dest 目标List.
     * @param source 源List.
     * @param <T> 返回类型.
     */
    public static <T> void listAddAllAvoidNPE(List<T> dest, List<T> source) {
        if (dest == null || source == null) {
            return;
        }
        dest.addAll(source);
    }

    /**
     * 添加List内对象到目标List.
     * @param dest 目标List.
     * @param source 源对象.
     * @param <T> 返回类型.
     */
    public static <T> void listAddAvoidNull(List<T> dest, T source) {
        if (source == null) {
            return;
        }
        dest.add(source);
    }

    /**
     * 获取固定格式的DecimalFormat避免语言切换后点变逗号问题.
     * @return DecimalFormat.
     */
    public static DecimalFormat getDecimalFormat(String format) {
        final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        final DecimalFormat df = new DecimalFormat(format);
        df.setDecimalFormatSymbols(dfs);
        return df;
    }

    /**
     * 检测多个权限中是否有未授权的权限.
     *
     * @param permissions 权限列表.
     * @return 有未授权权限: true; 全部已授权:false
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkMultilePermission(Context context, String[] permissions) {
//        MyLogUtils.i("checkMultilePermission() permissionSize = " + permissions.length);
        boolean haveRefuse = false;
        for (int i = 0; i < permissions.length; i++) {
            if (!checkMyPermission(context, permissions[i])) {
                haveRefuse = true;
                break;
            }
        }
        return haveRefuse;
    }

    /**
     * 判断单个权限是否有授权.
     *
     * @param permission 权限名称.
     * @return 没授权: false; 授权:true
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkMyPermission(Context context, String permission) {
//        MyLogUtils.i("checkMyPermission() permission = " + permission);
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 设置Activity背景透明度.
     * @param activity activity.
     * @param bgAlpha 背景透明度.
     */
    public static void setBackgroundAlpha(AppCompatActivity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        (activity).getWindow().setAttributes(lp);
    }

    /**
     * 调用系统邮箱发送邮件.
     * @param context 上下文.
     * @param address 邮箱地址.
     */
    public static void sendEmail(Context context, String address) {
        if (isEmptyString(address)) {
            return;
        }
        Intent mIntent = new Intent(Intent.ACTION_SENDTO);
        mIntent.setData(Uri.parse("mailto:" + address));
        context.startActivity(mIntent);
    }

    public static List<String> getPhotoData(String photos) {
        final List<String> data = new ArrayList<>();
        if (isEmptyString(photos)) {
            return data;
        }
        final String[] photoStrs = photos.split(",");
        for (final String photoStr : photoStrs) {
            listAddAvoidNull(data, photoStr);
        }
        return data;
    }

    /**
     * 根据Uri获取图片绝对路径
     * @param context context
     * @param uri     uri
     */
    public static String getFileAbsolutePath(Context context, Uri uri) {
        if (context == null || uri == null) {
            return null;
        }
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else if ("home".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/documents/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                if (isEmptyString(id)) {
                    return null;
                }
                if (id.startsWith("raw:")) {
                    return id.substring(4);
                }
                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"
                };
                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    try {
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix),
                                Long.valueOf(id));
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception ignore) {
                    }
                }
                try {
                    String path = getDataColumn(context, uri, null, null);
                    if (path != null) {
                        return path;
                    }
                } catch (Exception ignore) {
                }
                // path could not be retrieved using ContentResolver, therefore copy file to
                // accessible cache using streams
                return null;
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri;
                switch (type.toLowerCase(Locale.ENGLISH)) {
                    case "image":
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "video":
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "audio":
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        break;
                    default:
                        contentUri = MediaStore.Files.getContentUri("external");
                        break;
                }
                final String selection = MediaStore.MediaColumns._ID + "=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
            // File
            return uri.getPath();
        }
        return null;
    }

    /**
     * 通过游标获取当前文件路径
     * @param context       context
     * @param uri           uri
     * @param selection     selection
     * @param selectionArgs selectionArgs
     * @return 路径，未找到返回null
     */
    public static String getDataColumn(Context context, @NonNull Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs
                    , null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (Exception ignore) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 判断定位服务是否开启
     *
     * @param context 上下文
     * @return true：开启；false：未开启
     */
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static String hideMobile(String mobile) {
        //隐藏手机号码中间四位
        final String regexStr = "(\\d{3})\\d{4}(\\d{4})";
        return mobile.replaceAll(regexStr, "$1****$2");
    }

    /**
     * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     *
     * @param c
     * @return
     */
    public static long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    /**
     * 获取标签图片
     */
    public static List<Integer> getWaterMarker(Context context) {
        List<Integer> res = new ArrayList<>();

        for (int i = 1; i <= 24; i++) {
            res.add(context.getResources().getIdentifier("watermarker_" + i, "drawable",
                    context.getPackageName()));
        }
        return res;
    }

    /**
     * 获取标签图片
     */
    public static List<Integer> getLables(Context context) {
        List<Integer> res = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            res.add(context.getResources().getIdentifier("lable_" + i, "drawable",
                    context.getPackageName()));
        }
        return res;
    }

//    /**
//     * 识别图片二维码
//     * 若识别成功则返回二维码内容
//     */
//    public static String recoQRCode(String picturePath) {
//        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
//        hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true; // 先获取原大小
//        Bitmap scanBitmap;
//        options.inJustDecodeBounds = false; // 获取新的大小
//        int sampleSize = (int) (options.outHeight / (float) 200);
//        if (sampleSize <= 0) {
//            sampleSize = 1;
//        }
//        options.inSampleSize = sampleSize;
//        scanBitmap = BitmapFactory.decodeFile(picturePath, options);
//        LuminanceSource source1 = new PlanarYUVLuminanceSource(
//                rgb2YUV(scanBitmap), scanBitmap.getWidth(),
//                scanBitmap.getHeight(), 0, 0, scanBitmap.getWidth(),
//                scanBitmap.getHeight(), false);
//        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source1));
//        MultiFormatReader reader1 = new MultiFormatReader();
//        Result result1;
//        try {
//            result1 = reader1.decode(binaryBitmap);
//            String content = result1.getText();
//            if (Utils.isEmpty(content)) {
//                return null;
//            } else {
//                return content;
//            }
//        } catch (NotFoundException e1) {
//            e1.printStackTrace();
//            return null;
//        }
//    }

    private static byte[] rgb2YUV(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int len = width * height;
        byte[] yuv = new byte[len * 3 / 2];
        int y, u, v;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = pixels[i * width + j] & 0x00FFFFFF;
                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;
                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;
                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);
                yuv[i * width + j] = (byte) y;
            }
        }
        return yuv;
    }

    /**
     * 获取文件大小(字节byte)
     */
    public static long getFileLength(File file) {
        long fileLength = 0L;
        if (file.exists() && file.isFile()) {
            fileLength = file.length();
        }
        return fileLength;
    }

    /**
     * 文件是否超大
     * @param paths
     * @return
     */
    public static boolean isOverSize(List<String> paths) {
        long totalSize = 0;
        for (String path : paths) {
            long size = getFileLength(new File(path));
            totalSize += size;
        }
        double realSize = totalSize / (1024 * 1024.0);
        if (realSize > 10) {
            return true;
        }
        return false;
    }

    /**
     * 获取巡逻记录列表筛选map选择项
     * @param typesMap
     * @return
     */
    public static String getMapSelectKey(Map<String, Boolean> typesMap) {
        StringBuffer sb = new StringBuffer();
        for (final Map.Entry<String, Boolean> beanMap : typesMap.entrySet()) {
            if (beanMap.getValue()) {
                if (!CommonUtils.isEmptyString(sb.toString())) {
                    sb.append(",");
                }
                sb.append(beanMap.getKey());
            }
        }
        return sb.toString();
    }

    /**
     * @param fileName 文件名，需要包含后缀.xml类似这样的
     * @date :2020/3/17 0017
     * @author : gaoxiaoxiong
     * @description:根据文件后缀名获得对应的MIME类型
     **/
    public static String getFileMIMEType(String fileName) {
        String type = "*/*";
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fileName.substring(dotIndex, fileName.length()).toLowerCase();
        if (end == "") {
            return type;
        }
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < getFileMiMeTypeReal().length; i++) {
            //MIME_MapTable
            if (end.equals(getFileMiMeTypeReal()[i][0])) {
                type = getFileMiMeTypeReal()[i][1];
            }
        }
        return type;
    }

    /**
     * @date :2020/3/17 0017
     * @author : gaoxiaoxiong
     * @description:获取文件的mimetype类型
     **/
    public static String[][] getFileMiMeTypeReal() {
        String[][] MIME_MapTable = {
                //{后缀名，MIME类型}
                {".3gp", "video/3gpp"},
                {".apk", "application/vnd.android.package-archive"},
                {".asf", "video/x-ms-asf"},
                {".avi", "video/x-msvideo"},
                {".bin", "application/octet-stream"},
                {".bmp", "image/bmp"},
                {".c", "text/plain"},
                {".class", "application/octet-stream"},
                {".conf", "text/plain"},
                {".cpp", "text/plain"},
                {".doc", "application/msword"},
                {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml" +
                        ".document"},
                {".xls", "application/vnd.ms-excel"},
                {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
                {".exe", "application/octet-stream"},
                {".gif", "image/gif"},
                {".gtar", "application/x-gtar"},
                {".gz", "application/x-gzip"},
                {".h", "text/plain"},
                {".htm", "text/html"},
                {".html", "text/html"},
                {".jar", "application/java-archive"},
                {".java", "text/plain"},
                {".jpeg", "image/jpeg"},
                {".jpg", "image/jpeg"},
                {".js", "application/x-javascript"},
                {".log", "text/plain"},
                {".m3u", "audio/x-mpegurl"},
                {".m4a", "audio/mp4a-latm"},
                {".m4b", "audio/mp4a-latm"},
                {".m4p", "audio/mp4a-latm"},
                {".m4u", "video/vnd.mpegurl"},
                {".m4v", "video/x-m4v"},
                {".mov", "video/quicktime"},
                {".mp2", "audio/x-mpeg"},
                {".mp3", "audio/x-mpeg"},
                {".mp4", "video/mp4"},
                {".mpc", "application/vnd.mpohun.certificate"},
                {".mpe", "video/mpeg"},
                {".mpeg", "video/mpeg"},
                {".mpg", "video/mpeg"},
                {".mpg4", "video/mp4"},
                {".mpga", "audio/mpeg"},
                {".msg", "application/vnd.ms-outlook"},
                {".ogg", "audio/ogg"},
                {".pdf", "application/pdf"},
                {".png", "image/png"},
                {".pps", "application/vnd.ms-powerpoint"},
                {".ppt", "application/vnd.ms-powerpoint"},
                {".pptx", "application/vnd.openxmlformats-officedocument.presentationml" +
                        ".presentation"},
                {".prop", "text/plain"},
                {".rc", "text/plain"},
                {".rmvb", "audio/x-pn-realaudio"},
                {".rtf", "application/rtf"},
                {".sh", "text/plain"},
                {".tar", "application/x-tar"},
                {".tgz", "application/x-compressed"},
                {".txt", "text/plain"},
                {".wav", "audio/x-wav"},
                {".wma", "audio/x-ms-wma"},
                {".wmv", "audio/x-ms-wmv"},
                {".wps", "application/vnd.ms-works"},
                {".xml", "text/plain"},
                {".z", "application/x-compress"},
                {".zip", "application/x-zip-compressed"},
                {"", "*/*"}
        };
        return MIME_MapTable;
    }

    public static String getSinglePicRealPath(String path) {
        return !CommonUtils.isEmptyString(path) ? path.contains("http") ? path : path.contains(",") ?
                UrlConfig.BASE_IP + ":81" + CommonUtils.getPhotoData(path).get(0) : UrlConfig.BASE_IP + ":81" + path
                : "";
    }

    /**
     * 读取磁盘文件，转换成字节数组
     *
     * @param path 磁盘文件绝对路径
     * @return 字节数组
     */
    public static byte[] readFileToByteArray(String path) {
        try (InputStream is = new FileInputStream(path)) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, length);
                }
                return bos.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

}
