package com.gdu.command.ui.alarm.dialog;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.gdu.baselibrary.base.BaseRVAdapter;
import com.gdu.baselibrary.dialog.BottomPushDialog;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.command.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 预警详情补充信息弹窗
 * @author wixche
 */
public class AlarmAdditionalDialog extends BottomPushDialog {
    private TextView cancelBtn, confirmBtn;
    private EditText et_remarkContent;
    private RecyclerView rv_picContent, rv_videoContent;

    private List<String> mPicPathList = new ArrayList<>();
    private BaseRVAdapter mPicRvAdapter;

    private List<String> mVideoPathList = new ArrayList<>();
    private BaseRVAdapter mVideoRvAdapter;

    private Activity mActivity;

    private IAdditionalCallback mAdditionalCallback;

    public AlarmAdditionalDialog(@NonNull Activity context) {
        super(context);
        mActivity = context;
        setContentView(R.layout.dialog_alarm_additional);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        cancelBtn = findViewById(R.id.tv_cancelBtn);
        confirmBtn = findViewById(R.id.tv_confirmBtn);
        et_remarkContent = findViewById(R.id.et_remarkContent);
        rv_picContent = findViewById(R.id.rv_picContent);
        rv_videoContent = findViewById(R.id.rv_videoContent);
    }

    private void initData() {
        initPicAdapter();
        initVideoAdapter();
    }

    private void initListener() {
        cancelBtn.setOnClickListener(v -> {
            dismiss();
        });
        confirmBtn.setOnClickListener(v -> {
            final String remarkStr = et_remarkContent.getText().toString().trim();
            if (CommonUtils.isEmptyString(remarkStr)) {
                ToastUtil.s("请输入补充信息");
                return;
            }
            final List<String> validatePicList = new ArrayList<>();
            for (int i = 0; i < mPicPathList.size(); i++) {
                if (!"add".equals(mPicPathList.get(i))) {
                    CommonUtils.listAddAvoidNull(validatePicList, mPicPathList.get(i));
                }
            }

            String videoPath = "";
            for (int i = 0; i < mVideoPathList.size(); i++) {
                if (!"add".equals(mVideoPathList.get(i))) {
                    videoPath = mVideoPathList.get(i);
                    break;
                }
            }

            if (mAdditionalCallback != null) {
                mAdditionalCallback.callbackParame(validatePicList, videoPath, remarkStr);
            }

            initViewData();

            dismiss();
        });
    }

    private void initViewData() {
        et_remarkContent.setText("");

        mPicPathList.clear();
        mPicPathList.add("add");
        mPicRvAdapter.setList(mPicPathList);

        mVideoPathList.clear();
        mVideoPathList.add("add");
        mVideoRvAdapter.setList(mVideoPathList);
    }

    private void initPicAdapter() {
        mPicPathList.clear();
        mPicPathList.add("add");

        mPicRvAdapter = new BaseRVAdapter<String>(R.layout.item_case_picture) {
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
                    MyImageLoadUtils.loadImage(getContext(), path, 0,
                            contentIv);
                }
            }
        };

        mPicRvAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mPicPathList.get(position).equals("add")) {
                if (mPicPathList.size() == 4) {
                    ToastUtil.s("最多上传3张照片");
                    return;
                }
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(false)  //设置是否单选
                        .canPreview(true) //是否可以预览图片，默认为true
                        .setContainVideo(false)
                        .setContainPhoto(true)
                        .setMaxSelectCount(4 - mPicPathList.size())
                        .start(mActivity, MyConstants.REQUEST_PIC_CODE); // 打开相册
            }
        });
        mPicRvAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_delBtn) {
                for (int i = 0; i < mPicPathList.size(); i++) {
                    if (mPicPathList.get(position).equals(mPicPathList.get(i))) {
                        mPicPathList.remove(position);
                        break;
                    }
                }
                mPicRvAdapter.setList(mPicPathList);
            }
        });
        mPicRvAdapter.addChildClickViewIds(R.id.iv_delBtn);
        mPicRvAdapter.setList(mPicPathList);
        rv_picContent.setAdapter(mPicRvAdapter);
    }

    private void initVideoAdapter() {
        mVideoPathList.clear();
        mVideoPathList.add("add");

        mVideoRvAdapter = new BaseRVAdapter<String>(R.layout.item_case_picture) {
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
                    MyImageLoadUtils.loadImage(getContext(), path, 0,
                            contentIv);
                }
            }
        };

        mVideoRvAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mVideoPathList.get(position).equals("add")) {
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(true)  //设置是否单选
                        .canPreview(false) //是否可以预览图片，默认为true
                        .setContainVideo(true)
                        .setContainPhoto(false)
                        .setMaxSelectCount(1)
                        .start(mActivity, MyConstants.REQUEST_VIDEO_CODE); // 打开相册
            }
        });
        mVideoRvAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_delBtn) {
                for (int i = 0; i < mVideoPathList.size(); i++) {
                    if (mVideoPathList.get(position).equals(mVideoPathList.get(i))) {
                        mVideoPathList.remove(position);
                        break;
                    }
                }
                if (mVideoPathList.size() == 0) {
                    mVideoPathList.add("add");
                }
                mVideoRvAdapter.setList(mVideoPathList);
            }
        });
        mVideoRvAdapter.addChildClickViewIds(R.id.iv_delBtn);
        mVideoRvAdapter.setList(mVideoPathList);
        rv_videoContent.setAdapter(mVideoRvAdapter);
    }

    public void updatePicData(List<String> picData) {
        if (CommonUtils.isEmptyList(picData)) {
            return;
        }
        CommonUtils.listAddAllAvoidNPE(mPicPathList, picData);
        mPicRvAdapter.setList(mPicPathList);
    }

    public void updateVideoData(String videoPath) {
        if (CommonUtils.isEmptyString(videoPath)) {
            return;
        }
        mVideoPathList.clear();
        CommonUtils.listAddAvoidNull(mVideoPathList, videoPath);
        mVideoRvAdapter.setList(mVideoPathList);
    }

    public void setAdditionalCallback(IAdditionalCallback additionalCallback) {
        mAdditionalCallback = additionalCallback;
    }

    public interface IAdditionalCallback {
        void callbackParame(List<String> uploadPics, String uploadVideo, String remark);
    }
}
