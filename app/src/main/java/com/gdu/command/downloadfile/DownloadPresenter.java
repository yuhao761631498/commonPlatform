package com.gdu.command.downloadfile;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.lifecycle.LifecycleOwner;

import com.gdu.baselibrary.network.RetrofitDownloadClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.rxjava.rxlife.RxLife;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class DownloadPresenter {
    private Context mContext;
    private LifecycleOwner mOwner;
    private DownloadService mAlarmService;

    public DownloadPresenter(Context context, LifecycleOwner owner) {
        mContext = context;
        mOwner = owner;
        mAlarmService = RetrofitDownloadClient.getAPIService(DownloadService.class);
    }

    /**
     * 下载预警中心视频
     */
    public void downloadVideo(String url, String path, MyDownloadListener listener) {
        MyLogUtils.d("downloadVideo() url = " + url + "; path = " + path);
        try {
            if (CommonUtils.isEmptyString(url)) {
                listener.onFail("下载路径不能为空");
                return;
            }
            if (CommonUtils.isEmptyString(path)) {
                listener.onFail("文件保存路径不能为空");
                return;
            }
            final String fileName = url.substring(url.lastIndexOf("/") + 1);
            MyLogUtils.d("downloadVideo() fileName = " + fileName);
//            final File filePath = new File(path + File.separator + fileName);
//            if (!filePath.exists()) {
//                filePath.createNewFile();
//            } else {
//                filePath.delete();
//            }
            mAlarmService.downloadVideo(url)
                    .subscribeOn(Schedulers.io())
                    .map(responseBody -> {
                // writeResponseToDisk(filePath.getPath(), response, listener);
                Uri uri = null;
                InputStream is = null;
                OutputStream os = null;
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.Video.Media.MIME_TYPE,
                            CommonUtils.getFileMIMEType(fileName));
                    contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, fileName);
                    contentValues.put(MediaStore.Video.Media.DATE_TAKEN,
                            System.currentTimeMillis());
                    //只是往 MediaStore 里面插入一条新的记录，MediaStore 会返回给我们一个空的 Content Uri
                    //接下来问题就转化为往这个 Content Uri 里面写入
                    uri = mContext.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);

                    is = responseBody.byteStream();
                    os = mContext.getContentResolver().openOutputStream(uri);
                    if (os != null) {
                        byte[] buffer = new byte[4096];
                        int len;
                        long currentLength = 0;
                        long totalLength = responseBody.contentLength();
                        while ((len = is.read(buffer)) != -1) {
                            os.write(buffer, 0, len);
                            currentLength += len;
                            listener.onProgress((int) (100 * currentLength / totalLength));
                        }

                        //下载完成，并返回保存的文件路径
                        listener.onFinish("");
                    }
                } catch (Exception e) {
                    listener.onFail("IOException");
                    MyLogUtils.e("下载文件出错", e);
                } finally {
                    if (os != null) {
                        os.flush();
                    }
                    if (is != null) {
                        is.close();
                    }
                }
                return uri;
            }).to(RxLife.to(mOwner))
                    .subscribe(uri -> {

                    }, throwable -> {
                        listener.onFail("文件下载出错");
                        MyLogUtils.e("下载视频文件出错", throwable);
                    });

        } catch (Exception e) {
            listener.onFail("文件下载出错");
            MyLogUtils.e("文件下载出错", e);
        }
    }

    private void writeResponseToDisk(String path, ResponseBody response, MyDownloadListener downloadListener) {
        //从response获取输入流以及总大小
        writeFileFromIS(new File(path), response.byteStream(), response.contentLength(), downloadListener);
    }

    /**
     * 将输入流写入文件
     * @param file
     * @param is
     * @param totalLength
     * @param downloadListener
     */
    private void writeFileFromIS(File file, InputStream is, long totalLength, MyDownloadListener downloadListener) {
        MyLogUtils.i("writeFileFromIS() totalLength = " + totalLength);
        // 开始下载
        downloadListener.onStart();

        // 创建文件
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                MyLogUtils.e("文件创建出错", e);
                downloadListener.onFail("createNewFile IOException");
                return;
            }
        }

        OutputStream os = null;
        long currentLength = 0;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            final int sBufferSize = 4096;
            byte[] mData = new byte[sBufferSize];
            int len;
            while ((len = is.read(mData, 0, sBufferSize)) != -1) {
                os.write(mData, 0, len);
                currentLength += len;
                //计算当前下载进度
                downloadListener.onProgress((int) (100 * currentLength / totalLength));
            }
            //下载完成，并返回保存的文件路径
            downloadListener.onFinish(file.getAbsolutePath());
        } catch (IOException e) {
            MyLogUtils.e("文件写入出错", e);
            downloadListener.onFail("IOException");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载升级差分文件
     */
    public void downloadFile(String url, String path, MyDownloadListener listener) {
        MyLogUtils.d("downloadFile() url = " + url + "; path = " + path);
        try {
            if (CommonUtils.isEmptyString(url)) {
                listener.onFail("下载路径不能为空");
                return;
            }
            if (CommonUtils.isEmptyString(path)) {
                listener.onFail("文件保存路径不能为空");
                return;
            }
            final File saveFolderFile = new File(path);
            if (!saveFolderFile.exists()) {
                saveFolderFile.mkdirs();
            }
            final String fileName = url.substring(url.lastIndexOf("/") + 1);
            MyLogUtils.d("downloadFile() fileName = " + fileName);
            final File filePath = new File(path + File.separator + fileName);
            if (!filePath.exists()) {
                filePath.createNewFile();
            } else {
                filePath.delete();
            }
            mAlarmService.downloadVideo(url)
                    .subscribeOn(Schedulers.io())
                    .to(RxLife.to(mOwner))
                    .subscribe(responseBody -> {
                        writeResponseToDisk(filePath.getPath(), responseBody, listener);
                    }, throwable -> {
                        listener.onFail("文件下载出错");
                        MyLogUtils.e("下载视频文件出错", throwable);
                    });

        } catch (Exception e) {
            listener.onFail("文件下载出错");
            MyLogUtils.e("文件下载出错", e);
        }
    }

}
