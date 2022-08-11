package com.gdu.command.ui.video.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.TextureView;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.FileUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.ui.my.LoginActivity;
import com.gdu.command.ui.video.bean.PlayStreamBean;
import com.gdu.command.ui.video.model.ChildrenBean;
import com.gdu.command.ui.video.model.VideoService;
import com.gdu.command.ui.video.view.IVideoDetailView;
import com.gdu.model.LightType;
import com.gdu.model.config.StorageConfig;
import com.gdu.model.device.PTZCmdType;
import com.google.gson.Gson;
import com.rxjava.rxlife.RxLife;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 监控详情
 */
public class VideoDetailPresenter extends BasePresenter {

    private static final int SAVE_PICTURE_SUCCEED = 0x01;
    private static final int SAVE_PICTURE_FAILED = 0x02;
    private static final int STREAM_URL_SUCCEED = 0x03;
    private static final int STREAM_URL_FAILED = 0x04;

    private IVideoDetailView mVideoDetailView;
    private DevControlService mDevControlService;
    private String mDeviceId;
    private String mChannelID;
    private LightType mCurrentLightType;
    private ChildrenBean mDeviceInfo;
    private TextureView mSurface;
    private Handler mHandler;
    private int mStep;
    private String mAccessType = "0";
    private String mChannel1;
    private String mChannel2;

    private PlayStreamBean.DataBean curPlayBean;

    /**
     * 记录当前码流格式(1: 标清(默认)；2: 高清)
     */
    private int curStreamType = 1;

    public VideoDetailPresenter() {
        mDevControlService = RetrofitClient.getAPIService(DevControlService.class);
        mStep = 4;
        initHandler();
    }

