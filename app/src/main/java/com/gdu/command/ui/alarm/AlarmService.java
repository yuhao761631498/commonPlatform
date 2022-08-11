package com.gdu.command.ui.alarm;


import com.gdu.baselibrary.network.AlarmTypeStatictisBean;
import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.HostUrl;
import com.gdu.command.ui.alarm.detail.AlarmDetailBean;
import com.gdu.command.ui.alarm.dialog.AlarmDispatchedBean;
import com.gdu.command.views.drop.AlarmDeviceBean;
import com.gdu.model.cases.MyCaseInfo;
import com.gdu.model.organization.OrganizationEmployeeBean;
import com.gdu.model.organization.OrganizationInfo;

import java.util.HashMap;
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
import retrofit2.http.QueryMap;


public interface AlarmService {

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
     * 查询组织树
     * @return
     */
    @GET(HostUrl.GET_DEPT_TREE)
    Observable<BaseBean<List<OrganizationInfo>>> getDeptTree();

    /**
     * 人员信息搜索
     */
    @POST(HostUrl.GET_EMPLOYEE_TREE)
    @Headers({"Content-type:application/json;charset=UTF-8"})
    Call<BaseBean<OrganizationEmployeeBean>> getSearchEmployee(@Body RequestBody jsonBody);

    /**
     * 更新告警状态
     * @return
     */
    @FormUrlEncoded
    @POST(HostUrl.UPDATE_ALARM)
    Observable<BaseBean<Boolean>> updateAlarm(@FieldMap Map<String, String> params);

    /**
     * 获取预警列表
     * @return
     */
    @FormUrlEncoded
    @POST(HostUrl.GET_ALARM_LIST)
    Observable<AlarmListBean> getAlarmList(@FieldMap Map<String, String> params);

    /**
     * 获取告警中心(新-支持筛选)
     * @return
     */
    @GET(HostUrl.GET_ALARM_LIST_NEW)
    Observable<AlarmListBean> getAlarmListNew(@QueryMap Map<String, Object> params);

    /**
     * 获取设备列表
     * @return
     */
    @GET(HostUrl.GET_DEVICE_LIST)
    Observable<AlarmDeviceBean> getDeviceList(@QueryMap Map<String, String> params);

    /**
     * 获取某一设备预警列表
     * @return
     */
    @GET(HostUrl.GET_ALARM_DEVICE_INFO)
    Observable<AlarmListBean> getAlarmDeviceInfo(@QueryMap Map<String, String> params);

    /**
     * 关注/取消关注
     * @return
     */
    @POST(HostUrl.ATTENTION)
    Observable<BaseBean<String>> attention(@Body RequestBody body);

    /**
     * app端预警类型统计
     * @return
     */
    @GET(HostUrl.APP_ALARM_TYPE_STATISTICS)
    Observable<AlarmTypeStatictisBean> appAlarmTypeStatistics();

    /**
     * 是否有app菜单权限
     * @return
     */
    @GET(HostUrl.IS_APP_MENU)
    Observable<BaseBean<Boolean>> isAppMenu();

    /**
     * 查询分派和处理信息(获取案件详情信息)
     * @return
     */
    @GET(HostUrl.GET_CASE_DETAIL_INFO)
    Observable<AlarmDetailBean> getCaseDetailInfo(@QueryMap HashMap<String, Integer> params);

    // <<<-------------------------------------------------------------------------------

    /**
     * 查询分派记录
     * @return
     */
    @GET(HostUrl.QUERY_ASSIGNMENT)
    Observable<BaseBean<String>> queryAssignment(@QueryMap HashMap<String, Integer> params);

    /**
     * 分派预警
     * @return
     */
    @POST(HostUrl.ASSIGNMENT)
    Observable<BaseBean<String>> assignment(@Body RequestBody body);

    /**
     * 预警处理
     * @return
     */
    @POST(HostUrl.ALARM_HANDLE)
    @Headers({"Content-type:application/json;charset=UTF-8"})
    Observable<BaseBean<String>> alarmHandle(@Body RequestBody body);

    /**
     * 处理信息列表
     * @return
     */
    @GET(HostUrl.QUERY_ALARM_HANDLE_RECORD)
    Observable<BaseBean<String>> queryAlarmHandleRecord(@QueryMap HashMap<String, Integer> params);

    /**
     * 补充处理
     * @return
     */
    @POST(HostUrl.SUPPLEMENT_HANDLE)
    Observable<BaseBean<String>> supplementHandle(@Body RequestBody body);

    /**
     * 预警接警
     * @return
     */
    @FormUrlEncoded
    @POST(HostUrl.ANSWER_POLICE)
    Observable<BaseBean<String>> answerPolice(@FieldMap HashMap<String, Integer> params);

    /**
     * 查询告警被分派列表
     * @return
     */
    @GET(HostUrl.QUERY_ASSIGNMENT_LIST)
    Observable<AlarmDispatchedBean> queryAssignmentList(@QueryMap HashMap<String, Integer> params);

    /**
     * 聊天处理信息详情
     * @return
     */
    @GET(HostUrl.GET_ALARM_HANDLE_RECORD)
    Observable<BaseBean<String>> getAlarmHandleRecord(@QueryMap HashMap<String, Integer> params);

}
