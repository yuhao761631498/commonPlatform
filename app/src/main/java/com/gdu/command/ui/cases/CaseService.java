package com.gdu.command.ui.cases;


import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.HostUrl;
import com.gdu.command.ui.home.CaseInfoBean;
import com.gdu.command.ui.home.RoleInfoBean;
import com.gdu.model.cases.CommentInfo;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.cases.MyCaseInfo;
import com.gdu.model.config.UrlConfig;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface CaseService {


    /**
     * 获取分派案件列表
     * @param jsonBody
     * @return
     */
    @POST(HostUrl.GET_DESIGNATE_CASE_LIST)
    @Headers({"Content-type:application/json;charset=UTF-8"})
    Call<BaseBean<MyCaseInfo>> getData(@Body RequestBody jsonBody);

    /**
     * 接警
     * @param jsonBody
     * @return
     */
    @POST(HostUrl.DESIGNATE_STATUS_UPDATE)
    @Headers({"Content-type:application/json;charset=UTF-8"})
    Call<BaseBean<Object>> updateCaseStatus(@Body RequestBody jsonBody);

    /**
     * 案情处置
     * @param jsonBody
     * @return
     */
    @POST(HostUrl.DESIGNATE_CASE)
    @Headers({"Content-type:application/json;charset=UTF-8"})
    Call<BaseBean<Object>> designateCase(@Body RequestBody jsonBody);

    /**
     * 案情结案
     * @param jsonBody
     * @return
     */
    @POST(HostUrl.FINISH_CASE)
    @Headers({"Content-type:application/json;charset=UTF-8"})
    Call<BaseBean<Object>> finishCase(@Body RequestBody jsonBody);

    /**
     * 案件待批示列表
     * @return
     */
    @POST(HostUrl.GET_WAIT_COMMENT_CASE_LIST)
    @Headers({"Content-type:application/json;charset=UTF-8"})
    Call<BaseBean<List<IssueBean>>> getWaitCommentCaseList();

    /**
     * 案件批示
     * @param jsonBody
     * @return
     */
    @POST(HostUrl.COMMENT_CASE)
    @Headers({"Content-type:application/json;charset=UTF-8"})
    Call<BaseBean<Object>> commentCase(@Body RequestBody jsonBody);

    /**
     * 查询案件批示
     * @param jsonBody
     * @return
     */
    @POST(HostUrl.SELECT_CASE_COMMENTS)
    @Headers({"Content-type:application/json;charset=UTF-8"})
    Call<BaseBean<List<CommentInfo>>> selectCaseCommentS(@Body RequestBody jsonBody);


    /**
     * 案情处置记录
     * @param jsonBody
     * @return
     */
    @POST(HostUrl.SELECT_CASE_RECORD)
    @Headers({"Content-type:application/json;charset=UTF-8"})
    Call<BaseBean<Map<String, List<Map<String, Object>>>>> selectCaseRecord(@Body RequestBody jsonBody);

    /**
     *查看权限列表
     * @return
     */
    @GET(HostUrl.QUERY_USER_ROLE_TREE)
    Call<RoleInfoBean> getUserRoleTree();

    /**
     *案件列表统计
     * @return
     */
    @GET(HostUrl.SELECT_COUNT_MY_CASE)
    Call<BaseBean<Map<String, Double>>> selectCountCaseAction();

    /**
     * 获取首页案件列表数据
     * @return
     */
    @POST(UrlConfig.getCaseInfo)
    Call<CaseInfoBean> getCaseCenter(@Body RequestBody jsonBody);

    /**
     * 获取案件来源和案件类型
     * @return
     */
    @GET(UrlConfig.getTypeOrSource)
    Observable<TypeCodeBean> getTypeOrSource(@Query("typeCode") String typeCode);

    /**
     * 新增案件
     * @return
     */
    @POST(UrlConfig.addCase)
    Observable<AddCaseResultBean> addCase(@Body RequestBody jsonBody);

    /**
     * 二次定位更新案件地址
     * @return
     */
    @POST(UrlConfig.updateLocation)
    Observable<BaseBean<String>> updateLocation(@Body RequestBody jsonBody);

    /**
     * 手动告警
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConfig.uploadAlarm)
    Observable<BaseBean<String>> uploadAlarm(@FieldMap Map<String ,String> map);


    @GET(UrlConfig.alarmArea)
    Observable<AlarmAreaBean> getAlarmArea();
}
