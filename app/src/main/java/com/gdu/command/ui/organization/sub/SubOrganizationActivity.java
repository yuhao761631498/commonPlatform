package com.gdu.command.ui.organization.sub;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.baselibrary.base.BaseRVAdapter;
import com.gdu.baselibrary.base.RefreshActivity;
import com.gdu.command.R;
import com.gdu.command.ui.organization.OrganizationPersonDetailActivity;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.organization.OrganizationEmployeeBean;
import com.gdu.model.organization.OrganizationInfo;
import com.gdu.utils.MyVariables;
import com.gyf.immersionbar.ImmersionBar;

/**
 * 视频详情界面
 */
public class SubOrganizationActivity extends RefreshActivity<SubOrganizationPresenter> implements View.OnClickListener,ISubOrganizationView {

    private ImageView mBackImageView;
    private TextView mOrgNameTextView;
    private BaseRVAdapter<OrganizationInfo.UseBean> mOrgAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sub_organization;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(R.color.color_224CD0).init();
        getPresenter().setView(this);
        initView();
        initData();
        initListener();
    }

    private void initData() {
//        getPresenter().searchOrgPersonList();
        mOrgNameTextView.setText(GlobalVariable.sOrganizationInfo.getDeptName());
    }

    private void initListener() {
        mBackImageView.setOnClickListener(this);
    }

    private void initView() {
        mBackImageView = findViewById(R.id.back_imageview);
        mOrgNameTextView = findViewById(R.id.org_name_textview);
        initRefreshLayout();
        initRecyclerView();

        mOrgAdapter = new BaseRVAdapter<OrganizationInfo.UseBean>(R.layout.item_employee) {
            @Override
            public void onBindVH(BaseViewHolder holder, OrganizationInfo.UseBean data, int position) {
                holder.setText(R.id.employee_name_textview, data.getUsername());
//                holder.setText(R.id.employee_type_textview, data.getUsername());
            }
        };
        mOrgAdapter.setOnItemClickListener(((adapter1, view, position) -> {
            Intent intent = new Intent();
            intent.setClass(SubOrganizationActivity.this, OrganizationPersonDetailActivity.class);
            intent.putExtra(MyVariables.ORG_EMPLOYEE_BEAN,
                    (OrganizationInfo.UseBean)adapter1.getData().get(position));
            startActivity(intent);
        }));
        setLoadData(mOrgAdapter, GlobalVariable.sOrganizationInfo.getUsers());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mOrgAdapter);
    }

    @Override
    public void showPersonList(OrganizationEmployeeBean bean) {
        setLoadData(mOrgAdapter, bean.getRecords());
    }

    @Override
    public void showEmptyView() {
        final View emptyDataView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_view, null);
        TextView emptyTipTv = emptyDataView.findViewById(R.id.tv_empty_tip);
        emptyTipTv.setText("暂无数据");
        mOrgAdapter.setEmptyView(emptyDataView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mOrgAdapter);
        setLoadData(mOrgAdapter, null);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_imageview:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void loadData() {
        finishRefresh();
    }

}