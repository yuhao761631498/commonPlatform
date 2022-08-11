package com.gdu.command.ui.cases.dealt;


import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.VideoDecoder;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.base.BaseRVAdapter;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.command.R;
import com.gdu.command.ui.cases.deal.CommentsActivity;
import com.gdu.command.views.picpreview.PicturePreviewActivity;
import com.gdu.model.cases.CaseStatus;
import com.gdu.model.cases.CommentInfo;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.config.GlobalVariable;
import com.gyf.immersionbar.ImmersionBar;

import java.security.MessageDigest;
import java.util.List;

/**
 * 已归档案件
 */
public class DealtCaseActivity extends BaseActivity<DealtCasePresenter>
        implements View.OnClickListener, IDealtCaseView {

    private ImageView mBackImageView;
    private ImageView mConfirmCaseImageView;
    private TextView mCaseIdTextView;
    private TextView mCaseDescTextView;
    private TextView mCaseSrcTextView;
    private TextView mCaseReporterTextView;
    private TextView mCaseReportTimeTextView;
    private TextView mCaseReportPhoneTextView;
    private TextView mCaseLocationTextView;
    private LinearLayout mCaseCommentsLayout;
    private LinearLayout mCaseHandleLayout;
    private EditText mCaseCommentsEditText;
    private TextView mCaseStatusTextView;

    private RecyclerView mCasePictureGridView;
//    private CasePictureAdapter mCasePictureAdapter;
    private BaseRVAdapter<String> mCasePictureAdapter;

    private RecyclerView mCaseVideoGridView;
//    private CaseVideoAdapter mCaseVideoAdapter;
    private BaseRVAdapter<String> mCaseVideoAdapter;

    private ImageView mVideoCoverImageView;
    private RelativeLayout mVideoLayout;
    private VideoView mVideoView;
    private ImageView mCasePhotoView;
    private ImageView mVideoBackImageView;

    private RecyclerView mCommentsListView;
    private RelativeLayout mCaseRecordsLayout;
//    private CaseCommentsAdapter mCaseCommentsAdapter;
    private BaseRVAdapter<CommentInfo> mCaseCommentsAdapter;
    private TextView mLookMoreCommentsTextView;

    private LinearLayout mLoadingLayout;

    private int mCurrentIssueStatus;
    private List<CommentInfo> mCommentInfoList;
    private List<String> mVideoList;
    private List<String> mImageList;

    private ImageView photoIv;
    /** 当前顶部控件显示图片的Url */
    private String curPicShowUrl = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dealt_case;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
        initViews();
        initData();
        initListener();
    }

    private void initListener() {
        photoIv.setOnClickListener(this);
        mBackImageView.setOnClickListener(this);
        mConfirmCaseImageView.setOnClickListener(this);
        mLookMoreCommentsTextView.setOnClickListener(this);
        mVideoBackImageView.setOnClickListener(this);
        mCaseRecordsLayout.setOnClickListener(this);
        mVideoView.setOnInfoListener((mediaPlayer, what, extra) -> {
            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {

            } else {
                mLoadingLayout.setVisibility(View.GONE);
            }
            return true;
        });
    }

    private void initViews() {
        mBackImageView = findViewById(R.id.back_imageview);
        mConfirmCaseImageView = findViewById(R.id.confirm_case_imageview);
        mCaseIdTextView = findViewById(R.id.case_id_textview);
        mCaseDescTextView = findViewById(R.id.case_desc_textview);
        mCaseSrcTextView = findViewById(R.id.case_src_textview);
        mCaseReporterTextView = findViewById(R.id.case_reporter_textview);
        mCaseReportTimeTextView = findViewById(R.id.case_report_time_textview);
        mCaseReportPhoneTextView = findViewById(R.id.case_report_phone_textview);
        mCaseLocationTextView = findViewById(R.id.case_location_textview);
        mVideoCoverImageView = findViewById(R.id.video_cover_imageview);
        mCasePictureGridView = findViewById(R.id.case_picture_gridview);
        mCaseVideoGridView = findViewById(R.id.case_video_gridview);

        mVideoBackImageView = findViewById(R.id.video_back_imageview);
        mVideoLayout = findViewById(R.id.video_layout);
        mVideoView = findViewById(R.id.video_view);
        mCasePhotoView = findViewById(R.id.case_photo_imageview);

        mCaseCommentsLayout = findViewById(R.id.case_comments_layout);
        mCaseCommentsEditText = findViewById(R.id.case_comments_edittext);
        mCaseHandleLayout = findViewById(R.id.handle_case_layout);
        mCaseStatusTextView = findViewById(R.id.case_status_textview);

        mCommentsListView = findViewById(R.id.case_comments_listview);
        mCaseRecordsLayout = findViewById(R.id.case_records_layout);
        mCaseRecordsLayout.setClickable(false);
        mLookMoreCommentsTextView = findViewById(R.id.look_more_comments_textview);

        mLoadingLayout = findViewById(R.id.loading_layout);
        photoIv = findViewById(R.id.iv_photoContainer);

        getPresenter().setView(this);
        initListView();
    }

    private void initListView() {
//        mCasePictureAdapter = new CasePictureAdapter(this);
        mCasePictureAdapter = new BaseRVAdapter<String>(R.layout.item_case_picture) {
            @Override
            public void onBindVH(BaseViewHolder holder, String data, int position) {
                holder.getView(R.id.iv_delBtn).setVisibility(View.GONE);
                if (CommonUtils.isEmptyString(data)) {
                    return;
                }
                final ImageView contentIv = holder.getView(R.id.case_imageview);
                if (data.equals("add")) {
                    contentIv.setImageResource(R.mipmap.icon_add_picture);
                } else {
                    String path = data;
                    if (!data.contains("http:")) {
                        path = "file://" + data;
                    }
                    MyImageLoadUtils.loadImage(DealtCaseActivity.this, path, 0,
                            contentIv);
                }
            }
        };
        mCasePictureGridView.setAdapter(mCasePictureAdapter);
        mCasePictureAdapter.setOnItemClickListener((adapter, view, position) -> {
            mVideoLayout.setVisibility(View.VISIBLE);
            mCasePhotoView.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.GONE);
            mLoadingLayout.setVisibility(View.GONE);
            MyImageLoadUtils.loadImage(DealtCaseActivity.this, mImageList.get(position),
                    0, mCasePhotoView);
        });

