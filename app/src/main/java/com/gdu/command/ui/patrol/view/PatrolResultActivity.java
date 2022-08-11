package com.gdu.command.ui.patrol.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps2d.model.LatLng;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.base.BaseRVAdapter;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.command.R;
import com.gdu.command.ui.patrol.bean.QueryDataBean;
import com.gdu.command.ui.patrol.presenter.IPatrolDiaryView;
import com.gdu.command.ui.patrol.presenter.PatrolPresenter;
import com.gdu.command.ui.patrol.presenter.PatrolService;
import com.gdu.model.config.UrlConfig;
import com.gdu.utils.AMapUtil;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 巡逻结束记录界面
 */
public class PatrolResultActivity extends BaseActivity<PatrolPresenter>
        implements IPatrolDiaryView {
    private ImageView backBtnIv;
    private TextView titleTv;
    private TextView tv_distanceContent;
    private TextView tv_lockPatrolTrack;
    private RecyclerView rv_trackContent;

    private int patrolId = -1;

    private List<QueryDataBean.DataBean.PatrolRecordListBean> recordData = new ArrayList<>();
    private BaseRVAdapter<QueryDataBean.DataBean.PatrolRecordListBean> recordRvAdapter;
    private QueryDataBean mQueryDataBean;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        final Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            patrolId = mBundle.getInt(MyConstants.DEFAULT_PARAM_KEY_1, -1);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_patrol_result;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
        getPresenter().setIView(this);
        initView();
        initAdapter();
        initListener();
        getHistory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initView() {
        backBtnIv = findViewById(R.id.iv_left_back);
        titleTv = findViewById(R.id.tv_left_title_name);

        tv_distanceContent = findViewById(R.id.tv_distanceContent);
        tv_lockPatrolTrack = findViewById(R.id.tv_lockPatrolTrack);
        rv_trackContent = findViewById(R.id.rv_trackContent);

        initViewData();
    }

    private void initAdapter() {
        recordRvAdapter = new BaseRVAdapter<QueryDataBean.DataBean.PatrolRecordListBean>(
                R.layout.item_patrol_result) {
            @Override
            public void onBindVH(BaseViewHolder holder,
                                 QueryDataBean.DataBean.PatrolRecordListBean data, int position) {
                holder.setText(R.id.tv_title, data.getTypeName());
                holder.setText(R.id.tv_time, data.getCreateTime());
                holder.setText(R.id.tv_content, data.getContent());
                holder.setText(R.id.tv_locationLabel, data.getLocateName());
                RecyclerView picRv = holder.getView(R.id.rv_picContent);
                if (CommonUtils.isEmptyString(data.getImageUrl())) {
                    picRv.setVisibility(View.GONE);
                } else {
                    final List<String> picData = CommonUtils.getPhotoData(data.getImageUrl());
                    BaseRVAdapter<String> picAdapter = new BaseRVAdapter<String>
                            (R.layout.item_case_picture) {
                        @Override
                        public void onBindVH(BaseViewHolder holder, String data, int position) {
                            holder.setVisible(R.id.iv_delBtn, false);
                            ImageView contentIv = holder.getView(R.id.case_imageview);
                            final String url = CommonUtils.getSinglePicRealPath(data);
                            MyImageLoadUtils.loadImage(PatrolResultActivity.this, url, 0, contentIv);
                        }
                    };
                    picRv.setAdapter(picAdapter);
                    picAdapter.setList(picData);
                    picRv.setVisibility(View.VISIBLE);
                }
            }
        };
        recordRvAdapter.addChildClickViewIds(R.id.view_rvOverLayer);
        recordRvAdapter.setOnItemClickListener((adapter, view, position) -> {
            intoHistoryView(position);
        });
        recordRvAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            intoHistoryView(position);
        });
        rv_trackContent.setAdapter(recordRvAdapter);
    }

    private void initViewData() {
        titleTv.setText("巡逻记录");
    }

    private void initListener() {
        backBtnIv.setOnClickListener(v -> {
            finish();
        });
        tv_lockPatrolTrack.setOnClickListener(v -> {
            intoHistoryView(-1);
        });
    }

    private void intoHistoryView(int position) {
        final Bundle mBundle = new Bundle();
        mBundle.putSerializable(MyConstants.DEFAULT_PARAM_KEY_1, mQueryDataBean.getData().get(0));
        mBundle.putInt(MyConstants.DEFAULT_PARAM_KEY_2, position);
        openActivity(HistoryTrackActivity.class, mBundle);
    }

    private void getHistory() {
        if (patrolId == -1) {
            return;
        }
        getPresenter().queryRecord("", "", patrolId,
                "", "", 0, 0);
    }

    @Override
    public void showOrHidePb(boolean isShow, String tip) {

    }

    @Override
    public void onRequestCallback(String requestName, boolean isSuc, Object result) {
        if (PatrolService.REQ_NAME_QUERY_RECORDS.equals(requestName) && isSuc) {
            final QueryDataBean bean = (QueryDataBean) result;
            final boolean isEmptyData = bean == null || bean.getData() == null || bean.getData().size() == 0;
            if (isEmptyData) {
                recordData.clear();
                recordRvAdapter.setList(recordData);
            }
            if (bean.getData().get(0).getPatrolRecordList() != null
                    && bean.getData().get(0).getPatrolRecordList().size() != 0) {
                CommonUtils.listAddAllAvoidNPE(recordData, bean.getData().get(0).getPatrolRecordList());
                recordRvAdapter.setList(recordData);
            }

            if (bean.getData().get(0).getPatrolLocateList() != null
                    && bean.getData().get(0).getPatrolLocateList().size() != 0) {
                final ArrayList<LatLng> linePoints = new ArrayList<>();
                for (int i = 0; i < bean.getData().get(0).getPatrolLocateList().size(); i++) {
                    final QueryDataBean.DataBean.PatrolLocateListBean mLocateListBean =
                            bean.getData().get(0).getPatrolLocateList().get(i);
                    final LatLng mLatLng = new LatLng(mLocateListBean.getLat(),
                            mLocateListBean.getLon());
                    linePoints.add(mLatLng);
                }
                final float distance = AMapUtil.getPatrolDistance(linePoints);
                if (distance != 0) {
                    tv_distanceContent.setText("距离: " + distance + "米");
                }
            }
            mQueryDataBean = bean;
        }
    }
}
