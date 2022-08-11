package com.gdu.command.ui.cases.navigate;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.model.LatLng;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.BuildConfig;
import com.gdu.command.R;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.utils.MyUiUtils;
import com.gdu.utils.MyVariables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 底部弹出的多选弹窗.
 *
 * @author wixche
 */
public class BottomSelectDialogActivity extends Activity {

    public static final String PIC_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/gduCommand";
    private static final int REQUEST_PERMISSION_CAMERA = 0x001;
    private static final int REQUEST_PERMISSION_WRITE = 0x002;
    private static final int CROP_REQUEST_CODE = 0x003;

    View placeHolderView;
    TextView cancelBtnTV;
    TextView mapAddressContentTV;

    private RecyclerView selectItemContentRV;
    private DialogItemAdapter mAdapter;
    private File captureFile;
    private File rootFile;
    private File cropFile;

    private String mapAddressContent = null;
    private LatLng mapCoordinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_bottom_item_select);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        init();
    }

    private void init() {
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mapAddressContent = bundle.getString(MyConstants.MAP_ADDRESS_CONTENT);
            mapCoordinate = bundle.getParcelable(MyConstants.MAP_COORDINATE);
        }

        placeHolderView = findViewById(R.id.view_bottom_dialog_placeHolder);
        cancelBtnTV = findViewById(R.id.tv_bottom_dialog_cancelBtn);
        mapAddressContentTV = findViewById(R.id.tv_bottom_dialog_addressContent);

        selectItemContentRV = findViewById(R.id.rv_bottom_dialog_content);

        mAdapter = new DialogItemAdapter(MyVariables.SELECTITEMS);
        mAdapter.setOnItemClickListener(dialogItemClickListener);
        selectItemContentRV.setAdapter(mAdapter);
        selectItemContentRV.setLayoutManager(new LinearLayoutManager(this));
        final DividerItemDecoration itemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(MyUiUtils.getDrawable(R.drawable.bg_rv_gray_divider_1px));
        selectItemContentRV.addItemDecoration(itemDecoration);

        cancelBtnTV.setText("取消");
        cancelBtnTV.setOnClickListener(v -> finish());

//        if (!CommonUtils.isEmptyString(mapAddressContent)) {
//            mapAddressContentTV.setText(mapAddressContent);
//            mapAddressContentTV.setVisibility(View.VISIBLE);
//        } else {
            mapAddressContentTV.setVisibility(View.GONE);
