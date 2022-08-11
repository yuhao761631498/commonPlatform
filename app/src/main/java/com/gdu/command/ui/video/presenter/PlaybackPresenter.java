package com.gdu.command.ui.video.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.TextureView;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.FileUtil;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.ui.video.model.RecordBean;
import com.gdu.command.ui.video.model.VideoService;
import com.gdu.command.ui.video.player.GduMediaPlayer;
import com.gdu.command.ui.video.view.IPlaybackView;
import com.gdu.model.LightType;
import com.gdu.model.config.StorageConfig;
import com.gdu.model.config.UrlConfig;
import com.gdu.model.device.RecordInfo;
import com.gdu.model.device.RecordInfoList;
import com.gdu.util.TimeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaybackPresenter extends BasePresenter {

    private final int RECORD_GET_SUCCEED = 1;  //获取录像成功

    private final int RECORD_GET_FAILED = 2;  //获取录像失败

    private final int MP4_PLAY_END = 3; //当前MP4播放完成

    private final int TIME_CHANGED = 4; //播放时间改变

    private  final int SAVE_PICTURE_SUCCEED = 0x05;  //保存图片成功

    private  final int SAVE_PICTURE_FAILED = 0x06;   //保存图片失败

    private final int PLAY_START = 0x07; //播放成功

    private final int PLAY_FAILED = 0x08;  //播放失败

    private final int PLAY_PAUSED = 0x09;  //播放停止或暂停

    private Context mContext;

    private Handler mHandler;

    private IPlaybackView mPlaybackView;

    private GduMediaPlayer mGduMediaPlayer;

    private TextureView mSurface;

    private RecordInfoList mRecordInfoList;

    private RecordInfo mCurrentRecordInfo;  //当前正在播放的MP4

    private int mCurrentRecordPosition; //当前播放MP4在列表中的位置

    private long mStartDayTime;  //一天的开始时间

    private long mRecordTotalTime; //录像的总时间 单位秒s

    private int mSelectYear;

    private int mSelectMonth;

    private int mSelectDay;

    private String mStream;

    private LightType mCurrentLightType = LightType.VISIBLE_LIGHT;

    private String mCurrentPlayUrl;


    public PlaybackPresenter(){
        initHandler();
    }

    public void init(IPlaybackView playbackView, Context context) {
        mPlaybackView = playbackView;
        mContext = context;
    }

    public void setStream(String stream){
        MyLogUtils.i("setStream() stream = " + stream);
        mStream = stream;
    }

    private void initHandler() {
        mHandler = new Handler(msg -> {
            switch (msg.what){
                case RECORD_GET_FAILED:
                    mPlaybackView.showToast("获取文件失败");
                    break;
                case RECORD_GET_SUCCEED:
                    setPlayerPath();
                    break;
                case MP4_PLAY_END:
                    dealEndMp4();
                    break;
                case TIME_CHANGED:
                    long currentPlayTime = (long) msg.obj;
                    getPositionByTime(currentPlayTime);
                    break;
                case SAVE_PICTURE_SUCCEED:
                    mPlaybackView.showToast("图片保存成功");
                    break;
                case SAVE_PICTURE_FAILED:
                    mPlaybackView.showToast("图片保存失败");
                    break;
                case PLAY_START:
                    mPlaybackView.showPlayStatus(true);
                    break;
                case PLAY_FAILED:
                    mPlaybackView.showPlayStatus(false);
                    break;
                case PLAY_PAUSED:
                    mPlaybackView.showPlayStatus(false);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    /**
     * 设置播放器
     * @param gduMediaPlayer
     */
    public void setGduMediaPlayer(GduMediaPlayer gduMediaPlayer, TextureView textureView){
        MyLogUtils.d("setGduMediaPlayer()");
        mGduMediaPlayer = gduMediaPlayer;
        mSurface = textureView;
        mGduMediaPlayer.setOnGduMediaPlayerListener(new GduMediaPlayer.OnGduMediaPlayerListener() {
            @Override
            public void onTimeChanged(long currentTime) {
                Message message = new Message();
                message.what = TIME_CHANGED;
                message.obj = currentTime;
                mHandler.sendMessage(message);
            }

            @Override
            public void onEnd() {
                mHandler.sendEmptyMessage(MP4_PLAY_END);
            }

            @Override
            public void onStart() {
                mHandler.sendEmptyMessage(PLAY_START);
            }

            @Override
            public void onPaused() {
                mHandler.sendEmptyMessage(PLAY_PAUSED);
            }

            @Override
            public void onStopped() {
                mHandler.sendEmptyMessage(PLAY_PAUSED);
            }

            @Override
            public void onPicSaved() {
                ToastUtil.s("图片保存成功");
            }
        });
    }

    /**
     * 当前Mp4播放完成，判断是否还有Mp4，有则继续播放
     */
    private void dealEndMp4(){
        MyLogUtils.d("dealEndMp4()");
        List<RecordInfo> recordInfoList = mRecordInfoList.getPaths();
        if (recordInfoList != null && recordInfoList.size() > 0) {
          if(mCurrentRecordPosition < (recordInfoList.size() - 1)){
              mCurrentRecordPosition += 1;
              startPlay(recordInfoList.get(mCurrentRecordPosition));
          }
        }
    }

    private void setPlayerPath(){
        MyLogUtils.d("setPlayerPath()");
        if (mRecordInfoList.getPaths() != null && mRecordInfoList.getPaths().size() > 0) {
            RecordInfo recordInfo = mRecordInfoList.getPaths().get(0);
            if (recordInfo != null) {
                mCurrentRecordPosition = 0;
                startPlay(recordInfo);
            }
        }
    }

    /**
     * 开始播放当前MP4录像
     * @param recordInfo
     */
    private void startPlay(RecordInfo recordInfo){
        MyLogUtils.d("startPlay()");
        String url = getUrlFromRecordInfo(recordInfo);
        MyLogUtils.d("startPlay() url = " + url);
        mCurrentPlayUrl = url;
        mCurrentRecordInfo = recordInfo;
//        mPlaybackView.setMaxSeekBar(recordInfo.getTime_len());
        mPlaybackView.setPlayerPath(url);
        mPlaybackView.startPlayer();
    }

    /**
     * 设置当天日期
     */
    public void setDate(int year, int monthOfYear, int dayOfMonth){
        final String time = year + "_" + monthOfYear + "_" + dayOfMonth;
        MyLogUtils.d("setDate() time = " + time);
        long timeL = TimeUtils.getTimeStamp(time, "yyyy_MM_dd");
        mStartDayTime = timeL / 1000;
        mSelectYear = year;
        mSelectMonth = monthOfYear;
        mSelectDay = dayOfMonth;
        getMP4List();
    }

    /**
     * 拖动修改时间  进度条240 分成24
     * @param position 进度条的位置
     */
    public void changeTime(int position){
        MyLogUtils.d("changeTime() position = " + position);
//        long time = getTimeByPosition(position);
//        RecordInfo recordInfo = getMP4ByTime(mStartDayTime + time);
//        if (mCurrentRecordInfo != recordInfo) {
//            startPlay(recordInfo);
//        }
        mGduMediaPlayer.setTime(getCurrentTimeByPosition(position));
    }

    /**
     * 从录像信息中获取播放地址
     * @param recordInfo
     * @return
     */
    private String getUrlFromRecordInfo(RecordInfo recordInfo){
        MyLogUtils.d("getUrlFromRecordInfo()");
        String url = null;
        if (recordInfo != null) {
            String rootPath = mRecordInfoList.getRootPath();
            int index = rootPath.indexOf("live/");
            String stream = rootPath.substring(index, rootPath.length());
            url = UrlConfig.HttpCP + UrlConfig.getMp4RecordFile + stream + recordInfo.getFile_name();
        }
        MyLogUtils.d("getUrlFromRecordInfo() url = " + url);
        return url;
    }

    /**
     * 根据选择的位置获取当天的时间，返回从0点开始的时间长度
     * @param position
     * @return
     */
    public long getTimeByPosition(int position){
        MyLogUtils.d("getTimeByPosition() position = " + position);
        long hour = position / 10;
        long min = (position % 10) * 6 ;
        //从零点开始当选择点的时间长度为秒
        long time = hour * 60 * 60 + min * 60;
        return time;
    }

    /**
     * 根据进度条位置，获取当前Mp4已播放的时间, 返回ms
     * @param position
     * @return
     */
    public long getCurrentTimeByPosition(int position) {
        MyLogUtils.d("getCurrentTimeByPosition() position = " + position);
//        long min = (position % 10) * 6 ;
//        return min * 60 * 1000;
        return position * 1000;
    }

    /**
     * 根据播放时间，计算当前进度条位置
     * @param time  单条MP4播放的时间
     * @return
     */
    public void getPositionByTime(long time){
//        MyLogUtils.d("getPositionByTime() time = " + time);
        if (mCurrentRecordInfo == null) {
            return;
        }
        long playTime = mCurrentRecordInfo.getStart_time() + time - mStartDayTime;
//        int position = (int) ((playTime / (1000 * 60)) / 6);
        int position = (int) (playTime / 1000);
        mPlaybackView.setPosition(position);
    }

    /**
     * 根据时间点获取当前MP4信息
     * @param time
     * @return
     */
    public RecordInfo getMP4ByTime(long time){
        MyLogUtils.d("getMP4ByTime() time = " + time);
        if (mRecordInfoList != null && mRecordInfoList.getPaths() != null) {
            int position = 0;
            for (RecordInfo recordInfo : mRecordInfoList.getPaths()) {
                position++;
                long startTime = recordInfo.getStart_time();
                long length = recordInfo.getTime_len();
                if (time > startTime && time < (startTime + length)) {
                    mCurrentRecordPosition = position;
                    return recordInfo;
                }
            }
            RecordInfo nearestRecordInfo = null;
            long nearTime = Long.MAX_VALUE;
            position = 0;
            for (RecordInfo recordInfo : mRecordInfoList.getPaths()) {  //时间点没有视频，则找最近的视频
                position++;
                long period = Math.abs(time - recordInfo.getStart_time());
               if(period < nearTime){
                   nearTime = period;
                   mCurrentRecordPosition = position;
                   nearestRecordInfo = recordInfo;
               }
            }
            return nearestRecordInfo;
        }
        return null;
    }

    /**
     * 设置当前光类型
     * @param lightType
     */
    public void setLightType(LightType lightType){
       mCurrentLightType = lightType;
    }

    public void getMP4List(){
        String secret = "035c73f7-bb6b-4889-a715-d9eb2d1925cc";
        String vhost = "__defaultVhost__";
        String app = "live";
//        String stream = "6C01728PA4A9A100";
        String stream = mStream;
        if (mCurrentLightType == LightType.INFRARED_LIGHT) {
            stream += "1";
        } else {
            stream += "0";
        }
        String month;
        if(mSelectMonth < 10){
            month = "0" + mSelectMonth;
        } else {
            month = "" + mSelectMonth;
        }
        String day;
        if(mSelectDay < 10){
            day = "0" + mSelectDay;
        } else {
            day = "" + mSelectDay;
        }

        final String period = mSelectYear + "-" + month + "-" + day;
        final Map<String, String> params = new HashMap<>();
        params.put("secret", secret);
        params.put("vhost", vhost);
        params.put("app", app);
        params.put("stream", stream);
        params.put("period", period);

        VideoService mVideoService = RetrofitClient.getAPIService(VideoService.class);
        mVideoService.getRecordFolder(params).enqueue(new Callback<RecordBean>() {
            @Override
            public void onResponse(Call<RecordBean> call, Response<RecordBean> response) {
                final RecordBean bean = response.body();
                final boolean isEmptyData =
                        bean == null || bean.getData() == null || bean.getCode() != 0;
                if (bean != null && bean.getCode() == 401) {
                    final Message msg = new Message();
                    msg.what = RECORD_GET_FAILED;
                    msg.arg1 = 401;
                    mHandler.sendMessage(msg);
                    return;
                }
                if (isEmptyData) {
                    mHandler.sendEmptyMessage(RECORD_GET_FAILED);
                    return;
                }
                mRecordInfoList = bean.getData();
                calculateTotalTime();
                mHandler.sendEmptyMessage(RECORD_GET_SUCCEED);
            }

            @Override
            public void onFailure(Call<RecordBean> call, Throwable t) {
                MyLogUtils.e("获取回放记录出错", t);
                mHandler.sendEmptyMessage(RECORD_GET_FAILED);
            }
        });
    }

    /**
     * 计算录像的总时间
     */
    private void calculateTotalTime(){
        if (mRecordInfoList != null && mRecordInfoList.getPaths() != null) {
            for (RecordInfo path : mRecordInfoList.getPaths()) {
                mRecordTotalTime += path.getTime_len();
            }
        }
    }


    /***********************
     * 本地副本保存
     * 拍照----余浩
     */
    public synchronized void savePicTranscript() {
        MyLogUtils.d("savePicTranscript()");
        try {
            if (!FileUtil.isFileExist(StorageConfig.OUT_IMAGE_PATH)) {
                FileUtil.createFolder(StorageConfig.OUT_IMAGE_PATH);
            }
            String fileNamePic = System.currentTimeMillis() + ".png";
            Bitmap bitmap = mSurface.getBitmap(1280, 720);
            new SavePicThread(bitmap, fileNamePic).start();
        } catch (Exception e) {
            mPlaybackView.showToast("图片保存失败");
            e.printStackTrace();
        }
    }

    /**
     * 开始录像时，先下载视频到本地，然后再对视频进行裁剪
     */
    public void startRecord() {
        File file = new File( StorageConfig.OUT_IMAGE_PATH, "temp1.mp4");
        URL url = null;
        try {
            url = new URL(mCurrentPlayUrl);
        } catch (MalformedURLException e) {
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
                MyLogUtils.d("SavePicThread run() bitmap is not null");
                try {
                    String destPath = StorageConfig.OUT_IMAGE_PATH + fileNamePic;
                    MyLogUtils.d("SavePicThread run() filePath = " + destPath);
                    FileOutputStream fileOutputStream = new FileOutputStream(destPath);
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 99, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    mHandler.sendEmptyMessage(SAVE_PICTURE_SUCCEED);
                } catch (Exception e) {
                    mHandler.sendEmptyMessage(SAVE_PICTURE_FAILED);
                    e.printStackTrace();
                }
            } else {
                mHandler.sendEmptyMessage(SAVE_PICTURE_FAILED);
            }
        }
    }
}
