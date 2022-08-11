package com.gdu.command.ui.organization.sub;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.command.ui.organization.OrganizationService;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.organization.OrganizationEmployeeBean;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubOrganizationPresenter extends BasePresenter {

    private ISubOrganizationView mSubOrganizationView;
    private Gson mGson;

    public void setView(ISubOrganizationView subOrganizationView) {
        mSubOrganizationView = subOrganizationView;
        mGson = new Gson();
    }

    //请求人员列表信息
    public void searchOrgPersonList() {
        String deptCode = GlobalVariable.sOrganizationInfo.getDeptCode();
        Map<String, Object> map  = new HashMap<>();
        map.put("currentPage", 1);
        map.put("pageSize", 100);
        map.put("deptCode", deptCode);
        String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),strEntity);
        OrganizationService caseService = RetrofitClient.getAPIService(OrganizationService.class);
        caseService.getSearchEmployee(body).enqueue(new Callback<BaseBean<OrganizationEmployeeBean>>() {
            @Override
            public void onResponse(Call<BaseBean<OrganizationEmployeeBean>> call, Response<BaseBean<OrganizationEmployeeBean>> bean) {
                if (bean != null && bean.body().data != null) {
                    if (bean.body().data.getRecords() != null && bean.body().data.getRecords().size() != 0) {
                        mSubOrganizationView.showPersonList(bean.body().data);
                    } else {
                        mSubOrganizationView.showEmptyView();
                    }
                } else {
                    mSubOrganizationView.showEmptyView();
                }
            }

            @Override
            public void onFailure(Call<BaseBean<OrganizationEmployeeBean>> call, Throwable t) {

            }
        });
    }

}
