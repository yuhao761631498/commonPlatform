package com.gdu.command.ui.video.view;

public interface IPlaybackView {

    /**
     * 设置播放器路径
     * @param path
     */
    void setPlayerPath(String path);

    /**
     * 开始播放
     */
    void startPlayer();

    /**
     * 设置当前进度条
     * @param position
     */
    void setPosition(int position);

    /**
     * 弹窗
     * @param toast
     */
    void showToast(String toast);

    /**
     * 显示播放状态
     * @param isPlaying
     */
    void showPlayStatus(boolean isPlaying);

    /**
     * 设置进度条的最大值
     */
    void setMaxSeekBar(long max);
}
