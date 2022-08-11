package com.gdu.mqttchat.chat.presenter;

import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.HostUrl;
import com.gdu.model.cases.IssueBean;
import com.gdu.mqttchat.chat.bean.CaseToastBean;
import com.gdu.mqttchat.chat.view.CaseChatDetailBean;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ChatService {

    //上传图片和视频
    @Multipart
    @POST(HostUrl.UPLOAD_CHAT_FILE)
    Observable<BaseBean<String>> uploadChatFile(@Part MultipartBody.Part body);

    /**
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GET(HostUrl.GET_CHAT_HISTORY)
    Call<BaseBean<Object>> getChatHistory(@Query("caseId") String  caseId, @Query("currentPage") String  page, @Query("pageSize") String  pageSize);

    /**
     * 获取某个人所有聊天群
     * @return
     */
    @GET(HostUrl.GET_CHAT_HIS_LIST)
    Call<MessageBean> getChatHisList();

    /**
     * 案情详情
     * @param jsonBody
     * @return
     */
    @POST(HostUrl.GET_CASE_DETAIL)
    @Headers({"Content-type:application/json;charset=UTF-8"})
    Call<BaseBean<IssueBean>> getCaseDetail(@Body RequestBody jsonBody);

    /**
     * 案情详情(新)
     * @param params
     * @return
     */
    @GET(HostUrl.GET_CASE_DETAIL_NEW)
    Observable<BaseBean<String>> getCaseDetailNew(@QueryMap Map<String, String> params);

    /**
     * 获取案件通知列表
     * @return
     */
    @GET(HostUrl.GET_MY_CASE)
    Observable<CaseToastBean> getMyCase();

    /**
     * 修改案件通知为已读
     * @return
     */
    @POST(HostUrl.READ_CASE)
    Observable<BaseBean<String>> readCase(@Body RequestBody jsonBody);

    /**
     * 聊天处理信息详情
     * @return
     */
    @GET(HostUrl.GET_ALARM_HANDLE_RECORD)
    Observable<CaseChatDetailBean> getAlarmHandleRecord(@QueryMap HashMap<String, Integer> params);

    /**
     * 获取聊天列表顶部预计详情
     * @return
     */
    @GET(HostUrl.GET_ALARM_BY_ALARM_ID)
    Observable<CaseChatDetailBean> getAlarmByAlarmId(@QueryMap HashMap<String, Integer> params);

}
