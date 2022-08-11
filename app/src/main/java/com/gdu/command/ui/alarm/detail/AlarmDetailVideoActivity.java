package com.gdu.command.ui.alarm.detail;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.downloadfile.DownloadPresenter;
import com.gdu.command.downloadfile.MyDownloadListener;
import com.gdu.command.ui.exo.GSYExo2PlayerView;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.shuyu.gsyvideoplayer.utils.FileUtils;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.io.File;

/**
 * 告警详情抓拍视频播放界面
 */
public class AlarmDetailVideoActivity extends BaseActivity<BasePresenter>
        implements View.OnClickListener {

    private ImageView backBtnIv;
    private ImageView downloadBtnIv;
    private ImageView shareBtnIv;
    private GSYExo2PlayerView mVideoView;
    private OrientationUtils orientationUtils;

    private String downloadPath = FileUtils.getPath();

    private DownloadPresenter mDownloadPresenter;

    private int tempProgress = 0;

    private String videoUrl = "";

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        super.onBaseCreate(savedInstanceState);
        isShowWaterMark = false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_video_player;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true)
                .statusBarColor(R.color.color_224CD0).init();
        mDownloadPresenter = new DownloadPresenter(this, this);
        downloadPath += "AlarmVideo";
        File downloadFolderFile = new File(downloadPath);
        if (!downloadFolderFile.exists()) {
            downloadFolderFile.mkdir();
        }
        initData();
        initView();
        initPlayer();
        initListener();
    }

    private void initData() {
        final Bundle mBundle = getIntent().getExtras();
        if(mBundle != null) {
            videoUrl = mBundle.getString(MyConstants.DEFAULT_PARAM_KEY_1);
        }
    }

    private void initView() {
        MyLogUtils.d("initView()");
        mVideoView = findViewById(R.id.view_videoPlayer);
        downloadBtnIv = findViewById(R.id.iv_downloadBtn);
        shareBtnIv = findViewById(R.id.iv_shareBtn);
        backBtnIv = findViewById(R.id.iv_backBtn);

        downloadBtnIv.setVisibility(View.VISIBLE);
        shareBtnIv.setVisibility(View.VISIBLE);
    }

    private void initPlayer() {
        MyLogUtils.d("initPlayer() videoUrl = " + videoUrl);
        if (CommonUtils.isEmptyString(videoUrl)) {
            ToastUtil.s("未获取到播放地址");
            finish();
            return;
        }
        mVideoView.setUp(new GSYVideoModel(videoUrl, ""));
        //增加title
        mVideoView.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        mVideoView.getBackButton().setVisibility(View.GONE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, mVideoView);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        mVideoView.getFullscreenButton().setOnClickListener(v -> orientationUtils.resolveByClick());
        //是否可以滑动调整
        mVideoView.setIsTouchWiget(true);
        mVideoView.startPlayLogic();
    }

    private void initListener() {
        backBtnIv.setOnClickListener(this);
        downloadBtnIv.setOnClickListener(this);
        shareBtnIv.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.onVideoResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.onVideoPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
        }
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mVideoView.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        mVideoView.setVideoAllCallBack(null);
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.iv_backBtn:
                if (mVideoView != null && mVideoView.isInPlayingState()) {
                    mVideoView.onVideoPause();
                }
                finish();
                break;

            case R.id.iv_downloadBtn:
                if (mVideoView != null && mVideoView.isInPlayingState()) {
                    mVideoView.onVideoPause();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    XXPermissions.with(this)
                            .permission(Permission.WRITE_EXTERNAL_STORAGE)
                            .permission(Permission.READ_EXTERNAL_STORAGE)
                            .request((permissions, all) -> {
                                if (all) {
                                    downloadHandle();
                                }
                            });
                } else {
                    downloadHandle();
                }
                break;

            case R.id.iv_shareBtn:
                if (mVideoView != null && mVideoView.isInPlayingState()) {
                    mVideoView.onVideoPause();
                }
                final String fileName = videoUrl.substring(videoUrl.lastIndexOf("/") + 1);
//                final String filePath = downloadPath + fileName;
                openFileThirdApp(this, fileName);
                break;

            default:
                break;
        }
    }

    /**
     * 调用系统应用打开文件（系统分享）
     *
     * @param context
     * @param fileName 文件路径
     */
    public void openFileThirdApp(Context context, String fileName) {
        MyLogUtils.d("openFileThirdApp() fileName = " + fileName);
        final Uri fileUri = queryCursorFile(fileName);
        if (fileUri == null) {
            ToastUtil.s("为找到分享文件,请先下载文件");
            return;
        }

        checkFileUriExposure();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        //分享文件类型
        intent.setType("video/*");
        context.startActivity(Intent.createChooser(intent, "分享"));
    }

    private Uri queryCursorFile(String fileName) {
        MyLogUtils.d("queryCursorFile() fileName = " + fileName);
        final String[] projection = new String[] {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME
        };
        final String selection = MediaStore.Video.Media.DISPLAY_NAME +
                " LIKE ?";
        final String[] selectionArgs = new String[] {"%" + fileName};

        Cursor mCursor = getApplicationContext().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
        Uri fileUri;
        try {
            if (mCursor == null) {
                return null;
            }

            // Cache column indices.
            int idColumn = mCursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumn = mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);

            fileUri = null;
            while (mCursor.moveToNext()) {
                // Get values of columns for a given video.
                final long id = mCursor.getLong(idColumn);
                final String name = mCursor.getString(nameColumn);
                fileUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                MyLogUtils.d("queryCursorFile() uri = " + fileUri + "; name = " + name);
            }
        } finally {
            if (mCursor != null) {
                mCursor.close();
            }
        }
        return fileUri;
    }

    /**
     * 分享前必须执行本代码，主要用于兼容SDK18以上的系统
     * 否则会报android.os.FileUriExposedException: file:///xxx.pdf exposed beyond app through ClipData.Item.getUri()
     */
    private void checkFileUriExposure() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }

    private void downloadHandle() {
        MyLogUtils.d("downloadHandle()");
        final String fileName = videoUrl.substring(videoUrl.lastIndexOf("/") + 1);
        final Uri fileUri = queryCursorFile(fileName);
        if (fileUri != null) {
            ToastUtil.s("文件已存在,请勿重复下载");
            return;
        }
        showProgressDialog();
        mDownloadPresenter.downloadVideo(videoUrl, downloadPath,
                mMyDownloadListener);
    }

    /**
     * 下载文件的监听
     */
    private MyDownloadListener mMyDownloadListener = new MyDownloadListener() {
        @Override
        public void onStart() {
            MyLogUtils.d("onStart()");
        }

        @Override
        public void onProgress(int progress) {
            if (tempProgress == progress) {
                return;
            } else {
                tempProgress = progress;
            }
            MyLogUtils.d("onProgress() progress = " + tempProgress);
        }

        @Override
        public void onFinish(String path) {
            hideProgressDialog();
            tempProgress = 0;
            MyLogUtils.d("onFinish() path = " + path);
            runOnUiThread(() -> {
                ToastUtil.s("文件下载成功");
            });
        }

        @Override
        public void onFail(String errorInfo) {
            hideProgressDialog();
            tempProgress = 0;
            MyLogUtils.d("onFail() errorInfo = " + errorInfo);
            runOnUiThread(() -> {
                ToastUtil.s("文件下载失败");
            });
        }
    };
}
