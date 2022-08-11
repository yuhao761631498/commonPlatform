package com.gdu.command.ui.alarm.center;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdu.baselibrary.base.RefreshActivity;
import com.gdu.baselibrary.network.AlarmTypeStatictisBean;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.event.RefreshAlarmEvent;
import com.gdu.command.ui.alarm.AlarmCenterAdapter;
import com.gdu.command.ui.alarm.detail.AlarmDetailActivity;
import com.gdu.command.ui.cases.navigate.NavigateActivity;
import com.gdu.command.views.AllAlarmChooseView;
import com.gdu.command.views.AllAlarmDevChooseView;
import com.gdu.command.views.drop.AlarmDeviceBean;
import com.gdu.command.views.drop.DropDownMenu;
import com.gdu.command.views.drop.DropListBean;
import com.gdu.command.views.drop.GirdDropDownAdapter;
import com.gdu.model.alarm.AlarmInfo;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.config.GlobalVariable;
import com.gdu.utils.MyVariables;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 告警中心
 */
public class AlarmCenterActivity extends RefreshActivity<AlarmCenterPresenter>
        implements View.OnClickListener, IAlarmCenterView {

    private ImageView mBackImageView;
    private DropDownMenu mDropDownMenu;
    /** 综合排序选项列表 */
    private RecyclerView mComprehensiveRv;
    /** 告警设备过滤界面 */
    private AllAlarmDevChooseView mAllAlarmDevChooseView;
    /** 全部筛选界面 */
    private AllAlarmChooseView mAllAlarmChooseView;

    private String headers[] = {"综合排列", "告警设备", "全部筛选"};
//    private String headers[] = {"综合排列",  "全部筛选"};
    private String comprehensiveData[] = {"默认排列", "时间由近到远", "时间由远到近", "等级红橙黄蓝", "等级蓝黄橙红"};
    private List<DropListBean> levelData = new ArrayList<>();
    private List<View> popupViews = new ArrayList<>();

    private GirdDropDownAdapter mComprehensiveViewAdapter;

    /** 当前排序类型 */
    private int curSortType = -1;
    /** 筛选选中的设备ID */
    private List<String> selectDevIds = new ArrayList();
    /** 事件类型过滤选项 */
    private Map<Integer, Boolean> mEventTypeMap;
    /** 事件状态过滤选项 */
    private Map<Integer, Boolean> mStatusMap;
    /** 事件等级过滤选项 */
    private Map<Integer, Boolean> mLevelMap;
    /** 记录开始时间 */
    private String mStartTime = "";
    /** 记录结束时间 */
    private String mEndTime = "";
    /** 是否是加载更模式 */
    private boolean isLoadMoreData = false;
    /** 是否已经全部加载完成 */
    private boolean isLoadFinish = false;

    private List<AlarmInfo> curShowList = new ArrayList<>();
    private AlarmCenterAdapter mAlarmCenterAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_center;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
        PAGE_COUNT = 20;
        getPresenter().getDeviceList();
        getPresenter().setView(this);
        initView();
        initDropView();
        initListener();
    }

    private void initListener() {
        mBackImageView.setOnClickListener(this);
    }

    private void initView() {
        EventBus.getDefault().register(this);
        mBackImageView = findViewById(R.id.back_imageview);
        initRefreshLayout();
        initRecyclerView();
        initAdapter();
    }

    private void initAdapter() {
        mAlarmCenterAdapter = new AlarmCenterAdapter(null);
        mAlarmCenterAdapter.addChildClickViewIds(R.id.alarm_position_textview, R.id.iv_attention);
        mAlarmCenterAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(AlarmCenterActivity.this, AlarmDetailActivity.class);
            GlobalVariable.sAlarmInfo = mAlarmCenterAdapter.getData().get(position);
            startActivity(intent);
        });
        mAlarmCenterAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            final AlarmInfo data = mAlarmCenterAdapter.getData().get(position);
            switch (view.getId()) {

                case R.id.alarm_position_textview:
                    Intent intent = new Intent(getContext(), NavigateActivity.class);
                    final IssueBean bean = new IssueBean();
                    bean.setCaseDesc(data.getAlarmTypeName());
                    bean.setLatitude(data.getLat());
                    bean.setLongitude(data.getLon());
                    GlobalVariable.sCurrentIssueBean = bean;
                    startActivity(intent);
                    break;

                case R.id.iv_attention:
                    getPresenter().attention(data.getId(), data.getAttention() == 1 ? 0 : 1);
                    break;

                default:
                    break;
            }
        });
        if (mAlarmCenterAdapter.getLoadMoreModule() != null) {
            mAlarmCenterAdapter.getLoadMoreModule().setAutoLoadMore(true);
            mAlarmCenterAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
            mAlarmCenterAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
                MyLogUtils.d("onLoadMore()");
                loadMore();
            });
        }
        final View emptyDataView = LayoutInflater.from(this).inflate(R.layout.layout_empty_view, null);
        TextView emptyTipTv = emptyDataView.findViewById(R.id.tv_empty_tip);
        emptyTipTv.setText("暂无数据");
        mAlarmCenterAdapter.setEmptyView(emptyDataView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAlarmCenterAdapter);
    }

    private void initDropView() {
        mDropDownMenu = findViewById(R.id.dropDownMenu);

        initComprehensiveRv();

        mAllAlarmDevChooseView = new AllAlarmDevChooseView(this);
        mAllAlarmDevChooseView.setOnAlarmChooseListener(new AllAlarmDevChooseView.OnAlarmChooseListener() {
            @Override
            public void onConfirm(Map<String, Boolean> devIds, List<AlarmDeviceBean.DataBean.RowsBean> data) {
                selectDevIds.clear();
                Iterator<Map.Entry<String, Boolean>> it = devIds.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Boolean> bean = it.next();
                    if (bean.getValue()) {
                        CommonUtils.listAddAvoidNull(selectDevIds, bean.getKey());
                    }
                }
                if (selectDevIds.size() == 0) {
                    mDropDownMenu.setTabText(headers[1]);
                } else if (selectDevIds.size() == 1) {
                    for (final AlarmDeviceBean.DataBean.RowsBean mAlarmDevDatum : data) {
                        if (selectDevIds.get(0).equals(mAlarmDevDatum.getId() + "")) {
                            mDropDownMenu.setTabText(mAlarmDevDatum.getDeviceName());
                            break;
                        }
                    }
                } else {
                    mDropDownMenu.setTabText("已选" + selectDevIds.size() + "个设备");
                }
                mDropDownMenu.closeMenu();
                loadFilterFirstData();
            }

            @Override
            public void onReset() {
                resetHandler();
                mDropDownMenu.closeMenu();
            }
        });

        mAllAlarmChooseView = new AllAlarmChooseView(this);
        mAllAlarmChooseView.setOnAlarmChooseListener(new AllAlarmChooseView.OnAlarmChooseListener() {

            @Override
            public void onConfirm(Map<Integer, Boolean> statusMap, Map<Integer, Boolean> levelMap, String startTime, String endTime) {
                getPresenter().setFilterDataType(statusMap, levelMap, startTime, endTime);
                mDropDownMenu.closeMenu();
                loadFilterFirstData();
            }

            @Override
            public void onConfirm(Map<Integer, Boolean> eventTypeMap,
                                  Map<Integer, Boolean> statusMap, Map<Integer, Boolean> levelMap,
                                  String startTime, String endTime) {
                getPresenter().setFilterDataType(eventTypeMap, statusMap, levelMap, startTime, endTime);
                mDropDownMenu.closeMenu();
                loadFilterFirstData();
            }

            @Override
            public void onReset() {
                resetHandler();
                mDropDownMenu.closeMenu();
            }
        });

        //init popupViews
        popupViews.add(mComprehensiveRv);
        popupViews.add(mAllAlarmDevChooseView);
        popupViews.add(mAllAlarmChooseView);

        //add item click event
        mComprehensiveViewAdapter.setOnItemClickListener((adapter, view, position) -> {
            for (int i = 0; i < levelData.size(); i++) {
                levelData.get(i).setSelected(false);
            }
            levelData.get(position).setSelected(true);
            mComprehensiveViewAdapter.notifyDataSetChanged();
            mDropDownMenu.setTabText(position == 0 ? headers[0] : comprehensiveData[position]);
            mDropDownMenu.closeMenu();
            switch (position){
                case 0:
                    curSortType = -1;
                    loadFilterFirstData();
                    break;
                case 1:
                    curSortType = 1;
                    curShowList = getPresenter().sortByNearTime(curShowList);
                    break;
                case 2:
                    curSortType = 2;
                    curShowList = getPresenter().sortByFarTime(curShowList);
                    break;
                case 3:
                    curSortType = 3;
                    curShowList = getPresenter().sortByLevelHigh(curShowList);
                    break;
                case 4:
                    curSortType = 4;
                    curShowList = getPresenter().sortByLevelLow(curShowList);
                    break;
                default:
                    break;
            }
            setLoadData(mAlarmCenterAdapter, curShowList);
        });

        //init context view
        TextView contentView = new TextView(this);

        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }

    private void resetHandler() {
        mDropDownMenu.setChildText(1, headers[1]);
        selectDevIds.clear();
        curSortType = -1;
        mStartTime = "";
        mEndTime = "";
        MyVariables.isLockAtAttention = true;
        if (mAllAlarmDevChooseView != null) {
            mAllAlarmDevChooseView.reset();
        }
        if (mAllAlarmChooseView != null) {
            mAllAlarmChooseView.reset();
        }
        loadData();
    }

    private void initComprehensiveRv() {
        for (int i = 0; i < comprehensiveData.length; i++) {
            final DropListBean bean = new DropListBean();
            bean.setContent(comprehensiveData[i]);
            bean.setSelected(false);
            CommonUtils.listAddAvoidNull(levelData, bean);
        }
        //init city menu
        mComprehensiveRv = new RecyclerView(this);
        mComprehensiveViewAdapter = new GirdDropDownAdapter(levelData);
        mComprehensiveRv.setLayoutManager(new LinearLayoutManager(this));
        mComprehensiveRv.setAdapter(mComprehensiveViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_imageview) {
            finish();
        }
    }

    @Override
    public void loadData() {
//        mPage = 1;
//        isLoadFinish = false;
//        mAlarmCenterAdapter.getLoadMoreModule().setEnableLoadMore(true);
//        getPresenter().loadDataDefault(mStartTime, mEndTime, mPage, PAGE_COUNT);
        loadFilterFirstData();
    }

    /**
     * 设置过滤条件后的初次数据加载
     */
    private void loadFilterFirstData() {
        mPage = 1;
        isLoadFinish = false;
        mAlarmCenterAdapter.getLoadMoreModule().setEnableLoadMore(true);
        getPresenter().loadDataNew(5);
    }

    private void loadMore() {
        MyLogUtils.d("loadMore()");
        if (isLoadFinish) {
            ToastUtil.s("数据已全部加载完成");
            mAlarmCenterAdapter.getLoadMoreModule().setEnableLoadMore(false);
            return;
        }
        mPage++;
        isLoadMoreData = true;
        getPresenter().loadDataNew(5);
    }

    @Override
    public void showAlarmList(List<AlarmInfo> alarmInfos) {
        MyLogUtils.d("showAlarmList() listSize = " + alarmInfos.size());
        if (!isLoadMoreData) {
            curShowList.clear();
        } else {
            if (CommonUtils.isEmptyList(alarmInfos)) {
                isLoadFinish = true;
                isLoadMoreData = false;
                mAlarmCenterAdapter.getLoadMoreModule().loadMoreEnd();
                return;
            }
        }
        loadMoreFinish();
        CommonUtils.listAddAllAvoidNPE(curShowList, alarmInfos);
        // 判断集合为空就不用进行后继操作了
        if (curShowList == null || curShowList.size() == 0) {
            setLoadData(mAlarmCenterAdapter, curShowList);
            return;
        }
        if (curSortType != -1) {
            switch (curSortType) {

                case 1:
                    curShowList = getPresenter().sortByNearTime(curShowList);
                    break;

                case 2:
                    curShowList = getPresenter().sortByFarTime(curShowList);
                    break;

                case 3:
                    curShowList = getPresenter().sortByLevelLow(curShowList);
                    break;

                case 4:
                    curShowList = getPresenter().sortByLevelHigh(curShowList);
                    break;

                default:
                    break;
            }
        }
        setLoadData(mAlarmCenterAdapter, curShowList);
    }

    private void loadMoreFinish() {
        MyLogUtils.d("loadMoreFinish()");
        if (mAlarmCenterAdapter.getLoadMoreModule() != null && mAlarmCenterAdapter.getLoadMoreModule().isLoading()) {
            mAlarmCenterAdapter.getLoadMoreModule().loadMoreComplete();
        }
        isLoadMoreData = false;
    }

    @Override
    public void showDeviceList(List<AlarmDeviceBean.DataBean.RowsBean> devList) {
        MyLogUtils.d("showDeviceList() devSize = " + devList.size());
        mAllAlarmDevChooseView.setNewData(devList);
    }

    @Override
    public void attentionResult(int alarmId) {
        for (int i = 0; i < curShowList.size(); i++) {
            final AlarmInfo bean = curShowList.get(i);
            if (bean.getId() == alarmId) {
                final int oldAttentionStatus = bean.getAttention();
                bean.setAttention(oldAttentionStatus == 1 ? 0 : 1);
                if (oldAttentionStatus == 1) {
                    ToastUtil.s("取消关注成功");
                } else {
                    ToastUtil.s("关注成功");
                }
                mAlarmCenterAdapter.notifyItemChanged(i, "attention");
                break;
            }
        }
    }

    @Override
    public void showAlarmTypeStatisticsData(List<AlarmTypeStatictisBean.DataDTO> data) {

    }

    @Override
    public void callbackIsAppMenu(boolean isAppMenu) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAlarmHome(RefreshAlarmEvent refreshAlarmEvent) {
       loadData();
    }
}
