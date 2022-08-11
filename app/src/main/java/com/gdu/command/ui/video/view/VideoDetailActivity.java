package com.gdu.command.ui.video.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.ui.video.model.ChildrenBean;
import com.gdu.command.ui.video.player.GduMediaPlayer;
import com.gdu.command.ui.video.presenter.VideoDetailPresenter;
import com.gdu.command.views.GimbalControlView;
import com.gdu.command.views.GimbalPTZView;
import com.gdu.model.LightType;
import com.gdu.model.config.StorageConfig;
import com.gdu.model.device.DeviceInfo;
import com.gdu.model.device.PTZCmdType;
import com.gdu.model.device.PTZType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.easydarwin.video.EasyPlayerClient;

import java.nio.ByteBuffer;

/**
 * 视频详情界面
 */
public class VideoDetailActivity extends BaseActivity<VideoDetailPresenter> implements
        View.OnClickListener, IVideoDetailView {

    private final String KEY = "EasyPlayer is free!";
    private TextureView mEasySurface;
    private EasyPlayerClient mEasyPlayerClient;
    private GduMediaPlayer mGduMediaPlayer;
    private ImageView mBackImageView;
    private ImageView mLightChangeImageView;
    private ImageView mMapImageView;
    private ImageView mSavePictureImageView;
    private ImageView mGalleryImageView;
    private ImageView mVoiceImageView;
    private TextView mNameTextView;
    /**
     * 视频上点击事件
     */
    private ImageView mStartPauseImageView;
    private ImageView mPlayPauseImageView;
    private ImageView mPlaybackImageView;
    //    private LinearLayout mControlLayout;
    private RelativeLayout mLoadingLayout;
    private GimbalControlView mGimbalControlView;
    private GimbalPTZView mGimbalPTZView;
    private ChildrenBean mDeviceInfo;

    /**
     * 是否正在播放
     */
    private boolean isPlaying;
    /**
     * 是否静音
     */
    private boolean isMute;
//    /** 是否是竖屏 */
//    private boolean isPortrait;

    private StandardGSYVideoPlayer mGSYVideoPlayer;
//    private GSYExo2PlayerView mGSYVideoPlayer;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        super.onBaseCreate(savedInstanceState);
        isShowWaterMark = false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initialize() {
        MyLogUtils.i("initialize()");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setOrientationPortrait(false);
        getPresenter().setVideoDetailView(this);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        MyLogUtils.i("initData()");
        initPlayer();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
//        isPortrait = true;
        mDeviceInfo = (ChildrenBean)
                intent.getSerializableExtra(StorageConfig.DEVICE_INFO);
        final LightType lightType = (LightType) intent.getSerializableExtra(StorageConfig.DEVICE_LIGHT_TYPE);
        final String channelType = intent.getStringExtra(StorageConfig.DEVICE_CHANNEL_TYPE);
        if (mDeviceInfo != null) {
            mNameTextView.setText(mDeviceInfo.getLabel() + "-视频监控");
            getPresenter().setDeviceInfoNew(mDeviceInfo, lightType);
            if (CommonUtils.isEmptyString(channelType)) {
                getPresenter().setLightType(lightType);
                getPresenter().getVideoPath();
            } else {
                getPresenter().getVideoPathByChannel(channelType);
            }
        }
    }

    private void initPlayer() {
        MyLogUtils.i("initPlayer()");
        mGduMediaPlayer = new GduMediaPlayer(this, mGSYVideoPlayer);

        if (mEasySurface.getVisibility() == View.VISIBLE) {
//          mEasyPlayerClient = new EasyPlayerClient(this, KEY, mEasySurface, mEasyReceiver, mEasyDataCallback);
            mEasyPlayerClient = new EasyPlayerClient(this, KEY, mEasySurface, mEasyReceiver, null);
            getPresenter().setTextureView(mEasySurface);
        }
    }

    private void initListener() {
        MyLogUtils.i("initListener()");
        mBackImageView.setOnClickListener(this);
        mPlayPauseImageView.setOnClickListener(this);
        mStartPauseImageView.setOnClickListener(this);
        mLightChangeImageView.setOnClickListener(this);
        mMapImageView.setOnClickListener(this);
        mSavePictureImageView.setOnClickListener(this);
        mVoiceImageView.setOnClickListener(this);
        mGalleryImageView.setOnClickListener(this);
        mPlaybackImageView.setOnClickListener(this);
        mGduMediaPlayer.setOnGduMediaPlayerListener(new GduMediaPlayer.OnGduMediaPlayerListener() {
            @Override
            public void onTimeChanged(long currentTime) {
//                MyLogUtils.i("onTimeChanged() currentTime = " + currentTime);
            }

            @Override
            public void onEnd() {
                MyLogUtils.i("onEnd()");
            }

            @Override
            public void onStart() {
                MyLogUtils.i("onStart()");
                isPlaying = true;
                if (mLoadingLayout.getVisibility() == View.VISIBLE) {
                    mLoadingLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPaused() {
                MyLogUtils.i("onPaused()");
            }

            @Override
            public void onStopped() {
                MyLogUtils.i("onStopped()");
            }

            @Override
            public void onPicSaved() {
                MyLogUtils.i("onPicSaved()");
                showToast("图片保存成功");
            }
        });
        mGimbalControlView.setOnGimbalControlListener(new GimbalControlView.OnGimbalControlListener() {
            @Override
            public void onGimbalMove(float pitch, float direct) {
                MyLogUtils.d("onGimbalMove() pitch = " + pitch + "; direct = " + direct
                        + "; cTime " + System.currentTimeMillis());
//                getPresenter().gimbalMove(pitch, direct);
            }

            @Override
            public void onGimbalMoveNew(PTZCmdType cmdType, boolean isAutoStop) {
                getPresenter().gimbalSingleMove(cmdType, isAutoStop);
            }

            @Override
            public void onGimbalUp() {
                MyLogUtils.i("onGimbalUp()");
                getPresenter().gimbalUp();
            }

            @Override
            public void onZoomClick() {
                MyLogUtils.i("onZoomClick()");
                mGimbalPTZView.setType(PTZType.ZOOM);
            }

            @Override
            public void onFocusClick() {
                MyLogUtils.i("onFocusClick()");
                mGimbalPTZView.setType(PTZType.FOCUS);
            }

            @Override
            public void onIrisClick() {
                MyLogUtils.i("onIrisClick()");
                mGimbalPTZView.setType(PTZType.IRIS);
            }

            @Override
            public void onSpeedClick() {
                MyLogUtils.i("onSpeedClick()");
                mGimbalPTZView.setType(PTZType.SPEED);
            }

            @Override
            public void changeStreamFormat(int type) {
                MyLogUtils.i("changeStreamFormat() type = " + type);
                getPresenter().changeStreamType(type);
            }
        });
        mGimbalPTZView.setOnGimbalPTZListener(new GimbalPTZView.OnGimbalPTZListener() {

            @Override
            public void onMinusClick(PTZCmdType cmdType) {
                MyLogUtils.i("onMinusClick() cmdType = " + cmdType);
//                getPresenter().ptzControl(cmdType);
                handlerPTZControl(cmdType);
            }

            @Override
            public void onPlusClick(PTZCmdType cmdType) {
                MyLogUtils.i("onPlusClick() cmdType = " + cmdType);
//                getPresenter().ptzControl(cmdType);
                handlerPTZControl(cmdType);
            }

            @Override
            public void onSpeedChange(int speed) {
                MyLogUtils.i("onSpeedChange() speed = " + speed);
                getPresenter().setStep(speed);
            }
        });
    }

    private void handlerPTZControl(PTZCmdType cmdType) {
        MyLogUtils.i("handlerPTZControl() cmdType = " + cmdType);
        getPresenter().startPTZControl(cmdType);
        if (cmdType == PTZCmdType.FIIrisIn || cmdType == PTZCmdType.FIIrisOut) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getPresenter().stopPTZControl(cmdType);
                }
            }, 100);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getPresenter().stopPTZControl(cmdType);
                }
            }, 200);
        }
    }

    private void initView() {
        MyLogUtils.i("initView()");
        mBackImageView = findViewById(R.id.back_imageview);

        mLightChangeImageView = findViewById(R.id.light_change_imageview);
        mMapImageView = findViewById(R.id.map_imageview);
        mSavePictureImageView = findViewById(R.id.photo_imageview);
        mGalleryImageView = findViewById(R.id.gallery_imageview);
        mVoiceImageView = findViewById(R.id.voice_imageview);
        mPlaybackImageView = findViewById(R.id.video_imageview);

        mGSYVideoPlayer = findViewById(R.id.video_player);
        //禁止滑动屏幕快进快退等操作
        mGSYVideoPlayer.setIsTouchWiget(false);

        mNameTextView = findViewById(R.id.device_name_textview);
        mEasySurface = (TextureView) findViewById(R.id.tv_surface);

        mLoadingLayout = findViewById(R.id.loading_layout);
        mStartPauseImageView = findViewById(R.id.start_pause_imageview);
        mPlayPauseImageView = findViewById(R.id.play_pause_imageview);
//        mControlLayout = findViewById(R.id.video_operate_layout);

        mGimbalControlView = findViewById(R.id.gimbal_control_view);
        mGimbalPTZView = findViewById(R.id.gimbal_ptz_view);

//        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mGduMediaPlayer != null) {
            mGduMediaPlayer.onResume();
        }
        if (mEasyPlayerClient != null) {
            mEasyPlayerClient.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGduMediaPlayer != null) {
            mGduMediaPlayer.onPause();
        }
        if (mEasyPlayerClient != null) {
            mEasyPlayerClient.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGduMediaPlayer != null) {
            mGduMediaPlayer.onDestroy();
            mGduMediaPlayer = null;
        }
        if (mEasyPlayerClient != null) {
            mEasyPlayerClient.stop();
            mEasyPlayerClient = null;
        }
    }

    /**
     * 开始播放
     */
    private void startPlay() {
        MyLogUtils.d("startPlay() cTime = " + System.currentTimeMillis());
//        mLoadingLayout.setVisibility(View.VISIBLE);
        isPlaying = true;
    }


    @Override
    public void onClick(View view) {
        MyLogUtils.v("onClick() clickTime = " + System.currentTimeMillis());
        switch (view.getId()) {
            case R.id.back_imageview:
                finish();
                break;
            case R.id.start_pause_imageview:
            case R.id.play_pause_imageview:
                if (isPlaying) {
                    isPlaying = false;
//                    mStartPauseImageView.setVisibility(View.VISIBLE);
                    if (mGduMediaPlayer != null) {
                        mGduMediaPlayer.onPause();
                    }
                    if (mEasyPlayerClient != null) {
                        mEasyPlayerClient.pause();
                    }
                } else {
//                    mStartPauseImageView.setVisibility(View.GONE);
                    if (mGduMediaPlayer != null) {
                        mGduMediaPlayer.onResume();
                    }
                    if (mEasyPlayerClient != null) {
                        mEasyPlayerClient.resume();
                    }
                }
                break;
            case R.id.photo_imageview:
                if (mEasyPlayerClient != null) {
                    getPresenter().savePicTranscript();
                }
                mGduMediaPlayer.savePicTranscript();
                break;
            case R.id.video_imageview:
                Intent playbackIntent = new Intent(mContext, PlaybackActivity.class);
                playbackIntent.putExtra(StorageConfig.DEVICE_INFO, mDeviceInfo);
                playbackIntent.putExtra(StorageConfig.DEVICE_LIGHT_TYPE, getPresenter().getCurrentLightType());
                startActivity(playbackIntent);
                break;
            case R.id.light_change_imageview:
                getPresenter().changeLightType();
                break;
            case R.id.map_imageview:
                DeviceInfo deviceInfo = new DeviceInfo();
                if (!TextUtils.isEmpty(mDeviceInfo.getLat())) {
                    deviceInfo.setDeviceLatitude(Double.parseDouble(mDeviceInfo.getLat()));
                } else {
                    deviceInfo.setDeviceLatitude(0);
                }
                if (!TextUtils.isEmpty(mDeviceInfo.getLon())) {
                    deviceInfo.setDeviceLongitude(Double.parseDouble(mDeviceInfo.getLon()));
                } else {
                    deviceInfo.setDeviceLongitude(0);
                }

                deviceInfo.setDeviceTypeCode(mDeviceInfo.getDeviceType());
                deviceInfo.setDeviceCode(mDeviceInfo.getDeviceId());
                deviceInfo.setDeviceBrand(mDeviceInfo.getDeviceBrand());
                deviceInfo.setDeviceAddress(mDeviceInfo.getAddress());
                deviceInfo.setDeviceName(mDeviceInfo.getName());
                Intent intent = new Intent(mContext, DevicePositionActivity.class);
                intent.putExtra(StorageConfig.DEVICE_INFO, deviceInfo);
                startActivity(intent);
                break;
            case R.id.gallery_imageview:
                Intent galleryIntent = new Intent(mContext, PhotoAlbumActivity.class);
                galleryIntent.putExtra(StorageConfig.DEVICE_INFO, mDeviceInfo);
                startActivity(galleryIntent);
                break;
            case R.id.voice_imageview:
                if (isMute) {
                    isMute = false;
//                    mMediaPlayer.setVolume(100);
                } else {
                    isMute = true;
//                    mMediaPlayer.setVolume(1);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void startMedia(String mediaPath) {
        MyLogUtils.i("startMedia() cTime = " + System.currentTimeMillis());
        if (mGSYVideoPlayer.getVisibility() == View.VISIBLE) {
            mGduMediaPlayer.createPlayer(mediaPath);
        }
        startPlay();
        if (mEasySurface.getVisibility() == View.VISIBLE) {
            if (mEasyPlayerClient != null) {
                mEasyPlayerClient.stop();
                mEasyPlayerClient.play(mediaPath);
            }
        }
    }

    @Override
    public void setMediaPath(String mediaPath) {
        MyLogUtils.i("setMediaPath() mediaPath = " + mediaPath);
        startMedia(mediaPath);
    }

    @Override
    public void showToast(String toast) {
        if (mLoadingLayout.getVisibility() == View.VISIBLE) {
            mLoadingLayout.setVisibility(View.GONE);
        }
        ToastUtil.s(toast);
    }

    @Override
    public void showOrHideStreamFormatBtn(boolean isShow) {
        MyLogUtils.i("showOrHideStreamFormatBtn() isShow = " + isShow);
        runOnUiThread(() -> {
            mGimbalControlView.showOrHideStreamFormatBtn(isShow);
        });
    }

    private ResultReceiver mEasyReceiver = new ResultReceiver(new Handler()) {
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
//            MyLogUtils.i("mEasyReceiver onReceiveResult() resultCode = " + resultCode);
            super.onReceiveResult(resultCode, resultData);
        }
    };

    private EasyPlayerClient.I420DataCallback mEasyDataCallback = new EasyPlayerClient.I420DataCallback() {

        @Override
        public void onI420Data(ByteBuffer buffer) {
            MyLogUtils.i("mEasyDataCallback onI420Data()");

        }

        @Override
        public void onPcmData(byte[] pcm) {
            MyLogUtils.i("mEasyDataCallback onPcmData()");
        }
    };
}
