package com.gdu.command.ui.cases;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps2d.model.LatLng;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.base.BaseRVAdapter;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.command.R;
import com.gdu.command.event.DealSiteEvent;
import com.gdu.command.ui.secondmap.ManualLocationActivity;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class SiteDealActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_deal_verbal;
    private TextView tv_deal_administrative;
    private TextView tv_deal_other;
    private ImageView backIv;
    private TextView titleTv;

    private ConstraintLayout inc_alarm_location_again;
    private TextView alarmLocationLabelTv;
    private EditText alarmLocationContentEt;
    private ImageView alarmLocationRightIv;
    private TextView alarmLocationImportantTip;

    private ConstraintLayout inc_alarm_remark;
    private TextView remarkLabelTv;
    private EditText remarkContentEt;
    private ImageView remarkRightIv;
    private TextView remarkImportantTip;
    private RecyclerView picContentRv;
    private RecyclerView videoContentRv;

    private List<String> mPicturePathList = new ArrayList<>();
    private List<String> mRealImages;
    private BaseRVAdapter<String> mCasePictureAdapter;

    private List<String> mVideoPathList = new ArrayList<>();
    private List<String> mRealVideos;
    private BaseRVAdapter<String> mCaseVideoAdapter;

    private LatLng addressCoordinate;
    private TextView tv_cancel_deal;
    private TextView tv_site_deal;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_site_deal;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();

        initView();

        initAdapter();

        initListener();
    }

    private void initListener() {
        backIv.setOnClickListener(this);

        tv_deal_verbal.setOnClickListener(this);
        tv_deal_administrative.setOnClickListener(this);
        tv_deal_other.setOnClickListener(this);

        tv_cancel_deal.setOnClickListener(this);
        tv_site_deal.setOnClickListener(this);

        alarmLocationRightIv.setOnClickListener(v -> {
            openActivityForResult(ManualLocationActivity.class, MyConstants.SHOW_MANUAL_LOCATION_REQUEST_CODE);
        });
    }

    private void initAdapter() {
        mPicturePathList.clear();
        mPicturePathList.add("add");

        mVideoPathList.clear();
        mVideoPathList.add("add");

        mCasePictureAdapter = new BaseRVAdapter<String>(R.layout.item_case_picture) {
            @Override
            public void onBindVH(BaseViewHolder holder, String data, int position) {
                if (CommonUtils.isEmptyString(data)) {
                    return;
                }
                final ImageView contentIv = holder.getView(R.id.case_imageview);
                if (data.equals("add")) {
                    holder.getView(R.id.iv_delBtn).setVisibility(View.GONE);
                    contentIv.setImageResource(R.mipmap.icon_add_picture);
                } else {
                    holder.getView(R.id.iv_delBtn).setVisibility(View.VISIBLE);
                    String path = data;
                    if (!data.contains("http:")) {
                        path = "file://" + data;
                    }
                    MyImageLoadUtils.loadImage(SiteDealActivity.this, path, 0,
                            contentIv);
                }
            }
        };

        mCaseVideoAdapter = new BaseRVAdapter<String>(R.layout.item_case_picture) {
            @Override
            public void onBindVH(BaseViewHolder holder, String data, int position) {
                if (CommonUtils.isEmptyString(data)) {
                    return;
                }
                final ImageView contentIv = holder.getView(R.id.case_imageview);
                if (data.equals("add")) {
                    holder.getView(R.id.iv_delBtn).setVisibility(View.GONE);
                    contentIv.setImageResource(R.mipmap.icon_add_picture);
                } else {
                    holder.getView(R.id.iv_delBtn).setVisibility(View.VISIBLE);
                    String path = data;
                    if (!data.contains("http:")) {
                        path = "file://" + data;
                    }
                    MyImageLoadUtils.loadImage(SiteDealActivity.this, path, 0,
                            contentIv);
                }
            }
        };

        mCasePictureAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mPicturePathList.get(position).equals("add")) {
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(false)  //设置是否单选
                        .canPreview(true) //是否可以预览图片，默认为true
                        .setContainVideo(false)
                        .setContainPhoto(true)
                        .setMaxSelectCount(3 - mPicturePathList.size())
                        .start((Activity) mContext, MyConstants.REQUEST_PIC_CODE); // 打开相册
            }
        });

        mCaseVideoAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mVideoPathList.get(position).equals("add")) {
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(false)  //设置是否单选
                        .canPreview(true) //是否可以预览图片，默认为true
                        .setContainVideo(true)
                        .setContainPhoto(false)
                        .setMaxSelectCount(2 - mVideoPathList.size())
                        .start((Activity) mContext, MyConstants.REQUEST_VIDEO_CODE); // 打开相册
            }
        });

        mCasePictureAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_delBtn) {
                for (int i = 0; i < mPicturePathList.size(); i++) {
                    if (mPicturePathList.get(position).equals(mPicturePathList.get(i))) {
                        mPicturePathList.remove(position);
                        break;
                    }
                }
                mCasePictureAdapter.setList(mPicturePathList);
            }
        });

        mCaseVideoAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_delBtn) {
                for (int i = 0; i < mVideoPathList.size(); i++) {
                    if (mVideoPathList.get(position).equals(mVideoPathList.get(i))) {
                        mVideoPathList.remove(position);
                        break;
                    }
                }
                mCaseVideoAdapter.setList(mVideoPathList);
            }
        });

        mCasePictureAdapter.addChildClickViewIds(R.id.iv_delBtn);
        mCasePictureAdapter.setList(mPicturePathList);
        picContentRv.setAdapter(mCasePictureAdapter);

        mCaseVideoAdapter.addChildClickViewIds(R.id.iv_delBtn);
        mCaseVideoAdapter.setList(mVideoPathList);
        videoContentRv.setAdapter(mCaseVideoAdapter);
    }

    private void initView() {
        backIv = findViewById(R.id.iv_left_back);
        titleTv = findViewById(R.id.tv_left_title_name);
        titleTv.setText("人工告警");

        tv_deal_verbal = findViewById(R.id.tv_deal_verbal);  //口头警告
        tv_deal_verbal.setSelected(true);
        tv_deal_administrative = findViewById(R.id.tv_deal_administrative);  //行政处罚
        tv_deal_other = findViewById(R.id.tv_deal_other);  //其他

        TextView tv_deal_label = findViewById(R.id.tv_deal_label);
        tv_deal_label.setText("处理结果：");

        tv_cancel_deal = findViewById(R.id.tv_cancel_deal);
        tv_site_deal = findViewById(R.id.tv_site_deal);

        initAgainLocationView();  //预警地址

        initRemarkView();  //备注信息

        picContentRv = findViewById(R.id.rv_pic_content);

        videoContentRv = findViewById(R.id.rv_video_content);
    }

    private void initRemarkView() {
        inc_alarm_remark = findViewById(R.id.inc_alarm_remark);
        remarkLabelTv = inc_alarm_remark.findViewById(R.id.tv_label);
        remarkContentEt = inc_alarm_remark.findViewById(R.id.et_content);
        remarkRightIv = inc_alarm_remark.findViewById(R.id.iv_icon);
        remarkImportantTip = inc_alarm_remark.findViewById(R.id.tv_importantTip);
        initItemView(remarkLabelTv, "备注信息：",
                remarkContentEt, "请输入", remarkRightIv,
                0, remarkImportantTip, false);
    }

    private void initAgainLocationView() {
        inc_alarm_location_again = findViewById(R.id.inc_alarm_location_again);
        alarmLocationLabelTv = inc_alarm_location_again.findViewById(R.id.tv_label);
        alarmLocationContentEt = inc_alarm_location_again.findViewById(R.id.et_content);
        alarmLocationRightIv = inc_alarm_location_again.findViewById(R.id.iv_icon);
        alarmLocationImportantTip = inc_alarm_location_again.findViewById(R.id.tv_importantTip);
        initItemView(alarmLocationLabelTv, "预警地址：",
                alarmLocationContentEt, "请选择", alarmLocationRightIv,
                R.mipmap.icon_location_select, alarmLocationImportantTip, false);
    }


    private void initItemView(TextView labelTv, String labelContent, EditText contentEt, String hint,
                              ImageView rightIv, int rightPicId, TextView importantTip, boolean isShow) {
        labelTv.setText(labelContent);
        contentEt.setHint(hint);
        importantTip.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (rightIv != null) {
            rightIv.setVisibility(rightPicId != 0 ? View.VISIBLE : View.GONE);
            if (rightPicId != 0) {
                rightIv.setImageResource(rightPicId);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final boolean isHaveMapData = requestCode == MyConstants.SHOW_MANUAL_LOCATION_REQUEST_CODE
                && resultCode == MyConstants.RESULT_ADDRESS_INTENT
                && data != null && data.getExtras() != null;
        if (requestCode == MyConstants.REQUEST_PIC_CODE && data != null) {
            //获取选择器返回的数据
            mRealImages = data.getStringArrayListExtra(
                    ImageSelector.SELECT_RESULT);
            CommonUtils.listAddAllAvoidNPE(mPicturePathList, mRealImages);
            mCasePictureAdapter.setList(mPicturePathList);
        } else if (requestCode == MyConstants.REQUEST_VIDEO_CODE && data != null) {
            //获取选择器返回的数据
            mRealVideos = data.getStringArrayListExtra(
                    ImageSelector.SELECT_RESULT);
            CommonUtils.listAddAllAvoidNPE(mVideoPathList, mRealVideos);
            mCaseVideoAdapter.setList(mVideoPathList);
        } else if (isHaveMapData) {
            final Bundle mBundle = data.getExtras();
            final String addressStr = mBundle.getString(MyConstants.RESULT_ADDRESS, "");
            addressCoordinate = mBundle.getParcelable(MyConstants.RESULT_ADDRESS_COORDINATE);

            Log.e("yuhao", "addressCoordinate: " + addressCoordinate);

            alarmLocationContentEt.setText(addressStr);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_back:
            case R.id.tv_cancel_deal:
                finish();
                break;

            case R.id.tv_deal_verbal:
                tv_deal_verbal.setSelected(true);
                tv_deal_administrative.setSelected(false);
                tv_deal_other.setSelected(false);
                break;

            case R.id.tv_deal_administrative:
                tv_deal_verbal.setSelected(false);
                tv_deal_administrative.setSelected(true);
                tv_deal_other.setSelected(false);
                break;

            case R.id.tv_deal_other:
                tv_deal_verbal.setSelected(false);
                tv_deal_administrative.setSelected(false);
                tv_deal_other.setSelected(true);
                break;

            case R.id.tv_site_deal:
                EventBus.getDefault().post(new DealSiteEvent());
                break;
        }
    }
}