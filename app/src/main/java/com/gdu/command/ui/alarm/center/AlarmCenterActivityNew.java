package com.gdu.command.ui.alarm.center;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.network.AlarmTypeStatictisBean;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.eventbus.EventMessage;
import com.gdu.baselibrary.utils.eventbus.GlobalEventBus;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.event.RefreshAlarmEvent;
import com.gdu.command.ui.alarm.AlarmCenterAdapter;
import com.gdu.command.ui.alarm.detail.AlarmDetailActivity;
import com.gdu.command.ui.cases.navigate.NavigateActivity;
import com.gdu.command.views.AllAlarmChooseView;
import com.gdu.command.views.AllAlarmDevChooseView;
import com.gdu.command.views.AllAlarmTimeChooseView;
import com.gdu.command.views.drop.AlarmDeviceBean;
import com.gdu.command.views.drop.DropDownMenu;
import com.gdu.command.views.drop.DropListBean;
import com.gdu.model.alarm.AlarmInfo;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.config.GlobalVariable;
import com.gdu.utils.MyVariables;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 告警中心(新)
 * @author wixche
 */
public class AlarmCenterActivityNew extends BaseActivity<AlarmCenterPresenter>
        implements View.OnClickListener, IAlarmCenterView {

    private ImageView mBackImageView;
    private ImageView iv_collection;
    private ImageView iv_search;
    protected SmartRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;

    /** 待核实标签包裹布局 */
    private LinearLayout ll_subLayout1;
    private TextView tv_alarmType1Num;
    private TextView tv_alarmType1Name;
    private View view_alarmType1Line;
    /** 待接警标签包裹布局 */
    private LinearLayout ll_subLayout2;
    private TextView tv_alarmType2Num;
    private TextView tv_alarmType2Name;
    private View view_alarmType2Line;
    /** 待处理标签包裹布局 */
    private LinearLayout ll_subLayout3;
    private TextView tv_alarmType3Num;
    private TextView tv_alarmType3Name;
    private View view_alarmType3Line;
    /** 已处理标签包裹布局 */
    private LinearLayout ll_subLayout4;
    private TextView tv_alarmType4Num;
    private TextView tv_alarmType4Name;
    private View view_alarmType4Line;

    private DropDownMenu mDropDownMenu;
    /** 告警时间过滤界面 */
    private AllAlarmTimeChooseView mAllAlarmTimeChooseView;
    /** 告警设备过滤界面 */
    private AllAlarmDevChooseView mAllAlarmDevChooseView;
    /** 全部筛选界面 */
    private AllAlarmChooseView mAllAlarmChooseView;

    private String headers[] = {"预警时间", "预警类型", "预警设备"};
//    private String headers[] = {"预警类型", "预警设备"};
    private List<DropListBean> levelData = new ArrayList<>();
    private List<View> popupViews = new ArrayList<>();

    /** 当前获取预警数据类型 */
    private int curAlarmType = 1;

    private AlarmCenterAdapter mAlarmCenterAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_info;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
        GlobalEventBus.getBus().register(this);
        getPresenter().setView(this);
        getPresenter().setPageCount(20);

        initView();
        initDropView();
        initListener();

        showProgressDialog();
        getPresenter().getUsePermission();
    }

    private void initView() {
        mBackImageView = findViewById(R.id.back_imageview);
        iv_collection = findViewById(R.id.iv_collection);
        iv_search = findViewById(R.id.iv_search);

        iv_collection.setSelected(MyVariables.isLockAtAttention);

        ll_subLayout1 = findViewById(R.id.ll_subLayout1);
        tv_alarmType1Num = findViewById(R.id.tv_alarmType1Num);
        tv_alarmType1Name = findViewById(R.id.tv_alarmType1Name);
        view_alarmType1Line = findViewById(R.id.view_alarmType1Line);

        ll_subLayout2 = findViewById(R.id.ll_subLayout2);
        tv_alarmType2Num = findViewById(R.id.tv_alarmType2Num);
        tv_alarmType2Name = findViewById(R.id.tv_alarmType2Name);
        view_alarmType2Line = findViewById(R.id.view_alarmType2Line);

        ll_subLayout3 = findViewById(R.id.ll_subLayout3);
        tv_alarmType3Num = findViewById(R.id.tv_alarmType3Num);
        tv_alarmType3Name = findViewById(R.id.tv_alarmType3Name);
        view_alarmType3Line = findViewById(R.id.view_alarmType3Line);

        ll_subLayout4 = findViewById(R.id.ll_subLayout4);
        tv_alarmType4Num = findViewById(R.id.tv_alarmType4Num);
        tv_alarmType4Name = findViewById(R.id.tv_alarmType4Name);
        view_alarmType4Line = findViewById(R.id.view_alarmType4Line);

        mRecyclerView = (RecyclerView) findViewById(com.gdu.baselibrary.R.id.mRecyclerView);

        initRefreshLayout();
        initAdapter();
    }

    private void initRefreshLayout() {
        mRefreshLayout = (SmartRefreshLayout) findViewById(com.gdu.baselibrary.R.id.mRefreshLayout);
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableAutoLoadMore(false);
        mRefreshLayout.setDisableContentWhenLoading(true);
        mRefreshLayout.setDisableContentWhenRefresh(true);
        mRefreshLayout.setOnRefreshListener(refreshLayout -> onRefresh());
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> loadMoreHandle());
    }

    private void onRefresh() {
        getPresenter().getAlarmTypeStatistics();
    }

    private void loadMoreHandle() {
        loadMore();
    }

    private void initAdapter() {
        mAlarmCenterAdapter = new AlarmCenterAdapter(null);
        mAlarmCenterAdapter.addChildClickViewIds(R.id.alarm_position_textview, R.id.iv_attention);
        mAlarmCenterAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(AlarmCenterActivityNew.this, AlarmDetailActivity.class);
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
                    showProgressDialog();
                    getPresenter().attention(data.getId(), data.getAttention() == 1 ? 0 : 1);
                    break;

                default:
                    break;
            }
        });

        final View emptyDataView = LayoutInflater.from(this).inflate(R.layout.layout_empty_view, null);
        TextView emptyTipTv = emptyDataView.findViewById(R.id.tv_empty_tip);
        emptyTipTv.setText("暂无数据");
        mAlarmCenterAdapter.setEmptyView(emptyDataView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAlarmCenterAdapter);
    }

    private void initDropView() {
        mDropDownMenu = findViewById(R.id.ddm_searchMenu);

        mAllAlarmTimeChooseView = new AllAlarmTimeChooseView(this);
        mAllAlarmTimeChooseView.setOnAlarmChooseListener(new AllAlarmTimeChooseView.OnAlarmChooseListener() {

            @Override
            public void onConfirm(String startTime, String endTime) {
                getPresenter().setFilterDataType(null, null, null, startTime, endTime);
                mDropDownMenu.closeMenu();
                loadFilterFirstData();
            }

            @Override
            public void onReset() {
                resetHandler();
                mDropDownMenu.closeMenu();
            }
        });

        mAllAlarmDevChooseView = new AllAlarmDevChooseView(this);
        mAllAlarmDevChooseView.setOnAlarmChooseListener(new AllAlarmDevChooseView.OnAlarmChooseListener() {
            @Override
            public void onConfirm(Map<String, Boolean> devIds, List<AlarmDeviceBean.DataBean.RowsBean> data) {
                final List<String> selectDevIds = new ArrayList();
                Iterator<Map.Entry<String, Boolean>> it = devIds.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Boolean> bean = it.next();
                    if (bean.getValue()) {
                        CommonUtils.listAddAvoidNull(selectDevIds, bean.getKey());
                    }
                }
                if (selectDevIds.size() == 0) {
                    mDropDownMenu.setTabText(headers[2]);
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
                getPresenter().setSelectDevIds(selectDevIds);
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
//        popupViews.add(mComprehensiveRv);
        popupViews.add(mAllAlarmTimeChooseView);
        popupViews.add(mAllAlarmChooseView);
        popupViews.add(mAllAlarmDevChooseView);

        //init context view
        final TextView contentView = new TextView(this);
        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }

    private void initListener() {
        mBackImageView.setOnClickListener(this);
        iv_collection.setOnClickListener(this);
        iv_search.setOnClickListener(this);

        ll_subLayout1.setOnClickListener(this);
        ll_subLayout2.setOnClickListener(this);
        ll_subLayout3.setOnClickListener(this);
        ll_subLayout4.setOnClickListener(this);
    }

    private void resetHandler() {
        mDropDownMenu.setChildText(2, headers[2]);
        getPresenter().resetHandler();
        if (mAllAlarmDevChooseView != null) {
            mAllAlarmDevChooseView.reset();
        }
        if (mAllAlarmChooseView != null) {
            mAllAlarmChooseView.reset();
        }
        loadFilterFirstData();
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
        GlobalEventBus.getBus().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_imageview:
                finish();
                break;

            case R.id.iv_collection:
                showProgressDialog();
                iv_collection.setSelected(!MyVariables.isLockAtAttention);
                getPresenter().isLoadCollectData(!MyVariables.isLockAtAttention);
                break;

            case R.id.iv_search:
                if (mDropDownMenu.getVisibility() == View.VISIBLE) {
                    mDropDownMenu.closeMenu();
                    mDropDownMenu.setVisibility(View.GONE);
                } else {
                    mDropDownMenu.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.ll_subLayout1:
                view_alarmType1Line.setVisibility(View.VISIBLE);
                view_alarmType2Line.setVisibility(View.INVISIBLE);
                view_alarmType3Line.setVisibility(View.INVISIBLE);
                view_alarmType4Line.setVisibility(View.INVISIBLE);
                curAlarmType = 1;
                showProgressDialog();
                getPresenter().showAlarmDataByType(curAlarmType);
                break;

            case R.id.ll_subLayout2:
                view_alarmType1Line.setVisibility(View.INVISIBLE);
                view_alarmType2Line.setVisibility(View.VISIBLE);
                view_alarmType3Line.setVisibility(View.INVISIBLE);
                view_alarmType4Line.setVisibility(View.INVISIBLE);
                curAlarmType = 3;
                showProgressDialog();
                getPresenter().showAlarmDataByType(curAlarmType);
                break;

            case R.id.ll_subLayout3:
                view_alarmType1Line.setVisibility(View.INVISIBLE);
                view_alarmType2Line.setVisibility(View.INVISIBLE);
                view_alarmType3Line.setVisibility(View.VISIBLE);
                view_alarmType4Line.setVisibility(View.INVISIBLE);
                curAlarmType = 4;
                showProgressDialog();
                getPresenter().showAlarmDataByType(curAlarmType);
                break;

            case R.id.ll_subLayout4:
                view_alarmType1Line.setVisibility(View.INVISIBLE);
                view_alarmType2Line.setVisibility(View.INVISIBLE);
                view_alarmType3Line.setVisibility(View.INVISIBLE);
                view_alarmType4Line.setVisibility(View.VISIBLE);
                curAlarmType = 5;
                showProgressDialog();
                getPresenter().showAlarmDataByType(curAlarmType);
                break;

            default:
                break;
        }
    }

    /**
     * 设置过滤条件后的初次数据加载
     */
    private void loadFilterFirstData() {
        mRefreshLayout.setEnableLoadMore(true);
        getPresenter().loadDataNew(curAlarmType);
    }

    private void loadMore() {
        MyLogUtils.d("loadMore()");
        if (getPresenter().getFinishStatus()) {
            ToastUtil.s("数据已全部加载完成");
            mRefreshLayout.setEnableLoadMore(false);
            return;
        }
        getPresenter().loadDataNew(curAlarmType, true);
    }

    @Override
    public void showAlarmList(List<AlarmInfo> alarmInfos) {
        final String sizeStr = alarmInfos == null ? "0" : alarmInfos.size() + "";
        hideProgressDialog();
        MyLogUtils.d("showAlarmList() listSize = " + sizeStr);
        mRefreshLayout.setEnableLoadMore(!getPresenter().getFinishStatus());
        refreshFinish();
        loadMoreFinish();
        mAlarmCenterAdapter.setList(alarmInfos);
    }

    private void refreshFinish() {
        MyLogUtils.d("refreshFinish()");
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
        }
    }

    private void loadMoreFinish() {
        MyLogUtils.d("loadMoreFinish()");
        if (mRefreshLayout.isLoading()) {
            mRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public void showDeviceList(List<AlarmDeviceBean.DataBean.RowsBean> devList) {
        final String sizeStr = devList == null ? "0" : devList.size() + "";
        MyLogUtils.d("showDeviceList() devSize = " + sizeStr);
        mAllAlarmDevChooseView.setNewData(devList);
    }

    @Override
    public void attentionResult(int alarmId) {
        if (alarmId != -1) {
            ToastUtil.s("操作成功");
        } else {
            ToastUtil.s("操作失败");
        }
    }

    @Override
    public void showAlarmTypeStatisticsData(List<AlarmTypeStatictisBean.DataDTO> data) {
        loadFilterFirstData();
        if (data == null) {
            ToastUtil.s("获取类型统计数据失败");
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            final int alarmType = data.get(i).getAlarmHandleTypeValue();
            final String alarmName = data.get(i).getAlarmHandleTypeCode();
            final int num = data.get(i).getCount();
            switch (alarmType) {
                case 1:
                    tv_alarmType1Num.setText(num + "");
                    tv_alarmType1Name.setText(alarmName);
                    break;
                case 3:
                    tv_alarmType2Num.setText(num + "");
                    tv_alarmType2Name.setText(alarmName);
                    break;
                case 4:
                    tv_alarmType3Num.setText(num + "");
                    tv_alarmType3Name.setText(alarmName);
                    break;
                case 5:
                    tv_alarmType4Num.setText(num + "");
                    tv_alarmType4Name.setText(alarmName);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void callbackIsAppMenu(boolean isAppMenu) {
        getPresenter().getDeviceList();
        ll_subLayout1.setVisibility(isAppMenu ? View.VISIBLE : View.GONE);
        view_alarmType1Line.setVisibility(isAppMenu ? View.VISIBLE : View.INVISIBLE);
        view_alarmType2Line.setVisibility(isAppMenu ? View.INVISIBLE : View.VISIBLE);
        curAlarmType = isAppMenu ? 1 : 3;
        GlobalVariable.isAppMenu = isAppMenu;
        getPresenter().getAlarmTypeStatistics();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMsgHandle(EventMessage msg) {
        if (msg.getMsgType() == MyConstants.ALARM_HANDLE_SUC) {
            onRefresh();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAlarmHome(RefreshAlarmEvent refreshAlarmEvent) {
        onRefresh();
    }
}
