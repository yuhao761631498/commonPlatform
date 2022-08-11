package com.gdu.command.ui.patrol;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdu.baselibrary.base.RefreshActivity;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.command.R;
import com.gdu.command.ui.patrol.bean.QueryDataBean;
import com.gdu.command.ui.patrol.presenter.IPatrolDiaryView;
import com.gdu.command.ui.patrol.presenter.PatrolPresenter;
import com.gdu.command.ui.patrol.presenter.PatrolService;
import com.gdu.command.ui.patrol.view.AllPatrolChooseView;
import com.gdu.command.ui.patrol.view.HistoryTrackActivity;
import com.gdu.command.ui.patrol.view.PatrolResultActivity;
import com.gdu.command.views.drop.DropDownMenu;
import com.gdu.command.views.drop.DropListBean;
import com.gdu.command.views.drop.GirdDropDownAdapter;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 群众巡逻记录
 * create by zyf
 */
public class MassesPatrolRecordActivity extends RefreshActivity<PatrolPresenter> implements
        View.OnClickListener, IPatrolDiaryView {

    private ImageView ivLeftBack;
    private TextView tvTitleName;
    private DropDownMenu mDropDownMenu;
    private List<View> popupViews = new ArrayList<>();
    private String headers[] = {"综合排序", "全部筛选"};
    private String comprehensiveData[] = {"默认排列", "开始时间由近到远", "开始时间由远到近"};
    private PatrolRecordAdapter mAdapter;
    private RecyclerView comprehensiveView;
    private List<DropListBean> levelData = new ArrayList<>();
    private GirdDropDownAdapter mComprehensiveViewAdapter;

    private QueryDataBean mQueryDataBean;
    private List<QueryDataBean.DataBean> queryData = new ArrayList<>();

    private int sortType = -1;
    private Map<String, Boolean> filterPatrolType;
    private Map<String, Boolean> filterRecordType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_patrol_record;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
        getPresenter().setIView(this);
        initView();
        initListener();

        tvTitleName.setText("群众巡逻记录");
    }

    private void initView() {
        ivLeftBack = findViewById(R.id.iv_left_back);
        tvTitleName = findViewById(R.id.tv_left_title_name);
        mDropDownMenu = findViewById(R.id.dropDownMenu_person_patrol);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        initRefreshLayout();
        initAdapter();
        initDropMenu();
    }

    private void initAdapter() {
        mAdapter = new PatrolRecordAdapter(queryData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.addChildClickViewIds(R.id.tv_patrol_locus, R.id.tv_patrol_record);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Bundle mBundle;
            switch (view.getId()) {
                case R.id.tv_patrol_locus:
                    mBundle = new Bundle();
                    mBundle.putSerializable(MyConstants.DEFAULT_PARAM_KEY_1, mQueryDataBean.getData().get(position));
                    mBundle.putInt(MyConstants.DEFAULT_PARAM_KEY_2, -1);
                    openActivity(HistoryTrackActivity.class, mBundle);
                    break;

                case R.id.tv_patrol_record:
                    mBundle = new Bundle();
                    mBundle.putSerializable(MyConstants.DEFAULT_PARAM_KEY_1, mQueryDataBean.getData().get(position).getId());
                    openActivity(PatrolResultActivity.class, mBundle);
                    break;

                default:
                    break;
            }
        });
        mAdapter.setEmptyView(R.layout.layout_empty_view);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        ivLeftBack.setOnClickListener(this);
    }

    private void initDropMenu() {
        TextView contentView = new TextView(this);
        AllPatrolChooseView mAllPatrolChooseView = new AllPatrolChooseView(this);
        mAllPatrolChooseView.setOnAlarmChooseListener(new AllPatrolChooseView.OnAlarmChooseListener() {

            @Override
            public void onConfirm(Map<String, Boolean> patrolType,
                                  Map<String, Boolean> recordType, String startTime,
                                  String endTime) {
                final String patrolTypeStr = CommonUtils.getMapSelectKey(patrolType);
                final String recordTypeStr = CommonUtils.getMapSelectKey(recordType);

                final int userId = MMKVUtil.getInt(SPStringUtils.USER_ID);
                getPresenter().queryRecord(endTime, startTime, 0,
                        recordTypeStr, patrolTypeStr, userId, sortType);
                mDropDownMenu.closeMenu();
            }

            @Override
            public void onReset() {
                loadData();
                mDropDownMenu.closeMenu();
            }
        });

        initTimeSortAdapter();

        popupViews.add(comprehensiveView);
        popupViews.add(mAllPatrolChooseView);

        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }

    private void initTimeSortAdapter() {
        for (int i = 0; i < comprehensiveData.length; i++) {
            final DropListBean bean = new DropListBean();
            bean.setContent(comprehensiveData[i]);
            bean.setSelected(false);
            CommonUtils.listAddAvoidNull(levelData, bean);
        }
        comprehensiveView = new RecyclerView(this);
        mComprehensiveViewAdapter = new GirdDropDownAdapter(levelData);
        comprehensiveView.setLayoutManager(new LinearLayoutManager(this));
        comprehensiveView.setAdapter(mComprehensiveViewAdapter);
        mComprehensiveViewAdapter.setOnItemClickListener((adapter, view, position) -> {
            for (int i = 0; i < levelData.size(); i++) {
                levelData.get(i).setSelected(false);
            }
            levelData.get(position).setSelected(true);
            mComprehensiveViewAdapter.notifyDataSetChanged();
            mDropDownMenu.setTabText(position == 0 ? headers[0] : comprehensiveData[position]);
            mDropDownMenu.closeMenu();
            switch (position) {
                case 0:
                    loadData();
                    break;

                case 1:
                    queryData(1);
                    break;

                case 2:
                    queryData(0);
                    break;

                default:
                    break;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_back:
                onBackPressed();
                break;

            default:
                break;
        }
    }

    @Override
    public void loadData() {
        queryData(1);
    }

    private void queryData(int sort) {
        sortType = sort;
        getPresenter().queryRecord("", "", 0,
                "", "", 0, sort);
    }

    @Override
    public void showOrHidePb(boolean isShow, String tip) {

    }

    @Override
    public void onRequestCallback(String requestName, boolean isSuc, Object result) {
        queryData.clear();
        finishRefresh();
        if (PatrolService.REQ_NAME_QUERY_RECORDS.equals(requestName) && isSuc) {
            final QueryDataBean bean = (QueryDataBean) result;
            mQueryDataBean = bean;
            final boolean isEmptyData = bean == null || bean.getData() == null || bean.getData().size() == 0;
            if (isEmptyData) {
                mAdapter.notifyDataSetChanged();
                return;
            }
            CommonUtils.listAddAllAvoidNPE(queryData, bean.getData());
        }
        mAdapter.notifyDataSetChanged();
    }
}
