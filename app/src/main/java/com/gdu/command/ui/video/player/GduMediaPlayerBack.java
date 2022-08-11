package com.gdu.command.ui.video.player;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.TextureView;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSource;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


/**
 * 视频播放器，可播放RTMP MP4
 */
public class GduMediaPlayerBack {

    private static final int MSG_UPDATE_PROGRESS = 1;
    private static final int INTERVAL = 1000;

    private Context mContext;

    private SimpleExoPlayer mMediaPlayer;

    private TextureView mSurface;

    private long mTotalDuration;


    /**
     * 视频上次变化时间
     */
    private long mLastChangeTime;

    /**
     * 是否正在播放
     */
    private boolean isPlaying;

    /**
     * 播放路径
     */
    private String mFilePath;

    private OnGduMediaPlayerListener mOnGduMediaPlayerListener;

    public GduMediaPlayerBack(Context context, TextureView surface) {
        mContext = context;
        mSurface = surface;
        initPlayer();
    }

    private void initPlayer(){
        mMediaPlayer = new SimpleExoPlayer.Builder(mContext).build();
        mMediaPlayer.setVideoTextureView(mSurface);

        mMediaPlayer.addListener(new Player.EventListener() {

            @Override
            public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
                isPlaying = playWhenReady;
                if (playWhenReady) {
                    if (mOnGduMediaPlayerListener != null) {
                        mOnGduMediaPlayerListener.onStart();
                    }
                } else {
                    if (mOnGduMediaPlayerListener != null) {
                        mOnGduMediaPlayerListener.onPaused();
                    }
                }
            }

            @Override
            public void onPlaybackStateChanged(int state) {
                switch (state){
                    case Player.STATE_BUFFERING:
                        break;
                    case Player.STATE_ENDED:
                        if (mOnGduMediaPlayerListener != null) {
                            mOnGduMediaPlayerListener.onEnd();
                        }
                        mMediaPlayer.stop();
                        break;
                    case Player.STATE_READY:
                        isPlaying = true;
                        mTotalDuration = mMediaPlayer.getDuration();
                        if (mOnGduMediaPlayerListener != null) {
                            mOnGduMediaPlayerListener.onStart();
                        }
                        break;
                    case Player.STATE_IDLE:
                        break;
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                System.out.println("test onPlayerError " + error.getMessage());
            }
        });
    }


    //region updateVideoProgress
    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == MSG_UPDATE_PROGRESS) {
                updateVideoProgress();
            }
            return true;
        }
    });

    private void updateVideoProgress() {
        long currentPosition = mMediaPlayer.getCurrentPosition();
        timeChanged(currentPosition);
        isUpdateProgress = true;
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS, INTERVAL);

    }
    //endregion

    public void setOnGduMediaPlayerListener(OnGduMediaPlayerListener onGduMediaPlayerListener){
        mOnGduMediaPlayerListener = onGduMediaPlayerListener;
    }

    /**
     * 设置视频路径
     *
     * @param path
     */
    public void setMediaPath(String path) {
        mFilePath = path;
    }

    /**
     * Creates MediaPlayer and plays video
     *
     * @param path
     */
    public void createPlayer(String path) {
        System.out.println("test createPlayer " + path);
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
        mMediaPlayer.setMediaSource(videoSource);
        mMediaPlayer.prepare();
    }

    private boolean isUpdateProgress = false;

    /**
     * 开始播放
     */
    public void startPlayer() {
        isPlaying = true;
        mMediaPlayer.setPlayWhenReady(true);
        if (!isUpdateProgress) {
            isUpdateProgress = true;
            mHandler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
        }
    }

    /**
     * 设置播放速率
     */
    public void setRate(float rate) {
        if(mMediaPlayer != null) {
            PlaybackParameters playbackParameters = new PlaybackParameters(rate, 1.0F);
            mMediaPlayer.setPlaybackParameters(playbackParameters);
        }
    }


    public void onResume() {
        mMediaPlayer.setPlayWhenReady(true);
        if (!isUpdateProgress) {
            isUpdateProgress = true;
            mHandler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
        }
    }

    /**
     * 暂停播放
     */
    public void onPause() {
        mMediaPlayer.pause();
        isUpdateProgress = false;
        mHandler.removeMessages(MSG_UPDATE_PROGRESS);
    }

    public void onDestroy() {
        if (mMediaPlayer != null) {
            isUpdateProgress = false;
            mHandler.removeMessages(MSG_UPDATE_PROGRESS);
            mMediaPlayer.stop();
            mMediaPlayer.clearVideoTextureView(mSurface);
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 回退
     */
    public void rollBack(){
        long currentTime = mMediaPlayer.getCurrentPosition();
        long realTime = currentTime - 20 * 1000;
        if(realTime < 0){
            realTime = 0;
        }
        mMediaPlayer.seekTo(realTime);
    }

    /**
     * 前进
     */
    public void forward(){
        long currentTime = mMediaPlayer.getCurrentPosition();
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
        mMediaPlayer.seekTo(time);
    }

    /**
     * 时间变化
     */
    private void timeChanged(long position){
        System.out.println("test position " + position);
        if (mOnGduMediaPlayerListener != null) {
            mOnGduMediaPlayerListener.onTimeChanged(position);
        }
    }

    public interface OnGduMediaPlayerListener{
        void onTimeChanged(long currentTime);
        void onEnd();
        void onStart();
        void onPaused();
        void onStopped();
    }

}
