package com.gdu.command.ui.cases.dealt;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.command.R;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.config.UrlConfig;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 案件处置记录
 */
public class DealtRecordsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackImageView;
    private TextView mAlarmManTextView;
    private TextView mCaseReportTimeTextView;
    private TextView mCommentsStatusTextView;
    private ListView mCommentsListView;
    private ListView mConveneListView;
    private ListView mDealtListView;
    private TextView mConvenesStatusTextView;
    private TextView mDealStatusTextView;
    private TextView mFinishStatusTextView;
    private TextView mFinishTimeTextView;
    private TextView mFinishManTextView;
    private TextView mFinishInfoTextView;
    private RecordCommentAdapter mRecordCommentAdapter;
    private RecordConveneAdapter mRecordConveneAdapter;
    private RecordDealtAdapter mRecordDealtAdapter;

    private GridView mAccessoryGridView;
    private CaseAccessoryAdapter mAccessoryAdapter;
    private List<String> mAccessoryList;

    private RelativeLayout mVideoLayout;
    private ImageView      mPhotoPreviewImageView;
    private VideoView      mVideoView;
    private LinearLayout   mLoadingLayout;
    private ImageView mBackVideoImageView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dealt_records;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
        initView();
        initAdapter();
        initListener();
        initData();
    }

    private void initData() {
        if (GlobalVariable.sCaseRecords != null) {
            for (Map<String, Object> caseRecord : GlobalVariable.sCaseRecords) {
                double dispositionNode = (double) caseRecord.get("dispositionNode");
                int node = (int) dispositionNode;
               switch (node){
                   case 0:
                        showReceive(caseRecord);
                       break;
                   case 1:
                        showComments(caseRecord);
                       break;
                   case 2:
                       showConvenes(caseRecord);
                       break;
                   case 3:
                       showDealt(caseRecord);
                       break;
                   case 4:
                        showFinish(caseRecord);
                       break;
                   default:
                       break;
               }
            }
        }
    }

    private void initAdapter() {
        mRecordCommentAdapter = new RecordCommentAdapter(this);
        mCommentsListView.setAdapter(mRecordCommentAdapter);
        mRecordConveneAdapter = new RecordConveneAdapter(this);
        mConveneListView.setAdapter(mRecordConveneAdapter);
        mRecordDealtAdapter = new RecordDealtAdapter(this);
        mDealtListView.setAdapter(mRecordDealtAdapter);
        mAccessoryAdapter = new CaseAccessoryAdapter(this);
        mAccessoryGridView.setAdapter(mAccessoryAdapter);
    }

    private void initView() {
        mBackImageView = findViewById(R.id.back_imageview);
        mAlarmManTextView =  findViewById(R.id.alarm_man_textview);
        mCaseReportTimeTextView =  findViewById(R.id.case_report_time_textview);
        mCommentsListView = findViewById(R.id.comments_listview);
        mConveneListView = findViewById(R.id.convene_listview);
        mDealtListView = findViewById(R.id.deal_listview);
        mCommentsStatusTextView = findViewById(R.id.comments_status_textview);
        mConvenesStatusTextView = findViewById(R.id.convenes_status_textview);
        mDealStatusTextView = findViewById(R.id.dealt_status_textview);
        mFinishStatusTextView = findViewById(R.id.finish_status_textview);
        mAccessoryGridView = findViewById(R.id.accessory_gridview);
        mFinishInfoTextView = findViewById(R.id.finish_info_textview);
        mFinishManTextView = findViewById(R.id.finish_man_textview);
        mFinishTimeTextView = findViewById(R.id.finish_time_textview);

        mVideoLayout = findViewById(R.id.video_layout);
        mPhotoPreviewImageView = findViewById(R.id.case_photo_imageview);
        mVideoView = findViewById(R.id.video_view);
        mLoadingLayout = findViewById(R.id.loading_layout);
        mBackVideoImageView = findViewById(R.id.video_back_imageview);
    }

    private void initListener() {
        mBackImageView.setOnClickListener(this);
        mBackVideoImageView.setOnClickListener(this);
        mAccessoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                showAccessory(mAccessoryList.get(position));
            }
        });
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
                if (what != MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    mLoadingLayout.setVisibility(View.GONE);
                }
                return true;
            }
        });
    }

    /**
     * 显示图片或者视频
     * @param accessory
     */
    private void showAccessory(String accessory){
        if (accessory.contains("mp4")) {
            showVideo(accessory);
        } else {
            showPhoto(accessory);
        }
    }

    /**
     * 显示视频
     * @param path
     */
    private void showVideo(String path){
        mVideoLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mVideoView.setVisibility(View.VISIBLE);
        mPhotoPreviewImageView.setVisibility(View.GONE);
        mVideoView.setVideoPath(path);
        mVideoView.start();
    }

    /**
     * 显示图片
     */
    private void showPhoto(String path){
        mVideoLayout.setVisibility(View.VISIBLE);
        mPhotoPreviewImageView.setVisibility(View.VISIBLE);
        mVideoView.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        MyImageLoadUtils.loadImage(DealtRecordsActivity.this, path,
                0, mPhotoPreviewImageView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_imageview:
                finish();
                break;
            case R.id.video_back_imageview:
                mVideoLayout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * 显示受理信息
     * @param receiveInfo
     */
    public void showReceive(Map<String, Object> receiveInfo) {
        String createTime = (String) receiveInfo.get("createTime");
        Map<String,String> content = (Map<String, String>) receiveInfo.get("content");
        String alarmMan = content.get("receivingAlarmMan");
        mAlarmManTextView.setText(alarmMan);
        mCaseReportTimeTextView.setText(createTime);
    }

    /**
     * 显示批示信息
     * @param comments
     */
    public void showComments(Map<String, Object> comments) {
        double dispositionStatus = (double) comments.get("dispositionStatus");
        String status = "(进行中)";
        if (dispositionStatus == 1) {
            status = "(已完成)";
        }
        mCommentsStatusTextView.setText(status);
        List<Map<String,String>> contents = (List<Map<String, String>>) comments.get("content");
        mRecordCommentAdapter.setData(contents);
    }

    /**
     * 显示召集信息
     * @param convenes
     */
    public void showConvenes(Map<String, Object> convenes) {
        double dispositionStatus = (double) convenes.get("dispositionStatus");
        String status = "(进行中)";
        if (dispositionStatus == 1) {
            status = "(已完成)";
        }
        mConvenesStatusTextView.setText(status);
        List<Map<String,String>> contents = (List<Map<String, String>>) convenes.get("content");
        mRecordConveneAdapter.setData(contents);
    }

    /**
     * 显示处置信息
     * @param deals
     */
    public void showDealt(Map<String, Object> deals) {
        double dispositionStatus = (double) deals.get("dispositionStatus");
        String status = "(进行中)";
        if (dispositionStatus == 1) {
            status = "(已完成)";
        }
        mDealStatusTextView.setText(status);
        List<Map<String,String>> contents = (List<Map<String, String>>) deals.get("content");
        mRecordDealtAdapter.setData(contents);
    }

    /**
     * 显示回告信息
     * @param finish
     */
    public void showFinish(Map<String, Object> finish) {
        if (finish == null) {
            return;
        }
        double dispositionStatus = (double) finish.get("dispositionStatus");
        String status = "(进行中)";
        if (dispositionStatus == 1) {
            status = "(已完成)";
        }
        mFinishStatusTextView.setText(status);
        Map<String, String> contents = (Map<String, String>) finish.get("content");
        final String attachments = contents.get("disFinishAttachment");
        if (CommonUtils.isEmptyString(attachments)) {
            return;
        }
        String[] paths = attachments.split(",");
        if (paths != null && paths.length > 0) {
            List<String> realPath = new ArrayList<>();
            for (String path : paths) {
                realPath.add(UrlConfig.ImgIp + path);
            }
            mAccessoryAdapter.setData(realPath);
            mAccessoryList = realPath;
        }
        mFinishInfoTextView.setText(contents.get("dispositionRecord"));
        mFinishManTextView.setText(contents.get("employeeName"));
        mFinishTimeTextView.setText(contents.get("dispositionTime"));
    }
}
