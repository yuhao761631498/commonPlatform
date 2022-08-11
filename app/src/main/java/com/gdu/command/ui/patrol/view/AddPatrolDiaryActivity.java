package com.gdu.command.ui.patrol.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps2d.model.LatLng;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.base.BaseRVAdapter;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.ui.cases.CaseService;
import com.gdu.command.ui.cases.TypeCodeBean;
import com.gdu.command.ui.cases.TypeSelectDialog;
import com.gdu.command.ui.patrol.presenter.IPatrolDiaryView;
import com.gdu.command.ui.patrol.presenter.PatrolPresenter;
import com.gdu.command.ui.patrol.presenter.PatrolService;
import com.gdu.command.ui.secondmap.ManualLocationActivity;
import com.gdu.command.uploadpic.IUploadPicDiaryView;
import com.gdu.util.TimeUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 添加巡逻日记界面
 */
public class AddPatrolDiaryActivity extends BaseActivity<PatrolPresenter>
        implements IPatrolDiaryView {

    private ImageView backBtnIv;
    private TextView titleTv;

    private EditText inputContentEt;
    private RecyclerView attachPicRv;
    private TextView selectTypeTv;
    private TextView addLocationTv;
    private TextView releaseBtnTv;
    private TextView cancelBtnTv;
    private CheckBox changeCaseBtnCb;

    private List<String> mPicturePathList = new ArrayList<>();
    private BaseRVAdapter<String> mAttachPicAdapter;
    private List<String>  mRealImages;
    private String selectTypeNameStr = "";
    private LatLng addressCoordinate;

    private List<TypeCodeBean.DataBean> recordTypeData = new ArrayList<>();

    private String uploadPicStr = "";

    private int patrolId = -1;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        final Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            patrolId = mBundle.getInt(MyConstants.DEFAULT_PARAM_KEY_3, -1);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_patrol_diary;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
        getPresenter().setIView(this);
        initView();
        initAdapter();
        initListener();

        getRecordType();
    }

    private void initView() {
        backBtnIv = findViewById(R.id.iv_left_back);
        titleTv = findViewById(R.id.tv_left_title_name);
        inputContentEt = findViewById(R.id.et_inputContent);
        attachPicRv = findViewById(R.id.rv_attachContent);
        selectTypeTv = findViewById(R.id.tv_recordTypeLabel);
        addLocationTv = findViewById(R.id.tv_addLocationLabel);
        releaseBtnTv = findViewById(R.id.tv_releaseBtn);
        cancelBtnTv = findViewById(R.id.tv_cancelBtn);
        changeCaseBtnCb = findViewById(R.id.cb_changeCaseBtn);

        initViewData();
    }

    private void initAdapter() {
        mPicturePathList.clear();
        mPicturePathList.add("add");

        mAttachPicAdapter = new BaseRVAdapter<String>(R.layout.item_case_picture) {
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
                    MyImageLoadUtils.loadImage(mContext, path, 0, contentIv);
                }
            }
        };
        mAttachPicAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mPicturePathList.get(position).equals("add")) {
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(false)  //设置是否单选
                        .canPreview(true) //是否可以预览图片，默认为true
                        .setContainVideo(false)
                        .setContainPhoto(true)
                        .setMaxSelectCount(11 - mPicturePathList.size())
                        .start((Activity) mContext, MyConstants.REQUEST_PIC_CODE); // 打开相册
            }
        });
        mAttachPicAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_delBtn) {
                for (int i = 0; i < mPicturePathList.size(); i++) {
                    if (mPicturePathList.get(position).equals(mPicturePathList.get(i))) {
                        mPicturePathList.remove(position);
                        break;
                    }
                }
                mAttachPicAdapter.setList(mPicturePathList);
            }
        });
        mAttachPicAdapter.addChildClickViewIds(R.id.iv_delBtn);
        mAttachPicAdapter.setList(mPicturePathList);
        attachPicRv.setAdapter(mAttachPicAdapter);
    }

    private void initViewData() {
        titleTv.setText("添加巡逻记录");
    }

    private void initListener() {
        backBtnIv.setOnClickListener(v -> {
            finish();
        });
        selectTypeTv.setOnClickListener(v -> {
            if (recordTypeData == null || recordTypeData.size() == 0) {
                ToastUtil.s("未获取到记录类型数据");
                return;
            }
            showTypeSelectDialog("选择记录类型", recordTypeData);
        });
        addLocationTv.setOnClickListener(v -> {
            openActivityForResult(ManualLocationActivity.class, MyConstants.SHOW_MANUAL_LOCATION_REQUEST_CODE);
        });
        releaseBtnTv.setOnClickListener(v -> {
            final String contentStr = inputContentEt.getText().toString().trim();
            if (CommonUtils.isEmptyString(contentStr)) {
                ToastUtil.s("请输入记录内容");
                return;
            }
            List<String> files = new ArrayList<>();
            if (mRealImages != null && mRealImages.size() > 0) {
                files.addAll(mRealImages);
            }
            if (files.size() != 0) {
                if (!CommonUtils.isOverSize(files)) {
                    getPresenter().uploadFiles(mRealImages);
                } else {
                    ToastUtil.s("上传文件太大");
                }
            } else {
                ToastUtil.s("请选择上传图片");
            }
        });
        cancelBtnTv.setOnClickListener(v -> {
            finish();
        });

        getPresenter().setUploadPicDiaryView(new IUploadPicDiaryView() {
            @Override
            public void showOrHidePb(boolean isShow, String tip) {
                showProgressDialog();
            }

            @Override
            public void onStatusChange(int type, String content) {
                if (type == com.gdu.command.uploadpic.IUploadPicDiaryView.UPLOAD_SUC) {
                    uploadPicStr = content;
                    addPatrolHandler();
                } else {
                    hideProgressDialog();
                    ToastUtil.s("图片上传失败");
                }
            }

            @Override
            public void onStatusChange(int type, String urlImg, String urlVideo) {

            }
        });
    }

    /**
     * 发送添加巡逻记录接口
     */
    private void addPatrolHandler() {
        final String contentStr = inputContentEt.getText().toString().trim();
        if (CommonUtils.isEmptyString(contentStr)) {
            ToastUtil.s("请输入记录内容");
            return;
        }
        if (CommonUtils.isEmptyString(selectTypeNameStr)) {
            ToastUtil.s("请选择记录类型");
            return;
        }

        if (CommonUtils.isEmptyString(uploadPicStr)) {
            ToastUtil.s("请上传记录图片");
            return;
        }

        final String locationStr = addLocationTv.getText().toString().trim();
        if (addressCoordinate == null) {
            ToastUtil.s("请添加地理位置");
            return;
        }

        if (patrolId == -1) {
            return;
        }

        getPresenter().addPatrolRecord(contentStr, uploadPicStr, addressCoordinate.latitude,
                addressCoordinate.longitude, locationStr, patrolId, selectTypeNameStr);
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
            mAttachPicAdapter.setList(mPicturePathList);
        } else if (isHaveMapData) {
            final Bundle mBundle = data.getExtras();
            final String addressStr = mBundle.getString(MyConstants.RESULT_ADDRESS, "");
            addressCoordinate = mBundle.getParcelable(MyConstants.RESULT_ADDRESS_COORDINATE);
            addLocationTv.setText(addressStr);
        }
    }

    @Override
    public void showOrHidePb(boolean isShow, String tip) {
        if (isShow) {
            showProgressDialog();
        } else {
            hideProgressDialog();
        }
    }

    @Override
    public void onRequestCallback(String requestName, boolean isSuc, Object result) {
        switch (requestName) {
            case PatrolService.REQ_NAME_ADD_PATROL_RECORD:
                if (!changeCaseBtnCb.isChecked()) {
                    hideProgressDialog();
                }
                if (isSuc) {
                    ToastUtil.s("发布记录成功");
                    if (!changeCaseBtnCb.isChecked()) {
                        finishHandler();
                    }
                } else {
                    ToastUtil.s("发布记录失败");
                }
                // 如果勾选了转案件就发送添加案件接口
                if (changeCaseBtnCb.isChecked()) {
                    addCaseHandler();
                }
                break;

            case PatrolService.REQ_NAME_ADD_CASE:
                hideProgressDialog();
                if (isSuc) {
                    ToastUtil.s("案件新增成功");
                    finishHandler();
                } else {
                    ToastUtil.s("新增案件失败");
                }
                break;

            default:
                break;
        }
    }

    private void finishHandler() {
        final Intent mIntent = new Intent();
        mIntent.putExtra(MyConstants.DEFAULT_PARAM_KEY_1, addressCoordinate);
        setResult(RESULT_OK, mIntent);
        finish();
    }

    private void getRecordType() {
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.getTypeOrSource("patrolRecordType")
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(bean -> {
                    final boolean isEmpty = bean == null || bean.getCode() != 0
                            || bean.getData() == null || bean.getData().size() == 0;
                    if (isEmpty) {
                        return;
                    }
                    recordTypeData.clear();
                    CommonUtils.listAddAllAvoidNPE(recordTypeData, bean.getData());
                }, throwable -> {
                    MyLogUtils.e("获取巡逻记录类型出错", throwable);
                });
    }

    /**
     * 显示时间选择
     * @param data
     */
    private void showTypeSelectDialog(String title, List<TypeCodeBean.DataBean> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        TypeSelectDialog dialog = new TypeSelectDialog(mContext, title, data);
        dialog.setListener((position, o) -> {
            MyLogUtils.d("onItemClick() position = " + position);
            selectTypeNameStr = data.get(position).getTypeName();
            selectTypeTv.setText(selectTypeNameStr);
            dialog.dismiss();
        });
        dialog.show();
    }

    /**
     * 发送添加案件接口
     */
    private void addCaseHandler() {
        final String contentStr = inputContentEt.getText().toString().trim();
        if (CommonUtils.isEmptyString(contentStr)) {
            ToastUtil.s("请输入记录内容");
            return;
        }
        if (CommonUtils.isEmptyString(selectTypeNameStr)) {
            ToastUtil.s("请选择记录类型");
            return;
        }

        if (CommonUtils.isEmptyString(uploadPicStr)) {
            ToastUtil.s("请上传记录图片");
            return;
        }

        final String locationStr = addLocationTv.getText().toString().trim();
        if (addressCoordinate == null) {
            ToastUtil.s("请添加地理位置");
            return;
        }

        final String timeStr = TimeUtils.getCurrentTime();

        getPresenter().addCase(contentStr, selectTypeNameStr, timeStr,
                "caseTypesFFBY", "caseSourcesXLFX",
                "", locationStr, "", timeStr,
                0, uploadPicStr, "",
                addressCoordinate != null ? addressCoordinate.latitude : 0,
                addressCoordinate != null ? addressCoordinate.longitude : 0,
                "",
                "", "");
    }
}
