package com.gdu.command.uploadpic;

import android.util.Log;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.network.RetrofitUploadClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.ui.cases.deal.UploadPicService;
import com.gdu.command.ui.home.HomeService;
import com.rxjava.rxlife.RxLife;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 上传图片的Presenter
 *
 * @author wixche
 */
public class UploadPicPresenter extends BasePresenter {
    private IUploadPicDiaryView mUploadPicDiaryView;
    private UploadPicService mUploadPicService;

    public UploadPicPresenter() {
        mUploadPicService = RetrofitUploadClient.getAPIService(UploadPicService.class);
    }

    public void setIView(IUploadPicDiaryView view) {
        mUploadPicDiaryView = view;
    }

    /**
     * 上传文件
     *
     * @param images
     */
    public void uploadFiles(List<String> images, List<String> videos) {
        MyLogUtils.d("uploadFiles()");

        final int[] totalSize = {0};
        boolean haveImage;
        boolean haveVideo;
        if (images != null && images.size() > 0) {
            MyLogUtils.d("uploadFiles() imgSize = " + images.size());
            totalSize[0] = images.size();
            haveImage = true;
        } else {
            MyLogUtils.d("uploadFiles() imgSize = 0");
            images = new ArrayList<>();
            haveImage = false;
        }
        if (videos != null && videos.size() > 0) {
            MyLogUtils.d("uploadFiles() videoSize = " + videos.size());
            totalSize[0] += videos.size();
            haveVideo = true;
        } else {
            MyLogUtils.d("uploadFiles() videoSize = 0");
            haveVideo = false;
        }

        final boolean isEmptyData = !haveImage & !haveVideo;
        if (isEmptyData) {
            mUploadPicDiaryView.onStatusChange(IUploadPicDiaryView.UPLOAD_FAIL, "");
            mUploadPicDiaryView.showOrHidePb(false, "");
            return;
        }

        final int[] sucNum = {0};
        final int[] failNum = {0};
        final StringBuffer sb = new StringBuffer();
        if (mUploadPicDiaryView != null) {
            mUploadPicDiaryView.showOrHidePb(true, "文件上传中，已完成（" + sucNum[0]
                    + "/" + totalSize[0] + ")...");
        }
        Observable.just(images)
                .flatMapIterable(list ->
                        Luban.with(mView.getContext()).load(list).filter(path ->
                                !CommonUtils.isEmptyList(list)).ignoreBy(MyConstants.LUBAN_IGONE_SIZE)
                                .setCompressListener(new OnCompressListener() {
                                    @Override
                                    public void onStart() {
                                        MyLogUtils.d("CompressListener onStart()");
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        MyLogUtils.d("CompressListener onSuccess()");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        MyLogUtils.d("CompressListener onError()");
                                        MyLogUtils.e("压缩图片出错", e);
                                    }
                                }).get())
                .concatMap((Function<File, ObservableSource<BaseBean<String>>>) file -> {
                    MyLogUtils.d("uploadPic name = " + file.getName());
                    RequestBody requestBody = RequestBody.create(file,
                            MediaType.parse("multipart/form-data"));
                    MultipartBody.Part body = MultipartBody.Part.createFormData("files",
                            file.getName(), requestBody);
                    return mUploadPicService.uploadMultiFile(body);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .to(RxLife.to(mView.getBaseActivity()))
                .subscribe(response -> {
                    MyLogUtils.d("onNext()");
                    if (CommonUtils.isEmptyString(response.data)) {
                        failNum[0]++;
//                            mHandler.sendEmptyMessage(CASE_FILE_FAILED);
                        return;
                    }
                    final String result = response.data;
                    if (!CommonUtils.isEmptyString(sb.toString())) {
                        sb.append(",");
                    }
                    sb.append(result);
                    sucNum[0]++;
                    if (mUploadPicDiaryView != null) {
                        mUploadPicDiaryView.showOrHidePb(true,
                                "文件上传中，已完成（" + sucNum[0] + "/" + totalSize[0] + ")...");
                    }
                }, throwable -> {
                    MyLogUtils.d("onError()");
                    MyLogUtils.e("图片上传出错", throwable);
                    if (mUploadPicDiaryView != null) {
                        mUploadPicDiaryView.onStatusChange(IUploadPicDiaryView.UPLOAD_FAIL, "");
                        mUploadPicDiaryView.showOrHidePb(false, "");
                    }
                }, () -> {
                    MyLogUtils.d("onComplete()");
                    if (!haveVideo) {
                        resultHandler(sb.toString(), "");
                    } else {
                        Observable.just(videos.get(0))
                                .subscribeOn(Schedulers.io())
                                .concatMap(vf -> {
                                    MyLogUtils.d("uploadVideo path = " + vf);
                                    final File file = new File(vf);
                                    RequestBody requestBody = RequestBody.create(file,
                                            MediaType.parse("multipart/form-data"));
                                    MultipartBody.Part body =
                                            MultipartBody.Part.createFormData("files",
                                                    file.getName(), requestBody);
                                    return mUploadPicService.uploadMultiFile(body);
                                }).to(RxLife.toMain(mView.getBaseActivity()))
                                .subscribe(response -> {
                                    MyLogUtils.d("onNext()");
                                    if (CommonUtils.isEmptyString(response.data)) {
                                        failNum[0]++;
                                        return;
                                    }
                                    final String result = response.data;
                                    sucNum[0]++;
                                    if (mUploadPicDiaryView != null) {
                                        mUploadPicDiaryView.showOrHidePb(true,
                                                "文件上传中，已完成（" + sucNum[0] + "/" + totalSize[0] + ").." +
                                                        ".");
                                    }
                                    resultHandler(sb.toString(), result);
                                }, throwable -> {
                                    MyLogUtils.d("onError()");
                                    MyLogUtils.e("视频上传出错", throwable);
                                    if (mUploadPicDiaryView != null) {
                                        mUploadPicDiaryView.onStatusChange(IUploadPicDiaryView.UPLOAD_FAIL, "");
                                        mUploadPicDiaryView.showOrHidePb(false, "");
                                    }
                                });
                    }
                });
    }

    private void resultHandler(String urlsImg, String urlVideo) {
        if (!CommonUtils.isEmptyString(urlsImg) || !CommonUtils.isEmptyString(urlVideo)) {
            if (mUploadPicDiaryView != null) {
                mUploadPicDiaryView.onStatusChange(IUploadPicDiaryView.UPLOAD_SUC, urlsImg, urlVideo);
                mUploadPicDiaryView.showOrHidePb(false, "");
            }
        } else {
            if (mUploadPicDiaryView != null) {
                mUploadPicDiaryView.onStatusChange(IUploadPicDiaryView.UPLOAD_FAIL, "", "");
                mUploadPicDiaryView.showOrHidePb(false, "");
            }
        }
    }

}
