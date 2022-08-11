package com.gdu.command.ui.my;


import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.HostUrl;
import com.gdu.command.ui.setting.SelectDepartmentListEntity;
import com.gdu.model.organization.OrganizationInfo;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 个人接口
 */
public interface PersonService {


    /**
     *查询组织树
     * @return
     */
    @GET(HostUrl.GET_DEPT_TREE)
    Call<BaseBean<List<OrganizationInfo>>> getDeptTree();

    //案情批示
    @POST(HostUrl.UPDATE_PWD)
    @Headers({"Content-type:text/plain;charset=UTF-8"})
    Call<BaseBean<Object>> updatePwd(@Query("confirmPassword") String  confirmPassword, @Query("newPassword") String  newPassword, @Query("oldPassword") String  oldPassword);

    /**
     * 查询组织机构菜单
     */
    @GET(HostUrl.GET_DEPT_TREE)
    Observable<SelectDepartmentListEntity> getDepartmentTree();
}
