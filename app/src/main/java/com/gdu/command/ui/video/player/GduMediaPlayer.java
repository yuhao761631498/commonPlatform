package com.gdu.command.ui.video.player;

import android.content.Context;
import android.view.TextureView;
import android.view.View;

import com.gdu.baselibrary.utils.FileUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.ui.exo.GSYExo2PlayerView;
import com.gdu.model.config.StorageConfig;
import com.gdu.baselibrary.utils.CommonUtils;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSource;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.GSYVideoShotSaveListener;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.io.File;


/**
 * 视频播放器，可播放RTMP MP4
 */
public class GduMediaPlayer {

    private static final int MSG_UPDATE_PROGRESS = 1;
    private static final int INTERVAL = 1000;

    private Context mContext;

//    private SimpleExoPlayer mMediaPlayer;

    private TextureView mSurface;

    private long mTotalDuration;

    private StandardGSYVideoPlayer mMediaPlayer;

    /** 视频上次变化时间 */
    private long mLastChangeTime;

    /** 是否正在播放 */
    private boolean isPlaying;

    /** 播放路径 */
    private String mFilePath;

    /** 是否是回复视频 */
    private boolean isPlayBack;

    private OnGduMediaPlayerListener mOnGduMediaPlayerListener;

    public GduMediaPlayer(Context context, TextureView surface) {
        mContext = context;
        mSurface = surface;
        initPlayer();
    }

    public GduMediaPlayer(Context context, StandardGSYVideoPlayer surface, boolean playBack) {
        mContext = context;
        mMediaPlayer = surface;
        this.isPlayBack = playBack;
        initPlayer();
    }

    public GduMediaPlayer(Context context, StandardGSYVideoPlayer surface) {
        mContext = context;
        mMediaPlayer = surface;
        initPlayer();
    }

