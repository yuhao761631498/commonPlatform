package com.gdu.command.uploadpic;

import java.util.List;

public interface IUploadSeparateBackView {
    int UPLOAD_FAIL = 0xa1;
    int UPLOAD_PIC_SUC = 0xa2;
    int UPLOAD_VIDEO_SUC = 0xa3;

    void showOrHidePb(boolean isShow, String tip);

    void onUpResultCallback(int type, List<String> upResult);

    void onUpFinish();
}
