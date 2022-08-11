package com.gdu.command.ui.alarm.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.gdu.baselibrary.base.BaseRVAdapter;
import com.gdu.baselibrary.bean.StringItemSelectBean;
import com.gdu.baselibrary.dialog.BottomPushDialog;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.ui.secondmap.ManualLocationActivity;
import com.gdu.utils.AMapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 预警详情已处理弹窗
 * @author wixche
 */
public class AlarmHandleDialog extends BottomPushDialog {
    private TextView cancelBtn, confirmBtn;
    private RecyclerView rv_handleResult;
    private TextView tv_alarmAddressContent;
    private EditText et_remarkContent;
    private RecyclerView rv_picContent, rv_videoContent;

    private List<StringItemSelectBean> mHandleResults = new ArrayList<>();
    private BaseRVAdapter<StringItemSelectBean> mHandleResultAdapter;

    private List<String> mPicPathList = new ArrayList<>();
    private BaseRVAdapter mPicRvAdapter;

    private List<String> mVideoPathList = new ArrayList<>();
    private BaseRVAdapter mVideoRvAdapter;

    private Activity mActivity;
    private LatLng addressCoordinate;

    private IHandleCallback mHandleCallback;

    private boolean mIsSecondLocation;
    private final int UPDATE_ADDRESS = 1111;

    private Handler mHandler = new Handler(msg -> {
        switch (msg.what) {
            case UPDATE_ADDRESS:
                tv_alarmAddressContent.setText((String) msg.obj);
                break;

            default:
                break;
        }
        return true;
    });

    public AlarmHandleDialog(@NonNull Activity context) {
        super(context);
        mActivity = context;
        setContentView(R.layout.dialog_alarm_handle);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        cancelBtn = findViewById(R.id.tv_cancelBtn);
        confirmBtn = findViewById(R.id.tv_confirmBtn);
        rv_handleResult = findViewById(R.id.rv_handleResult);
        tv_alarmAddressContent = findViewById(R.id.tv_alarmAddressContent);
        et_remarkContent = findViewById(R.id.et_remarkContent);
        rv_picContent = findViewById(R.id.rv_picContent);
        rv_videoContent = findViewById(R.id.rv_videoContent);
    }

    private void initListener() {
        cancelBtn.setOnClickListener(v -> {
            dismiss();
        });
        tv_alarmAddressContent.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, ManualLocationActivity.class);
            mActivity.startActivityForResult(intent, MyConstants.SHOW_MANUAL_LOCATION_REQUEST_CODE);
        });
        confirmBtn.setOnClickListener(v -> {
            int selectResultType = -1;
            for (int i = 0; i < mHandleResults.size(); i++) {
                if (mHandleResults.get(i).isSelected()) {
                    if ("行政处罚".equals(mHandleResults.get(i).getContent())) {
                        selectResultType = 1;
                    } else if ("行政拘留".equals(mHandleResults.get(i).getContent())) {
                        selectResultType = 2;
                    } else if ("口头警告".equals(mHandleResults.get(i).getContent())) {
                        selectResultType = 3;
                    } else if ("其他处罚".equals(mHandleResults.get(i).getContent())) {
                        selectResultType = 4;
                    }
                    break;
                }
            }
            if (selectResultType == -1) {
                ToastUtil.s("请选择处理结果");
                return;
            }
            final String remarkStr = et_remarkContent.getText().toString().trim();
            final String addressStr = tv_alarmAddressContent.getText().toString().trim();
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

            if (mHandleCallback == null) {
                return;
            }
            if (addressCoordinate != null) {
                mHandleCallback.callbackParam(selectResultType, addressStr, addressCoordinate.latitude,
                        addressCoordinate.longitude, validatePicList, videoPath, remarkStr);
            } else {
                mHandleCallback.callbackParam(selectResultType, "", 0,
                        0, validatePicList, videoPath, remarkStr);
            }
            initViewData();

            dismiss();
        });
    }

    private void initViewData() {
        et_remarkContent.setText("");
        tv_alarmAddressContent.setText("");

        for (int i = 0; i < mHandleResults.size(); i++) {
            mHandleResults.get(i).setSelected(false);
        }
        mHandleResultAdapter.setList(mHandleResults);

        mPicPathList.clear();
        mPicPathList.add("add");
        mPicRvAdapter.setList(mPicPathList);

        mVideoPathList.clear();
        mVideoPathList.add("add");
        mVideoRvAdapter.setList(mVideoPathList);
    }

    private void initData() {
        MyLogUtils.i("initData()");
        initHandleResultAdapter();
        initPicAdapter();
        initVideoAdapter();
    }

    private void initHandleResultAdapter() {
        StringItemSelectBean handleResultBean;

        handleResultBean = new StringItemSelectBean();
        handleResultBean.setContent("行政处罚");
        mHandleResults.add(handleResultBean);

        handleResultBean = new StringItemSelectBean();
        handleResultBean.setContent("行政拘留");
        mHandleResults.add(handleResultBean);

        handleResultBean = new StringItemSelectBean();
        handleResultBean.setContent("口头警告");
        mHandleResults.add(handleResultBean);

        handleResultBean = new StringItemSelectBean();
        handleResultBean.setContent("其他处罚");
        mHandleResults.add(handleResultBean);

        mHandleResultAdapter = new BaseRVAdapter<StringItemSelectBean>(R.layout.item_alarm_handle_result) {
            @Override
            public void onBindVH(BaseViewHolder holder, StringItemSelectBean data, int position) {
                final TextView contentTv = holder.findView(R.id.tv_content);
                contentTv.setText(data.getContent());
                contentTv.setSelected(data.isSelected());
            }
        };
        mHandleResultAdapter.setOnItemClickListener(((adapter, view, position) -> {
            for (int i = 0; i < mHandleResults.size(); i++) {
                mHandleResults.get(i).setSelected(false);
            }
            mHandleResults.get(position).setSelected(true);
            mHandleResultAdapter.notifyDataSetChanged();
        }));
        mHandleResultAdapter.setList(mHandleResults);
        rv_handleResult.setAdapter(mHandleResultAdapter);
    }

    private void initPicAdapter() {
        MyLogUtils.i("initPicAdapter()");
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
                    MyImageLoadUtils.loadImage(getContext(), path, R.mipmap.default_photo,
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
        MyLogUtils.i("initVideoAdapter()");
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

    public void updateLocation(String locationStr, LatLng coordinate, boolean isSecondLocation) {
        if (coordinate.latitude == 0 || coordinate.longitude == 0) {
            return;
        }
        addressCoordinate = coordinate;
        mIsSecondLocation = isSecondLocation;
        if (mIsSecondLocation) {
            tv_alarmAddressContent.setText(locationStr);
        } else {
            AMapUtil.getCurrentLocationDetailsNew(getContext(), new LatLonPoint(coordinate.latitude,
                    coordinate.longitude), new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    MyLogUtils.i("onRegeocodeSearched()");
                    final Message msg = new Message();
                    msg.what = UPDATE_ADDRESS;
                    msg.obj = regeocodeResult.getRegeocodeAddress().getDistrict();
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                    MyLogUtils.i("onGeocodeSearched()");
                }
            });
        }
    }

    public void setHandleCallback(IHandleCallback dispatchCallback) {
        mHandleCallback = dispatchCallback;
    }

    public interface IHandleCallback {
//        void callbackParam(int selectResultType, List<String> uploadPics, String uploadVideo, String remark);
        void callbackParam(int selectResultType, String secondAddress, double lat, double lon,
                           List<String> uploadPics, String uploadVideo, String remark);
    }
}