    private void initPlayer(){
        MyLogUtils.d("initPlayer()");
        mMediaPlayer.getBackButton().setVisibility(View.GONE);
        mMediaPlayer.findViewById(R.id.bottom_progressbar).setVisibility(isPlayBack ?
                View.VISIBLE : View.GONE);
        mMediaPlayer.findViewById(R.id.progress).setVisibility(isPlayBack ?
                View.VISIBLE : View.GONE);
        mMediaPlayer.findViewById(R.id.lock_screen).setVisibility(View.GONE);
        mMediaPlayer.findViewById(R.id.layout_bottom).setVisibility(View.GONE);
        mMediaPlayer.findViewById(R.id.current).setVisibility(View.GONE);
        mMediaPlayer.findViewById(R.id.total).setVisibility(View.GONE);
        mMediaPlayer.findViewById(R.id.fullscreen).setVisibility(View.GONE);
        mMediaPlayer.setVideoAllCallBack(new VideoAllCallBack() {
            @Override
            public void onStartPrepared(String url, Object... objects) {
                MyLogUtils.d("onStartPrepared() url = " + url);
            }

            @Override
                public void onPrepared(String url, Object... objects) {
                MyLogUtils.d("onPrepared() cTime + " + System.currentTimeMillis() + "; url = " + url);
                isPlaying = true;
                if (mOnGduMediaPlayerListener != null) {
                    mOnGduMediaPlayerListener.onStart();
                }
            }

            @Override
            public void onClickStartIcon(String url, Object... objects) {
                MyLogUtils.d("onClickStartIcon() url = " + url);
            }

            @Override
            public void onClickStartError(String url, Object... objects) {
                MyLogUtils.d("onClickStartError() url = " + url);
            }

            @Override
            public void onClickStop(String url, Object... objects) {
                MyLogUtils.d("onClickStop() url = " + url);
            }

            @Override
            public void onClickStopFullscreen(String url, Object... objects) {
                MyLogUtils.d("onClickStopFullscreen() url = " + url);
            }

            @Override
            public void onClickResume(String url, Object... objects) {
                MyLogUtils.d("onClickResume() url = " + url);
            }

            @Override
            public void onClickResumeFullscreen(String url, Object... objects) {
                MyLogUtils.d("onClickResumeFullscreen() url = " + url);
            }

            @Override
            public void onClickSeekbar(String url, Object... objects) {
                MyLogUtils.d("onClickSeekbar() url = " + url);
            }

            @Override
            public void onClickSeekbarFullscreen(String url, Object... objects) {
                MyLogUtils.d("onClickSeekbarFullscreen() url = " + url);
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                MyLogUtils.d("onAutoComplete() url = " + url);
            }

            @Override
            public void onComplete(String url, Object... objects) {
                MyLogUtils.d("onComplete() url = " + url);
                if (mOnGduMediaPlayerListener != null) {
                    mOnGduMediaPlayerListener.onEnd();
                }
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                MyLogUtils.d("onEnterFullscreen() url = " + url);
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                MyLogUtils.d("onQuitFullscreen() url = " + url);
            }

            @Override
            public void onQuitSmallWidget(String url, Object... objects) {
                MyLogUtils.d("onQuitSmallWidget() url = " + url);
            }

            @Override
            public void onEnterSmallWidget(String url, Object... objects) {
                MyLogUtils.d("onEnterSmallWidget() url = " + url);
            }

            @Override
            public void onTouchScreenSeekVolume(String url, Object... objects) {
                MyLogUtils.d("onTouchScreenSeekVolume() url = " + url);
            }

            @Override
            public void onTouchScreenSeekPosition(String url, Object... objects) {
                MyLogUtils.d("onTouchScreenSeekPosition() url = " + url);
            }

            @Override
            public void onTouchScreenSeekLight(String url, Object... objects) {
                MyLogUtils.d("onTouchScreenSeekLight() url = " + url);
            }

            @Override
            public void onPlayError(String url, Object... objects) {
                MyLogUtils.d("onPlayError() url = " + url + "; objects = " + objects.toString());
            }

            @Override
            public void onClickStartThumb(String url, Object... objects) {
                MyLogUtils.d("onClickStartThumb() url = " + url);
            }

            @Override
            public void onClickBlank(String url, Object... objects) {
                MyLogUtils.d("onClickBlank() url = " + url);
            }

            @Override
            public void onClickBlankFullscreen(String url, Object... objects) {
                MyLogUtils.d("onClickBlankFullscreen() url = " + url);
            }
        });
        mMediaPlayer.setGSYVideoProgressListener(new GSYVideoProgressListener() {
            @Override
            public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
//                MyLogUtils.d("onProgress() progress = " + progress + "; secProgress = " + secProgress
//                        + "; currentPosition = " + currentPosition + "; duration = " + duration);
                timeChanged(currentPosition);
            }
        });
    }

    public void setOnGduMediaPlayerListener(OnGduMediaPlayerListener onGduMediaPlayerListener){
        mOnGduMediaPlayerListener = onGduMediaPlayerListener;
    }

    /**
     * 设置视频路径
     *
     * @param path
     */
    public void setMediaPath(String path) {
        MyLogUtils.d("setMediaPath() path = " + path);
        mFilePath = path;
    }

    /**
     * Creates MediaPlayer and plays video
     *
     * @param path
     */
    public void createPlayer(String path) {
        if (CommonUtils.isEmptyString(path)) {
            return;
        }
        MyLogUtils.d("createPlayer() cTime + " + System.currentTimeMillis() + "; path = " + path);
        MediaSource videoSource;
        MediaItem mediaItem =  MediaItem.fromUri(path);
        if (path.contains("rtmp")) {
            RtmpDataSource.Factory dataSourceFactory = new RtmpDataSourceFactory();
            videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
        } else {
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                    Util.getUserAgent(mContext, "gdu"));
            //这是一个代表将要被播放的媒体的MediaSource
            videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem);
            //使用资源准备播放器
