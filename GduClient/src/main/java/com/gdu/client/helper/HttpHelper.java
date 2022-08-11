package com.gdu.client.helper;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zhangzhilai on 2018/1/9.
 */

public class HttpHelper implements IHttpHelper {

    private final int FILE_SIZE_GOT = 0x1;
    private Handler mHandler;
    private OnFileGetListener mOnFileGetListener;

    public HttpHelper(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case FILE_SIZE_GOT:
                        mOnFileGetListener.onFileSizeGet(msg.arg1);
                        break;
                }
            }
        };
    }

    @Override
    public void getFileSize(final String url, final OnFileGetListener onFileGetListener) {
        mOnFileGetListener = onFileGetListener;
        new  Thread(new Runnable() {
            @Override
            public void run() {
                int length = -1;
                try {
                   getFileSize(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = FILE_SIZE_GOT;
                message.arg1 = length;
                mHandler.sendMessage(message);
            }
        }).start();
    }

    public int getFileSize(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestProperty("Accept-Encoding", "identity");
        conn.connect();
        int length = conn.getContentLength();
        conn.disconnect();
       return  length;
    }
}
