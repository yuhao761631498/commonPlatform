package com.gdu.command.ui.data;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.command.R;
import com.gdu.model.data.DataAlarmTotalBean;
import com.gdu.model.data.DataCaseTotalBean;
import com.gdu.model.data.DataHighTotal;

import java.util.ArrayList;
import java.util.List;

public class WarnDeviceRankActivity extends BaseActivity<WarnDeviceRankPresenter> implements IDataMonitorShowView {

    private List<DataCaseRankBean.DataBean> caseRankList = new ArrayList<>();
    private DataProgressAdapter progressAdapter;
    private RecyclerView rc_case_deal_rank;
    private ImageView iv_back;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_warn_device_rank;
    }

    @Override
    protected void initialize() {
        getPresenter().setView(this);
        getPresenter().requestData();
        initView();
        initListener();
        initAdapter();
    }

    private void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        rc_case_deal_rank = ((RecyclerView) findViewById(R.id.rc_case_deal_rank));
        iv_back = ((ImageView) findViewById(R.id.iv_back));
    }

    private void initAdapter() {
        progressAdapter = new DataProgressAdapter(caseRankList);
        rc_case_deal_rank.setLayoutManager(new LinearLayoutManager(this));
        rc_case_deal_rank.setAdapter(progressAdapter);
    }

    @Override
    public void getCaseTotalCount(DataCaseTotalBean bean) {

    }

    @Override
    public void getAlarmTotalCount(DataAlarmTotalBean bean) {

    }

    @Override
    public void getHighTotalCount(DataHighTotal bean) {

    }

    @Override
    public void getCaseFromByYear(List<DataCaseResFromBean.CaseInfoSourceCountVOSBean> bean) {

    }

    @Override
    public void getCaseDealRank(List<DataCaseRankBean.DataBean> bean) {
        boolean isNotEmpty = null != bean && bean.size() != 0;
        if (isNotEmpty) {
            caseRankList.clear();
            caseRankList.addAll(bean);
            progressAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getAlarmResInfo(AlarmResInfoBean.DataBean bean) {

    }

    @Override
    public void getAlarmByYearDistribute(List<DataAlarmByYearBean.DataBean> bean) {

    }
}