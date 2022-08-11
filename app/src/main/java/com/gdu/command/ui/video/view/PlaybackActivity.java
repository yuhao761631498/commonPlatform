package com.gdu.command.ui.video.view;

import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.ui.video.model.ChildrenBean;
import com.gdu.command.ui.video.player.GduMediaPlayer;
import com.gdu.command.ui.video.presenter.PlaybackPresenter;
import com.gdu.command.views.PickerScrollView;
import com.gdu.model.LightType;
import com.gdu.model.config.StorageConfig;
import com.gdu.util.TimeUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 录像回放
 */
public class PlaybackActivity extends BaseActivity<PlaybackPresenter> implements View.OnClickListener, IPlaybackView {

    /** 选择倍数 */
    private final int SELECT_RATE = 1;
    /** 选择日期 */
    private final int SELECT_DATE = 2;

    private ImageView mBackImageView;
    private ImageView mSavePictureImageView;
    private ImageView mPlayImageView;
    private TextView mDateTextView;
    private TextView mRateButton;
    private Button mCancelDateButton;
    private Button mConfirmDateButton;
    private TextureView mSurface;
//    private GduMediaPlayer mGduMediaPlayer;
    private SeekBar mTimeSeekBar;
    private TextView playTimeTv, totalTimeTv;
    private DatePicker mDatePicker;
    private PickerScrollView mRatePickerView;
    private LinearLayout mSelectLayout;

    private ImageView mVideoBackImageView;
    private ImageView mVideoFrontImageView;
    private ImageView mVideoRecordImageView;

    private GduMediaPlayer mGduMediaPlayer;
    private StandardGSYVideoPlayer videoPlayer;
//    private GSYExo2PlayerView videoPlayer;

    private int mSelectYear;
    private int mSelectMonth;
    private int mSelectDay;
    private String mCurrentRate;
    private int mCurrentSelectType;
    private boolean isPlaying;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        super.onBaseCreate(savedInstanceState);
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true)
                .statusBarColor(R.color.black).init();
        isShowWaterMark = false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_playback;
    }

    @Override
    protected void initialize() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getPresenter().init(this, this);
        mBackImageView = findViewById(R.id.back_imageview);
        mDateTextView = findViewById(R.id.date_textview);
        mSavePictureImageView = findViewById(R.id.photo_imageview);
        mVideoBackImageView = findViewById(R.id.play_back_imageview);
        mPlayImageView = findViewById(R.id.play_pause_imageview);
        mVideoFrontImageView = findViewById(R.id.play_front_imageview);
        mVideoRecordImageView = findViewById(R.id.video_imageview);
        mSurface = (TextureView) findViewById(R.id.tv_surface);
        mTimeSeekBar = findViewById(R.id.time_seekbar);
        mDatePicker = findViewById(R.id.date_picker);
        mSelectLayout = findViewById(R.id.select_layout);
        mRatePickerView = findViewById(R.id.rate_picker_view);

        mRateButton = findViewById(R.id.rate_button);
        mCancelDateButton = findViewById(R.id.cancel_date_button);
        mConfirmDateButton = findViewById(R.id.confirm_date_button);

        videoPlayer = findViewById(R.id.video_player);

        playTimeTv = findViewById(R.id.tv_playTime);
        totalTimeTv = findViewById(R.id.tv_totalTime);

        initTimeSeekBar();
        initPlayer();
        initListener();
        initData();
