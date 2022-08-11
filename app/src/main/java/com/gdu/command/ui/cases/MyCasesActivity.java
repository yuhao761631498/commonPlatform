package com.gdu.command.ui.cases;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.event.RefreshMyCaseEvent;
import com.gdu.command.ui.cases.deal.CaseClosedActivity;
import com.gdu.command.ui.cases.deal.DealCaseActivity;
import com.gdu.command.ui.cases.dealt.DealtCaseActivity;
import com.gdu.command.views.DealCaseView;
import com.gdu.model.cases.CaseStatus;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.cases.MyCaseInfo;
import com.gdu.model.config.GlobalVariable;
import com.gdu.utils.DialogUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 我的案件
 */
public class MyCasesActivity extends BaseActivity<MyCasesPresenter>
        implements IMyCaseView, View.OnClickListener {

    private ImageView mBackImageView;
    private ImageView addCaseIv;
    private CaseRvAdapter mCaseAdapter;
    /** 未处理的案件 */
    private List<IssueBean> issueNoHandleList;
    /** 待处理的案件 */
    private List<IssueBean> issueWaitList;
    /** 处理中的案件 */
    private List<IssueBean> issueHandlingList;
    /** 已处理的案件 */
    private List<IssueBean> issueHandledList;

    private LinearLayout mNoHandleLayout;
    private LinearLayout mWaitLayout;
    private LinearLayout mDealingLayout;
    private LinearLayout mDealtLayout;
    private TextView mTvIssueNoHandle;
    private TextView mTvWaitHandle;
    private TextView mTvIssueHandling;
    private TextView mTvIssueHandled;
    private View mNoHandleLine;
    private View mWaitLine;
    private View mDealingLine;
    private View mDealtLine;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private DealCaseView mDealCaseView;

    private CaseStatus mSelectedStatus;

    private DialogUtils mDialogUtils;

    /** 是否显示待批示案件 */
    private boolean isShowWaitView = false;

    public static void start(Context context) {
        Intent starter = new Intent(context, MyCasesActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_case;
    }

    @Override
    protected void initialize() {
        MyLogUtils.i("initialize()");
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();

        isShowWaitView = getIntent().getBooleanExtra(MyConstants.IS_SHOW_WAIT_VIEW_KEY, false);
        MyLogUtils.i("initialize() isShowWaitView = " + isShowWaitView);

        EventBus.getDefault().register(this);
        getPresenter().initView(this);
        mDialogUtils = new DialogUtils(this);
        initView();
        initListener();
        mCaseAdapter = new CaseRvAdapter(null);
        //是否隐藏头部
        mCaseAdapter.setHeaderWithEmptyEnable(true);
        //是否隐藏底部
        mCaseAdapter.setFooterWithEmptyEnable(true);
        mCaseAdapter.addChildClickViewIds(R.id.tv_instructionBtn, R.id.handle_case_layout,
                R.id.tv_finishBtn);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mCaseAdapter);

        mWaitLayout.setVisibility(isShowWaitView ? View.VISIBLE : View.GONE);

        if (mCaseAdapter.getLoadMoreModule() != null) {
            mCaseAdapter.getLoadMoreModule().setAutoLoadMore(false);
            mCaseAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
            mCaseAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
                onLoadMore();
            });
        }

        mCaseAdapter.setOnItemClickListener((adapter, view, position) -> {
            final IssueBean data = mCaseAdapter.getData().get(position);
            if (data.getCaseStatus() == CaseStatus.HANDLED.getKey()) {
                Intent chatIntent = new Intent(getActivity(), DealtCaseActivity.class);
                GlobalVariable.sCurrentIssueBean = data;
                chatIntent.putExtra(GlobalVariable.EXTRA_CASE_STATUS, data.getCaseStatus());
                startActivityForResult(chatIntent, GlobalVariable.REQUEST_COMMENT_CASE_CODE);
            }
        });

        mCaseAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            final IssueBean data = mCaseAdapter.getData().get(position);
            switch (view.getId()) {
                case R.id.tv_instructionBtn:
                    instructionHandle(data);
                    break;

                case R.id.handle_case_layout:
                    mDialogUtils.createDialogWith2Btn("接警", data.getCaseName(),
                            "取消", "确认",
                            v1 -> {
                                mDialogUtils.cancelDialog();
                                if (v1.getId() == R.id.dialog_btn_sure) {
                                    getPresenter().updateCaseStatus(data.getId(),
                                            data.getDesignateId());
                                }
                            });
                    break;

                case R.id.tv_finishBtn:
                    Intent chatIntent = new Intent(getContext(), CaseClosedActivity.class);
                    GlobalVariable.sCurrentIssueBean = data;
                    chatIntent.putExtra(GlobalVariable.EXTRA_CASE_STATUS, data.getCaseStatus());
                    getContext().startActivity(chatIntent);
                    break;

                default:
                    break;
            }
        });

        loadData();
    }

    private void instructionHandle(IssueBean dataParam) {
        MyLogUtils.i("instructionHandle()");
        Intent chatIntent = new Intent(getActivity(), DealCaseActivity.class);
        GlobalVariable.sCurrentIssueBean = dataParam;
        chatIntent.putExtra(GlobalVariable.EXTRA_CASE_STATUS, dataParam.getCaseStatus());
        startActivityForResult(chatIntent, GlobalVariable.REQUEST_COMMENT_CASE_CODE);
    }

    private void initListener() {
        MyLogUtils.i("initListener()");
        mNoHandleLayout.setOnClickListener(this);
        mWaitLayout.setOnClickListener(this);
        mDealingLayout.setOnClickListener(this);
        mDealtLayout.setOnClickListener(this);
        mBackImageView.setOnClickListener(this);
        addCaseIv.setOnClickListener(this);

        mDealCaseView.setOnDealCaseListener(comments -> {
            MyLogUtils.i("mDealCaseView onConfirmClick() comments = " + comments);
            showProgressDialog("案件处置中...");
            getPresenter().finishCase(comments);
        });
    }

    private void initView() {
        MyLogUtils.i("initView()");
        mBackImageView = findViewById(R.id.back_imageview);
        addCaseIv = findViewById(R.id.iv_addCase);

        mNoHandleLayout = findViewById(R.id.ll_caseWaitLayout);
        mWaitLayout = findViewById(R.id.ll_caseTotalLayout);
        mDealingLayout = findViewById(R.id.ll_caseDealingLayout);
        mDealtLayout = findViewById(R.id.ll_caseDealtLayout);

        mTvIssueNoHandle = findViewById(R.id.case_wait_textview);
        mTvWaitHandle = findViewById(R.id.case_total_textview);
        mTvIssueHandling = findViewById(R.id.case_dealing_textview);
        mTvIssueHandled = findViewById(R.id.case_dealt_textview);

        mNoHandleLine = findViewById(R.id.view_caseWaitLine);
        mWaitLine = findViewById(R.id.view_caseTotalLine);
        mDealingLine = findViewById(R.id.view_caseDealinglLine);
        mDealtLine = findViewById(R.id.view_caseDealtLine);

        mDealCaseView = findViewById(R.id.deal_case_view);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRefreshLayout = findViewById(R.id.mRefreshLayout);
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnableRefresh(true);
            mRefreshLayout.setEnableLoadMore(false);
            mRefreshLayout.setEnableAutoLoadMore(false);
            mRefreshLayout.setDisableContentWhenLoading(true);
            mRefreshLayout.setDisableContentWhenRefresh(true);
            mRefreshLayout.setOnRefreshListener(refreshlayout -> loadData());
        }

        if (isShowWaitView) {
            mWaitLine.setVisibility(View.VISIBLE);
            mNoHandleLine.setVisibility(View.INVISIBLE);
            mSelectedStatus = CaseStatus.WAIT_COMMENT;
        } else {
            mWaitLine.setVisibility(View.GONE);
            mNoHandleLine.setVisibility(View.VISIBLE);
            mSelectedStatus = CaseStatus.NO_HANDLE;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_caseTotalLayout:
                changeUI(CaseStatus.WAIT_COMMENT);
                break;
            case R.id.ll_caseWaitLayout:
                changeUI(CaseStatus.NO_HANDLE);
                break;
            case R.id.ll_caseDealingLayout:
                changeUI(CaseStatus.HANDLING);
                break;
            case R.id.ll_caseDealtLayout:
                changeUI(CaseStatus.HANDLED);
                break;
            case R.id.back_imageview:
                finish();
                break;
            case R.id.iv_addCase:
                openActivity(AddCaseActivity.class);
                break;
            default:
                break;
        }
    }

    public void changeUI(CaseStatus status){
        MyLogUtils.i("changeUI() status = " + status);
        mSelectedStatus = status;
        switch (status){
            case NO_HANDLE:
                changeUiDetail(View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        issueNoHandleList);
                if (mCaseAdapter.getLoadMoreModule() != null) {
                    judgeIsSetLoadEnd(getPresenter().getCurrentNoHandleCaseInfo());
                }
                break;

            case WAIT_COMMENT:
                changeUiDetail(View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE,
                        issueWaitList);
                if (mCaseAdapter.getLoadMoreModule() != null) {
                    mCaseAdapter.getLoadMoreModule().setEnableLoadMore(false);
                }
                break;

            case HANDLING:
                changeUiDetail(View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE,
                        issueHandlingList);
                if (mCaseAdapter.getLoadMoreModule() != null) {
                    judgeIsSetLoadEnd(getPresenter().getCurrentHandlingCaseInfo());
                }
                break;

            case HANDLED:
                changeUiDetail(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        issueHandledList);
                if (mCaseAdapter.getLoadMoreModule() != null) {
                    judgeIsSetLoadEnd(getPresenter().getCurrentHandledCaseInfo());
                }
                break;

            default:
                break;
        }
    }

    private void changeUiDetail(int noHandleVisible, int waitHandleVisible,
                                int dealingVisible, int dealtLineVisible,
                                List<IssueBean> updateData) {
        MyLogUtils.i("changeUiDetail()");
        mNoHandleLine.setVisibility(noHandleVisible);
        mWaitLine.setVisibility(waitHandleVisible);
        mDealingLine.setVisibility(dealingVisible);
        mDealtLine.setVisibility(dealtLineVisible);
        mRecyclerView.setAdapter(null);
        mRecyclerView.setAdapter(mCaseAdapter);
        setLoadData(mCaseAdapter, updateData);
    }

    public void loadData() {
        MyLogUtils.i("loadData()");
        getPresenter().loadData();
        if (isShowWaitView) {
            getPresenter().getWaitCommentCase();
        }
    }

    private void setLoadData(BaseQuickAdapter adapter, List<IssueBean> data) {
        if (CommonUtils.isEmptyList(data)) {
            MyLogUtils.i("setLoadData() dataSize = 0");
        } else {
            MyLogUtils.i("setLoadData() dataSize = " + data.size());
        }
        adapter.setList(data);
    }

    /**
     * 结束刷新
     */
    public void finishRefresh() {
        MyLogUtils.i("finishRefresh()");
        if (mRefreshLayout != null && mRefreshLayout.getState() == RefreshState.Refreshing) {
            mRefreshLayout.finishRefresh();
        }
    }

    /**
     * 结束刷新
     */
    public void loadMoreComplete() {
        MyLogUtils.i("loadMoreComplete()");
        if (mCaseAdapter.getLoadMoreModule() != null) {
            mCaseAdapter.getLoadMoreModule().loadMoreComplete();
        }
    }

    @Override
    public void issueNoHandle(List<IssueBean> data, boolean isLoadMoreData) {
        MyLogUtils.i("issueNoHandle() dataSize = " + data.size() + "; isLoadMoreData = " + isLoadMoreData);
        finishRefresh();
        final MyCaseInfo mCaseInfo = getPresenter().getCurrentNoHandleCaseInfo();
        if (CommonUtils.isEmptyList(data) || mCaseInfo == null) {
            mTvIssueNoHandle.setText("0");
        }
        if (mSelectedStatus == CaseStatus.NO_HANDLE) {
            setLoadData(mCaseAdapter, data);
        }
        if (isLoadMoreData) {
            loadMoreComplete();
            judgeIsSetLoadEnd(mCaseInfo);
        }
        issueNoHandleList = data;
        mTvIssueNoHandle.setText(mCaseInfo.getTotal() + "");
    }

    @Override
    public void issueHandling(List<IssueBean> data, boolean isLoadMoreData) {
        MyLogUtils.i("issueHandling() dataSize = " + data.size() + "; isLoadMoreData = " + isLoadMoreData);
        finishRefresh();
        final MyCaseInfo mCaseInfo = getPresenter().getCurrentHandlingCaseInfo();
        if (CommonUtils.isEmptyList(data) || mCaseInfo == null) {
            mTvIssueHandling.setText("0");
        }
        if (mSelectedStatus == CaseStatus.HANDLING) {
            setLoadData(mCaseAdapter, data);
        }
        if (isLoadMoreData) {
            loadMoreComplete();
            judgeIsSetLoadEnd(mCaseInfo);
        }
        issueHandlingList = data;
        mTvIssueHandling.setText(mCaseInfo.getTotal() + "");
    }

    @Override
    public void issueWaitHandled(List<IssueBean> data, boolean isLoadMoreData) {
        MyLogUtils.i("issueWaitHandled() dataSize = " + data.size() + "; isLoadMoreData = " + isLoadMoreData);
        if (CommonUtils.isEmptyList(data)) {
            mTvWaitHandle.setText("0");
        }
        if (mSelectedStatus == CaseStatus.WAIT_COMMENT) {
            setLoadData(mCaseAdapter, data);
        }
        if (isLoadMoreData) {
            mCaseAdapter.getLoadMoreModule().setEnableLoadMore(false);
        }
        issueWaitList = data;
        mTvWaitHandle.setText(data.size() + "");
    }

    @Override
    public void issueHandled(List<IssueBean> data, boolean isLoadMoreData) {
        MyLogUtils.i("issueHandled() dataSize = " + data.size() + "; isLoadMoreData = " + isLoadMoreData);
        finishRefresh();
        final MyCaseInfo mCaseInfo = getPresenter().getCurrentHandledCaseInfo();
        if (CommonUtils.isEmptyList(data)) {
            mTvIssueHandled.setText("0");
        }
        if (mSelectedStatus == CaseStatus.HANDLED) {
            setLoadData(mCaseAdapter, data);
        }
        if (isLoadMoreData) {
            loadMoreComplete();
            judgeIsSetLoadEnd(mCaseInfo);
        }
        issueHandledList = data;
        mTvIssueHandled.setText(mCaseInfo.getTotal() + "");
    }

    @Override
    public void loadMoreCompleted() {
        MyLogUtils.i("loadMoreEnd()");
        mCaseAdapter.getLoadMoreModule();
        mCaseAdapter.getLoadMoreModule().loadMoreEnd();
    }

    private void judgeIsSetLoadEnd(MyCaseInfo mCaseInfo) {
        if (mCaseInfo != null && mCaseInfo.isLoadFinish()) {
            mCaseAdapter.getLoadMoreModule().setEnableLoadMore(false);
        } else {
            mCaseAdapter.getLoadMoreModule().setEnableLoadMore(true);
        }
    }

    @Override
    public void toast(String toast) {
        if ("获取列表数据失败".equals(toast)) {
            finishRefresh();
            loadMoreComplete();
        } else {
            ToastUtil.s(toast);
        }
    }

    @Override
    public void setCommentsCount(List<IssueBean> data) {
        MyLogUtils.i("setCommentsCount() dataSize = " + data.size());
        finishRefresh();
        if (data == null) {
            return;
        }
        runOnUiThread(() -> {
            if (CommonUtils.isEmptyList(data)) {
                mTvWaitHandle.setText("0");
            }
            if (mSelectedStatus == CaseStatus.WAIT_COMMENT) {
                setLoadData(mCaseAdapter, data);
                mCaseAdapter.getLoadMoreModule().setEnableLoadMore(false);
            }
            issueWaitList = data;
            mTvWaitHandle.setText(data.size() + "");
        });
    }

    @Override
    public void isShowWaitView(boolean isShow) {
        MyLogUtils.i("isShowWaitView() isShow = " + isShow);
        runOnUiThread(() -> {
            mWaitLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void showOrHidePb(boolean isShow, String tip) {
        MyLogUtils.i("showOrHidePb() isShow = " + isShow + "; tip = " + tip);
        if (isShow) {
            showProgressDialog(tip);
        } else {
            hideProgressDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode,  resultCode, data);
        switch (requestCode){
            case GlobalVariable.REQUEST_DEAL_CASE_CODE:
                if (resultCode == 0) {
                    if (GlobalVariable.sCurrentIssueBean != null) {
                        getPresenter().removeCaseFromHandling(GlobalVariable.sCurrentIssueBean.getId());
                    }
                    getPresenter().loadData();
                }
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshHome(RefreshMyCaseEvent refreshMyCaseEvent) {
//        if (GlobalVariable.sCurrentIssueBean != null) {
//            getPresenter().removeCaseFromHandling(GlobalVariable.sCurrentIssueBean.getId());
//        }
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onLoadMore() {
        MyLogUtils.i("onLoadMore()");
        if (mSelectedStatus != CaseStatus.WAIT_COMMENT) {
            getPresenter().loadMoreData(mSelectedStatus.getKey());
        }
    }
}
