package com.gdu.command.uploadpic;

public interface IUploadPicDiaryView {
    int UPLOAD_FAIL = 0xf1;
    int UPLOAD_SUC = 0xf2;
    int UPLOAD_VIDEO_SUC = 0xf3;

    void showOrHidePb(boolean isShow, String tip);

    void onStatusChange(int type, String content);

    void onStatusChange(int type, String urlImg,String urlVideo);
}