//       mRulerView.getSelectEndTime();
    }

    private void initTimeSeekBar() {
        MyLogUtils.d("initTimeSeekBar()");
        mTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                getPresenter().changeTime(seekBar.getProgress());
            }
        });
    }

    private void initData() {
        MyLogUtils.d("initData()");
        if (getIntent() == null) {
            return;
        }
        ChildrenBean deviceInfo =
                (ChildrenBean)
                        getIntent().getSerializableExtra(StorageConfig.DEVICE_INFO);
        final LightType mLightType = (LightType) getIntent().getSerializableExtra(StorageConfig.DEVICE_LIGHT_TYPE);
        final int channelType = mLightType == LightType.VISIBLE_LIGHT ? 1 : 2;
        if (deviceInfo != null && deviceInfo.getChildren() != null && deviceInfo.getChildren().size() > 0) {
            String devCodeStr = "";
            for (final ChildrenBean mChild :
                    deviceInfo.getChildren()) {
                if (channelType == mChild.getChannelType()) {
                    devCodeStr = mChild.getDeviceId();
                    break;
                }
            }
            if (!CommonUtils.isEmptyString(devCodeStr)) {
                getPresenter().setStream(devCodeStr);
            }
        }
        int[] dates = TimeUtils.getDateByTimeStamp(System.currentTimeMillis());
        mSelectYear = dates[0];
        mSelectMonth = dates[1] + 1;
        mSelectDay = dates[2];
        setTime();
        List<String> rates = new ArrayList<>();
        float baseRate = 0.25f;
        for (int i = 0; i < 15; i++) {
            rates.add((baseRate) + "");
            baseRate += 0.25f;
        }
        mRatePickerView.setData(rates);
        mRatePickerView.setSelected(3);
    }

    private void setTime(){
        MyLogUtils.d("setTime()");
        mDateTextView.setText(mSelectYear + "年" + mSelectMonth + "月" + mSelectDay + "日");
        getPresenter().setDate(mSelectYear, mSelectMonth,mSelectDay);
    }

    private void initListener() {
        mBackImageView.setOnClickListener(this);
        mSavePictureImageView.setOnClickListener(this);

        mPlayImageView.setOnClickListener(this);
        mVideoBackImageView.setOnClickListener(this);
        mVideoFrontImageView.setOnClickListener(this);

        mDateTextView.setOnClickListener(this);
        mCancelDateButton.setOnClickListener(this);
        mConfirmDateButton.setOnClickListener(this);
        mRateButton.setOnClickListener(this);

        mVideoBackImageView.setOnClickListener(this);
        mVideoFrontImageView.setOnClickListener(this);
        mVideoRecordImageView.setOnClickListener(this);

        // 从日历类中获取默认时间(即当前时间)
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        // 按照外国习惯,月份是从0 开始的
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker.init(year, month, day, (datePicker, year1, monthOfYear, dayOfMonth) -> {
            mSelectYear = year1;
            mSelectMonth = monthOfYear + 1;
            mSelectDay = dayOfMonth;
        });

        mRatePickerView.setOnSelectListener(pickers -> mCurrentRate = pickers);
    }

    private void initPlayer() {
        mGduMediaPlayer = new GduMediaPlayer(this, videoPlayer, true);
        mGduMediaPlayer.setIsTouchWiget(false);
        getPresenter().setGduMediaPlayer(mGduMediaPlayer, mSurface);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGduMediaPlayer != null) {
            mGduMediaPlayer.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGduMediaPlayer != null) {
            mGduMediaPlayer.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGduMediaPlayer != null) {
            mGduMediaPlayer.onDestroy();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_imageview:
//                mGduMediaPlayer.setRate(3);
                finish();
                break;
            case R.id.play_pause_imageview:
                if (isPlaying) {
                    isPlaying = false;
                    mGduMediaPlayer.onPause();
                    mPlayImageView.setImageResource(R.mipmap.icon_record_play);
                } else {
                    isPlaying = true;
                    mGduMediaPlayer.onResume();
                    mPlayImageView.setImageResource(R.mipmap.icon_back_play_pause);
                }
                break;
            case R.id.date_textview:
                mCurrentSelectType = SELECT_DATE;
                mDatePicker.setVisibility(View.VISIBLE);
                mRatePickerView.setVisibility(View.GONE);
                mSelectLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.cancel_date_button:
                mSelectLayout.setVisibility(View.GONE);
                break;
            case R.id.confirm_date_button:
                mSelectLayout.setVisibility(View.GONE);
                if (mCurrentSelectType == SELECT_DATE) {
                    setTime();
                } else {
                    float rate = Float.parseFloat(mCurrentRate);
                    mGduMediaPlayer.setRate(rate);
                }
                break;
            case R.id.rate_button:
                mCurrentSelectType = SELECT_RATE;
                mDatePicker.setVisibility(View.GONE);
                mRatePickerView.setVisibility(View.VISIBLE);
                mSelectLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.play_back_imageview:
                mGduMediaPlayer.rollBack();
                break;
            case R.id.play_front_imageview:
                mGduMediaPlayer.forward();
                break;
            case R.id.photo_imageview:
//                showProgressDialog();
//                getPresenter().savePicTranscript();
                mGduMediaPlayer.savePicTranscript();
                break;
            case R.id.video_imageview:
                getPresenter().startRecord();
                break;
            default:
                break;
        }
    }

    @Override
    public void setPlayerPath(String path) {
        MyLogUtils.d("setPlayerPath() path = " + path);
        if (CommonUtils.isEmptyString(path)) {
            return;
        }
        mGduMediaPlayer.createPlayer(path);
    }

    @Override
    public void startPlayer() {
        mGduMediaPlayer.startPlayer();
    }

    @Override
    public void setPosition(int position) {
//        MyLogUtils.d("setPosition() position = " + position);
        mTimeSeekBar.setProgress(position);
    }

    @Override
    public void showToast(String toast) {
        hideProgressDialog();
        ToastUtil.s(toast);
    }

    @Override
    public void showPlayStatus(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if (isPlaying) {
            mPlayImageView.setImageResource(R.mipmap.icon_back_play_pause);
        } else {
            mPlayImageView.setImageResource(R.mipmap.icon_record_play);
        }
    }

    @Override
    public void setMaxSeekBar(long max) {
        MyLogUtils.d("setMaxSeekBar() max = " + max);
        mTimeSeekBar.setMax((int) max);
    }
}