//            player.prepare(videoSource);
        }

        mMediaPlayer.setUp(path, false, "");
//        ((GSYExo2PlayerView)mMediaPlayer).setUp(new GSYVideoModel(path, ""));
        mMediaPlayer.startPlayLogic();
//        mMediaPlayer.setMediaSource(videoSource);
//        mMediaPlayer.prepare();
    }


    /**
     * 开始播放
     */
    public void startPlayer() {
        MyLogUtils.d("startPlayer()");
        isPlaying = true;
        mMediaPlayer.startPlayLogic();
    }

    /**
     * 设置播放速率
     */
    public void setRate(float rate) {
        MyLogUtils.d("setRate() rate = " + rate);
        if(mMediaPlayer != null) {
//            PlaybackParameters playbackParameters = new PlaybackParameters(rate, 1.0F);
            final float speed = mMediaPlayer.getSpeed();
            MyLogUtils.d("setRate() mMediaPlayer is not null speed = " + speed);
            mMediaPlayer.setSpeed(rate);
//            mMediaPlayer
        }
    }


    public void onResume() {
        MyLogUtils.d("onResume()");
        mMediaPlayer.getGSYVideoManager().start();
    }

    /**
     * 暂停播放
     */
    public void onPause() {
        MyLogUtils.d("onPause()");
        mMediaPlayer.getGSYVideoManager().pause();
    }

    public void onDestroy() {
        MyLogUtils.d("onDestroy()");
        if (mMediaPlayer != null) {
            mMediaPlayer.getGSYVideoManager().stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 回退
     */
    public void rollBack(){
        MyLogUtils.d("rollBack()");
        long currentTime = mMediaPlayer.getGSYVideoManager().getCurrentPosition();
        long realTime = currentTime - 20 * 1000;
        if(realTime < 0){
            realTime = 0;
        }
        mMediaPlayer.seekTo(realTime);
    }

    /**
     * 前进
     */
    public void forward() {
        MyLogUtils.d("forward()");
        long currentTime = mMediaPlayer.getGSYVideoManager().getCurrentPosition();
        long realTime = currentTime + 20 * 1000;
        if (realTime > mMediaPlayer.getDuration()) {
            realTime = mMediaPlayer.getDuration();
        }
        mMediaPlayer.seekTo(realTime);
    }

    /**
     * 设置视频的时间
     * @param time
     */
    public void setTime(long time){
        MyLogUtils.d("setTime() time = " + time);
        mMediaPlayer.seekTo(time);
    }

    /**
     * 时间变化
     */
    private void timeChanged(long position){
//        MyLogUtils.d("timeChanged() position = " + position);
        if (mOnGduMediaPlayerListener != null) {
            mOnGduMediaPlayerListener.onTimeChanged(position);
        }
    }

    /**
     * 保存图片
     */
    public void savePicTranscript(){
        MyLogUtils.d("savePicTranscript()");
        if (!FileUtil.isFileExist(StorageConfig.OUT_IMAGE_PATH)) {
            FileUtil.createFolder(StorageConfig.OUT_IMAGE_PATH);
        }
        String fileNamePic = System.currentTimeMillis() + ".png";
        File file = new File(StorageConfig.OUT_IMAGE_PATH + fileNamePic);
        mMediaPlayer.saveFrame(file, new GSYVideoShotSaveListener() {
            @Override
            public void result(boolean success, File file) {
                if (success) {
                    mOnGduMediaPlayerListener.onPicSaved();
                }
            }
        });
    }

    public void setIsTouchWiget(boolean isTouchWiget){
        MyLogUtils.d("setIsTouchWiget() isTouchWiget = " + isTouchWiget);
        mMediaPlayer.setIsTouchWiget(isTouchWiget);
    }

    public interface OnGduMediaPlayerListener{
        void onTimeChanged(long currentTime);
        void onEnd();
        void onStart();
        void onPaused();
        void onStopped();
        void onPicSaved();
    }

}
