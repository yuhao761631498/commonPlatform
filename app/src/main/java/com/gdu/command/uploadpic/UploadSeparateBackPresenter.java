package com.gdu.command.uploadpic;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.RetrofitUploadClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.ui.cases.deal.UploadPicService;
import com.rxjava.rxlife.RxLife;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 上传图片和视频的Presenter(结果分开返回)
 * @author wixche
 */
public class UploadSeparateBackPresenter extends BasePresenter {
    private IUploadSeparateBackView mIUploadSeparateBackView;
    private UploadPicService mUploadPicService;

    public UploadSeparateBackPresenter() {
        mUploadPicService = RetrofitUploadClient.getAPIService(UploadPicService.class);
    }

    public void setIView(IUploadSeparateBackView view) {
        mIUploadSeparateBackView = view;
    }

    /**
     * 上传文件
     * @param images
     */
    public void uploadFiles(List<String> images, List<String> videos) {
        MyLogUtils.d("uploadFiles()");

        final int[] totalSize = {0};
        boolean haveImage;
        boolean haveVideo;
        if (!CommonUtils.isEmptyList(images)) {
            MyLogUtils.d("uploadFiles() imgSize = " + images.size());
            totalSize[0] = images.size();
            haveImage = true;
        } else {
            MyLogUtils.d("uploadFiles() imgSize = 0");
            images = new ArrayList<>();
            haveImage = false;
        }
        if (!CommonUtils.isEmptyList(videos)) {
            MyLogUtils.d("uploadFiles() videoSize = " + videos.size());
            totalSize[0] += videos.size();
            haveVideo = true;
        } else {
            MyLogUtils.d("uploadFiles() videoSize = 0");
            haveVideo = false;
        }

        final boolean isEmptyData = !haveImage & !haveVideo;
        if (isEmptyData) {
            mIUploadSeparateBackView.onUpResultCallback(IUploadSeparateBackView.UPLOAD_FAIL, null);
            mIUploadSeparateBackView.showOrHidePb(false, "");
            return;
        }

        final int[] sucNum = {0};
        final int[] failNum = {0};
        final List<String> upSucPics = new ArrayList<>();
        if (mIUploadSeparateBackView != null) {
            mIUploadSeparateBackView.showOrHidePb(true, "文件上传中，已完成（" + sucNum[0]
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
                    RequestBody requestBody = RequestBody.create(file,
                            MediaType.parse("multipart/form-data"));
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file",
                            file.getName(), requestBody);
                    return mUploadPicService.uploadFile(body);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(response -> {
                    MyLogUtils.d("onNext()");
                    if (CommonUtils.isEmptyString(response.data)) {
                        failNum[0]++;
//                            mHandler.sendEmptyMessage(CASE_FILE_FAILED);
                        return;
                    }
                    final String result = response.data;
                    CommonUtils.listAddAvoidNull(upSucPics, result);
                    sucNum[0]++;
                    if (mIUploadSeparateBackView != null) {
                        mIUploadSeparateBackView.showOrHidePb(true,
                                "文件上传中，已完成（" + sucNum[0] + "/" + totalSize[0] + ")...");
                    }
                }, throwable -> {
                    MyLogUtils.d("onError()");
                    MyLogUtils.e("图片上传出错", throwable);
                    if (mIUploadSeparateBackView != null) {
                        mIUploadSeparateBackView.onUpResultCallback(IUploadSeparateBackView.UPLOAD_FAIL, null);
                        mIUploadSeparateBackView.showOrHidePb(false, "");
                    }
                }, () -> {
                    MyLogUtils.d("onComplete()");
                    resultHandler(upSucPics, false);
                    if (!haveVideo) {
                        mIUploadSeparateBackView.onUpFinish();
                        return;
                    }
                    final List<String> upSucVideo = new ArrayList<>();
                    Observable.just(videos.get(0))
                            .subscribeOn(Schedulers.io())
                            .concatMap(vf -> {
                                MyLogUtils.d("uploadVideo path = " + vf);
                                final File file = new File(vf);
                                RequestBody requestBody = RequestBody.create(file,
                                        MediaType.parse("multipart/form-data"));
                                MultipartBody.Part body =
                                        MultipartBody.Part.createFormData("file",
                                                file.getName(), requestBody);
                                return mUploadPicService.uploadFile(body);
                            }).to(RxLife.toMain(getBaseActivity()))
                            .subscribe(response -> {
                                MyLogUtils.d("onNext()");
                                if (CommonUtils.isEmptyString(response.data)) {
                                    failNum[0]++;
                                    return;
                                }
                                final String result = response.data;
                                CommonUtils.listAddAvoidNull(upSucVideo, result);
                                sucNum[0]++;
                                if (mIUploadSeparateBackView != null) {
                                    mIUploadSeparateBackView.showOrHidePb(true,
                                            "文件上传中，已完成（" + sucNum[0] + "/" + totalSize[0] + ").." +
                                                    ".");
                                }
                                resultHandler(upSucVideo, true);
                                mIUploadSeparateBackView.onUpFinish();
                            }, throwable -> {
                                MyLogUtils.d("onError()");
                                MyLogUtils.e("视频上传出错", throwable);
                                if (mIUploadSeparateBackView != null) {
                                    mIUploadSeparateBackView.onUpResultCallback(IUploadSeparateBackView.UPLOAD_FAIL,
                                            null);
                                    mIUploadSeparateBackView.showOrHidePb(false, "");
                                }
                            });
                });

    }

    private void resultHandler(List<String> picUrls, boolean isVideo) {
        MyLogUtils.d("picUpResultHandler() picUrls " + picUrls + "; isVideo = " + isVideo);
        if (!CommonUtils.isEmptyList(picUrls)) {
            if (mIUploadSeparateBackView != null) {
                mIUploadSeparateBackView.onUpResultCallback(isVideo ? IUploadSeparateBackView.UPLOAD_VIDEO_SUC :
                                IUploadSeparateBackView.UPLOAD_PIC_SUC, picUrls);
                mIUploadSeparateBackView.showOrHidePb(false, "");
            }
        } else {
            if (mIUploadSeparateBackView != null) {
                mIUploadSeparateBackView.onUpResultCallback(IUploadSeparateBackView.UPLOAD_FAIL, null);
                mIUploadSeparateBackView.showOrHidePb(false, "");
            }
        }
    }

}
