package com.gdu.command.ui.home;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.gdu.baselibrary.base.RefreshFragment;
import com.gdu.baselibrary.network.AlarmTypeStatictisBean;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.event.RefreshHomeEvent;
import com.gdu.command.ui.alarm.center.AlarmCenterActivityNew;
import com.gdu.command.ui.alarm.center.IAlarmCenterView;
import com.gdu.command.ui.alarm.detail.AlarmDetailActivity;
import com.gdu.command.ui.cases.AlarmRvAdapter;
import com.gdu.command.ui.cases.MyCasesActivity;
import com.gdu.command.ui.cases.PublicAlarmActivity;
import com.gdu.command.ui.cases.deal.DealCaseActivity;
import com.gdu.command.ui.data.DataMonitorShowActivity;
import com.gdu.command.ui.home.banner.TextAdapter;
import com.gdu.command.ui.home.weather.WeatherBeans;
import com.gdu.command.ui.patrol.view.PatrolDiaryActivity;
import com.gdu.command.ui.resource.ResDistributionActivity;
import com.gdu.command.ui.video.view.VideoControlActivity;
import com.gdu.command.views.DealCaseView;
import com.gdu.command.views.drop.AlarmDeviceBean;
import com.gdu.model.alarm.AlarmInfo;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.config.GlobalVariable;
import com.gdu.mqttchat.MqttMessage;
import com.gdu.mqttchat.chat.presenter.ChatService;
import com.gdu.mqttchat.chat.presenter.MessageBean;
import com.gdu.mqttchat.chat.view.ChatActivity;
import com.gdu.utils.MyVariables;
import com.gdu.utils.WeatherQueryHelper;
import com.google.gson.Gson;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.constraintlayout.widget.ConstraintLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends RefreshFragment<HomePresenter>
        implements View.OnClickListener, IHomeView, IAlarmCenterView {

    private TextView mMyCaseLayout;
    private ConstraintLayout mHeaderContentLayout;
    private TextView mVideoControlLayout;
    private TextView tv_patrolDiary;
    private TextView mAlarmCenterLayout;
    private TextView tv_dataMonitoring;
//    private TextView mCaseCommentsLayout;
    private TextView mResDistributionLayout;

    private TextView mPositionTextView;
    private TextView mTempTextView;
    private TextView mWeatherTextView;
    private Banner mBanner;

    private DealCaseView mDealCaseView;

//    private TextView mCaseTotalTextView;
//    private TextView mCaseWaitTextView;
//    private TextView mCaseDealingTextView;
//    private TextView mCaseDealtTextView;
//    private TextView mCaseCommentsTextView;

//    private CaseRvAdapter mAdapter;
    private AlarmRvAdapter mAdapter;

    /** 是否滑到顶部 */
    private boolean isHeader = true;
    /** 头部是否显示 */
    private boolean isHeaderShow = true;

    /**
     * 是否显示批示数据
     */
    private boolean isShowWaitView = false;

    private TextAdapter mTextAdapter;
    private List<MessageBean.DataBean> bannerData = new ArrayList<>();
    private TextView tv_fore_warn;

    private void initView() {
        mMyCaseLayout  = getView().findViewById(R.id.my_case_layout);
        mHeaderContentLayout = getView().findViewById(R.id.header_content_layout);
        mVideoControlLayout = getView().findViewById(R.id.video_control_layout);
        tv_patrolDiary = getView().findViewById(R.id.tv_patrolDiary);
        mAlarmCenterLayout = getView().findViewById(R.id.alarm_center_layout);
        tv_dataMonitoring = getView().findViewById(R.id.tv_dataMonitoring);
//        mCaseCommentsLayout = getView().findViewById(R.id.case_comments_layout);
        mResDistributionLayout = getView().findViewById(R.id.ll_resourceLayout);

        mPositionTextView = getView().findViewById(R.id.position_textview);
        mTempTextView = getView().findViewById(R.id.temp_textview);
        mWeatherTextView = getView().findViewById(R.id.weather_textview);

        mBanner = getView().findViewById(R.id.view_notifyBanner);

        mDealCaseView = getView().findViewById(R.id.deal_case_view);

        tv_fore_warn= getView().findViewById(R.id.tv_fore_warn);

//        mCaseTotalTextView = getView().findViewById(R.id.case_total_textview);
//        mCaseWaitTextView = getView().findViewById(R.id.case_wait_textview);
//        mCaseDealingTextView = getView().findViewById(R.id.case_dealing_textview);
//        mCaseDealtTextView = getView().findViewById(R.id.case_dealt_textview);
//        mCaseCommentsTextView = getView().findViewById(R.id.case_comments_textview);
    }

    private void initListener() {
        mMyCaseLayout.setOnClickListener(this);
        mVideoControlLayout.setOnClickListener(this);
        tv_patrolDiary.setOnClickListener(this);
        tv_dataMonitoring.setOnClickListener(this);
        mAlarmCenterLayout.setOnClickListener(this);
        mResDistributionLayout.setOnClickListener(this);
        tv_fore_warn.setOnClickListener(this);

        mDealCaseView.setOnDealCaseListener(comments -> {
            showProgressDialog("案件处置中...");
            getPresenter().finishCase(comments);
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.my_case_layout:
                // 我的案件
                Intent caseIntent = new Intent(getActivity(), MyCasesActivity.class);
                caseIntent.putExtra(MyConstants.IS_SHOW_WAIT_VIEW_KEY, isShowWaitView);
                startActivity(caseIntent);
                break;

            case R.id.video_control_layout:
                Intent videoIntent = new Intent(getActivity(), VideoControlActivity.class);
                startActivity(videoIntent);
                break;

            case R.id.ll_resourceLayout:
                // 资源分布
                Intent resIntent = new Intent(getActivity(), ResDistributionActivity.class);
                startActivity(resIntent);
                break;

            case R.id.tv_patrolDiary:
                Intent mIntent = new Intent(requireContext(), PatrolDiaryActivity.class);
                startActivity(mIntent);
                break;

            case R.id.tv_dataMonitoring:
                //数据监测
                Intent intent = new Intent(getActivity(), DataMonitorShowActivity.class);
                intent.putExtra(MyVariables.CURRENT_LOCATION, mPositionTextView.getText().toString());
                intent.putExtra(MyVariables.CURRENT_WEATHER,
                        mTempTextView.getText().toString()
                        + "  "
                        + mWeatherTextView.getText().toString());
                startActivity(intent);
                break;

            case R.id.alarm_center_layout:
                // 预警中心
                Intent alarmCenterIntent = new Intent(getActivity(), AlarmCenterActivityNew.class);
                startActivity(alarmCenterIntent);
                break;

            case R.id.tv_fore_warn:
//                Intent forewarnIntent = new Intent(getActivity(), ForewarnStatActivity.class);
//                startActivity(forewarnIntent);

//                Intent forewarnIntent = new Intent(getActivity(), DealResultStatActivity.class);
//                startActivity(forewarnIntent);

//                Intent warnRankIntent = new Intent(getActivity(), WarnDeviceRankActivity.class);
//                startActivity(warnRankIntent);

//                Intent warnRankIntent = new Intent(getActivity(), ArtificialAlarmActivity.class);
//                startActivity(warnRankIntent);

                Intent warnRankIntent = new Intent(getActivity(), PublicAlarmActivity.class);
                startActivity(warnRankIntent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void initEventAndData() {
        EventBus.getDefault().register(this);
        initView();
        initBanner();
        getPresenter().queryWeather();
        getPresenter().getCaseCount();
        getPresenter().setView(getContext(), this, this);
        initRefreshLayout();
        initRecyclerView();
        initListener();
//        mAdapter = new CaseRvAdapter(null);
        mAdapter = new AlarmRvAdapter(null);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.addChildClickViewIds(R.id.tv_instructionBtn, R.id.tv_finishBtn);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(requireContext(), AlarmDetailActivity.class);
            GlobalVariable.sAlarmInfo = mAdapter.getData().get(position);
            startActivity(intent);
        });
//        mAdapter.setOnItemClickListener((adapter, view, position) -> {
//            final IssueBean data = mAdapter.getData().get(position);
//            instructionHandle(data);
//        });
//        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
//            final IssueBean data = mAdapter.getData().get(position);
//            switch (view.getId()) {
//                case R.id.tv_instructionBtn:
//                    instructionHandle(data);
//                    break;
//
//                case R.id.tv_finishBtn:
//                    GlobalVariable.sCurrentIssueBean = data;
//                    mDealCaseView.setVisibility(View.VISIBLE);
//                    break;
//
//                default:
//                    break;
//            }
//        });

        lazyLoad();
    }

    private void instructionHandle(IssueBean dataParam) {
        Intent chatIntent = new Intent(getActivity(), DealCaseActivity.class);
        GlobalVariable.sCurrentIssueBean = dataParam;
        chatIntent.putExtra(GlobalVariable.EXTRA_CASE_STATUS, dataParam.getCaseStatus());
        startActivityForResult(chatIntent, GlobalVariable.REQUEST_COMMENT_CASE_CODE);
    }

    private void initBanner() {
        mBanner.setLoopTime(3000);
        mTextAdapter = new TextAdapter(requireContext(), bannerData);
        mTextAdapter.setOnBannerListener((clickData, position) -> {
            MyLogUtils.d("data = " + clickData.toString());
            final MessageBean.DataBean data = (MessageBean.DataBean) clickData;
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra(GlobalVariable.EXTRA_CASE_ID, data.getMsgTopic());
            startActivity(intent);
        });
        mBanner.setAdapter(mTextAdapter).addBannerLifecycleObserver(this);
    }

    /**
     * 从消息中获取发送时间
     * @param content
     * @return
     */
    private String getSendTimeFromMessage(String content){
        MqttMessage mqttMessage = new Gson().fromJson(content, MqttMessage.class);
        return mqttMessage.getSendTime();
    }

    /**
     * 随机获取案件图片
     * @return
     */
    private int getRandomPicture(){
        Random random = new Random();
        int casePicture = random.nextInt(4);
        int resId;
        switch (casePicture){
            case 1:
                resId = R.mipmap.icon_case_default2;
                break;

            case 2:
                resId = R.mipmap.icon_case_default3;
                break;

            case 3:
                resId = R.mipmap.icon_case_default4;
                break;

            default:
                resId = R.mipmap.icon_case_default1;
                break;
        }
        return resId;
    }


    @Override
    protected void lazyLoad() {
        getPresenter().loadData();
        downLoadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void showData(List<IssueBean> beanList) {
        MyLogUtils.d("showData() listSize = " + beanList.size());
        setLoadData(mAdapter, beanList);
    }

    @Override
    public void setWeatherBeans(WeatherBeans weatherBeans) {
        if (weatherBeans == null) {
            return;
        }
        int temp = (int) weatherBeans.getMain().getTemp();
        temp = (int) (temp - 273.15);
        int id = weatherBeans.getWeather().get(0).getId();
        String weatherName = WeatherQueryHelper.getWeatherName(id);
        mTempTextView.setText(temp + "℃");
        mWeatherTextView.setText(weatherName);
        mPositionTextView.setText(weatherBeans.getName());
    }

    @Override
    public void setCaseCount(Map<String, Double> map) {
//        mCaseTotalTextView.setText(map.get("caseCount").intValue() + "");
//        mCaseWaitTextView.setText(map.get("caseCountByWait").intValue()+ "");
//        mCaseDealingTextView.setText(map.get("caseCountByMaking").intValue() + "");
//        mCaseDealtTextView.setText(map.get("caseCountByAlready").intValue() + "");
    }

    @Override
    public void setCommentsCount(int count) {
//        mCaseCommentsTextView.setText(count + "");
    }

    @Override
    public void showComments() {
        isShowWaitView = true;
//        mCaseCommentsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToast(String toast) {
        ToastUtil.s(toast);
    }

    @Override
    public void showOrHidePb(boolean isShow, String tip) {
        if (isShow) {
            showProgressDialog(tip);
        } else {
            hideProgressDialog();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshHome(RefreshHomeEvent refreshHomeEvent) {
        getPresenter().removeCase(refreshHomeEvent.getType(), refreshHomeEvent.getRemoveCaseId());
        getPresenter().loadData();
    }

    private void downLoadData(){
        ChatService chatService = RetrofitClient.getAPIService(ChatService.class);
        Call<MessageBean> call = chatService.getChatHisList();
        call.enqueue(new Callback<MessageBean>() {
            @Override
            public void onResponse(Call<MessageBean> call, Response<MessageBean> response) {
                if (response.body() != null) {
                    List<MessageBean.DataBean> mCaseChatList = response.body().getData();
                    CommonUtils.listAddAllAvoidNPE(bannerData, mCaseChatList);
                    mTextAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MessageBean> call, Throwable t) {
            }
        });
    }

    @Override
    public void showAlarmList(List<AlarmInfo> alarmInfos) {
        final String sizeStr = alarmInfos == null ? "0" : alarmInfos.size() + "";
        hideProgressDialog();
        MyLogUtils.d("showAlarmList() listSize = " + sizeStr);
        mRefreshLayout.setEnableLoadMore(!getPresenter().getFinishStatus());
        setLoadData(mAdapter, alarmInfos);
//        refreshFinish();
//        loadMoreFinish();
    }

    @Override
    public void showDeviceList(List<AlarmDeviceBean.DataBean.RowsBean> devList) {

    }

    @Override
    public void attentionResult(int alarmId) {

    }

    @Override
    public void showAlarmTypeStatisticsData(List<AlarmTypeStatictisBean.DataDTO> data) {

    }

    @Override
    public void callbackIsAppMenu(boolean isAppMenu) {
        GlobalVariable.isAppMenu = isAppMenu;
        getPresenter().loadDataNew(isAppMenu ? 1 : 3);
    }
}