//        }
        placeHolderView.setOnClickListener(v -> finish());
    }

    /**
     * 判断权限是否没有授权.
     *
     * @param permission 权限名称.
     * @return 没授权: true; 授权:false
     */
    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermission(String permission) {
        MyLogUtils.i("checkPermission() permission = " + permission);
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        MyLogUtils.i("onRequestPermissionsResult() requestCode = " + requestCode);
        switch (requestCode) {

            case REQUEST_PERMISSION_CAMERA: {
                boolean isAllPass = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        isAllPass = false;
                        break;
                    }
                }
                if (isAllPass) {
                    takePhoto();
                } else {
                    Toast.makeText(this, "相机授权失败请进入设置界面开启权限！", Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case REQUEST_PERMISSION_WRITE: {
                boolean isAllPass = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        isAllPass = false;
                        break;
                    }
                }
                if (isAllPass) {
                    choosePhoto();
                } else {
                    Toast.makeText(this, "相机授权失败请进入设置界面开启权限！", Toast.LENGTH_SHORT).show();
                }
            }
            break;

            default:
                break;
        }
    }

    /**
     * 弹窗item点击处理.
     */
    private OnItemClickListener dialogItemClickListener =
            (adapter, view, position) -> {
                try {
                    itemClickHandler(position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

    private void itemClickHandler(int position) {
        final String baiduMapStr = "百度地图";
        final String gaodeMapStr = "高德地图";
        final String tencentMapStr = "腾讯地图";
        final String viewStr = MyVariables.SELECTITEMS.get(position);

        Intent navigationIntent;
        Uri navigationUri;

        if (viewStr.equals(baiduMapStr)) {
            // 百度地图
            navigationUri = Uri.parse("baidumap://map/geocoder?" +
                    "location=" + mapCoordinate.latitude + "," + mapCoordinate.longitude +
                    "&name=" + mapAddressContent + //终点的显示名称
                    "&coord_type=gcj02");//坐标 （百度同样支持他自己的db0911的坐标，但是高德和腾讯不支持）
            navigationIntent = new Intent(Intent.ACTION_VIEW);
            navigationIntent.setPackage(CommonUtils.PN_BAIDU_MAP);
            navigationIntent.setData(navigationUri);
            startActivity(navigationIntent);
            finish();
        } else if (viewStr.equals(gaodeMapStr)) {
            // 高德地图
            navigationUri = Uri.parse("androidamap://route?sourceApplication={"
                    + MyUiUtils.getString(R.string.app_name)
                    + "}&dlat=" + mapCoordinate.latitude//终点的纬度
                    + "&dlon=" + mapCoordinate.longitude//终点的经度
                    + "&dname=" + mapAddressContent////终点的显示名称
                    + "&dev=0&m=0&t=0");
            navigationIntent = new Intent(Intent.ACTION_VIEW);
            navigationIntent.setPackage(CommonUtils.PN_GAODE_MAP);
            navigationIntent.setData(navigationUri);
            startActivity(navigationIntent);
            finish();
        } else if (viewStr.equals(tencentMapStr)) {
            // 腾讯地图
            navigationUri = Uri.parse("qqmap://map/routeplan?type=drive"
                    + "&to=" + mapAddressContent//终点的显示名称 必要参数
                    + "&tocoord=" + mapCoordinate.latitude
                    + "," + mapCoordinate.longitude//终点的经纬度
                    + "&referer={" + MyUiUtils.getString(R.string.app_name) + "}");
            navigationIntent = new Intent(Intent.ACTION_VIEW);
            navigationIntent.setPackage(CommonUtils.PN_TENCENT_MAP);
            navigationIntent.setData(navigationUri);
            startActivity(navigationIntent);
            finish();
        } else {
            Intent intent = new Intent();
            intent.putExtra(MyConstants.CLICK_ITEM_NAME, viewStr);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * 将URI转为图片的路径.
     *
     * @param context 上下文.
     * @param uri     uri.
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        try {
            //用于保存调用相机拍照后所生成的文件
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return;
            }
            final String curTimeStr = String.valueOf(System.currentTimeMillis());
            captureFile = new File(rootFile, "le_cap_" + curTimeStr + ".jpg");
            if (!captureFile.exists()) {
                try {
                    final boolean isSucess = captureFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //跳转到调用系统相机
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //判断版本 如果在Android7.0以上,使用FileProvider获取Uri
            Uri uri = getUriFormFile(this, captureFile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            } else {
                //否则使用Uri.fromFile(file)方法获取Uri
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
            if (hasCamera()) {
                startActivityForResult(intent, REQUEST_PERMISSION_CAMERA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取共享的后转化的Uri.
     * @param context 上下文.
     * @param file 共享文件.
     * @return 转化后的Uri.
     */
    public static Uri getUriFormFile(Context context, File file) {
        Uri convertUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            convertUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
        } else {
            convertUri = Uri.fromFile(file);
        }
        return convertUri;
    }

    /**
     * 判断系统中是否存在可以启动的相机应用
     *
     * @return 存在返回true，不存在返回false
     */
    public boolean hasCamera() {
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 从相册选择
     */
    private void choosePhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_PERMISSION_WRITE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case REQUEST_PERMISSION_CAMERA:
                    Uri uri = getUriFormFile(this, captureFile);
                    final String captureFilePathStr = getRealFilePath(this, uri);
                    callbackPhotoFilePath(captureFilePathStr);
//                    cropPhoto(uri);
                    break;

                case REQUEST_PERMISSION_WRITE:
                    final String albumFilePathStr = getRealFilePath(this, data.getData());
                    callbackPhotoFilePath(albumFilePathStr);
//                    cropPhoto(data.getData());
                    break;

                case CROP_REQUEST_CODE:
                    final String cropFilePathStr = cropFile.getAbsolutePath();
                    saveImage(cropFilePathStr);
                    callbackPhotoFilePath(cropFilePathStr);
                    break;

                default:
                    break;
            }
        } else {
            // 对应没有选择图片或拍照返回对情况处理.
            finish();
        }
    }

    private void callbackPhotoFilePath(String cropFilePathStr) {
        Intent intent = new Intent();
        intent.putExtra(MyConstants.PHOTO_FILE_PATH, cropFilePathStr);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        final String curTimeStr = String.valueOf(System.currentTimeMillis());
        cropFile = new File(rootFile, "le_crop_" + curTimeStr + ".jpg");
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    /**
     * 保存拍摄的照片.
     *
     * @param path 保存路径.
     * @return
     */
    public String saveImage(String path) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        try {
            FileOutputStream fos = new FileOutputStream(cropFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return cropFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyVariables.SELECTITEMS.clear();
    }
}