//        mCaseCommentsAdapter = new CaseCommentsAdapter(mContext);
        mCaseCommentsAdapter = new BaseRVAdapter<CommentInfo>(R.layout.item_case_comment) {
            @Override
            public void onBindVH(BaseViewHolder holder, CommentInfo data, int position) {
                String name = data.getDeptName();
                if (CommonUtils.isEmptyString(name)) {
                    name = "执法员" + position;
                }
                holder.setText(R.id.name_textview, name);
                holder.setText(R.id.time_textview, data.getCheckTime());
                holder.setText(R.id.comments_textview, data.getCheckComment());
            }
        };
        mCommentsListView.setAdapter(mCaseCommentsAdapter);

//        mCaseVideoAdapter = new CaseVideoAdapter(this);
        mCaseVideoAdapter = new BaseRVAdapter<String>(R.layout.item_case_video) {
            @Override
            public void onBindVH(BaseViewHolder holder, String data, int position) {
                if (CommonUtils.isEmptyString(data)) {
                    return;
                }
                final String casePath = data;
                String path = casePath;
                if (!casePath.contains("http:")) {
                    path = "file://" + casePath;
                }
                loadCover(holder.getView(R.id.case_imageview), path);
            }
        };
        mCaseVideoGridView.setAdapter(mCaseVideoAdapter);
        mCaseVideoAdapter.setOnItemClickListener((adapter, view, position) -> {
            mVideoLayout.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.VISIBLE);
            mCasePhotoView.setVisibility(View.GONE);
            mVideoView.setVideoPath(mVideoList.get(position));
            mVideoView.start();
        });
    }

    private void initData() {
        IssueBean issueBean = GlobalVariable.sCurrentIssueBean;
        Intent intent = getIntent();
        getPresenter().loadDatas(issueBean.getId());
        mCurrentIssueStatus = intent.getIntExtra(GlobalVariable.EXTRA_CASE_STATUS, -1);
        Uri data = intent.getData();
        if (data != null && mCurrentIssueStatus == -1) {
            String caseStatus = data.getQueryParameter("caseStatus");
            mCurrentIssueStatus =  Integer.parseInt(caseStatus);
        }

        String snStr = "";
        if (!CommonUtils.isEmptyString(issueBean.getCaseNo())) {
            snStr = issueBean.getCaseNo();
        }
        if (CommonUtils.isEmptyString(snStr) && !CommonUtils.isEmptyString(issueBean.getId())) {
            snStr = issueBean.getId();
        }
        if (CommonUtils.isEmptyString(snStr)) {
            snStr = "";
        }
        mCaseIdTextView.setText(snStr);

        mCaseDescTextView.setText(issueBean.getCaseDesc());
        mCaseSrcTextView.setText(issueBean.getDeptName());
        mCaseStatusTextView.setText("已处置");
        String name = issueBean.getReportMan();
        if (CommonUtils.isEmptyString(name)) {
            name = issueBean.getReport_man();
        }
        mCaseReporterTextView.setText(name);
        mCaseReportTimeTextView.setText(issueBean.getReportTime());
        mCaseReportPhoneTextView.setText(issueBean.getReportTel());
        mCaseLocationTextView.setText(issueBean.getReportAddr());

        curPicShowUrl = CommonUtils.getSinglePicRealPath(issueBean.getCaseFile());
        MyImageLoadUtils.loadImage(DealtCaseActivity.this,
                curPicShowUrl,
                R.mipmap.icon_case_default1, photoIv);

        if (mCurrentIssueStatus == CaseStatus.WAIT_COMMENT.getKey()) {
            mCaseCommentsLayout.setVisibility(View.VISIBLE);
            mCaseHandleLayout.setVisibility(View.GONE);
        } else {
            mCaseCommentsLayout.setVisibility(View.GONE);
            mCaseHandleLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载第四秒的帧数作为封面
     *  url就是视频的地址
     */
    public void loadCover(ImageView imageView, String url) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        RequestOptions requestOptions = RequestOptions.frameOf(1 * 1000 * 1000);
        requestOptions.set(VideoDecoder.FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
        requestOptions.transform(new BitmapTransformation() {
            @Override
            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                return toTransform;
            }

            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {
                try {
                    messageDigest.update((mContext.getPackageName() + "RotateTransform").getBytes("utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Glide.with(mContext).load(url).apply(requestOptions).into(imageView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.back_imageview:
                finish();
                break;

            case R.id.confirm_case_imageview:

                break;

            case R.id.look_more_comments_textview:
                GlobalVariable.sCommentInfoList = mCommentInfoList;
                Intent intent = new Intent(DealtCaseActivity.this, CommentsActivity.class);
                startActivity(intent);
                break;

            case R.id.video_back_imageview:
                mVideoLayout.setVisibility(View.GONE);
                break;

            case R.id.case_records_layout:
                Intent recordIntent = new Intent(DealtCaseActivity.this, DealtRecordsActivity.class);
                startActivity(recordIntent);
                break;

            case R.id.iv_photoContainer:
                if (GlobalVariable.sCurrentIssueBean == null
                        || CommonUtils.isEmptyString(curPicShowUrl)) {
                    return;
                }
                final Bundle bundle1 = new Bundle();
                bundle1.putString(MyConstants.SEND_PIC_PATH_KEY, curPicShowUrl);
                final Intent mIntent = new Intent(this, PicturePreviewActivity.class);
                mIntent.putExtras(bundle1);
                startActivity(mIntent);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void showComments(List<CommentInfo> commentInfoList) {
        mCommentInfoList = commentInfoList;
        List<CommentInfo> tmp;
        if (commentInfoList.size() > 2) {
            mLookMoreCommentsTextView.setVisibility(View.VISIBLE);
            tmp = commentInfoList.subList(0, 1);
        } else {
            mLookMoreCommentsTextView.setVisibility(View.GONE);
            tmp = commentInfoList;
        }
        mCaseCommentsAdapter.setList(tmp);
    }

    @Override
    public void showDetail(IssueBean issueBean) {
       String attachment = issueBean.getDisFinishAttachment();
        if (CommonUtils.isEmptyString(attachment)) {
            return;
        }
    }

    @Override
    public void showPictures(List<String> pictures) {
        mImageList = pictures;
        mCasePictureAdapter.setList(pictures);
    }

    @Override
    public void showVideos(List<String> videos) {
        mVideoList = videos;
        mCaseVideoAdapter.setList(videos);
    }

    @Override
    public void showRecords() {
        mCaseRecordsLayout.setClickable(true);
    }
}
