package com.gdu.command.ui.organization;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.command.ui.my.LoginActivity;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.organization.OrganizationEmployeeBean;
import com.gdu.model.organization.OrganizationInfo;
import com.google.gson.Gson;
import com.rxjava.rxlife.RxLife;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizationPresenter extends BasePresenter {

    /**
     * 组织机构获取成功
     */
    private final int ORGANIZATION_SUCCEED = 0x01;
    /**
     * 组织机构获取失败
     */
    private final int ORGANIZATION_FAILED = 0x02;
    /**
     * 获取组织联系人成功
     */
    private final int CONTACT_PERSON_SUCCESS = 0x03;
    /**
     * 获取组织联系人失败
     */
    private final int CONTACT_PERSON_FAIL = 0x04;
    private final int CONTACT_PERSON_EMPTY = 0x05;

    private Gson mGson;
    private Handler mHandler;
    private IOrganizationView mOrganizationView;

    private List<OrganizationInfo> mOrganizationInfos;

    public OrganizationPresenter(){
        mGson = new Gson();
        initHandler();
    }

    public void setView(IOrganizationView organizationView){
        mOrganizationView = organizationView;
    }

    private void initHandler() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case ORGANIZATION_SUCCEED:
                        mOrganizationView.showOrganization(mOrganizationInfos);
                        break;
                    case ORGANIZATION_FAILED:

                        break;
                    case CONTACT_PERSON_SUCCESS:

                        break;
                    case CONTACT_PERSON_EMPTY:
                        mOrganizationView.showEmptyView();
                        break;
                }
            }
        };
    }

    public void loadData(){
        OrganizationService organizationService = RetrofitClient.getAPIService(OrganizationService.class);
        Call<BaseBean<List<OrganizationInfo>>> call = organizationService.getDeptTree();
        call.enqueue(new Callback<BaseBean<List<OrganizationInfo>>>() {
            @Override
            public void onResponse(Call<BaseBean<List<OrganizationInfo>>> call, Response<BaseBean<List<OrganizationInfo>>> response) {
                final boolean isUnEmptyData =
                        response.body() != null && response.body().code == 0 && response.body().data != null;
                if (isUnEmptyData) {
                    parseOrganization(response.body());
                }  else {
                    mHandler.sendEmptyMessage(ORGANIZATION_FAILED);
                }
            }

            @Override
            public void onFailure(Call<BaseBean<List<OrganizationInfo>>> call, Throwable t) {
               mHandler.sendEmptyMessage(ORGANIZATION_FAILED);
            }
        });
    }

    /**
     * 查询联系人
     * @param
     */
    public void searchContact(String searchContent) {
        Map<String, Object> map  = new HashMap<>();
        map.put("currentPage", 1);
        map.put("pageSize", 100);
        map.put("content", searchContent);
        String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),strEntity);
        OrganizationService caseService = RetrofitClient.getAPIService(OrganizationService.class);
        caseService.getSearchEmployee(body).enqueue(new Callback<BaseBean<OrganizationEmployeeBean>>() {
            @Override
            public void onResponse(Call<BaseBean<OrganizationEmployeeBean>> call, Response<BaseBean<OrganizationEmployeeBean>> beanBaseBean) {

                if (beanBaseBean.code() == 401) {
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                }

                if (beanBaseBean != null && beanBaseBean.body().data != null) {
                    if (beanBaseBean.body().data.getRecords() != null && beanBaseBean.body().data.getRecords().size() != 0) {
                        mOrganizationView.showSearchList(beanBaseBean.body().data);
                    } else {
                        mOrganizationView.showEmptyView();
                    }
                } else {
                    mOrganizationView.showEmptyView();
                }
            }

            @Override
            public void onFailure(Call<BaseBean<OrganizationEmployeeBean>> call, Throwable t) {
//                mOrganizationView.showEmptyView();
            }
        });
    }

    private void parseOrganization(BaseBean body){
        if (body.code == 0) {
            mOrganizationInfos = (List<OrganizationInfo>) body.data;
            mHandler.sendEmptyMessage(ORGANIZATION_SUCCEED);
        } else {
            mHandler.sendEmptyMessage(ORGANIZATION_FAILED);
        }
    }
}
