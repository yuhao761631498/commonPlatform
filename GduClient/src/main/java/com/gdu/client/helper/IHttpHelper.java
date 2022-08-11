package com.gdu.client.helper;

/**
 * Created by zhangzhilai on 2018/1/9.
 */

public interface IHttpHelper {

    void getFileSize(String url, OnFileGetListener onFileGetListener);

    interface OnFileGetListener{
        void onFileSizeGet(int size);
    }
}