    private void initHandler() {
        mHandler = new Handler(msg -> {
            switch (msg.what) {
                case SAVE_PICTURE_SUCCEED:
                    mVideoDetailView.showToast("图片保存成功");
                    break;
                case SAVE_PICTURE_FAILED:
                    mVideoDetailView.showToast("图片保存失败");
                    break;
                case STREAM_URL_SUCCEED:
                    MyLogUtils.d("handleMessage() STREAM_URL_SUCCEED handlerTime = " + System.currentTimeMillis());
                    String url = (String) msg.obj;
                    mVideoDetailView.setMediaPath(url);
                    break;
                case STREAM_URL_FAILED:
                    mVideoDetailView.showToast("获取播放地址失败");
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    public void setVideoDetailView(IVideoDetailView videoDetailView) {
        mVideoDetailView = videoDetailView;
    }

    public void setTextureView(TextureView surface) {
        mSurface = surface;
    }

    public void setDeviceInfoNew(ChildrenBean deviceInfo, LightType lightType) {
        MyLogUtils.d("setDeviceInfoNew()");
        mDeviceInfo = deviceInfo;
        mChannel1 = "1";
        mChannel2 = "2";
        mCurrentLightType = lightType;
        final List<ChildrenBean> streamList = deviceInfo.getChildren();
        if (streamList == null || streamList.size() == 0) {
            return;
        }
        // 征哥说目前都是国标所以写死
        mAccessType = "1";
        if (streamList.size() == 1) {
            ChildrenBean streamInfo = streamList.get(0);
            mChannel1 = streamInfo.getChannelType() + "";
        } else if (streamList.size() == 2) {
            ChildrenBean streamInfo1 = streamList.get(0);
            mChannel1 = streamInfo1.getChannelType() + "";
            ChildrenBean streamInfo2 = streamList.get(1);
            mChannel2 = streamInfo2.getChannelType() + "";
        }
    }
//    public void setDeviceInfo(DeviceInfo deviceInfo) {
//        MyLogUtils.d("setDeviceInfo()");
//        mDeviceId = deviceInfo.getDeviceCode();
//        mDeviceInfo = deviceInfo;
//        mChannel1 = "0";
//        mChannel2 = "1";
//        mCurrentLightType = deviceInfo.getLightType();
//        if (deviceInfo.getStreamList() == null) {
//            return;
//        }
//        if (deviceInfo.getStreamList().size() == 1) {
//            StreamInfo streamInfo = deviceInfo.getStreamList().get(0);
//            mAccessType = streamInfo.getAccessType();
//            mChannel1 = streamInfo.getAccessType();
//        } else if (deviceInfo.getStreamList().size() == 2) {
//            StreamInfo streamInfo1 = deviceInfo.getStreamList().get(0);
//            mAccessType = streamInfo1.getAccessType();
//            mChannel1 = streamInfo1.getStreamType();
//            StreamInfo streamInfo2 = deviceInfo.getStreamList().get(1);
//            mAccessType = streamInfo2.getAccessType();
//            mChannel2 = streamInfo2.getStreamType();
//        }
//    }

    /**
     * @param lightType
     */
    public void setLightType(LightType lightType) {
        mCurrentLightType = lightType;
    }

    /**
     * 切换光类型
     */
    public void changeLightType() {
        MyLogUtils.d("changeLightType()");
        if (mCurrentLightType == LightType.VISIBLE_LIGHT) {
            mCurrentLightType = LightType.INFRARED_LIGHT;
        } else {
            mCurrentLightType = LightType.VISIBLE_LIGHT;
        }
        getVideoPath();
    }

    /**
     * 切换码流类型
     * @param type
     */
    public void changeStreamType(int type) {
        MyLogUtils.d("changeStreamType() type = " + type);
        curStreamType = type;
        dealSteam(curPlayBean);
    }

    /**
     * 获取当前流的播放地址
     *
     * @return
     */
    public String getVideoPath() {
        MyLogUtils.d("getVideoPath()");
        String path = null;
        if (mCurrentLightType == LightType.VISIBLE_LIGHT) {
            getDeviceStreamUrl(mChannel1);
        } else {
            getDeviceStreamUrl(mChannel2);
        }
        MyLogUtils.d("getVideoPath() path = " + path);
        return path;
    }

    /**
     * 获取当前流的播放地址
     *
     * @return
     */
    public void getVideoPathByChannel(String channel) {
        MyLogUtils.d("getVideoPathByChannel() channel = " + channel);
        if (channel.equals(mChannel1)) {
            mCurrentLightType = LightType.VISIBLE_LIGHT;
            getDeviceStreamUrl(mChannel1);
        } else if (channel.equals(mChannel2)) {
            mCurrentLightType = LightType.INFRARED_LIGHT;
            getDeviceStreamUrl(mChannel2);
        }
    }

    private void getDeviceStreamUrl(String type) {
        MyLogUtils.d("getDeviceStreamUrl() type = " + type);
        Map<String, String> params = new HashMap<>();
        ChildrenBean streamBean = null;
        for (int i = 0; i < mDeviceInfo.getChildren().size(); i++) {
            if (type.equals(mDeviceInfo.getChildren().get(i).getChannelType() + "")) {
                streamBean = mDeviceInfo.getChildren().get(i);
                break;
            }
        }
        if (streamBean == null) {
            return;
        }
        mDeviceId = streamBean.getDeviceId();
        mChannelID = streamBean.getChannelId();
        params.put("deviceCode", streamBean.getDeviceId());
        params.put("channelId", streamBean.getChannelId());
        params.put("accessType", mAccessType);
        params.put("deviceTypeCode", getEnDeviceType(mDeviceInfo.getDeviceType()));
        List<Map<String, String>> result = new ArrayList<>();
        result.add(params);
        Gson gson = new Gson();
        String paramString = gson.toJson(result);

        VideoService mVideoService = RetrofitClient.getAPIService(VideoService.class);
        mVideoService.getPlayStream(paramString).enqueue(new Callback<PlayStreamBean>() {
            @Override
            public void onResponse(Call<PlayStreamBean> call, Response<PlayStreamBean> response) {
                MyLogUtils.d("onResponse()");
                final PlayStreamBean bean = response.body();
                final boolean isEmptyData = bean == null || bean.getData() == null
                        || bean.getData().size() == 0 || bean.getCode() != 0;
                if (isEmptyData) {
                    if (bean!=null&&bean.getCode() == 401) {
                        getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                    }
                    mHandler.sendEmptyMessage(STREAM_URL_FAILED);
                    return;
                }
                dealSteam(bean.getData().get(0));
            }

            @Override
            public void onFailure(Call<PlayStreamBean> call, Throwable t) {
                MyLogUtils.d("onFailure()");
                mHandler.sendEmptyMessage(STREAM_URL_FAILED);
                MyLogUtils.e("获取视频流地址出错", t);
            }
        });
    }

    private String getEnDeviceType(String type) {
        String typeStr = "";
        switch (type) {
            case "2":
                typeStr = "QJ";
                break;

            case "3":
                typeStr = "GDJK";
                break;

            case "4":
                typeStr = "DDJK";
                break;

            case "5":
                typeStr = "WRJ";
                break;

            case "6":
                typeStr = "SCSB";
                break;

            default:
                break;
        }
        return typeStr;
    }

    private void dealSteam(PlayStreamBean.DataBean data) {
        MyLogUtils.d("dealSteam()");
        if (data == null) {
            mHandler.sendEmptyMessage(STREAM_URL_FAILED);
            return;
        }
        curPlayBean = data;
        String url;
        final String app = data.getApp();
        final String deviceCode = data.getDeviceCode();
        final String channelId = data.getChannelId();
        final boolean isHaveSubSteam = !CommonUtils.isEmptyString(data.getUseSubStream())
                && ("enabled".equals(data.getUseSubStream()) ? true : false)
                && !CommonUtils.isEmptyString(data.getSubStreamPlayUrl());
        if (mVideoDetailView != null) {
            mVideoDetailView.showOrHideStreamFormatBtn(isHaveSubSteam);
        }
        final String ip = data.getIp();
        final String stream = data.getStream();
        MyLogUtils.d("dealSteam() curStreamType = " + curStreamType + "; isHaveSubSteam = " + isHaveSubSteam
                + "; app = " + app
                + "; ip = " + ip
                + "; app = " + app
                + "; deviceCode = " + deviceCode
                + "; channelId = " + channelId
                + "; stream = " + stream);
        if (curStreamType == 1 && isHaveSubSteam) {
            // 辅码流
            url = data.getSubStreamPlayUrl();
        } else {
//            // 主码流(rtsp)
//            if ("live".equals(app)) {
//                if ("GDJK".equals(mDeviceInfo.getDeviceTypeCode())) {
//                    url = "rtsp://" + ip + "/" + app + "/" + deviceCode + channelId;
//                } else {
//                    url = "rtsp://" + ip + "/" + app + "/" + stream;
//                }
//            } else {
//                url = "rtsp://" + ip + "/" + app + "/" + stream;
//            }
            // 主码流(flv)
            url = "http://" + ip + ":" + data.getWsPort() + "/" + app + "/" + stream + ".flv";
        }

        MyLogUtils.d("dealSteam() url = " + url);
        if (!CommonUtils.isEmptyString(url)) {
            Message message1 = new Message();
            message1.what = STREAM_URL_SUCCEED;
            message1.obj = url;
            mHandler.sendMessage(message1);
        } else {
            mHandler.sendEmptyMessage(STREAM_URL_FAILED);
        }
    }

    /**
     * 调整云台运动
     *
     * @param pitch
     * @param direct
     */
    public void gimbalMove(float pitch, float direct) {
        MyLogUtils.d("gimbalMove() pitch = " + pitch + "; direct = " + direct);
//        String channelId;
//        if (mCurrentLightType == LightType.VISIBLE_LIGHT) {
//            channelId = "0";
//        } else {
//            channelId = "1";
//        }
//        mDeviceControl.gimbalMove(mDeviceId, channelId, pitch, direct);
    }

    /**
     * 调整云台运动(单步移动)
     */
    public void gimbalSingleMove(PTZCmdType cmdType, boolean isAutoStop) {
        MyLogUtils.d("gimbalSingleMove() cmdType = " + cmdType + "; isAutoStop = " + isAutoStop);
        final String optType = getCmdTypeValue(cmdType);
        final HashMap<String, String> params = new HashMap<>();
        params.put("horizonSpeed", "125");
        params.put("verticalSpeed", "125");
        params.put("zoomSpeed", "125");
        if (!CommonUtils.isEmptyString(mDeviceId)) {
            params.put("deviceId", mDeviceId);
        }
        if (!CommonUtils.isEmptyString(mChannelID)) {
            params.put("channelId", mChannelID);
        }
        if (!CommonUtils.isEmptyString(optType)) {
            params.put("command", optType);
        }
        mDevControlService.ptzControl(params)
        .subscribeOn(Schedulers.io())
        .to(RxLife.to(mView.getBaseActivity()))
        .subscribe(bean -> {
            if (bean!=null&&bean.code == 401) {
                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
            }
            final boolean isNeedStop = bean != null && (bean.code == 0 || bean.code == 200) && isAutoStop;
            if (isNeedStop) {
                sendStopCmd(params);
            }
        }, throwable -> {
            MyLogUtils.e("设置云台方向/变倍出错", throwable);
        });
    }

    private void sendStopCmd(HashMap<String, String> params) {
        params.put("command", "stop");
        mDevControlService.ptzControl(params)
                .subscribeOn(Schedulers.io())
                .to(RxLife.to(mView.getBaseActivity()))
                .subscribe(bean -> {

                });
    }


    public void gimbalUp() {
        MyLogUtils.d("gimbalUp()");
//        String channelId;
//        if (mCurrentLightType == LightType.VISIBLE_LIGHT) {
//            channelId = "0";
//        } else {
//            channelId = "1";
//        }
//        mDeviceControl.gimbalUp(mDeviceId, channelId);
    }

    /**
     * 云台光学控制
     * @param cmdType
     */
    public void ptzControl(PTZCmdType cmdType){
        MyLogUtils.d("ptzControl() cmdType = " + cmdType);
//        if (cmdType != PTZCmdType.SpeedMinus && cmdType != PTZCmdType.SpeedPlus) {
//            String channelId;
//            if (mCurrentLightType == LightType.VISIBLE_LIGHT) {
//                channelId = "0";
//            } else {
//                channelId = "1";
//            }
//            mDeviceControl.deviceControl(mDeviceId, channelId, cmdType, 0 + "" , mStep + "");
//            mDeviceControl.deviceControl(mDeviceId, channelId, cmdType, 1 + "" , mStep + "");
//        }
    }

    /**
     * 开始云台光学控制
     */
    public void startPTZControl(PTZCmdType cmdType) {
        MyLogUtils.i("startPTZControl() cmdType = " + cmdType);
        if (cmdType == PTZCmdType.ZoomOut || cmdType == PTZCmdType.ZoomIn) {
            gimbalSingleMove(cmdType, true);
        }
//        if (cmdType != PTZCmdType.SpeedMinus && cmdType != PTZCmdType.SpeedPlus) {
//            String channelId;
//            if (mCurrentLightType == LightType.VISIBLE_LIGHT) {
//                channelId = "0";
//            } else {
//                channelId = "1";
//            }
//            mDeviceControl.deviceControl(mDeviceId, channelId, cmdType, 0 + "" , mStep + "");
//        }
    }

    /**
     * 停止云台光学控制
     */
    public void stopPTZControl(PTZCmdType cmdType) {
        MyLogUtils.i("stopPTZControl() cmdType = " + cmdType);
//        if (cmdType != PTZCmdType.SpeedMinus && cmdType != PTZCmdType.SpeedPlus) {
//            String channelId;
//            if (mCurrentLightType == LightType.VISIBLE_LIGHT) {
//                channelId = "0";
//            } else {
//                channelId = "1";
//            }
//            mDeviceControl.deviceControl(mDeviceId, channelId, cmdType, 1 + "" , mStep + "");
//        }
    }

    /**
     * 设置云台控制步进
     * @param step
     */
    public void setStep(int step){
        MyLogUtils.i("setStep() step = " + step);
        mStep = step;
    }


    /***********************
     * 本地副本保存
     * 拍照----余浩
     */
    public synchronized void savePicTranscript() {
        try {
            if (!FileUtil.isFileExist(StorageConfig.OUT_IMAGE_PATH)) {
                FileUtil.createFolder(StorageConfig.OUT_IMAGE_PATH);
            }
            String fileNamePic = System.currentTimeMillis() + ".png";
            Bitmap bitmap = mSurface.getBitmap(1280, 720);
            new SavePicThread(bitmap, fileNamePic).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***********************
     * 保存图片的功能，还是放到线程中，比较好---ron
     */
    private class SavePicThread extends Thread {
        private Bitmap mBitmap;
        private String fileNamePic;

        public SavePicThread(Bitmap bitmap, String fileNamePic) {
            this.mBitmap = bitmap;
            this.fileNamePic = fileNamePic;
        }


        @Override
        public void run() {
            MyLogUtils.d("SavePicThread run()");
            if (mBitmap != null) {
                try {
                    String destPath = StorageConfig.OUT_IMAGE_PATH + fileNamePic;
                    FileOutputStream fileOutputStream = new FileOutputStream(destPath);
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 99, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    mHandler.sendEmptyMessage(SAVE_PICTURE_SUCCEED);
                } catch (Exception e) {
                    mHandler.sendEmptyMessage(SAVE_PICTURE_FAILED);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取控制类型值
     * @param ptzCmdType
     * @return
     */
    private String getCmdTypeValue(PTZCmdType ptzCmdType){
        String cmdType = "stop";
        switch (ptzCmdType){
            case TiltUp:
                cmdType = "up";
                break;
            case TiltDown:
                cmdType = "down";
                break;
            case PanLeft:
                cmdType = "left";
                break;
            case PanRight:
                cmdType = "right";
                break;
            case ZoomIn:
                cmdType = "zoomin";
                break;
            case ZoomOut:
                cmdType = "zoomout";
                break;

//            case FIFocusFar:
//                cmdType = "6";
//                break;
//            case FIFocusNear:
//                cmdType = "7";
//                break;
//            case FIIrisIn:
//                cmdType = "8";
//                break;
//            case FIIrisOut:
//                cmdType = "9";
//                break;

            case Stop:
                cmdType = "stop";
                break;
            default:
                break;
        }
        return cmdType;
    }

    public LightType getCurrentLightType() {
        return mCurrentLightType;
    }
}
