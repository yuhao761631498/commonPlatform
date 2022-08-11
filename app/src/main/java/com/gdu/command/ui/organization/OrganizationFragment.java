package com.gdu.command.ui.organization;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.baselibrary.base.BaseRVAdapter;
import com.gdu.baselibrary.base.RefreshFragment;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.command.R;
import com.gdu.command.ui.organization.sub.SubOrganizationActivity;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.organization.OrganizationEmployeeBean;
import com.gdu.model.organization.OrganizationInfo;
import com.gdu.utils.MyVariables;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 组织机构
 */
public class OrganizationFragment extends RefreshFragment<OrganizationPresenter> implements IOrganizationView, View.OnClickListener {

    private BaseRVAdapter<OrganizationInfo> mAdapter;

    private BaseRVAdapter<OrganizationEmployeeBean.RecordsDTO> mAdapterEmployee;

    protected RecyclerView mRecyclerViewEmployee;
    private ImageView mSearchImageView, mEditSearchIv, mClearEditIv;
    private RelativeLayout mSearchLayout;
    private EditText mSearchEdit;

    private boolean isShowSearchView = false;
    private ImageView iv_left_back;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_organization;
    }

    private void initView() {
        mSearchImageView = getView().findViewById(R.id.search_imageview);
        mSearchImageView.setOnClickListener(this);
        mSearchLayout = getView().findViewById(R.id.search_edit_layout);
        mSearchLayout.setOnClickListener(this);
        mEditSearchIv = getView().findViewById(R.id.edit_search);
        mEditSearchIv.setOnClickListener(this);
        mSearchEdit = getView().findViewById(R.id.search_edit_text);
        mClearEditIv = getView().findViewById(R.id.search_edit_clear_imageview);
        mClearEditIv.setOnClickListener(this);
        mRecyclerViewEmployee = getView().findViewById(R.id.mRecyclerViewEmployee);

        iv_left_back = ((ImageView) getView().findViewById(R.id.iv_left_back));
        iv_left_back.setOnClickListener(this);

        //软键盘搜索按钮监听
        mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//按下搜索
                    if (!TextUtils.isEmpty(mSearchEdit.getText().toString())) {
                        getPresenter().searchContact(mSearchEdit.getText().toString());
                    } else {
                        ToastUtil.s("请输入搜索内容");
                    }
                }
                return true; //返回true，保留软键盘。false，隐藏软键盘
            }
        });

        //软键盘输入监听
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    getPresenter().searchContact(s.toString());
                }
            }
        });

    }

    @Override
    protected void initEventAndData() {
        getPresenter().setView(this);
        initRefreshLayout();
        initRecyclerView();
        initView();
        mAdapter = new BaseRVAdapter<OrganizationInfo>(R.layout.item_organization) {
            @Override
            public void onBindVH(BaseViewHolder holder, OrganizationInfo data, int position) {
                holder.setImageResource(R.id.org_type_imageview, R.mipmap.icon_org);
                holder.setText(R.id.organization_name_textview, data.getDeptName());
            }
        };
        mAdapter.setOnItemClickListener(((adapter, view, position) -> {
            Intent organizationIntent = new Intent(getActivity(), SubOrganizationActivity.class);
            GlobalVariable.sOrganizationInfo = (OrganizationInfo) adapter.getData().get(position);
            startActivity(organizationIntent);
        }));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        lazyLoad();
        setAdapterEmployee();
    }

    /**
     * 搜索联系人
     */
    private void setAdapterEmployee() {
        mAdapterEmployee = new BaseRVAdapter<OrganizationEmployeeBean.RecordsDTO>(R.layout.item_employee) {
            @Override
            public void onBindVH(BaseViewHolder holder, OrganizationEmployeeBean.RecordsDTO data, int position) {
                holder.setText(R.id.employee_name_textview, data.getEmployeeName());
                holder.setText(R.id.employee_type_textview, data.getEmployeeIdentity());
            }
        };
        mAdapterEmployee.setOnItemClickListener(((adapter, view, position) -> {
            Intent intent = new Intent();
            intent.setClass(getContext(), OrganizationPersonDetailActivity.class);
            intent.putExtra(MyVariables.ORG_EMPLOYEE_BEAN,
                    (OrganizationEmployeeBean.RecordsDTO) adapter.getData().get(position));
            startActivity(intent);
            CommonUtils.hideInputSoftKey(getContext(), mSearchEdit);
        }));
        mRecyclerViewEmployee.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewEmployee.setAdapter(mAdapterEmployee);
    }

    @Override
    protected void lazyLoad() {
        getPresenter().loadData();
    }

    @Override
    public void showOrganization(List<OrganizationInfo> organizationInfo) {
        if (organizationInfo != null) {
            OrganizationInfo tmp = organizationInfo.get(0);
            List<OrganizationInfo> sub = tmp.getChildren();
            setLoadData(mAdapter, sub);
        }
    }

    /**
     * 显示搜索联系人
     */
    @Override
    public void showSearchList(OrganizationEmployeeBean bean) {
        if (bean.getRecords() != null) {
            setLoadData(mAdapterEmployee, bean.getRecords());
            CommonUtils.hideInputSoftKey(getContext(), mSearchEdit);
        } else {
            showEmptyView();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_imageview: //标题搜索按钮
                isShowSearchView = !isShowSearchView;
                mSearchLayout.setVisibility(isShowSearchView ? View.VISIBLE : View.GONE);
                mRecyclerViewEmployee.setVisibility(isShowSearchView ? View.VISIBLE : View.GONE);
                mRecyclerView.setVisibility(isShowSearchView ? View.GONE : View.VISIBLE);
                //弹出键盘
                if (isShowSearchView) {
                    CommonUtils.showInputSoftKey(getContext(), mSearchEdit);
                    setAdapterEmployee();
                } else {
                    CommonUtils.hideInputSoftKey(getContext(), mSearchEdit);
                    mSearchEdit.setText("");
                    setLoadData(mAdapterEmployee, null);
                }
                break;
            case R.id.edit_search: //输入框内搜索按钮
                if (!TextUtils.isEmpty(mSearchEdit.getText().toString())) {
                    getPresenter().searchContact(mSearchEdit.getText().toString());
                } else {
                    ToastUtil.s("请输入搜索内容");
                }
                break;
            case R.id.search_edit_clear_imageview:
                mSearchEdit.setText("");
                break;

            case R.id.iv_left_back:
                getActivity().finish();
                break;
        }
    }

    /**
     * 没有搜索到显示空布局
     */
    @Override
    public void showEmptyView() {
        final View emptyDataView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_view, null);
        TextView emptyTipTv = emptyDataView.findViewById(R.id.tv_empty_tip);
        emptyTipTv.setText("没有找到相关用户");
        mAdapterEmployee.setEmptyView(emptyDataView);
        mRecyclerViewEmployee.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewEmployee.setAdapter(mAdapterEmployee);
        setLoadData(mAdapterEmployee, null);
    }
}