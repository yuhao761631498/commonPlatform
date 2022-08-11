package com.gdu.command.ui.video.view;

public interface IVideoDetailView {

    void startMedia(String mediaPath);

    void setMediaPath(String mediaPath);

    void showToast(String toast);

    void showOrHideStreamFormatBtn(boolean isShow);
}
