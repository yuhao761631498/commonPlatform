package com.gdu.command.ui.organization;


import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.HostUrl;
import com.gdu.model.cases.MyCaseInfo;
import com.gdu.model.organization.OrganizationEmployeeBean;
import com.gdu.model.organization.OrganizationInfo;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface OrganizationService {


    /**
     * 分派案件列表
     *
     * @return
     */

    //接口有参数时 要写上该注解
    @POST(HostUrl.GET_DESIGNATE_CASE_LIST)
    @Headers({"Content-type:application/json;charset=UTF-8"})
    Observable<BaseBean<MyCaseInfo>> getData(@Body RequestBody jsonBody);


    /**
     *查询组织树
     * @return
     */
    @GET(HostUrl.GET_DEPT_TREE)
    Call<BaseBean<List<OrganizationInfo>>> getDeptTree();


    //查询人员分页信息
    @POST(HostUrl.GET_EMPLOYEE_TREE)
    @Headers({"Content-type:application/json;charset=UTF-8"})
    Call<BaseBean<Object>> getEmployeeTree(@Body RequestBody jsonBody);

    /**
     * 人员信息搜索
     */
    @POST(HostUrl.GET_EMPLOYEE_TREE)
    @Headers({"Content-type:application/json;charset=UTF-8"})
    Call<BaseBean<OrganizationEmployeeBean>> getSearchEmployee(@Body RequestBody jsonBody);

}
